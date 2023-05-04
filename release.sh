#!/bin/bash

#Build and Release the artifact to Maven Central Repository
#refer profile release-to-maven-central in the pom.xml

mvn clean deploy -Prelease-to-maven-central
