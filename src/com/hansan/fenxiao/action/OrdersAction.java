package com.hansan.fenxiao.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hansan.fenxiao.entities.BounsRule;
import com.hansan.fenxiao.entities.Commission;
import com.hansan.fenxiao.entities.Config;
import com.hansan.fenxiao.entities.Financial;
import com.hansan.fenxiao.entities.GroupRule;
import com.hansan.fenxiao.entities.Kami;
import com.hansan.fenxiao.entities.Orders;
import com.hansan.fenxiao.entities.Product;
import com.hansan.fenxiao.entities.User;
import com.hansan.fenxiao.service.IBounsRuleService;
import com.hansan.fenxiao.service.ICommissionService;
import com.hansan.fenxiao.service.IConfigService;
import com.hansan.fenxiao.service.IFinancialService;
import com.hansan.fenxiao.service.IGroupRuleService;
import com.hansan.fenxiao.service.IKamiService;
import com.hansan.fenxiao.service.IOrdersService;
import com.hansan.fenxiao.service.IProductService;
import com.hansan.fenxiao.service.IUserService;
import com.hansan.fenxiao.utils.BjuiJson;
import com.hansan.fenxiao.utils.BjuiPage;
import com.hansan.fenxiao.utils.FreemarkerUtils;
import com.hansan.fenxiao.utils.PageModel;
import com.sun.glass.ui.Window.Level;

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
  
  @Resource(name="groupRuleService")
  private IGroupRuleService<GroupRule> groupRuleService;
  
  private Orders orders;
  private String ftlFileName;
  private int level398 = 1;
  private int level990 = 2;
  private int level3980 = 3;
  private int level10000 = 4; //区级
  private int level100000 = 5; //市级
  private int level200000 = 6; //省级

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
       } else if(checkIdentity(loginUser, findProduct.getLevel())){    	 
    	  this.request.setAttribute("status", "0");
          this.request.setAttribute("message", "您所在地区已有此级别代理（股东），无法购买");              
       } else {
    	   User user = this.userService.getUserByPhone(loginUser.getPhone());
//    	   if (user.getLevel() < findProduct.getLevel() && findProduct.getLevel() > level398) {
//    		   this.request.setAttribute("status", "0");
//               this.request.setAttribute("message", "您的权限不足，无法购买此产品");
//    	   }else {
	    	   if (user.getReBuyStatus() == 1 && findProduct.getLevel() == level398) {
	    		     findProduct.setMoney(findProduct.getRebuy());
	    	   }
	    	   	this.request.setAttribute("status", "1");
	    	   	this.request.setAttribute("rebuy", user.getReBuyStatus());
	    	   	this.request.setAttribute("product", findProduct);
    	   }
       }
//    }
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
    	User user = this.userService.getUserByName(loginUser.getName());
        Orders newOrders = new Orders();
        newOrders.setProductId(""+findProduct.getId());
        newOrders.setProductName(findProduct.getTitle());
        newOrders.setProductNum(Integer.valueOf(1));
        
        if(user.getReBuyStatus() != 0 && findProduct.getLevel()==1) {
        	newOrders.setProductMoney(findProduct.getRebuy());
        } else {
        	newOrders.setProductMoney(findProduct.getMoney());        	
        }
        
        newOrders.setUser(loginUser);
        newOrders.setUser(user);
        
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
	int reward = Integer.parseInt(this.request.getParameter("reward"));
    String no = this.request.getParameter("no");//订单号
    Orders findOrders = this.ordersService.findByNo(no);//订单
    HttpSession session = this.request.getSession();
    User loginUser = (User)session.getAttribute("loginUser");//登录用户
    loginUser = this.userService.getUserByNo(loginUser.getNo());
    Double existCommission = loginUser.getCommission();
    String summary = "";
    Product product = new Product();
    int productLevel = 0;    
    JSONObject json = new JSONObject();
    Double useReward = 0.00d;   
    if ((loginUser == null) || (loginUser.getId() == null)) {
      json.put("status", "0");
      json.put("message", "您未登陆或者登陆失效，请重新登陆");
      json.put("href", "../login.jsp");
    } else if (findOrders == null) {
        json.put("status", "0");
        json.put("message", "订单不存在");
     }else{
      User findUser = (User)this.userService.findById(User.class, loginUser.getId().intValue());
      product = this.productService.findById(Product.class, Integer.parseInt(findOrders.getProductId()));
      productLevel = product.getLevel();
      if (findOrders.getUser().getId() != findUser.getId()) {
    	//登录用户和订单用户不一致
        json.put("status", "0");
        json.put("message", "登录用户非本订单用户，没有权限支付");
      } else if ((findUser.getBalance().doubleValue() + existCommission < findOrders.getMoney().doubleValue() && reward == 1) || (findUser.getBalance() < findOrders.getMoney() && reward == 0)) {
        json.put("status", "0");
        json.put("message", "余额不足，请先充值");
      } else if (findOrders.getStatus().intValue() == 1) {
        json.put("status", "0");
        json.put("message", "该订单已付款，请不要重复提交支付");
      }else if (checkIdentity(findUser, productLevel)) {
          json.put("status", "0");
          json.put("message", "您所在地区已有此级别代理（股东），无法购买");
      } else {
        List<Kami> kamiList = this.kamiService.list("from Kami where deleted=0 and status=0 and product.id=" + findOrders.getProductId(), 0, findOrders.getProductNum().intValue(), new Object[0]);
        if (kamiList.size() < findOrders.getProductNum().intValue()) {
          //未销售产品kami数量小于订单购买数量
          json.put("status", "0");
          json.put("message", "库存不足，请联系管理员");
        } else {
        	//更新用户资金，含佣金抵扣
        	if (reward == 1 && existCommission > 0) {           	
            	if (existCommission <= findOrders.getMoney()) {
            		findUser.setBalance(Double.valueOf(findUser.getBalance().doubleValue() - findOrders.getMoney().doubleValue())+existCommission);//更新金额总数
            		findUser.setCommission(0.00);
            		summary += "本次购买使用佣金抵扣 ￥"+ existCommission + "元 <br>";
            		useReward = existCommission;
            	}else {
            		existCommission -= findOrders.getMoney();
            		findUser.setCommission(existCommission);
            		summary += "本次购买使用佣金抵扣 ￥"+ findOrders.getMoney() + "元 <br>";
            		useReward = findOrders.getMoney();
            	}            	
            }else {
            	findUser.setBalance(Double.valueOf(findUser.getBalance().doubleValue() - findOrders.getMoney().doubleValue()));//更新金额总数
            }
          
          //如果用户未激活，则激活用户，并记录激活时间
          if (findUser.getStatus().intValue() == 0) {
            findUser.setStatus(Integer.valueOf(1));
            findUser.setStatusDate(new Date());//
          }
          
          
          String superListStr = findUser.getSuperior();
          double remainMoney  = findOrders.getMoney();
          Date date = new Date();
          
          //买的产品是零售产品
          if(level398 == productLevel) {
        	  if (superListStr != null) {
        	  //从订单中更新卡密信息
//        	  summary = "卡密信息:<br/>";
//              date = new Date();
//              for (Kami kami : kamiList) {
//                summary = summary + "卡号:" + kami.getNo() + ",密码:" + kami.getPassword() + "<br/>";
//                //更新kami状态
//                kami.setSaleTime(date);
//                kami.setOrdersNo(findOrders.getNo());
//                kami.setStatus(Integer.valueOf(1));
//                this.kamiService.saveOrUpdate(kami);
//              }

              
              if(findUser.getReBuyStatus()==1) {//是复购
            	  
            	  
            	  //获取用户的推荐人链表
                  superListStr = findUser.getSuperior();
                  String[] superList = superListStr.split(";");
                  
                  //设置三个boolean, 记录2级之后的管理奖是否被分完了，为true代表还没分，为false代表已经被分出去了。
                  boolean firstReward = true;
                  boolean secondReward = true;
                  boolean areaReward=true;
                  boolean MainReward=true;
                  boolean shareholderReward=true; //level00000
                  boolean stockholderReward=true; //level200000
                  
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
                	  if(firstReward) {
                		  //计算佣金
                		  //如果是普通vip，直推顺延为其上级获得
                		  if (diUser.getLevel() == level398) {
                			  continue;
                		  }
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
                          summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                        		  "直接推荐人复购奖励："+commissionNum+"<br/>";
                          
                          
                          commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                        		  				"）购买产品 ["+findOrders.getProductName()+"] 的直接推荐复购奖励");
                          remainMoney = remainMoney - commissionNum;
                          firstReward = false;
                		  
                	  }//间接推荐人
                	  else if(secondReward){
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
                          summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                        		  "间接直接推荐人复购奖励："+commissionNum+"<br/>";
                          
                          
                          commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                        		  				"）购买产品 ["+findOrders.getProductName()+"] 的间接推荐复购奖励");
                          remainMoney = remainMoney - commissionNum;
                          secondReward = false;
                	  }//二级之后的管理奖
                	  else {
                		  //二级之后,只有:社区合伙人  区域合伙人 总代合伙人 联创股东可以享有, 一共四笔,分完即止
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
                                  summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后总代管理复购奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
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
                                  summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后区域管理复购奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
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
                                  summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后社区管理复购奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后社区管理复购奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  areaReward = false;
                			  }
                		  } else if(diUser.getLevel()==6) {
                			  if(stockholderReward) {
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
                                  summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后股东管理复购奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后股东管理复购奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  shareholderReward=false;
                			  }
                		  }
                		  if(shareholderReward==false && MainReward == false && areaReward == false && stockholderReward == false) {
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
                  superListStr = findUser.getSuperior();
                  
                  String[] superList = superListStr.split(";");
                  
                  //设置三个boolean, 记录2级之后的管理奖是否被分完了，为true代表还没分，为false代表已经被分出去了。
                  boolean areaReward=true;
                  boolean MainReward=true;
                  boolean shareholderReward=true;
                  boolean stockholderReward=true;
                  
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
                          summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                        		  "直接推荐人奖励："+commissionNum+"<br/>";
                          
                          
                          commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
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
                          summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                        		  "间接直接推荐人奖励："+commissionNum+"<br/>";
                          
                          
                          commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                        		  				"）购买产品 ["+findOrders.getProductName()+"] 的间接推荐奖励");
                          remainMoney = remainMoney - commissionNum;
                          
                	  }//二级之后的管理奖
                	  else {
                		  //二级之后,只有:社区合伙人  区域合伙人 总代合伙人 联创股东可以享有, 一共四笔,分完即止
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
                                  summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后总代管理奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
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
                                  summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后区域管理奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
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
                                  summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后社区管理奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后社区管理奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  areaReward = false;
                			  }
                		  } else if(diUser.getLevel()==6) {
                			  if(stockholderReward) {
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
                                  summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                                		  "2级之后股东管理复购奖励："+commissionNum+"<br/>";
                                  
                                  
                                  commission.setRemark("来自用户（编号："+loginUser.getName()+",用户名："+loginUser.getName()+
                                		  				"）购买产品 ["+findOrders.getProductName()+"] 的2级之后股东管理复购奖励");
                                  remainMoney = remainMoney - commissionNum;
                                  
                				  shareholderReward=false;
                			  }
                		  }
                		  if(shareholderReward==false && MainReward == false && areaReward == false && stockholderReward == false) {
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
        	  }
        	  //保存user，之后的操作与user无关
        	  if (findUser.getLevel() < product.getLevel())
        		  findUser.setLevel(product.getLevel());
        	  if (findUser.getReBuyStatus() == 0 && product.getLevel() == level398) {
            	  findUser.setReBuyStatus(Integer.valueOf(1));
              }
        	  this.userService.saveOrUpdate(findUser);
        	  
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
          else if(level990 == productLevel || level3980 == productLevel ||  level10000 == productLevel || level100000 == productLevel ||
        		  level200000 == productLevel) {

        	  
        	  if (superListStr != null) {
        		//从订单中更新卡密信息
//        	  summary = "卡密信息:<br/>";
//        	  date = new Date();
//              for (Kami kami : kamiList) {
//                summary = summary + "卡号:" + kami.getNo() + ",密码:" + kami.getPassword() + "<br/>";
//                //更新kami状态
//                kami.setSaleTime(date);
//                kami.setOrdersNo(findOrders.getNo());
//                kami.setStatus(Integer.valueOf(1));
//                this.kamiService.saveOrUpdate(kami);
//              }
              
        	  
        	  //获取用户的推荐人链表
              superListStr = findUser.getSuperior();
             
              String[] superList = superListStr.split(";");

              remainMoney  = findOrders.getMoney();
              
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
                      summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                    		  "直接推荐人奖励："+commissionNum+"<br/>";
                      
                      
                      commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                    		  				"）购买产品 ["+findOrders.getProductName()+"] 的直接推荐奖励");
                      remainMoney = remainMoney - commissionNum;
                      commission.setType(Integer.valueOf(1));
                      commission.setMoney(Double.valueOf(commissionNum));
                      commission.setNo(""+System.currentTimeMillis());
                      commission.setOperator(loginUser.getName());
                      commission.setUser(diUser);
                      commission.setCreateDate(date);
                      commission.setDeleted(false);
                      commission.setLevel(superList.length-1-i+1);
                      this.commissionService.saveOrUpdate(commission);
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
                      summary=summary+diUser.getPhone()+" "+diUser.getName()+" "+bounsRule.getIdentityName()+" "+
                    		  "间接直接推荐人奖励："+commissionNum+"<br/>";
                      
                      
                      commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                    		  				"）购买产品 ["+findOrders.getProductName()+"] 的间接推荐奖励");
                      remainMoney = remainMoney - commissionNum;
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
              
              //区域奖
              String address = findUser.getAddress();
              Commission commission = new Commission();
              double commissionNum =0.0d;
              
              int province = Integer.parseInt(address.split("\\|")[0]);
              int city = Integer.parseInt(address.split("\\|")[1]);
              int area = Integer.parseInt(address.split("\\|")[2]);
              List<User> pList = this.userService.list("from User where deleted = 0 and level = "+level200000);
              List<User> cList = this.userService.list("from User where deleted = 0 and level = "+level100000);
              List<User> aList = this.userService.list("from User where deleted = 0 and level = "+level10000);
              for (User puser:pList) {
            	  if (puser.getId() == findUser.getId()) continue;
            	  int p = Integer.parseInt(puser.getAddress().split("\\|")[0]);
            	  if (p == province) {
            		  BounsRule bounsRule = this.bounsRuleService.bounsRuleByLevel(puser.getLevel());
            		  String rewardString = bounsRule.getRegionalReward();
            		  if(rewardString.contains("%")) {
            			  commissionNum= findOrders.getMoney() * 
            					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
            					  0.01d;
            		  }else {
                		  commissionNum = Double.parseDouble(rewardString);
            		  }
            		  commissionNum = Double.valueOf(String .format("%.2f", commissionNum));
            		  summary=summary+puser.getPhone()+" "+puser.getName()+" "+bounsRule.getIdentityName()+" "+
                    		  "区域奖励："+commissionNum+"<br/>";
                      
                      
                      commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                    		  				"）购买产品 ["+findOrders.getProductName()+"] 的"+bounsRule.getIdentityName()+"区域奖励");
                      remainMoney = remainMoney - commissionNum;
                      remainMoney = Double.valueOf(String .format("%.2f", remainMoney));
                      commission.setType(Integer.valueOf(1));
                      commission.setMoney(Double.valueOf(commissionNum));
                      commission.setNo(""+System.currentTimeMillis());
                      commission.setOperator(loginUser.getName());
                      commission.setUser(puser);
                      commission.setCreateDate(date);
                      commission.setDeleted(false);
                      commission.setLevel(level200000);
                      this.commissionService.saveOrUpdate(commission);
                      break;
            	  }         	  
              }
              for (User cuser:cList) {
            	  if (cuser.getId() == findUser.getId()) continue;
            	  int p = Integer.parseInt(cuser.getAddress().split("\\|")[0]);
            	  int c = Integer.parseInt(cuser.getAddress().split("\\|")[1]);
            	  if (c == city && p == province) {
            		  BounsRule bounsRule = this.bounsRuleService.bounsRuleByLevel(cuser.getLevel());
            		  String rewardString = bounsRule.getRegionalReward();
            		  if(rewardString.contains("%")) {
            			  commissionNum= findOrders.getMoney() * 
            					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
            					  0.01d;
            		  }else {
                		  commissionNum = Double.parseDouble(rewardString);
            		  }
            		  commissionNum = Double.valueOf(String .format("%.2f", commissionNum));
            		  summary=summary+cuser.getPhone()+" "+cuser.getName()+" "+bounsRule.getIdentityName()+" "+
                    		  "区域奖励："+commissionNum+"<br/>";
                      
                      
                      commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                    		  				"）购买产品 ["+findOrders.getProductName()+"] 的"+bounsRule.getIdentityName()+"区域奖励");
                      remainMoney = remainMoney - commissionNum;
                      commission.setType(Integer.valueOf(1));
                      commission.setMoney(Double.valueOf(commissionNum));
                      commission.setNo(""+System.currentTimeMillis());
                      commission.setOperator(loginUser.getName());
                      commission.setUser(cuser);
                      commission.setCreateDate(date);
                      commission.setDeleted(false);
                      commission.setLevel(level200000);
                      this.commissionService.saveOrUpdate(commission);
                      break;
            	  }
              }
              for (User auser:aList) {
            	  if (auser.getId() == findUser.getId()) continue;
            	  int a = Integer.parseInt(auser.getAddress().split("\\|")[2]);
            	  int p = Integer.parseInt(auser.getAddress().split("\\|")[0]);
            	  int c = Integer.parseInt(auser.getAddress().split("\\|")[1]);
            	  if (a == area && c == city && p == province) {
            		  BounsRule bounsRule = this.bounsRuleService.bounsRuleByLevel(auser.getLevel());
            		  String rewardString = bounsRule.getRegionalReward();
            		  if(rewardString.contains("%")) {
            			  commissionNum= findOrders.getMoney() * 
            					  Double.parseDouble(rewardString.substring(0,rewardString.length()-1)) * 
            					  0.01d;
            		  }else {
                		  commissionNum = Double.parseDouble(rewardString);
            		  }
            		  commissionNum = Double.valueOf(String .format("%.2f", commissionNum));
            		  summary=summary+auser.getPhone()+" "+auser.getName()+" "+bounsRule.getIdentityName()+" "+
                    		  "区域奖励："+commissionNum+"<br/>";
                      
                      
                      commission.setRemark("来自用户（手机号："+loginUser.getPhone()+",用户名："+loginUser.getName()+
                    		  				"）购买产品 ["+findOrders.getProductName()+"] 的"+bounsRule.getIdentityName()+"区域奖励");
                      remainMoney = remainMoney - commissionNum;
                      commission.setType(Integer.valueOf(1));
                      commission.setMoney(Double.valueOf(commissionNum));
                      commission.setNo(""+System.currentTimeMillis());
                      commission.setOperator(loginUser.getName());
                      commission.setUser(auser);
                      commission.setCreateDate(date);
                      commission.setDeleted(false);
                      commission.setLevel(level200000);
                      this.commissionService.saveOrUpdate(commission);
                      break;
            	  }
              }
          }
          	
              summary = summary + "剩余金额："+remainMoney+"<br/>";
          
              //将订单描述存入订单中,保存订单信息

              //保存user，之后的操作与user无关
              if (findUser.getLevel() < product.getLevel())
            	  findUser.setLevel(product.getLevel());
        	  this.userService.saveOrUpdate(findUser);
              findOrders.setStatus(Integer.valueOf(1));//更新订单支付状态（已支付）
              findOrders.setSummary(summary);
              findOrders.setPayDate(date);
              findOrders.setRemainMoney(remainMoney);
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
              
              if (superListStr != null) {
              summary = "卡密信息:<br/>";
              date = new Date();
//              for (Kami kami : kamiList) {
//                summary = summary + "卡号:" + kami.getNo() + ",密码:" + kami.getPassword() + "<br/>";
//                //更新kami状态
//                kami.setSaleTime(date);
//                kami.setOrdersNo(findOrders.getNo());
//                kami.setStatus(Integer.valueOf(1));
//                this.kamiService.saveOrUpdate(kami);
//              }
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
                      commission.setRemark("第" + j + "级用户:手机号【" + loginUser.getPhone() + "】购买商品奖励");
                      this.commissionService.saveOrUpdate(commission);
                    }
                  }
                  i--;
                }

              }

          }          
          
          
        }
          
          json.put("status", "1");
          json.put("message", "付款成功");
          if (reward == 1 ) {
        	  if (existCommission > 0) {
        		  json.put("message", "付款成功，使用佣金抵扣 ￥"+useReward+"元");
        	  } else {
        		  json.put("message", "付款成功，佣金不足，使用了余额支付");
        	  }
        		  
          }
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
    if (json.get("status") == "1") {
    	loginUser.setStatus(1);
    	session.setAttribute("loginUser", loginUser);
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
  
  //判断是否已经有了某一级别的代理，能否购买，true为已存在，false为可以购买
  public boolean checkIdentity(User user, int level) {
	  String address = user.getAddress();
	  String area = address.split("\\|")[2];
	  String city = address.split("\\|")[1];
	  String province = address.split("\\|")[0];
	  switch (level) {
	  	case 4 :		  
          List<User> aList = this.userService.list("from User where deleted = 0 and level = "+level10000);
		  for (User aUser:aList) {
			  if (aUser.getAddress().split("\\|")[2].equals(area) && aUser.getAddress().split("\\|")[1].equals(city) && aUser.getAddress().split("\\|")[0].equals(province)) {
				  return true;
			  }
		  } 
		  break;
	  	case 5 :	  		
	  		List<User> cList = this.userService.list("from User where deleted = 0 and level = "+level100000);	  		
	  		for (User cUser:cList) {	  			 
				  if (cUser.getAddress().split("\\|")[1].equals(city) && cUser.getAddress().split("\\|")[0].equals(province)) {
					  return true;
				  }
			  } 
	  		break;
	  	case 6 :
	  		List<User> pList = this.userService.list("from User where deleted = 0 and level = "+level200000);
	  		for (User pUser:pList) {
				  if (pUser.getAddress().split("\\|")[0].equals(province)) {
					  return true;
				  }
			}
	  		break;
	  	}	  
	  return false;
  }
}
