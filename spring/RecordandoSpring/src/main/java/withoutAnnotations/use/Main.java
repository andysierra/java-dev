package withoutAnnotations.use;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import withoutAnnotations.services.Service;
import withoutAnnotations.services.ServiceImpl;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) {

		String url = "withoutAnnotations/ApplicationContext.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(url);
		
		Service service = context.getBean("ServiceImpl",ServiceImpl.class);
		service.doPersist();
		
		context.close();
	}

}
