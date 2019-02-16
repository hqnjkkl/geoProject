package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * LocationSenseIndex entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.LocationSenseIndex
 * @author MyEclipse Persistence Tools
 */

public class LocationSenseIndexDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(LocationSenseIndexDAO.class);
	// property constants
	public static final String SENSE_STRING = "senseString";

	public void save(LocationSenseIndex transientInstance) {
		log.debug("saving LocationSenseIndex instance");
		try {
			getSession().save(transientInstance);
			getSession().flush();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(LocationSenseIndex persistentInstance) {
		log.debug("deleting LocationSenseIndex instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LocationSenseIndex findById(java.lang.Integer id) {
		log.debug("getting LocationSenseIndex instance with id: " + id);
		try {
			LocationSenseIndex instance = (LocationSenseIndex) getSession()
					.get("com.geoImage.dao.LocationSenseIndex", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(LocationSenseIndex instance) {
		log.debug("finding LocationSenseIndex instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.LocationSenseIndex")
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
		log.debug("finding LocationSenseIndex instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from LocationSenseIndex as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySenseString(Object senseString) {
		return findByProperty(SENSE_STRING, senseString);
	}

	public List findAll() {
		log.debug("finding all LocationSenseIndex instances");
		try {
			String queryString = "from LocationSenseIndex";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public LocationSenseIndex merge(LocationSenseIndex detachedInstance) {
		log.debug("merging LocationSenseIndex instance");
		try {
			LocationSenseIndex result = (LocationSenseIndex) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(LocationSenseIndex instance) {
		log.debug("attaching dirty LocationSenseIndex instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LocationSenseIndex instance) {
		log.debug("attaching clean LocationSenseIndex instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}