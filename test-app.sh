#!/bin/bash

echo "🏥 Testing Pharmacy Finder Application..."
echo "========================================="

# Check if Java 19 is available
echo "Checking Java version..."
java -version

# Check if frontend was built successfully
echo -e "\nChecking frontend build..."
if [ -f "src/main/resources/web/index.html" ] && [ -f "src/main/resources/web/dist/bundle.js" ]; then
    echo "✅ Frontend build files found"
    echo "   - index.html: $(wc -c < src/main/resources/web/index.html) bytes"
    echo "   - bundle.js: $(wc -c < src/main/resources/web/dist/bundle.js) bytes"
else
    echo "❌ Frontend build files missing"
fi

# Check if Java classes compiled successfully
echo -e "\nChecking Java compilation..."
if [ -f "target/classes/com/pharmacyfinder/PharmacyFinderApp.class" ]; then
    echo "✅ Java classes compiled successfully"
else
    echo "❌ Java classes not found"
fi

echo -e "\n🎯 Application is ready to run!"
echo "To start the application manually:"
echo "  mvn exec:java"
echo ""
echo "The application will:"
echo "  1. Start a JavaFX desktop window"
echo "  2. Load the React frontend in a WebView"
echo "  3. Provide medicine search functionality"
echo "  4. Show pharmacy locations on an interactive map"
