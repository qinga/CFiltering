package com.jju.cfiltering.util;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class LePathUtil {

	public static JSONObject parseLearn(List<Object[]> learnPath) {
		JSONObject result = new JSONObject();
		JSONObject innerData = new JSONObject();
		if(learnPath.size() > 0) {
			for(int j = 0;j<learnPath.size();j++) {
				for(int k = 0;k<learnPath.get(j).length;k++) {
					Object[] objs = learnPath.get(j);
					if(k == 0)
						innerData.put("bookBeginId" + j, objs[k].toString());
					else if( k == 1)
						innerData.put("bookBeginName" + j, objs[k].toString());
					else if( k == 2)
						innerData.put("bookBeginAddr" + j, objs[k].toString());
					else
						innerData.put("bookBeginMark" + j, objs[k].toString());
				}	
			}
			innerData.put("bookCount", learnPath.size());
			result.put("code", 200);
			result.put("msg", "SUCCESS");
			result.put("data", innerData);
			return result;
		}
			result.put("code", 404);
			result.put("msg", "EMPTY");
			result.put("data", innerData);
		return result;
	}

	public static JSONObject parseDirect(List<Object[]> dataResult) {
		JSONObject result = new JSONObject();
		JSONObject innerData = new JSONObject();
		if(dataResult != null) {
			for(int j = 0;j<dataResult.size();j++) {
				for(int k = 0;k<dataResult.get(j).length;k++) {
					Object[] objs = dataResult.get(j);
					if(k == 0)
						innerData.put("directID", objs[k].toString());
					else if( k == 1)
						innerData.put("name", objs[k].toString());
					else if( k == 2)
						innerData.put("detail", objs[k].toString());
					else if( k == 3)
						innerData.put("video", objs[k].toString());
					else 
						innerData.put("mark", objs[k].toString());
				}	
			}
			result.put("code", 200);
			result.put("msg", "SUCCESS");
			result.put("data", innerData);
			return result;
		}else {
			result.put("code", 404);
			result.put("msg", "EMPTY");
			result.put("data", innerData);
			return result;
		}
	}
}
