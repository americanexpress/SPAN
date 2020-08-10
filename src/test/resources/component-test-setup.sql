--
-- Copyright 2020 American Express Travel Related Services Company, Inc.
--
-- Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
-- in compliance with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software distributed under the License
-- is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
-- or implied. See the License for the specific language governing permissions and limitations under
-- the License.
--

CREATE SCHEMA SCHEMA_ID AUTHORIZATION SA;
---
CREATE TABLE SCHEMA_ID.COMPTEST(
rs_str VARCHAR(100),
rs_int INT,
rs_integer INT,
rs_long BIGINT,
rs_longObj BIGINT,
rs_float FLOAT,
rs_floatObj FLOAT,
rs_double DOUBLE,
rs_doubleObj DOUBLE,
rs_bigint BIGINT,
rs_decimal DECIMAL(30,4),
rs_boolean BOOLEAN,
rs_booleanObj BOOLEAN
)
---

INSERT INTO SCHEMA_ID.COMPTEST(rs_str, rs_int, rs_integer, rs_long, rs_longObj, rs_float, rs_floatObj,
  rs_double, rs_doubleObj, rs_bigint, rs_decimal, rs_boolean, rs_booleanObj)
  VALUES ('2-RS', 2, 20, 400, 4000, 200, 2000, 20000, 200000, 2000000, 20000000, true, true);
---
INSERT INTO SCHEMA_ID.COMPTEST(rs_str, rs_int, rs_integer, rs_long, rs_longObj, rs_float, rs_floatObj,
  rs_double, rs_doubleObj, rs_bigint, rs_decimal, rs_boolean, rs_booleanObj)
  VALUES ('3-RS', 3, 30, 500, 5000, 300, 3000, 30000, 300000, 3000000, 30000000, true, true);
---
CREATE PROCEDURE SCHEMA_ID.PROC_NAME_1 (
IN in_str VARCHAR(100),
IN in_int INT,
IN in_integer INT,
IN in_long BIGINT,
IN in_longObj BIGINT,
IN in_float FLOAT,
IN in_floatObj FLOAT,
IN in_double DOUBLE,
IN in_doubleObj DOUBLE,
IN in_bigint BIGINT,
IN in_decimal DECIMAL(30,4),
IN in_boolean BOOLEAN,
IN in_booleanObj BOOLEAN,
IN in_timestamp TIMESTAMP,
OUT out_str VARCHAR(100),
OUT out_int INT,
OUT out_integer INT,
OUT out_long BIGINT,
OUT out_longObj BIGINT,
OUT out_float FLOAT,
OUT out_floatObj FLOAT,
OUT out_double DOUBLE,
OUT out_doubleObj DOUBLE,
OUT out_bigint BIGINT,
OUT out_decimal DECIMAL(30,4),
OUT out_boolean BOOLEAN,
OUT out_booleanObj BOOLEAN,
OUT out_timestamp TIMESTAMP
)
LANGUAGE SQL
READS SQL DATA DYNAMIC RESULT SETS 1
P1: BEGIN ATOMIC

   DECLARE clientcur CURSOR FOR SELECT * FROM SCHEMA_ID.COMPTEST;
   SET out_str = in_str;
   SET out_int = in_int;
   SET out_integer = in_integer;
   SET out_long = in_long;
   SET out_longObj = in_longObj;
   SET out_float = in_float;
   SET out_floatObj = in_floatObj;
   SET out_double = in_double;
   SET out_doubleObj = in_doubleObj;
   SET out_bigint = in_bigint;
   SET out_decimal = in_decimal;
   SET out_boolean = in_boolean;
   SET out_booleanObj = in_booleanObj;
   SET out_timestamp = in_timestamp;
   OPEN clientcur;

END P1;
---
CREATE PROCEDURE SCHEMA_ID.NORESULTSET ()
LANGUAGE SQL
BEGIN ATOMIC
  DECLARE BECAUSE_EMTPY_SP_NOT_ALLOWED int;
  SET BECAUSE_EMTPY_SP_NOT_ALLOWED = 0;

END
---
CREATE PROCEDURE SCHEMA_ID.MULTIRS (
)
LANGUAGE SQL
READS SQL DATA DYNAMIC RESULT SETS 2
P1: BEGIN ATOMIC

   DECLARE clientcur CURSOR FOR SELECT * FROM SCHEMA_ID.COMPTEST;
   DECLARE clientcur2 CURSOR FOR SELECT * FROM SCHEMA_ID.COMPTEST;
   DECLARE clientcur3 CURSOR FOR SELECT * FROM SCHEMA_ID.COMPTEST;
   OPEN clientcur;
   OPEN clientcur2;
   OPEN clientcur3;
END P1;
