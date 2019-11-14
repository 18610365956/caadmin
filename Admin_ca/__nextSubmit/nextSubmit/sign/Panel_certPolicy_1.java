package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign;

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

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplate;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_certPolicy_1 extends ApplicationWindow {

	// 证书相关策略扩展
	private Button btn_allowDNRepeat, btn_appleCertChgKey, btn_useCertDN;

	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_certPolicy_1() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}

	// 证书相关策略视图
	protected void panel_certPolicy_1(Composite comp_certPolicy_1) {
		Group group_6 = new Group(comp_certPolicy_1, SWT.NONE);
		GridData gd_group_6 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_6.heightHint = 493;
		gd_group_6.widthHint = 500;
		group_6.setLayoutData(gd_group_6);
		group_6.setBounds(0, 0, 480, 412);
		group_6.setText(l.getString("certPolicy_1"));

		GridLayout gl_group_6 = new GridLayout(1, false);
		gl_group_6.marginTop = 10;
		gl_group_6.marginBottom = 10;
		gl_group_6.marginLeft = 10;
		gl_group_6.marginHeight = 10;
		gl_group_6.horizontalSpacing = 10;
		gl_group_6.verticalSpacing = 10;

		group_6.setLayout(gl_group_6);
		new Label(group_6, SWT.NONE);

		btn_allowDNRepeat = new Button(group_6, SWT.CHECK);
		GridData gd_btn_allowDNRepeat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_allowDNRepeat.heightHint = 29;
		btn_allowDNRepeat.setLayoutData(gd_btn_allowDNRepeat);
		btn_allowDNRepeat.setText(l.getString("allowDNRepeat"));

		btn_appleCertChgKey = new Button(group_6, SWT.CHECK);
		GridData gd_btn_appleCertChgKey = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_appleCertChgKey.widthHint = 465;
		gd_btn_appleCertChgKey.heightHint = 30;
		btn_appleCertChgKey.setLayoutData(gd_btn_appleCertChgKey);
		btn_appleCertChgKey.setText(l.getString("appleCertChgKey"));

		btn_useCertDN = new Button(group_6, SWT.CHECK);
		GridData gd_btn_useCertDN = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_useCertDN.heightHint = 32;
		btn_useCertDN.setLayoutData(gd_btn_useCertDN);
		btn_useCertDN.setText(l.getString("useCertDN"));
	}

	// 封装证书相关策略
	public void packageTempInfo(CertTemplate certTemplate) {
		certTemplate.setCheckCsrSubject(btn_useCertDN.getSelection());
		if (btn_appleCertChgKey.getSelection()) {
			certTemplate.setKeyPolicy("1");
		} else {
			certTemplate.setKeyPolicy("0");
		}
		if (btn_allowDNRepeat.getSelection()) {
			certTemplate.setPermitSameDN("1");
		} else {
			certTemplate.setPermitSameDN("0");
		}
	}

	// 加载证书相关策略
	public void loadCertTemplateInfo(CertTemplate certTemplate) {
		btn_useCertDN.setSelection(certTemplate.isCheckCsrSubject());
		btn_allowDNRepeat.setSelection("1".equalsIgnoreCase(certTemplate.getPermitSameDN()));
		btn_appleCertChgKey.setSelection("1".equalsIgnoreCase(certTemplate.getKeyPolicy())); // rekey
	}
	

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		container.setLayout(gl_composite_24);

		this.panel_certPolicy_1(container);

		return container;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_certPolicy_1 window = new Panel_certPolicy_1();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
