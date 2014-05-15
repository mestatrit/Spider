/**
 * InitUtil.java
 * cn.vko.web.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.util;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.core.web.config.ConfigKey;
import cn.vko.core.web.config.KvConfig;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

/**
 * 初始化工具类
 * 
 * @author 彭文杰
 * @Date 2013-11-16
 */
public final class InitUtil {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(InitUtil.class);

	public static final String GLOBAL_CONFIG_KEY = "vgc";

	private InitUtil() {

	}

	/**
	 * 初始化web全局的配置
	 * 
	 * @param config
	 *            配置
	 * @param sc
	 *            全局上下文
	 */
	public static void initWebGlobalConfig(final KvConfig config,
			final ServletContext sc) {
		if (config == null || sc == null) {
			return;
		}
		sc.setAttribute(GLOBAL_CONFIG_KEY, config.getValues());
		sc.setAttribute(ConfigKey.STATIC.key(),
				config.getValue(ConfigKey.STATIC));
	}

	/**
	 * 初始化ftl全局的配置
	 * 
	 * @param config
	 *            配置
	 * @param ftlConf
	 *            ftl的配置
	 */
	public static void initFtlGlobalConfig(final KvConfig config,
			final Configuration ftlConf) {
		if (config == null || ftlConf == null) {
			return;
		}
		try {
			ftlConf.setSharedVariable(GLOBAL_CONFIG_KEY, config.getValues());
			ftlConf.setSharedVariable(ConfigKey.STATIC.key(),
					config.getValue(ConfigKey.STATIC));
		} catch (TemplateModelException e) {
			logger.error("设置ftl公用属性时出错！", e); //$NON-NLS-1$
		}
	}
}
