/*
 * 本配置文件声明了整个应用的数据库连接部分。
 */
var ioc = {
	ds : {
		type : "com.alibaba.druid.pool.DruidDataSource",
		events : {
			depose : 'close'
		},
		fields : {
			DriverClassName : 'net.bull.javamelody.JdbcDriver',
			url : 'jdbc:mysql://192.168.1.231:3306/tikuout?useUnicode=true&characterEncoding=UTF-8',
			username : 'dba',
			password : 'mUdiKeji',
			InitialSize : 5,
			minIdle : 5,
			maxActive : 20,
			MinEvictableIdleTimeMillis : 1800,
			TimeBetweenEvictionRunsMillis : 1801,
			TestOnBorrow : false,
			testOnReturn : false,
			poolPreparedStatements : false,
			filters : 'stat'
		}
	},
	nut : {
		type : "org.nutz.dao.impl.NutDao",
		args : [ {
			refer : "ds"
		} ]
	},
	dao : {
		type : "cn.vko.core.db.dao.impl.DbDao",
		args : [ {
			refer : "nut"
		} ]
	},
	pipeline:{
		type : "cn.vko.business.spider.pipeline.MysqlPageModelSinglePipeline",
		fields : {
			dao:{refer:"dao"}
		}
	},
	updatepl:{
		type : "cn.vko.business.spider.pipeline.MysqlPageUpdatePipeline",
		fields : {
			dao:{refer:"dao"}
		}
	}
};