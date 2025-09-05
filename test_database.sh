#!/bin/bash
echo "🔧 Testing SQLite Database Integration..."

# Compile the project
mvn compile -q

# Get classpath
CLASSPATH=$(mvn dependency:build-classpath -q 2>/dev/null | tail -n 1)

# Run the database test
echo "🗄️ Running database test..."
java -cp "target/classes:$CLASSPATH" com.pharmacyfinder.TestSQLiteDatabase

# Check if database was created
echo "📁 Checking database files..."
if [ -f "./data/pharmacy_db.sqlite" ]; then
    echo "✅ SQLite database found!"
    ls -la ./data/pharmacy_db.sqlite
    
    # Show database size
    echo "📊 Database size: $(du -h ./data/pharmacy_db.sqlite | cut -f1)"
    
    # Try to query the database directly
    echo "🔍 Querying database directly..."
    sqlite3 ./data/pharmacy_db.sqlite "SELECT name, address FROM pharmacy LIMIT 3;" 2>/dev/null || echo "sqlite3 command not available"
else
    echo "❌ Database file not found"
fi

echo "✅ Database test complete!"
