package com.rsaraiva.youmoney.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.rsaraiva.youmoney.model.CartaoDeCredito;
import com.rsaraiva.youmoney.model.Despesa;
import com.rsaraiva.youmoney.model.Lancamento;
import com.rsaraiva.youmoney.model.MeioDePagamento;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Despesa.class);
		configuration.addAnnotatedClass(CartaoDeCredito.class);
		configuration.addAnnotatedClass(MeioDePagamento.class);
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
