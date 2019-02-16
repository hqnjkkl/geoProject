package com.geoImage.dialect;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;


/**
 * 为了修改hibernate中和mysql数据格式不相符的内容
 * @author hqn
 * @description   
 * @version
 * @update 2014-2-20 上午11:07:03
 */
public class MyDialect extends MySQLDialect{
	public MyDialect()
	{
		super();
//		 registerHibernateType(Types.LONGVARCHAR,Hibernate.TEXT.getName());
		// registerHibernateType(Types.CHAR, Hibernate.STRING.getName());
		// registerHibernateType(Types.LONGVARBINARY, Hibernate.BLOB.getName());
		registerHibernateType(Types.LONGVARCHAR, "text");
		registerHibernateType(Types.CHAR, "string");
	}
}
