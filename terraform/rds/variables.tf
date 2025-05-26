variable "region" {
    default = "us-east-2"
    description = "Regiao rodando as aplicações"
}

variable "db_password" {
    description = "Senha para usuario RDS"
    sensitive = true
}