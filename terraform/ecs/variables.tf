variable "region" {
  default = "us-east-2"
  type = string
  description = "Regiao da AWS"
}

variable "rds_security_group_id" {
  type = string
  description = "rds_security_group_id"
}

variable "vpc_id" {
  type = string
  description = "Id da VPC criada"
}

variable "db_instance_identifier" {
  type = string
  description = "Identificador da instancia do RDS"
}

variable "db_instance_username" {
  type = string
  description = "Username da instancia do RDS"
  sensitive = true
}

variable "db_instance_password" {
  type = string
  description = "Senha da instancia do RDS"
  sensitive = true
}

variable "holder_service_salt_name" {
  type = string
  description = "Nome do salt no secrets manager usado no holder service"
}

variable "ecr_holder_url" {
  type = string
  description = "URL do ECR do holder service"
}

variable "ecr_account_url" {
  type = string
  description = "URL do ECR do account service"
}

variable "holder_url" {
  type = string
  description = "URL do holder service"
}

variable "holder_target_group_arn" {
  type        = string
  description = "ARN do target group do holder service"
}

variable "account_target_group_arn" {
  type = string
  description = "ARN do target group do account service"
}

variable "holder_service_discovery_arn" {
  type = string
  description = "ARN do service discovery do holder service"
}

variable "account_service_discovery_arn" {
  type = string
  description = "ARN do service discovery do account service"
}