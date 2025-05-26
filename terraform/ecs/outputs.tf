output "ecs_cluster_name" {
  value = aws_ecs_cluster.digital_account_cluster.name
}

output "ecs_cluster_arn" {
  value = aws_ecs_cluster.digital_account_cluster.arn
}

output "private_subnet_ids" {
  value = data.aws_subnets.private_subnets.ids
}

output "ecs_security_group_id" {
  value = aws_security_group.ecs_security_group.id
}

output "ecs_task_execution_role_arn" {
  value = aws_iam_role.ecs_task_execution_role.arn
}