package com.jju.cfiltering.util;

import java.util.regex.Pattern;

public class EnCode {

	public static final int ENGLISH = 2;
	public static final int CHINESE = 1;
	public static final int FIX_EN_ZH= 0;
	
	public static int isCnorEn(String searchValue){
    	char[] c = searchValue.toCharArray();
    	int[] count = {0,0,0};
    	
    	for(char v : c ) {
			if(v >= 0x0391 && v <= 0xFFE5) {
				//中文字符
				count[0]++;
			}
    		if(v >= 0x0000 && v <= 0x00FF){ //英文字符
    			count[1]++;
    		}
    		if(count[0] > 0 && count[1] > 0 ) {
    			return FIX_EN_ZH;
    		}
    	}
    	return count[0] > 0 ? CHINESE : ENGLISH;
    }
	
	 public static boolean isInteger(String str) {  
	        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
	        return pattern.matcher(str).matches();  
	  }
}
