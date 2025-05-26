output "loadbalancer_url" {
  value = aws_lb.digital_account_load_balancer.dns_name
}

output "loadbalancer_arn" {
  value = aws_lb.digital_account_load_balancer.arn
}

output "loadbalancer_id" {
  value = aws_lb.digital_account_load_balancer.id
}

output "holder_target_group_arn" {
  value = aws_lb_target_group.holder_target_group.arn
}

output "account_target_group_arn" {
  value = aws_lb_target_group.account_target_group.arn
}