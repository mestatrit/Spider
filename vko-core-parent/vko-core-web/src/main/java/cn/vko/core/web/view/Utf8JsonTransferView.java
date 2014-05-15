/**
 * Utf8JsonTransferView.java
 * cn.vko.core.web.view
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.view;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.mvc.view.UTF8JsonView;

import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.web.util.RequestUtil;
import cn.vko.core.web.view.interfaces.IAddTransfer;
import cn.vko.core.web.view.interfaces.IStringTransfer;

/**
 * 自定义可以进行转换的jsonView类
 * 
 * @author 彭文杰
 * @Date 2013-12-24
 */
public class Utf8JsonTransferView extends UTF8JsonView implements IAddTransfer {
	private List<IStringTransfer> transfers = CollectionUtil.list();
	private JsonFormat format;
	private Object data;

	@Override
	public void setData(final Object data) {
		this.data = data;
		super.setData(data);
	}

	public Utf8JsonTransferView(final JsonFormat format) {
		super(format);
		this.format = format;
	}

	@Override
	public void addTransfer(final IStringTransfer transfer) {
		if (transfers == null) {
			transfers = CollectionUtil.list();
		}
		if (transfer == null) {
			return;
		}
		transfers.add(transfer);
	}

	@Override
	public void render(final HttpServletRequest req,
			final HttpServletResponse resp, final Object obj)
			throws IOException {
		if (Util.isEmpty(transfers) && !RequestUtil.isAjaxP(req)) {
			super.render(req, resp, obj);
			return;
		}
		String result = Json.toJson(null == obj ? data : obj, format);
		for (IStringTransfer st : transfers) {
			result = st.transfer(req, resp, result);
		}
		resp.setHeader("Cache-Control", "no-cache");
		resp.setContentType("text/plain");
		result = RequestUtil.toJsonP(req, resp, result, false);
		resp.setContentLength(-1);
		resp.getWriter().write(result);
		resp.flushBuffer();
	}
}
