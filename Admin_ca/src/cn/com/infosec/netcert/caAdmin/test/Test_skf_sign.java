package cn.com.infosec.netcert.caAdmin.test;

import java.security.Security;

import cn.com.infosec.jce.provider.InfosecProvider;
import cn.com.infosec.netcert.framework.crypto.IHSM;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.CertContainer;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.UsbKeySKFImpl;

/**   
 
 * @Time 2019-10-28 14:17
 */
public class Test_skf_sign {

	/** 
	 * @Time   2019-10-28 14:17
	 * @version 1.0 
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Security.addProvider(new InfosecProvider());

		System.out.println("start");
		
		String keyDriverLibPath = "/opt/TESTca6.3all/NetCertCAAdmin_6.3.1.0001/libSKFAPI-x64-log.so";
		String[] devItems = UsbKeySKFImpl.enumDev(keyDriverLibPath);
		
		UsbKeySKFImpl skf = new UsbKeySKFImpl(keyDriverLibPath, devItems[0]);

//		skf.release();
		
		UsbKeySKFImpl skf_1 = new UsbKeySKFImpl(keyDriverLibPath, devItems[0]);

		System.out.println("111");

	}

}
