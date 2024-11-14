module "msk" {
  source = "terraform-aws-modules/msk-kafka-cluster/aws"

  name                   = "${var.project_name}-cluster"
  kafka_version          = "3.6.0"
  number_of_broker_nodes = "2"
  enhanced_monitoring    = "PER_TOPIC_PER_PARTITION"

  broker_node_instance_type   = "kafka.t3.small"
  broker_node_security_groups = [aws_security_group.msk.id]
  broker_node_client_subnets  = module.vpc.private_subnets

  broker_node_storage_info = {
    ebs_storage_info = {
      volume_size = "16"
      volume_type = "gp2"
    }
  }

  # encryption_in_transit_client_broker = "TLS"
  # encryption_in_transit_in_cluster    = true

  client_authentication = {
    sasl = {
      scram = true
    }
  }

  create_scram_secret_association = true
  scram_secret_association_secret_arn_list = [
    aws_secretsmanager_secret.scram.arn,
  ]

  configuration_name        = "provisioned-cluster-config"
  configuration_description = "Configuration for provisioned MSK cluster"
  configuration_server_properties = {
    "auto.create.topics.enable" = true
    "delete.topic.enable"       = true
  }

  jmx_exporter_enabled    = true
  node_exporter_enabled   = true
  cloudwatch_logs_enabled = true
  # s3_logs_enabled         = false                      # 필요에 따라 활성화
  # s3_logs_bucket          = "aws-msk-provisioned-logs" # 실제 S3 버킷으로 변경
  # s3_logs_prefix          = "msk-provisioned-cluster"
}

resource "aws_security_group" "msk" {
  name   = "${var.project_name}-sg-cluster"
  vpc_id = module.vpc.vpc_id

  ingress {
    protocol    = "tcp"
    self        = true
    cidr_blocks = ["0.0.0.0/0"]
    from_port   = "9096" // SASL_SSL
    to_port     = "9096"
  }

  egress {
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    from_port   = "0"
    to_port     = "0"
  }

  lifecycle {
    ignore_changes = [
      ingress,
      egress
    ]
  }
}
