package cn.xuexibao.business.spider.entity;

import java.sql.Timestamp;

import lombok.Data;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("spider_rerouter")
@Comment("爬虫重启路由器")
@Data
public class SpiderReRouter {
	@Id
	@ColDefine(type = ColType.INT, width = 20, notNull = true)
	@Comment("主键")
	private long id;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 200)
	@Comment("域名")
	private String domain;
	@Column
	@Comment("创建时间")
	@ColDefine(type = ColType.DATETIME, notNull = true)
	private Timestamp crTime;
}
