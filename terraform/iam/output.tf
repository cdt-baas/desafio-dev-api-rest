output "ecr_group" {
  value = aws_iam_group.ecr_group.name
  description = "Grupo criado para o ECR"
}

output "github_user_name" {
  value = aws_iam_user.ecr_user.name
  description = "Nome do usuario criado"
}

output "github_user_access_id" {
  value = aws_iam_access_key.github_user_access_key.id
  description = "ID da credencial do usuario github"
}

output "github_user_access_key" {
  value = aws_iam_access_key.github_user_access_key.secret
  description = "Secret do usuario github"
  sensitive = true
}

