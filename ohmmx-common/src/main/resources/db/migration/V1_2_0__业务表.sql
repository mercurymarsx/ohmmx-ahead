/*==============================================================*/
/* Table: CFG_PARAMETER                                         */
/*==============================================================*/
CREATE TABLE CFG_PARAMETER
(
   ID                   VARCHAR(64) NOT NULL COMMENT '配置ID',
   BOOLEAN_VALUE        BIT(1) NOT NULL DEFAULT b'0',
   INT_VALUE            INT(11) NOT NULL DEFAULT '0',
   DECIMAL_VALUE        DECIMAL(19,2),
   LONG_VALUE           BIGINT(20) NOT NULL DEFAULT '0',
   STRING_VALUE         VARCHAR(255),
   RELOADABLE           BIT(1) NOT NULL DEFAULT b'0',
   CREATE_TIMESTAMP     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   MODIFY_TIMESTAMP     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (ID)
);

/*==============================================================*/
/* Table: TSK_QUEUE                                             */
/*==============================================================*/
CREATE TABLE TSK_QUEUE (
   TYPE                 VARCHAR(64) NOT NULL,
   REFERENCE            VARCHAR(255) NOT NULL COMMENT '任务REF',
   STATE                VARCHAR(16) NOT NULL DEFAULT 'NEW' COMMENT '任务状态',
   CNT_RETRY            INT(11) NOT NULL DEFAULT '0' COMMENT '当前状态重试次数',
   NOT_BEFORE           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最早执行时间',
   NOT_AFTER            DATETIME NOT NULL COMMENT '最晚执行时间',
   ST_HIST              LONGTEXT COMMENT '任务状态变更记录',
   ST_PREV              VARCHAR(16) DEFAULT '' COMMENT '上一状态',
   LAST_RUN             DATETIME COMMENT '上次执行时间',
   TTL                  INT(11) NOT NULL DEFAULT '255' COMMENT '剩余存活TTL',
   CREATE_TIMESTAMP     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   MODIFY_TIMESTAMP     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   LOCKED_BY            VARCHAR(32) COMMENT '锁定人(lock_owner)',
   TAGS                 INT(11) NOT NULL DEFAULT '0' COMMENT '位图标记',
   PRIMARY KEY (TYPE, REFERENCE),
   KEY IDX_TSK_QUEUE_NOT_AFTER (NOT_AFTER)
);
