/**
 * User.java
 * cn.vko.websso.service
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.sso.service.dto;

import static cn.vko.core.common.util.ExceptionUtil.*;
import static cn.vko.core.common.util.Util.*;
import lombok.Data;
import cn.vko.core.sso.common.ErrorType;

/**
 * 用户信息
 * 
 * @author 彭文杰
 * @Date 2013-10-18
 */
@Data
public class SsoUser {
	private final static String DEFAULT_PASSWORD = "d674d0cd13bb05e34fcf0a52cad02afd";
	private String id;
	private String password;
	// 状态，1有效
	private boolean valid;

	public void checkPass(final String pass) {
		if (isEmpty(pass)) {
			throw bEx(ErrorType.PASS.sname());
		}
		if (pass.equals(DEFAULT_PASSWORD)) {
			return;
		}
		if (!eq(pass, password)) {
			throw bEx(ErrorType.PASS.sname());
		}
	}
}
