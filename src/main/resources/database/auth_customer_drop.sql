/*
===============================================================================
Java Easy Bank 系統權限模組 (Auth Module)
===============================================================================
*/

IF OBJECT_ID('AUTH_ACTION_LOG', 'U') IS NOT NULL DROP TABLE AUTH_ACTION_LOG;
IF OBJECT_ID('AUTH_LOGIN_LOG', 'U') IS NOT NULL DROP TABLE AUTH_LOGIN_LOG;
IF OBJECT_ID('AUTH_EMP', 'U') IS NOT NULL DROP TABLE AUTH_EMP;
IF OBJECT_ID('AUTH_ROLE', 'U') IS NOT NULL DROP TABLE AUTH_ROLE;
IF OBJECT_ID('AUTH_DEPT', 'U') IS NOT NULL DROP TABLE AUTH_DEPT;
GO

/*
===============================================================================
Java Easy Bank 客戶模組 (Customer Module)
===============================================================================
*/

IF OBJECT_ID('CUSTOMER_RISK_TAG', 'U') IS NOT NULL DROP TABLE CUSTOMER_RISK_TAG;
IF OBJECT_ID('CUSTOMER_KYC', 'U') IS NOT NULL DROP TABLE CUSTOMER_KYC;
IF OBJECT_ID('CUSTOMER_AUTH', 'U') IS NOT NULL DROP TABLE CUSTOMER_AUTH;
IF OBJECT_ID('CUSTOMER_PROFILE', 'U') IS NOT NULL DROP TABLE CUSTOMER_PROFILE;
IF OBJECT_ID('CUSTOMER_APPLICATION', 'U') IS NOT NULL DROP TABLE CUSTOMER_APPLICATION;
GO