/**
 * FailProcessor.java
 * cn.vko.web.common.chain
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.chain;

import static cn.vko.core.common.util.ExceptionUtil.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.impl.processor.ViewProcessor;

import cn.vko.core.common.exception.IBusinessException;
import cn.vko.core.common.exception.IParamException;
import cn.vko.core.common.exception.ITimeoutException;
import cn.vko.core.web.view.FailView;
import cn.vko.core.web.view.LoginView;

/**
 * web项目统一的异常处理
 * 
 * @author 彭文杰
 * @Date 2012-4-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FailProcessor extends ViewProcessor {
	/**
	 * request存放错误信息的key
	 */
	public static final String REQ_ERROR_KEY = "REQ_ERROR_KEY";

	@Override
	public void init(final NutConfig config, final ActionInfo ai)
			throws Throwable {
		view = evalView(config, ai, ai.getFailView());
	}

	/**
	 * 处理错误信息
	 * <p>
	 * 首先根据错误类别对错误进行日志记录 根据错误view的方式，返回错误信息
	 * 
	 * @see org.nutz.mvc.impl.processor.ViewProcessor#process(org.nutz.mvc.ActionContext)
	 */
	@Override
	public void process(final ActionContext ac) throws Throwable {
		Throwable th = ac.getError();
		handleError(ac, th);
		if (th instanceof ITimeoutException) {
			new LoginView(getLoginUrl()).render(ac.getRequest(),
					ac.getResponse(), th);
			return;
		}
		if (view instanceof FailView) {
			view.render(ac.getRequest(), ac.getResponse(), th);
			return;
		}
		if (th instanceof IBusinessException || th instanceof IParamException) {
			ac.getRequest().setAttribute(REQ_ERROR_KEY, getSimpleMessage(th));
			super.process(ac);
			return;
		}
		new FailView().render(ac.getRequest(), ac.getResponse(), th);
	}

	/**
	 * 错误处理逻辑
	 * 
	 * @param ac
	 *            请求的上下文
	 * @param th
	 *            异常信息
	 */
	protected void handleError(final ActionContext ac, final Throwable th) {
	}

	/**
	 * 获取登录的页面路径 如果是TimeOutExpetion，需要进行页面转向
	 * 
	 * @return 路径
	 */
	protected String getLoginUrl() {
		return "";
	}
}
