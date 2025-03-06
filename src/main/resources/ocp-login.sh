#!/bin/bash
#
# OpenShift Login Script for Git Bash on Windows
# This script authenticates to OpenShift Container Platform using OAuth token
#
# SECURITY WARNING: This script contains a hardcoded password which is a significant security risk!
# ----------------------------------------------------------------------------------------------
# DANGER: Storing credentials in scripts is NOT RECOMMENDED for production environments.
# Better alternatives:
# 1. Use environment variables stored securely
# 2. Use OpenShift service accounts with token authentication
# 3. Use credential management tools like credential-helper
# 4. Use OAuth device flow authentication
# ----------------------------------------------------------------------------------------------

# ========== OpenShift Configuration ==========
# CUSTOMIZE THESE VALUES FOR YOUR ENVIRONMENT
OCP_SERVER="https://api.cluster.example.com:6443"
OAUTH_SERVER="https://oauth-openshift.apps.cluster.example.com/oauth/token"

# ========== Hardcoded Credentials ==========
# WARNING: This is a major security risk - use only for testing/development
# REPLACE THIS with your actual password
PASSWORD="your-secure-password-here" 

# ========== Script Variables ==========
USERNAME=""
TOKEN=""
CURL_OPTS="--insecure --silent" # Remove --insecure in production!

# ========== Functions ==========
function display_banner() {
    echo "============================================================"
    echo "  OpenShift Container Platform Login Script"
    echo "============================================================"
    echo "  WARNING: This script contains hardcoded credentials"
    echo "  This is a significant security risk and should not be used"
    echo "  in production environments or committed to source control."
    echo "============================================================"
    echo ""
}

function check_dependencies() {
    # Check if curl is available
    if ! command -v curl &> /dev/null; then
        echo "Error: curl is required but not installed. Please install curl."
        exit 1
    fi

    # Check if jq is available (for JSON parsing)
    if ! command -v jq &> /dev/null; then
        echo "Warning: jq is not installed. Token extraction may be less reliable."
    fi

    # Check if oc is available
    if ! command -v oc &> /dev/null; then
        echo "Error: OpenShift CLI (oc) is required but not installed."
        exit 1
    fi
}

function get_token() {
    echo "Obtaining OAuth token for user: $USERNAME"
    
    # Using curl to get token from OAuth server
    local response
    
    if command -v jq &> /dev/null; then
        # Using jq for JSON parsing if available
        response=$(curl $CURL_OPTS \
            -X POST \
            -d "grant_type=password" \
            -d "username=$USERNAME" \
            -d "password=$PASSWORD" \
            -d "client_id=openshift-challenging-client" \
            "$OAUTH_SERVER")
        
        # Extract token using jq
        TOKEN=$(echo "$response" | jq -r .access_token 2>/dev/null)
    else
        # Fallback method using grep and cut if jq is not available
        response=$(curl $CURL_OPTS \
            -X POST \
            -d "grant_type=password" \
            -d "username=$USERNAME" \
            -d "password=$PASSWORD" \
            -d "client_id=openshift-challenging-client" \
            "$OAUTH_SERVER")
        
        # Extract token using grep and cut
        TOKEN=$(echo "$response" | grep -o '"access_token":"[^"]*' | cut -d'"' -f4)
    fi
    
    # Check if token was obtained
    if [[ -z "$TOKEN" || "$TOKEN" == "null" ]]; then
        echo "Error: Failed to obtain token. Check your credentials and server configuration."
        echo "Response: $response"
        exit 1
    else
        echo "Token successfully obtained."
    fi
}

function login_to_openshift() {
    echo "Logging in to OpenShift cluster at $OCP_SERVER"
    
    # Login using the obtained token
    if oc login --token="$TOKEN" --server="$OCP_SERVER" --insecure-skip-tls-verify=true; then
        echo "Successfully logged in as $USERNAME"
        echo "Current context: $(oc config current-context)"
        echo "You can now use 'oc' commands to interact with the cluster."
    else
        echo "Error: Failed to log in to OpenShift cluster."
        exit 1
    fi
}

function cleanup() {
    # Clear sensitive variables
    TOKEN=""
    PASSWORD=""
}

# ========== Main Script ==========
# Show banner with warning
display_banner

# Check for username parameter
if [ $# -lt 1 ]; then
    echo "Usage: $0 <username>"
    echo "Example: $0 johndoe"
    exit 1
fi

# Set username from first parameter
USERNAME="$1"

# Check for required tools
check_dependencies

# Get authentication token
get_token

# Login to OpenShift
login_to_openshift

# Cleanup sensitive data
trap cleanup EXIT
echo "Login process completed."

