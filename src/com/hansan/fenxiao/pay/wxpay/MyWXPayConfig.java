package com.hansan.fenxiao.pay.wxpay;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.hansan.fenxiao.pay.wxpay.WXPayConfig;

public class MyWXPayConfig extends WXPayConfig{
	
	private byte[] certData;

    public MyWXPayConfig() throws Exception {
        String certPath = "/path/to/apiclient_cert.p12";//微信支付证书，需替换正式文件
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return "wx8888888888888888";
    }

    public String getMchID() {
        return "12888888";
    }

    public String getKey() {
        return "88888888888888888888888888888888";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

	@Override
	IWXPayDomain getWXPayDomain() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean getAutoReport() {
        return false;
    }
	
	public boolean getUseSandBox() {
        return false;
    }
	
	public String getNotifyUrl(){
		return "http://aiwac.net/fenxiao/wxNotifyUrl";
	}
}
