package withAnnotations.use;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import withAnnotations.service.Service;

public class Main {

	public static void main(String[] args) {

		String url = "withAnnotations/ApplicationContext.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(url);
		
		Service service = context.getBean("serviceImpl", withAnnotations.service.ServiceImpl.class);
		service.doPersist();
		
		context.close();
	}

}
