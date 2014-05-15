package cn.xuexibao.business.spider.model;

import java.util.Map;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractBy.Type;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import cn.vko.core.common.util.MapUtil;
import cn.vko.core.common.util.StringUtil;

@TargetUrl("http://www.jyeoo.com/*/ques/detail/*")
@Data
public class JyeooFilter implements IExam {
	@ExtractBy(value = ".pt1", type = Type.Css, notNull = true)
	private String t;
	@ExtractBy(value = ".pt2", type = Type.Css)
	private String q;
	private String type;
	// 科目中包含
	private String learnPhase;
	@ExtractBy(value = ".location a:nth-child(2)", type = Type.Css)
	private String subject;
	// TODO 这个没有
	private String diff;
	// extra中包含
	private String knowledge;
	@ExtractBy(value = ".pt3", type = Type.Css)
	private String know;
	// 从题干里面获取
	private String source;
	@ExtractByUrl
	private String url;
	// 如果是选择，则从题干获取，否则和解析混合
	private String answer;
	@ExtractBy(value = ".pt6", type = Type.Css)
	private String solution;

	@ExtractBy(value = ".pt7", type = Type.Css)
	private String improve;
	@ExtractBy(value = ".pt5", type = Type.Css)
	private String analyse;

	public String getContent() {
		return t + StringUtil.nvl(q);
	}

	public Map<String, String> getExtra() {
		return MapUtil.map("improve", improve, "solution", solution, "know",
				know);
	}
}
