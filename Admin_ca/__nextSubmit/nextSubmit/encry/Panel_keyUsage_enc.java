package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.encry;

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

public class Panel_keyUsage_enc extends ApplicationWindow {

	private Button btn_keyUsageIscritical_enc, btn_digitalSign_enc, btn_nonRepudiation_enc, btn_keyEn_enc, btn_dataEn_enc, btn_keyAgree_enc,
			btn_keyCertSign_enc, btn_CRLSign_enc, btn_enci_enc, btn_deci_enc;
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * Create the application window.
	 */
	public Panel_keyUsage_enc() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}

	// 密钥用法视图
	protected void panel_keyUsage_enc(Composite comp_keyUsage_enc) {
		Group group_23 = new Group(comp_keyUsage_enc, SWT.NONE);

		GridLayout gl_group_23 = new GridLayout(2, false);
		gl_group_23.marginTop = 10;
		gl_group_23.marginBottom = 10;
		gl_group_23.marginLeft = 10;
		gl_group_23.marginRight = 10;
		gl_group_23.horizontalSpacing = 50;
		gl_group_23.verticalSpacing = 15;
		group_23.setLayout(gl_group_23);

		GridData gd_group_23 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_23.heightHint = 493;
		gd_group_23.widthHint = 500;
		group_23.setLayoutData(gd_group_23);

		group_23.setText(l.getString("keyUsage"));

		btn_keyUsageIscritical_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyUsageIscritical_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyUsageIscritical_enc.heightHint = 25;
		gd_btn_keyUsageIscritical_enc.widthHint = 123;
		btn_keyUsageIscritical_enc.setLayoutData(gd_btn_keyUsageIscritical_enc);
		btn_keyUsageIscritical_enc.setText(l.getString("critical"));
		new Label(group_23, SWT.NONE);

		Label label_1 = new Label(group_23, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_label_1.widthHint = 480;
		label_1.setLayoutData(gd_label_1);

		btn_digitalSign_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_digitalSign_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_digitalSign_enc.heightHint = 40;
		btn_digitalSign_enc.setLayoutData(gd_btn_digitalSign_enc);
		btn_digitalSign_enc.setText("digitalSignature");
		btn_digitalSign_enc.setEnabled(false);

		btn_keyCertSign_enc = new Button(group_23, SWT.CHECK);
		btn_keyCertSign_enc.setText("keyCertSign");
		btn_keyCertSign_enc.setEnabled(false);

		btn_nonRepudiation_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_nonRepudiation_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_nonRepudiation_enc.heightHint = 37;
		btn_nonRepudiation_enc.setLayoutData(gd_btn_nonRepudiation_enc);
		btn_nonRepudiation_enc.setText("nonRepudiation");
		btn_nonRepudiation_enc.setEnabled(false);

		btn_CRLSign_enc = new Button(group_23, SWT.CHECK);
		btn_CRLSign_enc.setText("CRLSign");
		btn_CRLSign_enc.setEnabled(false);

		btn_keyEn_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyEn_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyEn_enc.heightHint = 35;
		btn_keyEn_enc.setLayoutData(gd_btn_keyEn_enc);
		btn_keyEn_enc.setText("keyEncipherment");

		btn_enci_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_enci_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_enci_enc.widthHint = 159;
		btn_enci_enc.setLayoutData(gd_btn_enci_enc);
		btn_enci_enc.setText("encipherOnly");

		btn_dataEn_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_dataEn_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_dataEn_enc.heightHint = 34;
		gd_btn_dataEn_enc.widthHint = 244;
		btn_dataEn_enc.setLayoutData(gd_btn_dataEn_enc);
		btn_dataEn_enc.setText("dataEncipherment");

		btn_deci_enc = new Button(group_23, SWT.CHECK);
		btn_deci_enc.setText("decipherOnly");

		btn_keyAgree_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyAgree_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyAgree_enc.heightHint = 47;
		btn_keyAgree_enc.setLayoutData(gd_btn_keyAgree_enc);
		btn_keyAgree_enc.setText("keyAgreement");
		new Label(group_23, SWT.NONE);
		
	}
	
	//  封装密钥用法
	public Extension packageTempInfo() {
		StringBuffer s = new StringBuffer();
		if (btn_digitalSign_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_nonRepudiation_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyEn_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_dataEn_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyAgree_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyCertSign_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_CRLSign_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_enci_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_deci_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		ExtensionEntry entry_keyUsage = new ExtensionEntry();
		entry_keyUsage.setName("USAGE");
		entry_keyUsage.setValue(s.toString());
		
		List<ExtensionEntry> exList_keyUsage = new ArrayList<ExtensionEntry>();
		exList_keyUsage.add(entry_keyUsage);
		
		Extension ex_keyUsage = new Extension();
		ex_keyUsage.setOID("2.5.29.15");
		ex_keyUsage.setIscritical(btn_keyUsageIscritical_enc.getSelection());
		ex_keyUsage.setDatasource("CA");
		ex_keyUsage.setExtensionEntrys(exList_keyUsage);	
		
		return ex_keyUsage;
	}

	// 加载密钥用法
	public void loadCertTemplateInfo(Extension stand) {
		btn_keyUsageIscritical_enc.setSelection(stand.isIscritical());
		List<ExtensionEntry> exS = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exS) {
			char[] exC = ex.getValue().toCharArray();
			if ('1' == exC[0]) {
				btn_digitalSign_enc.setSelection(true);
			}
			if ('1' == exC[1]) {
				btn_nonRepudiation_enc.setSelection(true);
			}
			if ('1' == exC[2]) {
				btn_keyEn_enc.setSelection(true);
			}
			if ('1' == exC[3]) {
				btn_dataEn_enc.setSelection(true);
			}
			if ('1' == exC[4]) {
				btn_keyAgree_enc.setSelection(true);
			}
			if ('1' == exC[5]) {
				btn_keyCertSign_enc.setSelection(true);
			}
			if ('1' == exC[6]) {
				btn_CRLSign_enc.setSelection(true);
			}
			if ('1' == exC[7]) {
				btn_enci_enc.setSelection(true);
			}
			if ('1' == exC[8]) {
				btn_deci_enc.setSelection(true);
			}
		}
	}

	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		container.setLayout(gl_composite);
		
		this.panel_keyUsage_enc(container);
	
		return container;
		
	}
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_keyUsage_enc window = new Panel_keyUsage_enc();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
