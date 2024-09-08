#!/bin/bash

if [ "$#" -ne 7 ]; then
    echo "required args AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, BUCKET_NAME, PATH_TESTS, PUBLISHER_SETUP, GITHUB_REF_NAME, GITHUB_REPOSITORY"
    exit 9
fi

export AWS_ACCESS_KEY_ID=$1
export AWS_SECRET_ACCESS_KEY=$2
export BUCKET_NAME=$3
export PATH_TESTS=$4
export PUBLISHER_SETUP=$5
export GITHUB_REF_NAME=$6
export GITHUB_REPOSITORY=$7

exec java -jar publish-test.jar
