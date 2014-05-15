/**
 * LoginView.java
 * cn.vko.core.web.view
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Data;

import org.nutz.json.JsonFormat;
import org.nutz.mvc.View;

import cn.vko.core.web.chain.support.JsonResult;
import cn.vko.core.web.util.RequestUtil;

/**
 * 自定义登录视图
 * 
 * @author 庄君祥
 * @Date 2013-12-24
 */
@Data
public class LoginView implements View {
	private String loginUrl;

	public LoginView(final String loginUrl) {
		this.loginUrl = loginUrl;
	}

	@Override
	public void render(final HttpServletRequest req,
			final HttpServletResponse resp, final Object obj) throws Exception {
		if (!RequestUtil.isAjax(req)) {
			Throwable th = null;
			if (obj instanceof Throwable) {
				th = (Throwable) obj;
			}
			RequestUtil.redirectTo(req, resp, loginUrl, th);
			return;
		}
		Utf8JsonTransferView utv = new Utf8JsonTransferView(
				JsonFormat.compact());
		utv.render(req, resp, JsonResult.timeOut(""));
	}
}
