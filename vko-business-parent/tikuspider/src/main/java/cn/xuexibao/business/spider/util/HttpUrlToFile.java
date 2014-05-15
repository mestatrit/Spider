package cn.xuexibao.business.spider.util;

import cn.vko.core.common.util.StringUtil;
import cn.vko.core.common.util.Util;

public class HttpUrlToFile implements IUrlToFile {

	public String getFile(String url) {
		if (Util.isEmpty(url)) {
			return "";
		}
		String file = url;
		file = StringUtil.removeLeft(file, "http://");
		return StringUtil.subLastRight(file, "/");
	}

}
