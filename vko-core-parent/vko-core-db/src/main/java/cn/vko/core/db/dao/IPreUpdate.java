package cn.vko.core.db.dao;

/**
 * 校验是否需要update之前进行校验
 * 
 * @author 彭文杰
 * 
 */
public interface IPreUpdate {
	public void preUpdate(final IDbDao dao);
}
