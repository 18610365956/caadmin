/**  
 * @��Ŀ����: CAServer
 * @����: Keykeypolicy.java
 * @������cn.com.infosec.netcert.ca.template
 * @����: TODO
 * @���ߣ�����
 * @����ʱ�䣺 2012-5-8 ����02:52:27
 * @��Ȩ: 2012 www.infosec.com.cn Inc. All rights reserved.
 * @�汾�� V1.0  
 */

package cn.com.infosec.netcert.caAdmin.ui.template.templateUtils;



/**
 * @author Renlong
 * @date:2012-5-8 ����02:52:27
 * @version :
 * 
 */
public class Keypolicy {
	private String algorithmidentifier;
	private String minkeylength;
	private CertTemplate template;
	public String getAlgorithmidentifier() {
		return algorithmidentifier;
	}
	public void setAlgorithmidentifier(String algorithmidentifier) {
		this.algorithmidentifier = algorithmidentifier;
	}
	public String getMinkeylength() {
		return minkeylength;
	}
	public void setMinkeylength(String minkeylength) {
		this.minkeylength = minkeylength;
	}
	public CertTemplate getTemplate() {
		return template;
	}
	public void setTemplate(CertTemplate template) {
		this.template = template;
	}
	
	
}
