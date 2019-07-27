 package com.hansan.fenxiao.filter;
 
 import com.hansan.fenxiao.entities.Admin;
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
 
 public class AdminLoginFilter
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
     Admin loginAdmin = null;
     if (!"".equals(session.getAttribute("loginAdmin"))) {
       loginAdmin = (Admin)session.getAttribute("loginAdmin");
     }
     if ((loginAdmin == null) || (loginAdmin.getId() == null))
       response.sendRedirect(request.getContextPath() + "/adminLogin");//重定向到管理员登录
     else
       chain.doFilter(request, response);//传递到下一过滤器
   }
 
   public void init(FilterConfig arg0)
     throws ServletException
   {
   }
 }