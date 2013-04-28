#!/bin/bash

mvn clean install
chmod a+x ./target/log4j-convert/bin/log4j-convert
./target/log4j-convert/bin/log4j-convert -i sample.properties
