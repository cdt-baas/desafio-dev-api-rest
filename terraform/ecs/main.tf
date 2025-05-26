data "aws_vpc" "digital_account_vpc" {
    id = var.vpc_id
}

data "aws_subnets" "private_subnets" {
    filter {
      name = "vpc-id"
      values = [var.vpc_id]
    }
    filter {
      name = "tag:Name"
      values = ["*private*"]
    }
}

data "aws_db_instance" "digital_account_db_instance" {
    db_instance_identifier = var.db_instance_identifier
}

data "aws_secretsmanager_secret" "aws_salt_secret" {
  name = var.holder_service_salt_name
}

resource "aws_ecs_cluster" "digital_account_cluster" {
    name = "digital-account-cluster"

    setting {
      name = "containerInsights"
      value = "enabled"
    }

    tags = {
      Environment = "staging"
      ManagedBy = "terraform"
    }
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecs-task-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    ManagedBy = "terraform"
  }
}

resource "aws_iam_role" "ecs_task_role" {
    name = "ecs-task-role"
    assume_role_policy = jsonencode({
      Version = "2012-10-17"
      Statement = [
        {
          Action = "sts:AssumeRole"
          Effect = "Allow"
          Principal = {
            Service = "ecs-tasks.amazonaws.com"
          }
        }
      ]
    })

    tags = {
      ManagedBy = "terraform"
    }
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_policy" {
    role = aws_iam_role.ecs_task_execution_role.name
    policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy_attachment" "secrets_manager_access_policy" {
    role = aws_iam_role.ecs_task_role.name
    policy_arn = "arn:aws:iam::aws:policy/SecretsManagerReadWrite"
}

resource "aws_security_group" "ecs_security_group" {
    name = "digital-account-services-sg"
    description = "Grupo de seguranca para os servicos da conta digital"
    vpc_id = data.aws_vpc.digital_account_vpc.id

    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    ingress {
        from_port = 8081
        to_port = 8082
        protocol = "tcp"
        cidr_blocks = [data.aws_vpc.digital_account_vpc.cidr_block]
    }

    tags = {
      ManagedBy = "terraform"
    }

}

resource "aws_ecs_task_definition" "holder_task_definition" {
    family = "holder-task-definition"
    network_mode = "awsvpc"
    requires_compatibilities = ["FARGATE"]
    cpu = "512"
    memory = "1024"
    execution_role_arn = aws_iam_role.ecs_task_execution_role.arn

    task_role_arn = aws_iam_role.ecs_task_role.arn

    container_definitions = jsonencode([
      {
        name = "holder-container"
        image = var.ecr_holder_url
        cpu = 512
        memory = 1024
        essential = true
        portMappings = [
          {
            containerPort = 8081
            hostPort = 8081
          }
        ]
        logConfiguration = {
          logDriver = "awslogs"
          options = {
            awslogs-group         = "/ecs/holder"
            awslogs-region        = var.region
            awslogs-stream-prefix = "ecs"
          }
        }
        environment = [
          {
            name = "DB_HOST"
            value = data.aws_db_instance.digital_account_db_instance.address
          },
          {
            name = "DB_PORT"
            value = tostring(data.aws_db_instance.digital_account_db_instance.port)
          },
          {
            name = "DB_USER"
            value = var.db_instance_username
          },
          {
            name = "DB_PASSWORD"
            value = var.db_instance_password
          },
          {
            name = "AWS_SALT_SECRET_ID"
            value = data.aws_secretsmanager_secret.aws_salt_secret.arn
          }
        ]
      }
    ])
}

resource "aws_ecs_task_definition" "account_task_definition" {
    family = "account-task-definition"
    network_mode = "awsvpc"
    requires_compatibilities = ["FARGATE"]
    cpu = "512"
    memory = "1024"
    execution_role_arn = aws_iam_role.ecs_task_execution_role.arn

    task_role_arn = aws_iam_role.ecs_task_role.arn

    container_definitions = jsonencode([
      {
        name = "account-container"
        image = var.ecr_account_url
        cpu = 512
        memory = 1024
        essential = true
        portMappings = [
          {
            containerPort = 8082
            hostPort = 8082
          }
        ]
        logConfiguration = {
          logDriver = "awslogs"
          options = {
            awslogs-group         = "/ecs/account"
            awslogs-region        = var.region
            awslogs-stream-prefix = "ecs"
          }
        }
        environment = [
          {
            name = "DB_HOST"
            value = data.aws_db_instance.digital_account_db_instance.address
          },
          {
            name = "DB_PORT"
            value = tostring(data.aws_db_instance.digital_account_db_instance.port)
          },
          {
            name = "DB_USER"
            value = var.db_instance_username
          },
          {
            name = "DB_PASSWORD"
            value = var.db_instance_password
          },
          {
            name = "HOLDER_URL"
            value = var.holder_url
          }
        ]
      }
    ])
}



resource "aws_ecs_service" "holder_service" {
    name = "holder-service"
    cluster = aws_ecs_cluster.digital_account_cluster.id
    task_definition = aws_ecs_task_definition.holder_task_definition.arn
    desired_count = 1
    launch_type = "FARGATE"

    network_configuration {
      security_groups = [aws_security_group.ecs_security_group.id]
      subnets = data.aws_subnets.private_subnets.ids
    }

    load_balancer {
      container_name = "holder-container"
      container_port = 8081
      target_group_arn = var.holder_target_group_arn
    }

    service_registries {
      registry_arn = var.holder_service_discovery_arn
    }

}

resource "aws_ecs_service" "account_service" {
    name = "account-service"
    cluster = aws_ecs_cluster.digital_account_cluster.id
    task_definition = aws_ecs_task_definition.account_task_definition.arn
    desired_count = 1
    launch_type = "FARGATE"

    network_configuration {
      security_groups = [aws_security_group.ecs_security_group.id]
      subnets = data.aws_subnets.private_subnets.ids
    }

    load_balancer {
      container_name = "account-container"
      container_port = 8082
      target_group_arn = var.account_target_group_arn
    }

    service_registries {
      registry_arn = var.account_service_discovery_arn
    }
}

resource "aws_cloudwatch_log_group" "holder_logs" {
  name              = "/ecs/holder"
  retention_in_days = 30  # Set your desired log retention period
}

resource "aws_cloudwatch_log_group" "account_logs" {
  name              = "/ecs/account"
  retention_in_days = 30  # Set your desired log retention period
}


