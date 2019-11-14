package cn.com.infosec.netcert.caAdmin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * @Author 江岩    
 * @Time 2019-06-05 10:11
 */
public class Utils {
	public static String newLine = System.getProperty("line.separator");
	public static final String NameConnector = new String(new byte[] { 10 });
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * 字符串转日期格式
	 * @param     (String)
	 * @return    (Date)   
	 * @throws    (ParseException)
	 * @Author 江岩 
	 * @Time   2019-06-05 10:12
	 * @version 1.0 
	 * 将数据库读出来的 String 转换为 Date 对象
	 */
	public static Date parse(String s) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.parse(s);
	}
	/**
	 * 日期格式转字符串
	 * @param     (Date)
	 * @return    (String)   
	 * @throws    (ParseException)
	 * @Author 江岩 
	 * @Time   2019-06-05 10:12
	 * @version 1.0
	 * 将 Date 对象转换成 Sting 用来显示 
	 */
	public static String format(Date d) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(d);
	}
	/**
	 * 日期格式转字符串
	 * @param    (Date) 
	 * @return   (String)      
	 * @throws   (ParseException)
	 * @Author 江岩 
	 * @Time   2019-06-05 10:13
	 * @version 1.0
	 * @memo  'Z' 格林威治标准时间
	 */
	public static String formatZ(Date d) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		return df.format(d);
	}
	/**
	 * 日期格式转字符串  GMT 北京时间前置8小时
	 * @param    (Date) 
	 * @return   (String)      
	 * @throws   (ParseException)
	 * @Author 江岩 
	 * @Time   2019-06-05 10:13
	 * @version 1.0
	 * @memo  'Z' 格林威治标准时间
	 * 将 Date 对象转换成 String 为了保存到数据库
	 */
	public static String formatZwithGMT(Date d) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(d);
	}
	/**
	 * 字符串 转 日期格式 : 用于 归档任务查看时间转换
	 * @param     (Date)
	 * @return    (String)   
	 * @throws    (ParseException)
	 * @Author 江岩 
	 * @Time   2019-07-23 17:45
	 * @version 1.0
	 */
	public static Date parse_archiveTime(String s) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.parse(s);
	}
	/**
	 * 日期格式转字符串 : 用于 归档任务查看时间转换
	 * @param     (Date)
	 * @return    (String)   
	 * @throws    (ParseException)
	 * @Author 江岩 
	 * @Time   2019-07-23 17:45
	 * @version 1.0
	 */
	public static String format_archiveTime(Date d) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(d);
	}
	/**
	 * 检验电话号码和手机号  
	 * @Author 江岩 
	 * @Time   2019-08-26 10:19
	 * @version 1.0
	 */
	public static boolean checkTel(String tel) {
		Pattern p0 = null, p1 = null, p2 = null;
		Matcher m = null;
		boolean flag = false;
		p0 = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		p1 = Pattern.compile("^[0][1-9]{1}[0-9]{1,2}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		m = p0.matcher(tel);
		flag = m.matches();
		if (!flag) {
			if (tel.length() > 9) {
				m = p1.matcher(tel);
				flag = m.matches();
			} else {
				m = p2.matcher(tel);
				flag = m.matches();
			}
		}
		return flag;
	}
	
	/**
	 *  email校验
	 * @Description 检验email格式，但不检测是否为空
	 * @param     (email)
	 * @return    (boolean)    
	 * @Author 江岩 
	 * @Time   2019-06-05 10:16
	 * @version 1.0
	 */
	public static boolean checkEmail(String email) {
		if (email.indexOf("@") == -1 || email.indexOf(".") == -1) {
			return false;
		}
        String reg = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(email);
        if (m.matches())
            return true;
        else
            return false;
	}
	/**
	 * IP地址验证
	 * @Description 四位以 . 分割的小于255的字符串，不能为空
	 * @param     (String)
	 * @return    (boolean)  
	 * @Author 江岩 
	 * @Time   2019-06-05 10:18
	 * @version 1.0
	 */
	public static boolean checkIp(String ip){
		if(ip == null || ip.length() == 0)
			return false;
		String reg = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(ip);
        if (m.matches())
            return true;
        else
            return false;
	}
	/**
	 * 端口号校验
	 * @Description (端口号范围在 1 和 65535之间)
	 * @param     (String)
	 * @return    (String) 1 格式异常   2 0--65535  0 成功 
	 * @Author 江岩 
	 * @Time   2019-06-05 10:20
	 * @version 1.0
	 */
	public static int checkPort(String port) {  
		Pattern pattern = Pattern.compile("[0-9]+");
        Matcher isPort = pattern.matcher(port);
        if (!isPort.matches()) {
            return 1;
        }
        if(Integer.valueOf(port) < 1 || Integer.valueOf(port) > 65535)
        	return 2;
        return 0;
	 }

	/**
	 * 证书状态转换
	 * @Description 将服务器返回的证书状态值 (1234)，转换为中文
	 * @param     (String)
	 * @return    (String)   
	 * @Author 江岩 
	 * @Time   2019-06-05 10:30
	 * @version 1.0
	 */
	public static String changeStatus(String st){	
		if("1".equals(st))
			st = l.getString("notDownload");
		else if("2".equals(st))
			st = l.getString("normal");
		else if("3".equals(st))
			st = l.getString("lock");
		else if("4".equals(st))
			st = l.getString("revoke");
		else
			st = "";
		return st;
	}
	/**
	 * 处理操作日志失败时，遇到的null对象，和 日志记录签名问题
	 * @Author 江岩 
	 * @Time   2019-07-19 15:39
	 * @version 1.0
	 */
	public static String nullToEmpty(String s) {
		if (s == null)
			return "";
		else
			return s.trim().replaceAll("\n", "").replaceAll("\r", ""); // 之前版本记录操作日志的Target时，有些processor包含换行符，导致caAdmin审计签名验证不过。所以要去掉。
	}
}






