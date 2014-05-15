/**
 * JsonResult.java
 * cn.vko.chain.support
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.chain.support;

import static cn.vko.core.common.util.MapUtil.*;
import static cn.vko.core.common.util.Util.*;

import java.util.Map;

import cn.vko.core.common.enums.IEnum;
import cn.vko.core.common.exception.ITimeoutException;
import cn.vko.core.common.util.ExceptionUtil;

/**
 * 同dwz框架配合的返回对象
 * 
 * @author 何青
 * @author 彭文杰
 * @author 徐世超
 * @author 赵立伟
 * @Date 2012-4-25
 */

public class JsonResult {
	/**
	 * 同dwz框架配合的状态返回值
	 * <p>
	 * SUCCESS 成功 FAIL 失败 TIMEOUT 用户登录信息超时
	 * 
	 * @author 彭文杰
	 * @Date 2012-5-1
	 */
	public static enum StatusCode implements IEnum {
		SUCCESS("200", "成功"), FAIL("300", "失败"), TIMEOUT("301", "超时");
		private String key;
		private String value;

		private StatusCode(final String key, final String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public String sname() {
			return value;
		}
	}

	/**
	 * 同dwz框架配合的返回处理方式值
	 * <p>
	 * CLOSE 关闭当前tab FORWARD 转向新的url
	 * 
	 * @author 彭文杰
	 * @Date 2012-5-1
	 */
	public static enum CallbackType implements IEnum {
		CLOSE("closeCurrent", "关闭当前"), FORWARD("forward", "转向");
		private String value;
		private String key;

		private CallbackType(final String key, final String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String key() {
			return key;
		}

		@Override
		public String sname() {
			return value;
		}
	}

	/**
	 * 构建成功后的返回对象
	 * <p>
	 * 消息为空时，不提示，不为空则进行提示
	 * 
	 * @param message
	 *            成功提示消息
	 * @return json对象
	 */
	public static Map<String, String> success(final String message) {
		return success(message, null, false);
	}

	/**
	 * 构建成功后的返回对象
	 * <p>
	 * 消息为空时，不提示，不为空则进行提示
	 * 
	 * @param message
	 *            成功提示消息
	 * @param navTabId
	 *            需要刷新的tab
	 * @return json对象
	 */

	public static Map<String, String> success(final String message,
			final String navTabId) {
		return success(message, navTabId, true);
	}

	/**
	 * 构建成功后的返回对象
	 * <p>
	 * 消息为空时，不提示，不为空则进行提示
	 * 
	 * @param message
	 *            成功提示消息
	 * @param rel
	 *            需要局部刷新的div
	 * @return json对象
	 */

	public static Map<String, String> successRel(final String message,
			final String rel) {
		return success(message, null, rel, false);
	}

	/**
	 * 构建成功后的返回对象
	 * 
	 * @param message
	 *            成功消息
	 * @param navTabId
	 *            待刷新的navTabId
	 * @param isClose
	 *            是否关闭当前tab
	 * @return json对象
	 */
	public static Map<String, String> success(final String message,
			final String navTabId, final boolean isClose) {
		return success(message, navTabId, null, isClose);
	}

	/**
	 * 构建成功后的返回对象
	 * <p>
	 * 消息为空时，不提示，不为空则进行提示
	 * 
	 * @param message
	 *            成功消息
	 * @param navTabId
	 *            待刷新的navTabId
	 * @param rel
	 *            需要局部刷新的div
	 * @param isClose
	 *            是否关闭当前tab
	 * @return json对象
	 */
	public static Map<String, String> success(final String message,
			final String navTabId, final String rel, final boolean isClose) {
		Map<String, String> map = map();
		map.put("statusCode", StatusCode.SUCCESS.key());
		map.put("message", message);
		if (!isEmpty(navTabId)) {
			map.put("navTabId", navTabId);
		}
		if (!isEmpty(rel)) {
			map.put("rel", rel);
		}
		if (isClose) {
			map.put("callbackType", CallbackType.CLOSE.key());
		}
		return map;
	}

	/**
	 * 构建超时返回json对象
	 * 
	 * @return 超时json对象
	 */
	public static Map<String, String> timeOut(final String message) {
		Map<String, String> map = map();
		map.put("statusCode", StatusCode.TIMEOUT.key());
		if (isEmpty(message)) {
			map.put("message", "您离开的时间过长，请重新登陆！");
		} else {
			map.put("message", message);
		}
		return map;
	}

	/**
	 * 构建错误json对象
	 * 
	 * @param message
	 *            错误提示信息
	 * @return 错误json对象
	 */
	public static Map<String, String> error(final String message) {
		Map<String, String> map = map();
		map.put("statusCode", StatusCode.FAIL.key());
		map.put("message", message);
		return map;
	}

	/**
	 * 返回错误信息
	 * <p>
	 * 返回错误信息，并根据情况确定是否需要关闭窗口
	 * 
	 * @param message
	 * @param isClose
	 * @return 错误json对象
	 */
	public static Map<String, String> error(final String message,
			final boolean isClose) {
		Map<String, String> map = map();
		map.put("statusCode", StatusCode.FAIL.key());
		map.put("message", message);
		if (isClose) {
			map.put("callbackType", CallbackType.CLOSE.key());
		}
		return map;
	}

	/**
	 * 将异常转换为错误信息
	 * 
	 * @param th
	 *            异常
	 * @return 错误信息json对象
	 */
	public static Map<String, String> toJsonMap(final Throwable th) {
		if (th instanceof ITimeoutException) {
			return JsonResult.timeOut(th.getMessage());
		}
		return JsonResult.error(ExceptionUtil.getSimpleMessage(th));
	}
}
