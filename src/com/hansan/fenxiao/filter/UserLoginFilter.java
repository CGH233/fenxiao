 package com.hansan.fenxiao.filter;
 
 import com.hansan.fenxiao.entities.User;
 import java.io.IOException;
 import javax.servlet.Filter;
 import javax.servlet.FilterChain;
 import javax.servlet.FilterConfig;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 import javax.servlet.http.HttpSession;
 
 public class UserLoginFilter
   implements Filter
 {
   public void destroy()
   {
   }
 
   public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
     throws IOException, ServletException
   {
     HttpServletRequest request = (HttpServletRequest)req;
     HttpServletResponse response = (HttpServletResponse)res;
     HttpSession session = request.getSession();
     User loginUser = null;
     if (!"".equals(session.getAttribute("loginUser"))) {
       loginUser = (User)session.getAttribute("loginUser");
     }
     if ((loginUser == null) || (loginUser.getId() == null)){
       String path = request.getRequestURI().substring(9);//过滤掉项目名"/fenxiao/"
       String queryStr = request.getQueryString();
       String prePage;
       if(queryStr==null)
    	   prePage = path;
       else
    	   prePage = path + "?" +queryStr;
       session.setAttribute("prePage", prePage);
       System.out.println(prePage);
       response.sendRedirect(request.getContextPath() + "/login.jsp");//重定向到登录页面
     }else
       chain.doFilter(request, response);//传递到下一个滤器
   }
 
   public void init(FilterConfig arg0)
     throws ServletException
   {
   }
 }