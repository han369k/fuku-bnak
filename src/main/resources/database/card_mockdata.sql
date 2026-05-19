SET NOCOUNT ON;

DELETE FROM CARD_TRANSACTION;
DELETE FROM CARD_BILL;
DELETE FROM CREDIT_CARD;
DELETE FROM CARD_ACCOUNT;
DELETE FROM CARD_APPLICATION_ITEM;
DELETE FROM CARD_APPLICATION;
DELETE FROM MERCHANT;
DELETE FROM CARD_TYPE;

SET IDENTITY_INSERT CARD_TYPE ON;
INSERT INTO CARD_TYPE (card_type_id, card_type_name, brand, annual_fee, cashback_rate, card_image_url) VALUES
(1, N'Cashback', 'VISA', 1000, 1.5, 'img/cashback1.png'),
(2, N'Travel', 'Master', 2000, 2.0, 'img/travel1.png'),
(3, N'Store', 'JCB', 1500, 1.2, 'img/store1.png');
SET IDENTITY_INSERT CARD_TYPE OFF;

INSERT INTO MERCHANT (merchant_id, merchant_name, merchant_category) VALUES
(1, '7-11', 'SHOPPING'),
(2, 'Starbucks', 'FOOD'),
(3, 'Uber', 'TRAVEL');

SET IDENTITY_INSERT CARD_APPLICATION ON;
INSERT INTO CARD_APPLICATION (application_id, customer_id, apply_date, status, remark) VALUES
(1, 'Q8M4T7K2', '2026-05-01', 'COMPLETED', N'Primary demo card'),
(2, 'R5N9W3A6', '2026-05-02', 'COMPLETED', NULL),
(3, 'P4W7N6C3', '2026-05-03', 'PENDING', NULL);
SET IDENTITY_INSERT CARD_APPLICATION OFF;

SET IDENTITY_INSERT CARD_APPLICATION_ITEM ON;
INSERT INTO CARD_APPLICATION_ITEM (
    item_id, application_id, card_type_id, result, approved_limit,
    annual_fee, create_card_flag, review_date, remark
) VALUES
(1, 1, 1, 'APPROVED', 120000, 1000, 1, '2026-05-02', NULL),
(2, 2, 2, 'APPROVED', 200000, 2000, 1, '2026-05-03', NULL),
(3, 3, 3, 'PENDING', NULL, NULL, 0, NULL, NULL);
SET IDENTITY_INSERT CARD_APPLICATION_ITEM OFF;

SET IDENTITY_INSERT CARD_ACCOUNT ON;
INSERT INTO CARD_ACCOUNT (id, account_number, credit_limit, statement_day, due_days, customer_id) VALUES
(1, '80101100000001', 120000, 5, 15, 'Q8M4T7K2'),
(2, '80101100000002', 200000, 10, 15, 'R5N9W3A6'),
(3, '80101100000003', 150000, 15, 15, 'P4W7N6C3');
SET IDENTITY_INSERT CARD_ACCOUNT OFF;

SET IDENTITY_INSERT CREDIT_CARD ON;
INSERT INTO CREDIT_CARD (
    card_id, customer_id, card_type_id, application_item_id, card_number,
    expiry_date, current_debt, create_date, status,
    credit_card_account_number, card_account_id
) VALUES
(1, 'Q8M4T7K2', 1, 1, '4000000010000001', '2028-01-28', 5850.00, '2026-05-02', 'ACTIVE', '80101100000001', 1),
(2, 'R5N9W3A6', 2, 2, '4000000010000002', '2028-02-28', 6003.00, '2026-05-03', 'ACTIVE', '80101100000002', 2),
(3, 'P4W7N6C3', 3, 3, '4000000010000003', '2028-03-28', 0.00, '2026-05-04', 'ACTIVE', '80101100000003', 3);
SET IDENTITY_INSERT CREDIT_CARD OFF;

SET IDENTITY_INSERT CARD_BILL ON;
INSERT INTO CARD_BILL (
    bill_id, card_id, card_account_id, billing_month, bill_date, due_date,
    total_amount, minimum_payment, paid_amount, bill_status,
    cashback_amount, reward_posted, reward_reference_id
) VALUES
(1, 1, 1, '2026-05', '2026-05-05', '2026-05-20', 5850.00, 500.00, 0.00, 'UNPAID', 0.00, 0, NULL),
(2, 2, 2, '2026-05', '2026-05-05', '2026-05-20', 6003.00, 600.00, 0.00, 'UNPAID', 0.00, 0, NULL);
SET IDENTITY_INSERT CARD_BILL OFF;

SET IDENTITY_INSERT CARD_TRANSACTION ON;
INSERT INTO CARD_TRANSACTION (
    txn_id, card_id, merchant_id, bill_id, ref_txn_id, txn_amount,
    cashback_rate, cashback_amount, txn_type, txn_date, description,
    channel, external_txn_id
) VALUES
(1, 1, 1, 1, NULL, 120.00, 1.50, 1.80, 'PURCHASE', '2026-05-06', N'Convenience store', 'POS', 'EXT-CARD-0001'),
(2, 1, 2, 1, NULL, 240.00, 1.50, 3.60, 'PURCHASE', '2026-05-07', N'Coffee', 'POS', 'EXT-CARD-0002'),
(3, 2, 3, 2, NULL, 560.00, 2.00, 11.20, 'PURCHASE', '2026-05-08', N'Ride share', 'MOBILE', 'EXT-CARD-0003');
SET IDENTITY_INSERT CARD_TRANSACTION OFF;
