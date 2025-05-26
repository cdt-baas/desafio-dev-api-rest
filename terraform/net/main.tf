data "aws_vpc" "digital_account_vpc" {
  id = var.vpc_id
}

data "aws_route_tables" "private_route_tables" {
  filter {
    name = "vpc-id"
    values = [var.vpc_id]
  }
  filter {
    name = "tag:Name"
    values = ["*private*"]
  }
}

data "aws_route_tables" "public_route_tables" {
  filter {
    name = "vpc-id"
    values = [var.vpc_id]
  }
  filter {
    name = "tag:Name"
    values = ["*public*"]
  }
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

resource "aws_eip" "aws_digital_account_eip" {
  domain = "vpc"
}

resource "aws_nat_gateway" "digital_account_nat_gateway" {
  allocation_id = aws_eip.aws_digital_account_eip.allocation_id
  subnet_id = data.aws_subnets.public_subnets.ids[0]
}

resource "aws_route" "nat_route" {
  for_each = toset(data.aws_route_tables.private_route_tables.ids)
  route_table_id = each.value
  destination_cidr_block = "0.0.0.0/0"
  nat_gateway_id = aws_nat_gateway.digital_account_nat_gateway.id
}
