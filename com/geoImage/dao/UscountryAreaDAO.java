package com.geoImage.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * UscountryArea entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.UscountryArea
 * @author MyEclipse Persistence Tools
 */

public class UscountryAreaDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UscountryAreaDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String ASCIINAME = "asciiname";
	public static final String ALTERNATENAME = "alternatename";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String FEATURE_CLASS = "featureClass";
	public static final String FEATURE_CODE = "featureCode";
	public static final String COUNTRY_CODE = "countryCode";
	public static final String CC2 = "cc2";
	public static final String ADMIN1_CODE = "admin1Code";
	public static final String ADMIN2_CODE = "admin2Code";
	public static final String ADMIN3_CODE = "admin3Code";
	public static final String ADMIN4_CODE = "admin4Code";
	public static final String POPULATION = "population";
	public static final String ELEVATION = "elevation";
	public static final String DEM = "dem";
	public static final String TIMEZONE = "timezone";

	public void save(UscountryArea transientInstance) {
		log.debug("saving UscountryArea instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(UscountryArea persistentInstance) {
		log.debug("deleting UscountryArea instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UscountryArea findById(java.lang.Long id) {
		log.debug("getting UscountryArea instance with id: " + id);
		try {
			UscountryArea instance = (UscountryArea) getSession().get(
					"com.geoImage.dao.UscountryArea", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(UscountryArea instance) {
		log.debug("finding UscountryArea instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.UscountryArea")
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
		log.debug("finding UscountryArea instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from UscountryArea as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByAsciiname(Object asciiname) {
		return findByProperty(ASCIINAME, asciiname);
	}

	public List findByAlternatename(Object alternatename) {
		return findByProperty(ALTERNATENAME, alternatename);
	}

	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}

	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}

	public List findByFeatureClass(Object featureClass) {
		return findByProperty(FEATURE_CLASS, featureClass);
	}

	public List findByFeatureCode(Object featureCode) {
		return findByProperty(FEATURE_CODE, featureCode);
	}

	public List findByCountryCode(Object countryCode) {
		return findByProperty(COUNTRY_CODE, countryCode);
	}

	public List findByCc2(Object cc2) {
		return findByProperty(CC2, cc2);
	}

	public List findByAdmin1Code(Object admin1Code) {
		return findByProperty(ADMIN1_CODE, admin1Code);
	}

	public List findByAdmin2Code(Object admin2Code) {
		return findByProperty(ADMIN2_CODE, admin2Code);
	}

	public List findByAdmin3Code(Object admin3Code) {
		return findByProperty(ADMIN3_CODE, admin3Code);
	}

	public List findByAdmin4Code(Object admin4Code) {
		return findByProperty(ADMIN4_CODE, admin4Code);
	}

	public List findByPopulation(Object population) {
		return findByProperty(POPULATION, population);
	}

	public List findByElevation(Object elevation) {
		return findByProperty(ELEVATION, elevation);
	}

	public List findByDem(Object dem) {
		return findByProperty(DEM, dem);
	}

	public List findByTimezone(Object timezone) {
		return findByProperty(TIMEZONE, timezone);
	}

	public List findAll() {
		log.debug("finding all UscountryArea instances");
		try {
			String queryString = "from UscountryArea";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public UscountryArea merge(UscountryArea detachedInstance) {
		log.debug("merging UscountryArea instance");
		try {
			UscountryArea result = (UscountryArea) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(UscountryArea instance) {
		log.debug("attaching dirty UscountryArea instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UscountryArea instance) {
		log.debug("attaching clean UscountryArea instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}