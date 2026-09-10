#!/bin/bash
# Load environment variables from .env file
export $(cat .env | xargs)

# Run the JavaFX application
mvn javafx:run
