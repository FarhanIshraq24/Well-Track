-- Sample Data for Pharmacy Finder

-- Insert sample medicines
INSERT INTO medicine (name, generic_name, category, unit_price) VALUES
('Paracetamol 500mg', 'Acetaminophen', 'Analgesic', 2.50),
('Ibuprofen 400mg', 'Ibuprofen', 'NSAID', 3.75),
('Amoxicillin 250mg', 'Amoxicillin', 'Antibiotic', 8.90),
('Omeprazole 20mg', 'Omeprazole', 'Antacid', 12.50),
('Metformin 500mg', 'Metformin', 'Antidiabetic', 5.25),
('Amlodipine 5mg', 'Amlodipine', 'Antihypertensive', 7.80),
('Atorvastatin 20mg', 'Atorvastatin', 'Cholesterol', 15.60),
('Cetirizine 10mg', 'Cetirizine', 'Antihistamine', 4.20),
('Aspirin 75mg', 'Acetylsalicylic Acid', 'Analgesic', 1.80),
('Losartan 50mg', 'Losartan', 'Antihypertensive', 9.40);

-- Insert sample pharmacies in Dhaka, Bangladesh
INSERT INTO pharmacy (name, address, phone, latitude, longitude, is_24_hours, rating) VALUES
('City Pharmacy', '123 Main Street, Dhanmondi, Dhaka-1205', '+880-2-9612345', 23.7461, 90.3742, true, 4.5),
('HealthCare Plus', '456 Park Avenue, Gulshan-1, Dhaka-1212', '+880-2-8812345', 23.7808, 90.4123, false, 4.2),
('Green Life Pharmacy', '789 New Market Road, New Market, Dhaka-1205', '+880-2-9513456', 23.7275, 90.3854, true, 4.7),
('Lazz Pharma', '321 Banani Road, Banani, Dhaka-1213', '+880-2-9876543', 23.7937, 90.4044, false, 4.0),
('Square Pharmacy', '654 Elephant Road, Kathalbagan, Dhaka-1205', '+880-2-9654321', 23.7500, 90.3889, true, 4.8),
('Popular Pharmacy', '987 Mirpur Road, Dhanmondi, Dhaka-1207', '+880-2-9112233', 23.7643, 90.3656, false, 4.3),
('Apollo Pharmacy', '147 Panthapath, Kawran Bazar, Dhaka-1215', '+880-2-9556677', 23.7510, 90.3931, true, 4.6),
('Wellness Pharmacy', '258 Bailey Road, Ramna, Dhaka-1000', '+880-2-9998877', 23.7364, 90.4042, false, 4.1),
('MedLife Pharmacy', '369 Satmasjid Road, Dhanmondi, Dhaka-1209', '+880-2-9443322', 23.7392, 90.3721, true, 4.4),
('Care Pharmacy', '741 Shantinagar Road, Shantinagar, Dhaka-1217', '+880-2-9223344', 23.7458, 90.4156, false, 4.2);

-- Insert sample stock data (random availability)
-- Pharmacy 1 (City Pharmacy) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(1, 1, 150, 2.50), (1, 2, 80, 3.75), (1, 3, 45, 8.90), (1, 4, 90, 12.50), (1, 5, 120, 5.25),
(1, 6, 60, 7.80), (1, 7, 30, 15.60), (1, 8, 85, 4.20), (1, 9, 200, 1.80), (1, 10, 75, 9.40);

-- Pharmacy 2 (HealthCare Plus) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(2, 1, 100, 2.60), (2, 2, 0, 3.75), (2, 3, 25, 9.00), (2, 4, 70, 12.75), (2, 5, 95, 5.40),
(2, 6, 40, 8.00), (2, 7, 20, 15.90), (2, 8, 110, 4.30), (2, 9, 180, 1.85), (2, 10, 0, 9.40);

-- Pharmacy 3 (Green Life Pharmacy) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(3, 1, 200, 2.40), (3, 2, 120, 3.60), (3, 3, 65, 8.75), (3, 4, 110, 12.25), (3, 5, 140, 5.10),
(3, 6, 80, 7.60), (3, 7, 45, 15.40), (3, 8, 95, 4.10), (3, 9, 250, 1.75), (3, 10, 100, 9.20);

-- Pharmacy 4 (Lazz Pharma) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(4, 1, 75, 2.70), (4, 2, 50, 3.90), (4, 3, 0, 8.90), (4, 4, 60, 13.00), (4, 5, 85, 5.60),
(4, 6, 35, 8.20), (4, 7, 15, 16.20), (4, 8, 70, 4.50), (4, 9, 160, 1.90), (4, 10, 55, 9.80);

-- Pharmacy 5 (Square Pharmacy) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(5, 1, 180, 2.30), (5, 2, 90, 3.50), (5, 3, 55, 8.50), (5, 4, 100, 12.00), (5, 5, 130, 5.00),
(5, 6, 70, 7.40), (5, 7, 40, 15.00), (5, 8, 105, 3.90), (5, 9, 220, 1.70), (5, 10, 90, 9.00);

-- Continue with other pharmacies but with some out of stock items
-- Pharmacy 6 (Popular Pharmacy) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(6, 1, 90, 2.55), (6, 3, 35, 8.95), (6, 4, 80, 12.60), (6, 5, 0, 5.25), (6, 7, 25, 15.80),
(6, 8, 115, 4.25), (6, 9, 190, 1.80), (6, 10, 65, 9.50);

-- Pharmacy 7 (Apollo Pharmacy) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(7, 1, 160, 2.45), (7, 2, 100, 3.70), (7, 3, 50, 8.80), (7, 4, 95, 12.40), (7, 6, 85, 7.70),
(7, 7, 35, 15.50), (7, 8, 0, 4.20), (7, 9, 210, 1.75), (7, 10, 80, 9.30);

-- Pharmacy 8 (Wellness Pharmacy) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(8, 1, 120, 2.65), (8, 2, 70, 3.85), (8, 4, 85, 12.80), (8, 5, 110, 5.35), (8, 6, 45, 7.90),
(8, 8, 125, 4.35), (8, 9, 175, 1.85), (8, 10, 70, 9.60);

-- Pharmacy 9 (MedLife Pharmacy) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(9, 1, 140, 2.50), (9, 2, 85, 3.75), (9, 3, 40, 8.90), (9, 5, 125, 5.25), (9, 6, 55, 7.80),
(9, 7, 30, 15.60), (9, 9, 200, 1.80), (9, 10, 85, 9.40);

-- Pharmacy 10 (Care Pharmacy) stock
INSERT INTO pharmacy_medicine_stock (pharmacy_id, medicine_id, quantity, price) VALUES
(10, 1, 110, 2.60), (10, 3, 30, 9.10), (10, 4, 75, 12.90), (10, 5, 100, 5.50), (10, 6, 65, 8.10),
(10, 7, 20, 16.00), (10, 8, 95, 4.40), (10, 10, 75, 9.70);
