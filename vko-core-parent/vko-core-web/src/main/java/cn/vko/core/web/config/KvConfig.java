/**
 * KvConfig.java
 * cn.vko.web.common.config
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.config;

import java.util.Map;

import lombok.Data;
import cn.vko.core.common.enums.IEnum;

/**
 * keyvalue配置文件，系统统一配置
 * 
 * @author 彭文杰
 * @Date 2013-11-6
 */
@Data
public class KvConfig {
	/**
	 * map结构存储的配置
	 */
	private Map<String, String> values;

	public String getValue(final String key) {
		if (values == null) {
			return null;
		}
		return values.get(key);
	}

	public String getValue(final IEnum key) {
		if (values == null) {
			return null;
		}
		if (key == null) {
			return null;
		}
		return values.get(key.key());
	}
}
