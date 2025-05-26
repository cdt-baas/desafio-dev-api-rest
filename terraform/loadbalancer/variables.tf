variable "region" {
  default = "us-east-2"
  type = string
  description = "Regiao da AWS"
}

variable "vpc_id" {
  type = string
  description = "Id da VPC criada"
}