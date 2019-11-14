package cn.com.infosec.netcert.caAdmin.ui.template.templateUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.com.infosec.asn1.x509.X509Extensions;
import cn.com.infosec.netcert.framework.log.FileLogger;

/**
 * @项目名称：CAServer
 * @类名： CertTemplateParser.java
 * @描述： TODO
 * @作者： 任龙
 * @创建日期： 2012-4-18 下午08:08:08
 * @版本： V1.0
 */

public class CertTemplateParser {
	private static FileLogger log = FileLogger.getLogger(CertTemplateParser.class);

	
	/**
	 * @方法名称：parser
	 * @描述： 解析模板 XML文件，将解析出来的内容，赋给 传进来的模板对象
	 * @作者： 任龙
	 * @创建日期： 2012-4-18 下午08:08:13
	 * @参数：@param certTemplatename
	 * @参数：@param xmlInfo
	 * @参数：@param certTemplate
	 */
	public static void parser(String certTemplatename, String xmlInfo, CertTemplate certTemplate) throws TemplateException {
		if (xmlInfo == null) {
			throw new TemplateException("The Parameter xmlInfo can not be null");
		}
		try {
			SAXReader saxReader = new SAXReader();
			Document doc = null;
			try {
				 InputStreamReader utf8In=new InputStreamReader(new ByteArrayInputStream(xmlInfo.getBytes())); 
//				 InputStreamReader utf8In=new InputStreamReader(new ByteArrayInputStream(xmlInfo.getBytes()),"gb2312"); 
				 
//				 doc = DocumentHelper.parseText
				doc = saxReader.read(utf8In);
			} catch (Exception e1) {
				throw new TemplateException(e1, " The Certemplate is invalid：");
			}
			Element root = doc.getRootElement();
			certTemplate.setSpecialType(root.attribute("specialType").getText());
			Element eBase = root.element("base");
			if (eBase == null) {
				throw new TemplateException("Not found base tag");
			} else {
				certTemplate = BaseParser(eBase, certTemplate);
			}

			Element eExtensions = root.element("extensions");
			if (eExtensions == null) {
				throw new TemplateException("Not found base extensions");
			} else {
				HashMap<String, ArrayList<Extension>> map = getExtensionParser(eExtensions);
				certTemplate.setServerExtensions(map.get("CA"));
				certTemplate.setClientExtensions(map.get("CLIENT"));
				certTemplate.setStandardExtensions(map.get("standardExtensions"));
				
				//设置是否是ca模板
				for(Extension ext : certTemplate.getStandardExtensions()){
					if(ext.getOID().equals(X509Extensions.BasicConstraints.getId())){
						for(ExtensionEntry ee : ext.getExtensionEntrys()){
							if(ee.getName().equalsIgnoreCase("isca") && "true".equalsIgnoreCase(ee.getValue())){
								certTemplate.setCATemplate(true);
								break;
							}
						}
					}
				}
				
				//设置是否是ocsp server模板
				for(Extension ext : certTemplate.getStandardExtensions()){
					if(ext.getOID().equals(X509Extensions.ExtendedKeyUsage.getId())){
						for(ExtensionEntry ee : ext.getExtensionEntrys()){
							if(ee.getOid().equals("1.3.6.1.5.5.7.3.9")){
								certTemplate.setOcspTemplate(true);
								break;
							}
						}
					}
				}
				
				
				certTemplate.setOtherExtensions(map.get("otherExtensions"));
				certTemplate.setCustomerExtensions(map.get("customerExtensions"));
				Element userextensions = eExtensions.element("userextensions");
				if (userextensions != null) {
					certTemplate.setUserExtensions(getBooleanValue(userextensions.getTextTrim()));
				} else {
					certTemplate.setUserExtensions(false);
				}
			}
			Element eCertpolicy = root.element("certpolicy");
			if (eCertpolicy == null) {
				throw new TemplateException("Not found certpolicy tag");
			} else {
				
				if (eCertpolicy.element("checkCsrSubject") != null) {
					if (eCertpolicy.element("checkCsrSubject").getTextTrim().equalsIgnoreCase("true")) {
						certTemplate.setCheckCsrSubject(true);
					} else {
						certTemplate.setCheckCsrSubject(false);
					}
				} 
				
				if (eCertpolicy.element("permitsamedn") != null) {
					if (eCertpolicy.element("permitsamedn").getTextTrim().equalsIgnoreCase("true")) {
						certTemplate.setPermitSameDN(CertTemplate.Multi_DN_Allow);
					} else {
						certTemplate.setPermitSameDN(CertTemplate.Multi_DN_NOT_Allow);
					}
				} else {
					throw new TemplateException("The permitsamedn is null point of the certpolicy Element ");
				}

				if (eCertpolicy.element("keyPolicy") != null) {
					if (eCertpolicy.element("keyPolicy").getTextTrim().equalsIgnoreCase("rekey")) {
						certTemplate.setKeyPolicy(CertTemplate.ReKey);
					} else {
						certTemplate.setKeyPolicy(CertTemplate.ReNew);
					}
				} else {
					throw new TemplateException("The keyPolicy is null point of the certpolicy Element ");
				}
			}
			Element ePublish = root.element("publish");
			if (ePublish == null) {
				throw new TemplateException("Not found publish tag");
			} else {
				if (ePublish.element("iscert") != null) {
					if (ePublish.element("iscert").getTextTrim().equalsIgnoreCase("true")) {
						certTemplate.setIspublishCert(CertTemplate.CERTTEMPLATE_PUBLISH_CERT);
					} else {
						certTemplate.setIspublishCert(CertTemplate.CERTTEMPLATE_NOT_CERT);
					}
				} else {
					throw new TemplateException("The iscert is null point of the publish Element ");
				}
				if (ePublish.element("iscrl") != null) {
					if (ePublish.element("iscrl").getTextTrim().equalsIgnoreCase("true")) {
						certTemplate.setIspublishCrl(CertTemplate.CERTTEMPLATE_CRL);
					} else {
						certTemplate.setIspublishCrl(CertTemplate.CERTTEMPLATE_NOT_CRL);
					}
				} else {
					throw new TemplateException("The iscrl is null point of the publish Element ");
				}
			}

		} catch (Exception ee) {
			throw new TemplateException(ee, " The Certemplate is invalid：");
		}
	}

	/**
	 * @方法名称：getBooleanValue
	 * @描述： 将 字符串 转换成 布尔值
	 * @作者： 任龙
	 * @创建日期： 2012-4-24 上午10:03:01
	 * @参数：@param str
	 * @参数：@return
	 * @参数：@throws TemplateException
	 */
	public static boolean getBooleanValue(String str) throws TemplateException {
		if (str.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @方法名称：getIntformString
	 * @描述： 将字符串转换整形
	 * @作者： 任龙
	 * @创建日期： 2012-4-26 上午09:40:08
	 * @参数：@param str
	 * @参数：@return
	 * @参数：@throws TemplateException
	 */
	public static int getIntformString(String str) throws TemplateException {
		if (str == null) {
			throw new TemplateException(" the param is null in getIntformString() ");
		}
		return Integer.parseInt(str);
	}

	/**
	 * @方法名称：BaseParser
	 * @描述： 解析标准扩展信息，所有标准扩展中信息均为必填项，每项验证为空时，均抛出异常信息
	 * @作者： 任龙
	 * @创建日期： 2012-4-19 下午05:47:07
	 * @参数：@param eBase
	 * @参数：@return
	 */
	public static CertTemplate BaseParser(Element eBase, CertTemplate certTemplate) throws TemplateException {
		HashMap<String, String> map = new HashMap<String, String>();
		if (eBase.element("certificateversion") != null) {
			certTemplate.setCertificateVersion(getIntformString(eBase.element("certificateversion").getTextTrim()));
		} else {
			throw new TemplateException("The certificateversion is null point of the Base Element ");
		}
		if (eBase.element("validitylimit") != null) {
			certTemplate.setValiditylimit(getIntformString(eBase.element("validitylimit").getTextTrim()));
			Attribute attr = eBase.element("validitylimit").attribute("scale");
			if(attr!=null){		//默认是‘天’
				String unit = attr.getText();
				if(CertTemplate.ValidityUnit_Year.equalsIgnoreCase(unit))
					certTemplate.setValidityUnit(CertTemplate.ValidityUnit_Year);
				else if(CertTemplate.ValidityUnit_Month.equalsIgnoreCase(unit))
					certTemplate.setValidityUnit(CertTemplate.ValidityUnit_Month);
			}
		} else {
			throw new TemplateException("The validitylimit is null point of the Base Element ");
		}
		if (eBase.element("keygeneratplace") != null) {
			certTemplate.setKeyGeneratPlace(eBase.element("keygeneratplace").getTextTrim());
		} else {
			throw new TemplateException("The keygeneratplace is null point of the Base Element ");
		}
		Element pubkeykeypolicy = eBase.element("pubkeykeypolicy");
		if (pubkeykeypolicy != null) {
			List keykeypolicys = pubkeykeypolicy.elements();
			if (keykeypolicys != null) {
				Iterator itKeykeypolicys = keykeypolicys.iterator();
				List<Keypolicy> pubkeykeypolicys = new ArrayList<Keypolicy>();
				while (itKeykeypolicys.hasNext()) {
					Element policy = (Element) itKeykeypolicys.next();
					Keypolicy keypolicy = new Keypolicy();
					if (policy.attribute("algorithmidentifier") != null) {
						keypolicy.setAlgorithmidentifier(policy.attribute("algorithmidentifier").getValue());
					} else {
						throw new TemplateException("The algorithmidentifier is null point of the Base Element ");
					}
					if (policy.attribute("minkeylength") != null) {
						keypolicy.setMinkeylength(policy.attribute("minkeylength").getValue());
					} else {
						throw new TemplateException("The minkeylength is null point of the Base Element ");
					}
					pubkeykeypolicys.add(keypolicy);
				}
				certTemplate.setPubKeyPolicy(pubkeykeypolicys);
			}else{
				throw new TemplateException("Keypolicy is null ");
			}

		} else {
			throw new TemplateException("The pubkeykeypolicy is null point of the Base Element ");
		}
		Element signalg = eBase.element("signalg");
		if (signalg != null && !signalg.getTextTrim().equals("")) {
			certTemplate.setSignAlg(signalg.getTextTrim());
		} else {
			throw new TemplateException("The signalg is null point of the Base Element ");
		}
		return certTemplate;
	}

	/**
	 * @方法名称：StandardextensionParser
	 * @描述： 解析标准扩展内容
	 * @作者： 任龙
	 * @创建日期： 2012-4-24 上午10:29:35
	 * @参数：@param eExtensions
	 * @参数：@return
	 * @参数：@throws TemplateException
	 */
	public static HashMap<String, ArrayList<Extension>> StandardextensionParser(Element eExtensions) throws TemplateException {
		HashMap<String, ArrayList<Extension>> map = new HashMap<String, ArrayList<Extension>>();
		ArrayList<Extension> serverList = new ArrayList<Extension>();
		ArrayList<Extension> clientList = new ArrayList<Extension>();
		Element standardextension = eExtensions.element("standardextensions");
		if (standardextension != null) {
			List standardextensionList = standardextension.elements();
			if (standardextensionList != null) {
				Iterator itStandardextensions = standardextensionList.iterator();
				while (itStandardextensions.hasNext()) {
					Extension ex = new Extension();
					Element extension = (Element) itStandardextensions.next();
					if (extension.attribute("OID") != null) {
						ex.setOID(extension.attribute("OID").getText());
					} else {
						throw new TemplateException("The OID is null point of the standardextensions Element ");
					}
					if (extension.attribute("iscritical") != null) {
						ex.setIscritical(getBooleanValue(extension.attribute("iscritical").getText()));
					} else {
						throw new TemplateException("The iscritical is null point of the standardextensions Element ");
					}
					if (extension.attribute("datasource") != null) {
						ex.setDatasource(extension.attribute("datasource").getText());
					} else {
						ex.setDatasource("CA");
					}
					if (extension.attribute("ISMUST") != null) {
						ex.setIsmust(getBooleanValue(extension.attribute("ISMUST").getText()));
					} else {
						ex.setIsmust(false);
					}
					ArrayList<ExtensionEntry> entryList = new ArrayList<ExtensionEntry>();
					List entries = extension.elements("entry");
					Iterator itEntry = entries.iterator();
					while (itEntry.hasNext()) {
						ExtensionEntry en = new ExtensionEntry();
						Element entry = (Element) itEntry.next();
						if (entry.attribute("name") != null) {
							en.setName(entry.attribute("name").getText());
						}
						if (entry.getTextTrim() != null) {
							en.setValue(entry.getTextTrim());
						} else {
							if (entry.attribute("value") != null) {
								en.setValue(entry.attribute("value").getText());
							}
						}
						if (entry.attribute("type") != null) {
							en.setType(entry.attribute("type").getText());
						}
						if (entry.attribute("appendbasedn") != null) {
							en.setAppendbasedn(entry.attribute("appendbasedn").getText());
						}
						if (entry.attribute("oid") != null) {
							en.setOid(entry.attribute("oid").getText());
						}
						if (entry.attribute("oid") != null) {

						}
						entryList.add(en);
					}
					ex.setExtensionEntrys(entryList);
					if (ex.getDatasource().toUpperCase().equals("CLIENT")) {
						clientList.add(ex);
					} else {
						serverList.add(ex);
					}

				}
			}
		}
		map.put("CLIENT", clientList);
		map.put("CA", serverList);
		return map;
	}

	/**
	 * @方法名称：OtherextensionParser
	 * @描述： 解析其他扩展内容
	 * @作者： 任龙
	 * @创建日期： 2012-4-24 上午10:39:59
	 * @参数：@param eExtensions
	 * @参数：@return
	 * @参数：@throws TemplateException
	 */
	public static HashMap<String, ArrayList<Extension>> OtherextensionParser(Element eExtensions) throws TemplateException {
		HashMap<String, ArrayList<Extension>> map = new HashMap<String, ArrayList<Extension>>();
		ArrayList<Extension> serverList = new ArrayList<Extension>();
		ArrayList<Extension> clientList = new ArrayList<Extension>();
		Element otherextension = eExtensions.element("otherextensions");
		if (otherextension != null) {
			List otherextensionList = otherextension.elements();
			if (otherextensionList != null) {
				Iterator itOtherextensions = otherextensionList.iterator();
				while (itOtherextensions.hasNext()) {
					Extension ex = new Extension();
					Element extension = (Element) itOtherextensions.next();
					if (extension.attribute("OID") != null) {
						ex.setOID(extension.attribute("OID").getText());
					} else {
						throw new TemplateException("The OID is null point of the otherextensions Element ");
					}

					if (extension.attribute("iscritical") != null) {
						ex.setIscritical(getBooleanValue(extension.attribute("iscritical").getText()));
					} else {
						throw new TemplateException("The iscritical is null point of the otherextensions Element ");
					}

					if (extension.attribute("datasource") != null) {
						ex.setDatasource(extension.attribute("datasource").getText());
					} else {
						ex.setDatasource("CA");
					}

					if (extension.attribute("ISMUST") != null) {
						ex.setIsmust(getBooleanValue(extension.attribute("ISMUST").getText()));
					} else {
						ex.setIsmust(false);
					}
					if (ex.getDatasource().toUpperCase().equals("CLIENT")) {
						clientList.add(ex);
					} else {
						serverList.add(ex);
					}

				}
			}
		}
		map.put("CLIENT", clientList);
		map.put("CA", serverList);
		return map;
	}

	/**
	 * @方法名称：CustomerextensionParser
	 * @描述： 解析用户自定义扩展内容
	 * @作者： 任龙
	 * @创建日期： 2012-4-24 上午10:47:02
	 * @参数：@param eExtensions
	 * @参数：@return
	 * @参数：@throws TemplateException
	 */
	public static HashMap<String, ArrayList<Extension>> CustomerextensionParser(Element eExtensions) throws TemplateException {
		HashMap<String, ArrayList<Extension>> map = new HashMap<String, ArrayList<Extension>>();
		ArrayList<Extension> serverList = new ArrayList<Extension>();
		ArrayList<Extension> clientList = new ArrayList<Extension>();
		Element customextension = eExtensions.element("customextensions");
		
		if (customextension != null) {
			List customextensionList = customextension.elements();
			if (customextensionList != null) {
				Iterator itCustomerextensions = customextensionList.iterator();
				Vector<String> v = new Vector<String>();
				while (itCustomerextensions.hasNext()) {
					Extension ex = new Extension();
					Element extension = (Element) itCustomerextensions.next();
					if (extension.attribute("OID") != null) {
						if(v.contains(extension.attribute("OID").getText())){
							throw new TemplateException(extension.attribute("OID").getText() + " Repeat error" );
						}else{
							log.debugLog(" the oid = " + extension.attribute("OID").getText());
							for(int i = 0; i<v.size();i++){
								String id = v.get(i);
								log.debugLog(" memry oid = " + id);
							}							
							ex.setOID(extension.attribute("OID").getText());
							v.add(extension.attribute("OID").getText());
						}
						
					} else {
						throw new TemplateException("The OID is null point of the customextensions Element ");
					}
					if (extension.attribute("iscritical") != null) {
						ex.setIscritical(getBooleanValue(extension.attribute("iscritical").getText()));
					} else {
						throw new TemplateException("The iscritical is null point of the customextensions Element ");
					}
					if (extension.attribute("datasource") != null) {
						ex.setDatasource(extension.attribute("datasource").getText());
					} else {
						ex.setDatasource("CA");
					}
					if (extension.attribute("ISMUST") != null) {
						ex.setIsmust(getBooleanValue(extension.attribute("ISMUST").getText()));
					} else {
						ex.setIsmust(false);
					}
					ArrayList<ExtensionEntry> entryList = new ArrayList<ExtensionEntry>();
					List entries = extension.elements("entry");
					Iterator itEntry = entries.iterator();
					while (itEntry.hasNext()) {
						ExtensionEntry en = new ExtensionEntry();
						Element entry = (Element) itEntry.next();
						if (entry.attribute("name") != null) {
							en.setName(entry.attribute("name").getText());
						}
						if (entry.getTextTrim() != null) {
							en.setValue(entry.getTextTrim());
						} else {
							if (entry.attribute("value") != null) {
								en.setValue(entry.attribute("value").getText());
							}
						}
						if (entry.attribute("type") != null) {
							en.setType(entry.attribute("type").getText());
						}
						if (entry.attribute("appendbasedn") != null) {
							en.setAppendbasedn(entry.attribute("appendbasedn").getText());
						}
						if (entry.attribute("oid") != null) {
							en.setOid(entry.attribute("oid").getText());
						}
						if (entry.attribute("encoding") != null) {
							en.setEncoding(entry.attribute("encoding").getText());
						}
						entryList.add(en);
					}
					ex.setExtensionEntrys(entryList);
					if (ex.getDatasource().toUpperCase().equals("CLIENT")) {
						clientList.add(ex);
					} else {
						serverList.add(ex);
					}

				}
			}
		}
		map.put("CLIENT", clientList);
		map.put("CA", serverList);
		return map;
	}

	/**
	 * @方法名称：getExtensionParser
	 * @描述： 解析扩展信息
	 * @作者： 任龙
	 * @创建日期： 2012-4-25 上午10:18:11
	 * @参数：@param eExtensions
	 * @参数：@return
	 * @参数：@throws TemplateException
	 */
	public static HashMap<String, ArrayList<Extension>> getExtensionParser(Element eExtensions) throws TemplateException {
		HashMap<String, ArrayList<Extension>> map = new HashMap<String, ArrayList<Extension>>();
		ArrayList<Extension> serverExtensions = new ArrayList<Extension>();
		ArrayList<Extension> clientExtensions = new ArrayList<Extension>();

		HashMap<String, ArrayList<Extension>> standardMap = StandardextensionParser(eExtensions);
		serverExtensions.addAll(standardMap.get("CA"));
		clientExtensions.addAll(standardMap.get("CLIENT"));

		ArrayList<Extension> standardExtensions = new ArrayList<Extension>();
		standardExtensions.addAll(standardMap.get("CA"));
		standardExtensions.addAll(standardMap.get("CLIENT"));
		map.put("standardExtensions", standardExtensions);

		HashMap<String, ArrayList<Extension>> otherMap = OtherextensionParser(eExtensions);
		serverExtensions.addAll(otherMap.get("CA"));
		clientExtensions.addAll(otherMap.get("CLIENT"));
		ArrayList<Extension> otherExtensions = new ArrayList<Extension>();
		otherExtensions.addAll(otherMap.get("CA"));
		otherExtensions.addAll(otherMap.get("CLIENT"));
		map.put("otherExtensions", otherExtensions);

		HashMap<String, ArrayList<Extension>> customerMap = CustomerextensionParser(eExtensions);
		serverExtensions.addAll(customerMap.get("CA"));
		clientExtensions.addAll(customerMap.get("CLIENT"));
		ArrayList<Extension> customerExtensions = new ArrayList<Extension>();
		customerExtensions.addAll(customerMap.get("CA"));
		customerExtensions.addAll(customerMap.get("CLIENT"));
		map.put("customerExtensions", customerExtensions);

		map.put("CA", serverExtensions);
		map.put("CLIENT", clientExtensions);
		return map;
	}

	/**
	 * @方法名称：UpdateCerttemplateContentXML
	 * @描述： TODO
	 * @作者： 任龙
	 * @创建日期： 2012-5-3 下午04:03:40
	 * @参数：@param xmlInfo 要修改的xml字符串
	 * @参数：@param algorithmidentifier 签名算法
	 * @参数：@param minkeylength 最小密钥长度
	 * @参数：@return
	 * @参数：@throws TemplateException
	 */
	public static String UpdateCerttemplateContentXML(String xmlInfo, String signAlg) throws TemplateException {
		if (xmlInfo == null) {
			throw new TemplateException("The Parameter xmlInfo can not be null");
		}
		String newStr = "<signalg>" + signAlg;
		return xmlInfo.replace(xmlInfo.substring(xmlInfo.indexOf("<signalg>"), xmlInfo.indexOf("</signalg>")), newStr);

	}
/*
	public void Test() throws IOException, TemplateException {

		String caSignAlg = CAServer.getConf().getKeyStore().getAlg();

		String content = null;
		InputStream in = null;
		in = this.getClass().getResourceAsStream("/cn/com/infosec/netcert/ca/template/ca.xml");
		byte[] b = new byte[in.available()];
		in.read(b);
		in.close();
		content = new String(b);
		CertTemplate ca = new CertTemplate("CA", content, CertTemplate.CERTTEMPLATE_STATUS_NOMAL, CertTemplate.CERTTEMPLATE_STATUS_USED,
			CertTemplate.CERTTEMPLATE_IS_SYS, caSignAlg);

		// 修改初始化时用户指定的扩展
		String pathLen = "3"; // 证书链长度限制
		if (pathLen != null && pathLen.length() > 0) {
			List<Extension> list = ca.getServerExtensions();
			for (Extension e : list) {
				if ("2.5.29.19".equals(e.getOID())) {
					List<ExtensionEntry> entryList = e.getExtensionEntrys();
					for (ExtensionEntry ee : entryList) {
						if ("MAXPATHLEN".equalsIgnoreCase(ee.getName())) {
							ee.setValue(pathLen);
						}
					}
				}
			}
		}

		String cpsOid = CAServer.getConf().getCaCpsOid(); // cps扩展
		String cpsUrl = CAServer.getConf().getCaCpsUrl();
		if (cpsOid != null && cpsOid.length() > 0 && cpsUrl != null && cpsUrl.length() > 0) {
			ExtensionEntry ee = new ExtensionEntry();
			ee.setOid(cpsOid);
			ee.setType("CPSURI");
			ee.setValue(cpsUrl);

			Extension e = new Extension();
			e.setOID("2.5.29.32");
			e.setIscritical(false);
			e.getExtensionEntrys().add(ee);

			ca.getServerExtensions().add(e);
		}
		System.out.println(ca.toString());
	}
	*/
	
	public void tt() throws Exception{
		
		InputStream in  = this.getClass().getResourceAsStream("/cn/com/infosec/netcert/ca/template/test.xml");
		byte[] b = new byte[in.available()];
		in.read(b);
		in.close();
		String content = new String(b);
		CertTemplate ca = new CertTemplate("CA", content, CertTemplate.CERTTEMPLATE_STATUS_NOMAL, CertTemplate.CERTTEMPLATE_STATUS_USED,
			CertTemplate.CERTTEMPLATE_IS_SYS, "SM2withSM3");
		
		
		
		System.out.print(ca.toString());
	}

	public static void main(String[] args) throws Exception {
		CertTemplateParser p = new CertTemplateParser();
		p.tt();


		
		
		
		// String ee_signing =
		// "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?><certtemplate specialType = \"MS_smartCardlogon\"><base><certificateversion>3</certificateversion><validitylimit>3600</validitylimit><keygeneratplace>CLIENT</keygeneratplace><pubkeykeypolicy><allow algorithmidentifier=\"SM2\" minkeylength=\"256\" /><allow algorithmidentifier=\"RSA\" minkeylength=\"1024\" /></pubkeykeypolicy><signalg>SHA1withRSA</signalg>	</base><extensions><standardextensions><extension OID=\"1.3.6.1.5.5.7.1.1\" iscritical=\"true\"><entry name=\"LOCATIONURI\" value=\"http://127.0.0.1:12333/\" /></extension><extension  OID=\"2.5.29.35\" iscritical=\"false\"/><extension  OID=\"2.5.29.14\" iscritical=\"false\" /><extension  OID=\"2.5.29.15\" iscritical=\"false\"  ><entry name=\"USAGE\"  value=\"110000000\"/></extension><extension  OID=\"2.5.29.32\" iscritical=\"false\"  datasource=\"CA\" ISMUST=\"TRUE\"><entry oid=\"1.2.3.4\" type=\"USERNOTICE\" value=\"dasdwfgsdheguw\"/></extension><extension OID=\"2.5.29.19\" iscritical=\"false\" ><entry name=\"isca\" value=\"false\"/></extension><extension OID=\"2.5.29.37\" iscritical=\"false\"><entry oid=\"1.3.6.1.5.5.7.3.2\" /><entry oid=\"1.3.6.1.5.5.7.3.4\" /></extension><extension  OID=\"2.5.29.31\" iscritical=\"false\"  ><entry name=\"DIR\" value=\"cn=crl*,ou=crl\" appendbasedn=\"TRUE\"/></extension></standardextensions></extensions><certpolicy><permitsamedn>TRUE</permitsamedn><updateandrevoke>TRUE</updateandrevoke><keyPolicy>rekey</keyPolicy></certpolicy><publish><iscert>TRUE</iscert><iscrl>TRUE</iscrl></publish></certtemplate>";

		// String ee_signing =
		// "<?xml version=\"1.0\" encoding=\"GB2312\" standalone=\"yes\" ?><certtemplate specialType = \"MS_smartCardlogon\"><base><certificateversion>3</certificateversion><validitylimit>3600</validitylimit><keygeneratplace>CLIENT</keygeneratplace><pubkeykeypolicy><allow algorithmidentifier=\"SM2\" minkeylength=\"256\" /><allow algorithmidentifier=\"RSA\" minkeylength=\"1024\"/></pubkeykeypolicy><signalg>SHA1withRSA</signalg></base><extensions><standardextensions><extension OID=\"1.3.6.1.5.5.7.1.1\" iscritical=\"true\"><entry name=\"LOCATIONURI\"><![CDATA[http://127.0.0.1:12333/]]></entry> <entry name=\"OCSPURI\"><![CDATA[http://127.0.0.1:12333/]]></entry></extension><extension  OID=\"2.5.29.35\" iscritical=\"false\"/><extension  OID=\"2.5.29.14\" iscritical=\"false\" /><extension  OID=\"2.5.29.15\" iscritical=\"false\"  ><entry name=\"USAGE\"  value=\"110000000\"/></extension><extension  OID=\"2.5.29.32\" iscritical=\"false\"  datasource=\"CA\" ISMUST=\"TRUE\"> <entry type=\"policyOid\">1.2.3.4</entry><entry type=\"USERNOTICE\"><![CDATA[dasdwfgsdheguw]]></entry><entry type=\"CPSURI\"><![CDATA[dasdwfgsdheguw]]></entry></extension><extension OID=\"2.5.29.19\" iscritical=\"false\" ><entry name=\"isca\" value=\"false\"/></extension><extension OID=\"2.5.29.37\" iscritical=\"false\"><entry oid=\"1.3.6.1.5.5.7.3.2\" /><entry oid=\"1.3.6.1.5.5.7.3.4\" /></extension><extension  OID=\"2.5.29.31\" iscritical=\"false\"  ><entry name=\"DIR\" value=\"cn=crl*,ou=crl\" appendbasedn=\"TRUE\"/></extension></standardextensions><customextensions><extension OID=\"1.3.6.1.4.1.27971.35.2\" iscritical=\"false\"><entry oid=\"1.3.6.1.4.1.27971.35.2\" /> 	</extension>   </customextensions></extensions><certpolicy><permitsamedn>TRUE</permitsamedn><updateandrevoke>TRUE</updateandrevoke><keyPolicy>rekey</keyPolicy></certpolicy><publish><iscert>TRUE</iscert><iscrl>TRUE</iscrl></publish></certtemplate>";
		// CertTemplate template = new CertTemplate("ee_signing", ee_signing,
		// CertTemplate.CERTTEMPLATE_STATUS_NOMAL, "0", "0");
		//
		// List<Keypolicy> list = template.getPubKeyPolicy();
		// for (int i = 0; i < list.size(); i++) {
		// Keypolicy key = list.get(i);
		// System.out.println(key.getAlgorithmidentifier());
		// System.out.println(key.getMinkeylength());
		//
		// }
		//
		// System.out.println(template.getSpecialType());
		// System.out.println(template.toString());
		//
		// CertTemplate template1 = new CertTemplate("ee_signing1",
		// template.toString(), CertTemplate.CERTTEMPLATE_STATUS_NOMAL, "0",
		// "0");
		// System.out.println(template1.toString());
		//		
		// System.out.println(CertTemplateParser.UpdateCerttemplateContentXML(ee_signing,
		// "SH23A1"));

		// BigInteger a;
		// long st = System.currentTimeMillis();
		// for (int i = 0; i < 100000; i++) {
		// a = AdminCertSnGenerator.generateRandomBigInteger(4);
		// // System.out.println(a.toString());
		// }
		// long end = System.currentTimeMillis();
		//		
		// System.out.println(end - st);
//		new CertTemplateParser().Test();

	}

	/**
	 * @方法名称：getResourceAsStream
	 * @描述： TODO
	 * @作者： 任龙
	 * @创建日期： 2012-5-25 上午09:51:34
	 * @参数：@param string
	 * @参数：@return
	 */

	private static InputStream getResourceAsStream(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
