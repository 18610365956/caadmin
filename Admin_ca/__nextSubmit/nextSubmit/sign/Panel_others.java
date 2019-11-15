package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign;

import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_others extends ApplicationWindow {

	// 其他扩展
	private Button btn_subKeyIdentity, btn_subKeyIdentityM, btn_pubKeyIdentity, btn_pubKeyIdentityM, btn_allowAppend;
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_others() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}

	// 其他扩展视图
	protected void panel_others(Composite comp_others) {

		Group group_2110 = new Group(comp_others, SWT.NONE);
		GridData gd_group_2110 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2110.widthHint = 500;
		gd_group_2110.heightHint = 493;
		group_2110.setLayoutData(gd_group_2110);
		group_2110.setText(l.getString("othersExt"));
		group_2110.setLayout(new GridLayout(1, false));

		Group group_2111 = new Group(group_2110, SWT.NONE);
		GridData gd_group_2111 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2111.heightHint = 88;
		gd_group_2111.widthHint = 484;
		group_2111.setLayoutData(gd_group_2111);
		group_2111.setLayout(new GridLayout(2, false));

		btn_subKeyIdentity = new Button(group_2111, SWT.CHECK);
		GridData gd_btn_subKeyIdentity = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_subKeyIdentity.widthHint = 263;
		gd_btn_subKeyIdentity.heightHint = 34;
		btn_subKeyIdentity.setLayoutData(gd_btn_subKeyIdentity);
		btn_subKeyIdentity.setText(l.getString("subKeyIdentity"));
		btn_subKeyIdentity.setSelection(true);
		btn_subKeyIdentity.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_subKeyIdentity.getSelection();
				btn_subKeyIdentityM.setEnabled(flag);
			}
		});
		btn_subKeyIdentityM = new Button(group_2111, SWT.CHECK);
		GridData gd_btn_subKeyIdentityM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_subKeyIdentityM.widthHint = 120;
		btn_subKeyIdentityM.setLayoutData(gd_btn_subKeyIdentityM);
		btn_subKeyIdentityM.setText(l.getString("critical"));

		btn_pubKeyIdentity = new Button(group_2111, SWT.CHECK);
		GridData gd_btn_pubKeyIdentity = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_pubKeyIdentity.widthHint = 288;
		gd_btn_pubKeyIdentity.heightHint = 32;
		btn_pubKeyIdentity.setLayoutData(gd_btn_pubKeyIdentity);
		btn_pubKeyIdentity.setText(l.getString("btn_pubKeyIdentity"));
		btn_pubKeyIdentity.setSelection(true);
		btn_pubKeyIdentity.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_pubKeyIdentity.getSelection();
				btn_pubKeyIdentityM.setEnabled(flag);
			}
		});
		btn_pubKeyIdentityM = new Button(group_2111, SWT.CHECK);
		btn_pubKeyIdentityM.setText(l.getString("critical"));

		btn_allowAppend = new Button(group_2110, SWT.CHECK);
		GridData gd_btn_allowAppend = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_allowAppend.widthHint = 366;
		gd_btn_allowAppend.heightHint = 46;
		btn_allowAppend.setLayoutData(gd_btn_allowAppend);
		btn_allowAppend.setText(l.getString("allowAppend"));
	}

	// 封装其他扩展 颁发机构密钥标识符 
	public Extension packageTempInfo_pub() { 
		if (btn_pubKeyIdentity.getSelection()) {
			Extension ex_pubKeyIdentity = new Extension();
			ex_pubKeyIdentity.setOID("2.5.29.35");
			ex_pubKeyIdentity.setIscritical(btn_pubKeyIdentityM.getSelection());
			ex_pubKeyIdentity.setDatasource("CA");
			ex_pubKeyIdentity.setIsmust(false);
			return ex_pubKeyIdentity;
		} else {
			return null;
		}
	}
	// 封装其他扩展  主题密钥标识符
	public Extension packageTempInfo_sub() { 
		if (btn_subKeyIdentity.getSelection()) {
			Extension ex_subKeyIdentityC = new Extension();
			ex_subKeyIdentityC.setOID("2.5.29.14");
			ex_subKeyIdentityC.setIscritical(btn_subKeyIdentityM.getSelection());
			ex_subKeyIdentityC.setDatasource("CA");
			ex_subKeyIdentityC.setIsmust(false);
			return ex_subKeyIdentityC;
		} else {
			return null;
		}
	}

	// 加载其他扩展
	public void loadCertTemplateInfo_sub(Extension stand_sub) {

		btn_subKeyIdentity.setSelection(true);
		btn_subKeyIdentityM.setEnabled(true);
		btn_subKeyIdentityM.setSelection(stand_sub.isIscritical());
	}

	// 加载其他扩展
	public void loadCertTemplateInfo_pub(Extension stand_pub) {

		btn_pubKeyIdentity.setSelection(true);
		btn_pubKeyIdentityM.setEnabled(true);
		btn_pubKeyIdentityM.setSelection(stand_pub.isIscritical());

	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		container.setLayout(gl_composite_24);

		this.panel_others(container);

		return container;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_others window = new Panel_others();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
