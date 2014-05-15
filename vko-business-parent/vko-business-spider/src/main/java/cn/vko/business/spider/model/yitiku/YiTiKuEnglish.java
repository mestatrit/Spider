package cn.vko.business.spider.model.yitiku;

import java.util.Map;

import lombok.Data;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractBy.Type;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import cn.vko.business.spider.model.IExam;
import cn.vko.core.common.util.MapUtil;

@HelpUrl(value = { "http://www.yitiku.cn/tiku/yingyu/",
		"http://www.yitiku.cn/tiku/yingyu/[0-9]*" })
@TargetUrl("http://www.yitiku.cn/shiti/*")
@Data
public class YiTiKuEnglish implements IExam {
	@ExtractBy(value = ".box1000 .txt .quesdiv", type = Type.Css, notNull = true)
	private String content;
	@ExtractBy(value = ".box1000 .detailsTxt:nth-child(1) h3", type = Type.Css)
	private String type;
	@ExtractBy(value = ".full03 .path a:nth-child(2)", type = Type.Css)
	private String learnPhase;
	// 科目跟 学段放在一起
	private String subject;
	@ExtractBy(value = ".detailsTxt .handle div u:nth-child(1)", type = Type.Css)
	private String diff;
	private String knowledge;
	@ExtractBy(value = ".detailsTxt .quesTxt2 .fl", type = Type.Css)
	private String know;
	// 试题来源
	private String source;
	@ExtractByUrl
	private String url;
	@ExtractBy(value = ".detailsTxt .quesTxt2 li:nth-child(2) .editorBox", type = Type.Css, notNull = true)
	private String answer;
	@ExtractBy(value = ".detailsTxt .quesTxt2 li:nth-child(1) .editorBox", type = Type.Css, notNull = true)
	private String solution;

	@Override
	public Map<String, String> getExtra() {
		return MapUtil.map("know", know);
	}
}
