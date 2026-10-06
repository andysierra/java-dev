package withAnnotations.dependency;

import org.springframework.stereotype.Component;

@Component
public class MySQL implements Persistence{

	@Override
	public String persist() {
		return "Persistiendo en el motor MySQL";
	}

}
