module "ecs" {
  source  = "terraform-aws-modules/ecs/aws"
  version = "5.11.4"

  cluster_name = "${var.project_name}-cluster"

  fargate_capacity_providers = {
    FARGATE = {
      default_capacity_provider_strategy = {
        weight : 1
        base : 0
      }
    }
  }

  services = {
    product = {
      cpu        = 1024
      memory     = 4096
      subnet_ids = module.vpc.private_subnets

      load_balancer = {
        service = {
          target_group_arn = module.alb.target_groups["${var.project_name}-tg-product"].arn
          container_name   = "app"
          container_port   = 8080
        }
      }

      security_group_rules = {
        alb_ingress = {
          type                     = "ingress"
          from_port                = 8080
          to_port                  = 8080
          protocol                 = "tcp"
          description              = "Service port"
          source_security_group_id = module.alb.security_group_id  # security group id of alb module
        }
        egress_all = {
          type        = "egress"
          from_port   = 0
          to_port     = 0
          protocol    = "-1"
          cidr_blocks = ["0.0.0.0/0"]
        }
      }

      container_definitions = {
        app = {
          cpu                = 512
          memory             = 1024
          essential          = true
          image              = "658986583341.dkr.ecr.ap-northeast-2.amazonaws.com/mesh/product"
          memory_reservation = 128

          health_check = {
            command = ["CMD-SHELL", "curl -f http://localhost:8080/healthcheck || exit 1"]
          }

          port_mappings = [
            {
              name          = "app"
              containerPort = 8080
              hostPort      = 8080
              protocol      = "tcp"
            }
          ]

          log_configuration = {
            log_driver = "awslogs"
            options = {
              "awslogs-group"         = "/ecs/${var.project_name}-product"
              "awslogs-region"        = "ap-northeast-2"
              "awslogs-stream-prefix" = "ecs"
            }
          }

          enable_cloudwatch_logging = true
        }
      }
    }
  }
}

resource "aws_cloudwatch_log_group" "ecs_product" {
  name              = "/ecs/${var.project_name}-product"
  retention_in_days = 7
}