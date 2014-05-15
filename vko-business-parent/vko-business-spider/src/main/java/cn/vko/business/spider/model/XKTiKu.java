package cn.vko.business.spider.model;

import java.util.Map;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractBy.Type;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@HelpUrl(value = { "http://tiku.zujuan.com/Search.aspx\\?bi=*&kw=0&pg=*" })
@TargetUrl("http://tiku.zujuan.com/SoftInfos*.html")
@Data
public class XKTiKu implements IExam {
	@ExtractBy(value = ".info_left div:nth-child(3)", type = Type.Css, notNull = true)
	private String content;
	private String type;
	private String learnPhase;
	private String subject;
	private String diff;
	private String knowledge;
	@ExtractBy(value = ".info_left_top strong", type = Type.Css)
	private String source;
	@ExtractByUrl
	private String url;
	// @ExtractBy(value = ".info_left div:nth-child(5)", type = Type.Css)
	// <img
	// src="http://image.zujuan.com/Img.aspx?bankid=12&quesid=320529&quespart=1&queswidth=640">
	private String answer;
	// @ExtractBy(value = ".info_left div:nth-child(7)", type = Type.Css)
	// 格式如下 如果找不到<img
	// src="http://image.zujuan.com/Img.aspx?bankid=15&quesid=1695671&quespart=2&queswidth=640">
	// 则为省略
	private String solution;

	@Override
	public Map<String, String> getExtra() {
		return null;
	}
}
