package cn.com.infosec.netcert.caAdmin.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**  
 * @Description  SN 转为  16进制 
 * @Author jy    
 * @Time 2019-07-17 20:52
 */
public class Test_SN_HEX {
	/**       
	 * @Author jy 
	 * @Time   2019-07-17 20:52
	 * @version 1.0 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
	
		//String sn = "16314519";
		String sn = "16706245";
		String hex = "1ab924"; // 16进制
		//String hex = "020202ba"; // 16进制
		
		// BigDecimal 不可变的任意精度整数。
		// string 转  BigDecimal
		String SN = new BigDecimal("13321713").toBigInteger().toString(16);
		System.out.println(SN);

		// string 转  BigDecimal
		BigDecimal integer  = new BigDecimal(new BigInteger("7F2E96",16));			
		System.out.println(integer);		
	}
}
