package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.encry;

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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_subAltName_enc extends ApplicationWindow {

	// 签名模板属性
	private Button btn_subjectAltNameM_enc, btn_dNSName_enc, btn_ipAddress_enc, btn_rfc822Name_enc, btn_otherName_enc;
	public Button btn_keyUsageIscritical_enc;
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * Create the application window.
	 */
	public Panel_subAltName_enc() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}


	// 主题备用名视图
	protected void panel_subAltName_enc(Composite comp_subAltName) {
		Group group_27 = new Group(comp_subAltName, SWT.NONE);
		GridLayout gl_group_27 = new GridLayout(1, false);
		gl_group_27.marginLeft = 10;
		group_27.setLayout(gl_group_27);
		GridData gd_group_27 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_27.heightHint = 493;
		gd_group_27.widthHint = 500;
		group_27.setLayoutData(gd_group_27);
		group_27.setText(l.getString("subjectAltName"));

		btn_subjectAltNameM_enc = new Button(group_27, SWT.CHECK);
		GridData gd_btn_subjectAltNameM_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_subjectAltNameM_enc.heightHint = 31;
		gd_btn_subjectAltNameM_enc.widthHint = 118;
		btn_subjectAltNameM_enc.setLayoutData(gd_btn_subjectAltNameM_enc);
		btn_subjectAltNameM_enc.setText(l.getString("critical"));

		Label label_5 = new Label(group_27, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_5 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_5.widthHint = 480;
		label_5.setLayoutData(gd_label_5);
		new Label(group_27, SWT.NONE);

		Group group_28 = new Group(group_27, SWT.NONE);
		GridData gd_group_28 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_28.heightHint = 237;
		gd_group_28.widthHint = 472;
		group_28.setLayoutData(gd_group_28);

		GridLayout gl_group_28 = new GridLayout(1, false);
		gl_group_28.marginTop = 10;
		gl_group_28.marginBottom = 10;
		gl_group_28.marginLeft = 10;
		gl_group_28.marginRight = 10;
		gl_group_28.horizontalSpacing = 20;
		gl_group_28.verticalSpacing = 15;
		group_28.setLayout(gl_group_28);
		group_28.setText(l.getString("allowSubjectAltName"));

		btn_dNSName_enc = new Button(group_28, SWT.CHECK);
		GridData gd_btn_dNSName_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_dNSName_enc.heightHint = 39;
		btn_dNSName_enc.setLayoutData(gd_btn_dNSName_enc);
		btn_dNSName_enc.setText(l.getString("DNSName"));

		btn_ipAddress_enc = new Button(group_28, SWT.CHECK);
		GridData gd_btn_ipAddress_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_ipAddress_enc.heightHint = 35;
		btn_ipAddress_enc.setLayoutData(gd_btn_ipAddress_enc);
		btn_ipAddress_enc.setText(l.getString("IPAddress"));

		btn_rfc822Name_enc = new Button(group_28, SWT.CHECK);
		GridData gd_btn_rfc822Name_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_rfc822Name_enc.heightHint = 36;
		btn_rfc822Name_enc.setLayoutData(gd_btn_rfc822Name_enc);
		btn_rfc822Name_enc.setText(l.getString("rfc822Name"));

		btn_otherName_enc = new Button(group_28, SWT.CHECK);
		GridData gd_btn_otherName_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_otherName_enc.widthHint = 175;
		gd_btn_otherName_enc.heightHint = 39;
		btn_otherName_enc.setLayoutData(gd_btn_otherName_enc);
		btn_otherName_enc.setText(l.getString("otherName"));
		
	}
	
	
	// 封装主题备用名设置信息
	public List<ExtensionEntry> packageTempInfo() {
		List<ExtensionEntry> exList_subjectAltName_enc = new ArrayList<ExtensionEntry>();
		if (btn_dNSName_enc.getSelection()) {
			ExtensionEntry entry_DNSName = new ExtensionEntry();
			entry_DNSName.setName("DNSNAME");
			exList_subjectAltName_enc.add(entry_DNSName);
		}
		if (btn_ipAddress_enc.getSelection()) {
			ExtensionEntry entry_IPAddress = new ExtensionEntry();
			entry_IPAddress.setName("IPADDRESS");
			exList_subjectAltName_enc.add(entry_IPAddress);
		}
		if (btn_rfc822Name_enc.getSelection()) {
			ExtensionEntry entry_rfc822Name = new ExtensionEntry();
			entry_rfc822Name.setName("RFC822NAME");
			exList_subjectAltName_enc.add(entry_rfc822Name);
		}
		if (btn_otherName_enc.getSelection()) {
			ExtensionEntry entry_otherName = new ExtensionEntry();
			entry_otherName.setName("OtherName.principalName");
			exList_subjectAltName_enc.add(entry_otherName);
		}
		return exList_subjectAltName_enc;
	}

	
	// 主题备用名
	public void loadCertTemplateInfo(Extension stand) {
		btn_subjectAltNameM_enc.setSelection(stand.isIscritical());
		List<ExtensionEntry> exS_3 = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exS_3) {
			if ("DNSName".equalsIgnoreCase(ex.getName())) {
				btn_dNSName_enc.setSelection(true);
			}
			if ("ipAddress".equalsIgnoreCase(ex.getName())) {
				btn_ipAddress_enc.setSelection(true);
			}
			if ("rfc822Name".equalsIgnoreCase(ex.getName())) {
				btn_rfc822Name_enc.setSelection(true);
			}
			if ("otherName.principalName".equalsIgnoreCase(ex.getName())) {
				btn_otherName_enc.setSelection(true);
			}
		}
	}


	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		container.setLayout(gl_composite);

		this.panel_subAltName_enc(container);

		return container;

	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_subAltName_enc window = new Panel_subAltName_enc();
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
