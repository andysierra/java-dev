package com.andysierra.dao;

import java.util.List;
import javax.persistence.Query;
import com.andysierra.models.impl.Cliente;
import com.andysierra.persistence.Connection;

public class ClienteDAO implements IDao {

	private Connection connection;
	
	public ClienteDAO() {
		connection = new Connection();
	}
	
	public List<Cliente> getAll() {
		List <Cliente> result = null;
		
		try {
			connection.open();
			
			Query query = connection.getEntityMan().createQuery("SELECT * FROM CLIENTE", Cliente.class);
			result = query.getResultList();
			connection.close(true);
		}
		catch (Exception e) {
			connection.close(false);
		}
		
		return result;
	}
}