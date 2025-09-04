#!/bin/bash

# Welltrack - JavaFX Application Runner for macOS
# Healthcare & Medicine Tracker with macOS optimizations

echo "🚀 Starting WellTrack Application..."
echo "💊 Royal Green Healthcare & Medicine Tracker"
echo "Using Java version: $(java --version | head -n 1)"

# Clean and compile first
echo "Cleaning and compiling project..."
mvn clean compile

# Run with JavaFX plugin (recommended approach)
echo "Running application with JavaFX plugin..."
mvn javafx:run \
    -Djavafx.args="--add-opens=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED \
    --add-opens=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED \
    --add-opens=javafx.base/com.sun.javafx.binding=ALL-UNNAMED \
    --add-opens=javafx.base/com.sun.javafx.event=ALL-UNNAMED \
    --add-opens=javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED \
    --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
    -Dapple.awt.application.appearance=system \
    -Dapple.laf.useScreenMenuBar=true \
    -Dcom.apple.mrj.application.apple.menu.about.name=WellTrack"

echo "✅ WellTrack application finished."
