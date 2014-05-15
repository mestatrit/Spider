package cn.xuexibao.business.spider.transfer;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TransferTest {

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

}
