package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_keyUsage extends ApplicationWindow {
	
	public Button btn_keyUsageIscritical, btn_digitalSign, btn_nonRepudiation, btn_keyEn, btn_dataEn, btn_keyAgree,
			btn_keyCertSign, btn_CRLSign, btn_enci, btn_deci;
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_keyUsage() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}

	// 封装密钥用法
	public Extension packageTempInfo() {
		Extension ex_keyUsage = new Extension();	
		List<ExtensionEntry> exList_keyUsage = new ArrayList<ExtensionEntry>();
		ExtensionEntry entry_keyUsage = new ExtensionEntry();
		entry_keyUsage.setName("USAGE");
		StringBuffer s = new StringBuffer();
		if (btn_digitalSign.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_nonRepudiation.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyEn.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_dataEn.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyAgree.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyCertSign.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_CRLSign.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_enci.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_deci.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		entry_keyUsage.setValue(s.toString());
		exList_keyUsage.add(entry_keyUsage);

		ex_keyUsage.setOID("2.5.29.15");
		ex_keyUsage.setIscritical(btn_keyUsageIscritical.getSelection());
		ex_keyUsage.setDatasource("CA");
		ex_keyUsage.setIsmust(false);
		ex_keyUsage.setExtensionEntrys(exList_keyUsage);
		return ex_keyUsage;
	}

	// 加载密钥用法
	public void loadCertTemplateInfo(Extension stand) {
		btn_keyUsageIscritical.setSelection(stand.isIscritical());
		List<ExtensionEntry> exS = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exS) {
			char[] exC = ex.getValue().toCharArray();
			if ('1' == exC[0]) {
				btn_digitalSign.setSelection(true);
			}
			if ('1' == exC[1]) {
				btn_nonRepudiation.setSelection(true);
			}
			if ('1' == exC[2]) {
				btn_keyEn.setSelection(true);
			}
			if ('1' == exC[3]) {
				btn_dataEn.setSelection(true);
			}
			if ('1' == exC[4]) {
				btn_keyAgree.setSelection(true);
			}
			if ('1' == exC[5]) {
				btn_keyCertSign.setSelection(true);
			}
			if ('1' == exC[6]) {
				btn_CRLSign.setSelection(true);
			}
			if ('1' == exC[7]) {
				btn_enci.setSelection(true);
			}
			if ('1' == exC[8]) {
				btn_deci.setSelection(true);
			}
		}
	}

	// 证书策略视图
	protected void panel_keyUsage(Composite composite_23) {

		Group group_23 = new Group(composite_23, SWT.NONE);
		GridData gd_group_23 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_23.heightHint = 493;
		gd_group_23.widthHint = 500;
		group_23.setLayoutData(gd_group_23);
		group_23.setBounds(0, 0, 480, 412);
		group_23.setText(l.getString("keyUsage"));

		GridLayout gl_group_23 = new GridLayout(2, false);
		gl_group_23.marginTop = 10;
		gl_group_23.marginBottom = 10;
		gl_group_23.marginLeft = 10;
		gl_group_23.marginRight = 10;
		gl_group_23.horizontalSpacing = 10;
		gl_group_23.verticalSpacing = 10;

		group_23.setLayout(gl_group_23);

		btn_keyUsageIscritical = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyUsageIscritical = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyUsageIscritical.widthHint = 120;
		btn_keyUsageIscritical.setLayoutData(gd_btn_keyUsageIscritical);
		btn_keyUsageIscritical.setText(l.getString("critical"));
		btn_keyUsageIscritical.setSelection(true);
		new Label(group_23, SWT.NONE);

		Label label_1 = new Label(group_23, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_label_1.widthHint = 480;
		label_1.setLayoutData(gd_label_1);
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_digitalSign = new Button(group_23, SWT.CHECK);
		btn_digitalSign.setText("digitalSignature");
		btn_digitalSign.setSelection(true);

		btn_keyCertSign = new Button(group_23, SWT.CHECK);
		btn_keyCertSign.setText("keyCertSign");
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_nonRepudiation = new Button(group_23, SWT.CHECK);
		btn_nonRepudiation.setText("nonRepudiation");
		btn_nonRepudiation.setSelection(true);

		btn_CRLSign = new Button(group_23, SWT.CHECK);
		btn_CRLSign.setText("CRLSign");
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_keyEn = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyEn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyEn.widthHint = 270;
		btn_keyEn.setLayoutData(gd_btn_keyEn);
		btn_keyEn.setText("keyEncipherment");

		btn_enci = new Button(group_23, SWT.CHECK);
		btn_enci.setText("encipherOnly");
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_dataEn = new Button(group_23, SWT.CHECK);
		btn_dataEn.setText("dataEncipherment");

		btn_deci = new Button(group_23, SWT.CHECK);
		btn_deci.setText("decipherOnly");
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_keyAgree = new Button(group_23, SWT.CHECK);
		btn_keyAgree.setText("keyAgreement");
		new Label(group_23, SWT.NONE);
	}
	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		container.setLayout(gl_composite);
		
		this.panel_keyUsage(container);
	
		return container;
		
	}
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_keyUsage window = new Panel_keyUsage();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
