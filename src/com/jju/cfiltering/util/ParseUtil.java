package com.jju.cfiltering.util;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jju.cfiltering.entity.BookDirection;

public class ParseUtil {
	
	public static JSONObject parseSQL(List<Object[]> resData, List<Object[]> secondData) {
		JSONObject result = new JSONObject();
		if(resData != null) {
			result.put("code",200);
			result.put("msg", "REQUESTSUCCESS");
			JSONObject data = new JSONObject();
			for(int j = 0;j<resData.size();j++) {
				JSONObject item = new JSONObject();
				for(int k = 0;k<resData.get(j).length;k++) {
					Object[] objs = resData.get(j);
					if( k == 0 ) 
						item.put("bobegin", objs[k].toString());
					else if( k == 1)
						 item.put("boskill", objs[k].toString());
					else if( k == 2)
						 item.put("bonew", objs[k].toString());
					else if( k == 3)
						 item.put("boId",objs[k].toString());
					else if( k == 4)
						 item.put("boName", objs[k].toString());
					else if( k == 5)
						 item.put("boDesc", objs[k].toString());
					else if( k == 6)
						 item.put("btyName", objs[k].toString());
					else if( k == 7)
						 item.put("auName", objs[k].toString());
					else if( k == 8)
						 item.put("puName", objs[k].toString());
					else if( k == 9)
						 item.put("bmgAddr", objs[k].toString());
					else if( k == 10)
						 item.put("bdrId", objs[k].toString());
					else if( k == 11)
						 item.put("btySelectedId", objs[k].toString());
				}	
				data.put("book"+j, item);
			}
			
			JSONObject innerData = new JSONObject();
			for(int j = 0;j<secondData.size();j++) {
				JSONObject item = new JSONObject();
				for(int k = 0;k<secondData.get(j).length;k++) {
					Object[] objs = secondData.get(j);
					if( k == 0 ) 
						item.put("UnSelectTypeName", objs[k].toString());
					else if( k == 1)
						 item.put("UnSelectTypeId", objs[k].toString());
				}	
				innerData.put("UnSelectType"+j, item);
			}
			innerData.put("UnSelectTypeCount", secondData.size());
			
			data.put("bookTypeUnSelect", innerData);
			data.put("bookCount", resData.size());
			
			result.put("data", data);
		}else {
			result.put("code", 404);
			result.put("msg", "DATAEMPTY");
		}
		return result;
	}

	public static JSONObject parseHql(List<Object[]> ...bdList) {
		if( bdList.length == 1 ) {
			JSONObject item = new JSONObject();
			JSONObject data = new JSONObject();
			for(int j = 0;j<bdList[0].size();j++) {
				for(int k = 0;k<bdList[0].get(j).length;k++) {
					Object[] objs = bdList[0].get(j);
					if( k == 0)
						item.put("btyId" + j, objs[k].toString());
					else
						item.put("btyName" + j,objs[k].toString());
				}	
			}
			item.put("typeCount", bdList[0].size());
			data.put("data", item);
			data.put("code", 200);
			data.put("msg", "EMPTY");
			return data;
		}else {
			JSONObject item = parseHql(bdList[0]);
			item.remove("typeCount");
			item.remove("msg");
			
			JSONObject data = new JSONObject();
			for(int j = 0;j<bdList[1].size();j++) {
				JSONObject oneBook = new JSONObject();
				for(int k = 0;k<bdList[1].get(j).length;k++) {
					Object[] objs = bdList[1].get(j);
					if( k == 0)
						oneBook.put("boId", objs[k].toString());
					else if( k == 1)
						oneBook.put("boName",objs[k].toString());
					else if( k == 2)
						oneBook.put("boDesc",objs[k].toString());
					else if( k == 3)
						oneBook.put("auName",objs[k].toString());
					else if( k == 4)
						oneBook.put("bmgAddr",objs[k].toString());
					else if( k == 5)
						oneBook.put("puName",objs[k].toString());
					else if( k == 6)
						oneBook.put("btyName",objs[k].toString());
				}	
				data.put("book" + j, oneBook);
			}
			data.put("bookCount", bdList[1].size());
			item.getJSONObject("data").put("book", data);
			item.put("msg", "init");
			return item;
		}
	}
	public static JSONObject parsePageSearch(List<Object[]> bdList) {
			JSONObject data = new JSONObject();
			JSONObject res = new JSONObject();
			for(int j = 0;j<bdList.size();j++) {
				JSONObject oneBook = new JSONObject();
				for(int k = 0;k<bdList.get(j).length;k++) {
					Object[] objs = bdList.get(j);
					if( k == 0)
						oneBook.put("boId", objs[k].toString());
					else if( k == 1)
						oneBook.put("boName",objs[k].toString());
					else if( k == 2)
						oneBook.put("boDesc",objs[k].toString());
					else if( k == 3)
						oneBook.put("auName",objs[k].toString());
					else if( k == 4)
						oneBook.put("bmgAddr",objs[k].toString());
					else if( k == 5)
						oneBook.put("puName",objs[k].toString());
					else if( k == 6)
						oneBook.put("btyName",objs[k].toString());
				}	
				data.put("book" + j, oneBook);
			}
			data.put("bookCount", bdList.size());
			res.put("code", 200);
			res.put("msg", "change");
			res.put("data", data);
			return res;
	}

	public static JSONObject parseBoType(List<Object[]> listBooks, List<Object[]> listType) {
		JSONObject reBooks = new JSONObject();
		if(listBooks.size() > 0 && listType.size() > 0 ) {
			JSONObject reData = parseBo(listBooks);
			reData.remove("code");
			reData.remove("msg");
			JSONObject reType = parseType(listType);
			reType.remove("msg");
			reType.remove("code");
			reData.put("bType", reType);
			reBooks.put("code", 200);
			reBooks.put("msg", "SUCCESS");
			reBooks.put("data", reData);
			return reBooks;
		}else {
			JSONObject reType = parseType(listType);
			reType.remove("msg");
			reType.remove("code");
			reBooks.put("code", 200);
			reBooks.put("data", reType);
			reBooks.put("code", 404);
			reBooks.put("msg", "EMPTY");
			return reBooks;
		}
	}
	
	public static JSONObject parseBo(List<Object[]> listBooks) {
		JSONObject reData = new JSONObject();
		JSONObject book = new JSONObject();
		for(int j = 0;j<listBooks.size();j++) {
			JSONObject oneBook = new JSONObject();
			for(int k = 0;k<listBooks.get(j).length;k++) {
				Object[] objs = listBooks.get(j);
				if( k == 0)
					oneBook.put("boId", objs[k].toString());
				else if( k == 1)
					oneBook.put("boName",objs[k].toString());
				else if( k == 2)
					oneBook.put("boDesc",objs[k].toString());
				else if( k == 3)
					oneBook.put("btyName",objs[k].toString());
				else if( k == 4)
					oneBook.put("auName",objs[k].toString());
				else if( k == 5)
					oneBook.put("puName",objs[k].toString());
				else if( k == 6)
					oneBook.put("bmgAddr",objs[k].toString());
			}	
			book.put("book" + j, oneBook);
		}
		book.put("bookCount", listBooks.size());
		reData.put("book", book);
		reData.put("msg", "SECOND");
		reData.put("code", 200);
		return reData;
	}
	
	
	public static JSONObject parseType(List<Object[]> listType) {
		
		JSONObject booksType = new JSONObject();
		JSONObject oneType = new JSONObject();
		for(int j = 0;j<listType.size();j++) {
			for(int k = 0;k<listType.get(j).length;k++) {
				Object[] objs = listType.get(j);
				if( k == 0)
					oneType.put("typeId" + j, objs[k].toString());
				else
					oneType.put("typeName" + j, objs[k].toString());
			}
		}
		booksType.put("booksType", oneType);
		booksType.put("boTypeCount", listType.size());
		booksType.put("code", 200);
		booksType.put("msg", "SUCCESS");
		return booksType;
	}
	
}
