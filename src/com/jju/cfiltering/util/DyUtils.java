package com.jju.cfiltering.util;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DyUtils {
	
	public static JSONObject parseShare(List<Object[]> data ) {
		JSONObject result = new JSONObject();
		JSONArray arr = new JSONArray();
		if(data != null) {
			for(int j = 0;j<data.size();j++) {
				JSONObject inner = new JSONObject();
				for(int k = 0;k<data.get(j).length;k++) {
					Object[] objs = data.get(j);
					if(k == 0)
						inner.put("shareId", objs[k].toString());
					else if( k == 1)
						inner.put("intro", objs[k].toString());
					else if( k == 2)
						inner.put("date", objs[k].toString());
					else if( k == 3)
						inner.put("media", objs[k].toString());
					else if( k == 4)
						inner.put("comment", objs[k].toString());
					else
						inner.put("like", objs[k] + "");
				}
				arr.add(inner);
			}
			result.put("code", 200);
			result.put("msg", "SUCCESS");
			result.put("data", arr);
			return result;
		}
			result.put("code", 404);
			result.put("msg", "EMPTY");
			result.put("data", arr);
		return result;
	}
	
	
	public static JSONObject parsePersonShare(List<Object[]> data ) {
		JSONObject result = new JSONObject();
		JSONArray arr = new JSONArray();
		if(data != null) {
			for(int j = 0;j<data.size();j++) {
				JSONObject inner = new JSONObject();
				for(int k = 0;k<data.get(j).length;k++) {
					Object[] objs = data.get(j);
					if(k == 0)
						inner.put("shareId", objs[k].toString());
					else if( k == 1)
						inner.put("title", objs[k].toString());
					else if( k == 2)
						inner.put("type", objs[k].toString());
					else if( k == 3)
						inner.put("comment", objs[k].toString());
					else if( k == 4)
						inner.put("date", objs[k].toString());
					else if( k == 5)
						inner.put("media", objs[k].toString());
					else if( k == 6)
						inner.put("skimCount", objs[k]+"");
					else if( k == 7)
						inner.put("comCount", objs[k].toString());
					else 
						inner.put("thumbCount", objs[k].toString());
				
				}
				arr.add(inner);
			}
			result.put("count", data.size());
			result.put("code", 200);
			result.put("msg", "SUCCESS");
			result.put("data", arr);
			return result;
		}else {
			result.put("code", 404);
			result.put("count", 0);
			result.put("msg", "EMPTY");
			result.put("data", arr);
			return result;
		}
	}
}
