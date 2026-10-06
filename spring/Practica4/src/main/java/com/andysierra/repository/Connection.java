package com.andysierra.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.andysierra.model.impl.Usuario;

public class Connection {

	private SessionFactory sessionFactory;
	private Session session;
	
	public Connection() {
		this.sessionFactory
			= new Configuration().configure("hibernate.cfg.xml")
				.addAnnotatedClass(Usuario.class).buildSessionFactory();
	}
	
	public Session open() {
		this.session = sessionFactory.openSession();
		return this.session;
	}
	
	public boolean close() {
		this.session.close();
		this.sessionFactory.close();
		return !session.isConnected() && sessionFactory.isClosed();
	}
}
