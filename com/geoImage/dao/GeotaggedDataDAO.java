package com.geoImage.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * GeotaggedData entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.GeotaggedData
 * @author MyEclipse Persistence Tools
 */

public class GeotaggedDataDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(GeotaggedDataDAO.class);
	// property constants
	public static final String PHOTO_TAGS = "photoTags";
	public static final String OWNER_NAME = "ownerName";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String HEIGHT = "height";
	public static final String WIDTH = "width";
	public static final String PHOTO_TITLE = "photoTitle";
	public static final String OWNER_URL = "ownerUrl";
	public static final String OWNER_ID = "ownerId";
	public static final String PHOTO_FILE_URL = "photoFileUrl";
	public static final String PHOTO_URL = "photoUrl";

	public void save(GeotaggedData transientInstance) {
		log.debug("saving GeotaggedData instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GeotaggedData persistentInstance) {
		log.debug("deleting GeotaggedData instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GeotaggedData findById(java.lang.Integer id) {
		log.debug("getting GeotaggedData instance with id: " + id);
		try {
			GeotaggedData instance = (GeotaggedData) getSession().get(
					"com.geoImage.dao.GeotaggedData", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(GeotaggedData instance) {
		log.debug("finding GeotaggedData instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.GeotaggedData")
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
		log.debug("finding GeotaggedData instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from GeotaggedData as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPhotoTags(Object photoTags) {
		return findByProperty(PHOTO_TAGS, photoTags);
	}

	public List findByOwnerName(Object ownerName) {
		return findByProperty(OWNER_NAME, ownerName);
	}

	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}

	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}

	public List findByHeight(Object height) {
		return findByProperty(HEIGHT, height);
	}

	public List findByWidth(Object width) {
		return findByProperty(WIDTH, width);
	}

	public List findByPhotoTitle(Object photoTitle) {
		return findByProperty(PHOTO_TITLE, photoTitle);
	}

	public List findByOwnerUrl(Object ownerUrl) {
		return findByProperty(OWNER_URL, ownerUrl);
	}

	public List findByOwnerId(Object ownerId) {
		return findByProperty(OWNER_ID, ownerId);
	}

	public List findByPhotoFileUrl(Object photoFileUrl) {
		return findByProperty(PHOTO_FILE_URL, photoFileUrl);
	}

	public List findByPhotoUrl(Object photoUrl) {
		return findByProperty(PHOTO_URL, photoUrl);
	}

	public List findAll() {
		log.debug("finding all GeotaggedData instances");
		try {
			String queryString = "from GeotaggedData";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GeotaggedData merge(GeotaggedData detachedInstance) {
		log.debug("merging GeotaggedData instance");
		try {
			GeotaggedData result = (GeotaggedData) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GeotaggedData instance) {
		log.debug("attaching dirty GeotaggedData instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GeotaggedData instance) {
		log.debug("attaching clean GeotaggedData instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}