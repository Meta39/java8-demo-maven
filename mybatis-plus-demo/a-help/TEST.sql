/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : Oracle
 Source Server Version : 110200
 Source Host           : localhost:1521
 Source Schema         : TEST

 Target Server Type    : Oracle
 Target Server Version : 110200
 File Encoding         : 65001

 Date: 11/04/2023 20:14:22
*/


-- ----------------------------
-- Table structure for ORACLEENTITY
-- ----------------------------
DROP TABLE "TEST"."ORACLEENTITY";
CREATE TABLE "TEST"."ORACLEENTITY" (
  "ID" NUMBER NOT NULL,
  "NAME" VARCHAR2(255 BYTE)
)
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN "TEST"."ORACLEENTITY"."ID" IS '主键';
COMMENT ON COLUMN "TEST"."ORACLEENTITY"."NAME" IS '姓名';

-- ----------------------------
-- Records of ORACLEENTITY
-- ----------------------------
INSERT INTO "TEST"."ORACLEENTITY" VALUES ('1', 'Meta39');

-- ----------------------------
-- Sequence structure for SEQ_ORACLEENTITY
-- ----------------------------
DROP SEQUENCE "TEST"."SEQ_ORACLEENTITY";
CREATE SEQUENCE "TEST"."SEQ_ORACLEENTITY" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 ORDER CACHE 20;

-- ----------------------------
-- Primary Key structure for table ORACLEENTITY
-- ----------------------------
ALTER TABLE "TEST"."ORACLEENTITY" ADD CONSTRAINT "SYS_C0011104" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table ORACLEENTITY
-- ----------------------------
ALTER TABLE "TEST"."ORACLEENTITY" ADD CONSTRAINT "SYS_C0011103" CHECK ("ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Triggers structure for table ORACLEENTITY
-- ----------------------------
CREATE TRIGGER "TEST"."TRG_ORACLEENTITY" BEFORE INSERT ON "TEST"."ORACLEENTITY" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
  select SEQ_ORACLEENTITY.nextval into:NEW.ID from dual;
END;
/
