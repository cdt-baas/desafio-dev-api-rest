resource "aws_service_discovery_private_dns_namespace" "digital_account" {
  name = "digital-account"
  vpc  = var.vpc_id
}

resource "aws_service_discovery_service" "holder_dns" {
  name = "holder-service"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.digital_account.id
    dns_records {
      ttl  = 10
      type = "A"
    }
    routing_policy = "MULTIVALUE"
  }

}

resource "aws_service_discovery_service" "account_dns" {
  name = "account-service"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.digital_account.id
    dns_records {
      ttl  = 10
      type = "A"
    }
    routing_policy = "MULTIVALUE"
  }

}