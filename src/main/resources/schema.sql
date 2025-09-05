-- Pharmacy Database Schema for SQLite

-- Create Pharmacy table
CREATE TABLE IF NOT EXISTS pharmacy (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    address TEXT NOT NULL,
    phone TEXT,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    is_24_hours INTEGER DEFAULT 0,
    rating REAL DEFAULT 0.0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Create Medicine table
CREATE TABLE IF NOT EXISTS medicine (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    generic_name TEXT,
    category TEXT,
    unit_price REAL DEFAULT 0.00,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Create Pharmacy Medicine Stock table (junction table)
CREATE TABLE IF NOT EXISTS pharmacy_medicine_stock (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pharmacy_id INTEGER NOT NULL,
    medicine_id INTEGER NOT NULL,
    quantity INTEGER DEFAULT 0,
    price REAL DEFAULT 0.00,
    last_updated TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pharmacy_id) REFERENCES pharmacy(id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicine(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_pharmacy_location ON pharmacy(latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_medicine_name ON medicine(name);
CREATE INDEX IF NOT EXISTS idx_stock_pharmacy ON pharmacy_medicine_stock(pharmacy_id);
CREATE INDEX IF NOT EXISTS idx_stock_medicine ON pharmacy_medicine_stock(medicine_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_pharmacy_medicine ON pharmacy_medicine_stock(pharmacy_id, medicine_id);
