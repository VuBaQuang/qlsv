package utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

	private static SessionFactory sessionFactoryObj = buildSessionFactoryObj();

	public static SessionFactory buildSessionFactoryObj() {
		try {
			sessionFactoryObj = new Configuration().configure().buildSessionFactory();
		} catch (ExceptionInInitializerError exceptionObj) {
			exceptionObj.printStackTrace();
		}
		return sessionFactoryObj;
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactoryObj;
	}
}
