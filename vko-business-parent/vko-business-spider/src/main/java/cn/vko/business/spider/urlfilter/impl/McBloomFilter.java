package cn.vko.business.spider.urlfilter.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import cn.vko.business.spider.urlfilter.IRequestFilter;
import cn.vko.core.common.util.EncryptUtil;
import cn.vko.core.common.util.Util;

public class McBloomFilter implements IRequestFilter, Closeable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(McBloomFilter.class);

	private MemcachedClient mc;
	private boolean isFirst = true;

	public McBloomFilter(MemcachedClient mc) {
		this.mc = mc;
		this.mc.setEnableHeartBeat(false);
		// this.mc.setEnableHealSession(false);
	}

	@Override
	public boolean canPush(Request request, Task task) {
		if (request.getExtra(Request.CYCLE_TRIED_TIMES) != null) {
			return true;
		}
		String trimUrl = getTrimUrl(request.getUrl());
		try {
			if (isFirst) {
				mc.add(task.getUUID(), 0, "100000000|0.001");
				isFirst = false;
			}
			String isSet = mc.get(task.getUUID() + "|" + trimUrl);
			if ("1".equals(isSet)) {
				return false;
			}
			mc.set(task.getUUID(), 0, trimUrl);
			return true;
		} catch (Exception e) {
			logger.error("push" + request.getUrl(), e); //$NON-NLS-1$
		}
		return false;
	}

	@Override
	public void close() throws IOException {
		if (mc != null) {
			mc.shutdown();
		}
	}

	@Override
	public void clear(String uuid) {
		try {
			mc.delete(uuid);
		} catch (Exception e) {
			logger.error("clear", e); //$NON-NLS-1$
		}
	}

	@Override
	public void addUrl(String uuid, List<String> urlList) {
		if (Util.isEmpty(urlList)) {
			return;
		}
		try {
			for (String one : urlList) {
				mc.set(uuid, 0, one);
			}
		} catch (Exception e) {
			logger.error("add url", e); //$NON-NLS-1$
		}
	}

	@Override
	public boolean isFilted(String uuid) {
		try {
			return !mc.add(uuid, 0, "100000000|0.001");
		} catch (Exception e) {
			logger.error("isFilted", e); //$NON-NLS-1$
		}
		return false;
	}

	private String getTrimUrl(String url) {
		if (Util.isEmpty(url)) {
			return "";
		}
		if (url.length() > 210) {
			return EncryptUtil.md5(url);
		}
		return url;
	}
}
