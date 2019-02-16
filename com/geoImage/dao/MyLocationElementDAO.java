package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * MyLocationElement entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.MyLocationElement
 * @author MyEclipse Persistence Tools
 */

public class MyLocationElementDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(MyLocationElementDAO.class);
	// property constants
	public static final String LOCATION_NAME = "locationName";
	public static final String LOCATION_ORIGINAL_TEXT = "locationOriginalText";
	public static final String TYPE = "type";
	public static final String CONFIDENCE = "confidence";
	public static final String START = "start";
	public static final String END = "end";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";

	public void save(MyLocationElement transientInstance) {
		log.debug("saving MyLocationElement instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(MyLocationElement persistentInstance) {
		log.debug("deleting MyLocationElement instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MyLocationElement findById(com.geoImage.dao.MyLocationElementId id) {
		log.debug("getting MyLocationElement instance with id: " + id);
		try {
			MyLocationElement instance = (MyLocationElement) getSession().get(
					"com.geoImage.dao.MyLocationElement", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(MyLocationElement instance) {
		log.debug("finding MyLocationElement instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.MyLocationElement")
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
		log.debug("finding MyLocationElement instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from MyLocationElement as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByLocationName(Object locationName) {
		return findByProperty(LOCATION_NAME, locationName);
	}

	public List findByLocationOriginalText(Object locationOriginalText) {
		return findByProperty(LOCATION_ORIGINAL_TEXT, locationOriginalText);
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findByConfidence(Object confidence) {
		return findByProperty(CONFIDENCE, confidence);
	}

	public List findByStart(Object start) {
		return findByProperty(START, start);
	}

	public List findByEnd(Object end) {
		return findByProperty(END, end);
	}

	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}

	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}

	public List findAll() {
		log.debug("finding all MyLocationElement instances");
		try {
			String queryString = "from MyLocationElement";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public MyLocationElement merge(MyLocationElement detachedInstance) {
		log.debug("merging MyLocationElement instance");
		try {
			MyLocationElement result = (MyLocationElement) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(MyLocationElement instance) {
		log.debug("attaching dirty MyLocationElement instance");
		try {
			getSession().saveOrUpdate(instance);
			getSession().flush();
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MyLocationElement instance) {
		log.debug("attaching clean MyLocationElement instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}