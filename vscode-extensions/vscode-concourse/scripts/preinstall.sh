#!/bin/bash
set -e
(cd ../commons-vscode ; npm install)
npm install ../commons-vscode
../mvnw -U -f ../pom.xml -pl vscode-concourse -am clean install
