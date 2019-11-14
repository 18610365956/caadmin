package cn.com.infosec.netcert.caAdmin.ui.template.templateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.com.infosec.netcert.framework.dao.annotation.Column;
import cn.com.infosec.netcert.framework.dao.annotation.Key;
import cn.com.infosec.netcert.framework.dao.annotation.Table;

@Table("CUSTOM_EXT")
public class CustomExt {
	@Key("autoGen")
	@Column("ID")
	private BigDecimal id;
	
	@Column("CERTSN")
	private BigDecimal certSN;
	
	@Column("OID")
	private String oid;

	//此oid相关的扩展值
	private List<CustomExtValue> extValues = new ArrayList<CustomExtValue>();
	
	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getCertSN() {
		return certSN;
	}

	public void setCertSN(BigDecimal certSN) {
		this.certSN = certSN;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public List<CustomExtValue> getExtValues() {
		return extValues;
	}
	
}
