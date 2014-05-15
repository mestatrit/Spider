/**
 * AbstractCacheCms.java
 * cn.vko.core.cms.interfaces.impl
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.cms.interfaces.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cn.vko.core.cms.ParseUtil;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.MapUtil;
import cn.vko.core.common.util.StringUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.redis.IRedisDao;

/**
 * 使用缓存和数据库进行处理的cms类
 * 
 * @author 彭文杰
 * @Date 2013-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractCacheCms extends AbstractCms {
	/**
	 * 原始的字符串
	 */
	private static final String ORIGIN = "origin";
	/**
	 * 原始的字符串,()
	 */
	public static final String AFTER_CMS = "afterCms";

	protected IRedisDao redis6;
	protected IDbDao dbDao6;

	@Override
	protected void changeMatch(final HttpServletRequest req,
			final Set<String> needDeal, final Map<String, String> result) {
		// 获取每个cms参数的列表
		List<Map<String, String>> props = getPropMap(needDeal);
		// 先从redis中获取
		// loadFromRedis(props, result);
		// 剩余的从db中获取后，进行构造，并塞入缓存
		loadFormDb(props, result);
		// 如果还有未处理的暂时不处理
	}

	protected List<Map<String, String>> getPropMap(final Set<String> needDeal) {
		@SuppressWarnings("unchecked")
		List<Map<String, String>> result = CollectionUtil.list();
		for (String one : needDeal) {
			Map<String, String> props = ParseUtil.getProps(one);
			props.put(ORIGIN, one);
			result.add(props);
		}
		return result;
	}

	public void loadFromRedis(final List<Map<String, String>> tagProps,
			final Map<String, String> result) {
		List<String> keys = CollectionUtil.list();
		for (Map<String, String> one : tagProps) {
			keys.add(getRedisKey(one));
		}
		List<String> re = redis6.mget(CollectionUtil.collection2array(keys));
		for (int i = re.size() - 1; i >= 0; i--) {
			if (re.get(i) == null) {
				continue;
			}
			Map<String, String> remove = tagProps.remove(i);
			result.put(remove.get(ORIGIN), re.get(i));
		}
	}

	protected String getRedisKey(final Map<String, String> map) {
		List<String> keys = CollectionUtil.list("cms", getType());
		if (map.containsKey(UID)) {
			keys.add(map.get(UID));
		}
		if (map.containsKey(SUB_TYPE)) {
			keys.add(map.get(SUB_TYPE));
		}
		return StringUtil.join(":", keys);
	}

	protected void loadFormDb(final List<Map<String, String>> tagProps,
			final Map<String, String> result) {
		loadFormDb(tagProps);
		Map<String, String> needCache = MapUtil.map();
		for (Map<String, String> one : tagProps) {
			String after = "";
			if (one.containsKey(AFTER_CMS)) {
				after = one.get(AFTER_CMS);
			}
			result.put(one.get(ORIGIN), after);
			needCache.put(getRedisKey(one), after);
		}
		redis6.set(needCache);
	}

	/**
	 * 从数据库里面加载数据 处理完毕之后，直接放到tagProps的每个map的AFTER_CMS属性里面
	 * 
	 * @param tagProps
	 *            待处理的数据
	 */
	protected abstract void loadFormDb(final List<Map<String, String>> tagProps);

	@Override
	public void rmCache(final String type, final String id) {
		if (!Util.eq(type, getType())) {
			return;
		}
		List<String> keys = CollectionUtil.list("cms", getType());
		if (!Util.isEmpty(id)) {
			keys.add(id);
		}
		String key = StringUtil.join(":", keys) + "*";
		redis6.removeKeys(key);
	}
}
