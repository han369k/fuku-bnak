/*
===============================================================================
Java Easy Bank 系統權限模組 (Auth Module)
===============================================================================
*/

IF OBJECT_ID('auth_login_log', 'U') IS NOT NULL DROP TABLE auth_login_log;
IF OBJECT_ID('auth_emp', 'U') IS NOT NULL DROP TABLE auth_emp;
IF OBJECT_ID('auth_role', 'U') IS NOT NULL DROP TABLE auth_role;
IF OBJECT_ID('auth_dept', 'U') IS NOT NULL DROP TABLE auth_dept;
GO

/*
===============================================================================
Java Easy Bank 客戶模組 (Customer Module)
===============================================================================
*/

IF OBJECT_ID('customer_risk_tag', 'U') IS NOT NULL DROP TABLE customer_risk_tag;
IF OBJECT_ID('customer_kyc', 'U') IS NOT NULL DROP TABLE customer_kyc;
IF OBJECT_ID('customer_profile', 'U') IS NOT NULL DROP TABLE customer_profile;
IF OBJECT_ID('customer_application', 'U') IS NOT NULL DROP TABLE customer_application;
GO