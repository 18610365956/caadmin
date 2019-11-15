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

public class Panel_publishPolicy extends ApplicationWindow {

	// 发布策略
	public Button btn_pubCert, btn_pubCRLList;

	private static ResourceBundle l = Env.getLanguage();

	/**
	 * Create the application window.
	 */
	public Panel_publishPolicy() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}

	// 发布策略视图
	protected void panel_publishPolicy(Composite comp_publishPolicy) {
		Group group_7 = new Group(comp_publishPolicy, SWT.NONE);
		GridLayout gl_group_7 = new GridLayout(1, false);
		gl_group_7.marginTop = 10;
		gl_group_7.marginBottom = 10;
		gl_group_7.marginLeft = 10;
		gl_group_7.marginHeight = 10;
		gl_group_7.horizontalSpacing = 10;
		gl_group_7.verticalSpacing = 10;

		group_7.setLayout(gl_group_7);
		GridData gd_group_7 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_7.heightHint = 493;
		gd_group_7.widthHint = 500;
		group_7.setLayoutData(gd_group_7);
		group_7.setBounds(0, 0, 480, 412);
		group_7.setText(l.getString("publishPolicy"));
		new Label(group_7, SWT.NONE);

		btn_pubCert = new Button(group_7, SWT.CHECK);
		GridData gd_btn_pubCert = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_pubCert.widthHint = 152;
		gd_btn_pubCert.heightHint = 39;
		btn_pubCert.setLayoutData(gd_btn_pubCert);
		btn_pubCert.setText(l.getString("pubCert"));

		btn_pubCRLList = new Button(group_7, SWT.CHECK);
		GridData gd_btn_pubCRLList = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_pubCRLList.widthHint = 212;
		gd_btn_pubCRLList.heightHint = 39;
		btn_pubCRLList.setLayoutData(gd_btn_pubCRLList);
		btn_pubCRLList.setText(l.getString("pubCRLList"));
	}

	// 封装发布策略
	public void packageTempInfo(CertTemplate certTemplate) {
		if (btn_pubCert.getSelection()) {
			certTemplate.setIspublishCert("1");
		} else {
			certTemplate.setIspublishCert("0");
		}
		if (btn_pubCRLList.getSelection()) {
			certTemplate.setIspublishCrl("1");
		} else {
			certTemplate.setIspublishCrl("0");
		}
	}

	// 加载发布策略
	public void loadCertTemplateInfo(CertTemplate certTemplate) {
		btn_pubCRLList.setSelection("1".equalsIgnoreCase(certTemplate.getIspublishCrl()) ? true : false);
		btn_pubCert.setSelection("1".equalsIgnoreCase(certTemplate.getIspublishCert()) ? true : false);
	}
	

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		container.setLayout(gl_composite_24);

		this.panel_publishPolicy(container);

		return container;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_publishPolicy window = new Panel_publishPolicy();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
