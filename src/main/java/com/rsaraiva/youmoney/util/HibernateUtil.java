package com.rsaraiva.youmoney.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.rsaraiva.youmoney.model.Lancamento;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Lancamento.class);
		configuration.configure();
		
		ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		
		ServiceRegistry serviceRegistry = serviceRegistryBuilder
				.buildServiceRegistry();
		
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	public Session getSession() {
		Session session = sessionFactory.openSession();
		return session;
	}

}
