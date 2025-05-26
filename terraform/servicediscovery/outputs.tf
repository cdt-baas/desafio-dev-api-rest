output "holder_service_discovery_arn" {
  value = aws_service_discovery_service.holder_dns.arn
}

output "account_service_discovery_arn" {
  value = aws_service_discovery_service.account_dns.arn
}