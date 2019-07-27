 package com.hansan.fenxiao.pay.alipay;
 
 public class AlipayConfig
 {
//   public static String partner = "285684519923417943";
// 
//   public static String seller_email = "ishangluo1@qq.com";
// 
//   public static String key = "72e2043ee4e74164b66f8fb9d1545248";
// 
//   public static String log_path = "D:\\";
// 
//   public static String input_charset = "utf-8";
// 
//   public static String sign_type = "MD5";
	 
	 
	// 商户appid
	 public static String APPID = "2016101100658444";
	 
	 // 私钥 pkcs8格式的 沙箱环境，之后需换成正式环境的
	 public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQClcGOcorReC9Z42BQa9Yx31LXzLce5F2wQ6lRT3n2hWrSAZIqErAEdZ69c0fpxmYt+G/mIVAg9U1tjwVL26ockxxCqz0taibvonz8mM6uz+Q5mqE6fg8cXc0Rc9v1iJEcfX8VoiTs/4qeXsGzjNacov4KZww1VysHUXjaQlJrz5+hOlypU0ZVhWWPBuuJZceh3lAVuuwUAHtvt2Rt5dxiMC4pkdxBPrmKJKUQzWgsA8xLNHT11KETlDzuH24cvYJpBSKH79T3+Drbx2c/ausrwCHnrLs3exfknvxl/wKmwO41XnKrtFcdMde2yJDDLUX8at803IQ8D0YCMPMudwTIbAgMBAAECggEAfh33UekW5rrPfL/8U7DsrE0Th5ZWstsQHrUEJAtwahjjkR0UG5+kGe542KGZ8g/DLxfUWgD1ZwqsmJc/z8tpyreWFSCBqTtmbCc51L1/H2u1drzHcwLaOx4790KyVqahMWGo88/pwwHeEN0FpQ+5pm6X+O+gss4mhOZtYjpnBZd+e12rx9xXeNx2m9A1kTusqRUyRGMaCPdrwJdkpIHLdJImn0iNTSezmpHo1uvyqTp0EczFdh+S4SxQG8tEGuWMJTtl0x27Ui+ut2LhN4KqcI5Q3RwDX8e9coHeoxDgRoaTWJxUTrlqKOBnwTg30+saJjnwPj3y7jHh+m5yZZI7wQKBgQDzAtqaGc1sdItjDYANhn02U5vlbGWwvDyXjHdWR4UkYxH/NotbtDu3umghLyLrmReW4R98BMUM4oUa3qpE8bQkeR8iB5hZXh9Xs5PGrhhfvIaz8v71UofYYHMyUJA2jbxX3ACM/guykUn1nGuqETdkpOqLN1YbH3j4wAV1ZhIhLwKBgQCuSBuhS9KaXc06ArIx/kdjnWvfHAIfMxw/a5KwWcxSkyiLyXOdNksF9saxv8YY8XwmD6aRVvI8zWBEcnMiex0H/SsKuQcvZWHHPyrQQ5yL+prlBkNwHqcqcPgboTvUIHQCH4As9mOF0YppymBN7kVR1am5kwHWByo/q1Z3cSxK1QKBgQDlubLxrqa1ek8yala9cn/31X4wmhJfyvL0yQMfeyjwRv+CZgvDu1Qg0YUSqb63PdhA97yNvuyQy6rMxeXsmxmSyTWXM9rf0MyMWeS/E2FTT6N/hZ1kSIrHEBaWX/aUu9Rqsj2LC7X/Lr3eA+r7sJ7O8+ANj1hE97GiZdRDMSS+XQKBgFCWRzBuPxPiC+OnG37CdYA6aobgDJsgpw8sTsf8vd3w7e61WCB52ncHhRjr9E3UW/O9iOfrrFcxIDKGOXbYNrRa/GwY4pmn526aZtGmcdzIJJeCLezKP8IAOrmL9H5CA2J37mzcAWj/W2FPkvh2FNeiVLjaoR62cXK/Z1wTmMeBAoGBANcLGdPrpygXLRPxzKPX0BgA9vbuwOk8q2QPGyvUMWi8t4AbA58pQVP4P2b/5CmMePEXgs4wxJNx4+6l7vDUYhGrL/+VrEADj9cIuBDnEr9rmxCJBM/GDxIDS++EOXVLb2K41Aip7fXZgqUeOsecrANiQm8YeLRDpis5eyd+4ELK";
	 
	 // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	 public static String notify_url = "http://aiwac.net/fenxiao/aliNotifyUrl";
	 // 页面跳转同步通知页面路径,即用户支付后看到的页面 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	 public static String return_url = "http://aiwac.net/fenxiao/user/index.jsp";
	 // 请求网关地址
	 // public static String URL = "https://openapi.alipay.com/gateway.do";
	 
	 //测试地址
	 //public static String notify_url = "http://c6m42n.natappfree.cc/fenxiao/aliNotifyUrl";
	 //public static String return_url = "http://c6m42n.natappfree.cc/fenxiao/user/index.jsp";	 
	 // 沙箱测试环境网关，之后换成正式网关
	 public static String URL = "https://openapi.alipaydev.com/gateway.do";
	
	 // 编码
	 public static final String CHARSET = "UTF-8";
	 // 返回格式
	 public static String FORMAT = "json";
	 // 支付宝公钥
	 public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkyY1jDUdU1Fts1bMEvEgLnytj8PLbMgiFl+wD2R/q1fEFIJ4jHNvaz9Cr6zLQfjblS/KrOmThDJH41TBgpjJnS1DhvciFhWoLRjOoka0AZxNHAGupoa3ttMy7Cpqfc/7wkaJH0oW3JpLjTqD7o9AOkcDmkl+dujaaERHXqoEeFxqNN21RknHuNeWcJ6G/+FZRsf1CicHoyoxTYhFa52X3+IpY85DDUT6rdwVcbHqbq38wjqyKh0KN/s7l0BvKzQ/sLyRImRVpKM4r3qmfElwyYZQAUV5T1nVO1UwpUsLWERwnvhNi0MQpISRa3Q2wMCIUmAejd8TzN15UdSN2lzPLwIDAQAB";
     // 日志记录目录
	 public static String log_path = "/log";
	 // RSA2
	 public static String SIGNTYPE = "RSA2";
	 
	 //商户邮箱 这里是测试账号，需换成正式邮箱
	 public static String seller_email = "epulkx0598@sandbox.com";
 }