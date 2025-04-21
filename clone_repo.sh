#!/bin/bash

# Script to clone GitHub repositories listed in repos.txt into Projects directory
# Author: Generated script
# Date: 2025-04-20

echo "Starting repository cloning process..."

# Check if repos.txt exists
if [ ! -f "repos.txt" ]; then
    echo "Error: repos.txt file not found!"
    echo "Please create a file named repos.txt with GitHub repository URLs (one per line)."
    exit 1
fi

# Create Projects directory if it doesn't exist
if [ ! -d "Projects" ]; then
    echo "Creating Projects directory..."
    mkdir -p Projects
    if [ $? -ne 0 ]; then
        echo "Error: Failed to create Projects directory!"
        exit 1
    fi
    echo "Projects directory created successfully."
else
    echo "Projects directory already exists."
fi

# Initialize counters
total=0
success=0
failed=0

# Read repos.txt line by line and clone repositories
while IFS= read -r repo || [ -n "$repo" ]; do
    # Skip empty lines
    if [ -z "$repo" ]; then
        continue
    fi
    
    total=$((total + 1))
    repo_name=$(basename "$repo" .git)
    
    echo "---------------------------------------"
    echo "Cloning repository ($total): $repo_name"
    
    # Check if repository already exists
    if [ -d "Projects/$repo_name" ]; then
        echo "Repository $repo_name already exists. Skipping..."
        continue
    fi
    
    # Clone the repository
    git clone "$repo" "Projects/$repo_name"
    
    # Check if cloning was successful
    if [ $? -eq 0 ]; then
        echo "Successfully cloned $repo_name"
        success=$((success + 1))
    else
        echo "Failed to clone $repo_name"
        failed=$((failed + 1))
    fi
done < repos.txt

echo "---------------------------------------"
echo "Cloning process completed!"
echo "Summary:"
echo "  Total repositories processed: $total"
echo "  Successfully cloned: $success"
echo "  Failed to clone: $failed"

if [ $failed -eq 0 ]; then
    echo "All repositories were cloned successfully!"
else
    echo "Some repositories failed to clone. Please check the output for details."
fi

# Make the script executable
chmod +x clone_repos.sh

