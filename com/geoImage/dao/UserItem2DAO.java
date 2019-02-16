package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * UserItem2 entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.geoImage.dao.UserItem2
 * @author MyEclipse Persistence Tools
 */

public class UserItem2DAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UserItem2DAO.class);
	// property constants
	public static final String SIMILARITY = "similarity";

	public void save(UserItem2 transientInstance) {
		log.debug("saving UserItem2 instance");
		try {
			getSession().save(transientInstance);
			getSession().flush();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(UserItem2 persistentInstance) {
		log.debug("deleting UserItem2 instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UserItem2 findById(com.geoImage.dao.UserItem2Id id) {
		log.debug("getting UserItem2 instance with id: " + id);
		try {
			UserItem2 instance = (UserItem2) getSession().get(
					"com.geoImage.dao.UserItem2", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(UserItem2 instance) {
		log.debug("finding UserItem2 instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.UserItem2")
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
		log.debug("finding UserItem2 instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from UserItem2 as model where model."
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
		log.debug("finding all UserItem2 instances");
		try {
			String queryString = "from UserItem2";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public UserItem2 merge(UserItem2 detachedInstance) {
		log.debug("merging UserItem2 instance");
		try {
			UserItem2 result = (UserItem2) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(UserItem2 instance) {
		log.debug("attaching dirty UserItem2 instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UserItem2 instance) {
		log.debug("attaching clean UserItem2 instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}