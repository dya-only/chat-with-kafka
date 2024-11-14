resource "aws_secretsmanager_secret" "scram" {
  name        = "AmazonMSK_${var.project_name}-key"
  description = "SCRAM secret for MSK cluster user1"
  recovery_window_in_days = "0"
  kms_key_id = aws_kms_key.scram.id
}

resource "aws_secretsmanager_secret_version" "scram" {
  secret_id     = aws_secretsmanager_secret.scram.id
  secret_string = jsonencode({
    username = "dya-only"
    password = "password"
  })
}

resource "aws_kms_key" "scram" {
  description             = "An symmetric encryption KMS key for msk sasl/scram"
  enable_key_rotation     = true
  deletion_window_in_days = 7
}

resource "aws_kms_key_policy" "scram" {
  key_id = aws_kms_key.scram.id
  policy = jsonencode({
    Version = "2012-10-17"
    Id      = "msk-key"
    Statement = [
      {
        Sid    = "Enable IAM User Permissions"
        Effect = "Allow"
        Principal = {
          AWS = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:root"
        },
        Action   = "kms:*"
        Resource = "*"
      }
    ]
  })
}