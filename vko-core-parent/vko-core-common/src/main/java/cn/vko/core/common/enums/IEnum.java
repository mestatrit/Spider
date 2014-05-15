/**
 * IEnum.java
 * cn.vko.common.enums
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.enums;

/**
 * 自己实现的enum接口
 * 
 * @author 彭文杰
 * @Date 2013-1-26
 */
public interface IEnum {
	/**
	 * 获取唯一标示
	 * 
	 * @return 唯一标示
	 */
	public Object key();

	/**
	 * 获取显示值
	 * 
	 * @return 显示值
	 */
	public String sname();
}
