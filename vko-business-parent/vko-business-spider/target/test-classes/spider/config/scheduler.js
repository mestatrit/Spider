/*
 * 本配置文件声明了整个应用的数据库连接部分。
 */
var ioc = {
	scheduler : {
		type : "cn.vko.business.spider.scheduler.RedisFilterScheduler",
		args : [ "192.168.1.231", 6379 ],
		fields : {
			filter : {
				refer : "rbf"
			}
		}
	},
	rbf : {
		type : "cn.vko.business.spider.urlfilter.impl.RedisBloomFilter",
		args : [ "192.168.1.231", 6379 ]
	},
	rejectf : {
		type : "cn.vko.business.spider.urlfilter.impl.RejectFilter"
	},
	rejectsc : {
		type : "cn.vko.business.spider.scheduler.RedisFilterScheduler",
		args : [ "192.168.1.231", 6379 ],
		fields : {
			filter : {
				refer : "rejectf"
			}
		}
	}
};