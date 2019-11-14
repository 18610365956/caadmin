package cn.com.infosec.netcert.caAdmin.utils;

import java.security.cert.Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

import java.security.cert.X509Certificate;

import cn.com.infosec.netcert.caAdmin.ui.admin.Panel_VerifyPin;
import cn.com.infosec.netcert.caAdmin.utils.Env.ALG;
import cn.com.infosec.netcert.framework.Request;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.crypto.CryptoException;
import cn.com.infosec.netcert.framework.crypto.IHSM;
import cn.com.infosec.netcert.framework.crypto.SM2Id;
import cn.com.infosec.netcert.framework.crypto.impl.SoftCryptoAndStore;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.NeedPinException;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.UsbKeyCSPImpl;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.UsbKeySKFImpl;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.protocol.IProtocol;
import cn.com.infosec.netcert.framework.protocol.Protocol;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;
import cn.com.infosec.netcert.framework.transport.Sender;
import cn.com.infosec.netcert.framework.transport.chanel.ChannelConf;
import cn.com.infosec.netcert.framework.transport.chanel.ChannelSecure;
import cn.com.infosec.netcert.framework.transport.chanel.ChannelSign;

/**
 * ca_admin ��ͨ�Ź���
 * @Author ����    
 * @Time 2019-06-04 21:02
 */
public class AdminClient {
	public Certificate signCert; // ǩ��֤��
	
	private IHSM hsm;
	private Sender sender;

	private boolean isAnonymous; // true-����,���������Ĳ�ǩ��; false-����������ǩ��
	private String keyIdx, signAlgName; // ǩ����Կ��ʶ��ǩ���㷨
	private String channelName = "ADMIN", protocolName, localIp;
	
	FileLogger log = FileLogger.getLogger(AdminClient.class);
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * ����ͻ���
	 * @param channelName  ͨ������
	 * @param chSign   ����ǩ������
	 * @param chSecure ͨѶ��ȫ����
	 * @param chConf  ͨѶ�������ã�ip���˿ڵ�
	 * @throws Exception
	 */
	public AdminClient(ChannelSign chSign, ChannelSecure chSecure, String protocolName, ChannelConf chConf)
			throws Exception {
		this.protocolName = protocolName;
		this.localIp = chConf.getLocalIp();
		this.sender = new Sender(chConf, chSecure);
		if (chSign != null) { 
			if (ALG.SM2 == Env.alg) {
				String skf_sn = chSign.getKeyIdx().split(Utils.NameConnector)[0];
				String keyDriverName = chSign.getHsm();
				this.hsm = new UsbKeySKFImpl(chSign.getHsm(), skf_sn);
				
				//System.out.println("new:" + ((UsbKeySKFImpl)hsm).getHApp() +","+ ((UsbKeySKFImpl)hsm).getHDev());  // ���� �б껷�� д�û�֤��ʧ��
				 
				Env.getMap().put(keyDriverName + skf_sn, (UsbKeySKFImpl)hsm);
				
			} else {
				this.hsm = new UsbKeyCSPImpl(chSign.getHsm());
			}
			this.keyIdx = chSign.getKeyIdx();
			this.signAlgName = chSign.getSignAlgName();
			this.signCert = chSign.getSignCert();

			this.isAnonymous = false;
		} else {
			this.isAnonymous = true;
		}
	}

	/**
	 * �������󵽷�����     
	 * @Description  ����typeֵ �� Properties ��������������������type ������Ե� ��������
	 * @param      (String,Properties)
	 * @return     (Response)   
	 * @throws     (Exception)
	 * @Author ���� 
	 * @Time   2019-06-04 21:03
	 * @version 1.0
	 * @throws Exception 
	 */
	public Response sendRequest(String type, Properties p) throws Exception {
		Request req = new Request(type);
		req.setHeader(PropertiesKeysRes.VERSION, "0");
		req.setHeader(PropertiesKeysRes.LOCALTIME, String.valueOf(System.currentTimeMillis()));
		req.setHeader(PropertiesKeysRes.TASKTAG, String.valueOf(new Random().nextInt(100000)));
		if (p != null) {
			Collection<Object> pList = p.keySet();
			Iterator<Object> pIter = pList.iterator();
			while (pIter.hasNext()) {
				String name = (String) pIter.next();
				req.setValue(name, p.getProperty(name, ""));
			}
		}
		IProtocol protocol = Protocol.getInst(this.protocolName);
		// ע��Э��ʵ����
		req.setProtocol(protocol);
		// ����ͻ���ָ���˱���ip�������header
		if (this.localIp != null && this.localIp.length() > 0) {
			req.setHeader(PropertiesKeysRes.X_CLIENT_IP, this.localIp);
		}
		if (!this.isAnonymous) {
			byte[] sign = null;
			String containerName = keyIdx.split(Utils.NameConnector)[1];
			// ��ȡǩ��ԭ��
			if (ALG.SM2 == Env.alg) {
				String cert_SN = ((X509Certificate) this.signCert).getSerialNumber().toString(16);
				req.setHeader(PropertiesKeysRes.ID, cert_SN);
				//req.setSignCert(signCert);
				byte[] source = req.getSignSource();
				String sm2id_sign = SM2Id.getSignId(channelName);
				byte[] hash = SoftCryptoAndStore.SM3WithId(source, sm2id_sign.getBytes(), signCert.getPublicKey());
				// ��������ǩ��
				try {
					//log.errlog("-----info_0---");
					//log.errlog("-----"+ containerName +"----");
					// System.out.println("sign:" + ((UsbKeySKFImpl)hsm).getHApp() +","+ ((UsbKeySKFImpl)hsm).getHDev()); ���� �б�����д�û���֤��ʧ��
					sign = hsm.sign(containerName, 0, null, hash, signAlgName, sm2id_sign);
					//log.errlog("-----info_0---");
				} catch (NeedPinException ee) {
					Panel_VerifyPin verifyPin = new Panel_VerifyPin();
					verifyPin.setBlockOnOpen(true);
					int w = verifyPin.open();
					if (w == 0) { // ��ť����close()�رգ�����������
						UsbKeySKFImpl skf = (UsbKeySKFImpl) this.hsm;
						int result = skf.verifyPIN(verifyPin.pin);
						if (result == 0) {
							//log.errlog("-----info_1----");
							//log.errlog("-----"+ containerName +"----");
							sign = hsm.sign(containerName, 0, null, hash, signAlgName, sm2id_sign);
							//log.errlog("-----info_1----");
						} else {
							throw new Exception(l.getString("Notice_error_PIN"));
						}
					} else {
						throw new Exception(l.getString("Notice_null_PIN"));
					}
				}
			}
			if (ALG.RSA == Env.alg) {
				String csp_sn = keyIdx.split(Utils.NameConnector)[0];
				req.setHeader(PropertiesKeysRes.ID, csp_sn);
				byte[] source = req.getSignSource();
				sign = hsm.sign(containerName, 0, null, source, signAlgName, null); 
			}
			req.setSignature(sign);
			req.setSignAlg(signAlgName);
		}	
		// �õ������������
		byte[] b = req.getEncoded();
		// �����������ݵ����������õ� ��Ӧ����
		byte[] r = this.sender.send(b);
		Response resp = protocol.parseResponse(r);
		resp.setProtocol(protocol);
		String errorNum = resp.getHeader(PropertiesKeysRes.ERRORNUM);
		if (errorNum.equals("0")) {
			return resp;
		} else {
			throw new ServerException(errorNum, resp.getHeader(PropertiesKeysRes.ERRORMSG));
		}
	}
	/** 
	 * @Description  ���� sign��ʹ��֤����ǩ��,�����֤ǩ��ʹ��
	 * @param      (byte[])
	 * @return     (byte[])  
	 * @throws     (Exception)
	 * @Author ���� 
	 * @Time   2019-06-04 21:03
	 * @version 1.0
	 * @throws CryptoException 
	 */
	public byte[] genSign(byte[] source) throws CryptoException {
		byte[] sign = null;
		String container = keyIdx.split(Utils.NameConnector)[1];
		if (ALG.SM2 == Env.alg) {
			String sm2id_sign = SM2Id.getSignId(this.channelName);
			byte[] hash = SoftCryptoAndStore.SM3WithId(source, sm2id_sign.getBytes(), signCert.getPublicKey());
			sign = this.hsm.sign(container, 0, null, hash, IHSM.SM3withSM2, sm2id_sign);
		}
		if (ALG.RSA == Env.alg) {
			sign = this.hsm.sign(container, 0, null, source, IHSM.SHA1withRSA, null);
		}
		return sign;
	}
	/**
	 * ��ȡ ��ǰʹ�õ��㷨��ֻ�� SM3withSM2  SHA1withRSA ����
	 * @Author ���� 
	 * @Time   2019-07-12 15:53
	 * @version 1.0
	 */
	public String getSignAlg() {
		return this.signAlgName;
	}
	/**
	 * �Ͽ����ӣ�����ر� ����ʱ�����������Ĺر�Ӧ��
	 * @Author ���� 
	 * @Time   2019-08-09 13:37
	 * @version 1.0
	 */
	public void close(){
		this.sender.close();
	}
}