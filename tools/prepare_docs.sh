#!/bin/bash
set -e

# Generate API docs
./gradlew dokkaHtmlMultiModule

# Copy *.md files into docs directory
cp README.md docs/index.md
cp jettheme/README.md docs/jettheme.md
cp jettheme-material/README.md docs/jettheme-material.md
cp CHANGELOG.md docs/changelog.md
