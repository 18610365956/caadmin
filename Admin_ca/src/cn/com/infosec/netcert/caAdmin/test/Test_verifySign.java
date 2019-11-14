package cn.com.infosec.netcert.caAdmin.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;

import cn.com.infosec.jce.provider.InfosecProvider;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.crypto.CryptoException;
import cn.com.infosec.netcert.framework.crypto.HSM;
import cn.com.infosec.netcert.framework.crypto.IHSM;
import cn.com.infosec.util.Base64;

/**   
 * @Author 江岩    
 * @Time 2019-07-15 20:06
 */
public abstract class Test_verifySign {

	public static void main(String[] args)
			throws CertificateException, NoSuchProviderException, IOException, CryptoException, ParseException {
		Security.addProvider(new InfosecProvider());

		//File certFile = null;
		String certStr = null;

		//certFile = new File("E:/1_Project/1_Eclipse/CA_Admin/caServer_50/cert/ca.cer");
		certStr = "MIIB6jCCAY2gAwIBAgIDLhuxMAwGCCqBHM9VAYN1BQAwPzELMAkGA1UEBhMCY24xEDAOBgNVBAoMB2luZm9zZWMxCzAJBgNVBAsMAmNhMREwDwYDVQQDDAhjYV9ha181MDAeFw0xOTA3MTEwNzAwMjdaFw0yMDA3MTAwNzAwMjdaMFgxCzAJBgNVBAYTAmNuMRAwDgYDVQQKDAdpbmZvc2VjMQswCQYDVQQLDAJjYTEXMBUGA1UECwwOYWRtaW5pc3RyYXRvcnMxETAPBgNVBAMMCGFvX2FrXzUwMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEqT2EVCm2YElAiv3zhcd/81ohEECch/vcA9+j9U0FF8jV3G25nyzkWjIdxjMcWllq2Fcclmkd7weoGOl7/14gy6NdMFswHwYDVR0jBBgwFoAUMLfUYlJ9BkB5zXGfl6CRlgjGpg8wCQYDVR0TBAIwADAOBgNVHQ8BAf8EBAMCB4AwHQYDVR0OBBYEFHT1qhKJgAQWcO/RUUqbMX28N6pUMAwGCCqBHM9VAYN1BQADSQAwRgIhAL3HrkPzC0Lg6764SvZ0K0tCLpTHItUL7EyMdO5o6EfxAiEAtQWHy1jO8CKrICzh0c/hh3w1PcE+knkCe05P9dIOTeA=";
		certStr = "MIIB/zCCAaOgAwIBAgIFUWEM7K0wDAYIKoEcz1UBg3UFADBAMQswCQYDVQQGEwJDTjEQMA4GA1UECgwHaW5mb3NlYzEOMAwGA1UECwwFYnVtZW4xDzANBgNVBAMMBlJPT1RDQTAeFw0xOTExMDUwNzA5MThaFw0yMDExMDUwNzA5MThaMBgxFjAUBgNVBAMMDXRfc2lnbl8xMTA1MDIwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAAS5F3Afv4gFJPCDI31VsifZFfC+PYsIQ7fL7BWAM1lG/P3dUgm6aElkJN48ljgyreLNHNaZxUeWT00z9iyvdMT8o4GvMIGsMB8GA1UdIwQYMBaAFLQiaZq+OdmP/Wj5kcjGoym2GwzeMAkGA1UdEwQCMAAwTwYDVR0fBEgwRjBEoEKgQKQ+MDwxDTALBgNVBAMMBGNybDExDDAKBgNVBAsMA2NybDEQMA4GA1UECgwHaW5mb3NlYzELMAkGA1UEBhMCQ04wDgYDVR0PAQH/BAQDAgbAMB0GA1UdDgQWBBTu3BrKuAzWjbVrcNkAfy5EGsmn+zAMBggqgRzPVQGDdQUAA0gAMEUCIQDfsKOC1Gv5ke1owFk6cbM8Q1MqmXz5SvFQ1sd0/s8aBAIgJr1dv2SnjnNfnIZ43hQ42XG4hmnhK/qpPfCPiijU+yU=";
		String src = "PHNvdXJjZT48aGVhZGVyPjxUQVNLVEFHPjE2OTYzPC9UQVNLVEFHPjxMT0NBTFRJTUU+MTU2MzE5MTkzNzM2ODwvTE9DQUxUSU1FPjxWRVJTSU9OPjA8L1ZFUlNJT04+PFRZUEU+TE9HSU48L1RZUEU+PC9oZWFkZXI+PC9zb3VyY2U+";
		String signature = "MEUCIFjeywklEQ44uIC9bX0Vp4gVGVChR9pIVuPOFfJLi+v6AiEA22bp2MawHZ5M0c1BKVud153gXSwIJnqHMb0vEzhDU4s=";

		Test_verifySign.getCertInfo(null, certStr); // 解析证书
		//Test_verifySign.verifySign(null, certStr, src, signature);// 验证签名
	}

	// 从Base64的证书字符串中获取 各项数据 
	public static void getCertInfo(File certFile, String certStr)
			throws ParseException, IOException, CertificateException, NoSuchProviderException {
		if (certFile != null) {
			FileInputStream fin_1 = new FileInputStream(certFile);
			byte[] bytes = new byte[fin_1.available()];
			fin_1.read(bytes);
			fin_1.close();
			certStr = Base64.encode(bytes);
		}

		byte[] cerBin = Base64.decode(certStr); // 解析 Base64
		CertificateFactory cf = CertificateFactory.getInstance("X.509", "INFOSEC");
		X509Certificate c = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(cerBin));

		System.out.println("SerialNumber:" + c.getSerialNumber());
		System.out.println("subjectDN:" + c.getSubjectDN());
		System.out.println("Issuer:" + c.getIssuerDN());
		System.out.println("Pubkey:" + Base64.encode(c.getPublicKey().getEncoded()));
		System.out.println("notBefore:" + Utils.format(c.getNotBefore()));
		System.out.println("notAfter:" + Utils.format(c.getNotAfter()));
	}

	// 验证签名
	public static void verifySign(File certFile, String certStr, String src, String signature)
			throws CertificateException, NoSuchProviderException, CryptoException, IOException {
		Security.addProvider(new InfosecProvider());
		X509Certificate cert = null;
		if (certFile == null) {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "INFOSEC");
			cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(Base64.decode(certStr)));
		} else {
			FileInputStream fin_1 = new FileInputStream(certFile);
			byte[] bytes = new byte[fin_1.available()];
			fin_1.read(bytes);
			fin_1.close();
			certStr = Base64.encode(bytes);
			byte[] cerBin = Base64.decode(certStr); // 解析 Base64
			CertificateFactory cf = CertificateFactory.getInstance("X.509", "INFOSEC");
			cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(cerBin));
		}
		
		
		IHSM hsm = HSM.getInst("");
		if (!hsm.verify(Base64.decode(src), Base64.decode(signature), cert.getPublicKey(), "SM3withSM2",
				"1234567812345678")) {
			System.out.println("verify error");
		} else {
			System.out.println("verify succ");
		}
	}
}
