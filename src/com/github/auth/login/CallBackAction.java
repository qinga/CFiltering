package com.github.auth.login;



import java.util.Map;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONObject;
import com.github.auth.utils.AuthUtil;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.util.MD5;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class CallBackAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	
	public String callBack() {
	    Map<String,Object> params = ServletActionContext.getContext().getParameters();
	    String[] code = (String[]) params.get("code");
	    System.out.println("code="+code[0].toString());
		String url = "https://github.com/login/oauth/access_token";
	    // 传递参数code、redirect_uri、client_id
	    String param = "client_id=" + AuthUtil.GITHUB_CLIENT_ID
	    		+ "&client_secret=" + AuthUtil.GITHUB_CLIENT_SECRET
	    		+ "&grant_type=authorization_code"
	    		+ "&code=" + code[0];
	    //通过code换取access_token,成功后以json格式返回access_token token_type
	    JSONObject jsonObject = AuthUtil.doPostJson(url+ "?" + param);
	    //2.获取access_token(GET),用access_token请求授权用户信息
	    String accessToken = jsonObject.getString("access_token");
	    String tokenType = jsonObject.getString("token_type");
	    
	    String userUrl = "https://api.github.com/user?access_token="+accessToken
	    		+ "&token_type=" + tokenType;
	    JSONObject userInfo = AuthUtil.doGetJson(userUrl);
	    //用户github的所有信息
	    if(userInfo != null){
	    	Map<String,Object> session = ActionContext.getContext().getSession();
	    	session.put("GitHubUserInfo", userInfo);
	    	User user = new User();
	    	user.setName(userInfo.getString("login"));
	    	user.setPassword(MD5.getMd5("program1024"));
	    	user.setProfile_img(userInfo.getString("avatar_url"));
	    	//重定向到 注册action 保存用户信息
	    	ActionContext.getContext().put("user", user);
	    	return "SAVE_USERINFO_CHAIN";
	    }else{
	    	return "RELOGIN";
	    }
	}
	

}
