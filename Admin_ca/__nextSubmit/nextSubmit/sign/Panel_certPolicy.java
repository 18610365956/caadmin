package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign;

import java.util.ArrayList;
import java.util.List;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_certPolicy extends ApplicationWindow {

	private Text text_specifyPolicyOID, text_CPS, text_userNotice;
	private Button btn_certPolicyIscritical,btn_noPolicy, btn_anyPolicy, btn_specifyPolicyOID, btn_CPS, btn_userNotice;

	private static ResourceBundle l = Env.getLanguage();
	
	/**
	 * Create the application window.
	 */
	public Panel_certPolicy() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		
	}

	// 封装证书策略
	public Extension packageTempInfo() {	
		Extension ex_certPolicy = new Extension();
		List<ExtensionEntry> exList_certPolicy = new ArrayList<ExtensionEntry>();

		ExtensionEntry entry_specifyPolicyOID = new ExtensionEntry();
		entry_specifyPolicyOID.setType("POLICYOID");
		entry_specifyPolicyOID.setValue(text_specifyPolicyOID.getText().trim());
		exList_certPolicy.add(entry_specifyPolicyOID);

		if (btn_CPS.getSelection()) {
			ExtensionEntry entry_CPS = new ExtensionEntry();
			entry_CPS.setType("CPSURI");
			entry_CPS.setValue(text_CPS.getText().trim());
			exList_certPolicy.add(entry_CPS);
		}
		if (btn_userNotice.getSelection()) {
			ExtensionEntry entry_userNotice = new ExtensionEntry();
			entry_userNotice.setType("USERNOTICE");
			entry_userNotice.setValue(text_userNotice.getText().trim());
			exList_certPolicy.add(entry_userNotice);
		}
		ex_certPolicy.setOID("2.5.29.32");
		ex_certPolicy.setIscritical(btn_certPolicyIscritical.getSelection());
		ex_certPolicy.setDatasource("CA");
		ex_certPolicy.setIsmust(false);
		ex_certPolicy.setExtensionEntrys(exList_certPolicy);
		return ex_certPolicy;
	}
	// 加载证书策略
	public void loadCertTemplateInfo(Extension stand) {
		btn_certPolicyIscritical.setSelection(stand.isIscritical());
		btn_specifyPolicyOID.setSelection(true);
		specifyPolicyOIDSelection(true);
		List<ExtensionEntry> exList2 = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exList2) {
			if ("policyOid".equalsIgnoreCase(ex.getType())) {
				btn_noPolicy.setSelection(false);
				btn_anyPolicy.setSelection(false);
				text_specifyPolicyOID.setText(ex.getValue());
			}
			if ("CPSURI".equalsIgnoreCase(ex.getType())) {
				btn_CPS.setSelection(true);
				text_CPS.setText(ex.getValue());
			}
			if ("userNotice".equalsIgnoreCase(ex.getType())) {
				btn_userNotice.setSelection(true);
				text_userNotice.setText(ex.getValue());
			}
		}
	}

	// 证书策略视图
	protected void panel_certPolicy(Composite composite_22) {

		Group group_22 = new Group(composite_22, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginTop = 10;
		gridLayout.marginBottom = 10;
		gridLayout.marginLeft = 10;
		gridLayout.marginRight = 10;
		group_22.setLayout(gridLayout);
		group_22.setText(l.getString("certPolicy"));
		GridData gd_group_22 = new GridData();
		gd_group_22.heightHint = 493;
		gd_group_22.widthHint = 500;
		group_22.setLayoutData(gd_group_22);

		btn_certPolicyIscritical = new Button(group_22, SWT.CHECK);
		GridData gd_btn_certPolicyIscritical = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_certPolicyIscritical.heightHint = 35;
		btn_certPolicyIscritical.setLayoutData(gd_btn_certPolicyIscritical);

		// btn_certPolicyIscritical.setBounds(27, 35, 100, 21);
		btn_certPolicyIscritical.setText(l.getString("critical"));

		Label label_6 = new Label(group_22, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_6 = new GridData();
		gd_label_6.widthHint = 480;
		label_6.setLayoutData(gd_label_6);
		// label_6.setBounds(21, 70, 420, 2);

		Group group_221 = new Group(group_22, SWT.NONE);
		GridLayout gl_group_221 = new GridLayout(2, false);
		gl_group_221.marginTop = 10;
		gl_group_221.marginBottom = 10;
		gl_group_221.marginLeft = 10;
		gl_group_221.marginRight = 10;
		gl_group_221.horizontalSpacing = 10;
		gl_group_221.verticalSpacing = 15;
		group_221.setLayout(gl_group_221);
		// group_221.setBounds(21, 76, 540, 150);

		GridData gd_group_221 = new GridData();
		gd_group_221.widthHint = 470;
		group_221.setLayoutData(gd_group_221);

		btn_noPolicy = new Button(group_221, SWT.RADIO);
		GridData gd_btn_noPolicy = new GridData();
		gd_btn_noPolicy.horizontalSpan = 2;
		btn_noPolicy.setLayoutData(gd_btn_noPolicy);
		// btn_noPolicy.setBounds(10, 21, 100, 21);
		btn_noPolicy.setText(l.getString("none"));
		btn_noPolicy.setSelection(true);

		btn_anyPolicy = new Button(group_221, SWT.RADIO);
		GridData gd_btn_anyPolicy = new GridData();
		gd_btn_anyPolicy.horizontalSpan = 2;
		gd_btn_anyPolicy.heightHint = 25;
		btn_anyPolicy.setLayoutData(gd_btn_anyPolicy);
		// btn_anyPolicy.setBounds(10, 63, 120, 21);
		btn_anyPolicy.setText(l.getString("anyPolicy"));

		btn_specifyPolicyOID = new Button(group_221, SWT.RADIO);
		GridData gd_btn_specifyPolicyOID = new GridData();
		btn_specifyPolicyOID.setLayoutData(gd_btn_specifyPolicyOID);

		btn_specifyPolicyOID.setText(l.getString("specifyOID"));
		btn_specifyPolicyOID.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_specifyPolicyOID.getSelection();
				specifyPolicyOIDSelection(flag);
			}
		});

		text_specifyPolicyOID = new Text(group_221, SWT.BORDER);
		GridData gd_text_specifyPolicyOID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_specifyPolicyOID.widthHint = 226;
		text_specifyPolicyOID.setLayoutData(gd_text_specifyPolicyOID);
		gd_btn_specifyPolicyOID.widthHint = 186;
		text_specifyPolicyOID.setEnabled(false);

		Group group_222 = new Group(group_22, SWT.NONE);
		GridLayout gl_group_222 = new GridLayout(2, false);
		gl_group_222.horizontalSpacing = 2;
		gl_group_222.marginLeft = 10;
		gl_group_222.verticalSpacing = 15;
		group_222.setLayout(gl_group_222);

		GridData gd_group_222 = new GridData();
		gd_group_222.heightHint = 102;
		gd_group_222.widthHint = 470;
		group_222.setLayoutData(gd_group_222);

		btn_CPS = new Button(group_222, SWT.CHECK);
		GridData gd_btn_CPS = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_CPS.widthHint = 60;
		btn_CPS.setLayoutData(gd_btn_CPS);
		btn_CPS.setText("CPS");
		btn_CPS.setEnabled(false);
		btn_CPS.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_CPS.getSelection();
				text_CPS.setEnabled(flag);
			}
		});

		text_CPS = new Text(group_222, SWT.BORDER);
		GridData gd_text_CPS = new GridData();
		gd_text_CPS.widthHint = 230;
		text_CPS.setLayoutData(gd_text_CPS);
		text_CPS.setEnabled(false);

		btn_userNotice = new Button(group_222, SWT.CHECK);
		GridData gd_btn_userNotice = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_userNotice.heightHint = 34;
		gd_btn_userNotice.widthHint = 195;
		btn_userNotice.setLayoutData(gd_btn_userNotice);
		btn_userNotice.setBounds(10, 75, 120, 21);
		btn_userNotice.setText(l.getString("userNotice"));
		btn_userNotice.setEnabled(false);
		btn_userNotice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_userNotice.getSelection();
				text_userNotice.setEnabled(flag);
			}
		});

		text_userNotice = new Text(group_222, SWT.BORDER);
		GridData gd_text_userNotice = new GridData();
		gd_text_userNotice.widthHint = 230;
		text_userNotice.setLayoutData(gd_text_userNotice);
		text_userNotice.setEnabled(false);
	}
	// 选中证书策略，使其他设置可选
	private void specifyPolicyOIDSelection(boolean flag) {
		text_specifyPolicyOID.setEnabled(flag);
		btn_CPS.setEnabled(flag);
		btn_userNotice.setEnabled(flag);
	}
	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		container.setLayout(gl_composite_24);
		
		this.panel_certPolicy(container);
	
		return container;
		
	}
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_certPolicy window = new Panel_certPolicy();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
