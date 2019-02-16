package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * PhotoCluster entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.PhotoCluster
 * @author MyEclipse Persistence Tools
 */

public class PhotoClusterDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(PhotoClusterDAO.class);
	// property constants
	public static final String ERADIUS2 = "eradius2";
	public static final String CLUSTER_ID = "clusterId";
	public static final String MIN_PTS = "minPts";

	public void save(PhotoCluster transientInstance) {
		log.debug("saving PhotoCluster instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PhotoCluster persistentInstance) {
		log.debug("deleting PhotoCluster instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PhotoCluster findById(com.geoImage.dao.PhotoClusterId id) {
		log.debug("getting PhotoCluster instance with id: " + id);
		try {
			PhotoCluster instance = (PhotoCluster) getSession().get(
					"com.geoImage.dao.PhotoCluster", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PhotoCluster instance) {
		log.debug("finding PhotoCluster instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.PhotoCluster")
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
		log.debug("finding PhotoCluster instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from PhotoCluster as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByEradius2(Object eradius2) {
		return findByProperty(ERADIUS2, eradius2);
	}

	public List findByClusterId(Object clusterId) {
		return findByProperty(CLUSTER_ID, clusterId);
	}

	public List findByMinPts(Object minPts) {
		return findByProperty(MIN_PTS, minPts);
	}

	public List findAll() {
		log.debug("finding all PhotoCluster instances");
		try {
			String queryString = "from PhotoCluster";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PhotoCluster merge(PhotoCluster detachedInstance) {
		log.debug("merging PhotoCluster instance");
		try {
			PhotoCluster result = (PhotoCluster) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PhotoCluster instance) {
		log.debug("attaching dirty PhotoCluster instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PhotoCluster instance) {
		log.debug("attaching clean PhotoCluster instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}