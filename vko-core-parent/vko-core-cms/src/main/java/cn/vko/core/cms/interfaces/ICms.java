/**
 * ICms.java
 * cn.vko.core.cms.interfaces
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.cms.interfaces;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * cms否则进行某类业务转换的接口
 * 
 * @author 彭文杰
 * @Date 2013-12-20
 */
public interface ICms {
	/**
	 * 所有需要转换的字符串进行过滤之后，将能进行转换的进行转换放入map
	 * 
	 * @param cmsTags
	 *            所有的cms标签
	 * @param needChange
	 *            需要转换的map
	 */
	void change(final HttpServletRequest req, final Set<String> cmsTags,
			final Map<String, String> needChange);

	/**
	 * 清除缓存的数据
	 * 
	 * @param type
	 *            数据类别
	 * @param id
	 *            唯一标示
	 */
	void rmCache(final String type, final String id);
}
