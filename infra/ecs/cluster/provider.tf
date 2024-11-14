variable "project_name" {
  default = "mesh"
}

variable "region" {
  default = "ap-northeast-2"
  # default = "ap-northeast-1"
}

provider "aws" {
  region = var.region

  default_tags {
    tags = {
      Project = var.project_name
    }
  }
}

data "aws_caller_identity" "caller" {}