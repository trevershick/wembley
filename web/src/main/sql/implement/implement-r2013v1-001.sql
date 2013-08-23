alter session set CURRENT_SCHEMA = R2DQ;

CREATE TABLE SYSTEM_AUDIT_LOG ( 
	AUDIT_LOG_ID		NUMBER(20,0) NOT NULL,
	ACTION				VARCHAR2(50) NOT NULL,
	ENTITY_NAME			VARCHAR2(100) NOT NULL,
	ENTITY_ID			VARCHAR2(255) NOT NULL,
	SOURCE_ENTITY_NAME	VARCHAR2(100),
	SOURCE_ENTITY_ID	VARCHAR2(255),
	AUDIT_LOG_TYPE	VARCHAR2(50) NOT NULL,
	ERROR_CAUSE			VARCHAR2(2000),
	DETAILS				VARCHAR2(2500) NOT NULL,
	CREATED_TS			TIMESTAMP NOT NULL	
);

GRANT SELECT,INSERT,UPDATE,DELETE ON SYSTEM_AUDIT_LOG TO R2DQ_APP_USER;

 CREATE TABLE IMPLEMENTATIO
   (	IMPLEMEN_ID NUMBER(19,0) NOT NULL ENABLE, 
	CREATED_TS TIMESTAMP (6) NOT NULL ENABLE, 
	CREATED_BY VARCHAR2(25 CHAR) NOT NULL ENABLE, 
	UPDATED_TS TIMESTAMP (6) NOT NULL ENABLE, 
	UPDATED_BY VARCHAR2(25 CHAR) NOT NULL ENABLE, 
	DELETED VARCHAR2(1 CHAR) NOT NULL ENABLE, 
	NOTE VARCHAR2(512 CHAR), 
	PRECEDENCE NUMBER(10,0) NOT NULL ENABLE, 
	TYPE VARCHAR2(50) NOT NULL ENABLE, 
	RULE_NUM_FROM NUMBER(19,0), 
	RULE_NUM_THRU NUMBER(19,0), 
	RULE_NUM_TYPE VARCHAR2(7 CHAR) NOT NULL ENABLE, 
	VERSION NUMBER(10,0), 
	SS_ID VARCHAR2(20 CHAR), 
	 PRIMARY KEY (IMPLEMEN_ID)
  );
GRANT SELECT,INSERT,UPDATE,DELETE ON IMPLEMENTATION TO R2DQ_APP_USER;

Create table MDM_EXCPTN_STATUS(
EXCPTN_ID NUMBER(20,0) NOT NULL,
EXCPTN_STATUS  VARCHAR2(100) NOT NULL,
CREATED_TS TIMESTAMP NOT NULL
);

GRANT SELECT,INSERT,UPDATE,DELETE ON MDM_EXCPTN_STATUS TO R2DQ_APP_USER;


CREATE TABLE MDM_EXCPTN_STATUS_VW(
    EXCPTN_ID NUMBER(20,0) NOT NULL ENABLE, 
	CREATED_TS TIMESTAMP (6) NOT NULL ENABLE, 
	EDW_SRC_ID NUMBER(20,0) NOT NULL ENABLE, 
	EDW_SRC_NAME VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	EXCPTN_CD NUMBER(20,0), 
	EXCPTN_DESC VARCHAR2(100 BYTE), 
	MDM_EXCPTN_TYPE VARCHAR2(100 BYTE), 
	MDM_EXCPTN_COL_NAME VARCHAR2(100 BYTE), 
	MDM_EXCPTN_VALUE VARCHAR2(100 BYTE), 
	SRC_KEY_COLUMN VARCHAR2(100 BYTE), 
	SRC_KEY NUMBER(20,0), 
	PARTIAL_SRC_INFO VARCHAR2(100 BYTE), 
	MDM_VALUE VARCHAR2(100 BYTE),
	EXCPTN_STATUS VARCHAR2(50)
 );
 
 GRANT SELECT,INSERT,UPDATE,DELETE ON MDM_EXCPTN_STATUS_VW TO R2DQ_APP_USER;