package com.jju.cfiltering.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.entity.DynamicShare;
import com.jju.cfiltering.entity.User;
import com.jju.cfiltering.service.SaveDyShareService;
import com.jju.cfiltering.util.CFileUtil;
import com.jju.cfiltering.util.DyUtils;
import com.jju.cfiltering.util.MD5;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class SaveDyShareAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private File file;
	private SaveDyShareService sds;

	
	
	public String save() {
		String basePath = CFileUtil.getImgBasePath() 
				+ CFileUtil.getArticleImagePath();
		
		String urlDatabase = basePath.substring(basePath.replace("\\", "/").indexOf("/CFilteringUpload"), basePath.length()).replace("\\", "/");
		File f = new File(basePath);
		//不存在则创建
		if(!f.exists()){ 
			f.mkdirs(); 
		}
		//循环将文件上传到指定路径
		String randomName = null;
		JSONObject imageResult = new JSONObject();
		StringBuffer sb = new StringBuffer();
		HttpServletResponse re = ServletActionContext.getResponse();
		if(file != null) {
			Boolean flag = null;
			JSONArray storeImage = new JSONArray();
			if( ActionContext.getContext().getSession().get("shareImg") != null ) {
				flag = true;
				storeImage =  (JSONArray) ActionContext.getContext().getSession().get("shareImg");
			}
				try {
					//保存到数据库的图片地址
						//图片的随机名字
						randomName = "/"+CFileUtil.getRandomFileName()+".jpg";
						//全名
						if(flag == null) {
							//第一次取为空，则放入
							sb.append(urlDatabase + randomName);
							JSONArray inner = new JSONArray();
							inner.add(sb);
							ActionContext.getContext().getSession().put("shareImg", inner);
							storeImage = (JSONArray)ActionContext.getContext().getSession().get("shareImg");
						}else {
							//第二次取不为空，添加图片至session
							storeImage.add(urlDatabase + randomName);
						}
						System.out.println( storeImage.toJSONString());
						//读到本地
						FileUtils.copyFile(file, new File(f,randomName));
				}catch (IOException e) {
					imageResult.put("code", 404);
					imageResult.put("msg", "UPLOADFAIL");
					imageResult.put("data", new JSONObject());
					try {
						re.getWriter().print(imageResult);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
				JSONObject innerData = new JSONObject();
				//图片(每次讲最后一张返回)
				innerData.put("src", storeImage.get(storeImage.size() - 1));
				innerData.put("title", storeImage.get(storeImage.size() - 1));
				//返回数据
				imageResult.put("code", 200);
				imageResult.put("msg", "SUCCESS");
				imageResult.put("data",innerData);
				
				Cookie cookie = new Cookie("imgSrc", storeImage.get(storeImage.size() - 1) + "");
				cookie.setPath("/");
				ServletActionContext.getResponse().addCookie(cookie);
			try {
				re.getWriter().println(imageResult);
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		return null;
	}
	
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}


	private DynamicShare dynamicShare;
	
	public String saveArticle() throws IOException {
		JSONObject jsResult = new JSONObject();
		HttpServletResponse res = ServletActionContext.getResponse();
		try {
			StringBuffer sb = new StringBuffer();
			JSONArray jsonImg = (JSONArray) ActionContext.getContext().getSession().get("shareImg");
			//数组图片
			if( jsonImg != null) {
				Object [] artImg =  jsonImg.toArray();
				for (Object onePic : artImg) {
					sb.append(onePic);
					sb.append(";");
				}
			}
			dynamicShare.setPublishTime(CFileUtil.formateDate(new Date()));
			dynamicShare.setLastEditTime(CFileUtil.formateDate(new Date()));
			dynamicShare.setCheckStats(0);
			dynamicShare.setImageAddr(sb.toString());
			//普通登录
			User geneU = (User) ActionContext.getContext().getSession().get("userInfo");
			//github登录
			JSONObject gitU = (JSONObject) ActionContext.getContext().getSession().get("GitHubUserInfo");
			User gitUser = new User();
			//普通登录时
			if(gitU != null) {
				gitUser.setId(Integer.parseInt(gitU.getString("userId")) );
				gitUser.setName(gitU.getString("login"));
				gitUser.setProfile_img(gitU.getString("avatar_url"));
				gitUser.setPassword(MD5.getMd5("program1024"));
			}
			User u = geneU == null ? gitUser: geneU;
			sds.saveArticle(dynamicShare, u);
			ActionContext.getContext().getSession().remove("shareImg");
			System.out.println(ActionContext.getContext().getSession().get("shareImg") + " clear ");
			jsResult.put("code", 200);
			jsResult.put("msg", "SUCCESS");
			res.getWriter().println(jsResult);
		}catch(Exception e) {
			jsResult.put("code", 500);
			jsResult.put("msg", "ERROR");
			res.getWriter().println(jsResult);
			e.printStackTrace();
		}
		return null;
	}
	public DynamicShare getDynamicShare() {
		return dynamicShare;
	}
	public void setDynamicShare(DynamicShare dynamicShare) {
		this.dynamicShare = dynamicShare;
	}
	
	public SaveDyShareService getSds() {
		return sds;
	}
	public void setSds(SaveDyShareService sds) {
		this.sds = sds;
	}
	
	
	private Integer userId;
	private Integer start;
	private Integer size;
	private Integer count;
	public String findDyShare() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		List<Object[]> shareDetail = null;
		JSONObject data = null;
		if( count == null && size == null) {
			//猿分享，表明查询所有
			 shareDetail = sds.findDyShare();
			 data = DyUtils.parseShare(shareDetail);
		}else if( userId != null && count == null){
			//个人空间(用户登录之后)
			 shareDetail = sds.findPersonDyShare(userId,start,size);
			 data = DyUtils.parsePersonShare(shareDetail);
		}else if( count != null) {
			//主页中的分享查询
			shareDetail = sds.findPersonDyShare(count);
			data = DyUtils.parsePersonShare(shareDetail);
		}
		response.getWriter().println(data);
		return null;
	}
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}


	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}
	
	
	private Integer shareID;
	private Integer userID;
	private Integer goodCount;
	public String saveGoodCount() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		JSONObject re = new JSONObject();
		try {
			sds.saveGood(shareID, userID, goodCount);
			re.put("code", 200);
			re.put("msg", "SUCCESS");
			re.put("data", new JSONArray());
			response.getWriter().println(re);
		}catch(Exception e) {
			re.put("code", 404);
			re.put("msg", "ERROR");
			re.put("data", new JSONArray());
			response.getWriter().println(re);
			e.printStackTrace();
		}
		return null;
	}
	public Integer getShareID() {
		return shareID;
	}
	public void setShareID(Integer shareID) {
		this.shareID = shareID;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public Integer getGoodCount() {
		return goodCount;
	}
	public void setGoodCount(Integer goodCount) {
		this.goodCount = goodCount;
	}
	
	private String coments;
	public String saveOpPin() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		JSONObject re = new JSONObject();
		try {
			sds.saveOpPin(shareID, userID, coments);
			re.put("code", 200);
			re.put("msg", "SUCCESS");
			re.put("data", new JSONArray());
			response.getWriter().println(re);
		}catch(Exception e) {
			re.put("code", 404);
			re.put("msg", "ERROR");
			re.put("data", new JSONArray());
			response.getWriter().println(re);
			e.printStackTrace();
		}
		return null;
	}
	public String getComents() {
		return coments;
	}
	public void setComents(String coments) {
		this.coments = coments;
	}
}
