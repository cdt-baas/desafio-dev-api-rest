output "rds_host" {
  description = "Endereço do host"
  value = aws_db_instance.digital_account_db_instance.address
}

output "rds_port" {
  description = "Porta da instancia do RDS"
  value = aws_db_instance.digital_account_db_instance.port
}

output "rds_username" {
  description = "Username Root da Instancia do RDS"
  value = aws_db_instance.digital_account_db_instance.username
}