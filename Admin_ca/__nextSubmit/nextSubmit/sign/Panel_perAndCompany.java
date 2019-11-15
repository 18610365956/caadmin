package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_perAndCompany extends ApplicationWindow {

	// 签名模板属性
	
	private Button btn_identityCode, btn_identityCodeMust, btn_socialInsuranceCode,
			btn_socialInsuranceCodeM, btn_companyRegistCode, btn_companyRegistCodeM, btn_companyOrganCode,
			btn_companyOrganCodeM, btn_companyTaxCode, btn_companyTaxCodeM;

	
	private Panel_perAndCompany panel_perAndCompany;
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_perAndCompany() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		panel_perAndCompany = this;
	}

	// 封装
	public ArrayList<Extension> packageTempInfo() {
		
		ArrayList<Extension> otherExtensions = new ArrayList<Extension>();
		if (btn_identityCode.getSelection()) {
			Extension ex_identityCode = new Extension();
			ex_identityCode.setOID("1.2.156.10260.4.1.1");
			ex_identityCode.setDatasource("CLIENT");
			ex_identityCode.setIsmust(btn_identityCodeMust.getSelection());
			otherExtensions.add(ex_identityCode);
		}
		if (btn_socialInsuranceCode.getSelection()) {
			Extension ex_socialInsuranceCode = new Extension();
			ex_socialInsuranceCode.setOID("1.2.156.10260.4.1.2");
			ex_socialInsuranceCode.setDatasource("CLIENT");
			ex_socialInsuranceCode.setIsmust(btn_socialInsuranceCodeM.getSelection());
			otherExtensions.add(ex_socialInsuranceCode);
		}
		if (btn_companyRegistCode.getSelection()) {
			Extension ex_companyRegistCode = new Extension();
			ex_companyRegistCode.setOID("1.2.156.10260.4.1.3");
			ex_companyRegistCode.setDatasource("CLIENT");
			ex_companyRegistCode.setIsmust(btn_companyRegistCodeM.getSelection());
			otherExtensions.add(ex_companyRegistCode);
		}
		if (btn_companyOrganCode.getSelection()) {
			Extension ex_companyOrganCode = new Extension();
			ex_companyOrganCode.setOID("1.2.156.10260.4.1.4");
			ex_companyOrganCode.setDatasource("CLIENT");
			ex_companyOrganCode.setIsmust(btn_companyOrganCodeM.getSelection());
			otherExtensions.add(ex_companyOrganCode);
		}
		if (btn_companyTaxCode.getSelection()) {
			Extension ex_companyTaxCode = new Extension();
			ex_companyTaxCode.setOID("1.2.156.10260.4.1.5");
			ex_companyTaxCode.setDatasource("CLIENT");
			ex_companyTaxCode.setIsmust(btn_companyTaxCodeM.getSelection());
			otherExtensions.add(ex_companyTaxCode);
		}
		return otherExtensions;
	}

	// 加载
	public void loadCertTemplateInfo(ArrayList<Extension> otherS) {
		for (Extension other : otherS) {
			String oid = other.getOID();
			boolean flag = other.isIsmust();
			if ("1.2.156.10260.4.1.1".equalsIgnoreCase(oid)) { // 身份识别码
				btn_identityCode.setSelection(true);
				btn_identityCodeMust.setEnabled(true);
				btn_identityCodeMust.setSelection(flag);
			}
			if ("1.2.156.10260.4.1.2".equalsIgnoreCase(oid)) { // 社会保险号
				btn_socialInsuranceCode.setSelection(true);
				btn_socialInsuranceCodeM.setEnabled(true);
				btn_socialInsuranceCodeM.setSelection(flag);
			}
			if ("1.2.156.10260.4.1.3".equalsIgnoreCase(oid)) { // 企业工商注册号
				btn_companyRegistCode.setSelection(true);
				btn_companyRegistCodeM.setEnabled(true);
				btn_companyRegistCodeM.setSelection(flag);
			}
			if ("1.2.156.10260.4.1.4".equalsIgnoreCase(oid)) { // 企业组织机构代码
				btn_companyOrganCode.setSelection(true);
				btn_companyOrganCodeM.setEnabled(true);
				btn_companyOrganCodeM.setSelection(flag);
			}
			if ("1.2.156.10260.4.1.5".equalsIgnoreCase(oid)) { // 企业税号
				btn_companyTaxCode.setSelection(true);
				btn_companyTaxCodeM.setEnabled(true);
				btn_companyTaxCodeM.setSelection(flag);
			}
		}
	}
	
	// 视图
	protected void panel_perAndCompany(Composite composite_210) {

		Group group_210 = new Group(composite_210, SWT.NONE);
		GridData gd_group_210 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_210.heightHint = 493;
		gd_group_210.widthHint = 500;
		group_210.setLayoutData(gd_group_210);
		group_210.setText(l.getString("personalAndCompany"));

		GridLayout gl_group_210 = new GridLayout(1, false);
		gl_group_210.marginTop = 10;
		gl_group_210.marginBottom = 10;
		gl_group_210.marginLeft = 10;
		gl_group_210.marginHeight = 10;
		gl_group_210.horizontalSpacing = 10;

		group_210.setLayout(gl_group_210);

		Group group_2101 = new Group(group_210, SWT.NONE);
		GridData gd_group_2101 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2101.heightHint = 103;
		gd_group_2101.widthHint = 475;
		group_2101.setLayoutData(gd_group_2101);
		group_2101.setText(l.getString("personalInfo"));
		group_2101.setLayout(new GridLayout(2, false));

		btn_identityCode = new Button(group_2101, SWT.CHECK);
		GridData gd_btn_identityCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_identityCode.heightHint = 46;
		btn_identityCode.setLayoutData(gd_btn_identityCode);
		btn_identityCode.setText(l.getString("identifyCode"));
		btn_identityCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_identityCode.getSelection();
				if (!flag) {
					btn_identityCodeMust.setSelection(false);
				}
				btn_identityCodeMust.setEnabled(flag);
			}
		});
		btn_identityCodeMust = new Button(group_2101, SWT.CHECK);
		GridData gd_btn_identityCodeMust = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_identityCodeMust.widthHint = 112;
		btn_identityCodeMust.setLayoutData(gd_btn_identityCodeMust);
		btn_identityCodeMust.setText(l.getString("writeMust"));
		btn_identityCodeMust.setEnabled(false);

		btn_socialInsuranceCode = new Button(group_2101, SWT.CHECK);
		GridData gd_btn_socialInsuranceCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_socialInsuranceCode.widthHint = 309;
		gd_btn_socialInsuranceCode.heightHint = 37;
		btn_socialInsuranceCode.setLayoutData(gd_btn_socialInsuranceCode);
		btn_socialInsuranceCode.setText(l.getString("socialInsuranceCode"));
		btn_socialInsuranceCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_socialInsuranceCode.getSelection();
				if (!flag) {
					btn_socialInsuranceCodeM.setSelection(false);
				}
				btn_socialInsuranceCodeM.setEnabled(flag);
			}
		});
		btn_socialInsuranceCodeM = new Button(group_2101, SWT.CHECK);
		GridData gd_btn_socialInsuranceCodeM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_socialInsuranceCodeM.widthHint = 117;
		btn_socialInsuranceCodeM.setLayoutData(gd_btn_socialInsuranceCodeM);
		btn_socialInsuranceCodeM.setText(l.getString("writeMust"));
		btn_socialInsuranceCodeM.setEnabled(false);
		new Label(group_210, SWT.NONE);

		Group group_2102 = new Group(group_210, SWT.NONE);
		GridData gd_group_2102 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2102.heightHint = 148;
		gd_group_2102.widthHint = 473;
		group_2102.setLayoutData(gd_group_2102);
		group_2102.setText(l.getString("companyInfo"));
		group_2102.setLayout(new GridLayout(2, false));

		btn_companyRegistCode = new Button(group_2102, SWT.CHECK);
		GridData gd_btn_companyRegistCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_companyRegistCode.widthHint = 306;
		gd_btn_companyRegistCode.heightHint = 40;
		btn_companyRegistCode.setLayoutData(gd_btn_companyRegistCode);
		btn_companyRegistCode.setText(l.getString("companyRegistCode"));
		btn_companyRegistCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_companyRegistCode.getSelection();
				if (!flag) {
					btn_companyRegistCodeM.setSelection(false);
				}
				btn_companyRegistCodeM.setEnabled(flag);
			}
		});
		btn_companyRegistCodeM = new Button(group_2102, SWT.CHECK);
		GridData gd_btn_companyRegistCodeM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_companyRegistCodeM.widthHint = 92;
		btn_companyRegistCodeM.setLayoutData(gd_btn_companyRegistCodeM);
		btn_companyRegistCodeM.setText(l.getString("writeMust"));
		btn_companyRegistCodeM.setEnabled(false);

		btn_companyOrganCode = new Button(group_2102, SWT.CHECK);
		GridData gd_btn_companyOrganCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_companyOrganCode.widthHint = 314;
		gd_btn_companyOrganCode.heightHint = 39;
		btn_companyOrganCode.setLayoutData(gd_btn_companyOrganCode);
		btn_companyOrganCode.setText(l.getString("companyOrganCode"));
		btn_companyOrganCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_companyOrganCode.getSelection();
				if (!flag) {
					btn_companyOrganCodeM.setSelection(false);
				}
				btn_companyOrganCodeM.setEnabled(flag);
			}
		});
		btn_companyOrganCodeM = new Button(group_2102, SWT.CHECK);
		btn_companyOrganCodeM.setText(l.getString("writeMust"));
		btn_companyOrganCodeM.setEnabled(false);

		btn_companyTaxCode = new Button(group_2102, SWT.CHECK);
		GridData gd_btn_companyTaxCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_companyTaxCode.heightHint = 36;
		btn_companyTaxCode.setLayoutData(gd_btn_companyTaxCode);
		btn_companyTaxCode.setText(l.getString("companyTaxCode"));
		btn_companyTaxCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_companyTaxCode.getSelection();
				if (!flag) {
					btn_companyTaxCodeM.setSelection(false);
				}
				btn_companyTaxCodeM.setEnabled(flag);
			}
		});
		btn_companyTaxCodeM = new Button(group_2102, SWT.CHECK);
		btn_companyTaxCodeM.setText(l.getString("writeMust"));
		btn_companyTaxCodeM.setEnabled(false);

	}

	// 为了查看方便
	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		container.setLayout(gl_composite);

		panel_perAndCompany.panel_perAndCompany(container);
		
		return container;

	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_perAndCompany window = new Panel_perAndCompany();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(615, 677);
	}

}
