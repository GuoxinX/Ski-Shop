package dao.impl;
import java.sql.SQLException;
import java.util.List;

import model.Pic;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.PicDao;

public class PicDaoImpl extends HibernateDaoSupport implements PicDao {

	

	public void deleteBean(Pic bean) {
		this.getHibernateTemplate().delete(bean);
		
	}

	public void insertBean(Pic bean) {
		this.getHibernateTemplate().save(bean);
		
	}

	@SuppressWarnings("unchecked")
	public Pic selectBean(String where) {
		List<Pic> list = this.getHibernateTemplate().find("from Pic "+where);
		if(list.size()==0){
			return null;
		}
		return list.get(0);
	}

	public long selectBeanCount(final String where) {
		long count = (Long)this.getHibernateTemplate().find(" select count(*) from Pic  "+where).get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Pic> selectBeanList(final int start,final int limit,final String where) {
		return (List<Pic>)this.getHibernateTemplate().executeFind(new HibernateCallback<Object>(){

			public Object doInHibernate(final Session session) throws HibernateException, SQLException {
				List<Pic> list = session.createQuery(" from Pic"+where).setFirstResult(start).setMaxResults(limit).list();
				return list;
			}
		});
		
	}

	public void updateBean(Pic bean) {
		this.getHibernateTemplate().update(bean);
		
	}
	
	
	
	
	
	
	
}
