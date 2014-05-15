package cn.vko.business.spider.transfer;

import java.util.List;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.impl.DbDao;

public class TransferTest {
	private IDbDao dbDao;
	private NutDao nutDao;
	private Ioc ioc;

	@Test
	public void testRmEnter() throws Exception {
		Assert.assertEquals(Transfer.rmEnter("bbbaaaa\r\n b<>   <>"),
				"bbbaaaa b<><>");
	}

	@Test
	public void testReplaceMathSign() throws Exception {
		Assert.assertEquals(Transfer.replaceMathSign("A∪B=A，求a+b的值"),
				"A\\cup B=A，求a+b的值");
	}

	@Test
	public void testBug() throws Exception {

		ioc = new NutIoc(new JsonLoader("spider/config/pipeline.js"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
		List<TestContent> testContent = dbDao.query(TestContent.class, null,
				null);
		String s = Transfer.transfer(testContent.get(2).getContent());
		System.out.println(s);
	}
}
