package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * UserItem entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.geoImage.dao.UserItem
 * @author MyEclipse Persistence Tools
 */

public class UserItemDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UserItemDAO.class);
	// property constants
	public static final String SIMILARITY = "similarity";

	public void save(UserItem transientInstance) {
		log.debug("saving UserItem instance");
		try {
			getSession().save(transientInstance);
			getSession().flush();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(UserItem persistentInstance) {
		log.debug("deleting UserItem instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UserItem findById(com.geoImage.dao.UserItemId id) {
		log.debug("getting UserItem instance with id: " + id);
		try {
			UserItem instance = (UserItem) getSession().get(
					"com.geoImage.dao.UserItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(UserItem instance) {
		log.debug("finding UserItem instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.UserItem")
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
		log.debug("finding UserItem instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from UserItem as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySimilarity(Object similarity) {
		return findByProperty(SIMILARITY, similarity);
	}

	public List findAll() {
		log.debug("finding all UserItem instances");
		try {
			String queryString = "from UserItem";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public UserItem merge(UserItem detachedInstance) {
		log.debug("merging UserItem instance");
		try {
			UserItem result = (UserItem) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(UserItem instance) {
		log.debug("attaching dirty UserItem instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UserItem instance) {
		log.debug("attaching clean UserItem instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}