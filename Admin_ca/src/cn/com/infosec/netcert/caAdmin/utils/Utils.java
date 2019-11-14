package cn.com.infosec.netcert.caAdmin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ������
 * @Author ����    
 * @Time 2019-06-05 10:11
 */
public class Utils {
	public static String newLine = System.getProperty("line.separator");
	public static final String NameConnector = new String(new byte[] { 10 });
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * �ַ���ת���ڸ�ʽ
	 * @param     (String)
	 * @return    (Date)   
	 * @throws    (ParseException)
	 * @Author ���� 
	 * @Time   2019-06-05 10:12
	 * @version 1.0 
	 * �����ݿ�������� String ת��Ϊ Date ����
	 */
	public static Date parse(String s) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.parse(s);
	}
	/**
	 * ���ڸ�ʽת�ַ���
	 * @param     (Date)
	 * @return    (String)   
	 * @throws    (ParseException)
	 * @Author ���� 
	 * @Time   2019-06-05 10:12
	 * @version 1.0
	 * �� Date ����ת���� Sting ������ʾ 
	 */
	public static String format(Date d) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(d);
	}
	/**
	 * ���ڸ�ʽת�ַ���
	 * @param    (Date) 
	 * @return   (String)      
	 * @throws   (ParseException)
	 * @Author ���� 
	 * @Time   2019-06-05 10:13
	 * @version 1.0
	 * @memo  'Z' �������α�׼ʱ��
	 */
	public static String formatZ(Date d) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		return df.format(d);
	}
	/**
	 * ���ڸ�ʽת�ַ���  GMT ����ʱ��ǰ��8Сʱ
	 * @param    (Date) 
	 * @return   (String)      
	 * @throws   (ParseException)
	 * @Author ���� 
	 * @Time   2019-06-05 10:13
	 * @version 1.0
	 * @memo  'Z' �������α�׼ʱ��
	 * �� Date ����ת���� String Ϊ�˱��浽���ݿ�
	 */
	public static String formatZwithGMT(Date d) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(d);
	}
	/**
	 * �ַ��� ת ���ڸ�ʽ : ���� �鵵����鿴ʱ��ת��
	 * @param     (Date)
	 * @return    (String)   
	 * @throws    (ParseException)
	 * @Author ���� 
	 * @Time   2019-07-23 17:45
	 * @version 1.0
	 */
	public static Date parse_archiveTime(String s) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.parse(s);
	}
	/**
	 * ���ڸ�ʽת�ַ��� : ���� �鵵����鿴ʱ��ת��
	 * @param     (Date)
	 * @return    (String)   
	 * @throws    (ParseException)
	 * @Author ���� 
	 * @Time   2019-07-23 17:45
	 * @version 1.0
	 */
	public static String format_archiveTime(Date d) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(d);
	}
	/**
	 * ����绰������ֻ���  
	 * @Author ���� 
	 * @Time   2019-08-26 10:19
	 * @version 1.0
	 */
	public static boolean checkTel(String tel) {
		Pattern p0 = null, p1 = null, p2 = null;
		Matcher m = null;
		boolean flag = false;
		p0 = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // ��֤�ֻ���
		p1 = Pattern.compile("^[0][1-9]{1}[0-9]{1,2}-[0-9]{5,10}$"); // ��֤�����ŵ�
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // ��֤û�����ŵ�
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
	 *  emailУ��
	 * @Description ����email��ʽ����������Ƿ�Ϊ��
	 * @param     (email)
	 * @return    (boolean)    
	 * @Author ���� 
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
	 * IP��ַ��֤
	 * @Description ��λ�� . �ָ��С��255���ַ���������Ϊ��
	 * @param     (String)
	 * @return    (boolean)  
	 * @Author ���� 
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
	 * �˿ں�У��
	 * @Description (�˿ںŷ�Χ�� 1 �� 65535֮��)
	 * @param     (String)
	 * @return    (String) 1 ��ʽ�쳣   2 0--65535  0 �ɹ� 
	 * @Author ���� 
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
	 * ֤��״̬ת��
	 * @Description �����������ص�֤��״ֵ̬ (1234)��ת��Ϊ����
	 * @param     (String)
	 * @return    (String)   
	 * @Author ���� 
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
	 * ����������־ʧ��ʱ��������null���󣬺� ��־��¼ǩ������
	 * @Author ���� 
	 * @Time   2019-07-19 15:39
	 * @version 1.0
	 */
	public static String nullToEmpty(String s) {
		if (s == null)
			return "";
		else
			return s.trim().replaceAll("\n", "").replaceAll("\r", ""); // ֮ǰ�汾��¼������־��Targetʱ����Щprocessor�������з�������caAdmin���ǩ����֤����������Ҫȥ����
	}
}





