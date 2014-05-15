package cn.vko.business.spider.model;

import java.util.Map;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractBy.Type;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import cn.vko.core.common.util.MapUtil;

@TargetUrl("http://www.tizi.com/paper/question/get_question\\?page=*")
@Data
public class TiZi implements IExam {
	@ExtractBy(value = ".*", type = Type.Regex)
	private String content;
	private String type;
	private String learnPhase;
	private String subject;
	private String diff;
	private String knowledge;
	private String know;
	private String source;
	@ExtractByUrl
	private String url;
	private String answer;
	private String solution;

	@Override
	public Map<String, String> getExtra() {
		return MapUtil.map("know", know);
	}
}
