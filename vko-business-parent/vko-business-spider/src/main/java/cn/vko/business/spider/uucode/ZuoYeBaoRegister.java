package cn.vko.business.spider.uucode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import lombok.Data;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.business.spider.entity.SpiderUser;
import cn.vko.business.spider.util.HttpUtil;
import cn.vko.core.common.util.ExceptionUtil;
import cn.vko.core.common.util.MapUtil;
import cn.vko.core.common.util.PinYinUtil;
import cn.vko.core.common.util.RandomUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;

@Data
public class ZuoYeBaoRegister {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ZuoYeBaoRegister.class);

	private IDbDao dbDao;
	private static final String TOREGISTERURL = "http://www.zuoyebao.com/user/reg";
	private static final String REGISTERURL = "http://www.zuoyebao.com/du/create";
	private static final String IMAGEURL = "http://www.zuoyebao.com/captcha?w=160&h=56&s=32&p=10";
	private static List<String> names = null;

	public void init() {
		UUCodeUtil.init();
		if (!Util.isEmpty(names)) {
			return;
		}
		Sql sql = Sqls.create("select name from names");
		names = DbSqlUtil.queryStringList(dbDao, sql);
	}

	public BasicCookieStore registerOneRepeat() {
		BasicCookieStore cookieStore = null;
		while (cookieStore == null) {
			cookieStore = registerOne();
		}
		return cookieStore;
	}

	public BasicCookieStore registerOne() {
		HttpHost proxy = null;
		BasicCookieStore cookieStore = new BasicCookieStore();
		boolean isOk = process(cookieStore, TOREGISTERURL, proxy);
		if (isOk) {
			return cookieStore;
		}
		return null;
	}

	private boolean process(BasicCookieStore cookieStore, String toRegisterUrl,
			final HttpHost proxy) {
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		CloseableHttpResponse response = null;
		try {
			try {
				HttpUriRequest register = HttpUtil.getUri(toRegisterUrl, null,
						false);
				response = httpclient.execute(register);

			} catch (Exception e) {
				logger.error("to reg error", e); //$NON-NLS-1$
				return false;
			} finally {
				Util.safeClose(response);
			}
			String code = getCode(httpclient);
			if (code == null) {
				return false;
			}
			return reg(httpclient, response, code);

		} finally {
			Util.safeClose(httpclient);
		}
	}

	public static String getCode(CloseableHttpClient httpclient) {
		CloseableHttpResponse response = null;
		try {
			HttpUriRequest register = HttpUtil.getUri(IMAGEURL,
					MapUtil.map("_rnd", getNumChar()), false);
			register.addHeader("Referer", "http://www.zuoyebao.com/user/reg");
			register.addHeader("Accept", "image/webp,*/*;q=0.8");
			response = httpclient.execute(register);
		} catch (Exception e) {
			logger.error("to img error", e); //$NON-NLS-1$
			return null;
		} finally {
			Util.safeClose(response);
		}
		String code = null;
		HttpEntity entity = null;
		InputStream input = null;
		ByteArrayOutputStream output = null;
		try {
			HttpUriRequest register = HttpUtil.getUri(IMAGEURL,
					MapUtil.map("_rnd", getNumChar()), false);
			register.addHeader("Referer", "http://www.zuoyebao.com/user/reg");
			register.addHeader("Accept", "image/webp,*/*;q=0.8");
			logger.info("image url is" + register.getURI());
			response = httpclient.execute(register);
			entity = response.getEntity();
			input = entity.getContent();
			output = new ByteArrayOutputStream();
			byte b[] = new byte[1024];
			int j = 0;
			while ((j = input.read(b)) != -1) {
				output.write(b, 0, j);
			}
			output.flush();
			code = UUCodeUtil.getImageCode(output.toByteArray());
			logger.info("code is" + code);
		} catch (Exception e) {
			logger.error("to img error", e); //$NON-NLS-1$
			return null;
		} finally {
			Streams.safeClose(input);
			Streams.safeClose(output);
			EntityUtils.consumeQuietly(entity);
			Util.safeClose(response);
		}
		return code;
	}

	private boolean reg(CloseableHttpClient httpclient,
			CloseableHttpResponse response, String code) {
		HttpEntity entity;
		try {
			String email = getEmail();
			Map<String, String> param = MapUtil.map();
			param.put("uEmail", email);
			String pw = RandomUtil.randomString(RandomUtil.nextInt(3) + 6);
			param.put("uPasswd", pw);
			param.put("uName", getEmail().split("@")[0]);
			param.put("uCaptcha", code);
			param.put("uAgree", "1");
			HttpUriRequest register = HttpUtil.getUri(REGISTERURL, param, true);
			register.setHeader("Accept",
					"application/json, text/javascript, */*; q=0.01");
			register.setHeader("X-Requested-With", "XMLHttpRequest");
			register.setHeader("Content-Type",
					"application/x-www-form-urlencoded; charset=UTF-8");
			register.setHeader("Referer", "http://www.zuoyebao.com/user/reg");

			logger.info("re url is" + register.getURI() + "email:" + email
					+ ",pw:" + pw);
			response = httpclient.execute(register);
			entity = response.getEntity(); // 返回服务器响应

			String responseString = EntityUtils.toString(entity); // 返回服务器响应的HTML代码
			logger.info("register return is " + responseString);
			int status = response.getStatusLine().getStatusCode();
			if (responseString.indexOf("\"code\":-10") > -1) {
				throw ExceptionUtil.bEx("ip is forbidden！");
			}
			if (status != 200 || responseString.indexOf("\"code\":0") < 0) {
				return false;
			}
			SpiderUser su = new SpiderUser();
			su.setDomain("zuoyebao.com");
			su.setUserName(email);
			su.setUserPw(pw);
			su.setLoginUrl("http://www.zuoyebao.com/du/login");
			su.setIsUse(0);
			dbDao.insert(su);
			return true;
		} catch (ClientProtocolException e) {
			logger.info("reg error");
		} catch (IOException e) {
			logger.info("reg error");
		} catch (URISyntaxException e) {
			logger.info("reg error");
		} finally {
			EntityUtils.consumeQuietly(response.getEntity());
			Util.safeClose(response);
		}
		return false;
	}

	public static String getNumChar() {
		StringBuffer rand = new StringBuffer();
		rand.append("53325");
		rand.append(RandomUtil.randomString(8));
		return rand.toString();
	}

	public static String getEmail() {
		String[] suffix = { "@qq.com", "@163.com", "@126.com", "@gmail.com",
				"@hotmail.com", "@sina.com.cn", "@sohu.com" };
		String[] number = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		String[] en = { "new", "happy", "hello", "love", "fly", "dream", "day",
				"go", "life", "say", "who", "man", "up", "only", "year",
				"long", "show", "home", "world", "eye" };

		StringBuffer emailBuffer = new StringBuffer();
		String emailSuffix = suffix[RandomUtil.nextInt(7)];
		if (emailSuffix.equals("@qq.com")) {
			StringBuffer num = new StringBuffer();
			for (int i = 0; i < RandomUtil.nextInt(3) + 9; i++) {
				num.append(RandomUtil.nextInt(9) + 1);
			}
			emailBuffer.append(num.toString());
		} else {
			int isRandom = RandomUtil.nextInt(10) + 1;
			if (isRandom % 2 == 0) {
				int nextRand = RandomUtil.nextInt(10) + 1;
				if (nextRand % 2 == 0) {
					emailBuffer.append(en[RandomUtil.nextInt(20)]);
				}
				emailBuffer.append(PinYinUtil.getPinyin(names.get(RandomUtil
						.nextInt(names.size()))));

			} else {
				emailBuffer.append(PinYinUtil.getPinyin(names.get(RandomUtil
						.nextInt(names.size()))));
				int nextRand = RandomUtil.nextInt(10) + 1;
				if (nextRand % 2 == 0) {
					for (int j = 0; j < RandomUtil.nextInt(3) + 1; j++) {
						emailBuffer.append(number[RandomUtil.nextInt(10)]);
					}
				}
			}
		}
		emailBuffer.append(emailSuffix);
		return emailBuffer.toString();
	}
}
