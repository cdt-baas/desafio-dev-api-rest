provider "aws" {
    region = var.region
}

data "aws_availability_zones" "available" {}

module "rds_vpc" {
  source = "terraform-aws-modules/vpc/aws"
  version = "5.21.0"

  name = "digital_account_vpc"
  cidr = "10.0.0.0/16"
  azs = data.aws_availability_zones.available.names
  public_subnets = ["10.0.5.0/24","10.0.6.0/24"]
  private_subnets = ["10.0.0.0/24","10.0.1.0/24", "10.0.2.0/24","10.0.3.0/24"]
  enable_dns_hostnames = true
  enable_dns_support = true
}

resource "aws_db_subnet_group" "digital_account_rds_subnet" {
  name = "digital_account_subnet"
  subnet_ids = module.rds_vpc.private_subnets

  tags = {
    Name = "digital_account_rds_subnet"
  }
}

resource "aws_security_group" "rds_security_group" {
  name = "digital_account_rds"
  vpc_id = module.rds_vpc.vpc_id

  tags = {
    Name = "digital_account_rds"
  }

  ingress {
    from_port = 5432
    to_port = 5432
    protocol = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port = 5432
    to_port = 5432
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_parameter_group" "digital_account" {
  name = "digital-account"
  family = "postgres16"

  parameter {
    name = "log_connections"
    value = 1
  }
}

resource "aws_db_instance" "digital_account_db_instance" {
  identifier = "digital-account"
  instance_class = "db.t3.micro"
  allocated_storage = 5
  engine = "postgres"
  engine_version = "16.9"
  username = "digital_account"
  password = var.db_password
  db_subnet_group_name = aws_db_subnet_group.digital_account_rds_subnet.name
  vpc_security_group_ids = [aws_security_group.rds_security_group.id]
  parameter_group_name = aws_db_parameter_group.digital_account.name
  publicly_accessible = true
  skip_final_snapshot = true
}