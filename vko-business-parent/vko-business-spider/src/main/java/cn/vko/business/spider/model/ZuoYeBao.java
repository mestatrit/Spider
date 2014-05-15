package cn.vko.business.spider.model;

import java.util.Map;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractBy.Type;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import cn.vko.core.common.util.MapUtil;

@HelpUrl(value = { "http://www.zuoyebao.com/s*" })
@TargetUrl("http://www.zuoyebao.com/q/*")
@Data
public class ZuoYeBao implements IExam {
	@ExtractBy(value = "#qBodySec", type = Type.Css, notNull = true)
	private String content;
	@ExtractBy(value = "#qBodySec .title", type = Type.Css)
	private String type;
	// 根据tag进行处理
	private String learnPhase;
	// 根据tag进行处理
	private String subject;
	// TODO 这个没有
	private String diff;
	private String knowledge;
	// 根据tag进行处理
	@ExtractBy(value = ".q-sec", type = Type.Css)
	private String tag;
	// 根据knowledge进行处理
	private String source;
	@ExtractByUrl
	private String url;
	@ExtractBy(value = ".q-view-sec:nth-child(2)", type = Type.Css)
	private String answer;
	@ExtractBy(value = ".q-view-sec:nth-child(3)", type = Type.Css)
	private String solution;

	@Override
	public Map<String, String> getExtra() {
		return MapUtil.map("tag", tag);
	}
}
