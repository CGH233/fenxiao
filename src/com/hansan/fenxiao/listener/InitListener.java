 package com.hansan.fenxiao.listener;
 
 import com.hansan.fenxiao.entities.Config;
 import com.hansan.fenxiao.service.IConfigService;
 import javax.servlet.ServletContext;
 import javax.servlet.ServletContextEvent;
 import javax.servlet.ServletContextListener;
 import org.springframework.web.context.WebApplicationContext;
 import org.springframework.web.context.support.WebApplicationContextUtils;
 
 public class InitListener  implements ServletContextListener
 {
   /*
    * 应用程序已被载出，即关闭(non-Javadoc)
    * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
    */
   public void contextDestroyed(ServletContextEvent arg0)
   {
   }
 
   /*
    * 应用程序已经被加载及初始化(non-Javadoc)
    * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
    */
   public void contextInitialized(ServletContextEvent arg0)
   {
     WebApplicationContext rwp = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());
 
     IConfigService configService = (IConfigService)rwp.getBean("configService");
     Config config = (Config)configService.findById(Config.class, 1);
     arg0.getServletContext().setAttribute("config", config);
   }
 }