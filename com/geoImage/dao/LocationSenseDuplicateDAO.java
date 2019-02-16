package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * LocationSenseDuplicate entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.LocationSenseDuplicate
 * @author MyEclipse Persistence Tools
 */

public class LocationSenseDuplicateDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(LocationSenseDuplicateDAO.class);
	// property constants
	public static final String WORD_ID_DUPLICATE = "wordIdDuplicate";
	public static final String DOC_ID_DUPLICATE = "docIdDuplicate";
	public static final String LOCATION_NAME = "locationName";

	public void save(LocationSenseDuplicate transientInstance) {
		log.debug("saving LocationSenseDuplicate instance");
		try {
			getSession().save(transientInstance);
			getSession().flush();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(LocationSenseDuplicate persistentInstance) {
		log.debug("deleting LocationSenseDuplicate instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LocationSenseDuplicate findById(
			com.geoImage.dao.LocationSenseDuplicateId id) {
		log.debug("getting LocationSenseDuplicate instance with id: " + id);
		try {
			LocationSenseDuplicate instance = (LocationSenseDuplicate) getSession()
					.get("com.geoImage.dao.LocationSenseDuplicate", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(LocationSenseDuplicate instance) {
		log.debug("finding LocationSenseDuplicate instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.LocationSenseDuplicate")
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
		log.debug("finding LocationSenseDuplicate instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from LocationSenseDuplicate as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByWordIdDuplicate(Object wordIdDuplicate) {
		return findByProperty(WORD_ID_DUPLICATE, wordIdDuplicate);
	}

	public List findByDocIdDuplicate(Object docIdDuplicate) {
		return findByProperty(DOC_ID_DUPLICATE, docIdDuplicate);
	}

	public List findByLocationName(Object locationName) {
		return findByProperty(LOCATION_NAME, locationName);
	}

	public List findAll() {
		log.debug("finding all LocationSenseDuplicate instances");
		try {
			String queryString = "from LocationSenseDuplicate";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public LocationSenseDuplicate merge(LocationSenseDuplicate detachedInstance) {
		log.debug("merging LocationSenseDuplicate instance");
		try {
			LocationSenseDuplicate result = (LocationSenseDuplicate) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(LocationSenseDuplicate instance) {
		log.debug("attaching dirty LocationSenseDuplicate instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LocationSenseDuplicate instance) {
		log.debug("attaching clean LocationSenseDuplicate instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}