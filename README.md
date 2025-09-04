# 🏥 Welltrack - Pharmacy Finder Application

A modern desktop application built with JavaFX and React that helps users locate nearby pharmacies and check medicine availability.

## 🚀 Features

- **🔍 Medicine Search**: Search for specific medicines across nearby pharmacies
- **📍 Location Services**: Automatic location detection for finding nearby pharmacies
- **🗺️ Interactive Map**: Visual map showing pharmacy locations with availability status
- **📋 Pharmacy List**: Detailed list view with contact information and stock status
- **💻 Cross-Platform**: Desktop application that works on Windows, macOS, and Linux
- **🎨 Modern UI**: Clean, responsive React frontend embedded in JavaFX

## 🛠️ Technology Stack

### Backend
- **JavaFX 19**: Desktop application framework
- **JavaFX WebView**: Embedded browser for React frontend
- **Maven**: Build management and dependency handling

### Frontend
- **React 18**: Modern JavaScript UI framework
- **Styled Components**: CSS-in-JS styling
- **Leaflet**: Interactive maps with OpenStreetMap
- **Webpack**: Frontend build and bundling

## 📋 Prerequisites

- **Java 19 or later**: OpenJDK/Oracle JDK
- **Node.js 16 or later**: For frontend development
- **Maven 3.6+**: For building the application

## 🔧 Installation & Setup

### 1. Clone/Extract the Project
```bash
# If you have a git repository
git clone <repository-url>
cd Welltrack

# Or extract from zip and navigate to the directory
cd Welltrack
```

### 2. Build the Application
```bash
# This will install Node.js dependencies, build React frontend, and compile Java code
mvn compile
```

### 3. Run the Application
```bash
mvn exec:java
```

## 🎮 How to Use

### Starting the Application
1. Run `mvn exec:java` from the project root
2. A JavaFX window will open with the pharmacy finder interface
3. The React frontend loads automatically in the embedded WebView

### Finding Pharmacies
1. **Location Detection**: The app automatically detects your location (defaults to Dhaka, Bangladesh for demo)
2. **Search Medicine**: Enter a medicine name in the search field
3. **View Results**: 
   - Left panel shows a list of nearby pharmacies
   - Right panel displays an interactive map with pharmacy markers
   - Green markers (🟢) = Medicine in stock
   - Red markers (🔴) = Medicine out of stock

### Interacting with Results
- **Click on pharmacy cards** in the left panel to highlight them on the map
- **Click on map markers** to see detailed pharmacy information
- **View contact details** including address and phone numbers

## 🏗️ Project Structure

```
Welltrack/
├── src/main/java/com/pharmacyfinder/
│   ├── PharmacyFinderApp.java      # Main JavaFX application
│   └── HelloController.java        # JavaFX controller with WebView
├── src/main/resources/
│   ├── fxml/main.fxml              # JavaFX layout
│   ├── css/application.css         # JavaFX styling
│   └── web/                        # Built React app (generated)
├── frontend/
│   ├── src/
│   │   ├── App.jsx                 # Main React component
│   │   ├── components/             # React components
│   │   │   ├── MapView.jsx         # Interactive map
│   │   │   ├── PharmacyList.jsx    # Pharmacy list
│   │   │   └── SearchForm.jsx      # Search interface
│   │   └── index.js               # React entry point
│   ├── package.json               # Frontend dependencies
│   └── webpack.config.js          # Build configuration
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

## 🔧 Development

### Frontend Development
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Build for production
npm run build

# Development build (for testing)
npm run dev
```

### Backend Development
```bash
# Compile Java code
mvn compile

# Run the application
mvn exec:java

# Clean and rebuild everything
mvn clean compile
```

### Making Changes

1. **Frontend Changes**: 
   - Edit files in `frontend/src/`
   - Run `npm run build` to rebuild
   - Restart the JavaFX application

2. **Backend Changes**:
   - Edit Java files in `src/main/java/`
   - Run `mvn compile` to recompile
   - Restart the application

## 🎯 API Integration

The application currently uses mock data for demonstration. To integrate with real pharmacy APIs:

1. **Update `HelloController.java`**:
   - Replace mock data in `setupJavaScriptBridge()` method
   - Implement real location services
   - Add actual pharmacy API calls

2. **Add Dependencies**:
   - HTTP client libraries (OkHttp is already included)
   - JSON processing libraries (Jackson is already included)
   - Location services APIs

## 🚨 Troubleshooting

### Common Issues

1. **JavaFX Module Warnings**:
   ```
   WARNING: Unsupported JavaFX configuration
   ```
   - This is normal for development and doesn't affect functionality

2. **Frontend Not Loading**:
   - Ensure `npm run build` completed successfully
   - Check that files exist in `src/main/resources/web/`

3. **Map Not Displaying**:
   - Requires internet connection for OpenStreetMap tiles
   - Check browser console in WebView for JavaScript errors

4. **Java Version Issues**:
   - Ensure Java 19+ is installed and JAVA_HOME is set correctly
   - Update `maven.compiler.source` and `maven.compiler.target` in `pom.xml` if needed

### Testing the Setup
```bash
# Run the included test script
./test-app.sh
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📜 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- **OpenStreetMap** for map tiles
- **Leaflet** for mapping functionality  
- **React** team for the excellent UI framework
- **JavaFX** community for desktop application support

---

**🎉 Your Pharmacy Finder application is now ready to use!**

For questions or support, please check the troubleshooting section or create an issue in the repository.
