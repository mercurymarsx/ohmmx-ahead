/*==============================================================*/
/* Table: HIS_CFG_PARAMETER                                     */
/*==============================================================*/
CREATE TABLE HIS_CFG_PARAMETER (
   UID                  BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '更新ID',
   UPDATE_TIMESTAMP     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   ID                   VARCHAR(64) NOT NULL COMMENT '配置ID',
   NID                  VARCHAR(64),
   BOOLEAN_VALUE        BIT(1),
   INT_VALUE            INT(11),
   DECIMAL_VALUE        DECIMAL(19,2),
   LONG_VALUE           BIGINT(20),
   STRING_VALUE         VARCHAR(255),
   RELOADABLE           BIT(1),
   MODIFY_TIMESTAMP     DATETIME NOT NULL,
   PRIMARY KEY (UID)
);

/*==============================================================*/
/* Table: HIS_TSK_QUEUE                                         */
/*==============================================================*/
CREATE TABLE HIS_TSK_QUEUE (
   DID                  BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   DELETE_TIMESTAMP     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   TYPE                 VARCHAR(32) NOT NULL,
   REFERENCE            VARCHAR(255) NOT NULL,
   STATE                VARCHAR(16) NOT NULL,
   CNT_RETRY            INT(11) NOT NULL,
   NOT_BEFORE           DATETIME NOT NULL,
   NOT_AFTER            DATETIME NOT NULL,
   ST_HIST              LONGTEXT,
   ST_PREV              VARCHAR(16),
   LAST_RUN             DATETIME,
   TTL                  INT(11) NOT NULL,
   CREATE_TIMESTAMP     DATETIME,
   MODIFY_TIMESTAMP     DATETIME,
   LOCKED_BY            VARCHAR(32),
   TAGS                 INT(11) NOT NULL,
   PRIMARY KEY (DID),
   KEY IDX_HIS_TSK_QUEUE_TYPE_REF (TYPE, REFERENCE)
);
