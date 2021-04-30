#!/bin/bash

tar zxvf openjdk-11.0.1_linux-x64_bin.tar.gz
export JAVA_HOME=/workspace/jdk-11.0.1
export PATH=/workspace/jdk-11.0.1/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
cd jdk-11.0.1/bin
apt-get update
apt-get install -y unzip
unzip ../../allure-2.13.9.zip
allure-2.13.9/bin/allure generate ../../target/allure-results --clean -o ../../target/allure-report
cd ../../target/allure-report
ls -ll