package cn.xuexibao.business.spider.scheduler;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.EncryptUtil;
import cn.vko.core.common.util.JsonUtil;

@Data
@EqualsAndHashCode(callSuper = true)
public class RedisFilterScheduler extends FilterScheduler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(RedisFilterScheduler.class);
	private JedisPool pool;

	private static final String QUEUE_PREFIX = "queue_";

	private static final String ITEM_PREFIX = "item_";

	public RedisFilterScheduler(String host, int port) {
		pool = new JedisPool(new JedisPoolConfig(), host, port);
	}

	public RedisFilterScheduler(JedisPool pool) {
		this.pool = pool;
	}

	public Request poll(Task task) {
		Jedis jedis = pool.getResource();
		try {
			String url = jedis.lpop(QUEUE_PREFIX + task.getUUID());
			if (logger.isInfoEnabled()) {
				logger.info("poll url=" + url); //$NON-NLS-1$
			}

			if (url == null) {
				return null;
			}
			// if (filter.existUrl(task.getUUID(), url)) {
			// return null;
			// }
			String key = ITEM_PREFIX + task.getUUID();
			String field = EncryptUtil.md5(url);
			byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
			if (bytes != null) {
				Request o = JsonUtil.fromJson(new String(bytes), Request.class);
				return o;
			}
			Request request = new Request(url);
			return request;
		} finally {
			pool.returnResource(jedis);
		}
	}

	@Override
	public void pushWhenNoDuplicate(Request request, Task task) {
		Jedis jedis = pool.getResource();
		try {
			jedis.rpush(QUEUE_PREFIX + task.getUUID(), request.getUrl());
			if (request.getExtras() != null) {
				String field = EncryptUtil.md5(request.getUrl());
				String value = JsonUtil.toJson(request);
				jedis.hset((ITEM_PREFIX + task.getUUID()), field, value);
			}

		} finally {
			pool.returnResource(jedis);
		}

	}

	@Override
	public void clear(String uuid) {
		Jedis jedis = pool.getResource();
		try {
			jedis.del(QUEUE_PREFIX + uuid, ITEM_PREFIX + uuid);
		} finally {
			pool.returnResource(jedis);
		}
		filter.clear(uuid);
	}

	@Override
	public void addUrl(String uuid, List<String> url) {
		filter.addUrl(uuid, url);
	}

	@Override
	public boolean isScheduled(String uuid) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.exists(QUEUE_PREFIX + uuid);
		} finally {
			pool.returnResource(jedis);
		}
	}

	@Override
	public boolean isFilted(String uuid) {
		return filter.isFilted(uuid);
	}

	@Override
	public void addSchedulerUrl(String uuid) {
		Jedis jedis = pool.getResource();
		String key = QUEUE_PREFIX + uuid;
		try {
			long length = jedis.llen(key);
			long time = length / 1000 + 1;
			for (int i = 0; i < time; i++) {
				List<String> urls = jedis.lrange(key, i * 1000,
						(i + 1) * 1000 - 1);
				addUrl(uuid, urls);
			}
		} finally {
			pool.returnResource(jedis);
		}

	}

	@Override
	public void addDownloadUrl(String uuid, List<String> url) {
		Jedis jedis = pool.getResource();
		String key = QUEUE_PREFIX + uuid;
		try {
			jedis.lpush(key, CollectionUtil.collection2array(url));
		} finally {
			pool.returnResource(jedis);
		}
	}

	public void push(final String uuid, String... url) {
		Task t = new Task() {

			public String getUUID() {
				return uuid;
			}

			public Site getSite() {
				return null;
			}
		};
		Request req = new Request();
		for (String one : url) {
			req.setUrl(one);
			this.push(req, t);
		}

	}
}
