/**
 * MockEnum.java
 * cn.vko.common.entity
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.common.entity;

import cn.vko.core.common.enums.IEnum;

/**
 * 虚拟的Enum类
 * 
 * @author 彭文杰
 * @Date 2013-2-20
 */
public enum MockEnum implements IEnum {
	TEST("test", "测试"), MOCK("mock", "模拟");
	private String key;
	private String value;

	private MockEnum(final String key, final String value) {
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
