package cn.vko.common.util;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.vko.core.common.util.FileUtil;

/**
 * 无需自动测试
 * 
 * @author 赵立伟
 * @Date 2013-2-1
 * @version 5.1.0
 */
public class TestFileUtil {

	@Test
	public void testCreateNewFile() {
		File file = new File("sss.txt");
		Assert.assertEquals(true, FileUtil.createNewFile(file));
	}

	@Test(dependsOnMethods = "testCreateNewFile")
	public void testIsExist() {
		Assert.assertEquals(true, FileUtil.isExist("sss.txt"));
	}

	@Test(dependsOnMethods = "testCopyFile")
	public void testDeleteFile() {
		Assert.assertEquals(true, FileUtil.deleteFile("sss.txt"));
	}

	@Test
	public void testMakeDir() {
		String pathname = "new_1bak";
		FileUtil.deleteFile(pathname);
		File file = new File(pathname);
		Assert.assertEquals(true, FileUtil.makeDir(file));
		Assert.assertTrue(FileUtil.isExist(pathname));
	}

	@Test(dependsOnMethods = "testMakeDir")
	public void testGetFileName() {
		String pathName = "new_1bak";
		Assert.assertEquals("new_1bak", FileUtil.getFileName(pathName));
		Assert.assertEquals(true, FileUtil.deleteFile(pathName));
	}
}
