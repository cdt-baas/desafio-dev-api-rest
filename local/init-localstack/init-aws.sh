#!/bin/bash

awslocal secretsmanager create-secret \
    --name "local/digital-account/salt" \
    --secret-string 'aosid193821asikodj0923' \
    --description "Secret to be used as hash for document number"

echo "Secret created successfully"