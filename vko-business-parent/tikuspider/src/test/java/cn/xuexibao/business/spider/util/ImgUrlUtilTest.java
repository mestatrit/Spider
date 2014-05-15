package cn.xuexibao.business.spider.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.testng.annotations.Test;

public class ImgUrlUtilTest {

	@Test
	public void testImageUrl() throws Exception {
		List<String> imgUrl = null;
		StringBuffer sb = new StringBuffer();
		try {
			// 创建文件输入流对象
			FileInputStream is = new FileInputStream("d:/test.txt");
			// 设定读取的字节数
			int n = 1024;
			byte buffer[] = new byte[n];
			// 读取输入流

			while ((is.read(buffer, 0, n) != -1) && (n > 0)) {
				sb.append(new String(buffer));
			}
			System.out.println();
			// 关闭输入流
			is.close();
		} catch (IOException ioe) {
			System.out.println(ioe);
		} catch (Exception e) {
			System.out.println(e);
		}
		imgUrl = ImgUrlUtil.getJyeooImgUrl(sb.toString());
		for (String url : imgUrl) {
			System.out.println("----" + url);
		}
	}
}
