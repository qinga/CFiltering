package com.jju.cfiltering.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
	/*
	 * params:secondBooksBySearch表示后台所有书籍
	 * searchValue:表示检索书籍
	 */
	public static Map<String, String>pinyin(Map<String,String> secondBooksBySearch, String searchValue){
		if(secondBooksBySearch.size() != 0) {
			Map<String, List<String>> result = new HashMap<String, List<String>>();
			for(String key : secondBooksBySearch.keySet()){
				if(key.startsWith("bookName")) {
					List<String> list = new ArrayList<String>();
					//表示将数据库的查询书籍名称放入第一位
					list.add(secondBooksBySearch.get(key));
					//书籍名字首字母
					String[] reValue = converterToFirstSpell(secondBooksBySearch.get(key)).split(",");
					for(int i = 0; i < reValue.length; i++) {
						list.add(reValue[i]);
					}
					//拼音放入
					String[] spell = converterToSpell(secondBooksBySearch.get(key)).split(",");
					for(int i = 0; i < spell.length; i++) {
						list.add(spell[i]);
					}
					//构成map集合
					result.put(key, list);
				}
			}
			//存放包含了搜索字符的查询结果的名字
			List<String> contain = new ArrayList<String>();
			Iterator<Map.Entry<String, List<String>>> entries = result.entrySet().iterator();
			while(entries.hasNext()){
			    Map.Entry<String, List<String>> entry = entries.next();
			    for(int i = 0; i < entry.getValue().size(); i++ ) {
			    	//如果包含查询字符，就把书籍名称压入contain集合中。
			    	if(entry.getValue().get(i).contains(searchValue)) {
			    		contain.add(entry.getValue().get(0));
			    		break;
			    	}
			    }
			}
			List<String> index = new ArrayList<String>();
			for (Map.Entry<String, String> entry : secondBooksBySearch.entrySet()) {
				int k = 0;
				if(entry.getKey().startsWith("bookName")) {
					for(  k = 0; k < contain.size(); k++) {
						if(entry.getValue() == contain.get(k) ) {
							break;
						}
					}
				}
				//如果for循环走完了,还没有匹配，则视为数据库中这条书籍不在搜索范围内。
				if(  k == contain.size() ) {
					StringBuilder sb = new StringBuilder();
					for(int l = 0 ; l < entry.getKey().length() ; l++) {
						String endChar = entry.getKey().substring(entry.getKey().length()-1-l, entry.getKey().length()-l);
						if( !Character.isLowerCase(endChar.toCharArray()[0])) {
							if( Integer.parseInt(endChar) >= 0 && Integer.parseInt(endChar) < 10) {
								sb.insert(0, endChar);
							}
						}else
							break;
					}
					index.add(sb.toString());
				}
			}
			 Iterator<Map.Entry<String, String>> it = secondBooksBySearch.entrySet().iterator();  
		        while(it.hasNext()){  
		            Map.Entry<String, String> entry=it.next(); 
		            String key=entry.getKey(); 
		            Matcher matcher = Pattern.compile("\\d+").matcher(key);
		            while(matcher.find()){
		            	if( index.contains(matcher.group())) {
		            		it.remove();
		            	}
		            }
		        }
			return secondBooksBySearch;
		}
		// return null;
		return secondBooksBySearch;
	}
	/**
     * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字，生成方式如（长沙市长:cssc,zssz,zssc,cssz）
     * 
     * @param chines
     *            汉字
     * @return 拼音
     */
	public static String converterToFirstSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            // 取首字母
                            pinyinName.append(strs[j].charAt(0));
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }
	/**
     * 去除多音字重复数据
     * 
     * @param theStr
     * @return
     */
    private static List<Map<String, Integer>> discountTheChinese(String theStr) {
        // 去除重复拼音后的拼音列表
        List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();
        // 用于处理每个字的多音字，去掉重复
        Map<String, Integer> onlyOne = null;
        String[] firsts = theStr.split(" ");
        // 读出每个汉字的拼音
        for (String str : firsts) {
            onlyOne = new Hashtable<String, Integer>();
            String[] china = str.split(",");
            // 多音字处理
            for (String s : china) {
                Integer count = onlyOne.get(s);
                if (count == null) {
                    onlyOne.put(s, new Integer(1));
                } else {
                    onlyOne.remove(s);
                    count++;
                    onlyOne.put(s, count);
                }
            }
            mapList.add(onlyOne);
        }
        return mapList;
    }
    
    /**
     * 解析并组合拼音，对象合并方案(推荐使用)
     * 
     * @return
     */
    private static String parseTheChineseByObject(List<Map<String, Integer>> list) {
        Map<String, Integer> first = null; // 用于统计每一次,集合组合数据
        // 遍历每一组集合
        for (int i = 0; i < list.size(); i++) {
            // 每一组集合与上一次组合的Map
            Map<String, Integer> temp = new Hashtable<String, Integer>();
            // 第一次循环，first为空
            if (first != null) {
                // 取出上次组合与此次集合的字符，并保存
                for (String s : first.keySet()) {
                    for (String s1 : list.get(i).keySet()) {
                        String str = s + s1;
                        temp.put(str, 1);
                    }
                }
                // 清理上一次组合数据
                if (temp != null && temp.size() > 0) {
                    first.clear();
                }
            } else {
                for (String s : list.get(i).keySet()) {
                    String str = s;
                    temp.put(str, 1);
                }
            }
            // 保存组合数据以便下次循环使用
            if (temp != null && temp.size() > 0) {
                first = temp;
            }
        }
        String returnStr = "";
        if (first != null) {
            // 遍历取出组合字符串
            for (String str : first.keySet()) {
                returnStr += (str + ",");
            }
        }
        if (returnStr.length() > 0) {
            returnStr = returnStr.substring(0, returnStr.length() - 1);
        }
        return returnStr;
    }
    /**
     * 汉字转换为汉语全拼，英文字符不变，特殊字符丢失 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen ,chongdangshen,zhongdangshen,chongdangcan）
     * 
     * @param chines
     *            汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            pinyinName.append(strs[j]);
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }
}
