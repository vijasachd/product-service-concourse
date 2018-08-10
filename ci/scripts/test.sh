#!/bin/bash

set -eu

pushd source-code
  ls -lah
  ./gradlew test
popd
echo "all tests pass"