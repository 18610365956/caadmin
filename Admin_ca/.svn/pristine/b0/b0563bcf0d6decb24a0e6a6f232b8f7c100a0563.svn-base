package cn.com.infosec.netcert.caAdmin.ui.template.templateUtils;

/**
 * <p>Title: 证书扩展产生器</p>
 * <p>Description: 产生NetCert2.0中使用的扩展</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Infosec</p>
 * @author 张宝欣
 * @version 1.0
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Vector;

import cn.com.infosec.asn1.ASN1EncodableVector;
import cn.com.infosec.asn1.ASN1InputStream;
import cn.com.infosec.asn1.ASN1Sequence;
import cn.com.infosec.asn1.DERBoolean;
import cn.com.infosec.asn1.DEREncodable;
import cn.com.infosec.asn1.DERIA5String;
import cn.com.infosec.asn1.DERInteger;
import cn.com.infosec.asn1.DERObjectIdentifier;
import cn.com.infosec.asn1.DEROctetString;
import cn.com.infosec.asn1.DERSequence;
import cn.com.infosec.asn1.DERUTF8String;
import cn.com.infosec.asn1.misc.NetscapeCertType;
import cn.com.infosec.asn1.x509.AuthorityKeyIdentifier;
import cn.com.infosec.asn1.x509.BasicConstraints;
import cn.com.infosec.asn1.x509.CRLDistPoint;
import cn.com.infosec.asn1.x509.DisplayText;
import cn.com.infosec.asn1.x509.DistributionPoint;
import cn.com.infosec.asn1.x509.DistributionPointName;
import cn.com.infosec.asn1.x509.ExtendedKeyUsage;
import cn.com.infosec.asn1.x509.GeneralName;
import cn.com.infosec.asn1.x509.GeneralNames;
import cn.com.infosec.asn1.x509.KeyUsage;
import cn.com.infosec.asn1.x509.PolicyInformation;
import cn.com.infosec.asn1.x509.PolicyQualifierId;
import cn.com.infosec.asn1.x509.PolicyQualifierInfo;
import cn.com.infosec.asn1.x509.SubjectKeyIdentifier;
import cn.com.infosec.asn1.x509.SubjectPublicKeyInfo;
import cn.com.infosec.asn1.x509.UserNotice;
import cn.com.infosec.asn1.x509.X509Name;
import cn.com.infosec.util.Base64;
public class X509ExtensionGenerator {
	public static final String CDP_DIR = "DIR";
	public static final String CDP_URI = "URI";
	public static final String IA5STRING = "IA5String";

	public static final String UTF8STRING = "UTF8String";

	public static final String OCTETSTRING = "OCTETString";

	public static final String DERINTEGER = "Integer";

	public static final String DERBOOLEAN = "Boolean";
	public X509ExtensionGenerator() {
	}

	/**
	 * 产生AuthorityKeyIdentifier扩展对象
	 * @param pubkey java.security.PublicKey对象
	 * @return aki扩展
	 * @throws 发生在从PublicKey Object中提取公钥BITSTRING的Value（不包含Tag和Length)时
	 */
	public static DEREncodable genAuthorityKeyIdentifier(PublicKey pubkey) throws IOException {
		SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo(
					(ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(pubkey
						.getEncoded()))
						.readObject());
		AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier(spki);
		return (DEREncodable) aki;
		/** @todo 此处的aki里可以有三个选项目前只给了一个SubjectPublicKeyInfo */
	}


	/**
	 * 产生SubjectKeyIdentifier扩展对象
	 * @param pubkey java.security.PublicKey对象
	 * @return ski扩展
	 * @throws 发生在从PublicKey Object中提取公钥BITSTRING的Value（不包含Tag和Length)时
	 */
	public static DEREncodable genSubjectKeyIdentifier(PublicKey pubkey) throws IOException {
		SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo(
					(ASN1Sequence) new ASN1InputStream(new ByteArrayInputStream(pubkey
						.getEncoded()))
						.readObject());
		SubjectKeyIdentifier ski = new SubjectKeyIdentifier(spki);
		return (DEREncodable) ski;
	}

	/**
	 * 产生KeyUsage扩展对象
	 * @param usage 密钥用途的int值，用法示例：X509KeyUsage.digitalSignature | X509KeyUsage.cRLSign
	 * @return ku扩展
	 */
	public static DEREncodable genKeyUsage(int usage) {
		KeyUsage ku = new KeyUsage(usage);
		return (DEREncodable) ku;
		/** @todo Check carefully!My Cert is "CRL Signing(82)",Other CA's Cert is  CRL Signing(06)*/
	}

	/**
	 * 产生CertificatePolicies扩展对象
	 * @param policyOid policy的OID, 注：anyPolicy = "2.5.29.32.0"
	 * @param cpsuri CPS（Certification Practice Statement）的URI
	 * @return cp扩展
	 */
	public static DEREncodable genCertificatePolicies(String policyOid, String cpsuri, String uNoticeText) {
        int displayencoding = DisplayText.CONTENT_TYPE_BMPSTRING;
        ASN1EncodableVector policys = new ASN1EncodableVector();
        PolicyInformation pi = getPolicyInformation(policyOid, cpsuri, uNoticeText, displayencoding);
        if (pi != null) {
            policys.add(pi);                    	
        }

        // Add the final extension
        DERSequence seq = new DERSequence(policys);
        return seq; 
	}
	
    private static PolicyInformation getPolicyInformation(String policyOID, String cps, String unotice, int displayencoding) {
    	ASN1EncodableVector qualifiers = new ASN1EncodableVector();
        if ((cps != null) && cps.trim().length()>0) {
            PolicyQualifierInfo pqiCPS = new PolicyQualifierInfo(cps);
            qualifiers.add(pqiCPS);
        }
        if ((unotice != null) && unotice.trim().length()>0) {
            // Normally we would just use 'DisplayText(unotice)' here. IE has problems with UTF8 though, so lets stick with BMPSTRING to satisfy Bills sick needs.
            UserNotice un = new UserNotice(null, new DisplayText(displayencoding, unotice));
            PolicyQualifierInfo pqiUNOTICE = new PolicyQualifierInfo(PolicyQualifierId.id_qt_unotice, un);
            qualifiers.add(pqiUNOTICE);
        }
        PolicyInformation policyInformation = null;
        if ( qualifiers.size() > 0 ) {
            policyInformation = new PolicyInformation(new DERObjectIdentifier(policyOID), new DERSequence(qualifiers));            
        } else {
            policyInformation = new PolicyInformation(new DERObjectIdentifier(policyOID));
        }
        
        return policyInformation;
    } 

	/**
	 * 产生BasicConstraints扩展对象
	 * @param IsCA 是否为CA
	 * @param pathLenConstraint 路径长度限制
	 * @return bc扩展
	 */
	public static DEREncodable genBasicConstraints(
		boolean IsCA,
		int pathLenConstraint) {
		BasicConstraints bc = null;
		if (IsCA && pathLenConstraint>0) {
			bc = new BasicConstraints(pathLenConstraint);
		} else {
			bc = new BasicConstraints(IsCA);
		}
		return (DEREncodable) bc;
	}

	/**
	 * 产生BasicConstraints扩展对象
	 * @param IsCA 是否为CA
	 * @return bc扩展
	 */
	public static DEREncodable genBasicConstraints(boolean IsCA) {
		BasicConstraints bc = new BasicConstraints(IsCA);
		return (DEREncodable) bc;
	}

	/**
	 * 产生ExtendedKeyUsage扩展对象
	 * @param kpids DERObjectIdentifier数组，使用示例：DERObjectIdentifier[] kpids = {KeyPurposeId.id_kp_serverAuth,KeyPurposeId.id_kp_codeSigning};
	 * @return eku扩展
	 */
	public static DEREncodable genExtendedKeyUsage(DERObjectIdentifier[] kpids) {
		Vector vKpids = new Vector();
		for (int i = 0; i < kpids.length; i++) {
			vKpids.add(kpids[i]);
		}
		ExtendedKeyUsage eku = new ExtendedKeyUsage(vKpids);
		return (DEREncodable) eku;
	}

	/**
	 * 产生CRLDistributionPoints扩展对象
	 * @param type CDP类型，"URI"或"DIR"
	 * @param name CDP的Value
	 * @return cdp扩展
	 */
	public static DEREncodable genCRLDistributionPoints(String[][] cpds) {
		Vector<DistributionPoint> vDp = new Vector<DistributionPoint>();
		for (int i = 0; i < cpds.length; i++) {
			String type = cpds[i][0];
			String name = cpds[i][1];
			GeneralName gn = null;
			if (type.equalsIgnoreCase(CDP_DIR)) {
				X509Name x509name = null;
				x509name = new X509Name(name);
				gn = new GeneralName(x509name);
	
			} else if (type.equalsIgnoreCase(CDP_URI)) {
				gn = new GeneralName(new DERIA5String(name), 6);
			}
			GeneralNames gns = new GeneralNames(new DERSequence(gn));
			DistributionPointName dpn = new DistributionPointName(0, gns);
			DistributionPoint distp = new DistributionPoint(dpn, null, null);
			vDp.addElement(distp);
		}

		DistributionPoint[] dpa = (DistributionPoint[])(vDp.toArray(new DistributionPoint[]{}));

		CRLDistPoint cdp = new CRLDistPoint(dpa);
		return (DEREncodable) cdp;
	}

	/**
	 * 产生NetscapeCertType扩展对象
	 * @param usage 密钥用途的int值，用法示例：NetscapeCertType.objectSigning | NetscapeCertType.sslServer
	 * @return nct扩展
	 */
	public static DEREncodable genNetscapeCertType(int usage) {
		NetscapeCertType nct = new NetscapeCertType(usage);
		return (DEREncodable) nct;
	}

	public static DEREncodable getGeneral(String encoding, String value)
		throws Exception {

		DEREncodable deren = null;

		if (encoding.equalsIgnoreCase(IA5STRING)) {

			deren = new DERIA5String(value);

		} else if (encoding.equalsIgnoreCase(UTF8STRING)) {

			deren = new DERUTF8String(value);

		} else if (encoding.equalsIgnoreCase(OCTETSTRING)) {

			deren = new DEROctetString(Base64.decode(value));

		} else if (encoding.equalsIgnoreCase(DERINTEGER)) {

			deren = new DERInteger(new BigInteger(value));

		} else if (encoding.equalsIgnoreCase(DERBOOLEAN)) {

			deren = new DERBoolean(Boolean.valueOf(value).booleanValue());
		}
		return deren;

	}

}