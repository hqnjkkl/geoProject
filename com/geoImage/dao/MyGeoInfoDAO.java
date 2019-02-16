package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * MyGeoInfo entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.geoImage.dao.MyGeoInfo
 * @author MyEclipse Persistence Tools
 */

public class MyGeoInfoDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(MyGeoInfoDAO.class);
	// property constants
	public static final String GEO_INFO_SENSE = "geoInfoSense";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String GEO_INFO_FEATURE = "geoInfoFeature";
	public static final String GEO_INFO_FEATURE_DETAILS = "geoInfoFeatureDetails";

	public void save(MyGeoInfo transientInstance) {
		log.debug("saving MyGeoInfo instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(MyGeoInfo persistentInstance) {
		log.debug("deleting MyGeoInfo instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MyGeoInfo findById(java.lang.String id) {
		log.debug("getting MyGeoInfo instance with id: " + id);
		try {
			MyGeoInfo instance = (MyGeoInfo) getSession().get(
					"com.geoImage.dao.MyGeoInfo", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(MyGeoInfo instance) {
		log.debug("finding MyGeoInfo instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.MyGeoInfo")
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
		log.debug("finding MyGeoInfo instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from MyGeoInfo as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByGeoInfoSense(Object geoInfoSense) {
		return findByProperty(GEO_INFO_SENSE, geoInfoSense);
	}

	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}

	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}

	public List findByGeoInfoFeature(Object geoInfoFeature) {
		return findByProperty(GEO_INFO_FEATURE, geoInfoFeature);
	}

	public List findByGeoInfoFeatureDetails(Object geoInfoFeatureDetails) {
		return findByProperty(GEO_INFO_FEATURE_DETAILS, geoInfoFeatureDetails);
	}

	public List findAll() {
		log.debug("finding all MyGeoInfo instances");
		try {
			String queryString = "from MyGeoInfo";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public MyGeoInfo merge(MyGeoInfo detachedInstance) {
		log.debug("merging MyGeoInfo instance");
		try {
			MyGeoInfo result = (MyGeoInfo) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(MyGeoInfo instance) {
		log.debug("attaching dirty MyGeoInfo instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MyGeoInfo instance) {
		log.debug("attaching clean MyGeoInfo instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}