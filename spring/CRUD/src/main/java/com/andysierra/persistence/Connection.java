package com.andysierra.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Connection {
	private EntityManager em;
	
	public Connection() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("CRUDPersistence");
		this.em = emf.createEntityManager();
	}
	
	public void open() {
		em.getTransaction().begin();
	}
	
	public void close(boolean commitRollback) {
		if(commitRollback)
			em.getTransaction().commit();
		else
			em.getTransaction().rollback();
		em.clear();
		em.close();
	}
	
	public EntityManager getEntityMan() {
		return this.em;
	}
}