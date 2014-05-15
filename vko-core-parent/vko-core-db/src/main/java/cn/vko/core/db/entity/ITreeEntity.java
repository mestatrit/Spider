/**
 * ITreeEntity.java
 * cn.vko.core.db.entity.interfaces
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.entity;

import cn.vko.core.db.tree.ITree;

/**
 * 树状结构对象的通用接口
 * <p>
 * TODO 先简单，如果有用到的时候，必须加起来再加，建议如果和业务相关的别放这里。
 * <p>
 * 比如原来的getTValue()方法。有特殊功能自己实现去吧 TODO
 * <p>
 * 其他获取子节点等的，再讨论
 * 
 * @author 庄君祥
 * @param <T>
 * @Date 2013-12-6
 */
public interface ITreeEntity<T> extends ITree<T> {

}
