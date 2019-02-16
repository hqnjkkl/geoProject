package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * HuItem2 entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.geoImage.dao.HuItem2
 * @author MyEclipse Persistence Tools
 */

public class HuItem2DAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(HuItem2DAO.class);
	// property constants
	public static final String ITEM_ROUTES = "itemRoutes";
	public static final String ITEM_DOCS = "itemDocs";
	public static final String USER_IDS = "userIds";
	public static final String ITEM_ROUTE_NUMBER = "itemRouteNumber";
	public static final String ITEM_DOC_NUMBER = "itemDocNumber";
	public static final String ITEM_USER_NUMBER = "itemUserNumber";

	public void save(HuItem2 transientInstance) {
		log.debug("saving HuItem2 instance");
		try {
			getSession().save(transientInstance);
			getSession().flush();
			
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(HuItem2 persistentInstance) {
		log.debug("deleting HuItem2 instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HuItem2 findById(java.lang.Integer id) {
		log.debug("getting HuItem2 instance with id: " + id);
		try {
			HuItem2 instance = (HuItem2) getSession().get(
					"com.geoImage.dao.HuItem2", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(HuItem2 instance) {
		log.debug("finding HuItem2 instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.HuItem2")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding HuItem2 instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from HuItem2 as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByItemRoutes(Object itemRoutes) {
		return findByProperty(ITEM_ROUTES, itemRoutes);
	}

	public List findByItemDocs(Object itemDocs) {
		return findByProperty(ITEM_DOCS, itemDocs);
	}

	public List findByUserIds(Object userIds) {
		return findByProperty(USER_IDS, userIds);
	}

	public List findByItemRouteNumber(Object itemRouteNumber) {
		return findByProperty(ITEM_ROUTE_NUMBER, itemRouteNumber);
	}

	public List findByItemDocNumber(Object itemDocNumber) {
		return findByProperty(ITEM_DOC_NUMBER, itemDocNumber);
	}

	public List findByItemUserNumber(Object itemUserNumber) {
		return findByProperty(ITEM_USER_NUMBER, itemUserNumber);
	}

	public List findAll() {
		log.debug("finding all HuItem2 instances");
		try {
			String queryString = "from HuItem2";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public HuItem2 merge(HuItem2 detachedInstance) {
		log.debug("merging HuItem2 instance");
		try {
			HuItem2 result = (HuItem2) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(HuItem2 instance) {
		log.debug("attaching dirty HuItem2 instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HuItem2 instance) {
		log.debug("attaching clean HuItem2 instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}