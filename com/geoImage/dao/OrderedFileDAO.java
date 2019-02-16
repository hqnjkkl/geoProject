package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * OrderedFile entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.OrderedFile
 * @author MyEclipse Persistence Tools
 */

public class OrderedFileDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(OrderedFileDAO.class);
	// property constants
	public static final String CORE_DISTANCE = "coreDistance";
	public static final String REACHABILITY_DISTANCE = "reachabilityDistance";

	public void save(OrderedFile transientInstance) {
		log.debug("saving OrderedFile instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(OrderedFile persistentInstance) {
		log.debug("deleting OrderedFile instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OrderedFile findById(com.geoImage.dao.OrderedFileId id) {
		log.debug("getting OrderedFile instance with id: " + id);
		try {
			OrderedFile instance = (OrderedFile) getSession().get(
					"com.geoImage.dao.OrderedFile", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(OrderedFile instance) {
		log.debug("finding OrderedFile instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.OrderedFile")
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
		log.debug("finding OrderedFile instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from OrderedFile as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCoreDistance(Object coreDistance) {
		return findByProperty(CORE_DISTANCE, coreDistance);
	}

	public List findByReachabilityDistance(Object reachabilityDistance) {
		return findByProperty(REACHABILITY_DISTANCE, reachabilityDistance);
	}

	public List findAll() {
		log.debug("finding all OrderedFile instances");
		try {
			String queryString = "from OrderedFile";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OrderedFile merge(OrderedFile detachedInstance) {
		log.debug("merging OrderedFile instance");
		try {
			OrderedFile result = (OrderedFile) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OrderedFile instance) {
		log.debug("attaching dirty OrderedFile instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OrderedFile instance) {
		log.debug("attaching clean OrderedFile instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}