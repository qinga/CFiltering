package com.jju.cfiltering.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HouseBookUtil {

	public static JSONObject parseHouseBook(List<Object[]> result, ArrayList<String> hostTime,Object size) {
		JSONArray arr = new JSONArray();	
		JSONObject re = new JSONObject();
		for(int j = 0;j < result.size();j++) {
				JSONObject inner = new JSONObject();
				for(int k = 0;k<result.get(j).length;k++) {
					Object[] objs = result.get(j);
					if(k == 0)
						inner.put("bookID", objs[k].toString());
					else if( k == 1)
						inner.put("boName", objs[k].toString());
					else if( k == 2)
						inner.put("pbName", objs[k].toString());
					else if( k == 3)
						inner.put("auName", objs[k].toString());
					else if( k == 4)
						inner.put("boDesc", objs[k].toString());
					else if( k == 5)
						inner.put("img", objs[k].toString());
				}
				inner.put("hostTime", hostTime.get(j));
				arr.add(inner);
			}
		re.put("data", arr);
		re.put("code", 200);
		re.put("msg", "SUCCESS");
		if(size != null) {
			re.put("count", size);
		}else {
			re.put("count", result.size());
		}
		return re;
	}

}
