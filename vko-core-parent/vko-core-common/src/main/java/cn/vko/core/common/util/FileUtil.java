/**
 * FileUtil.java
 * cn.vko.util
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import static cn.vko.core.common.util.Util.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.nutz.lang.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件工具类
 * 
 * @author 赵立伟
 * @author 彭文杰
 * @Date 2012-11-20
 * @version 5.1.0
 */
public class FileUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(FileUtil.class);

	/**
	 * 校验文件是否存在
	 * 
	 * @param file
	 *            文件路径
	 * @return 存在为真。如果路径为空，则为假
	 */
	public static boolean isExist(final String file) {
		if (isEmpty(file)) {
			return false;
		}
		File f = new File(file);
		return f.exists();
	}

	/**
	 * 创建文件夹
	 * 
	 * @param dir
	 *            文件夹路径
	 * @return 是否创建成功
	 */
	public static boolean makeDir(final File dir) {
		if (null == dir || dir.exists()) {
			return false;
		}
		return dir.mkdirs();
	}

	/**
	 * 创建新文件
	 * 
	 * @param f
	 *            文件路径
	 * @return 是否创建成功
	 */
	public static boolean createNewFile(final File f) {
		if (null == f) {
			return false;
		}
		if (f.exists()) {
			f.delete();
		}
		makeDir(f.getParentFile());
		try {
			return f.createNewFile();
		} catch (IOException e) {
			logger.error("创建文件出错", e);
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 *            文件路径
	 * @return 是否删除成功
	 */
	public static boolean deleteFile(final String file) {
		File f = new File(file);
		return f.delete();
	}

	/**
	 * 获取路径中的文件名
	 * 
	 * @param pathName
	 *            路径
	 * @return 文件名
	 */
	public static String getFileName(final String pathName) {
		if (pathName.lastIndexOf("\\") > -1) {
			return pathName.substring(pathName.lastIndexOf("\\") + 1);
		} else if (pathName.lastIndexOf("/") > -1) {
			return pathName.substring(pathName.lastIndexOf("/") + 1);
		}
		return pathName;
	}

	/**
	 * 获取路径中的路径
	 * 
	 * @param pathName
	 *            路径
	 * @return 路径
	 */
	public static String getFilePath(final String pathName) {
		if (pathName.indexOf(File.separator) > -1) {
			return pathName.substring(0, pathName.lastIndexOf(File.separator));
		}
		return "";
	}

	/**
	 * 考虑到性能，使用该方法创建一个输入流
	 * 
	 * @param f
	 *            File 实例
	 * @return 输入流
	 */
	public static BufferedInputStream bis(final File f) {
		if (f.exists()) {
			try {
				return new BufferedInputStream(new FileInputStream(f));
			} catch (FileNotFoundException e) {
				// never happen!
			}
		}
		return null;
	}

	/**
	 * 考虑到性能，使用该方法创建一个输出流
	 * 
	 * @param f
	 *            File 实例
	 * @return 输出流
	 */
	public static BufferedOutputStream bos(final File f) {
		if (f.exists()) {
			try {
				return new BufferedOutputStream(new FileOutputStream(f));
			} catch (FileNotFoundException e) {
				// never happen!
			}
		}
		return null;
	}

	/**
	 * 获取文件后缀
	 * 
	 * @param file
	 *            文件路径
	 * @return 后缀
	 */
	public static String getSuffix(final String file) {
		if (isEmpty(file)) {
			return "";
		}
		return Files.getSuffixName(file);
	}
}
