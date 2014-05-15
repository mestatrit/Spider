package cn.vko.core.db.dao;

/**
 * 校验是否需要insert之前进行校验
 * 
 * @author 彭文杰
 * 
 */
public interface IPreInsert {
	public void preInsert(final IDbDao dao);
}
