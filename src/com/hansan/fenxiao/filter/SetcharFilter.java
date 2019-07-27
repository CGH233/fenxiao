 package com.hansan.fenxiao.filter;
 
 import java.io.IOException;
 import javax.servlet.Filter;
 import javax.servlet.FilterChain;
 import javax.servlet.FilterConfig;
 import javax.servlet.ServletException;
 import javax.servlet.ServletRequest;
 import javax.servlet.ServletResponse;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;
 
 public class SetcharFilter
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
     request.setCharacterEncoding("UTF-8");//设置request编码格式
     response.setCharacterEncoding("UTF-8");
     response.setContentType("text/html");//设置response字符编码
 
     chain.doFilter(request, response);//传递给下一过滤器
   }
 
   public void init(FilterConfig filterConfig)
     throws ServletException
   {
   }
 }