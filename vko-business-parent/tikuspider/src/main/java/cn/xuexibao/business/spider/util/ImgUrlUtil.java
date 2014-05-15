package cn.xuexibao.business.spider.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.vko.core.common.util.CollectionUtil;

public class ImgUrlUtil {
	public static List<String> getJyeooImgUrl(String html) {
		List<String> imgUrls = CollectionUtil.list();
		// String regEx_img =
		// "http://img.jyeoo.net/.*/\\w+.(jpeg|png|wmf|jpg|bmp)";
		// http://img.jyeoo.net/images/part/P.png'
		String regEx_img = "http://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		Pattern pattern = Pattern.compile(regEx_img);
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			if (!imgUrls.contains(matcher.group())) {
				imgUrls.add(matcher.group());
			}
		}
		return imgUrls;
	}

	public static List<String> getImgUrl(String html) {
		List<String> imgUrls = CollectionUtil.list();
		String regEx_img = "<img[^<>]*src=[\"']([^\"'<>]*)[\"']";
		// String regEx_img = "<img.*src=(.*?)[^>]*?>";
		Pattern pattern = Pattern.compile(regEx_img);
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			imgUrls.add(matcher.group(1));
		}
		return imgUrls;
	}
}
