/**
 * 产品更新数据表
 */
DROP TABLE IF EXISTS t_update;
CREATE TABLE t_update (
    id			bigint 		PRIMARY KEY auto_increment	COMMENT 'ID',
    date	varchar(32) 							COMMENT '日期',
    title	varchar(128)							COMMENT '更新标题',
    count	int										COMMENT '更新数量',
    url		varchar(64)								COMMENT 'URL'
)Engine=InnoDB DEFAULT CHARSET=UTF8 COMMENT '产品更新数据表';

ALTER TABLE t_update ADD CONSTRAINT T_UPDATE_BD_IU_U 	UNIQUE (date, url);


/**
 * 产品关系数据表
 */
DROP TABLE IF EXISTS t_relate;
CREATE TABLE t_relate (
    id			bigint 		PRIMARY KEY auto_increment	COMMENT 'ID',
    bid		int										COMMENT 'BID',
    tid		int										COMMENT 'TID',
    nid		int										COMMENT 'NID'
)Engine=InnoDB DEFAULT CHARSET=UTF8 COMMENT '产品关系数据表';

ALTER TABLE t_relate ADD CONSTRAINT T_RELATE_BD_TD_ND_U 	UNIQUE (bid, tid, nid);
