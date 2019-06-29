package com.github.auth.login;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import com.alibaba.fastjson.JSONObject;
import com.github.auth.utils.AuthUtil;
import com.jju.cfiltering.entity.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	public String Login() {	
		HttpServletResponse response = ServletActionContext.getResponse();
			// Github认证服务器地址
			String url = "https://github.com/login/oauth/authorize";
			// 传递参数response_type、client_id、state、redirect_uri
			String param = "client_id=" + AuthUtil.GITHUB_CLIENT_ID
					+"&response_type=code"
					+"&scope=user"
					+ "&redirect_uri=" + AuthUtil.GITHUB_REDIRECT_URL;
			//重定向github服务器
			try {
				System.out.println("redirect before");
				response.sendRedirect(url + "?" + param);
				System.out.println("redirect after");
			} catch (IOException e) {
				e.printStackTrace();
			}
        	return null;
	}

}
