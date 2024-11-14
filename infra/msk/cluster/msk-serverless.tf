# resource "aws_msk_serverless_cluster" "msk" {
#   cluster_name = "${var.project_name}-msk-cluster"

#   vpc_config {
#     subnet_ids         = module.vpc.private_subnets
#     security_group_ids = [aws_security_group.msk.id]
#   }

#   client_authentication {
#     sasl {
#       iam {
#         enabled = true
#       }
#     }
#   }
# }

# resource "aws_security_group" "msk" {
#   name = "${var.project_name}-sg-cluster"
#   vpc_id = module.vpc.vpc_id
  
#   ingress {
#     protocol = "tcp"
#     self = true
#     cidr_blocks = ["0.0.0.0/0"]
#     from_port = "9098"  // IAM access control port
#     to_port = "9098"
#   }

#   egress {
#     protocol = "-1"
#     cidr_blocks = ["0.0.0.0/0"]
#     from_port = "0"
#     to_port = "0"
#   }

#   lifecycle {
#     ignore_changes = [ 
#       ingress,
#       egress
#      ]
#   }
# }