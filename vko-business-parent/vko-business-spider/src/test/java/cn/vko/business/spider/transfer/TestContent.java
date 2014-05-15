package cn.vko.business.spider.transfer;

import lombok.Data;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("test_content")
public class TestContent {
	@ColDefine(type = ColType.TEXT)
	@Comment("试题内容")
	private String content;
}
