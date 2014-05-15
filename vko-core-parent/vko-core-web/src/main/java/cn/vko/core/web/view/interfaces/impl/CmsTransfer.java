/**
 * CmsTransfer.java
 * cn.vko.core.web.view.interfaces.impl
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.view.interfaces.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.EqualsAndHashCode;

import org.nutz.mvc.View;

import cn.vko.core.cms.Cms;

/**
 * cms引擎进行字符串转换
 * 
 * @author 彭文杰
 * @Date 2013-12-24
 */
@EqualsAndHashCode(callSuper = true)
public class CmsTransfer extends AbstractTransfer {
	private Cms cms;

	private CmsTransfer() {
	}

	@Override
	public String transfer(final HttpServletRequest req,
			final HttpServletResponse resp, final String str) {
		return cms.parse(req, str);
	}

	@Override
	public void warpper(final String type, final View view) {
		add("c", type, view);
	}

	public CmsTransfer setCms(final Cms cms) {
		this.cms = cms;
		return this;
	}

	public Cms getCms() {
		return cms;
	}

	public static CmsTransfer me() {
		return new CmsTransfer();
	}
}
