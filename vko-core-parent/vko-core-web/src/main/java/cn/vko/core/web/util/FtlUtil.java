/**
 * FreeMarkerUtil.java
 * cn.vko.core.utils
 * Copyright (c) 2011, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.util;

import static cn.vko.core.common.util.ExceptionUtil.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import lombok.Cleanup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 操作freemarker工具类
 * 
 * @author 彭文杰
 * @Date 2013-11-16
 * @version 5.2.1
 */
public class FtlUtil {
	private static final Logger logger = LoggerFactory.getLogger(FtlUtil.class);

	/**
	 * 初始化配置信息
	 * 
	 * @param context
	 * @param relativePath
	 *            模板相对路径
	 * @param propPath
	 *            配置文件路径
	 */
	public static void initConfiguration(final Configuration cfg,
			final ServletContext context, final String relativePath,
			final String propPath) {
		checkNull(cfg, "初始化freemarker配置时，配置对象为空！");
		if (context == null) {
			throw pEx("初始化freemarker配置时，ServletContext为空！");
		}
		if (relativePath == null) {
			throw pEx("初始化freemarker配置时，文件目录为空！");
		}

		try {
			cfg.setServletContextForTemplateLoading(context, relativePath);
			Properties p = new Properties();
			p.load(new FileInputStream(Thread.currentThread()
					.getContextClassLoader().getResource("").getPath()
					+ propPath));
			cfg.setSettings(p);
		} catch (Exception e) {
			throw pEx("初始化配置时，文件目录" + relativePath + "访问异常！", e);
		}
	}

	/**
	 * 初始化配置信息
	 * 
	 * @param context
	 * @param relativePath
	 *            模板相对路径
	 * @param propPath
	 *            配置文件路径
	 */
	public static void initConfiguration(final Configuration cfg,
			final File folder, final String relativePath, final String propPath) {
		checkNull(cfg, "初始化freemarker配置时，配置对象为空！");
		if (folder == null) {
			throw pEx("初始化freemarker配置时，ServletContext为空！");
		}
		if (relativePath == null) {
			throw pEx("初始化freemarker配置时，文件目录为空！");
		}

		try {
			cfg.setDirectoryForTemplateLoading(folder);
			Properties p = new Properties();
			p.load(new FileInputStream(Thread.currentThread()
					.getContextClassLoader().getResource("").getPath()
					+ propPath));
			cfg.setSettings(p);
		} catch (Exception e) {
			throw pEx("初始化配置时，文件目录" + relativePath + "访问异常！", e);
		}
	}

	/**
	 * 初始化配置信息
	 * 
	 * @param context
	 * @param relativePath
	 *            模板相对路径
	 * @param propPath
	 *            配置文件路径
	 */
	public static void initConfiguration(final Configuration cfg,
			final String folder, final String relativePath,
			final String propPath) {
		File file = new File(Thread.currentThread().getContextClassLoader()
				.getResource("").getPath()
				+ folder);
		initConfiguration(cfg, file, relativePath, propPath);
	}

	/**
	 * 使用模板拼装属性，写入out
	 * 
	 * @param templateName
	 *            模版名称
	 * @param context
	 *            对象map
	 * @param out
	 *            输出的流
	 * @throws IOException
	 *             文件读取错误
	 * @throws TemplateException
	 *             模版解析失败
	 */
	public static void parse(final Configuration cfg,
			final String templateName, final Map<String, Object> context,
			final Writer out) throws IOException, TemplateException {
		/* 获取或创建模板 */
		Template temp = null;
		temp = cfg.getTemplate(templateName);
		temp.process(context, out);
		out.flush();
	}

	/**
	 * 使用模板拼装属性，写入字符串
	 * 
	 * @param templateName
	 *            模版相对路径
	 * @param context
	 *            对象map
	 * @return 返回的字符串
	 */
	public static String build(final Configuration cfg,
			final String templateName, final Map<String, Object> context) {
		try {
			@Cleanup
			StringWriter stringWriter = new StringWriter();
			FtlUtil.parse(cfg, templateName, context, stringWriter);
			return stringWriter.toString();
		} catch (Exception e) {
			logger.error("模版写入流失败，请核查模版与数据是否匹配！", e);
			return "";
		}
	}

	/**
	 * 使用模板拼装属性，写入字符串
	 * 
	 * @param templateName
	 *            模版相对路径
	 * @param context
	 *            对象map
	 * @return 返回的字符串
	 * @exception DataException
	 *                模版写入流失败
	 */
	public static String buildWithEx(final Configuration cfg,
			final String templateName, final Map<String, Object> context) {
		try {
			@Cleanup
			StringWriter stringWriter = new StringWriter();
			FtlUtil.parse(cfg, templateName, context, stringWriter);
			return stringWriter.toString();
		} catch (Exception e) {
			throw pEx("模版写入流失败，请核查模版与数据是否匹配！", e);
		}
	}
}
