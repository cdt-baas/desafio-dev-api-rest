provider "aws" {
    region = var.region
}

resource "aws_secretsmanager_secret" "hash_salt" {
  name = "dev/digital-account/salt"
  description = "Salt para utilização em criptografia"

  tags = {
    Environment = "dev"
    Name = "digital_account_salt"
  }
}

resource "aws_secretsmanager_secret_version" "hash_salt_version" {
    secret_id = aws_secretsmanager_secret.hash_salt.id

    secret_string = var.salt

    lifecycle {
      ignore_changes = [ secret_string ]
    }
  
}