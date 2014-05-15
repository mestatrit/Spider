/**
 * SsoLog.java
 * cn.vko.sso.service.entity
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.sso.service.entity;

import java.sql.Timestamp;

import lombok.Data;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import cn.vko.core.common.enums.IEnum;
import cn.vko.core.common.util.DateTimeUtil;

/**
 * 单点登录系统日志
 * 
 * @author 彭文杰
 * @Date 2013-11-25
 */
@Data
@Table("sso_log")
@Comment("单点登录系统日志")
public class SsoLog {
	public static enum LogType implements IEnum {
		LOGIN(1, "登录"), LOGOUT(2, "退出"), TIMEOUT(3, "超时退出");
		private int key;
		private String value;

		private LogType(final int key, final String value) {
			this.value = value;
			this.key = key;
		}

		@Override
		public String key() {
			return String.valueOf(key);
		}

		public int getKeyInt() {
			return key;
		}

		@Override
		public String sname() {
			return value;
		}
	}

	@Id(auto = true)
	@ColDefine(type = ColType.INT, width = 20, notNull = true)
	@Comment("主键")
	private long id;
	@Column
	@ColDefine(type = ColType.INT, width = 1, notNull = true)
	@Comment("sso类别：1 网站 2管理后台 ")
	private int ssoType;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 50, notNull = true)
	@Comment("用户的主键")
	private String userId;
	@Column
	@ColDefine(type = ColType.INT, width = 20, notNull = true)
	@Comment("记录的类别：")
	private int logType;
	@Column
	@ColDefine(type = ColType.DATETIME, notNull = true)
	@Comment("记录时间")
	private Timestamp logTime = DateTimeUtil.nowDateTime();
}
