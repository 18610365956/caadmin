package cn.com.infosec.netcert.caAdmin.ui.template.templateUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import cn.com.infosec.asn1.ASN1EncodableVector;
import cn.com.infosec.asn1.DERBMPString;
import cn.com.infosec.asn1.DEREncodable;
import cn.com.infosec.asn1.DERObjectIdentifier;
import cn.com.infosec.asn1.DEROctetString;
import cn.com.infosec.asn1.DERSequence;
import cn.com.infosec.asn1.DERTaggedObject;
import cn.com.infosec.asn1.DERUTF8String;
import cn.com.infosec.asn1.ocsp.OCSPObjectIdentifiers;
import cn.com.infosec.asn1.x509.GeneralName;
import cn.com.infosec.asn1.x509.GeneralNames;
import cn.com.infosec.asn1.x509.KeyPurposeId;
import cn.com.infosec.asn1.x509.X509Extension;
import cn.com.infosec.asn1.x509.X509Extensions;
import cn.com.infosec.asn1.x509.X509Name;
import cn.com.infosec.jce.X509KeyUsage;

import cn.com.infosec.netcert.framework.dao.annotation.Column;
import cn.com.infosec.netcert.framework.dao.annotation.Key;
import cn.com.infosec.netcert.framework.dao.annotation.Table;
import cn.com.infosec.netcert.framework.log.FileLogger;

/**
 * @项目名称：CAServer
 * @类名： CertTemplate.java
 * @描述： 模板类
 * @作者： 任龙
 * @创建日期： 2012-4-18 下午04:41:58
 * @版本： V1.0
 */
@Table("CERTTEMPLATE")
public class CertTemplate {
	private FileLogger log = FileLogger.getLogger(this.getClass());

	//用于附带的加密模板的一些状态值
	public static final String CERTTEMPLATE_STATUS_NA = "x";
	
	public static final String CERTTEMPLATE_STATUS_NOMAL = "0";
	public static final String CERTTEMPLATE_STATUS_PAUSED = "1";

	public static final String CERTTEMPLATE_STATUS_USED = "1";
	public static final String CERTTEMPLATE_STATUS_UNUSED = "0";

	public static final String CERTTEMPLATE_IS_SYS = "0";
	public static final String CERTTEMPLATE_NOT_SYS = "1";

	public static final String CERTTEMPLATE_NOT_CRL = "0";
	public static final String CERTTEMPLATE_CRL = "1";

	public static final String CERTTEMPLATE_NOT_CERT = "0";
	public static final String CERTTEMPLATE_PUBLISH_CERT = "1";

	public static final String ReKey = "1", ReNew = "0";

	public static final String Multi_DN_Allow = "1", Multi_DN_NOT_Allow = "0";

	public static final String ADMIN_TEMPLATE_NAME = "admin_signing";
	
	public static final String ValidityUnit_Day = "d", ValidityUnit_Month = "m", ValidityUnit_Year = "y";

	// 是否是添加模板操作  jy
	private boolean isAdd;
	
	// 是否是单证模板  jy
	private boolean isSignal;
	
	@Key
	@Column("NAME")
	// 模板名称
	private String certtemplateName;

	@Column("CONTENT")
	// 模板内容
	private String certtemplateContent;

	@Column("STATUS")
	// 模板状态 0：正常；1：作废
	private String certtemplateStatus;

	@Column("ISUSED")
	// 是否被使用 // 0：未使用；1：使用
	private String isused;

	// 是否发布证书 0：不发布；1：发布
	private String ispublishCert;

	// 是否发布CRL 0：不发布；1：发布
	private String ispublishCrl;

	// 是否允许一个DN有多张证书
	private String permitSameDN;

	// 下载证书时 是采用 renew 还是 rekey
	private String keyPolicy;

	@Column("ISSYS")
	// 是否是系统内置模板 0：是；1不是
	private String isSysTemplate;
	
	@Column("ENCTMP")
	private String encTmpName;	//加密模板名
	
	
	private CertTemplate encTemplate;	//加密模板对象

	// 证书版本
	private int certificateVersion = 3;
	// 有效期
	private int validitylimit = 3600;
	
	//有效期长度
	private String validityUnit = ValidityUnit_Day;
	
	// 
	private String keyGeneratPlace = "CLIENT";

	// 签名算法
	private String signAlg;

	// 公钥长度限制
	private List<Keypolicy> pubKeyPolicy = new ArrayList<Keypolicy>();

	private String specialType;

	// 是否 考虑p10扩展
	private boolean userExtensions;
	
	private boolean checkCsrSubject = false;
	
	//true--ca证书的模板，false--普通证书的模板。如果调用DOWNCERTProcessor的时候，总是返回带ca证书的p7b
	private boolean isCATemplate = false;

	//true--ocsp server证书的模板
	private boolean isOcspTemplate = false;
	
	// 服务端扩展
	private List<Extension> serverExtensions = new ArrayList<Extension>();
	// client端 扩展
	private List<Extension> clientExtensions = new ArrayList<Extension>();

	private ArrayList<Extension> standardExtensions = new ArrayList<Extension>();
	private ArrayList<Extension> otherExtensions = new ArrayList<Extension>();
	private ArrayList<Extension> customerExtensions = new ArrayList<Extension>();

	public CertTemplate() {

	}

	/**
	 * @方法名称: CertTemplate
	 * @描述: 构造函数
	 * @参数：@param certtemplateName 模板名称
	 * @参数：@param certtemplateContent 模板内容
	 * @参数：@param certtemplateStatus 模板状态
	 * @参数：@param isused 是否被使用
	 * @参数：@param isSysTemplate 是否是系统内置模板
	 * @参数：@throws TemplateException
	 */
	public CertTemplate(CertTemplateEntry ce) throws TemplateException {
		this.certtemplateName = ce.getCerttemplateName();
		this.certtemplateContent = ce.getCerttemplateContent();
		this.certtemplateStatus = ce.getCerttemplateStatus();
		this.isused = ce.getIsused();
		this.isSysTemplate = ce.getIsSysTemplate();
		this.encTmpName = ce.getEncTmpName();
		CertTemplateParser.parser(certtemplateName, certtemplateContent, this);
	}

	/**
	 * @方法名称: CertTemplate
	 * @描述: 构造函数，用于CA初始化时，由于不知道CA是 SM2还是RSA的，所以在传递的SML字符串需要修改其中的算法和密钥长度
	 * 只用于单证模板
	 * @参数：@param certtemplateName 模板名称
	 * @参数：@param certtemplateContent 模板内容
	 * @参数：@param certtemplateStatus 模板状态
	 * @参数：@param isused 是否被使用
	 * @参数：@param isSysTemplate 是否是系统内置模板
	 * @参数：@param algorithmidentifier 签名算法
	 * @参数：@param minkeylength 最小密钥长度
	 * @参数：@throws TemplateException
	 */
	public CertTemplate(String certtemplateName, String certtemplateContent, String certtemplateStatus, String isused, String isSysTemplate,
		String signAlg) throws TemplateException {
		String certtemplateContentNew = CertTemplateParser.UpdateCerttemplateContentXML(certtemplateContent, signAlg);
		this.certtemplateName = certtemplateName;
		this.certtemplateContent = certtemplateContentNew;
		this.certtemplateStatus = certtemplateStatus;
		this.isused = isused;
		this.isSysTemplate = isSysTemplate;
		CertTemplateParser.parser(certtemplateName, certtemplateContentNew, this);
	}

	/**
	 * @方法名称：getExtensionXML
	 * @描述： 解析 扩展，返回 xml字符串
	 * @作者： 任龙
	 * @创建日期： 2012-4-18 下午04:41:41
	 * @参数：@param 扩展 set
	 * @参数：@param 扩展类型 ：standardextensions、otherextensions、customextensions
	 * @参数：@return
	 */
	public static String getExtensionXML(Set<Extension> set, String extensionsType) {
		StringBuffer s = new StringBuffer(1000);
		s.append("<" + extensionsType + ">");
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Extension ext = (Extension) it.next();
			s.append("<extension");
			s.append(" OID=\"").append(ext.getOID()).append("\" iscritical= \"").append(ext.isIscritical());
			if (ext.getDatasource().toUpperCase().equals("CLIENT")) {
				s.append("\" datasource= \"").append(ext.getDatasource());
				s.append("\" ISMUST= \"").append(ext.isIsmust());
			}
			s.append(" >");
			Iterator itr = ext.getExtensionEntrys().iterator();
			while (itr.hasNext()) {
				ExtensionEntry entry = (ExtensionEntry) itr.next();
				s.append("<entry");
				s.append(" name=\"").append(entry.getName()).append("\" value= \"").append(entry.getValue());
				if (entry.getOid().equals("2.5.29.31")) {
					s.append(" type=\"").append(entry.getType());
					s.append(" appendbasedn=\"").append(entry.getAppendbasedn());
				}
				s.append(" ></entry>");
			}
			s.append("</extension>");
		}
		s.append("</" + extensionsType + ">");
		return s.toString();
	}

	/**
	 * 组织这个特殊模板所需要的扩展
	 * 
	 * @param subject
	 *            用户证书的主题
	 * @param custExts
	 *            申请证书时存储在数据库里的扩展值
	 * @return
	 */
	public Hashtable<DERObjectIdentifier, X509Extension> specialExts(X509Name subject, Map<String, CustomExt> custExts) {

		Hashtable<DERObjectIdentifier, X509Extension> exts = new Hashtable<DERObjectIdentifier, X509Extension>();
		if (getSpecialType().equalsIgnoreCase("ms_domainController")) {
			ASN1EncodableVector vv = new ASN1EncodableVector();

			// 从主题获得cn部分，作为主题别名
			String cn = (String) subject.getValues(X509Name.CN).get(0);
			log.debugLog("prime name: " + cn);
			GeneralName dns = new GeneralName(GeneralName.dNSName, cn);
			vv.add(dns);

			// guid作为othername放到主题别名
			CustomExt ext = custExts.get(X509Extensions.SubjectAlternativeName.getId());

			for (CustomExtValue ev : ext.getExtValues()) {
				if (ev.getType().equalsIgnoreCase("DOMAINCONTROLLER.GUID")) {
					String guidStr = ev.getValue().replaceAll("-", "");
					log.debugLog("Guid: " + guidStr);
					byte[] guid = new byte[16];
					for (int i = 0; i < 16; i++) {
						String p = guidStr.substring(i * 2, i * 2 + 2);
						int it = Integer.parseInt(p, 16);
						guid[i] = (byte) it;
					}
					DERObjectIdentifier doid = new DERObjectIdentifier("1.3.6.1.4.1.311.25.1");

					DEROctetString id = new DEROctetString(guid);
					ASN1EncodableVector v = new ASN1EncodableVector();
					v.add(doid);
					v.add(new DERTaggedObject(0, id));
					DERSequence seq = new DERSequence(v);
					GeneralName gid = new GeneralName(GeneralName.otherName, seq);

					vv.add(gid);
					break;
				}
			}
			GeneralNames gn = new GeneralNames(new DERSequence(vv));
			X509Extension subjectAltName = new X509Extension(false, new DEROctetString(gn));

			exts.put(X509Extensions.SubjectAlternativeName, subjectAltName);

			// dc 模板
			log.debugLog("Add MSCertTemplate to certificate");
			DERBMPString template = new DERBMPString("DomainController");
			X509Extension tmpExt = new X509Extension(false, new DEROctetString(template));

			exts.put(new DERObjectIdentifier("1.3.6.1.4.1.311.20.2"), tmpExt);

			// 密钥用法
			DEREncodable kuDer = X509ExtensionGenerator.genKeyUsage(X509KeyUsage.digitalSignature | X509KeyUsage.keyEncipherment);
			X509Extension ku = new X509Extension(false, new DEROctetString(kuDer));
			exts.put(X509Extensions.KeyUsage, ku);

			// 扩展密钥用法
			DERObjectIdentifier[] ekuOid = new DERObjectIdentifier[] { KeyPurposeId.id_kp_serverAuth, KeyPurposeId.id_kp_clientAuth };
			DEREncodable derObject = X509ExtensionGenerator.genExtendedKeyUsage(ekuOid);
			X509Extension eku = new X509Extension(false, new DEROctetString(derObject));
			exts.put(X509Extensions.ExtendedKeyUsage, eku);
		} else if (getSpecialType().equalsIgnoreCase("ms_smartCard")) {
			// 取SMARTCARDLOGON.UPN，打主题别名扩展
			CustomExt ext = custExts.get(X509Extensions.SubjectAlternativeName.getId());

			for (CustomExtValue ev : ext.getExtValues()) {
				if (ev.getType().equalsIgnoreCase("SMARTCARDLOGON.UPN")) {
					log.debugLog("SMARTCARDLOGON.UPN: " + ev.getValue());
					DERObjectIdentifier doid = new DERObjectIdentifier("1.3.6.1.4.1.311.20.2.3");

					DERUTF8String upna = new DERUTF8String(ev.getValue());
					ASN1EncodableVector v = new ASN1EncodableVector();
					v.add(doid);
					v.add(new DERTaggedObject(0, upna));
					DERSequence seq = new DERSequence(v);
					GeneralName upn = new GeneralName(GeneralName.otherName, seq);

					DERSequence seqSub = new DERSequence(upn);
					GeneralNames gn = new GeneralNames(seqSub);
					X509Extension subjectAltName = new X509Extension(false, new DEROctetString(gn));
					exts.put(X509Extensions.SubjectAlternativeName, subjectAltName);

					break;
				}
			}

			// smartlogon 模板
			log.debugLog("Add MSCertTemplate to certificate");
			DERBMPString template = new DERBMPString("SmartcardLogon");
			X509Extension tmpExt = new X509Extension(false, new DEROctetString(template));
			exts.put(new DERObjectIdentifier("1.3.6.1.4.1.311.20.2"), tmpExt);

			// 密钥用法
			DEREncodable kuDer = X509ExtensionGenerator.genKeyUsage(X509KeyUsage.digitalSignature | X509KeyUsage.keyEncipherment);
			X509Extension ku = new X509Extension(false, new DEROctetString(kuDer));
			exts.put(X509Extensions.KeyUsage, ku);

			// 扩展密钥用法
			DERObjectIdentifier[] ekuOid = new DERObjectIdentifier[] { KeyPurposeId.id_kp_smartcardlogon, KeyPurposeId.id_kp_clientAuth };
			DEREncodable derObject = X509ExtensionGenerator.genExtendedKeyUsage(ekuOid);
			X509Extension eku = new X509Extension(false, new DEROctetString(derObject));
			exts.put(X509Extensions.ExtendedKeyUsage, eku);
		} else if (getSpecialType().equalsIgnoreCase("ocsp")) {
			X509Extension ext = new X509Extension(false, null);
			exts.put(OCSPObjectIdentifiers.id_pkix_ocsp_nocheck, ext);
		} else if (getSpecialType().equalsIgnoreCase("scep")) {
			// scep特定扩展密钥用法
			DEREncodable der = X509ExtensionGenerator.genExtendedKeyUsage(new DERObjectIdentifier[] { new DERObjectIdentifier("1.3.6.1.5.5.8.2.2") });
			X509Extension ext = new X509Extension(false, new DEROctetString(der));
			exts.put(X509Extensions.ExtendedKeyUsage, ext);

			// 如果dn有特殊项，则加入主题备用名
			if (subject.getOIDs().contains(X509Name.UnstructuredAddress) || subject.getOIDs().contains(X509Name.UnstructuredName)) {
				ASN1EncodableVector vv = new ASN1EncodableVector();
				// 如何包含UnstructuredAddress，作为主题备用名扩展的ip
				if (subject.getOIDs().contains(X509Name.UnstructuredAddress)) {
					Vector v = subject.getValues(X509Name.UnstructuredAddress);
					String ip = (String) v.get(0);
					GeneralName _ip = new GeneralName(GeneralName.iPAddress, ip);
					vv.add(_ip);
				}
				// 如果包含UnstructuredName，作为主题备用名的dnsName
				if (subject.getOIDs().contains(X509Name.UnstructuredName)) {
					Vector v = subject.getValues(X509Name.UnstructuredName);
					String dns = (String) v.get(0);
					GeneralName _dns = new GeneralName(GeneralName.dNSName, dns);
					vv.add(_dns);
				}
				DERSequence seqSub = new DERSequence(vv);
				GeneralNames gn = new GeneralNames(seqSub);
				X509Extension subjectAltName = new X509Extension(false, new DEROctetString(gn));

				exts.put(X509Extensions.SubjectAlternativeName, subjectAltName);
			}
		}
		return exts;
	}

	public String getCerttemplateName() {
		return certtemplateName;
	}

	public void setCerttemplateName(String certtemplateName) {
		this.certtemplateName = certtemplateName;
	}

	public String getCerttemplateContent() {
		return this.toString();
//		return certtemplateContent;
	}

	public void setCerttemplateContent(String certtemplateContent) {
		this.certtemplateContent = certtemplateContent;
	}

	public String getKeyPolicy() {
		return keyPolicy;
	}

	public void setKeyPolicy(String keyPolicy) {
		this.keyPolicy = keyPolicy;
	}

	public int getCertificateVersion() {
		return certificateVersion;
	}

	public void setCertificateVersion(int certificateVersion) {
		this.certificateVersion = certificateVersion;
	}

	public int getValiditylimit() {
		return validitylimit;
	}

	public void setValiditylimit(int validitylimit) {
		this.validitylimit = validitylimit;
	}

	public List<Extension> getServerExtensions() {
		return serverExtensions;
	}

	public List<Extension> getClientExtensions() {
		// 如果是微软域登录证书，添加用户名和guid的扩展oid，让客户端送值
		Extension item = null;
		for (Extension e : clientExtensions) {
			if (e.getOID().equals(X509Extensions.SubjectAlternativeName.getId())) {
				item = e;
				break;
			}
		}
		if (item == null) {
			item = new Extension();
			item.setOID(X509Extensions.SubjectAlternativeName.getId());
			item.setIsmust(true);
		}

		if (getSpecialType().equalsIgnoreCase("ms_smartCard")) {
			boolean has = false;
			for (ExtensionEntry entry : item.getExtensionEntrys()) {
				if (entry.getName().equalsIgnoreCase("SMARTCARDLOGON.UPN")) {
					has = true;
					break;
				}
			}
			if (!has) {
				ExtensionEntry en = new ExtensionEntry();
				en.setName("SMARTCARDLOGON.UPN");
				item.getExtensionEntrys().add(en);
				clientExtensions.add(item);
			}
		}

		if (getSpecialType().equalsIgnoreCase("ms_domainController")) {
			boolean has = false;
			for (ExtensionEntry entry : item.getExtensionEntrys()) {
				if (entry.getName().equalsIgnoreCase("DOMAINCONTROLLER.GUID")) {
					has = true;
					break;
				}
			}
			if (!has) {
				ExtensionEntry en = new ExtensionEntry();
				en.setName("DOMAINCONTROLLER.GUID");
				item.getExtensionEntrys().add(en);
				clientExtensions.add(item);
			}
			
		}
		return clientExtensions;
	}

	public String getSpecialType() {
		return specialType;
	}

	public void setSpecialType(String specialType) {
		this.specialType = specialType;
	}

	public String getKeyGeneratPlace() {
		return keyGeneratPlace;
	}

	public String getSignAlg() {
		return signAlg;
	}

	public void setSignAlg(String signAlg) {
		this.signAlg = signAlg;
	}

	public void setKeyGeneratPlace(String keyGeneratPlace) {
		this.keyGeneratPlace = keyGeneratPlace;
	}

	public List<Keypolicy> getPubKeyPolicy() {
		return pubKeyPolicy;
	}

	public void setPubKeyPolicy(List<Keypolicy> pubKeyPolicy) {
		this.pubKeyPolicy = pubKeyPolicy;
	}

	public boolean isUserExtensions() {
		return userExtensions;
	}

	public void setUserExtensions(boolean userExtensions) {
		this.userExtensions = userExtensions;
	}

	public void setServerExtensions(List<Extension> serverExtensions) {
		this.serverExtensions = serverExtensions;
	}

	public void setClientExtensions(List<Extension> clientExtensions) {
		this.clientExtensions = clientExtensions;
	}

	public String getCerttemplateStatus() {
		return certtemplateStatus;
	}

	public void setCerttemplateStatus(String certtemplateStatus) {
		this.certtemplateStatus = certtemplateStatus;
	}

	public String getIsused() {
		return isused;
	}

	public void setIsused(String isused) {
		this.isused = isused;
	}

	public String getIspublishCert() {
		return ispublishCert;
	}

	public void setIspublishCert(String ispublishCert) {
		this.ispublishCert = ispublishCert;
	}

	public String getIspublishCrl() {
		return ispublishCrl;
	}

	public void setIspublishCrl(String ispublishCrl) {
		this.ispublishCrl = ispublishCrl;
	}

	public String getPermitSameDN() {
		return permitSameDN;
	}

	public void setPermitSameDN(String permitSameDN) {
		this.permitSameDN = permitSameDN;
	}

	public String getIsSysTemplate() {
		return isSysTemplate;
	}

	public void setIsSysTemplate(String isSysTemplate) {
		this.isSysTemplate = isSysTemplate;
	}

	public boolean isIsused() {
		return isused.equals(CertTemplate.CERTTEMPLATE_STATUS_USED);
	}

	public ArrayList<Extension> getStandardExtensions() {
		return standardExtensions;
	}

	public void setStandardExtensions(ArrayList<Extension> standardExtensions) {
		this.standardExtensions = standardExtensions;
	}

	public ArrayList<Extension> getOtherExtensions() {
		return otherExtensions;
	}

	public void setOtherExtensions(ArrayList<Extension> otherExtensions) {
		this.otherExtensions = otherExtensions;
	}

	public ArrayList<Extension> getCustomerExtensions() {
		return customerExtensions;
	}

	public void setCustomerExtensions(ArrayList<Extension> customerExtensions) {
		this.customerExtensions = customerExtensions;
	}
	
	public boolean isCheckCsrSubject() {
		return checkCsrSubject;
	}

	public void setCheckCsrSubject(boolean checkCsrSubject) {
		this.checkCsrSubject = checkCsrSubject;
	}

	public String toString() {
		StringBuffer s = new StringBuffer(3000);
		s.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>");
		s.append("<certtemplate specialType = \"" + specialType + "\">");
		s.append(getBaseToString());
		s.append(getExtensions());
		s.append(getCertpolicy());
		s.append(getPublish());
		s.append("</certtemplate>");
		return s.toString();
	}

	public String getPublish() {
		StringBuffer s = new StringBuffer();
		s.append("<publish>");
		s.append("<iscert>");
		if(ispublishCert.equals(CertTemplate.CERTTEMPLATE_PUBLISH_CERT)){
			s.append("TRUE");
		}else{
			s.append("FALSE");
		}
		s.append("</iscert>");
		s.append("<iscrl>");
		if(ispublishCrl.equals(CertTemplate.CERTTEMPLATE_CRL)){
			s.append("TRUE");
		}else{
			s.append("FALSE");
		}		
		s.append("</iscrl>");
		s.append("</publish>");
		return s.toString();
	}

	public String getCertpolicy() {
		StringBuffer s = new StringBuffer();
		s.append("<certpolicy>");
		
		s.append("<checkCsrSubject>");
		s.append(checkCsrSubject);
		s.append("</checkCsrSubject>");
		
		s.append("<permitsamedn>");
		if(permitSameDN.equalsIgnoreCase(CertTemplate.Multi_DN_Allow)){
			s.append("TRUE");
		}else{
			s.append("FALSE");
		}
		s.append("</permitsamedn>");
		s.append("<keyPolicy>");
		if(keyPolicy.equalsIgnoreCase(CertTemplate.ReKey)){
			s.append("rekey");
		}else{
			s.append("renew");
		}
		s.append("</keyPolicy>");
		s.append("</certpolicy>");
		return s.toString();
	}

	public String getStandard_Extensions() {
		StringBuffer s = new StringBuffer();
		s.append("<standardextensions>");
		for (int i = 0; standardExtensions != null && i < standardExtensions.size(); i++) {
			s.append("<extension");
			Extension ex = standardExtensions.get(i);
			if (ex.getOID() != null) {
				s.append(" OID=\"" + ex.getOID() + "\"");
			}
			s.append(" iscritical=\"" + ex.isIscritical() + "\"");
			if (ex.getDatasource() != null) {
				s.append(" datasource=\"" + ex.getDatasource() + "\"");
			}
			s.append(" ISMUST=\"" + ex.isIsmust() + "\"");
			s.append(">");
			Iterator it = ex.getExtensionEntrys().iterator();
			while (it.hasNext()) {
				ExtensionEntry entry = (ExtensionEntry) it.next();
				s.append("<entry");
				if (entry.getOid() != null) {
					s.append(" oid=\"" + entry.getOid() + "\"");
				}
				if (entry.getName() != null) {
					s.append(" name=\"" + entry.getName() + "\"");
				}
				if (entry.getAppendbasedn() != null) {
					s.append(" appendbasedn=\"" + entry.getAppendbasedn() + "\"");
				}
				if (entry.getEncoding() != null) {
					s.append(" encoding=\"" + entry.getEncoding() + "\"");
				}
				if (entry.getType() != null) {
					s.append(" type=\"" + entry.getType() + "\"");
				}
				s.append(">");
				if (entry.getName() != null && entry.getName().equalsIgnoreCase("ISCA")) {
					s.append(entry.getValue());
				} else {
					if (entry.getValue() != null && !entry.getValue().equals("")) {
						s.append("<![CDATA[" + entry.getValue() + "]]>");
					}
				}
				s.append("</entry>");
			}
			s.append("</extension>");
		}
		s.append("</standardextensions>");
		return s.toString();
	}

	public String getOther_Extensions() {
		StringBuffer s = new StringBuffer();
		s.append("<otherextensions>");
		for (int i = 0; otherExtensions != null && i < otherExtensions.size(); i++) {
			s.append("<extension");
			Extension ex = otherExtensions.get(i);
			if (ex.getOID() != null) {
				s.append(" OID=\"" + ex.getOID() + "\"");
			}
			s.append(" iscritical=\"" + ex.isIscritical() + "\"");
			if (ex.getDatasource() != null) {
				s.append(" datasource=\"" + ex.getDatasource() + "\"");
			}
			s.append(" ISMUST=\"" + ex.isIsmust() + "\"");
			s.append(">");
			Iterator it = ex.getExtensionEntrys().iterator();
			while (it.hasNext()) {
				ExtensionEntry entry = (ExtensionEntry) it.next();
				s.append("<entry");
				if (entry.getOid() != null) {
					s.append(" oid=\"" + entry.getOid() + "\"");
				}
				if (entry.getName() != null) {
					s.append(" name=\"" + entry.getName() + "\"");
				}
				if (entry.getAppendbasedn() != null) {
					s.append(" appendbasedn=\"" + entry.getAppendbasedn() + "\"");
				}
				if (entry.getEncoding() != null) {
					s.append(" encoding=\"" + entry.getEncoding() + "\"");
				}
				if (entry.getType() != null) {
					s.append(" type=\"" + entry.getType() + "\"");
				}
				s.append(">");
				if (entry.getValue() != null && !entry.getValue().equals("")) {
					s.append("<![CDATA[" + entry.getValue() + "]]>");
				}
				s.append("</entry>");
			}
			s.append("</extension>");
		}
		s.append("</otherextensions>");
		return s.toString();
	}

	public String getCustomer_Extensions() {
		StringBuffer s = new StringBuffer();
		s.append("<customextensions>");
		for (int i = 0; customerExtensions != null && i < customerExtensions.size(); i++) {
			s.append("<extension");
			Extension ex = customerExtensions.get(i);
			if (ex.getOID() != null) {
				s.append(" OID=\"" + ex.getOID() + "\"");
			}
			s.append(" iscritical=\"" + ex.isIscritical() + "\"");
			if (ex.getDatasource() != null) {
				s.append(" datasource=\"" + ex.getDatasource() + "\"");
			}
			s.append(" ISMUST=\"" + ex.isIsmust() + "\"");
			s.append(">");
			Iterator it = ex.getExtensionEntrys().iterator();
			while (it.hasNext()) {
				ExtensionEntry entry = (ExtensionEntry) it.next();
				s.append("<entry");
				if (entry.getOid() != null) {
					s.append(" oid=\"" + entry.getOid() + "\"");
				}
				if (entry.getName() != null) {
					s.append(" name=\"" + entry.getName() + "\"");
				}
				if (entry.getAppendbasedn() != null) {
					s.append(" appendbasedn=\"" + entry.getAppendbasedn() + "\"");
				}
				if (entry.getEncoding() != null) {
					s.append(" encoding=\"" + entry.getEncoding() + "\"");
				}
				if (entry.getType() != null) {
					s.append(" type=\"" + entry.getType() + "\"");
				}
				s.append(">");
				if (entry.getValue() != null && !entry.getValue().equals("")) {
					s.append("<![CDATA[" + entry.getValue() + "]]>");
				}
				s.append("</entry>");
			}
			s.append("</extension>");
		}
		s.append("</customextensions>");
		return s.toString();
	}

	public String getExtensions() {
		StringBuffer s = new StringBuffer();
		s.append("<extensions>");
		s.append(getStandard_Extensions());
		s.append(getOther_Extensions());
		s.append(getCustomer_Extensions());
		s.append("</extensions>");
		return s.toString();
	}

	public String getBaseToString() {
		StringBuffer s = new StringBuffer();
		s.append("<base>");
		s.append("<certificateversion>");
		s.append(certificateVersion);
		s.append("</certificateversion>");
		s.append("<validitylimit ").append("scale=\"").append(getValidityUnit()).append("\">");
		s.append(validitylimit);
		s.append("</validitylimit>");
		s.append("<keygeneratplace>");
		s.append(keyGeneratPlace);
		s.append("</keygeneratplace>");
		s.append("<pubkeykeypolicy>");
		for (int i = 0; pubKeyPolicy != null && i < pubKeyPolicy.size(); i++) {
			Keypolicy policy = pubKeyPolicy.get(i);
			s.append("<allow algorithmidentifier=\"" + policy.getAlgorithmidentifier() + "\" minkeylength=\"" + policy.getMinkeylength() + "\" />");
		}
		s.append("</pubkeykeypolicy>");
		s.append("<signalg>");
		s.append(signAlg);
		s.append("</signalg>");
		s.append("</base>");
		return s.toString();
	}

	public String getEncTmpName() {
		return encTmpName;
	}

	public void setEncTmpName(String encTmpName) {
		this.encTmpName = encTmpName;
	}

	public CertTemplate getEncTemplate() {
		return encTemplate;
	}

	public void setEncTemplate(CertTemplate encTemplate) {
		this.encTemplate = encTemplate;
	}

	public boolean isCATemplate() {
		return isCATemplate;
	}

	public void setCATemplate(boolean isCATemplate) {
		this.isCATemplate = isCATemplate;
	}

	public String getValidityUnit() {
		return (validityUnit==null || validityUnit.length()==0)? ValidityUnit_Day : validityUnit;
	}

	public void setValidityUnit(String validityUnit) {
		this.validityUnit = validityUnit;
	}

	public boolean isOcspTemplate() {
		return isOcspTemplate;
	}

	public void setOcspTemplate(boolean isOcspTemplate) {
		this.isOcspTemplate = isOcspTemplate;
	}
	// jy
	public boolean isAdd() {
		return isAdd;
	}
	// jy
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	// jy
	public boolean isSignal() {
		return isSignal;
	}
	// jy
	public void setSignal(boolean isSignal) {
		this.isSignal = isSignal;
	}
}
