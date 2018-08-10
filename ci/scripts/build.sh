#!/bin/bash

set -eu

pushd source-code
    ./gradlew clean assemble
    cp build/libs/product-service-1.0.0.jar ../build-output
    cp manifest.yml ../build-output
popd
echo "build successful"
echo "deploying now..."