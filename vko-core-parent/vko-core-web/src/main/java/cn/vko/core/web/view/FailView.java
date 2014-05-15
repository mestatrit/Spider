/**
 * FailView.java
 * cn.vko.core.web.view
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.view;

import static cn.vko.core.common.util.ExceptionUtil.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Data;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.mvc.View;

import cn.vko.core.web.chain.FailProcessor;
import cn.vko.core.web.chain.support.JsonResult;
import cn.vko.core.web.util.RequestUtil;

/**
 * 自定义失败视图
 * 
 * @author 庄君祥
 * @Date 2013-12-24
 */
@Data
public class FailView implements View {

	public FailView() {

	}

	@Override
	public void render(final HttpServletRequest req,
			final HttpServletResponse resp, final Object obj) throws Exception {
		if (!RequestUtil.isAjax(req)) {
			RequestDispatcher rd = req
					.getRequestDispatcher("/WEB-INF/share/back.jsp");
			if (obj instanceof Throwable) {
				req.setAttribute(FailProcessor.REQ_ERROR_KEY,
						getSimpleMessage((Throwable) obj));
			}
			rd.forward(req, resp);
			return;
		}
		Utf8JsonTransferView utv = new Utf8JsonTransferView(
				JsonFormat.compact());
		if (obj instanceof Throwable) {
			utv.render(req, resp, JsonResult.toJsonMap((Throwable) obj));
		} else {
			utv.render(req, resp, Json.toJson(obj));
		}
	}
}
