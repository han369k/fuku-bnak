-- 1. CUSTOMER
INSERT INTO CUSTOMER (customer_id, name) VALUES
(1, 'John Wang'),
(2, 'Mary Lee'),
(3, 'David Chen'),
(4, 'Lisa Lin'),
(5, 'Tom Chang'),
(6, 'Amy Tsai'),
(7, 'Jay Chou'),
(8, 'Jack Wu'),
(9, 'Ryan Hsu'),
(10, 'Eddie Peng');

-- 2. CARD_TYPE
INSERT INTO CARD_TYPE (card_type_name, brand, annual_fee, cashback_rate, card_image_url) VALUES
('Cashback Card', 'VISA', 1000, 1.5, 'img/cashback1.png'),
('Travel Card', 'Master', 2000, 2.0, 'img/travel1.png'),
('Department Store Card', 'JCB', 1500, 1.2, 'img/store1.png'),
('Gas Card', 'VISA', 800, 3.0, 'img/gas1.png'),
('Student Card', 'Master', 0, 0.5, 'img/student1.png'),
('Business Card', 'VISA', 5000, 2.5, 'img/business1.png'),
('Airline Card', 'JCB', 3000, 2.2, 'img/airline1.png'),
('Shopping Card', 'Master', 1200, 1.8, 'img/shop1.png'),
('Dining Card', 'VISA', 900, 2.0, 'img/food1.png'),
('Black Card', 'Master', 10000, 3.5, 'img/black1.png');

-- 3. MERCHANT
INSERT INTO MERCHANT (merchant_id, merchant_name, merchant_category)
VALUES
(1, '7-11', 'SHOPPING'),
(2, 'FamilyMart', 'SHOPPING'),
(3, 'Starbucks', 'FOOD'),
(4, 'McDonalds', 'FOOD'),
(5, 'TSR', 'TRAVEL'),
(6, 'Uber', 'TRAVEL'),
(7, 'Shopee', 'SHOPPING'),
(8, 'Netflix', 'ENTERTAINMENT'),
(9, 'Eslite Bookstore', 'SHOPPING'),
(10, 'Carrefour', 'SHOPPING');

-- 4. CARD_APPLICATION
INSERT INTO CARD_APPLICATION (customer_id,apply_date, status, remark) VALUES
(1,getdate(), 'PENDING', NULL),
(2,getdate(), 'APPROVED', 'Good credit'),
(3,getdate(), 'REJECTED', 'Low income'),
(4,getdate(), 'PENDING', NULL),
(5,getdate(), 'APPROVED', NULL),
(6,getdate(), 'PENDING', NULL),
(7,getdate(), 'REJECTED', 'Bad credit history'),
(8,getdate(), 'APPROVED', NULL),
(9,getdate(), 'PENDING', NULL),
(10,getdate(), 'APPROVED', NULL);

-- 5. CARD_APPLICATION_ITEM
INSERT INTO CARD_APPLICATION_ITEM 
(application_id, card_type_id, result, approved_limit, annual_fee, create_card_flag, remark) VALUES
(1, 1, 'PENDING', NULL, NULL, 0, NULL),
(2, 2, 'APPROVED', 100000, 2000, 1, NULL),
(3, 3, 'REJECTED', NULL, NULL, 0, 'Credit issue'),
(4, 4, 'PENDING', NULL, NULL, 0, NULL),
(5, 5, 'APPROVED', 50000, 0, 1, NULL),
(6, 6, 'PENDING', NULL, NULL, 0, NULL),
(7, 7, 'REJECTED', NULL, NULL, 0, 'Score too low'),
(8, 8, 'APPROVED', 120000, 1200, 1, NULL),
(9, 9, 'PENDING', NULL, NULL, 0, NULL),
(10, 10, 'APPROVED', 500000, 10000, 1, NULL);

-- 6. CREDIT_CARD
;INSERT INTO CREDIT_CARD 
(customer_id, card_type_id, application_item_id, card_number, expiry_date, credit_limit, current_balance, status)
VALUES
(2, 2, 2, '4000000000000001', '2028-12-31', 100000, 2000, 'ACTIVE'),
(5, 5, 5, '4000000000000002', '2027-06-30', 50000, 1000, 'ACTIVE'),
(8, 8, 8, '4000000000000003', '2029-01-31', 120000, 5000, 'ACTIVE'),
(10, 10, 10, '4000000000000004', '2030-12-31', 500000, 20000, 'ACTIVE'),
(2, 1, NULL, '4000000000000005', '2027-05-31', 80000, 3000, 'ACTIVE'),
(5, 4, NULL, '4000000000000006', '2026-11-30', 60000, 4000, 'BLOCKED'),
(8, 6, NULL, '4000000000000007', '2028-09-30', 150000, 7000, 'ACTIVE'),
(10, 7, NULL, '4000000000000008', '2029-03-31', 200000, 10000, 'ACTIVE'),
(2, 3, NULL, '4000000000000009', '2027-08-31', 90000, 2000, 'ACTIVE'),
(5, 9, NULL, '4000000000000010', '2026-12-31', 70000, 1500, 'ACTIVE');

-- 7. CARD_TRANSACTION
INSERT INTO CARD_TRANSACTION
(card_id, merchant_id, ref_txn_id, txn_amount, txn_type, description)
VALUES
-- 卡1 消費
(1, 1, NULL, 150, 'PURCHASE', '7-11 purchase'),
(1, 3, NULL, 200, 'PURCHASE', 'Starbucks coffee'),
-- 卡2 消費
(2, 4, NULL, 300, 'PURCHASE', 'McDonalds meal'),
(2, 5, NULL, 1200, 'PURCHASE', 'Train ticket'),
-- 卡3 消費
(3, 7, NULL, 2500, 'PURCHASE', 'Shopee shopping'),
(3, 8, NULL, 390, 'PURCHASE', 'Netflix subscription'),
-- 卡4 消費
(4, 6, NULL, 500, 'PURCHASE', 'Uber ride'),
(4, 2, NULL, 180, 'PURCHASE', 'FamilyMart purchase'),
-- 卡5 消費
(5, 9, NULL, 600, 'PURCHASE', 'Book purchase'),
(5, 10, NULL, 2200, 'PURCHASE', 'Carrefour shopping');

-- 8. CREDIT_CARD_BILL
INSERT INTO CARD_BILL
(card_id, billing_month, bill_date, due_date, total_amount, minimum_payment, paid_amount, bill_status)
VALUES
(1, '2026-03', '2026-03-25', '2026-04-10', 2000, 200, 2000, 'PAID'),
(2, '2026-03', '2026-03-25', '2026-04-10', 1000, 100, 500, 'PARTIAL'),
(3, '2026-03', '2026-03-25', '2026-04-10', 5000, 500, 0, 'UNPAID'),
(4, '2026-03', '2026-03-25', '2026-04-10', 20000, 2000, 20000, 'PAID'),
(5, '2026-03', '2026-03-25', '2026-04-10', 3000, 300, 1000, 'PARTIAL'),
(6, '2026-03', '2026-03-25', '2026-04-10', 4000, 400, 0, 'UNPAID'),
(7, '2026-03', '2026-03-25', '2026-04-10', 7000, 700, 7000, 'PAID'),
(8, '2026-03', '2026-03-25', '2026-04-10', 10000, 1000, 5000, 'PARTIAL'),
(9, '2026-03', '2026-03-25', '2026-04-10', 2000, 200, 0, 'UNPAID'),
(10, '2026-03', '2026-03-25', '2026-04-10', 1500, 150, 1500, 'PAID');