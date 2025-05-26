output "secret_id" {
  value = aws_secretsmanager_secret.hash_salt.id
  description = "O identificador do salt gerado"
}