package cn.xuexibao.business.spider.proxy;

import org.apache.http.HttpHost;

public interface IProxyQueue {
	public HttpHost popHost();
}
