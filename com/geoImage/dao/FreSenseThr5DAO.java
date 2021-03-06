package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * FreSenseThr5 entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.FreSenseThr5
 * @author MyEclipse Persistence Tools
 */

public class FreSenseThr5DAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(FreSenseThr5DAO.class);
	// property constants
	public static final String ROUTE_LOCATION_NUMBER = "routeLocationNumber";
	public static final String ROUTE_OCCURENCE = "routeOccurence";
	public static final String ROUTE_TEXT = "routeText";
	public static final String ROUTE_WORD_IDS = "routeWordIds";
	public static final String ROUTE_THRESHOLD = "routeThreshold";

	public void save(FreSenseThr5 transientInstance) {
		log.debug("saving FreSenseThr5 instance");
		try {
			getSession().save(transientInstance);
			getSession().flush();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(FreSenseThr5 persistentInstance) {
		log.debug("deleting FreSenseThr5 instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FreSenseThr5 findById(java.lang.Integer id) {
		log.debug("getting FreSenseThr5 instance with id: " + id);
		try {
			FreSenseThr5 instance = (FreSenseThr5) getSession().get(
					"com.geoImage.dao.FreSenseThr5", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(FreSenseThr5 instance) {
		log.debug("finding FreSenseThr5 instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.FreSenseThr5")
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
		log.debug("finding FreSenseThr5 instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from FreSenseThr5 as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRouteLocationNumber(Object routeLocationNumber) {
		return findByProperty(ROUTE_LOCATION_NUMBER, routeLocationNumber);
	}

	public List findByRouteOccurence(Object routeOccurence) {
		return findByProperty(ROUTE_OCCURENCE, routeOccurence);
	}

	public List findByRouteText(Object routeText) {
		return findByProperty(ROUTE_TEXT, routeText);
	}

	public List findByRouteWordIds(Object routeWordIds) {
		return findByProperty(ROUTE_WORD_IDS, routeWordIds);
	}

	public List findByRouteThreshold(Object routeThreshold) {
		return findByProperty(ROUTE_THRESHOLD, routeThreshold);
	}

	public List findAll() {
		log.debug("finding all FreSenseThr5 instances");
		try {
			String queryString = "from FreSenseThr5";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public FreSenseThr5 merge(FreSenseThr5 detachedInstance) {
		log.debug("merging FreSenseThr5 instance");
		try {
			FreSenseThr5 result = (FreSenseThr5) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(FreSenseThr5 instance) {
		log.debug("attaching dirty FreSenseThr5 instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FreSenseThr5 instance) {
		log.debug("attaching clean FreSenseThr5 instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}