package dao.impl;
import java.sql.SQLException;
import java.util.List;

import model.Cart;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dao.CartDao;

public class CartDaoImpl extends HibernateDaoSupport implements CartDao {

	

	public void deleteBean(Cart bean) {
		this.getHibernateTemplate().delete(bean);
		
	}

	public void insertBean(Cart bean) {
		this.getHibernateTemplate().save(bean);
		
	}

	@SuppressWarnings("unchecked")
	public Cart selectBean(String where) {
		List<Cart> list = this.getHibernateTemplate().find("from Cart "+where);
		if(list.size()==0){
			return null;
		}
		return list.get(0);
	}

	public long selectBeanCount(final String where) {
		long count = (Long)this.getHibernateTemplate().find(" select count(*) from Cart  "+where).get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Cart> selectBeanList(final int start,final int limit,final String where) {
		return (List<Cart>)this.getHibernateTemplate().executeFind(new HibernateCallback<Object>(){

			public Object doInHibernate(final Session session) throws HibernateException, SQLException {
				List<Cart> list = session.createQuery(" from Cart"+where).setFirstResult(start).setMaxResults(limit).list();
				return list;
			}
		});
		
	}

	public void updateBean(Cart bean) {
		this.getHibernateTemplate().update(bean);
		
	}
	
	
	
	
	
	
	
}
