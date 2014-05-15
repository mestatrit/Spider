package cn.vko.business.spider.model;

import java.util.Map;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractBy.Type;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@HelpUrl("http://www.mofangge.com/qlist/*")
@TargetUrl("http://www.mofangge.com/html/qDetail/*")
@Data
public class MoFangGe implements IExam {
	@ExtractBy(value = "div.q_mokuai:nth-child(2) table", type = Type.Css, notNull = true)
	private String content;
	@ExtractBy(value = "div.q_mokuai:nth-child(2) .right span:nth-child(1)", type = Type.Css)
	private String type;
	@ExtractByUrl("http://www.mofangge.com/html/qDetail/\\d+/(\\w+)/*")
	private String learnPhase;
	@ExtractByUrl("http://www.mofangge.com/html/qDetail/(\\d+)/*")
	private String subject;
	@ExtractBy(value = "div.q_mokuai:nth-child(2) .right span:nth-child(2)", type = Type.Css)
	private String diff;
	@ExtractBy(value = "#seclist", type = Type.Css)
	private String knowledge;
	@ExtractBy(value = "div.q_mokuai:nth-child(2) .right span:nth-child(3)", type = Type.Css)
	private String source;
	@ExtractByUrl
	private String url;
	@ExtractBy(value = "div.q_mokuai:nth-child(3) table", type = Type.Css, notNull = true)
	private String answer;
	// TODO solution和答案存一起,根据答案进行处理
	private String solution;

	@Override
	public Map<String, String> getExtra() {
		return null;
	}
}
