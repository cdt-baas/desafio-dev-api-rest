output "account_ecr_url" {
  description = "Url do ECR para Account"
  value = aws_ecr_repository.account.repository_url
}

output "account_ecr_id" {
  description = "Id do ECR para Account"
  value = aws_ecr_repository.account.id
}

output "holder_ecr_url" {
  description = "Url do ECR para Holder"
  value = aws_ecr_repository.holder.repository_url
}

output "holder_ecr_id" {
  description = "Id do ECR para Holder"
  value = aws_ecr_repository.holder.id
}