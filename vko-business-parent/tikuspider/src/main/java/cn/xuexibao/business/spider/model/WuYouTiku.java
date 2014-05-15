package cn.xuexibao.business.spider.model;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractBy.Type;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import cn.vko.core.common.util.MapUtil;

@HelpUrl(value = { "http://tiku.ks5u.cn/home.aspx" })
@TargetUrl("http://tiku.ks5u.cn/home.aspx\\?mod=showpaper&ac=base&op=base&PaperId=*")
@Data
public class WuYouTiku implements IExam {
	@ExtractBy(value = ".deContent div:nth-child(2)", type = Type.Css, notNull = true)
	private String content;
	@ExtractBy(value = ".intro table  td:nth-child(1)", type = Type.Css)
	private String type;
	private String learnPhase = "高中";
	@ExtractBy(value = ".dail_right_title", type = Type.Css)
	private String subject;
	// 挪到dealDiff处理
	private String diff;
	@ExtractBy(value = ".intro table  td:nth-child(2)", type = Type.Css)
	private String dealDiff;
	@ExtractBy(value = ".intro ul li:nth-child(3)", type = Type.Css)
	private String knowledge;
	@ExtractBy(value = ".intro ul li:nth-child(1)", type = Type.Css)
	private String source;
	@ExtractByUrl
	private String url;
	private String answer;
	// 答案跟解析放在一起
	@ExtractBy(value = ".deContent div:nth-child(4)", type = Type.Css)
	private String solution;

	public Map<String, String> getExtra() {
		String keyword = "images/x1.jpg";
		if (dealDiff.indexOf(keyword) > -1) {
			int count = 0;
			Pattern p = Pattern.compile(keyword);
			Matcher m = p.matcher(dealDiff);
			while (m.find()) {
				count++;
			}
			dealDiff = count + "星";
		}
		return MapUtil.map("dealDiff", dealDiff);
	}
}
