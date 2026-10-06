package withConfigClass.use;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import withConfigClass.ApplicationConfig;
import withConfigClass.service.Service;

public class Main {

	public static void main(String[] args) {

		//String url = "withAnnotations/ApplicationContext.xml";
		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(url);
		Class<ApplicationConfig> location = ApplicationConfig.class;
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(location);
		
		Service service = context.getBean("serviceImpl", withConfigClass.service.ServiceImpl.class);
		service.doPersist();
		
		context.close();
	}

}
