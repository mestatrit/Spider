/**
 * DbMockEntity.java
 * cn.vko.core.db.mock.entity
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.mock.entity;

import lombok.Data;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import cn.vko.core.db.entity.IValidEntity;

/**
 * 数据库mock实体
 * 
 * @author 庄君祥
 * @Date 2013-12-10
 */
@Table("mock_entity_4_valid")
@Comment("数据库mock实体")
@Data
public class MockEntity4Valid implements IValidEntity {
	@Column
	@ColDefine(type = ColType.INT, width = 20, notNull = true)
	@Comment("主键")
	@Id
	private long id;
	@Column
	@ColDefine(type = ColType.BOOLEAN, notNull = true)
	@Comment("状态")
	private boolean valid = true;

	public MockEntity4Valid() {
	}

}
