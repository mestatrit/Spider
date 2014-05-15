package cn.xuexibao.business.spider;

import java.io.File;

public class Test2 {

	public static void main(String args[]) {
		String path = "/data/images" + File.separator + "test";
		File save = new File(path);
		if (!save.exists()) {
			save.mkdir();
		}
		System.out.println(save.getName());
		System.out.println(save.getAbsolutePath());
		System.out.println(save.getPath());
	}
}
