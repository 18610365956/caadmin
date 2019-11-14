package cn.com.infosec.netcert.caAdmin.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import cn.com.infosec.jce.provider.InfosecProvider;
import cn.com.infosec.util.Base64;

/**   
 * @Description file 转  字符串   &  file 转  Certificate
 * @Author 江岩    
 * @Time 2019-07-18 13:57
 */
public class Test_Cert_and_String {

	/**       
	 * @Author 江岩 
	 * @Time   2019-07-18 13:57
	 * @version 1.0 
	 * @throws NoSuchProviderException 
	 * @throws CertificateException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws CertificateException, NoSuchProviderException, IOException {
		Security.addProvider(new InfosecProvider());
		
		// 文件
		//String cert = "D:/cert.p7b";
		String cert = "E:/1_Project/7_From_132/sm2/ca__0923.cer";
		
		// 文件  转  String 
		FileInputStream fin_1 = new FileInputStream(new File(cert));
		byte[] bytes = new byte[fin_1.available()];
		fin_1.read(bytes);
		fin_1.close();
		
		String str = Base64.encode(bytes);
		System.out.println(str.length());
		System.out.println("证书文件:"+str);
		
		// 文件   转  Certificate
		FileInputStream fin_2 = new FileInputStream(new File(cert));
		CertificateFactory cf = CertificateFactory.getInstance("X.509", "INFOSEC");
		Certificate cer_2 = cf.generateCertificate(fin_2);
		fin_2.close();

		// Certificate  转   String 
		String cerB64 = Base64.encode(cer_2.getEncoded());
		System.out.println(cerB64.length());
		System.out.println("cert的String："+cerB64);

	}
}





