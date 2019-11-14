/**  
 * @项目名称: CAServer
 * @标题: CertTemplateEntry.java
 * @包名：cn.com.infosec.netcert.ca.template
 * @描述: TODO
 * @作者：任龙
 * @创建时间： 2012-4-26 上午11:09:04
 * @版权: 2012 www.infosec.com.cn Inc. All rights reserved.
 * @版本： V1.0  
 */

package cn.com.infosec.netcert.caAdmin.ui.template.templateUtils;

/**
 * @author Renlong
 * @date:2012-4-26 上午11:09:04
 * @version :
 * 
 */
public class CertTemplateEntry {

	// 模板名称
	private String certtemplateName;
	// 模板内容
	private String certtemplateContent;
	// 模板状态
	private String certtemplateStatus;
	// 是否被使用
	private String isused;
	// 是否是系统内置模板
	private String isSysTemplate;
	
	//附带的加密模板名
	private String encTmpName;
	
	
	
	public CertTemplateEntry() {

	}

	public CertTemplateEntry(String certtemplateName, String certtemplateContent, String certtemplateStatus, String isused, String isSysTemplate) {
		super();
		this.certtemplateName = certtemplateName;
		this.certtemplateContent = certtemplateContent;
		this.certtemplateStatus = certtemplateStatus;
		this.isused = isused;
		this.isSysTemplate = isSysTemplate;
	}

	public String getCerttemplateName() {
		return certtemplateName;
	}

	public void setCerttemplateName(String certtemplateName) {
		this.certtemplateName = certtemplateName;
	}

	public String getCerttemplateContent() {
		return certtemplateContent;
	}

	public void setCerttemplateContent(String certtemplateContent) {
		this.certtemplateContent = certtemplateContent;
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

	public boolean isIsused() {
		return isused.equals(CertTemplate.CERTTEMPLATE_STATUS_USED);
	}

	public String getIsSysTemplate() {
		return isSysTemplate;
	}

	public void setIsSysTemplate(String isSysTemplate) {
		this.isSysTemplate = isSysTemplate;
	}

	@Override
	public String toString() {
		return "CertTemplateEntry [certtemplateContent=" + certtemplateContent + ", certtemplateName=" + certtemplateName + ", certtemplateStatus="
			+ certtemplateStatus + ", isSysTemplate=" + isSysTemplate + ", isused=" + isused + "]";
	}

	public String getIsDual() {
		return (getEncTmpName()!=null && getEncTmpName().length()>0) ? "1" : "0";
	}


	public String getEncTmpName() {
		return encTmpName;
	}

	public void setEncTmpName(String encTmpName) {
		this.encTmpName = encTmpName;
	}

	
}
