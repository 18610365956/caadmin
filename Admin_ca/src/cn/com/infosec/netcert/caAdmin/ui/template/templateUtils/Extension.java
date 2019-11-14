package cn.com.infosec.netcert.caAdmin.ui.template.templateUtils;

import java.util.ArrayList;
import java.util.List;



public class Extension {
	// OID
	private String OID;
	// 是否关键
	private boolean iscritical;
	// 数据来源
	private String datasource;
	// 是否必须
	private boolean ismust;
	// 扩展属性
	private List<ExtensionEntry> extensionEntrys = new ArrayList();

	private CertTemplate certTemplate;

	public boolean isIscritical() {
		return iscritical;
	}

	public void setIscritical(boolean iscritical) {
		this.iscritical = iscritical;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public boolean isIsmust() {
		return ismust;
	}

	public void setIsmust(boolean ismust) {
		this.ismust = ismust;
	}

	public String getOID() {
		return OID;
	}

	public void setOID(String oID) {
		OID = oID;
	}

	public CertTemplate getCertTemplate() {
		return certTemplate;
	}

	public void setCertTemplate(CertTemplate certTemplate) {
		this.certTemplate = certTemplate;
	}

	public List<ExtensionEntry> getExtensionEntrys() {
		return extensionEntrys;
	}

	public void setExtensionEntrys(List<ExtensionEntry> extensionEntrys) {
		this.extensionEntrys = extensionEntrys;
	}
}
