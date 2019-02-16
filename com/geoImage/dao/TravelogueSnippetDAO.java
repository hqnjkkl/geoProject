package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * TravelogueSnippet entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.geoImage.dao.TravelogueSnippet
 * @author MyEclipse Persistence Tools
 */

public class TravelogueSnippetDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(TravelogueSnippetDAO.class);
	// property constants
	public static final String SNIPPET_TEXT = "snippetText";
	public static final String LOCATION_WORD_COUNT = "locationWordCount";
	public static final String START = "start";
	public static final String END = "end";
	public static final String PRIMARY_LOCATION_ID = "primaryLocationId";

	public void save(TravelogueSnippet transientInstance) {
		log.debug("saving TravelogueSnippet instance");
		try {
			getSession().save(transientInstance);
//			getSession().getTransaction().commit();
			getSession().flush();
			log.debug("save successful");
			
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TravelogueSnippet persistentInstance) {
		log.debug("deleting TravelogueSnippet instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TravelogueSnippet findById(com.geoImage.dao.TravelogueSnippetId id) {
		log.debug("getting TravelogueSnippet instance with id: " + id);
		try {
			TravelogueSnippet instance = (TravelogueSnippet) getSession().get(
					"com.geoImage.dao.TravelogueSnippet", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TravelogueSnippet instance) {
		log.debug("finding TravelogueSnippet instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.TravelogueSnippet")
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
		log.debug("finding TravelogueSnippet instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TravelogueSnippet as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySnippetText(Object snippetText) {
		return findByProperty(SNIPPET_TEXT, snippetText);
	}

	public List findByLocationWordCount(Object locationWordCount) {
		return findByProperty(LOCATION_WORD_COUNT, locationWordCount);
	}

	public List findByStart(Object start) {
		return findByProperty(START, start);
	}

	public List findByEnd(Object end) {
		return findByProperty(END, end);
	}

	public List findByPrimaryLocationId(Object primaryLocationId) {
		return findByProperty(PRIMARY_LOCATION_ID, primaryLocationId);
	}

	public List findAll() {
		log.debug("finding all TravelogueSnippet instances");
		try {
			String queryString = "from TravelogueSnippet";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TravelogueSnippet merge(TravelogueSnippet detachedInstance) {
		log.debug("merging TravelogueSnippet instance");
		try {
			TravelogueSnippet result = (TravelogueSnippet) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TravelogueSnippet instance) {
		log.debug("attaching dirty TravelogueSnippet instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TravelogueSnippet instance) {
		log.debug("attaching clean TravelogueSnippet instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}