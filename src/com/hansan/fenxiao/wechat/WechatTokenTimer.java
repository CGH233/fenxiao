package com.hansan.fenxiao.wechat;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token。
 * access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效
 * 设置一个监听器，程序启动时开启定时任务
 * @author xinyu
 * @date 2019年7月5日
 */
public class WechatTokenTimer implements ServletContextListener{
	
	public Timer timer = null;
	public void contextDestroyed(ServletContextEvent arg0) {
	    timer.cancel();
	    System.out.println("定时器已销毁");
	}

	public void contextInitialized(ServletContextEvent event) {
	  
	      Timer timer = new Timer(true);
	      SampleTask sampleTask =   new SampleTask(event.getServletContext());
	      System.out.println("定时器已启动");
	      timer.schedule(sampleTask, (long)0, (long)2*60 * 60 * 1000L);
	      System.out.println("已经添加任务调度表");
	     }
	}


	class SampleTask extends TimerTask{   
		ServletContext context;
		
	    public SampleTask(ServletContext context){
	     this.context = context; 
	    }  

	    public void run() {
	       //获取access_token
	    	
	      }
}
