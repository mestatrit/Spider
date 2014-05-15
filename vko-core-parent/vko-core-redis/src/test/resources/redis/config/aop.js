var ioc = {
	$aop : {
		type : 'org.nutz.ioc.aop.config.impl.ComboAopConfigration',
		fields : {
			aopConfigrations : [
					{
						type : 'org.nutz.ioc.aop.config.impl.JsonAopConfigration',
						fields : {
							itemList : [
							        [ 'cn.vko.core.redis.RedisDao', '.+', 'ioc:redisAop' ],
							        [ 'cn.vko.core.redis.ShardRedisDao', '.+', 'ioc:redisAop' ] ]
						}
					},
					{
						type : 'org.nutz.ioc.aop.config.impl.AnnotationAopConfigration'
					} ]
		}
	},
	redisAop : {
		type : 'cn.vko.core.redis.RedisInterceptor'
	}
}