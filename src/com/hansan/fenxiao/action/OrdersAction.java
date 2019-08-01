package com.hansan.fenxiao.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hansan.fenxiao.entities.BounsRule;
import com.hansan.fenxiao.entities.Commission;
import com.hansan.fenxiao.entities.Config;
import com.hansan.fenxiao.entities.Financial;
import com.hansan.fenxiao.entities.Kami;
import com.hansan.fenxiao.entities.Orders;
import com.hansan.fenxiao.entities.Product;
import com.hansan.fenxiao.entities.User;
import com.hansan.fenxiao.service.IBounsRuleService;
import com.hansan.fenxiao.service.ICommissionService;
import com.hansan.fenxiao.service.IConfigService;
import com.hansan.fenxiao.service.IFinancialService;
import com.hansan.fenxiao.service.IKamiService;
import com.hansan.fenxiao.service.IOrdersService;
import com.hansan.fenxiao.service.IProductService;
import com.hansan.fenxiao.service.IUserService;
import com.hansan.fenxiao.utils.BjuiJson;
import com.hansan.fenxiao.utils.BjuiPage;
import com.hansan.fenxiao.utils.FreemarkerUtils;
import com.hansan.fenxiao.utils.PageModel;
import freemarker.template.Configuration;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("ordersAction")
@Scope("prototype")
public class OrdersAction extends BaseAction
{
  private static final long serialVersionUID = 1L;

  @Resource(name="ordersService")
  private IOrdersService<Orders> ordersService;

  @Resource(name="userService")
  private IUserService<User> userService;

  @Resource(name="productService")
  private IProductService<Product> productService;

  @Resource(name="kamiService")
  private IKamiService<Kami> kamiService;

  @Resource(name="financialService")
  private IFinancialService<Financial> financialService;

  @Resource(name="commissionService")
  private ICommissionService<Commission> commissionService;
  
  @Resource(name="bounsRuleService")
  private IBounsRuleService<BounsRule> bounsRuleService;
  
  private Orders orders;
  private String ftlFileName;

  @Resource(name="configService")
  private IConfigService<Config> configService;

  public void list()
  {
    String key = this.request.getParameter("key");
    String countHql = "select count(*) from Orders where deleted=0";
    String hql = "from Orders where deleted=0";
    if (StringUtils.isNotEmpty(key)) {
      countHql = countHql + " and (user.name='" + key + "' or no='" + key + "' or productName='" + key + "')";
      hql = hql + " and (user.name='" + key + "' or no='" + key + "' or productName='" + key + "')";
    }
    hql = hql + " order by id desc";

    int count = 0;
    count = this.ordersService.getTotalCount(countHql, new Object[0]);
    this.page = new BjuiPage(this.pageCurrent, this.pageSize);
    this.page.setTotalCount(count);
    this.cfg = new Configuration();

    this.cfg.setServletContextForTemplateLoading(
      this.request.getServletContext(), "WEB-INF/templates/admin");
    List ordersList = this.ordersService.list(hql, this.page.getStart(), this.page.getPageSize(), new Object[0]);
    Map root = new HashMap();
    root.put("ordersList", ordersList);
    root.put("page", this.page);
    root.put("key", key);
    FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
  }

  public void add()
  {
    String pidStr = this.request.getParameter("pid");
    int pid = 0;
    try {
      pid = Integer.parseInt(pidStr);
    } catch (Exception e) {
      this.request.setAttribute("status", "0");
      this.request.setAttribute("message", "参数错误");
      try {
        this.request.getRequestDispatcher("cart.jsp").forward(this.request, this.response);
      } catch (ServletException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      return;
    }
    Product findProduct = (Product)this.productService.findById(Product.class, pid);
    if (findProduct == null) {
      this.request.setAttribute("status", "0");
      this.request.setAttribute("message", "商品不存在");
    } else {
    	HttpSession session = this.request.getSession();
        User loginUser = (User)session.getAttribute("loginUser");
        if ((loginUser == null) || (loginUser.getId() == null)) {
           this.request.setAttribute("status", "0");
           this.request.setAttribute("message", "您未登陆或者登陆失效，请重新登陆");
       } else {
    	   if (loginUser.getLevel() < findProduct.getLevel()) {
    		   this.request.setAttribute("status", "0");
               this.request.setAttribute("message", "您的权限不足，无法购买此产品");
    	   }else {
	    	   if (loginUser.getReBuyStatus() == 1 && findProduct.getLevel() == 1) {
	    		     findProduct.setMoney(findProduct.getRebuy());
	    	   }
	    	   	this.request.setAttribute("status", "1");
	    	   	this.request.setAttribute("rebuy", loginUser.getReBuyStatus());
	    	   	this.request.setAttribute("product", findProduct);
    	   }
       }
    }
    try {
      this.request.getRequestDispatcher("cart.jsp").forward(this.request, this.response);
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void save()
  {
    String pidStr = this.request.getParameter("pid");
    int pid = 0;
    try {
      pid = Integer.parseInt(pidStr);
    } catch (Exception e) {
      this.request.setAttribute("status", "0");
      this.request.setAttribute("message", "参数错误");
      try {
        this.request.getRequestDispatcher("orderAdd.jsp").forward(this.request, this.response);
      } catch (ServletException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      return;
    }
    Product findProduct = (Product)this.productService.findById(Product.class, pid);
    if (findProduct == null) {
      this.request.setAttribute("status", "0");
      this.request.setAttribute("message", "商品不存在");
    } else {
      HttpSession session = this.request.getSession();
      User loginUser = (User)session.getAttribute("loginUser");
      if ((loginUser == null) || (loginUser.getId() == null)) {
        this.request.setAttribute("status", "0");
        this.request.setAttribute("message", "您未登陆或者登陆失效，请重新登陆");
      } else {
        Orders newOrders = new Orders();
        newOrders.setProductId(""+findProduct.getId());
        newOrders.setProductName(findProduct.getTitle());
        newOrders.setProductNum(Integer.valueOf(1));
        if(loginUser.getReBuyStatus() != 0 && findProduct.getLevel()==1) {
        	newOrders.setProductMoney(findProduct.getRebuy());
        }else {
        	newOrders.setProductMoney(findProduct.getMoney());
        }
        
        newOrders.setUser(loginUser);
        newOrders.setStatus(Integer.valueOf(0));
        newOrders.setMoney(Double.valueOf(newOrders.getProductMoney().doubleValue()));

        Random random = new Random();
        int n = random.nextInt(9999);
        n += 10000;

        String no = ""+System.currentTimeMillis() + n;
        newOrders.setNo(no);

        newOrders.setCreateDate(new Date());
        newOrders.setDeleted(false);
        this.ordersService.saveOrUpdate(newOrders);
        try {
          this.response.sendRedirect("settle?no=" + no);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void settle()
  {
    String no = this.request.getParameter("no");
    Orders findOrders = this.ordersService.findByNo(no);
    if (findOrders == null) {
      this.request.setAttribute("status", "0");
      this.request.setAttribute("message", "订单不存在");
    } else {
      HttpSession session = this.request.getSession();
      User loginUser = (User)session.getAttribute("loginUser");
      if ((loginUser == null) || (loginUser.getId() == null)) {
        this.request.setAttribute("status", "0");
        this.request.setAttribute("message", "您未登陆或者登陆失效，请重新登陆");
      } else {
        this.request.setAttribute("orders", findOrders);
        try {
          this.request.getRequestDispatcher("settle.jsp").forward(this.request, this.response);
        } catch (ServletException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
/*
 * 订单支付
 */
  public void pay()
  {
    String no = this.request.getParameter("no");//订单号
    Orders findOrders = this.ordersService.findByNo(no);//订单
    HttpSession session = this.request.getSession();
    User loginUser = (User)session.getAttribute("loginUser");//登录用户

    JSONObject json = new JSONObject();
    if ((loginUser == null) || (loginUser.getId() == null)) {
      json.put("status", "0");
      json.put("message", "您未登陆或者登陆失效，请重新登陆");
      json.put("href", "../login.jsp");
    } else {
      User findUser = (User)this.userService.findById(User.class, loginUser.getId().intValue());
      if (findOrders == null) {
        json.put("status", "0");
        json.put("message", "订单不存在");
      }
      else if (findOrders.getUser().getId() != findUser.getId()) {
    	//登录用户和订单用户不一致
        json.put("status", "0");
        json.put("message", "没有权限");
      } else if (findUser.getBalance().doubleValue() < findOrders.getMoney().doubleValue()) {
        json.put("status", "0");
        json.put("message", "余额不足，请先充值");
      } else if (findOrders.getStatus().intValue() == 1) {
        json.put("status", "0");
        json.put("message", "该订单已付款，请不要重复提交支付");
      } else {
        List<Kami> kamiList = this.kamiService.list("from Kami where deleted=0 and status=0 and product.id=" + findOrders.getProductId(), 0, findOrders.getProductNum().intValue(), new Object[0]);
        if (kamiList.size() < findOrders.getProductNum().intValue()) {
          //未销售产品kami数量小于订单购买数量
          json.put("status", "0");
          json.put("message", "库存不足，请联系管理员");
        } else {
          findUser.setBalance(Double.valueOf(findUser.getBalance().doubleValue() - findOrders.getMoney().doubleValue()));//更新金额总数
          //如果用户未激活，则激活用户，并记录激活时间
          if (findUser.getStatus().intValue() == 0) {
            findUser.setStatus(Integer.valueOf(1));
            findUser.setStatusDate(new Date());//
          }
          //买的产品是零售产品
          if("7".equals(findOrders.getProductId())) {
        	  
        	  //从订单中更新卡密信息
        	  String summary = "卡密信息:<br/>";
              Date date = new Date();
              for (Kami kami : kamiList) {
                summary = summary + "卡号:" + kami.getNo() + ",密码:" + kami.getPassword() + "<br/>";
                //更新kami状态
                kami.setSaleTime(date);
                kami.setOrdersNo(findOrders.getNo());
                kami.setStatus(Integer.valueOf(1));
                this.kamiService.saveOrUpdate(kami);
              }

              double remainMoney  = findOrders.getMoney();
              if(findUser.getReBuyStatus()==1) {//是复购
            	  
            	  //保存user，之后的操作与user无关
            	  this.userService.saveOrUpdate(findUser);
            	  
            	  //获取用户的推荐人链表
                  String superListStr = findUser.getSuperior();
                  String[] superList = superListStr.split(";");
                  
                  //设置三个boolean, 记录2级之后的管理奖是否被分完了，为true代表还没分，为false代表已经被分出去了。
                  boolean areaReward=true;
                  boolean MainReward=true;
                  boolean shareholderReward=true;
                  
                  //第一个是空值,所以不遍历第一个,从尾部开始遍历
                  for(int i=superList.length-1;i>0;i--) {
            		  //获取推荐人
            		  User diUser = (User)this.userService.getUserByNo(superList[i]);
            		  //根据推荐人的level,获取奖励规则
            		  BounsRule bounsRule = this.bounsRuleService.bounsRuleByLevel(diUser.getLevel());

                      //添加佣金明细
                      Commission commission = new Commission();
            		  double commissionNum =0.0d;
            		  
                	  //直接推荐人
                	  if(i==superList.length-1) {
                		  //计算佣金
                		  String rewardString=bounsRule.getDirectRetailRe();
                		  if(rewardString.contains("%")) {
                			  commissionNum= findOrders.getMoney() * 
                					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                					  0.01d;
                		  }else {
                    		  commissionNum = Double.parseDouble(rewardString);
                		  }
                		  
                		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                          this.userService.saveOrUpdate(diUser);
                          //更新订单中的描述
                          summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                        		  "直接推荐人复购奖励："+commissionNum+"<br/>";
                          
                          
                          commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                        		  				"）购买产品 ["+findOrders.getProductName()+"] 的直接推荐复购奖励");
                          remainMoney = remainMoney - commissionNum;
                		  
                	  }//间接推荐人
                	  else if(i==superList.length-2){
                		  //计算佣金
                		  String rewardString=bounsRule.getIndirectRetailRe();
                		  if(rewardString.contains("%")) {
                			  commissionNum= findOrders.getMoney() * 
                					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                					  0.01d;
                		  }else {
                    		  commissionNum = Double.parseDouble(rewardString);
                		  }
                		  
                		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                          this.userService.saveOrUpdate(diUser);
                          //更新订单中的描述
                          summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                        		  "间接直接推荐人复购奖励："+commissionNum+"<br/>";
                          
                          
                          commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                        		  				"）购买产品 ["+findOrders.getProductName()+"] 的间接推荐复购奖励");
                          remainMoney = remainMoney - commissionNum;
                	  }//二级之后的管理奖
                	  else {
                		  //二级之后,只有:社区合伙人  区域合伙人 总代合伙人可以享有, 一共三笔,分完即止
                		  //如果推荐人是总代股东
                		  if(diUser.getLevel()==5) {
                			  if(shareholderReward) {
                				  //计算佣金
                        		  String rewardString=bounsRule.getTwoMaRetailRe();
                        		  if(rewardString.contains("%")) {
                        			  commissionNum= findOrders.getMoney() * 
                        					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                        					  0.01d;
                        		  }else {
                            		  commissionNum = Double.parseDouble(rewardString);
                        		  }
                        		  
                        		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                                  this.userService.saveOrUpdate(diUser);
                                  //更新订单中的描述
                                  summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后总代管理复购奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后总代管理复购奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  shareholderReward=false;
                			  }
                		  }else if (diUser.getLevel()==4) {//是区域合伙人
                			  if(MainReward) {
                				  //计算佣金
                        		  String rewardString=bounsRule.getTwoMaRetailRe();
                        		  if(rewardString.contains("%")) {
                        			  commissionNum= findOrders.getMoney() * 
                        					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                        					  0.01d;
                        		  }else {
                            		  commissionNum = Double.parseDouble(rewardString);
                        		  }
                        		  
                        		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                                  this.userService.saveOrUpdate(diUser);
                                  //更新订单中的描述
                                  summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后区域管理复购奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后区域管理复购奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  MainReward = false;
                			  }
                		  }else if (diUser.getLevel()==3) {//是社区合伙人
                			  if(areaReward) {
                				  //计算佣金
                        		  String rewardString=bounsRule.getTwoMaRetailRe();
                        		  if(rewardString.contains("%")) {
                        			  commissionNum= findOrders.getMoney() * 
                        					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                        					  0.01d;
                        		  }else {
                            		  commissionNum = Double.parseDouble(rewardString);
                        		  }
                        		  
                        		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                                  this.userService.saveOrUpdate(diUser);
                                  //更新订单中的描述
                                  summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后社区管理复购奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后社区管理复购奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  areaReward = false;
                			  }
                		  }
                		  if(shareholderReward==false && MainReward == false && areaReward == false) {
                			  //停止for循环
                        	  commission.setType(Integer.valueOf(1));
                              commission.setMoney(Double.valueOf(commissionNum));
                              commission.setNo(""+System.currentTimeMillis());
                              commission.setOperator(loginUser.getName());
                              commission.setUser(diUser);
                              commission.setCreateDate(date);
                              commission.setDeleted(false);
                              commission.setLevel(superList.length-1-i+1);
                              this.commissionService.saveOrUpdate(commission);
                			  break;
                		  }
                	  }
                	  
                	  
                	  commission.setType(Integer.valueOf(1));
                      commission.setMoney(Double.valueOf(commissionNum));
                      commission.setNo(""+System.currentTimeMillis());
                      commission.setOperator(loginUser.getName());
                      commission.setUser(diUser);
                      commission.setCreateDate(date);
                      commission.setDeleted(false);
                      commission.setLevel(superList.length-1-i+1);
                      this.commissionService.saveOrUpdate(commission);
                  }
              }else {//不是复购

                  //如果用户第一次购买，则设置为复购状态
                  if (findUser.getReBuyStatus() == 0) {
                	  findUser.setReBuyStatus(Integer.valueOf(1));
                  }
                  //设置用户的复购状态为1
                  //findUser.
            	  //保存user，之后的操作与user无关
            	  this.userService.saveOrUpdate(findUser);
            	  
            	  //获取用户的推荐人链表
                  String superListStr = findUser.getSuperior();
                  String[] superList = superListStr.split(";");
                  
                  //设置三个boolean, 记录2级之后的管理奖是否被分完了，为true代表还没分，为false代表已经被分出去了。
                  boolean areaReward=true;
                  boolean MainReward=true;
                  boolean shareholderReward=true;
                  
                  //第一个是空值,所以不遍历第一个,从尾部开始遍历
                  for(int i=superList.length-1;i>0;i--) {
            		  //获取推荐人
            		  User diUser = (User)this.userService.getUserByNo(superList[i]);
            		  //根据推荐人的level,获取奖励规则
            		  BounsRule bounsRule = this.bounsRuleService.bounsRuleByLevel(diUser.getLevel());

                      //添加佣金明细
                      Commission commission = new Commission();
            		  double commissionNum =0.0d;
            		  
                	  //直接推荐人
                	  if(i==superList.length-1) {
                		  //计算佣金
                		  String rewardString=bounsRule.getDirectRetail();
                		  if(rewardString.contains("%")) {
                			  commissionNum= findOrders.getMoney() * 
                					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                					  0.01d;
                		  }else {
                    		  commissionNum = Double.parseDouble(rewardString);
                		  }
                		  
                		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                          this.userService.saveOrUpdate(diUser);
                          //更新订单中的描述
                          summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                        		  "直接推荐人奖励："+commissionNum+"<br/>";
                          
                          
                          commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                        		  				"）购买产品 ["+findOrders.getProductName()+"] 的直接推荐奖励");
                          remainMoney = remainMoney - commissionNum;
                		  
                	  }//间接推荐人
                	  else if(i==superList.length-2){
                		  //计算佣金
                		  String rewardString=bounsRule.getIndirectRetail();
                		  if(rewardString.contains("%")) {
                			  commissionNum= findOrders.getMoney() * 
                					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                					  0.01d;
                		  }else {
                    		  commissionNum = Double.parseDouble(rewardString);
                		  }
                		  
                		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                          this.userService.saveOrUpdate(diUser);
                          //更新订单中的描述
                          summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                        		  "间接直接推荐人奖励："+commissionNum+"<br/>";
                          
                          
                          commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                        		  				"）购买产品 ["+findOrders.getProductName()+"] 的间接推荐奖励");
                          remainMoney = remainMoney - commissionNum;
                          
                	  }//二级之后的管理奖
                	  else {
                		  //二级之后,只有:社区合伙人  区域合伙人 总代合伙人可以享有, 一共三笔,分完即止
                		  //如果推荐人是总代股东
                		  if(diUser.getLevel()==5) {
                			  if(shareholderReward) {
                				  //计算佣金
                        		  String rewardString=bounsRule.getTwoMaReward();
                        		  if(rewardString.contains("%")) {
                        			  commissionNum= findOrders.getMoney() * 
                        					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                        					  0.01d;
                        		  }else {
                            		  commissionNum = Double.parseDouble(rewardString);
                        		  }
                        		  
                        		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                                  this.userService.saveOrUpdate(diUser);
                                  //更新订单中的描述
                                  summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后总代管理奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后总代管理奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  shareholderReward=false;
                			  }
                		  }else if (diUser.getLevel()==4) {//是区域合伙人
                			  if(MainReward) {
                				  //计算佣金
                        		  String rewardString=bounsRule.getTwoMaReward();
                        		  if(rewardString.contains("%")) {
                        			  commissionNum= findOrders.getMoney() * 
                        					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                        					  0.01d;
                        		  }else {
                            		  commissionNum = Double.parseDouble(rewardString);
                        		  }
                        		  
                        		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                                  this.userService.saveOrUpdate(diUser);
                                  //更新订单中的描述
                                  summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后区域管理奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后区域管理奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  MainReward = false;
                			  }
                		  }else if (diUser.getLevel()==3) {//是社区合伙人
                			  if(areaReward) {
                				  //计算佣金
                        		  String rewardString=bounsRule.getTwoMaReward();
                        		  if(rewardString.contains("%")) {
                        			  commissionNum= findOrders.getMoney() * 
                        					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
                        					  0.01d;
                        		  }else {
                            		  commissionNum = Double.parseDouble(rewardString);
                        		  }
                        		  
                        		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                                  this.userService.saveOrUpdate(diUser);
                                  //更新订单中的描述
                                  summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后社区管理奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后社区管理奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  areaReward = false;
                			  }
                		  }
                		  if(shareholderReward==false && MainReward == false && areaReward == false) {
                			  //停止for循环
                        	  commission.setType(Integer.valueOf(1));
                              commission.setMoney(Double.valueOf(commissionNum));
                              commission.setNo(""+System.currentTimeMillis());
                              commission.setOperator(loginUser.getName());
                              commission.setUser(diUser);
                              commission.setCreateDate(date);
                              commission.setDeleted(false);
                              commission.setLevel(superList.length-1-i+1);
                              this.commissionService.saveOrUpdate(commission);
                			  break;
                		  }
                	  }
                	  
                	  
                	  commission.setType(Integer.valueOf(1));
                      commission.setMoney(Double.valueOf(commissionNum));
                      commission.setNo(""+System.currentTimeMillis());
                      commission.setOperator(loginUser.getName());
                      commission.setUser(diUser);
                      commission.setCreateDate(date);
                      commission.setDeleted(false);
                      commission.setLevel(superList.length-1-i+1);
                      this.commissionService.saveOrUpdate(commission);
                  }
            	  
              }

              summary = summary + "剩余金额："+remainMoney+"<br/>";
              //将订单描述存入订单中,保存订单信息
              findOrders.setStatus(Integer.valueOf(1));//更新订单支付状态（已支付）
              findOrders.setSummary(summary);
              findOrders.setPayDate(date);
              this.ordersService.saveOrUpdate(findOrders);
              
              //为购买的用户添加财务明细
              Financial financial = new Financial();
              financial.setType(Integer.valueOf(0));
              financial.setMoney(Double.valueOf(-findOrders.getMoney().doubleValue()));
              financial.setNo(""+System.currentTimeMillis());

              financial.setOperator(loginUser.getName());

              financial.setUser(findUser);

              financial.setCreateDate(new Date());
              financial.setDeleted(false);

              financial.setBalance(findUser.getBalance());
              financial.setPayment("余额付款");
              financial.setRemark("购买" + findOrders.getProductName());
     	      this.financialService.saveOrUpdate(financial);
     	      
          }//买的产品是市场开拓产品
          else if("8".equals(findOrders.getProductId()) || "9".equals(findOrders.getProductId()) ||
        		  "10".equals(findOrders.getProductId()) || "11".equals(findOrders.getProductId()) ||
        		  "12".equals(findOrders.getProductId())) {

        	  //从订单中更新卡密信息
        	  String summary = "卡密信息:<br/>";
              Date date = new Date();
              for (Kami kami : kamiList) {
                summary = summary + "卡号:" + kami.getNo() + ",密码:" + kami.getPassword() + "<br/>";
                //更新kami状态
                kami.setSaleTime(date);
                kami.setOrdersNo(findOrders.getNo());
                kami.setStatus(Integer.valueOf(1));
                this.kamiService.saveOrUpdate(kami);
              }
              
              //保存user，之后的操作与user无关
        	  this.userService.saveOrUpdate(findUser);
        	  
        	  //获取用户的推荐人链表
              String superListStr = findUser.getSuperior();
              String[] superList = superListStr.split(";");
              
              //设置三个boolean, 记录2级之后的管理奖是否被分完了，为true代表还没分，为false代表已经被分出去了。
              boolean areaReward=true;
              boolean MainReward=true;
              boolean shareholderReward=true;
              double remainMoney  = findOrders.getMoney();
              
              //第一个是空值,所以不遍历第一个,从尾部开始遍历
              for(int i=superList.length-1;i>0;i--) {
        		  //获取推荐人
        		  User diUser = (User)this.userService.getUserByNo(superList[i]);
        		  //根据推荐人的level,获取奖励规则
        		  BounsRule bounsRule = this.bounsRuleService.bounsRuleByLevel(diUser.getLevel());

                  //添加佣金明细
                  Commission commission = new Commission();
        		  double commissionNum =0.0d;
        		  
            	  //直接推荐人
            	  if(i==superList.length-1) {
            		  //计算佣金
            		  String rewardString=bounsRule.getDirectReward();
            		  if(rewardString.contains("%")) {
            			  commissionNum= findOrders.getMoney() * 
            					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
            					  0.01d;
            		  }else {
                		  commissionNum = Double.parseDouble(rewardString);
            		  }
            		  
            		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                      this.userService.saveOrUpdate(diUser);
                      //更新订单中的描述
                      summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                    		  "直接推荐人奖励："+commissionNum+"<br/>";
                      
                      
                      commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                    		  				"）购买产品 ["+findOrders.getProductName()+"] 的直接推荐奖励");
                      remainMoney = remainMoney - commissionNum;
            	  }//间接推荐人
            	  else if(i==superList.length-2){
            		  //计算佣金
            		  String rewardString=bounsRule.getIndirectReward();
            		  if(rewardString.contains("%")) {
            			  commissionNum= findOrders.getMoney() * 
            					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
            					  0.01d;
            		  }else {
                		  commissionNum = Double.parseDouble(rewardString);
            		  }
            		  
            		  diUser.setCommission(Double.valueOf(diUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                      this.userService.saveOrUpdate(diUser);
                      //更新订单中的描述
                      summary=summary+diUser.getNo()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                    		  "间接直接推荐人奖励："+commissionNum+"<br/>";
                      
                      
                      commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                    		  				"）购买产品 ["+findOrders.getProductName()+"] 的间接推荐奖励");
                      remainMoney = remainMoney - commissionNum;
                      
            	  }//区域奖
            	  else {
            		  //待实现
            	  }
            	  
            	 
            	  commission.setType(Integer.valueOf(1));
                  commission.setMoney(Double.valueOf(commissionNum));
                  commission.setNo(""+System.currentTimeMillis());
                  commission.setOperator(loginUser.getName());
                  commission.setUser(diUser);
                  commission.setCreateDate(date);
                  commission.setDeleted(false);
                  commission.setLevel(superList.length-1-i+1);
                  this.commissionService.saveOrUpdate(commission);
            	 
                  
            	  
              }
              summary = summary + "剩余金额："+remainMoney+"<br/>";
              //将订单描述存入订单中,保存订单信息

              findOrders.setStatus(Integer.valueOf(1));//更新订单支付状态（已支付）
              findOrders.setSummary(summary);
              findOrders.setPayDate(date);
              this.ordersService.saveOrUpdate(findOrders);
              
              //为购买的用户添加财务明细
              Financial financial = new Financial();
              financial.setType(Integer.valueOf(0));
              financial.setMoney(Double.valueOf(-findOrders.getMoney().doubleValue()));
              financial.setNo(""+System.currentTimeMillis());

              financial.setOperator(loginUser.getName());

              financial.setUser(findUser);

              financial.setCreateDate(new Date());
              financial.setDeleted(false);

              financial.setBalance(findUser.getBalance());
              financial.setPayment("余额付款");
              financial.setRemark("购买" + findOrders.getProductName());
     	      this.financialService.saveOrUpdate(financial);
        	  
        	  
        	  
          }//买的产品是普通产品
          else {
        	  this.userService.saveOrUpdate(findUser);
              
              
              findOrders.setStatus(Integer.valueOf(1));//更新订单支付状态（已支付）
              
              
              String summary = "卡密信息:<br/>";
              Date date = new Date();
              for (Kami kami : kamiList) {
                summary = summary + "卡号:" + kami.getNo() + ",密码:" + kami.getPassword() + "<br/>";
                //更新kami状态
                kami.setSaleTime(date);
                kami.setOrdersNo(findOrders.getNo());
                kami.setStatus(Integer.valueOf(1));
                this.kamiService.saveOrUpdate(kami);
              }
              findOrders.setSummary(summary);
              findOrders.setPayDate(date);
              this.ordersService.saveOrUpdate(findOrders);

              //添加财务明细
              Financial financial = new Financial();
              financial.setType(Integer.valueOf(0));
              financial.setMoney(Double.valueOf(-findOrders.getMoney().doubleValue()));
              financial.setNo(""+System.currentTimeMillis());

              financial.setOperator(loginUser.getName());

              financial.setUser(findUser);

              financial.setCreateDate(new Date());
              financial.setDeleted(false);

              financial.setBalance(findUser.getBalance());
              financial.setPayment("余额付款");
              financial.setRemark("购买" + findOrders.getProductName());
              this.financialService.saveOrUpdate(financial);
              
              Config findConfig = (Config)this.configService.findById(Config.class, 1);

              String levelNos = findUser.getSuperior();
              if (!StringUtils.isEmpty(levelNos)) {
                String[] leverNoArr = levelNos.split(";");
                int i = leverNoArr.length - 1; 
                for (int j = 1; i > 0; j++) {
                  if (!StringUtils.isEmpty(leverNoArr[i])) {
                    User levelUser = this.userService.getUserByNo(leverNoArr[i]);
                    if (levelUser != null)
                    {
                      //计算佣金
                      double commissionRate = 0.0D;
                      if (j == 1)
                        commissionRate = findConfig.getFirstLevel().doubleValue();//一级佣金
                      else if (j == 2)
                        commissionRate = findConfig.getSecondLevel().doubleValue();//二级佣金
                      else if (j == 3) {
                        commissionRate = findConfig.getThirdLevel().doubleValue();//三级佣金
                      }

                      double commissionNum = findOrders.getMoney().doubleValue() * commissionRate;
                      levelUser.setCommission(Double.valueOf(levelUser.getCommission().doubleValue() + commissionNum));//更新佣金总额
                      this.userService.saveOrUpdate(levelUser);

                      //添加佣金明细
                      Commission commission = new Commission();
                      commission.setType(Integer.valueOf(1));
                      commission.setMoney(Double.valueOf(commissionNum));
                      commission.setNo(""+System.currentTimeMillis());

                      commission.setOperator(loginUser.getName());

                      commission.setUser(levelUser);

                      commission.setCreateDate(date);
                      commission.setDeleted(false);
                      commission.setLevel(Integer.valueOf(j));
                      commission.setRemark("第" + j + "级用户:编号【" + loginUser.getNo() + "】购买商品奖励");
                      this.commissionService.saveOrUpdate(commission);
                    }
                  }
                  i--;
                }

              }

          }
          
          json.put("status", "1");
          json.put("message", "付款成功");
          json.put("no", findOrders.getNo());
        }
      }
    }

    PrintWriter out = null;
    try {
      out = this.response.getWriter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    out.print(json.toString());
    out.flush();
    out.close();
  }
/*
 * 订单详情
 */
  public void detail()
  {
    String no = this.request.getParameter("no");
    Orders findOrders = this.ordersService.findByNo(no);
    if (findOrders == null) {
      this.request.setAttribute("status", "0");
      this.request.setAttribute("message", "订单不存在");
    } else {
      HttpSession session = this.request.getSession();
      User loginUser = (User)session.getAttribute("loginUser");
      if (findOrders.getUser().getId() != loginUser.getId()) {
        this.request.setAttribute("status", "0");
        this.request.setAttribute("message", "没有权限");
      } else {
        this.request.setAttribute("orders", findOrders);
        try {
          this.request.getRequestDispatcher("ordersDetail.jsp").forward(this.request, this.response);
        } catch (ServletException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void indexList() {
    String pStr = this.request.getParameter("p");
    int p = 1;
    if (!StringUtils.isEmpty(pStr)) {
      p = Integer.parseInt(pStr);
    }

    String type = this.request.getParameter("type");
    HttpSession session = this.request.getSession();
    User loginUser = (User)session.getAttribute("loginUser");
    String countHql = "select count(*) from Orders where deleted=0 and user.id=" + loginUser.getId();
    String hql = "from Orders where deleted=0 and user.id=" + loginUser.getId();
    if (("0".equals(type)) || ("1".equals(type))) {
      countHql = countHql + " and status=" + type;
      hql = hql + " and status=" + type;
    }
    hql = hql + " order by id desc";

    int count = 0;
    count = this.ordersService.getTotalCount(countHql, new Object[0]);
    PageModel pageModel = new PageModel();
    pageModel.setAllCount(count);
    pageModel.setCurrentPage(p);
    List ordersList = this.ordersService.list(hql, pageModel.getStart(), pageModel.getPageSize(), new Object[0]);
    JSONObject json = new JSONObject();
    if (ordersList.size() == 0)
    {
      json.put("status", "0");

      json.put("isNextPage", "0");
    }
    else {
      json.put("status", "1");
      if (ordersList.size() == pageModel.getPageSize())
      {
        json.put("isNextPage", "1");
      }
      else {
        json.put("isNextPage", "0");
      }
      JSONArray listJson = (JSONArray)JSONArray.toJSON(ordersList);
      json.put("list", listJson);
    }
    PrintWriter out = null;
    try {
      out = this.response.getWriter();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    out.print(json);
    out.flush();
    out.close();
  }

  public void info()
  {
    String idStr = this.request.getParameter("id");
    String callbackData = "";
    PrintWriter out = null;
    try {
      out = this.response.getWriter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try
    {
      if ((idStr == null) || ("".equals(idStr))) {
        callbackData = BjuiJson.json("300", "参数不能为空", "", "", "", 
          "", "", "");
      } else {
        int id = 0;
        try {
          id = Integer.parseInt(idStr);
        }
        catch (Exception e) {
          callbackData = BjuiJson.json("300", "参数错误", "", "", "", 
            "", "", "");
        }
        Orders findorders = (Orders)this.ordersService.findById(
          Orders.class, id);
        if (findorders == null)
        {
          callbackData = BjuiJson.json("300", "订单不存在", "", "", 
            "", "", "", "");
        } else {
          this.cfg = new Configuration();

          this.cfg.setServletContextForTemplateLoading(
            this.request.getServletContext(), 
            "WEB-INF/templates/admin");
          Map root = new HashMap();
          root.put("orders", findorders);
          FreemarkerUtils.freemarker(this.request, this.response, this.ftlFileName, this.cfg, root);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    out.print(callbackData);
    out.flush();
    out.close();
  }

  public void update()
  {
    PrintWriter out = null;
    try {
      out = this.response.getWriter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String callbackData = "";
    try {
      if (this.orders == null) {
        callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", 
          "", "");
      } else {
        Orders findorders = (Orders)this.ordersService.findById(Orders.class, this.orders.getId().intValue());
        this.orders.setCreateDate(findorders.getCreateDate());
        this.orders.setDeleted(findorders.isDeleted());
        this.orders.setVersion(findorders.getVersion());
        boolean result = this.ordersService.saveOrUpdate(this.orders);

        if (result) {
          callbackData = BjuiJson.json("200", "修改成功", "", 
            "", "", "true", "", "");
        }
        else
          callbackData = BjuiJson.json("300", "修改失败", "", 
            "", "", "", "", "");
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    out.print(callbackData);
    out.flush();
    out.close();
  }

  public void delete()
  {
    PrintWriter out = null;
    try {
      out = this.response.getWriter();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String callbackData = "";
    try {
      String idStr = this.request.getParameter("id");

      if ((idStr == null) || ("".equals(idStr))) {
        callbackData = BjuiJson.json("300", "参数错误", "", "", "", "", 
          "", "");
      } else {
        int id = 0;
        try {
          id = Integer.parseInt(idStr);
        }
        catch (Exception e) {
          callbackData = BjuiJson.json("300", "参数错误", "", "", "", 
            "", "", "");
        }
        Orders findorders = (Orders)this.ordersService.findById(Orders.class, id);
        if (findorders == null)
        {
          callbackData = BjuiJson.json("300", "订单不存在", "", "", 
            "", "true", "", "");
        } else {
          boolean result = this.ordersService.delete(findorders);
          if (result)
            callbackData = BjuiJson.json("200", "删除成功", "", 
              "", "", "", "", "");
          else
            callbackData = BjuiJson.json("300", "删除失败", "", 
              "", "", "", "", "");
        }
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
    out.print(callbackData);
    out.flush();
    out.close();
  }

  public Orders getOrders()
  {
    return this.orders;
  }

  public void setOrders(Orders orders) {
    this.orders = orders;
  }

  public String getFtlFileName() {
    return this.ftlFileName;
  }

  public void setFtlFileName(String ftlFileName) {
    this.ftlFileName = ftlFileName;
  }
}
