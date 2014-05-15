package cn.xuexibao.business.spider.entity;

import lombok.Data;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("spider_user")
@Comment("爬虫抓取的试题")
@Data
public class SpiderUser {
	@Id
	@ColDefine(type = ColType.INT, width = 20, notNull = true)
	@Comment("主键")
	private long id;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 200)
	@Comment("登陆名")
	private String userName;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 32)
	@Comment("登陆密码")
	private String userPw;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 200)
	@Comment("登陆url")
	private String loginUrl;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 20)
	@Comment("所属网站")
	private String domain;
	@Column
	@ColDefine(type = ColType.INT, width = 1)
	@Comment("是否使用")
	private int isUse;
}
