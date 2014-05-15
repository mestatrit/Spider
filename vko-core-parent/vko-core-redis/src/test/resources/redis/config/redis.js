var ioc = {
	redisDs : {
		type : 'cn.vko.core.redis.support.impl.RedisDataSource',
		fields : {
			config : {
				type : 'cn.vko.core.redis.support.RedisConfig',
				args : [ '192.168.1.7', 6378, 30000 ]
			}
		}
	},
	redis : {
		type : 'cn.vko.core.redis.RedisDao',
		args : [ {
			refer : "redisDs"
		} ]
	}
}