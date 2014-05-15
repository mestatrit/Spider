package cn.vko.common.util;

import static cn.vko.core.common.util.StringUtil.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.testng.annotations.Test;

import cn.vko.core.common.util.ArrayUtil;
import cn.vko.core.common.util.CollectionUtil;

/**
 * @author 宋星明
 * @author 赵立伟
 * @Date 2013-2-1
 * @version 5.2.0
 */
public class TestStringUtil {
	@Test
	public void testFormatStringObjectArray() {
		String testStr = "www.vko.cn?login.html&id={0}";
		assertEquals("www.vko.cn?login.html&id=000", format(testStr, "000"));
	}

	@Test
	public void testFormatLocaleStringObjectArray() {
		String str1 = "www.vko.cn?login.html&id={0}";
		assertEquals("www.vko.cn?login.html&id=111",
				format(Locale.getDefault(), str1, "111"));
	}

	@Test
	public void testJoinObjectCollectionOfT() {
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		assertEquals("a,b", join(",", list));

	}

	@Test
	public void testJoinObjectCollectionOfArray() {
		String[] str = new String[3];
		str[0] = "1";
		str[1] = "2";
		str[2] = "3";
		assertEquals("1,2,3", join(",", str));
	}

	@Test
	public void testClean() throws Exception {
		String str = "ab?!！！中文123１";
		assertEquals("ab中文123", clean(str));
	}

	@Test
	public void testReplace() throws Exception {
		String str = "你是大坏蛋和谐社会";
		String result = replace(str, CollectionUtil.list("坏蛋", "和谐"), '*');
		assertEquals("你是大****社会", result);
	}

	@Test
	public void testSplitLong() {
		assertEquals(0, splitLong("", ",").length);
		String str = "1,2,3";
		assertEquals(ArrayUtil.array("1,2,3"), split(str, "&"));
		long[] result = splitLong(str, ",");
		assertEquals(ArrayUtil.array(1l, 2l, 3l), result);
	}

	@Test
	public void testReplaceAll() throws Exception {
		String str = "abcd中文ac";
		assertEquals("abcda$ac", replaceAll(str, "中文", "a$"));
	}
}
