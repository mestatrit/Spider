/**
 * TreeUtil.java
 * cn.vko.webbmp.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.tree;

import static org.nutz.lang.Lang.isEmpty;
import static org.nutz.lang.Lang.list;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.vko.core.common.util.MapUtil;

/**
 * 树相关通用工具
 * 
 * @author 彭文杰
 * @Date 2013-11-19
 */
public class Tree6Util {
	/**
	 * 构造新的树结构
	 * 
	 * @param allNodes
	 *            所有的树节点
	 * @return TODO(这里描述每个参数,如果有返回值描述返回值,如果有异常描述异常)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends ITree> List<T> buildTree6(
			final Collection<T> allNodes) {

		List<T> result = list();
		if (isEmpty(allNodes)) {
			return result;
		}
		Map<Long, T> ids = MapUtil.map();
		for (T te : allNodes) {
			ids.put(te.getId(), te);
			te.clearSub();
		}
		for (T te : allNodes) {
			if (te.isFirst() || !ids.containsKey(te.getParentId())) {
				result.add(te);
			} else {
				if (ids.containsKey(te.getParentId())) {
					ids.get(te.getParentId()).addSub(te);
				}
			}
		}
		Collections.sort(result);
		return result;
	}
}
