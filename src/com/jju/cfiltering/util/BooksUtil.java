package com.jju.cfiltering.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.entity.Books;
import com.jju.cfiltering.entity.BooksImg;

public class BooksUtil {

	public static void returnResultData(List<Books> allBookInfo) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		JSONObject resultData  = new JSONObject();
		if(allBookInfo.size() > 0) {
			JSONArray tempResultData = new JSONArray();
			 
			for(Books book : allBookInfo) {
				JSONObject jsonObject1 = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				
				jsonObject1.put("bookId", book.getId());
				jsonObject1.put("name", book.getName());
				jsonObject1.put("author", book.getAuthor().getName());
				jsonObject1.put("publish", book.getPublish().getName());
				jsonObject1.put("bookVersion", book.getBookVersion());
				jsonObject1.put("createTime", book.getCreateTime());
				jsonObject1.put("lastEditTime", book.getLastEditTime());
				jsonObject1.put("newBookFlag", book.getNewBookFlag());
				jsonObject1.put("skillBoostFlag", book.getSkillBoostFlag());
				jsonObject1.put("beginBookFlag", book.getBeginBookFlag());
				jsonObject1.put("bookDesc", book.getBookDesc());
			
				for(BooksImg bmg : book.getBooksImg()) {
					jsonArray.add(bmg.getImgAddr());
				}
				jsonObject1.put("imageAddr", jsonArray);
				tempResultData.add(jsonObject1);
			}
			//不为空时，返回数据
			try {
				resultData.put("bookInforData", tempResultData);
				response.getWriter().print(resultData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			//书籍为空时返回
			try {
				resultData.put("stats", "EMPTY");
				response.getWriter().print(resultData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void returnOneBookInfo(Books oneBookInfo) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		JSONObject oneBookData  = new JSONObject();
		
		JSONArray jsonArray = new JSONArray();
		if(oneBookInfo != null) {
				oneBookData.put("stats", "SUCCESS");
				oneBookData.put("bookId", oneBookInfo.getId());
				oneBookData.put("name", oneBookInfo.getName());
				oneBookData.put("author", oneBookInfo.getAuthor().getName());
				oneBookData.put("publish", oneBookInfo.getPublish().getName());
				oneBookData.put("bookVersion", oneBookInfo.getBookVersion());
				oneBookData.put("createTime", oneBookInfo.getCreateTime());
				oneBookData.put("lastEditTime", oneBookInfo.getLastEditTime());
				oneBookData.put("newBookFlag", oneBookInfo.getNewBookFlag());
				oneBookData.put("skillBoostFlag", oneBookInfo.getSkillBoostFlag());
				oneBookData.put("beginBookFlag", oneBookInfo.getBeginBookFlag());
				oneBookData.put("bookDesc", oneBookInfo.getBookDesc());
				
				for(BooksImg bmg : oneBookInfo.getBooksImg()) {
					jsonArray.add(bmg.getImgAddr());
				}
				oneBookData.put("imageAddr", jsonArray);
				
				try {
					response.getWriter().print(oneBookData);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				//书籍为空时返回
				try {
					oneBookData.put("stats", "EMPTY");
					response.getWriter().print(oneBookData);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	public static JSONObject parseBookInfo(List<Object[]> data) {
		JSONObject result = new JSONObject();
		JSONObject inner = new JSONObject();
		if(data != null) {
			for(int j = 0;j<data.size();j++) {
				for(int k = 0;k<data.get(j).length;k++) {
					Object[] objs = data.get(j);
					if(k == 0)
						inner.put("bookID", objs[k].toString());
					else if( k == 1)
						inner.put("boName", objs[k].toString());
					else if( k == 2)
						inner.put("begin", objs[k].toString());
					else if( k == 3)
						inner.put("new", objs[k].toString());
					else if( k == 4)
						inner.put("skill", objs[k].toString());
					else if( k == 5)
						inner.put("boDesc", objs[k].toString());
					else if( k == 6)
						inner.put("boImg", objs[k].toString());
					else if( k == 7)
						inner.put("content", objs[k]);
					else if( k == 8)
						inner.put("tyName", objs[k].toString());
					else if( k == 9)
						inner.put("dcName", objs[k].toString());
					else if( k == 10)
						inner.put("houseCount", objs[k].toString());
					else if( k == 11)
						inner.put("comentCount", objs[k].toString());
					else 
						inner.put("scoreCount", objs[k] + "");
				}
			}
			result.put("code", 200);
			result.put("msg", "SUCCESS");
			result.put("data", inner);
			return result;
		}
			result.put("code", 404);
			result.put("msg", "EMPTY");
			result.put("data", inner);
		return result;
	}

	public static JSONObject parseRecommenderBook(List<Object[]> result) {
		JSONObject data = new JSONObject();
		JSONArray arr = new JSONArray();
		if(result.size() != 0) {
			for(int j = 0;j<result.size();j++) {
				JSONObject inner = new JSONObject();
				for(int k = 0;k<result.get(j).length;k++) {
					Object[] objs = result.get(j);
					if(k == 0)
						inner.put("bookID", objs[k].toString());
					else if( k == 1)
						inner.put("boName", objs[k].toString());
					else if( k == 2)
						inner.put("content", objs[k].toString());
					else if( k == 3)
						inner.put("boImg", objs[k].toString());
					else if( k == 4)
						inner.put("btyName", objs[k].toString());
					else if( k == 5)
						inner.put("pbName", objs[k].toString());
					else if( k == 6)
						inner.put("auName", objs[k].toString());
					else {
						inner.put("score", objs[k].toString());
						System.out.println();
					}
				}
				arr.add(inner);
			}
			data.put("count", result.size());
			data.put("msg", "SUCCESS");
			data.put("code", 200);
			data.put("data", arr);
		}else {
			data.put("count", result.size());
			data.put("msg", "EMPTY");
			data.put("code", 200);
			data.put("data", arr);
		}
		return data;
	}

	public static JSONObject parsePinLunStar(List<Object[]> result) {
		JSONObject data = new JSONObject();
		JSONArray arr = new JSONArray();
		if(result != null) {
			for(int j = 0;j<result.size();j++) {
				JSONObject inner = new JSONObject();
				for(int k = 0;k<result.get(j).length;k++) {
					Object[] objs = result.get(j);
					if(k == 0)
						inner.put("comment", objs[k].toString());
					else if( k == 1)
						inner.put("name", objs[k].toString());
				}
				arr.add(inner);
			}
			data.put("msg", "SUCCESS");
			data.put("code", 200);
			data.put("data", arr);
		}else {
			data.put("msg", "EMPTY");
			data.put("code", 200);
			data.put("data", arr);
		}
		return data;
	}
}
