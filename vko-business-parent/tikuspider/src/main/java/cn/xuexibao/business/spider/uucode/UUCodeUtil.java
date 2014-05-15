package cn.xuexibao.business.spider.uucode;

import java.io.IOException;

import cn.vko.core.common.util.ExceptionUtil;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class UUCodeUtil {
	public static String USERNAME = "yxlxxx"; // UU用户名
	public static String PASSWORD = "uuuuuu"; // UU密码
	public static String WINDLLPATH = "d:/UUWiseHelper.dll"; // DLL
	public static String LINUXDLLPATH = "/usr/lib/UUWiseHelper.so"; // DLL
	public static int SOFTID = 96505; // 软件ID
	public static String SOFTKEY = "ab626b19daae486c94e71fefac4b4551"; // 软件KEY
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isLinux() {
		return OS.indexOf("linux") >= 0;
	}

	public static String getPath() {
		if (isLinux()) {
			return LINUXDLLPATH;
		} else {
			return WINDLLPATH;
		}
	}

	public static interface DM extends Library {

		DM INSTANCE = (DM) Native.loadLibrary(getPath(), DM.class);

		public int uu_reportError(int id);

		public int uu_setTimeOut(int nTimeOut);

		public void uu_setSoftInfoA(int softId, String softKey);

		public int uu_loginA(String UserName, String passWord);

		public int uu_getScoreA(String UserName, String passWord);

		public int uu_recognizeByCodeTypeAndBytesA(byte[] picContent,
				int piclen, int codeType, byte[] returnResult);

		public void uu_getResultA(int nCodeID, String pCodeResult);

	}

	public static void init() {
		// setsoftinfo和login函数只需要执行一次，就可以无限执行图片识别函数了
		DM.INSTANCE.uu_setSoftInfoA(SOFTID, SOFTKEY);
		System.out.println("finish set soft info ");
		int userId = DM.INSTANCE.uu_loginA(USERNAME, PASSWORD);
		System.out.println("finish login ");
		if (userId < 0) {
			throw ExceptionUtil.bEx("login error"); // 错误代码请对应dll.uuwise.com各函数值查看
		}
	}

	public static synchronized String getImageCode(byte[] imageBytes)
			throws IOException {
		byte[] resultBtye = new byte[30]; // 为识别结果申请内存空间
		DM.INSTANCE.uu_recognizeByCodeTypeAndBytesA(imageBytes,
				imageBytes.length, 1, resultBtye); // 调用识别函数,resultBtye为识别结果
		String resultResult = new String(resultBtye, "UTF-8");
		resultResult = resultResult.trim();
		return resultResult;
	}

}
