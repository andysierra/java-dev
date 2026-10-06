package withoutAnnotations.dependency;

public class MySQL implements Persistence {

	@Override
	public String persist() {
		return "Persistiendo en el motor MySQL";
	}

}
