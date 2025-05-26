resource "aws_ecr_repository" "holder" {
    name = "holder-ecr"
    image_tag_mutability = "MUTABLE"

    image_scanning_configuration {
      scan_on_push = false
    }
}

resource "aws_ecr_repository" "account" {
    name = "account-ecr"
    image_tag_mutability = "MUTABLE"

    image_scanning_configuration {
      scan_on_push = false
    }
}

