package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Travelogue entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.geoImage.dao.Travelogue
 * @author MyEclipse Persistence Tools
 */

public class TravelogueDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(TravelogueDAO.class);
	// property constants
	public static final String COUNTRY = "country";
	public static final String GEO_INFO = "geoInfo";
	public static final String TITLE = "title";
	public static final String SOURCE = "source";
	public static final String AUTHOR = "author";
	public static final String DATE = "date";
	public static final String TEXT = "text";
	public static final String URL = "url";
	public static final String WORD_COUNT = "wordCount";
	public static final String PHOTO_COUNT = "photoCount";
	public static final String PHOTO_ID_LIST = "photoIdList";

	public void save(Travelogue transientInstance) {
		log.debug("saving Travelogue instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Travelogue persistentInstance) {
		log.debug("deleting Travelogue instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Travelogue findById(java.lang.Integer id) {
		log.debug("getting Travelogue instance with id: " + id);
		try {
			Travelogue instance = (Travelogue) getSession().get(
					"com.geoImage.dao.Travelogue", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Travelogue instance) {
		log.debug("finding Travelogue instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.Travelogue")
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
		log.debug("finding Travelogue instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Travelogue as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCountry(Object country) {
		return findByProperty(COUNTRY, country);
	}

	public List findByGeoInfo(Object geoInfo) {
		return findByProperty(GEO_INFO, geoInfo);
	}

	public List findByTitle(Object title) {
		return findByProperty(TITLE, title);
	}

	public List findBySource(Object source) {
		return findByProperty(SOURCE, source);
	}

	public List findByAuthor(Object author) {
		return findByProperty(AUTHOR, author);
	}

	public List findByDate(Object date) {
		return findByProperty(DATE, date);
	}

	public List findByText(Object text) {
		return findByProperty(TEXT, text);
	}

	public List findByUrl(Object url) {
		return findByProperty(URL, url);
	}

	public List findByWordCount(Object wordCount) {
		return findByProperty(WORD_COUNT, wordCount);
	}

	public List findByPhotoCount(Object photoCount) {
		return findByProperty(PHOTO_COUNT, photoCount);
	}

	public List findByPhotoIdList(Object photoIdList) {
		return findByProperty(PHOTO_ID_LIST, photoIdList);
	}

	public List findAll() {
		log.debug("finding all Travelogue instances");
		try {
			String queryString = "from Travelogue";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Travelogue merge(Travelogue detachedInstance) {
		log.debug("merging Travelogue instance");
		try {
			Travelogue result = (Travelogue) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Travelogue instance) {
		log.debug("attaching dirty Travelogue instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Travelogue instance) {
		log.debug("attaching clean Travelogue instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}