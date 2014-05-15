package cn.vko.business.spider.model;

import java.util.Map;

public interface IExam {
	/**
	 * 内容
	 * 
	 * @return 内容
	 */
	public String getContent();

	/**
	 * 获取题型
	 * 
	 * @return
	 */
	public String getType();

	public String getSubject();

	public String getLearnPhase();

	public String getKnowledge();

	public String getDiff();

	public String getSource();

	public String getUrl();

	public String getAnswer();

	public String getSolution();

	public Map<String, String> getExtra();

}
