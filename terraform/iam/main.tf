resource "aws_iam_group" "ecr_group" {
  name = "ecr-group"
}

resource "aws_iam_policy" "ecs_ecr_custom_policy" {
  name        = "ECSECRGithubDeployPolicy"
  description = "Custom policy for ECS and ECR access for Github Actions"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "ecs:DescribeTaskDefinition",
          "ecs:UpdateService",
          "ecs:RegisterTaskDefinition",
          "ecs:DescribeServices",
          "ecs:ListTaskDefinitions",
          "iam:PassRole"
        ],
        Resource = "*"
      },
      {
        Effect = "Allow",
        Action = [
          "ecr:GetAuthorizationToken",
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetDownloadUrlForLayer",
          "ecr:GetRepositoryPolicy",
          "ecr:DescribeRepositories",
          "ecr:ListImages",
          "ecr:DescribeImages",
          "ecr:BatchGetImage",
          "ecr:InitiateLayerUpload",
          "ecr:UploadLayerPart",
          "ecr:CompleteLayerUpload",
          "ecr:PutImage"
        ],
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_group_policy_attachment" "policy_attachment" {
  for_each = toset(var.ecr_policy_arn)  

  group = aws_iam_group.ecr_group.name
  policy_arn = each.value
}

resource "aws_iam_group_policy_attachment" "attach_ecs_ecr_policy" {
  group = aws_iam_group.ecr_group.name
  policy_arn = aws_iam_policy.ecs_ecr_custom_policy.arn
}

resource "aws_iam_user" "ecr_user" {
  name = "github-ecr-user"
}

resource "aws_iam_user_group_membership" "ecr_group_membership" {
  user = aws_iam_user.ecr_user.name
  groups = [aws_iam_group.ecr_group.name]
}

resource "aws_iam_access_key" "github_user_access_key" {
  user = aws_iam_user.ecr_user.name
}