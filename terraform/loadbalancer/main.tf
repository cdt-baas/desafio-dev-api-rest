data "aws_vpc" "digital_account_vpc" {
    id = var.vpc_id
}

data "aws_subnets" "public_subnets" {
    filter {
      name = "vpc-id"
      values = [var.vpc_id]
    }
    filter {
      name = "tag:Name"
      values = ["*public*"]
    }
}

data "aws_ecs_cluster" "digital_account_cluster" {
    cluster_name = "digital-account-cluster"
}

data "aws_ecs_service" "holder_service" {
    cluster_arn  = data.aws_ecs_cluster.digital_account_cluster.arn
    service_name = "holder-service"
}

data "aws_ecs_service" "account_service" {
    cluster_arn  = data.aws_ecs_cluster.digital_account_cluster.arn
    service_name = "account-service"
}

resource "aws_lb_target_group" "holder_target_group" {
    name = "holder-target-group"
    port = 8081
    protocol = "HTTP"
    vpc_id = data.aws_vpc.digital_account_vpc.id
    target_type = "ip"

    health_check {
        path = "/actuator/health"
        port = 8081
        timeout = 30
        interval = 50
        healthy_threshold = 2
        unhealthy_threshold = 10
    }
}

resource "aws_lb_target_group" "account_target_group" {
  name = "account-target-group"
  port = 8082
  protocol = "HTTP"
  vpc_id = data.aws_vpc.digital_account_vpc.id
  target_type = "ip"

  health_check {
      path = "/actuator/health"
      port = 8082
      timeout = 30
      interval = 50
      healthy_threshold = 2
      unhealthy_threshold = 10
  }
}

resource "aws_security_group" "load_balancer_security_group" {
    name = "load-balancer-security-group"
    vpc_id = data.aws_vpc.digital_account_vpc.id

    ingress {
        from_port = 80
        to_port = 80
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    ingress {
      from_port = 443
      to_port = 443
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
    }

    egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["0.0.0.0/0"]
    }
}

resource "aws_lb" "digital_account_load_balancer" {
    name = "digital-account-load-balancer"
    security_groups = [aws_security_group.load_balancer_security_group.id]
    subnets = data.aws_subnets.public_subnets.ids
    internal = false


    tags = {
      Name = "digital-account-load-balancer"
      ManagedBy = "terraform"
    }
}

resource "aws_lb_listener" "load_balancer_listener" {
  load_balancer_arn = aws_lb.digital_account_load_balancer.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "fixed-response"
    fixed_response {
      content_type = "text/plain"
      message_body = "Not Found"
      status_code  = "404"
    }
  }
}

resource "aws_lb_listener_rule" "holder_path" {
  listener_arn = aws_lb_listener.load_balancer_listener.arn
  priority     = 10

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.holder_target_group.arn
  }

  condition {
    path_pattern {
      values = ["/holder*", "/api/v1/holder*"]
    }
  }
}

resource "aws_lb_listener_rule" "account_and_movement" {
  listener_arn = aws_lb_listener.load_balancer_listener.arn
  priority     = 20

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.account_target_group.arn
  }

  condition {
    path_pattern {
      values = ["/account*", "/movement*", "/api/v1/account*", "/api/v1/movement*"]
    }
  }
}


