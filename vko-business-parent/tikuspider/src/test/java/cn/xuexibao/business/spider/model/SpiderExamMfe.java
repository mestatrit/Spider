package cn.xuexibao.business.spider.model;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import cn.xuexibao.business.spider.transfer.model.BaseSpiderExam;

@Table("spider_exam_mfe")
@Comment("爬虫抓取的魔方格英语试题")
public class SpiderExamMfe extends BaseSpiderExam {

}
