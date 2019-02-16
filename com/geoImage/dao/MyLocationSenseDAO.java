package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * MyLocationSense entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.MyLocationSense
 * @author MyEclipse Persistence Tools
 */

public class MyLocationSenseDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(MyLocationSenseDAO.class);
	// property constants
	public static final String LOCATION_SENSE_NAME = "locationSenseName";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String LOCATION_SENSE_TIME = "locationSenseTime";
	public static final String LOCATION_SENSE_PRIOR_PROBABILITY = "locationSensePriorProbability";
	public static final String SENSE_FEATURE = "senseFeature";
	public static final String SENSE_FEATURE_DETAILS = "senseFeatureDetails";

	public void save(MyLocationSense transientInstance) {
		log.debug("saving MyLocationSense instance");
		try {
			getSession().save(transientInstance);
			getSession().flush();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(MyLocationSense persistentInstance) {
		log.debug("deleting MyLocationSense instance");
		try {
			getSession().delete(persistentInstance);
			getSession().flush();
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MyLocationSense findById(com.geoImage.dao.MyLocationSenseId id) {
		log.debug("getting MyLocationSense instance with id: " + id);
		try {
			MyLocationSense instance = (MyLocationSense) getSession().get(
					"com.geoImage.dao.MyLocationSense", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(MyLocationSense instance) {
		log.debug("finding MyLocationSense instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.MyLocationSense")
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
		log.debug("finding MyLocationSense instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from MyLocationSense as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByLocationSenseName(Object locationSenseName) {
		return findByProperty(LOCATION_SENSE_NAME, locationSenseName);
	}

	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}

	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}

	public List findByLocationSenseTime(Object locationSenseTime) {
		return findByProperty(LOCATION_SENSE_TIME, locationSenseTime);
	}

	public List findByLocationSensePriorProbability(
			Object locationSensePriorProbability) {
		return findByProperty(LOCATION_SENSE_PRIOR_PROBABILITY,
				locationSensePriorProbability);
	}

	public List findBySenseFeature(Object senseFeature) {
		return findByProperty(SENSE_FEATURE, senseFeature);
	}

	public List findBySenseFeatureDetails(Object senseFeatureDetails) {
		return findByProperty(SENSE_FEATURE_DETAILS, senseFeatureDetails);
	}

	public List findAll() {
		log.debug("finding all MyLocationSense instances");
		try {
			String queryString = "from MyLocationSense";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public MyLocationSense merge(MyLocationSense detachedInstance) {
		log.debug("merging MyLocationSense instance");
		try {
			MyLocationSense result = (MyLocationSense) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(MyLocationSense instance) {
		log.debug("attaching dirty MyLocationSense instance");
		try {
			getSession().saveOrUpdate(instance);
			getSession().flush();
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MyLocationSense instance) {
		log.debug("attaching clean MyLocationSense instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}