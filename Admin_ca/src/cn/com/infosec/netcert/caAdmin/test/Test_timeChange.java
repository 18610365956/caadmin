package cn.com.infosec.netcert.caAdmin.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**   
 * @Description (补充描述) 
 * @Author jy
 * @Time 2019-07-23 18:06
 */
public class Test_timeChange {

	/**        
	 * @Author jy 
	 * @Time   2019-07-23 18:06
	 * @version 1.0 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		
		
		Date date = new Date();
		
		//Date_to_String();   // 日期格式转换为 String格式实例
		
		//String_to_Date_to_String();    // String格式转换为 其他的String格式，需要先转换为 Date格式
		
		//Calendar_And_Date();	//   Calendar 和  Date 的 关系
	}
	
	// 解析String 成为 Date(按照 " yyyyMMddHHmmss'Z' ")
	public static Date parse(String s) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.parse(s);
	}
	
	// 将 Date 转换为 String的格式(按照" yyyyMMddHHmmss'Z' ")
	public static String format(Date d){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(d);
	}
	
	// Date 转换 为 String
	public static void Date_to_String() throws ParseException {
		// Date 转 String
		Date date = new Date();
		System.out.println(date); // 当前时间输出显示为 ：Thu Jul 25 10:16:44 CST 2019

		SimpleDateFormat df_1 = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		String str_1 = df_1.format(date);
		System.out.println(str_1); // 输出显示为 ： 20190725101644Z   当前时区时间

		SimpleDateFormat df_2 = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		df_2.setTimeZone(TimeZone.getTimeZone("GMT"));
		String str_2 = df_2.format(date);
		System.out.println(str_2); // 输出显示为： 20190725021644Z  GMT 国际时间格式，中国时间 = GMT + 8小时，所以输出 时间要早8小时

		SimpleDateFormat df_3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str_3 = df_3.format(date);
		System.out.println(str_3); // 输出显示为 ： 2019-07-25 10:16:44

	}
	
	// String 转换为  Date
	public static void String_to_Date_to_String() throws ParseException {
		//  String 转换为 Date 时，String有没有 Z 都可以，格式中会处理
		//  切记 不要将 "20190725102542Z" 按 new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") 格式转换就行，解析时格式不匹配
		String str_4 = "20190725102542Z"; 
		System.out.println(str_4);		//输出显示为 ： 20190725102542Z
		
		SimpleDateFormat df_4 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date_4 = df_4.parse(str_4);
		System.out.println(date_4);    //输出显示为 ： Thu Jul 25 10:25:42 CST 2019
		
		
		String str_5 = "2019-07-25 10:25:42Z"; // 有没有Z没有影响
		System.out.println(str_5);     // 输出显示为 ： 2019-07-25 10:25:42Z
		
		SimpleDateFormat df_5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date_5 = df_5.parse(str_5);
		System.out.println(date_5);    // 输出显示为 ： Thu Jul 25 10:25:42 CST 2019
	}

	// 讲清楚 Calendar 和  Date 的 关系
	public static void Calendar_And_Date() {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2019);
		c.set(Calendar.MONTH, 0); // 输出的结果是 1月，表示月的标识有 ： MONTH ，请慎重使用
		c.set(Calendar.DAY_OF_MONTH, 1); // 输出结果 是 1，标识日的标识有 DAY_OF_MONTH 和 DATE ，两者结果一致，建议使用前者
		c.set(Calendar.HOUR_OF_DAY, 1); // 输出结果是 1，表示时的标识 有 HOUR_OF_DAY 和 HOUR ，其中 HOUR 为12显示法，超过12默认进位，推荐使用 前者
		c.set(Calendar.MINUTE, 30);
		c.set(Calendar.SECOND, 30);

		// 通常日历需要先转 Date
		Date date = c.getTime();
		System.out.println(date); // 输出显示为  Tue Jan 01 01:30:30 CST 2019  ，此为Date格式
		// 特别注意 c.set(xxxx,xxx); 任何值没有手动设置，将获取当前计算机的时间/系统时间 
	}

	// 应用场景： 数据库获取 20190725101644Z，输出为 2019-07-25 10:16:44 
	public static void demo() throws ParseException {
		// 先根据 String的格式 转为 Date
		String str_6 = "20190725101644Z";
		SimpleDateFormat df_6 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date_6 = df_6.parse(str_6);
		System.out.println(date_6); // 输出显示为：Thu Jul 25 10:16:44 CST 2019
		// 再根据需要的格式，转为 String
		SimpleDateFormat df_7 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str_7 = df_7.format(date_6);
		System.out.println(str_7); // 输出显示为： 2019-07-25 10:16:44
	}
}
