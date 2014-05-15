package cn.vko.core.db.dao;

/**
 * 校验是否需要del之前进行校验
 * 
 * @author 彭文杰
 * 
 */
public interface IPreDelete {
	public void preDelete(final IDbDao dao);
}
