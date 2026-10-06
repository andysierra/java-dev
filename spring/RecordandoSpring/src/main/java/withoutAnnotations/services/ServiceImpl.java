package withoutAnnotations.services;

import withoutAnnotations.dependency.Persistence;

public class ServiceImpl implements Service{

	private Persistence mySQL;
	
	public ServiceImpl() {}

	public Persistence getMySQL() {
		return mySQL;
	}

	// setter with weird name
	public void setMySQL_im_injectionSetter(Persistence mySQL) {
		this.mySQL = mySQL;
	}

	@Override
	public void doPersist() {
		System.out.println(this.mySQL.persist());	
	}
}
