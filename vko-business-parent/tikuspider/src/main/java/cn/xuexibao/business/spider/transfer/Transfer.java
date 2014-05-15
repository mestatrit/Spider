package cn.xuexibao.business.spider.transfer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.JsonUtil;
import cn.vko.core.common.util.MapUtil;
import cn.vko.core.common.util.StringUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.web.util.FtlUtil;
import freemarker.template.Configuration;

public class Transfer {
	public static final Map<String, String> MATH = MapUtil.map();
	static {
		MATH.put("∞", "\\\\infty ");
		MATH.put("π", "\\\\pi ");
		MATH.put("θ", "\\\\theta ");
		MATH.put("φ", "\\\\varphi ");
		MATH.put("∀", "\\\\forall ");
		MATH.put("∈", "\\\\in ");
		MATH.put("⊥", "\\\\bot ");
		MATH.put("∠", "\\\\angle ");
		MATH.put("∩", "\\\\cap ");
		MATH.put("∪", "\\\\cup ");
		MATH.put("ω", "\\\\omega ");
		MATH.put("△", "\\\\triangle ");
		MATH.put("ρ", "\\\\rho ");
		MATH.put("Γ", "\\\\Gamma ");
		MATH.put("⊙", "\\\\odot ");
		MATH.put("∃", "\\\\exists ");
		MATH.put("α", "\\\\alpha ");
		MATH.put("∁", "\\\\complement ");
		MATH.put("ξ", "\\\\xi ");
		MATH.put("￢", "\\\\neg ");
		MATH.put("∉", "\\\\notin ");
		MATH.put("∅", "\\\\varnothing ");
		MATH.put("λ", "\\\\lambda ");
		MATH.put("μ", "\\\\mu ");
		MATH.put("∧", "\\\\wedge ");
		MATH.put("≥", "\\\\geq ");
		MATH.put("≤", "\\\\leq ");
		MATH.put("°", "\\\\circ ");
		MATH.put("β", "\\\\beta ");
		MATH.put("∽", "\\\\sim ");
		MATH.put("⊆", "\\\\subset ");
		MATH.put("≠", "\\\\neq ");
		MATH.put("⊗", "\\\\otimes ");
		// MATH.put("∨", "∨");这个和v没有区别
		MATH.put("⊕", "\\\\oplus ");
		MATH.put("⊊", "\\\\subsetneqq ");
		MATH.put("η", "\\\\eta ");

	}

	public static String transfer(String s) {
		if (Util.isEmpty(s)) {
			return "";
		}
		Element e = Jsoup.parse(s);
		List<Element> es = e.getAllElements();
		for (Element element : es) {
			if (Util.eq("sup", element.tagName())) {
				element.replaceWith(new TextNode(StringUtil.format(Latex.sup,
						element.text()), ""));
			}
			if (Util.eq("sub", element.tagName())) {
				element.replaceWith(new TextNode(StringUtil.format(Latex.sub,
						element.text()), ""));
			}
		}
		List<Element> needChange = e.select(".MathJye");
		for (Element one : needChange) {
			String latex = Transfer.transfer(one);
			one.replaceWith(new TextNode(latex, ""));
		}
		List<Element> ignores = e.select(".sanwser");
		for (Element ignoreone : ignores) {
			ignoreone.empty();
		}
		return e.text();
	}

	public static String rmEnter(String s) {
		if (Util.isEmpty(s)) {
			return "";
		}
		String result = StringUtil.replaceAll(s, "[\r|\n]", "");
		return StringUtil.replaceAll(result, ">\\s*<", "><");
	}

	public static String replaceMathSign(String s) {
		String result = s;
		for (Entry<String, String> en : MATH.entrySet()) {
			result = StringUtil.replaceAll(result, en.getKey(), en.getValue());
		}
		// System.out.println(result);
		return result;
	}

	public static String getJyeooRightAnswer(String s) {
		if (Util.isEmpty(s)) {
			return "";
		}
		Element e = Jsoup.parse(s);
		List<Element> rights = e.select(".s");
		if (Util.isEmpty(rights)) {
			return "";
		}
		List<String> ra = CollectionUtil.list();
		for (Element one : rights) {
			ra.add(StringUtil.left(one.text(), 1));
		}
		return StringUtil.join(",", ra);
	}

	@SuppressWarnings("unchecked")
	public static String getJyeooKnowledge(String s) {
		if (Util.isEmpty(s)) {
			return "";
		}
		Map<String, String> map = JsonUtil.fromJson(s, Map.class);
		if (Util.isEmpty(map)) {
			return "";
		}
		String know = map.get("know");
		if (Util.isEmpty(know)) {
			return know;
		}
		Element e = Jsoup.parse(know);
		List<Element> knowa = e.select("a");
		List<String> ra = CollectionUtil.list();
		for (Element one : knowa) {
			ra.add(one.text());
		}
		return StringUtil.join(",", ra);
	}

	public static String transfer(Element el) {
		if (el == null) {
			return "";
		}
		if (Util.eq("table", el.tagName())) {
			return table(el);
		}
		if (Util.eq("tr", el.tagName())) {
			return tr(el);
		}
		if (Util.eq("td", el.tagName())) {
			return td(el);
		}
		if (Util.eq("div", el.tagName())) {
			return div(el);
		}
		if (Util.eq("font", el.tagName())) {
			return font(el);
		}
		if (Util.eq("span", el.tagName())) {
			return span(el);
		}
		return getText(el);
	}

	public static String span(Element el) {
		// TODO 需要上下标处理
		return getText(el);
	}

	public static String table(Element el) {
		if (el == null) {
			return "";
		}
		if (el.childNodeSize() == 0) {
			return "";
		}
		Element trParent = el;
		if (el.children().size() > 0 && Util.eq("tbody", el.child(0).tagName())) {
			trParent = el.child(0);
		}
		int trNum = trParent.children().size();
		if (trNum == 0) {
			return "";
		}
		if (trNum == 1) {
			return tr1(trParent);
		}
		if (trNum == 2) {
			return tr2(trParent);
		}
		if (trNum == 3) {
			return tr3(trParent);
		}
		return trOther(trParent);
	}

	private static String getText(Element el) {
		if (el == null) {
			return "";
		}
		if (el.childNodeSize() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Node n : el.childNodes()) {
			if (n instanceof TextNode) {
				sb.append(((TextNode) n).text());
			}
			if (n instanceof Element) {
				sb.append(transfer((Element) n));
			}
		}
		return sb.toString().trim();
	}

	private static String tr1(Element el) {
		Element tr = el.child(0);
		if (tr.children().size() == 0) {
			return "";
		}
		Element td1 = tr.child(0);
		if (td1.children().size() == 1
				&& Util.eq("div", td1.child(0).tagName())) {
			Element div = td1.child(0);
			String hassize = div.attr("hassize");
			if (Util.eq("0", hassize)) {
				return StringUtil.format(Latex.vm, getMatrixPara(tr));
			}
			if (Util.eq("32", hassize)) {
				return StringUtil.format(Latex.pm, getMatrixPara(tr));
			}
			if (Util.eq("2", hassize)) {
				return StringUtil.format(Latex.bm, getMatrixPara(tr));
			}
			if (Util.eq("21", hassize)) {
				return StringUtil.format(Latex.cases, getCasePara(tr));
			}
			if (Util.eq("7", hassize)) {
				return StringUtil.format(Latex.sqrt, getSqrtPara(tr));
			}
		}
		if (Util.eq("∫", td1.text())) {
			return StringUtil.format(Latex.int_, getInt_Para(tr));
		}
		return tr(tr);
	}

	private static String getSqrtPara(Element tr) {
		return td(getChild(tr, 1));
	}

	private static String getMatrixPara(Element tr) {
		Element trParent = getTableBody(tr);
		if (trParent == null) {
			return "错误";
		}
		List<String> trList = CollectionUtil.list();
		List<String> tdList = CollectionUtil.list();
		for (Element inTr : trParent.children()) {
			tdList.clear();
			for (Element inTd : inTr.children()) {
				tdList.add(td(inTd));
			}
			trList.add(StringUtil.join("&", tdList));
		}
		return StringUtil.join("\\\\", trList);
	}

	private static String getCasePara(Element tr) {
		Element trParent = getTableBody(tr);
		if (trParent == null) {
			return "错误";
		}
		List<String> trList = CollectionUtil.list();
		for (Element inTr : trParent.children()) {
			trList.add(tr(inTr));
		}
		return StringUtil.join("\\\\", trList);
	}

	private static Element getTableBody(Element tr) {
		Element td = getChild(tr, 1);
		if (td == null) {
			return null;
		}
		if (td.children().size() != 1
				|| !Util.eq("table", td.child(0).tagName())) {
			return null;
		}
		Element table = td.child(0);
		if (table.children().size() == 0) {
			return null;
		}
		Element trParent = table;
		if (table.children().size() > 0
				&& Util.eq("tbody", table.child(0).tagName())) {
			trParent = table.child(0);
		}
		return trParent;
	}

	private static Object[] getInt_Para(Element tr) {
		Element td = getChild(tr, 1);
		if (td == null) {
			return new String[] { "", "" };
		}
		Element divSup = (Element) Util.first(td
				.select("div[mathtag=msubsup_sup]"));
		Element divSub = (Element) Util.first(td
				.select("div[mathtag=msubsup_sub]"));
		return new String[] { div(divSup), div(divSub) };
	}

	private static String tr2(Element el) {
		Element tr = el.child(0);
		if (tr.children().size() == 0) {
			return "";
		}
		Element td1 = tr.child(0);
		if (td1.attr("style").indexOf(Latex.HTML_DIVIDE) > -1) {
			return StringUtil.format(Latex.divide, tr(tr), tr(getChild(el, 1)));
		}
		if (Util.eq("lim", td1.text())) {
			return StringUtil.format(Latex.lim, getLimPara(getChild(el, 1)));
		}
		if (td1.children().size() == 1
				&& Util.eq("div", td1.child(0).tagName())) {
			Element div = td1.child(0);
			String hassize = div.attr("hassize");
			if (Util.eq("9", hassize)) {
				return StringUtil.format(Latex.arrow, tr(getChild(el, 1)));
			}
		}
		return "错误";
	}

	private static String getLimPara(Element tr2) {
		return tr(tr2);
	}

	private static String tr3(Element el) {
		Element tr2 = el.child(1);
		if (tr2.children().size() == 0) {
			return "错误";
		}
		Element td1 = tr2.child(0);
		Element img = getChild(td1, 0);
		if (img == null) {
			return "错误";
		}
		if (Util.eq(img.attr("src"), Latex.HTML_SUM)) {
			return StringUtil.format(Latex.sum, tr(el.child(2)),
					tr(el.child(0)));
		}
		return "错误";
	}

	private static String trOther(Element el) {
		return "错误";
	}

	public static String div(Element el) {
		return getText(el);
	}

	public static String font(Element el) {
		return getText(el);
	}

	public static String tr(Element el) {
		return getText(el);
	}

	public static String td(Element el) {
		return getText(el);
	}

	private static Element getChild(Element el, int index) {
		if (el.children().size() > index) {
			return el.child(index);
		}
		return null;
	}

	public static String getMangFGDiff(String diff) {
		if (Util.isEmpty(diff)) {
			return "";
		}
		Element e = Jsoup.parse(diff);
		String[] value = e.text().split("：");
		if (value.length == 2) {
			return value[1];
		}
		return e.text();
	}

	public static String getMangFGText(String html) {
		if (Util.isEmpty(html)) {
			return "";
		}
		Element e = Jsoup.parse(html);
		return e.text();
	}

	public static String transStyle(String content, String type,
			Configuration cfg) {
		if (Util.isEmpty(content)) {
			return "";
		}
		if (type.indexOf("单选题") > -1 || type.indexOf("多选题") > -1) {
			Element e = Jsoup.parse(content);
			Elements elements = e.getElementsByTag("td");
			Map<String, Object> map = new HashMap<String, Object>();
			if (elements.size() == 3) {
				map.put("content", elements.get(0).html());
				map.put("options", elements.get(2).html());
			} else {
				return content;
			}
			String transContent = FtlUtil.build(cfg, "jymS.ftl", map);
			if (Util.isEmpty(transContent)) {
				return content;
			}
			return transContent;
		}
		return content;
	}

	public static String getMangFGAnswer(String answer) {
		if (Util.isEmpty(answer)) {
			return "";
		}
		Element e = Jsoup.parse(answer);
		if (e.text().length() > 1) {
			return answer;
		}
		return e.text();
	}

	public static String getMangFGType(String type) {
		if (Util.isEmpty(type)) {
			return "";
		}
		Element e = Jsoup.parse(type);
		String[] value = e.text().split("：");
		if (value.length == 2) {
			return value[1];
		}
		return e.text();
	}
}
