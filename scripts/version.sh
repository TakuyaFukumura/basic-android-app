#!/bin/bash

# Semantic Version Management Script
# Usage: ./scripts/version.sh [major|minor|patch|current]

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
VERSION_FILE="$PROJECT_ROOT/version.properties"

if [ ! -f "$VERSION_FILE" ]; then
    echo "Error: version.properties file not found at $VERSION_FILE"
    exit 1
fi

# Read current version
source "$VERSION_FILE"

current_version="$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"

case "${1:-current}" in
    "major")
        new_major=$((VERSION_MAJOR + 1))
        new_minor=0
        new_patch=0
        new_version="$new_major.$new_minor.$new_patch"
        echo "Bumping major version: $current_version → $new_version"
        
        # Update version.properties
        sed -i "s/VERSION_MAJOR=.*/VERSION_MAJOR=$new_major/" "$VERSION_FILE"
        sed -i "s/VERSION_MINOR=.*/VERSION_MINOR=$new_minor/" "$VERSION_FILE"
        sed -i "s/VERSION_PATCH=.*/VERSION_PATCH=$new_patch/" "$VERSION_FILE"
        ;;
    "minor")
        new_major=$VERSION_MAJOR
        new_minor=$((VERSION_MINOR + 1))
        new_patch=0
        new_version="$new_major.$new_minor.$new_patch"
        echo "Bumping minor version: $current_version → $new_version"
        
        # Update version.properties
        sed -i "s/VERSION_MINOR=.*/VERSION_MINOR=$new_minor/" "$VERSION_FILE"
        sed -i "s/VERSION_PATCH=.*/VERSION_PATCH=$new_patch/" "$VERSION_FILE"
        ;;
    "patch")
        new_major=$VERSION_MAJOR
        new_minor=$VERSION_MINOR
        new_patch=$((VERSION_PATCH + 1))
        new_version="$new_major.$new_minor.$new_patch"
        echo "Bumping patch version: $current_version → $new_patch"
        
        # Update version.properties
        sed -i "s/VERSION_PATCH=.*/VERSION_PATCH=$new_patch/" "$VERSION_FILE"
        ;;
    "current"|*)
        echo "Current version: $current_version"
        echo "Version code: $((VERSION_MAJOR * 10000 + VERSION_MINOR * 100 + VERSION_PATCH))"
        ;;
esac

if [ "$1" != "current" ] && [ -n "$1" ]; then
    echo "Updated version.properties"
    echo "New version: $(grep VERSION_MAJOR "$VERSION_FILE" | cut -d'=' -f2).$(grep VERSION_MINOR "$VERSION_FILE" | cut -d'=' -f2).$(grep VERSION_PATCH "$VERSION_FILE" | cut -d'=' -f2)"
fi