#!/bin/bash

# Test script to validate semantic versioning implementation
# This script simulates the version calculation logic from build.gradle.kts

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
VERSION_FILE="$PROJECT_ROOT/version.properties"

echo "Testing Semantic Versioning Implementation"
echo "========================================="

if [ ! -f "$VERSION_FILE" ]; then
    echo "❌ ERROR: version.properties file not found"
    exit 1
fi

# Read version properties (simulate Gradle property loading)
source "$VERSION_FILE"

echo "📋 Version Properties:"
echo "  VERSION_MAJOR=$VERSION_MAJOR"
echo "  VERSION_MINOR=$VERSION_MINOR" 
echo "  VERSION_PATCH=$VERSION_PATCH"
echo ""

# Calculate semantic version (simulate build.gradle.kts logic)
semantic_version="$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"
version_code=$((VERSION_MAJOR * 10000 + VERSION_MINOR * 100 + VERSION_PATCH))

echo "📱 Generated App Version:"
echo "  versionName: $semantic_version"
echo "  versionCode: $version_code"
echo ""

# Validate version format
if [[ "$semantic_version" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "✅ Version format is valid (semantic versioning)"
else
    echo "❌ Version format is invalid"
    exit 1
fi

# Validate version code is positive integer
if [[ "$version_code" =~ ^[0-9]+$ ]] && [ "$version_code" -gt 0 ]; then
    echo "✅ Version code is valid ($version_code)"
else
    echo "❌ Version code is invalid"
    exit 1
fi

echo ""
echo "🎉 All semantic versioning tests passed!"
echo "📦 Ready for Android build with version $semantic_version (code: $version_code)"