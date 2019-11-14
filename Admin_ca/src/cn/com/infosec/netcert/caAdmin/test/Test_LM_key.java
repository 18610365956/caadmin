package cn.com.infosec.netcert.caAdmin.test;

import java.security.Security;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import cn.com.infosec.asn1.x509.X509Name;
import cn.com.infosec.jce.provider.InfosecProvider;
import cn.com.infosec.netcert.caAdmin.ui.admin.Panel_VerifyPin;
import cn.com.infosec.netcert.framework.crypto.CryptoException;
import cn.com.infosec.netcert.framework.crypto.IHSM;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.CertContainer;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.NeedPinException;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.UsbKeySKFImpl;
import cn.com.infosec.util.Base64;

/**      
 * @Time 2019-11-04 13:47
 */
public class Test_LM_key {

	public static void main(String[] args) {

		Security.addProvider(new InfosecProvider());

		String driverFile = "D:\\2__Program\\5__Key_Tools\\GM3000 V3.6 B20190827(1)\\GM3000 V3.6 B20190827\\skf\\windows\\x64\\mtoken_gm3000.dll";
		driverFile = "C:\\Windows\\System32\\mtoken_gm3000.dll";
		
		String[] devItems = null;
		try {
			devItems = UsbKeySKFImpl.enumDev(driverFile);
		} catch (CryptoException e) {
			e.printStackTrace();
		}
		for (String sn : devItems) {
			System.out.println("SN :" + sn);
		}
		
		UsbKeySKFImpl skf = null;
		try {
			skf = new UsbKeySKFImpl(driverFile, devItems[0]);
		} catch (CryptoException e) {
			e.printStackTrace();
		}
		String cer = "MIICEzCCAbegAwIBAgIGAPpZnGPfMAwGCCqBHM9VAYN1BQAwQjELMAkGA1UEBhMCY24xEDAOBgNVBAoMB2luZm9zZWMxDzANBgNVBAsMBnJvb3RjYTEQMA4GA1UEAwwHaW5mb3NlYzAeFw0xOTExMDQwOTE3MzJaFw0yMDExMDQwOTE3MzJaMBgxFjAUBgNVBAMMDXRfc2lnbl8xMTA0MDYwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAAResgqafKuzSnwuyTVm6HQVIJkCTCOhkFs+K60514+Y6l7SV7KMpxXPF4sKj+8cRAOjVv4t3EMW4dccXYKgDs/Mo4HAMIG9MB8GA1UdIwQYMBaAFOdIis8RYJh3UNkpqPp+N6hFNap0MAkGA1UdEwQCMAAwYAYDVR0fBFkwVzBVoFOgUaRPME0xDTALBgNVBAMMBGNybDExDDAKBgNVBAsMA2NybDEPMA0GA1UECwwGcm9vdGNhMRAwDgYDVQQKDAdpbmZvc2VjMQswCQYDVQQGEwJjbjAOBgNVHQ8BAf8EBAMCBsAwHQYDVR0OBBYEFJ6uzn4UtC2KvBg9xBCE3A8mc7hGMAwGCCqBHM9VAYN1BQADSAAwRQIgJYvdvqNGjsME/HOm1GmpdrKJglztfjPj3Cjw+9JXpEICIQCrJvkVwqWUfuRSGPZygGmVMLA9gEe+eRtljJl0C/g6yA==";

		try {
			byte[] cerBin = Base64.decode(cer);
			try {
				skf.importCert("123", cerBin);
			} catch (NeedPinException e1) {
				skf.verifyPIN("12345678");
				skf.importCert("123", cerBin);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("finish");
	}

}
