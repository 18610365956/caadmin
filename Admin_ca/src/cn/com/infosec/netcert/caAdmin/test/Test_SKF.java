package cn.com.infosec.netcert.caAdmin.test;

import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.Set;

import cn.com.infosec.jce.provider.InfosecProvider;
import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.framework.crypto.CryptoException;
import cn.com.infosec.netcert.framework.crypto.IHSM;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.CertContainer;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.UsbKeySKFImpl;

public class Test_SKF {

	private static String[] csr;
	public static final String NameConnector = new String(new byte[] { 10 });
	public static void main(String[] args) throws CryptoException, IOException {

		Security.addProvider(new InfosecProvider());

		//UsbKeySKFImpl.loadLib("D:\\ISSKFAPI31001.dll");
		String lib = "D:\\ISSKFAPI31001.dll";
		lib = "C:/Windows/System32/Drcbank_ShuttleCsp11_3000GM.dll";
	
		String[] dev;
		//dev = UsbKeySKFImpl.enumDev("arguskey_csp11.dll");
		//dev = UsbKeySKFImpl.enumDev("ISSKFAPI31001");
		dev = UsbKeySKFImpl.enumDev(lib);
		//dev = UsbKeySKFImpl.enumDev("D:\\arguskey_csp11.dll");
		
		
		
		// 列序列号
		for (String s : dev)
			System.out.println(s);
/*
		// 指定一个key，对指定的key操作
		// 列证书
		UsbKeySKFImpl k1,k2;
		CertContainer[] cc;
		k1 = new UsbKeySKFImpl("D:\\ISSKFAPI31001.dll", "3CA944211854190C");
		k2 = new UsbKeySKFImpl("D:\\ISSKFAPI31001.dll", "3CA9442118527E0C");
		
		Env.getMap().put("111", k1);
		Env.getMap().put("222", k2);
		//k = new UsbKeySKFImpl("arguskey_csp11.dll", "95130007BB6B3CA4"); // arguskey_csp11
		cc = k1.enumCert(IHSM.SIGN);
		//cc = k.enumCert(IHSM.ENC);
		for (CertContainer c : cc)
			System.out.println(
					"certsn:[" + c.getCert().getSerialNumber().toString(16) + "] 容器名:[" + c.getContainerName() + "]");
*/
		// 认证pin
		//k.verifyPIN("1234");

		// 产生p10
		
		/*
		String[] p10;
		try {
			p10 = k.genCSR(256, new X509Name("cn=test"));
			System.out.println("容器名：" + p10[0].toString());
			System.out.println("P10：" + p10[1].toString());
		} catch (CryptoException e) {
			if (e instanceof NeedPinException) {
				k.verifyPIN("1234");
				p10 = k.genCSR(256, new X509Name("cn=test"));
				System.out.println("容器名：" + p10[0].toString());
				System.out.println("P10：" + p10[0].toString());
			} else {
				e.printStackTrace();
			}
		} 
		 */
		
		// //导入证书
		// String cerStr =
		// "MIIB7jCCAZKgAwIBAgIFVfxSZvEwDAYIKoEcz1UBg3UFADA2MQswCQYDVQQGEwJDTjETMBEGA1UECgwKU0lYVFdPX1NNMjESMBAGA1UEAwwJU0lYVFdPU00yMB4XDTE5MDQyODAzMjEzMloXDTIyMDEyMTAzMjEzMlowETEPMA0GA1UEAwwGdGVzdDAxMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEtLO5sQa9TcecgDRaoQNNowq0puPTkFpgcYE1ATpz4PNLrAgolT2OecfAgGGoqPh9gmWWayIfSOiVuNUYgSd576OBrzCBrDAfBgNVHSMEGDAWgBQT+ZP50AsUiqn1LDemYXs/JMuj7jAJBgNVHRMEAjAAMFIGA1UdHwRLMEkwR6BFoEOkQTA/MQ0wCwYDVQQDDARjcmwyMQwwCgYDVQQLDANjcmwxEzARBgNVBAoMClNJWFRXT19TTTIxCzAJBgNVBAYTAkNOMAsGA1UdDwQEAwIEMDAdBgNVHQ4EFgQUTch7iO6czg8aOyuJFY+oa7XTptYwDAYIKoEcz1UBg3UFAANIADBFAiEA3GiogV6V3YHlycQ7YY6UoaEbxf67/n6aI7FYwykQpmwCIEPO834OkLPdh4egwz17Q5htctI26VoudYXH17GbHbbV";
		// String envStr =
		// "MIHtMAkGByqBHM9VAWgweQIhALSzubEGvU3HnIA0WqEDTaMKtKbj05BaYHGBNQE6c+DzAiBLrAgolT2OecfAgGGoqPh9gmWWayIfSOiVuNUYgSd57wQg6MpUh3SYiHaz7hYF0VaThSxNQIn3BO7YE+sf0EeZ2xUEEG64ud79MXPb3OnpvEN53eMDQgAEtLO5sQa9TcecgDRaoQNNowq0puPTkFpgcYE1ATpz4PNLrAgolT2OecfAgGGoqPh9gmWWayIfSOiVuNUYgSd57wMhAAiyBYCEIqDPNpZ/l/0sKK6IE06aRmi9DEqk0NF1ZcyR";
		// k.importEncKeyPair("1556419886453",
		// Base64.decode(cerStr),Base64.decode(envStr));

		// 签名，keyIdx是容器名
		// byte[] hash = SoftCryptoAndStore.hash("SM3", "1111".getBytes());

		// byte[] sign = k.sign("1554271952260", 256, null, hash, "SM3withSM2",
		// "1234567812345678");

		// try{
		// csr = k.genCSR(256, new X509Name("cn=test"));
		// }catch (NeedPinException e1) {
		// int result = k.verifyPIN("1234");
		// if (result == 0) {
		// csr = k.genCSR(256, new X509Name("cn=test"));
		// }
		// }

		// try {
		// k.importCert("容器名称", b);
		// } catch (NeedPinException e1) {

		// int result = k.verifyPIN("1234");
		// if (result == 0) {
		// k.importCert("84384021052E8C09", b);
		// }
		// }

		// System.out.println("111");

		// 最后释放资源
		
		Set<String> keySet = Env.getMap().keySet();
		for (String key : keySet) {
			Env.getMap().get(key).release();
			System.out.println("111");
		}
		System.out.println("333");
		
		//k1.release();
		//k2.release();
	}
}
