#! /bin/bash

# new.sh: Create a new Maven project
# Usage: new.sh <project-name>
# Example: new.sh my-project

GROUP_ID='ai.rodolfomendes'
JAVA_VERSION='24'
JUNIT_VERSION='5.11.0'

mvn archetype:generate \
    -DarchetypeGroupId=org.apache.maven.archetypes \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DarchetypeVersion=1.5 \
    -DgroupId=$GROUP_ID \
    -DartifactId=$1 \
    -Dversion=1.0.0-SNAPSHOT \
    -Dpackage=$GROUP_ID \
    -DjavaCompilerVersion=$JAVA_VERSION \
    -DjunitVersion=$JUNIT_VERSION \
    -DinteractiveMode=false

    