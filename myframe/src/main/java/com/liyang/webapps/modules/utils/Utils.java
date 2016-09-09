package com.liyang.webapps.modules.utils;


import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;






public class Utils {
	private final static Logger log = Logger.getLogger(Utils.class);
	
	public Utils(){
		
	}
	
	/**
	 * 判断两个字符串是否相同
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equ(String str1,String str2) {
		if (str1==null || str2==null) return false;
		if (str1.equals(str2)) return true;
		else return false;
	}
	
	/**
	 * 判断两个字符串去前后空格后是否相同
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean trimEqu(String str1,String str2) {
		if (str1==null || str2==null) return false;
		if (str1.trim().equals(str2.trim())) return true;
		else return false;
	}
	
	/**
	 * 取得一个map值
	 * @param map
	 * @param name
	 * @return
	 */
	public static Object getMapValue(HashMap map,String name){	
		if (map==null) return null;
		return map.get(name);
	}
	
	public static String getDbDriver(String dbtype){
		if (equ(dbtype,"oracle")){
			return "oracle.jdbc.driver.OracleDriver";
		}else if (equ(dbtype,"sqlserver_jtds")){
			return "net.sourceforge.jtds.jdbc.Driver";
		}else{
			return null;
		}
	}
	
	/*
	 * 打开文件并转化为byte[]
	 */	
	public static byte[] readFile(String path) throws Exception{
		byte[]   arr   =   null;
		InputStream   is   =  null;
		try{
			is   =   new   FileInputStream(path);   
	        int   available   =   is.available();                                                 
	        arr   =   new   byte[available];   
	        is.read(arr);   
		}catch (Exception e) {
	    	  log.error(e,e);
	    	  throw new Exception(e.getMessage());
	    }finally{
	    	if (is!=null) is.close();
	    }
	    return arr;
	}
	
	//按编码读取文件，解决乱码问题。一般读 UTF-8
	public static String readFileToString(String path,String code) throws Exception {
		InputStream is = null;
		InputStreamReader isr=null;
		BufferedReader br = null;
		StringBuffer sb = null;
		try{
			is = new FileInputStream(path);
			isr = new InputStreamReader(is,code);   
			br = new BufferedReader(isr);
			String record = "";
			sb = new StringBuffer();
			int recCount = 0;
			
			while ((record = br.readLine()) != null) {
				recCount++;
				sb.append(record+"\n");
			}
			isr.close();
			br.close();
		}catch (Exception e) {
			log.error(e,e);
			throw new Exception(e.getMessage());
		}finally{
			if (isr!=null) isr.close();
			if (br!=null) br.close();
			if (is!=null) is.close();
		}
		return sb.toString();
	}


	
	
	
	/*
	 * 把byte[]保存成文件
	 */
	public static int saveFile(byte[] reb,String filePath) throws Exception {	
		int i_re=0;
		
		ByteArrayInputStream stream = null;
		OutputStream bos = null;
	    try {
	    	
	      stream = new ByteArrayInputStream(reb);	
	      bos = new FileOutputStream(filePath);//建立一个上传文件的输出流
	
	      int bytesRead = 0;
	      byte[] buffer = new byte[8192];
	      while ( (bytesRead = stream.read(buffer, 0, 8192)) != -1) {
	        bos.write(buffer, 0, bytesRead);//将文件写入服务器
	      }	      
	    }catch(Exception e){
	    	i_re=-1;
	    	log.error(e.getMessage());
	    	throw new Exception(e.getMessage());
	    }finally{
	    	bos.close();
		    stream.close();
	    }

		return i_re;
	}

	
	/**
	 * 根据正值表达式，找目标字符串
	 * @param o_str
	 * @param rule
	 * @return
	 * 
	 * 陈海生
	 */
	public static String findArea(String o_str,String rule){
		String re_str = "";
		//System.out.println("-------------------------");
		
		Pattern p = Pattern.compile(rule);//不管大小写     
		Matcher m = p.matcher(o_str);  
		//StringBuffer sb = new StringBuffer();        
		//int i=0;           
		boolean result = m.find();   
		if(result) {  
			for(int i=1;i<=m.groupCount();i++){
				//m.appendReplacement(sb, " in (\\$"+m.group(i)+")");    
				re_str = m.group(i);
				//System.out.println("第"+i+"次匹配后re_str的内容是："+re_str);   
			}
			//result = m.find(); 
		}

		//re_str = m.group(i).toString();
		//System.out.println(re_str);    
		return re_str;
	}
	
	public static String cleanUpRule(String rule){
		rule = rule.replace(".", "\\.");
		rule = rule.replace("{", "\\{");
		rule = rule.replace("}", "\\}");
		rule = rule.replace("[", "\\[");
		rule = rule.replace("]", "\\]");
		rule = rule.replace("(", "\\(");
		rule = rule.replace(")", "\\)");
		rule = rule.replace("|", "\\|");
		rule = rule.replace("?", "\\?");
		rule = rule.replace("+", "\\+");
		rule = rule.replace("-", "\\-");
		rule = rule.replace("*", "\\*");
		rule = rule.replace(",", "\\,");
		rule = rule.replace("^", "\\^");
		rule = rule.replace(" ", "\\s");
		rule = rule.replace("	", "\\s");
		return rule;
	}
	
	/**
	 * 取指定字符串之间的字符串(不包括始止字符串)
	 * @param o_str
	 * @param b_str
	 * @param e_str
	 * @return  
	 *           -1000 源字符串为空
	 *           -1001 开始字符串为空
	 *           -1002 结束字符串为空 
	 *           -2000 开始字符串找不到
	 *           -3000 结束字符串找不到
	 */
	public static String findArea(String o_str,String b_str,String e_str){
		int b;
		int e;
		String tmp_str = o_str;
		if (o_str==null || o_str.trim().equals("")) 
			return "-1000";
		else if(b_str==null || b_str.equals(""))
			return "-1001";
		else if(e_str==null || e_str.equals(""))
			return "-1002";
		
		b = tmp_str.indexOf(b_str);
		if (b<0) return "";;
		tmp_str = o_str.substring(b+b_str.length(),o_str.length());
		
		e = tmp_str.indexOf(e_str);
		if (e<0) return "";
		//System.out.println(""+b+","+e);

		return tmp_str.substring(0,e);
	}
	
	/**
	 * 取指定字符串之间的字符串(包括始止字符串)
	 * @param o_str
	 * @param b_str
	 * @param e_str
	 * @return  
	 *           -1000 源字符串为空
	 *           -1001 开始字符串为空
	 *           -1002 结束字符串为空 
	 *           -2000 开始字符串找不到
	 *           -3000 结束字符串找不到
	 */
	public static String findArea1(String o_str,String b_str,String e_str){
		int b;
		int e;
		String tmp_str = o_str;
		if (o_str==null || o_str.trim().equals("")) 
			return "-1000";
		else if(b_str==null || b_str.equals(""))
			return "-1001";
		else if(e_str==null || e_str.equals(""))
			return "-1002";
		
		b = tmp_str.indexOf(b_str);
		if (b<0) return "-2000";;
		tmp_str = o_str.substring(b+b_str.length(),o_str.length());
		
		e = tmp_str.indexOf(e_str);
		if (e<0) return "-3000";
		else e+=e_str.length()+b+b_str.length();
		//System.out.println(""+b+","+e);

		return o_str.substring(b,e);
	}
	
	/**
	 * 去掉HTML代码
	 * 
	 * 陈海生
	 */
	public static String cleanHtml(String o_str){
		String re_str = "";
		
		re_str = Utils.findArea(o_str,"(>\\s*[^<][^*]*[^>]\\s*<)");   //先去两边都有的情况
		if (re_str.equals("")){
			int l = o_str.indexOf(">"); 
			int r = o_str.indexOf("<");
			if(l>0){
				re_str = o_str.substring(l,o_str.length());
			}else if(o_str.indexOf("<")>0){
				re_str = o_str.substring(0,r);
			}else{
				re_str = o_str;
			}
		}
		re_str = re_str.replace(">","");
		re_str = re_str.replace("<","");
		
		re_str = re_str.replace("&nbsp;"," ");
		
		re_str = trimBlank(re_str);

		return re_str;
	}
	
	public static String trimBlank(String str){
		if (str==null) str="";
		else if (str.trim().equals("null")) str="";
		else str = str.trim();
		return str;
	}
	
	public static boolean bBlank(String str){
		if (str==null || str.equals("")) return true;
		return false;
	}
	
	public static String replaceHTML(String str){
		str = Utils.trimBlank(str);
		str = str.replace("&lt;","<");
		str = str.replace("&gt;",">");
		return str;
	}
	
	/**
	 * 文件是否已存在
	 * 
	 * 陈海生
	 */
	public static boolean isFile(String path){ 
	    try{
	      File f=new File( path );
	      if (f.exists()){
	        return true;
	      }
	    }
	    catch (Exception e)
	    {
	    	System.out.print(e.getMessage());
	    	return false;
	    }
	    return false;
	  }
	
	/**
	 * 删除已存在文件
	 * 
	 * 陈海生
	 */
	public static void deleteFile(String path){
	    try{
	      File f=new File( path );
	      if (f != null){
	        f.delete();
	      }
	    }
	    catch (Exception e)
	    {
	    	System.out.print(e.getMessage());
	    }
	  }

	/*
	 * 从url中取得页面的绝对地址
	 * 
	 * 陈海生
	 */
	public static String getPageStr(String url) throws Exception{
		int b;
		int e;
		String tmp_str = url;

		if (url==null || url.trim().equals("")) 
			throw new Exception("分析url时出错[url为空]!");
		tmp_str=tmp_str.replaceAll("//","");

		b = tmp_str.indexOf("/");
		if (b<0) throw new Exception("分析url时出错[找不到'/']!");
		
		//System.out.println(""+b+","+e);

		return tmp_str.substring(b,tmp_str.length());
	}
	
	/*
	 * 取得系统当前时间，然后转化为数据库时间格式
	 */
	public static java.sql.Timestamp getDBNow(){
		return new java.sql.Timestamp(System.currentTimeMillis());
	}
	
	public static Date getDate(String dateFormat, String invalue){
		  Date current=null;
		 try {
			 if(dateFormat !=null && !dateFormat.equals("")){
				    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
				    current = formatter.parse(invalue);

				  }
		 } catch (Exception e) {
			 log.error(e,e);
		 }
		  return current;
		}
	
	public static Date getDate(String dateFormat) {
		String _ddatetime = getFormatDate(dateFormat);
		Date ddatetime = getDate(dateFormat, _ddatetime);
		return ddatetime;
	}
	
	public static Date getDate(String dateFormat, Date invalue) {
		String _ddatetime = Utils.dateToStr(dateFormat, invalue);
		return getDate(dateFormat, _ddatetime);
	}
	
	/*根据输入指定日期格式返回当前日期
	 * 参数如："yyyy-MM" 或者 "yyyy-MM-dd' 'kk:mm:ss"等等
	 * yyyyMMddhhmmss
	 * 
	 * 
	 * 陈海生
	 */
	public static String getFormatDate(String dateFormat){
	  String current="";
	  if(dateFormat !=null && !dateFormat.equals("")){
	    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
	    Date currentTime_1 = new Date();
	    current = formatter.format(currentTime_1);

	  }
	  return current;
	}
	
	/*
	 * 把字符串按给定的长度分为几段
	 * 
	 * 陈海生
	 */
	public static List splitString(String str,int sublen) throws Exception{
		int len=0;
		int i_count=0;
		String context="";
		List list=new ArrayList();
		
		try{
			len=str.length();
			if (len>=sublen)
				if (len%sublen>0) i_count=(len/sublen)+1; //最后一条超过长度的情况
				else i_count=len/sublen; //最后一条等于长度的情况
			else i_count=1; //字符串小于长度的情况
	
			for(int j=1;j<=i_count;j++){ 
				if (j<i_count){
					context=str.substring(0,sublen);
					str=str.substring(sublen,str.length());
				}else{
					context=str;
				}
				list.add(context);
				log.debug(context);
			}
		}catch (Exception e){
			log.error(e,e);
			throw new Exception("分解字符串时，出错异常！");
		}
		return list;
	}
	
	public static String getMulBox(String[] strs){
		if (strs==null) return "";
		String s_re="";
		for(int i=0;i<strs.length;i++){
			if (i==0) s_re=strs[i];
			else s_re+=","+strs[i];
		}
		return s_re;
	}
	
	
	/**
	 * 按样式取得时间
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * 注意，小时24制的一定得是 HH 不能是 hh
	 */
	public static String getTimeFormat(String inValue,String inFormat){
		String re_s="";
		
		SimpleDateFormat inDateFormat =  null;
		Date din=null;
		try{   
			inDateFormat =  new SimpleDateFormat(inFormat);
			din = inDateFormat.parse(inValue);  
			re_s = inDateFormat.format(din.getTime()); 
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！inFormat["+inFormat+"],inValue["+inValue+"]");
			 log.error(e,e);
			 return "";
		 }  
		 		
		return re_s;
	}
	
	/**
	 * 按样式取得时间
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * 注意，小时24制的一定得是 HH 不能是 hh
	 */
	public static String getTimeFormat(String inValue,String inFormat,String outFormat){
		String re_s="";
		
		SimpleDateFormat inDateFormat =  null;
		SimpleDateFormat outDateFormat =  null;
		Date din=null;
		try{   
			inDateFormat =  new SimpleDateFormat(inFormat);
			din = inDateFormat.parse(inValue);  
			outDateFormat =  new SimpleDateFormat(outFormat);
			re_s = outDateFormat.format(din.getTime()); 
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！inValue["+inValue+"],inFormat["+inFormat+"],outFormat["+outFormat+"]");
			 log.error(e,e);
			 return "";
		 }  
		 		
		return re_s;
	}
	
	/**
	 * 按样式取得时间
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * 注意，小时24制的一定得是 HH 不能是 hh
	 */
	public static String getTimeFormat(Date inValue,String inFormat){
		String re_s="";
		
		SimpleDateFormat inDateFormat =  null;
		try{   
			inDateFormat =  new SimpleDateFormat(inFormat);
			re_s = inDateFormat.format(inValue.getTime()); 
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！inFormat["+inFormat+"],inValue["+inValue+"]");
			 log.error(e,e);
			 return "";
		 }  
		 		
		return re_s;
	}
	
	
	public static String getWeekStr(String inValue,String inFormat){
		String re_s=null;
		
		try{   
			SimpleDateFormat inDateFormat =  new SimpleDateFormat(inFormat);
			Date din = inDateFormat.parse(inValue); 
			
			int i = Utils.getWeek(din);
			
			if (i==0) re_s="日";
			else if (i==1) re_s="一";
			else if (i==2) re_s="二";
			else if (i==3) re_s="三";
			else if (i==4) re_s="四";
			else if (i==5) re_s="五";
			else if (i==6) re_s="六";
		 }catch(Exception e){
			 log.error("输入日期格式转化成星期出错！inValue["+inValue+"],inFormat["+inFormat+"]");
			 return "";
		 }  
		
		return re_s;
	}
	
	/**
	 * 从日期中取得星期几，0为星期日，1-6 为星期一至六
	 * @param dat
	 * @return
	 */
	public static int getWeek(Date dat){
		int re_i=-1;
		try{ 
			if (dat==null) log.error("输入日期为空！");
			
			java.util.Calendar cal = java.util.Calendar.getInstance(); 
	        cal.setTime(dat);           
	        re_i=cal.get(java.util.Calendar.DAY_OF_WEEK)-1; 

		}catch(Exception e){
			 log.error("从日期中取得星期出错！"+e.getMessage());
			 re_i = -1;
		 }  
		 		
		return re_i;
	}
	
	/**
	 * 时间相减
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * @param inValue1 时间值1
	 * @param type 相减后相差类型 d-天，h-小时，mi-分钟，s-秒，ss-毫秒
	 * @return long 相等=0，第一个大于第二个>0，第一个小于第二个<0 
	 * 注意，小时24制的一定得是 HH 不能是 hh
	 */
	public static long getTimeDec(String inFormat,String inValue,String inValue1,String type){
		long re_i=0;
		
		SimpleDateFormat inDateFormat =  null;
		Date din=null;
		Date din1=null;
		try{   
			inDateFormat =  new SimpleDateFormat(inFormat);
			din = inDateFormat.parse(inValue);  
			inDateFormat =  new SimpleDateFormat(inFormat);
			din1 = inDateFormat.parse(inValue1); 
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！inFormat["+inFormat+"],inValue["+inValue+"],inValue1["+inValue1+"]");
			 return -100000;
		 }  
		 
		 re_i = din.getTime()-din1.getTime();
		 
		 if (type.equals("d")){
			 re_i = re_i/24/60/60/1000;   
		 }else if (type.equals("h")){
			 re_i = re_i/60/60/1000;   
		 }else if (type.equals("mi")){
			 re_i = re_i/60/1000;   
		 }else if (type.equals("s")){
			 re_i = re_i/1000;    
		 }
		 		
		return re_i;
	}
	
	/**
	 * 时间相减
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * @param inValue1 时间值1
	 * @param type 相减后相差类型 d-天，h-小时，mi-分钟，s-秒，ss-毫秒
	 * @return long 相等=0，第一个大于第二个>0，第一个小于第二个<0 
	 * 注意，小时24制的一定得是 HH 不能是 hh
	 */
	public static long getTimeDec(Date din,Date din1,String type){
		long re_i=0;
		
		 
		 re_i = din.getTime()-din1.getTime();
		 
		 if (type.equals("d")){
			 re_i = re_i/24/60/60/1000;   
		 }else if (type.equals("h")){
			 re_i = re_i/60/60/1000;   
		 }else if (type.equals("mi")){
			 re_i = re_i/60/1000;   
		 }else if (type.equals("s")){
			 re_i = re_i/1000;    
		 }
		 		
		return re_i;
	}
	
	/**
	 * 日期型转为字符型
	 * @author 陈海生
	 */
	public static String dateToStr(String format,Date sdate){
		String re_s="";
		
		SimpleDateFormat inDateFormat =  null;
		try{   
			inDateFormat =  new SimpleDateFormat(format);
			re_s = inDateFormat.format(sdate.getTime()); 
			
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！format["+format+"],inValue["+sdate+"]");
			 return "";
		 }  
		 		
		return re_s;
	}
	
	/**
	 * 字符型转为日期型
	 * @author 陈海生
	 */
	public static Date strToDate(String format,String sdate){
		
		SimpleDateFormat inDateFormat =  null;
		Date din=null;
		try{   
			inDateFormat =  new SimpleDateFormat(format);
			din = inDateFormat.parse(sdate);  
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！format["+format+"],sdate["+sdate+"]]");
		 }  
		 		
		return din;
	}
	
	/**
	 * 时间比较大小
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * @param inFormat1 输出时间格式样式1，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue1 时间值1
	 * @return int 相等=0，第一个大于第二个>0，第一个小于第二个<0 
	 * 注意，小时24制的一定得是 HH 不能是 hh
	 */
	public static int getCompTime(String inFormat,String inValue,String inValue1){
		int re_i=0;
		
		SimpleDateFormat inDateFormat =  null;
		Date din=null;
		Date din1=null;
		try{   
			inDateFormat =  new SimpleDateFormat(inFormat);
			din = inDateFormat.parse(inValue);  
			inDateFormat =  new SimpleDateFormat(inFormat);
			din1 = inDateFormat.parse(inValue1); 
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！inFormat["+inFormat+"],inValue["+inValue+"],inValue1["+inValue1+"]");
		 }  
		 
		 if (din.getTime()>din1.getTime()) re_i=1;
		 else if (din.getTime()==din1.getTime()) re_i=0;
		 else re_i=-1;
		 		
		return re_i;
	}
	
	/**
	 * 时间比较大小
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * @param inFormat1 输出时间格式样式1，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue1 时间值1
	 * @return int 相等=0，第一个大于第二个>0，第一个小于第二个<0 
	 * * 注意，小时24制的一定得是 HH 不能是 hh
	 */
	public static int getCompTime(String inFormat,String inValue,String inFormat1,String inValue1){
		int re_i=0;
		
		SimpleDateFormat inDateFormat =  null;
		SimpleDateFormat inDateFormat1 =  null;
		Date din=null;
		Date din1=null;
		try{   
			inDateFormat =  new SimpleDateFormat(inFormat);
			din = inDateFormat.parse(inValue);   
		 }catch(Exception e){
			 log.error("第一个输入日期格式转化出错！inFormat["+inFormat+"],indate["+inValue+"]");
		 }  
		 
		 try{   
			inDateFormat1 =  new SimpleDateFormat(inFormat1);
			din1 = inDateFormat1.parse(inValue1);     
		 }catch(Exception e){
			 log.error("第二个输入日期格式转化出错！inFormat1["+inFormat1+"],indate1["+inValue1+"]");
		 }
		 
		 if (din.getTime()>din1.getTime()) re_i=1;
		 else if (din.getTime()==din1.getTime()) re_i=0;
		 else re_i=-1;
		 		
		return re_i;
	}
	
	/**
	 * 时间加N天
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param outFormat 输出时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * @param type 加减类型 y-年，m-月，d-日，h-时，mi-分，s-秒
	 * @param addi 加减数字 可以正，也可以负
	 * @return String
	 */
	public static String getDataAdd(String inFormat,String inValue,String type,int addi){
		String re_date = "";
		
		SimpleDateFormat inDateFormat =  null;
		Date din=null;
		try{   
			inDateFormat =  new SimpleDateFormat(inFormat);
			din = inDateFormat.parse(inValue);     
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！inFormat["+inFormat+"],indate["+inValue+"]");
			 log.error(e,e);
		 }  
 
		 try{
			 Calendar c = Calendar.getInstance();
			 c.setTime(din);
			 
			 if (type.equals("y")){
				 c.add(Calendar.YEAR,addi);    
			 }else if (type.equals("m")){
				 c.add(Calendar.MONTH,addi);   
			 }else if (type.equals("d")){
				 c.add(Calendar.DATE,addi);  
			 }else if (type.equals("h")){
				 c.add(Calendar.HOUR,addi);    
			 }else if (type.equals("mi")){
				 c.add(Calendar.MINUTE,addi);   
			 }else if (type.equals("s")){
				 c.add(Calendar.SECOND,addi);     
			 }
			 re_date = inDateFormat.format(c.getTime()); 
		 }catch(Exception e){
			 log.error("生成日期出错！inFormat["+inFormat+"],indate["+inValue+"]");
			 log.error(e,e);
		 } 

		return re_date;
	}
	
	/**
	 * 时间加N天
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param outFormat 输出时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * @param type 加减类型 y-年，m-月，d-日，h-时，mi-分，s-秒
	 * @param addi 加减数字 可以正，也可以负
	 * @return String
	 */
	public static Date getDataAddD(String inFormat,String inValue,String type,int addi){
		Date re_date = null;
		
		SimpleDateFormat inDateFormat =  null;
		Date din=null;
		try{   
			inDateFormat =  new SimpleDateFormat(inFormat);
			din = inDateFormat.parse(inValue);     
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！inFormat["+inFormat+"],indate["+inValue+"]");
			 log.error(e,e);
		 }  
 
		 try{
			 Calendar c = Calendar.getInstance();
			 c.setTime(din);
			 
			 if (type.equals("y")){
				 c.add(Calendar.YEAR,addi);    
			 }else if (type.equals("m")){
				 c.add(Calendar.MONTH,addi);   
			 }else if (type.equals("d")){
				 c.add(Calendar.DATE,addi);  
			 }else if (type.equals("h")){
				 c.add(Calendar.HOUR,addi);    
			 }else if (type.equals("mi")){
				 c.add(Calendar.MINUTE,addi);   
			 }else if (type.equals("s")){
				 c.add(Calendar.SECOND,addi);     
			 }
			 re_date = c.getTime(); 
			 //System.out.println("-------["+inValue+"]------:"+inDateFormat.format(c.getTime())); 
		 }catch(Exception e){
			 log.error("生成日期出错！inFormat["+inFormat+"],indate["+inValue+"]");
		 } 

		return re_date;
	}
	
	/**
	 * 时间加N天
	 * @author 陈海生
	 * @param inFormat 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param outFormat 输出时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param inValue 时间值
	 * @param type 加减类型 y-年，m-月，d-日，h-时，mi-分，s-秒
	 * @param addi 加减数字 可以正，也可以负
	 * @return String
	 */
	public static String getDataAdd(String inFormat,String outFormat,String inValue,String type,int addi){
		String re_date = "";
		
		SimpleDateFormat inDateFormat =  null;
		Date din=null;
		try{   
			inDateFormat =  new SimpleDateFormat(inFormat);
			din = inDateFormat.parse(inValue);     
		 }catch(Exception e){
			 log.error("输入日期格式转化出错！inFormat["+inFormat+"],indate["+inValue+"]");
			 log.error(e,e);
		 }  
		
		 SimpleDateFormat outDateFormat =  null;
		 try{   
			 outDateFormat =  new SimpleDateFormat(outFormat);
			 Calendar c = Calendar.getInstance();
			 c.setTime(din);
			 
			 if (type.equals("y")){
				 c.add(Calendar.YEAR,addi);    
			 }else if (type.equals("m")){
				 c.add(Calendar.MONTH,addi);   
			 }else if (type.equals("d")){
				 c.add(Calendar.DATE,addi);  
			 }else if (type.equals("h")){
				 c.add(Calendar.HOUR,addi);    
			 }else if (type.equals("mi")){
				 c.add(Calendar.MINUTE,addi);   
			 }else if (type.equals("s")){
				 c.add(Calendar.SECOND,addi);     
			 }
			 re_date = outDateFormat.format(c.getTime()); 
		 }catch(Exception e){
			 log.debug("输出日期格式转化出错！outFormat["+outFormat+"],indate["+inValue+"]");
		 }
		
		return re_date;
	}
	
	/**
	 * 取得一段时间的时间列表
	 * @param format 输入时间格式样式，如："yyyy-MM" 或者 "yyyy-MM-dd HH:mm:ss"等等
	 * @param btime  开始时间
	 * @param etime  结束时间
	 * @param type   加减类型 y-年，m-月，d-日，h-时，mi-分，s-秒
	 * @param addi   间隔
	 * @return
	 */
	public static List<Date> getDataList(String format,String btime,String etime,String type,int addi){
		List<Date> list = new ArrayList();
		long l=0;
		
		 try{   
			 l = getTimeDec(format,etime,btime,type);
			 //System.out.println("-------["+btime+"]-l:"+l);
			 if (l==0){
				 list.add(strToDate(format,btime));
			 }else{
				 for(int i=0;i<=l;i=i+addi){
					 if (l%addi!=0 && i>l) break;
					 list.add(getDataAddD(format,btime,type,i));
				 }
			 }
		 }catch(Exception e){
			 log.debug("取得一段时间的时间列表出错！format["+format+"],btime["+btime+"],etime["+etime+"],type["+type+"],addi["+addi+"]");
		 }
		
		return list;
	}
	
	
	//把要传入oracle 查询语句的时间，转成固定格式
	public static String renewOracleDate(String sdate){
		if (bBlank(sdate)) return null;
		sdate=sdate.trim();
		
		if (sdate.indexOf(" ")>0){
			String ss[] = sdate.split("\\ ");
			String ins1=ss[0];
			String ins2=ss[1];
			String[] inss2=ins2.split(":");
			if (inss2.length==2){
				sdate=ins1+" "+ins2+":00"; 
			}else if (inss2.length==1){
				sdate=ins1+" "+ins2+":00:00"; 
			}
		}else{
			sdate=sdate+" 00:00:00";
		}

		return "to_date('"+sdate+"','yyyy-mm-dd hh24:mi:ss')";
	}
	
    //把查询出来的oracle 时间，转成固定格式 yyyy-mm-dd hh24:mi:ss
	public static String showOracleDate(String sdate){
		if (bBlank(sdate)) return "";
		if (sdate.length()>19)sdate = sdate.substring(0,19);

		return sdate;
	}
	
	
	//字符串中，统计字符串的个数
	public   static   int   countStrSub(String   str,   String   sub)   { 
        if   (bBlank(sub))   { 
                return   0; 
        } 
        int   count   =   0; 
        int   idx   =   0; 
        while   ((idx   =   str.indexOf(sub,   idx))   !=   -1)   { 
                count++; 
                idx   +=   sub.length(); 
        } 
        return   count; 
	} 
	
	public static String basicConver(int num) throws Exception{
		double dtmp=0.0;
		try{
			dtmp=(double)num/10;
			dtmp = round(dtmp,1);
		}catch(Exception e){
			log.error(num+"，转化为double型错误!");
		}
		
		return ""+dtmp;
	}
	
	public static String basicConver(String num) throws Exception{
		double dtmp=0.0;
		try{
			dtmp=Double.parseDouble(num);
			dtmp=dtmp/10;
			dtmp = round(dtmp,1);
		}catch(Exception e){
			log.error(num+"，转化为double型错误!");
		}
		
		return ""+dtmp;
	}
	
	public static String basicConver(double num) throws Exception{
		double dtmp=0.0;
		try{
			dtmp=dtmp/10;
			dtmp = round(dtmp,1);
		}catch(Exception e){
			log.error(num+"，转化为double型错误!");
		}
		
		return ""+dtmp;
	}
	
	public static String basicConver(String num,int ibit) throws Exception{
		double dtmp=0.0;
		try{
			dtmp=Double.parseDouble(num);
			dtmp=dtmp/10;
			dtmp = round(dtmp,ibit);
		}catch(Exception e){
			log.error(num+"，四舍五入错误!");
		}
		
		return ""+dtmp;
	}
	
	public static String round(String num,int ibit) throws Exception{ 
		String stmp=num;
		try{
			if (ibit==0){
				if (stmp.indexOf(".")>0){
					stmp=stmp.substring(0,stmp.indexOf(".")+ibit);
				}
			}else{
				if ((stmp.length()-stmp.indexOf("."))>ibit) 
					stmp=stmp.substring(0,stmp.indexOf(".")+1+ibit);
			}
		}catch(Exception e){
			log.error(num+"，截小数点后几位错误!");
		}
		
		return ""+stmp;
	} 
	
	public static double round(double v,int scale) throws Exception{ 
		String temp="#."; 
		for (int i=0;i<scale ;i++ ) 
		{ 
			temp+="0"; 
		} 
		return Double.valueOf(new java.text.DecimalFormat(temp).format(v)); 
	} 
	
	 /**
     * support Numeric format:<br>
     * "33" "+33" "033.30" "-.33" ".33" " 33." " 000.000 "
     * @param str String
     * @return boolean
     */
   public static boolean isNumeric(String str) {
       int begin = 0;
       boolean once = true;
       if (str == null || str.trim().equals("")) {
           return false;
       }
       str = str.trim();
       if (str.startsWith("+") || str.startsWith("-")) {
           if (str.length() == 1) {
               // "+" "-"
               return false;
           }
           begin = 1;
       }
       for (int i = begin; i < str.length(); i++) {
           if (!Character.isDigit(str.charAt(i))) {
               if (str.charAt(i) == '.' && once) {
                   // '.' can only once
                   once = false;
               }
               else {
                   return false;
               }
           }
       }
       if (str.length() == (begin + 1) && !once) {
           // "." "+." "-."
           return false;
       }
       return true;
   }

   /**
     * support Integer format:<br>
     * "33" "003300" "+33" " -0000 "
     * @param str String
     * @return boolean
     */
   public static boolean isInteger(String str) {
       int begin = 0;
       if (str == null || str.trim().equals("")) {
           return false;
       }
       str = str.trim();
       if (str.startsWith("+") || str.startsWith("-")) {
           if (str.length() == 1) {
               // "+" "-"
               return false;
           }
           begin = 1;
       }
       for (int i = begin; i < str.length(); i++) {
           if (!Character.isDigit(str.charAt(i))) {
               return false;
           }
       }
       return true;
   }


   /**
     * use Exception
     * support Numeric format:<br>
     * "33" "+33" "033.30" "-.33" ".33" " 33." " 000.000 "
     * @param str String
     * @return boolean
     */
   public static boolean isNumericEx(String str) {
       try {
           Double.parseDouble(str);
           return true;
       }
       catch (NumberFormatException ex) {
           return false;
       }
   }

   /**
     * use Exception
     * support less than 11 digits(<11)<br>
     * support Integer format:<br>
     * "33" "003300" "+33" " -0000 " "+ 000"
     * @param str String
     * @return boolean
     */
   public static boolean isIntegerEx(String str) {
       str = str.trim();
       try {
           Integer.parseInt(str);
           return true;
       }
       catch (NumberFormatException ex) {
           if (str.startsWith("+")) {
               return isIntegerEx(str.substring(1));
           }
           return false;
       }
   }
   
   /*
    * 不够长度左边补空格
    */
   public static String strLeftNull(String str,int i){
	   int oi=0;
	   if (i>str.getBytes().length){
			oi=i-str.getBytes().length;
			for(int j=0;j<oi;j++){ 
				str+=" ";
			}
			return str;
		}else{
			return str;
		}
   }
   
   public static String strLeftNullTrim(String str,int i){
	   int oi=0;
	   if (i>str.getBytes().length+2){
			oi=i-str.getBytes().length;
			for(int j=0;j<oi;j++){ 
				str+=" ";
			}
			return str;
		}else{
			str=substring(str,i-2,"");
			return str+"  ";
		}
   }
   
   /*
    * 不够长度右边补空格
    */
   public static String strRigthNull(String str,int i){
	   int oi=0;
	   if (i>str.getBytes().length){
			oi=i-str.getBytes().length;
			for(int j=0;j<oi;j++){ 
				str=" "+str;
			}
			return str;
		}else{
			return str;
		}
   }
   
   /** *//**
    * 按字节长度截取字符串
    * @param str 将要截取的字符串参数
    * @param toCount 截取的字节长度
    * @param more 字符串末尾补上的字符串
    * @return 返回截取后的字符串
    */
   public static String substring(String str, int toCount, String more){
     int reInt = 0;
     String reStr = "";
     if (str == null)
       return "";
     char[] tempChar = str.toCharArray();
     //System.out.println("----------["+str+"]["+str.getBytes().length+"]");
     //if (toCount < tempChar.length){
     if (toCount < str.getBytes().length){
	     for (int kk = 0; (kk < str.getBytes().length && toCount > reInt); kk++) {
	       String s1 = str.valueOf(tempChar[kk]);
	       byte[] b = s1.getBytes();
	       reInt += b.length;
	       reStr += tempChar[kk];
	     }
	     if (toCount == reInt || (toCount == reInt - 1))
	       reStr += more;
     }else return str;
     return reStr;
   }   
   
   /**
    * 如果超过多少位，则截取多少位
    * @param str
    * @param toCount
    * @return
    */
   public static String substring(String str, int toCount){
	   if (str!=null && str.length()>toCount){
			str = str.substring(0,toCount);
	   }
	   return str;
	}  
   
   /*
    * 不够位数，前面补0
    */
   public static String addZ(String snum,int i) {
	   String re_s=Utils.trimBlank(snum);
	   if (re_s.length()<i){
		   for(int j=0;j<(i-Utils.trimBlank(snum).length());j++){
			   re_s="0"+re_s;
		   }
	   }
	   return re_s;
   }
   
   /*
    * 不够位数，后面补0
    */
   public static String addZB(String snum,int i) {
	   String re_s=Utils.trimBlank(snum);
	   if (re_s.length()<i){
		   for(int j=0;j<(i-Utils.trimBlank(snum).length());j++){
			   re_s+="0";
		   }
	   }else if (re_s.length()>i){
		   re_s=re_s.substring(0,i);
	   }
	   return re_s;
   }
 
   
   /*把字符串数组放到字符串中*/
   public static String arrayToString(String[] sa,String splitStr)
   {
	   String s_re="";
	   try
	   {
		   if (sa!=null){
				for(int f=0;f<sa.length;f++){
					if (f==0) s_re=sa[f];
					else s_re+=splitStr+sa[f];
				}
		   }
	   }catch(Exception ex){
		   log.error("把字符串数组，转换为字符串时出错！");
		   ex.printStackTrace();
	   }
	   
	   return s_re;
   }
   
   public static String downVauleToString(String[] sa)
   {
	   String s_re="";
	   String str=null;
	   try
	   {
		   if (sa!=null){
				for(int f=0;f<sa.length;f++){
					str=sa[f];
					str=str.substring(0,str.indexOf("_|_"));
					if (f==0) s_re=str;
					else s_re+=","+str;
				}
		   }
	   }catch(Exception ex){
		   log.error("在取下载的CSV字段列时出错！");
		   ex.printStackTrace();
	   }
	   
	   return s_re;
   }
   
   public static String downNameToString(String[] sa)
   {
	   String s_re="";
	   String str=null;
	   try
	   {
		   if (sa!=null){
				for(int f=0;f<sa.length;f++){
					str=sa[f];
					str=str.substring(str.indexOf("_|_")+3,str.length());
					if (f==0) s_re=str;
					else s_re+=","+str;
				}
		   }
	   }catch(Exception ex){
		   log.error("在取下载的CSV字段名时出错！");
		   ex.printStackTrace();
	   }
	   
	   return s_re;
   }
   
   /*做算术运算*/
   public static String operation(String value,String operation){
	   String s_re=value;
	   if (Utils.bBlank(value)) return "";
   		if (!Utils.bBlank(operation)){
   			
   			String op1="";
   			String op2="";
   			try{
	    			op1=operation.substring(0,1);
	    			op2=operation.substring(1,operation.length());
   			}catch(Exception ex){
   				log.debug("operation 格式出错，第一个字符必须为运算符！");
   			}
   			
   			double dvaule=0;
   			double dvaule1=0;
   			try{
				if (!Utils.bBlank(value)) 
					dvaule=Double.parseDouble(Utils.trimBlank(value));
   				dvaule1=Double.parseDouble(op2);
   				
   				if (op1.equals("+")){
   					dvaule=dvaule+dvaule1;
   				}else if (op1.equals("-")){
   					dvaule=dvaule-dvaule1;
   				}else if (op1.equals("*")){
   					dvaule=dvaule * dvaule1;
   				}else if (op1.equals("/")){
   					dvaule=dvaule / dvaule1;
   				}
   				
   				//dvaule = Utils.round(dvaule,2);
   				value = dvaule+"";
   				if (value.indexOf(".")>0){
   					String value1=value.substring(0,value.indexOf("."));
   					String value2=value.substring(value.indexOf(".")+1,value.length());
   					if (value2.length()>2) value2=value2.substring(0,2);
   					if (value2.equals("0")) value = value1;
   					else value = value1+"."+value2;
   				}
   			}catch(Exception ex){
   				//log.debug("运算出错！！！");
   			}
   			
   		}
   		s_re=value;
   
       return s_re;
   }

   /*取出文件最后多少行*/
   public static String fileEndNLine(String sfile,int n) throws Exception {
		String result = "";
		
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(sfile, "r");

			int b, count = 0;
			long head = 0, tail = raf.length(); //   
			raf.seek(raf.length() - 1);
			for (int i = 1; i < raf.length() && (b = raf.read()) != -1; i++, raf
					.seek(raf.length() - i)) { // 从文件尾向前搜索
				if (b == '\n') {
					count++; // 统计回车符
					if (count == n) { // 达到N个（即N行）时 
						head =raf.getFilePointer();
								// 设置从head的位置开始读取（若需读取的行数大于文件实际行数则head仍为0）
						break; // 且终止循环
					}
				}
			}

			raf.seek(head);
			byte[] buffer = new byte[(int) (tail - head)];
			raf.read(buffer);
			
			result = new String(buffer);   
			
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{			
			if (raf!=null){
				raf.close();
				raf=null;
			}
		}
			
		return result;
	}
   
   /**
    * 从指定位置读取文件剩余内容
    * @param file
    * @param lastSeek
    * @return
    * @throws Exception
    */
   public static HashMap readFileSeek(String file,long lastSeek) throws Exception {
	   HashMap map = new HashMap();
	   RandomAccessFile raf = null;
	   StringBuffer sb = new StringBuffer();
	   try {
		   raf = new RandomAccessFile(file, "r");
		   
		   String str = null;
		   raf.seek(lastSeek);
		   while ((str = raf.readLine()) != null) {
			   sb.append(str).append("\n");
			   //System.out.println(str);
		   }
		   lastSeek = raf.length();
	   } catch (Exception e) {
		   e.printStackTrace();
	   }finally{			
		   if (raf!=null){
			   raf.close();
			   raf=null;
		   }
	   }
	   
	   map.put("lastseek",""+lastSeek);
	   map.put("con",sb);
	   return map;
   }
   
   
   
   
   
   /*
    * 转换秒格式
    */
   public static String getChangTimeFam(int timeN) {
	   String timen2 = "";
		if (timeN > 60) {
			int hour = 0, min = 0, sec = 0;
			if (timeN > 60 * 60) {
				hour = timeN / (60 * 60);
				min = (timeN % (60 * 60)) / 60;

				sec = timeN % 60;
				if (min == 0) {
					timen2 = hour + "小时";
				} else if (sec == 0) {
					timen2 = hour + "小时" + min + "分钟";
				} else {
					timen2 = hour + "小时" + min + "分钟" + sec + "秒";
				}

			} else {
				min = timeN / 60; 
				sec = timeN % 60;
				if (sec == 0) {
					timen2 = min + "分钟";
				} else {
					timen2 = min + "分钟"+sec+"秒";
				}
			}

		} else {
			timen2 = timeN + "秒";
		}
		return timen2;

	} 
   

   /**
	 * 把HTML格式的颜色转为 Color
	 * @param color
	 * @return
	 */
	public static Color getHtmlColor(String color) {
		try {
			if(color.charAt(0) == '#') {
				color =color.substring(1);
			}
			if(color.length() != 6) {
				return null;
			}
		
			int r =Integer.parseInt(color.substring(0,2), 16);
			int g =Integer.parseInt(color.substring(2,4), 16);
			int b =Integer.parseInt(color.substring(4), 16);
			return new Color(r, g, b);
		} catch(Exception nfe) {
			log.error("HTML格式的颜色转为 Color,出错！"+nfe.getMessage());
			return null;
		}
	}
	
	/**
	 * 把HTML格式的颜色转为 Color ,加上透明度
	 * @param color
	 * @return
	 */
	public static Color getHtmlColor(String color,String alpha) {
		try {
			if(color.charAt(0) == '#') {
				color =color.substring(1);
			}
			if(color.length() != 6) {
				return null;
			}
		
			int r =Integer.parseInt(color.substring(0,2), 16);
			int g =Integer.parseInt(color.substring(2,4), 16);
			int b =Integer.parseInt(color.substring(4), 16);
			if (Utils.bBlank(alpha) || Integer.parseInt(alpha)<0){
				return new Color(r, g, b);
			}else{
				return new Color(r, g, b,Integer.parseInt(alpha));
			}
		} catch(Exception nfe) {
			log.error("HTML格式的颜色转为 Color,出错！"+nfe.getMessage());
			return null;
		}
	}
	
	/**
	 * 把字符串的字体表示，转为Font类型
	 * @param sfont 格式：宋体-p-12  其中，字体:“宋体”，类型：普通（p-普通,b-加粗,i-斜体)，字号：12
	 * @return
	 */
	public static Font toChangeFont(String sfont){
		Font f = null;
		if (sfont==null || sfont.indexOf("-")<=0){
			f = new Font("宋体", Font.PLAIN, 12);
		}else{
			String[] sa = sfont.split("-");
			String s1 = sa[0];
			String s2 = sa[1];
			String s3 = sa[2];
			try{
				if (s2.equals("b")){
					f = new Font(s1, Font.BOLD, Integer.parseInt(s3));
				}else if (s2.equals("i")){
					f = new Font(s1, Font.ITALIC, Integer.parseInt(s3));
				}else{
					f = new Font(s1, Font.PLAIN, Integer.parseInt(s3));
				}
			}catch(Exception e){
				f = new Font("宋体", Font.PLAIN, 12);
			}
		}
		return f;
	}
	
	/**
	 * 取得唯一号
	 */
	public static String getUniId(){
		UUID uuid  =  UUID.randomUUID(); 
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 设置日志级别
	 */
	public static Level setLogLevel(String str){
		Level l =null;
		
		if (Utils.equ(str,"debug")){
			l = Level.DEBUG;
		}else if (Utils.equ(str,"info")){
			l = Level.INFO;
		}else if (Utils.equ(str,"warn")){
			l = Level.WARN;
		}else if (Utils.equ(str,"error")){
			l = Level.ERROR;
		}
		
		return l;
	}
	
	
	public static Object getHashMap(HashMap map,Object key){
		Object o = null;
		try{
			o = map.get(key);
		}catch(Exception e){}
		
		return o;
	}
	
	public static String getNotInTime(String stype,String btime,String etime,String orderby,
			String notin,String feildname){
		String str = null;
		
		if (Utils.equ(stype,"d")){
			str=
				"select to_char(ddatetime,'yyyy-mm-dd') "+feildname+" from t_day_dim\n" +
				" where ddatetime>=to_date('"+btime+"','yyyy-mm-dd') " +
				" and ddatetime<=to_date('"+etime+"','yyyy-mm-dd')\n" + 
				" and to_char(ddatetime,'yyyy-mm-dd') not in ("+notin+")\n" + 
				"order by ddatetime "+orderby;
		}

		return str;
	}
	
	public static String getRandom(int ran) {
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<ran; i++) {
			int tmp = random.nextInt(10);
			buffer.append(tmp);
		}
		return buffer.toString();
		
	}


}
