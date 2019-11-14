package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
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
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_subAltName extends ApplicationWindow {

	// 签名模板属性
	private Button btn_subjectAltNameM, btn_dNSName, btn_ipAddress, btn_rfc822Name, btn_otherName;

	private Panel_subAltName panel_subAltName;
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_subAltName() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		panel_subAltName = this;
	}

	// 封装主题备用名设置信息
	public Extension packageTempInfo() {
		
		Extension ex_subjectAltName = new Extension();
		
		List<ExtensionEntry> exList_subjectAltName = new ArrayList<ExtensionEntry>();
		if (btn_dNSName.getSelection()) {
			ExtensionEntry entry_DNSName = new ExtensionEntry();
			entry_DNSName.setName("DNSNAME");
			exList_subjectAltName.add(entry_DNSName);
		}
		if (btn_ipAddress.getSelection()) {
			ExtensionEntry entry_IPAddress = new ExtensionEntry();
			entry_IPAddress.setName("IPADDRESS");
			exList_subjectAltName.add(entry_IPAddress);
		}
		if (btn_rfc822Name.getSelection()) {
			ExtensionEntry entry_rfc822Name = new ExtensionEntry();
			entry_rfc822Name.setName("RFC822NAME");
			exList_subjectAltName.add(entry_rfc822Name);
		}
		if (btn_otherName.getSelection()) {
			ExtensionEntry entry_otherName = new ExtensionEntry();
			entry_otherName.setName("OtherName.principalName");
			exList_subjectAltName.add(entry_otherName);
		}
		if (exList_subjectAltName.size() > 0) {
			ex_subjectAltName.setOID("2.5.29.17");
			ex_subjectAltName.setIscritical(btn_subjectAltNameM.getSelection());
			ex_subjectAltName.setDatasource("CLIENT");
			ex_subjectAltName.setIsmust(false);
			ex_subjectAltName.setExtensionEntrys(exList_subjectAltName);
		}
		return ex_subjectAltName;
	}

	// 主题备用名
	public void loadCertTemplateInfo(Extension stand) {
		btn_subjectAltNameM.setSelection(stand.isIscritical());
		List<ExtensionEntry> exS_3 = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exS_3) {
			if ("DNSName".equalsIgnoreCase(ex.getName())) {
				btn_dNSName.setSelection(true);
			}
			if ("ipAddress".equalsIgnoreCase(ex.getName())) {
				btn_ipAddress.setSelection(true);
			}
			if ("rfc822Name".equalsIgnoreCase(ex.getName())) {
				btn_rfc822Name.setSelection(true);
			}
			if ("otherName.principalName".equalsIgnoreCase(ex.getName())) {
				btn_otherName.setSelection(true);
			}
		}
	}

	// 主题备用名视图
	protected void panel_subAltName(Composite composite_27) {

		Group group_27 = new Group(composite_27, SWT.NONE);
		GridData gd_group_27 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_27.heightHint = 493;
		gd_group_27.widthHint = 500;
		group_27.setLayoutData(gd_group_27);
		group_27.setBounds(0, 0, 480, 412);
		group_27.setText(l.getString("subjectAltName"));

		GridLayout gl_group_27 = new GridLayout(1, false);
		gl_group_27.marginTop = 10;
		gl_group_27.marginBottom = 10;
		gl_group_27.marginLeft = 10;
		gl_group_27.marginHeight = 10;
		gl_group_27.horizontalSpacing = 10;
		gl_group_27.verticalSpacing = 10;

		group_27.setLayout(gl_group_27);

		btn_subjectAltNameM = new Button(group_27, SWT.CHECK);
		GridData gd_btn_subjectAltNameM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_subjectAltNameM.heightHint = 23;
		gd_btn_subjectAltNameM.widthHint = 111;
		btn_subjectAltNameM.setLayoutData(gd_btn_subjectAltNameM);
		btn_subjectAltNameM.setText(l.getString("critical"));

		Label label_5 = new Label(group_27, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_5 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_5.widthHint = 480;
		label_5.setLayoutData(gd_label_5);

		Group group_28 = new Group(group_27, SWT.NONE);
		GridData gd_group_28 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_28.widthHint = 477;
		gd_group_28.heightHint = 228;
		group_28.setLayoutData(gd_group_28);

		GridLayout gl_group_28 = new GridLayout(1, false);
		gl_group_28.marginTop = 10;
		gl_group_28.marginBottom = 10;
		gl_group_28.marginLeft = 10;
		gl_group_28.marginHeight = 10;
		gl_group_28.horizontalSpacing = 10;
		gl_group_28.verticalSpacing = 15;
		group_28.setLayout(gl_group_28);
		group_28.setText(l.getString("allowSubjectAltName"));

		btn_dNSName = new Button(group_28, SWT.CHECK);
		GridData gd_btn_dNSName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_dNSName.heightHint = 34;
		gd_btn_dNSName.widthHint = 153;
		btn_dNSName.setLayoutData(gd_btn_dNSName);
		btn_dNSName.setText(l.getString("DNSName"));

		btn_ipAddress = new Button(group_28, SWT.CHECK);
		GridData gd_btn_ipAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_ipAddress.heightHint = 34;
		gd_btn_ipAddress.widthHint = 150;
		btn_ipAddress.setLayoutData(gd_btn_ipAddress);
		btn_ipAddress.setText(l.getString("IPAddress"));

		btn_rfc822Name = new Button(group_28, SWT.CHECK);
		GridData gd_btn_rfc822Name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_rfc822Name.heightHint = 33;
		gd_btn_rfc822Name.widthHint = 180;
		btn_rfc822Name.setLayoutData(gd_btn_rfc822Name);
		btn_rfc822Name.setText(l.getString("rfc822Name"));

		btn_otherName = new Button(group_28, SWT.CHECK);
		GridData gd_btn_otherName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_otherName.heightHint = 34;
		gd_btn_otherName.widthHint = 145;
		btn_otherName.setLayoutData(gd_btn_otherName);
		btn_otherName.setText(l.getString("otherName"));
	}

	// 为了查看方便
	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		container.setLayout(gl_composite);

		panel_subAltName.panel_subAltName(container);

		return container;

	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_subAltName window = new Panel_subAltName();
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
