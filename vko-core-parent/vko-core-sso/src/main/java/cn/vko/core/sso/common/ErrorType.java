/**
 * ErrorType.java
 * cn.vko.websso.common
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.sso.common;

import cn.vko.core.common.enums.IEnum;

/**
 * 错误信息
 * 
 * @author 彭文杰
 * @Date 2013-10-18
 */
public enum ErrorType implements IEnum {
	NOTEXIST(1, "用户不存在"), PASS(2, "密码错误"), INVALID(3, "用户被禁用");
	private int key;
	private String value;

	private ErrorType(final int key, final String value) {
		this.value = value;
		this.key = key;
	}

	@Override
	public String key() {
		return String.valueOf(key);
	}

	@Override
	public String sname() {
		return value;
	}
}
