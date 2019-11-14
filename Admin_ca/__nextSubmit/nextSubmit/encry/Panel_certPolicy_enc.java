package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.encry;

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

public class Panel_certPolicy_enc extends ApplicationWindow {

	private Text text_specifyPolicyOID_enc, text_CPS_enc, text_userNotice_enc;
	private Button btn_certPolicyIscritical_enc, btn_noPolicy_enc, btn_anyPolicy_enc, btn_specifyPolicyOID_enc,
			btn_CPS_enc, btn_userNotice_enc;

	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_certPolicy_enc() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}

	// ֤�������ͼ
	protected void panel_certPolicy_enc(Composite comp_certPolicy_enc) {

		Group group_22 = new Group(comp_certPolicy_enc, SWT.NONE);
		GridData gd_group_22 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_22.widthHint = 500;
		gd_group_22.heightHint = 493;
		group_22.setLayoutData(gd_group_22);
		group_22.setText(l.getString("certPolicy"));

		GridLayout gl_group_22 = new GridLayout(1, false);
		gl_group_22.marginTop = 10;
		gl_group_22.marginBottom = 10;
		gl_group_22.marginLeft = 10;
		gl_group_22.marginRight = 10;
		gl_group_22.horizontalSpacing = 50;
		gl_group_22.verticalSpacing = 10;
		group_22.setLayout(gl_group_22);

		btn_certPolicyIscritical_enc = new Button(group_22, SWT.CHECK);
		GridData gd_btn_certPolicyIscritical_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_certPolicyIscritical_enc.widthHint = 115;
		gd_btn_certPolicyIscritical_enc.heightHint = 27;
		btn_certPolicyIscritical_enc.setLayoutData(gd_btn_certPolicyIscritical_enc);
		btn_certPolicyIscritical_enc.setText(l.getString("critical"));

		Label label_6 = new Label(group_22, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_6 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_6.widthHint = 480;
		label_6.setLayoutData(gd_label_6);

		Group group_221 = new Group(group_22, SWT.NONE);
		GridData gd_group_221 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_221.heightHint = 127;
		gd_group_221.widthHint = 470;
		group_221.setLayoutData(gd_group_221);

		GridLayout gl_group_221 = new GridLayout(2, false);
		gl_group_221.marginTop = 10;
		gl_group_221.marginBottom = 10;
		gl_group_221.marginLeft = 10;
		gl_group_221.marginRight = 10;
		gl_group_221.horizontalSpacing = 30;
		gl_group_221.verticalSpacing = 10;
		group_221.setLayout(gl_group_221);

		btn_noPolicy_enc = new Button(group_221, SWT.RADIO);
		GridData gd_btn_noPolicy_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_noPolicy_enc.heightHint = 24;
		btn_noPolicy_enc.setLayoutData(gd_btn_noPolicy_enc);
		btn_noPolicy_enc.setText(l.getString("none"));
		btn_noPolicy_enc.setSelection(true);
		new Label(group_221, SWT.NONE);

		btn_anyPolicy_enc = new Button(group_221, SWT.RADIO);
		GridData gd_btn_anyPolicy_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_anyPolicy_enc.heightHint = 23;
		btn_anyPolicy_enc.setLayoutData(gd_btn_anyPolicy_enc);
		btn_anyPolicy_enc.setText(l.getString("anyPolicy"));
		new Label(group_221, SWT.NONE);

		btn_specifyPolicyOID_enc = new Button(group_221, SWT.RADIO);
		GridData gd_btn_specifyPolicyOID_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_specifyPolicyOID_enc.heightHint = 25;
		gd_btn_specifyPolicyOID_enc.widthHint = 185;
		btn_specifyPolicyOID_enc.setLayoutData(gd_btn_specifyPolicyOID_enc);
		btn_specifyPolicyOID_enc.setText(l.getString("specifyOID"));
		btn_specifyPolicyOID_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_specifyPolicyOID_enc.getSelection();
				specifyPolicyOIDSelection_enc(flag);
			}
		});

		text_specifyPolicyOID_enc = new Text(group_221, SWT.BORDER);
		GridData gd_text_specifyPolicyOID_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_specifyPolicyOID_enc.widthHint = 196;
		text_specifyPolicyOID_enc.setLayoutData(gd_text_specifyPolicyOID_enc);
		text_specifyPolicyOID_enc.setEnabled(false);

		Group group_222 = new Group(group_22, SWT.NONE);
		group_222.setLayout(new GridLayout(2, false));
		GridData gd_group_222 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_222.heightHint = 105;
		gd_group_222.widthHint = 468;
		group_222.setLayoutData(gd_group_222);

		btn_CPS_enc = new Button(group_222, SWT.CHECK);
		GridData gd_btn_CPS_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_CPS_enc.heightHint = 40;
		btn_CPS_enc.setLayoutData(gd_btn_CPS_enc);
		btn_CPS_enc.setText("CPS");
		btn_CPS_enc.setEnabled(false);
		btn_CPS_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_CPS_enc.getSelection();
				text_CPS_enc.setEnabled(flag);
			}
		});

		text_CPS_enc = new Text(group_222, SWT.BORDER);
		GridData gd_text_CPS_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_CPS_enc.widthHint = 195;
		text_CPS_enc.setLayoutData(gd_text_CPS_enc);
		text_CPS_enc.setEnabled(false);

		btn_userNotice_enc = new Button(group_222, SWT.CHECK);
		GridData gd_btn_userNotice_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_userNotice_enc.heightHint = 43;
		gd_btn_userNotice_enc.widthHint = 224;
		btn_userNotice_enc.setLayoutData(gd_btn_userNotice_enc);
		btn_userNotice_enc.setText(l.getString("userNotice"));
		btn_userNotice_enc.setEnabled(false);
		btn_userNotice_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_userNotice_enc.getSelection();
				text_userNotice_enc.setEnabled(flag);
			}
		});

		text_userNotice_enc = new Text(group_222, SWT.BORDER);
		GridData gd_text_userNotice_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_userNotice_enc.widthHint = 195;
		text_userNotice_enc.setLayoutData(gd_text_userNotice_enc);
		text_userNotice_enc.setEnabled(false);
	}

	private void specifyPolicyOIDSelection_enc(boolean flag) {
		text_specifyPolicyOID_enc.setEnabled(flag);
		btn_CPS_enc.setEnabled(flag);
		btn_userNotice_enc.setEnabled(flag);
	}

	// ��װ֤�����
	public Extension packageTempInfo() {
		if (btn_specifyPolicyOID_enc.getSelection()) {
			Extension ex_certPolicy = new Extension();
			List<ExtensionEntry> exList_certPolicy = new ArrayList<ExtensionEntry>();

			ExtensionEntry entry_specifyPolicyOID = new ExtensionEntry();
			entry_specifyPolicyOID.setType("POLICYOID");
			entry_specifyPolicyOID.setValue(text_specifyPolicyOID_enc.getText().trim());
			exList_certPolicy.add(entry_specifyPolicyOID);

			if (btn_CPS_enc.getSelection()) {
				ExtensionEntry entry_CPS = new ExtensionEntry();
				entry_CPS.setType("CPSURI");
				entry_CPS.setValue(text_CPS_enc.getText().trim());
				exList_certPolicy.add(entry_CPS);
			}
			if (btn_userNotice_enc.getSelection()) {
				ExtensionEntry entry_userNotice = new ExtensionEntry();
				entry_userNotice.setType("USERNOTICE");
				entry_userNotice.setValue(text_userNotice_enc.getText().trim());
				exList_certPolicy.add(entry_userNotice);
			}
			ex_certPolicy.setOID("2.5.29.32");
			ex_certPolicy.setIscritical(btn_certPolicyIscritical_enc.getSelection());
			ex_certPolicy.setDatasource("CA");
			ex_certPolicy.setIsmust(false);
			ex_certPolicy.setExtensionEntrys(exList_certPolicy);
			return ex_certPolicy;
		} else {
			return null;
		}
	}
	
	// ������չ�� 
	public void loadCertTemplateInfo(Extension stand) {
		btn_certPolicyIscritical_enc.setSelection(stand.isIscritical());
		btn_specifyPolicyOID_enc.setSelection(true);
		specifyPolicyOIDSelection_enc(true);
		List<ExtensionEntry> exList2 = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exList2) {
			if ("policyOid".equalsIgnoreCase(ex.getType())) {
				btn_noPolicy_enc.setSelection(false);
				btn_anyPolicy_enc.setSelection(false);
				text_specifyPolicyOID_enc.setText(ex.getValue());
			}
			if ("CPSURI".equalsIgnoreCase(ex.getType())) {
				btn_CPS_enc.setSelection(true);
				text_CPS_enc.setText(ex.getValue());
			}
			if ("userNotice".equalsIgnoreCase(ex.getType())) {
				btn_userNotice_enc.setSelection(true);
				text_userNotice_enc.setText(ex.getValue());
			}
		}
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		container.setLayout(gl_composite_24);

		this.panel_certPolicy_enc(container);

		return container;

	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_certPolicy_enc window = new Panel_certPolicy_enc();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}