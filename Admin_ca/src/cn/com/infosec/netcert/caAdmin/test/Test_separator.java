package cn.com.infosec.netcert.caAdmin.test;

import java.util.Properties;

/**   
 * @Description (≤π≥‰√Ë ˆ) 
 * @Author Ω≠—“    
 * @Time 2019-10-28 11:55
 */
public class Test_separator {

	/**       
	 * @param     
	 * @return        
	 * @throws   
	 * @Author Ω≠—“ 
	 * @Time   2019-10-28 11:55
	 * @version 1.0 
	 */
	public static void main(String[] args) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("-----BEGIN NEW CERTIFICATE REQUEST-----")
		.append(System.getProperty("line.separator"))
		.append("MIH7MIGiAgEAMEIxCzAJBgNVBAYTAmNuMRAwDgYDVQQKDAdpbmZvc2VjMQ8wDQYD")
		.append(System.getProperty("line.separator"))
		.append("-----END NEW CERTIFICATE REQUEST-----");
	
		System.out.println(sb.toString());
		

		Properties p = new Properties();
		p.setProperty("csr", sb.toString());
		System.out.println(p.getProperty("csr"));
		
	}

}




