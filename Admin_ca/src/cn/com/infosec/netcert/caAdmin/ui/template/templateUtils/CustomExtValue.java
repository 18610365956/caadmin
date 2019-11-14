package cn.com.infosec.netcert.caAdmin.ui.template.templateUtils;

import java.math.BigDecimal;

import cn.com.infosec.netcert.framework.dao.annotation.Column;
import cn.com.infosec.netcert.framework.dao.annotation.Key;
import cn.com.infosec.netcert.framework.dao.annotation.Table;

@Table("CUSTOM_EXTVALUE")
public class CustomExtValue {
	@Key
	@Column("ID")
	private BigDecimal id;
	
	@Column("EXTTYPE")
	private String type;
	
	@Column("EXTVALUE")
	private String value;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
