#!/bin/bash

mvn clean install
java -jar target/log4j-convert-1.0-SNAPSHOT-jar-with-dependencies.jar -i realConfs/teiid-jboss-log4j.xml
