package cn.com.infosec.netcert.caAdmin.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**  
 * @Description  SN תΪ  16���� 
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
		String hex = "1ab924"; // 16����
		//String hex = "020202ba"; // 16����
		
		// BigDecimal ���ɱ�����⾫��������
		// string ת  BigDecimal
		String SN = new BigDecimal("13321713").toBigInteger().toString(16);
		System.out.println(SN);

		// string ת  BigDecimal
		BigDecimal integer  = new BigDecimal(new BigInteger("7F2E96",16));			
		System.out.println(integer);		
	}
}