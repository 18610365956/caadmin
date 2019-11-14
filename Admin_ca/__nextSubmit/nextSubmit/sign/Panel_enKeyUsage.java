package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_enKeyUsage extends ApplicationWindow {

	// 签名模板属性
	private Text text_oID;
	public Button btn_enKeyUsageIscritical, btn_serverAuth, btn_clientAuth, btn_codeSign, btn_emailPro,
			btn_timeStamp, btn_OCSPSign,btn_add, btn_delete;
	private org.eclipse.swt.widgets.List list1;
	private Set<String> oIDs = new HashSet<String>();

	private static ResourceBundle l = Env.getLanguage();

	/**
	 * Create the application window.
	 */
	public Panel_enKeyUsage() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}

	// 封装增强密钥用法
	public Extension packageTempInfo() {
		List<ExtensionEntry> exList_enKeyUsage = new ArrayList<ExtensionEntry>();
		if (btn_serverAuth.getSelection()) {
			ExtensionEntry entry_serverAuth = new ExtensionEntry();
			entry_serverAuth.setOid("1.3.6.1.5.5.7.3.1");
			exList_enKeyUsage.add(entry_serverAuth);
		}
		if (btn_clientAuth.getSelection()) {
			ExtensionEntry entry_clientAuth = new ExtensionEntry();
			entry_clientAuth.setOid("1.3.6.1.5.5.7.3.2");
			exList_enKeyUsage.add(entry_clientAuth);
		}
		if (btn_codeSign.getSelection()) {
			ExtensionEntry entry_codeSign = new ExtensionEntry();
			entry_codeSign.setOid("1.3.6.1.5.5.7.3.3");
			exList_enKeyUsage.add(entry_codeSign);
		}
		if (btn_emailPro.getSelection()) {
			ExtensionEntry entry_emailPro = new ExtensionEntry();
			entry_emailPro.setOid("1.3.6.1.5.5.7.3.4");
			exList_enKeyUsage.add(entry_emailPro);
		}
		if (btn_timeStamp.getSelection()) {
			ExtensionEntry entry_timeStamp = new ExtensionEntry();
			entry_timeStamp.setOid("1.3.6.1.5.5.7.3.8");
			exList_enKeyUsage.add(entry_timeStamp);
		}
		if (btn_OCSPSign.getSelection()) {
			ExtensionEntry entry_OCSPSign = new ExtensionEntry();
			entry_OCSPSign.setOid("1.3.6.1.5.5.7.3.9");
			exList_enKeyUsage.add(entry_OCSPSign);
		}
		if (oIDs.size() != 0) {
			for (String ss : oIDs) {
				ExtensionEntry entry_oid = new ExtensionEntry();
				entry_oid.setOid(ss);
				exList_enKeyUsage.add(entry_oid);
			}
		}
		Extension ex_enKeyUsage = new Extension();
		if (exList_enKeyUsage.size() > 0) {
			ex_enKeyUsage.setOID("2.5.29.37");
			ex_enKeyUsage.setIscritical(btn_enKeyUsageIscritical.getSelection());
			ex_enKeyUsage.setDatasource("CA");
			ex_enKeyUsage.setIsmust(false);
			ex_enKeyUsage.setExtensionEntrys(exList_enKeyUsage);
		}
		return ex_enKeyUsage;
	}

	// 加载密钥用法
	public void loadCertTemplateInfo(Extension stand) {
		btn_enKeyUsageIscritical.setSelection(stand.isIscritical());
		List<ExtensionEntry> exS_2 = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exS_2) {
			if("1.3.6.1.5.5.7.3.1".equals(ex.getOid()))
				btn_serverAuth.setSelection(true);
			else if("1.3.6.1.5.5.7.3.2".equals(ex.getOid()))
				btn_clientAuth.setSelection(true);
			else if("1.3.6.1.5.5.7.3.3".equals(ex.getOid()))
				btn_codeSign.setSelection(true);
			else if("1.3.6.1.5.5.7.3.4".equals(ex.getOid()))
				btn_emailPro.setSelection(true);
			else if("1.3.6.1.5.5.7.3.8".equals(ex.getOid()))
				btn_timeStamp.setSelection(true);
			else if("1.3.6.1.5.5.7.3.9".equals(ex.getOid()))
				btn_OCSPSign.setSelection(true);
			else{
				oIDs.add(ex.getOid());
				list1.add(ex.getOid());
			}
		}
	}
	// 增强密钥用法视图
	protected void panel_enKeyUsage(final Composite composite_24) {

		Group group_24 = new Group(composite_24, SWT.NONE);
		GridData gd_group_24 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_24.heightHint = 493;
		gd_group_24.widthHint = 500;
		group_24.setLayoutData(gd_group_24);
		group_24.setText(l.getString("en_keyUsage"));

		GridLayout gl_group_24 = new GridLayout(1, false);
		gl_group_24.marginTop = 10;
		gl_group_24.marginBottom = 10;
		gl_group_24.marginLeft = 10;
		gl_group_24.marginHeight = 10;
		gl_group_24.horizontalSpacing = 10;
		gl_group_24.verticalSpacing = 10;

		group_24.setLayout(gl_group_24);

		btn_enKeyUsageIscritical = new Button(group_24, SWT.CHECK);
		GridData gd_btn_enKeyUsageIscritical = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_enKeyUsageIscritical.heightHint = 23;
		gd_btn_enKeyUsageIscritical.widthHint = 100;
		btn_enKeyUsageIscritical.setLayoutData(gd_btn_enKeyUsageIscritical);
		btn_enKeyUsageIscritical.setText(l.getString("critical"));

		Label label_2 = new Label(group_24, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_2.widthHint = 480;
		label_2.setLayoutData(gd_label_2);

		Group group_241 = new Group(group_24, SWT.NONE);
		GridData gd_group_241 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_241.heightHint = 181;
		gd_group_241.widthHint = 469;

		group_241.setLayoutData(gd_group_241);
		group_241.setText(l.getString("standardExtKeyUsage"));
		GridLayout gl_group_241 = new GridLayout(2, false);
		gl_group_241.marginTop = 10;
		gl_group_241.marginBottom = 10;
		gl_group_241.marginLeft = 10;
		gl_group_241.marginRight = 10;
		gl_group_241.horizontalSpacing = 10;
		gl_group_241.verticalSpacing = 15;

		group_241.setLayout(gl_group_241);

		btn_serverAuth = new Button(group_241, SWT.CHECK);
		GridData gd_btn_serverAuth = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_serverAuth.heightHint = 38;
		btn_serverAuth.setLayoutData(gd_btn_serverAuth);
		btn_serverAuth.setText("serverAuth");

		btn_emailPro = new Button(group_241, SWT.CHECK);
		GridData gd_btn_emailPro = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_emailPro.widthHint = 177;
		btn_emailPro.setLayoutData(gd_btn_emailPro);
		btn_emailPro.setText("emailProtection");

		btn_clientAuth = new Button(group_241, SWT.CHECK);
		GridData gd_btn_clientAuth = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_clientAuth.heightHint = 34;
		btn_clientAuth.setLayoutData(gd_btn_clientAuth);
		btn_clientAuth.setText("clientAuth");

		btn_timeStamp = new Button(group_241, SWT.CHECK);
		btn_timeStamp.setText("timeStamping");

		btn_codeSign = new Button(group_241, SWT.CHECK);
		GridData gd_btn_codeSign = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_codeSign.heightHint = 40;
		gd_btn_codeSign.widthHint = 234;
		btn_codeSign.setLayoutData(gd_btn_codeSign);
		btn_codeSign.setText("codeSigning");

		btn_OCSPSign = new Button(group_241, SWT.CHECK);
		btn_OCSPSign.setText("OCSPSigning");

		Group group_132 = new Group(group_24, SWT.NONE);

		GridLayout gl_group_132 = new GridLayout(4, false);
		gl_group_132.marginTop = 10;
		gl_group_132.marginBottom = 10;
		gl_group_132.marginLeft = 10;
		gl_group_132.marginRight = 10;
		gl_group_132.horizontalSpacing = 10;
		gl_group_132.verticalSpacing = 10;

		group_132.setLayout(gl_group_132);
		GridData gd_group_132 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_132.widthHint = 471;
		gd_group_132.heightHint = 145;
		group_132.setLayoutData(gd_group_132);

		Label lblNewLabel_132 = new Label(group_132, SWT.NONE);
		lblNewLabel_132.setText("OID:");

		text_oID = new Text(group_132, SWT.BORDER);
		GridData gd_text_oID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_oID.widthHint = 252;
		text_oID.setLayoutData(gd_text_oID);
		new Label(group_132, SWT.NONE);

		btn_add = new Button(group_132, SWT.NONE);
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add.widthHint = 81;
		btn_add.setLayoutData(gd_btn_add);
		btn_add.setText(l.getString("add"));
		btn_add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String oID = text_oID.getText().trim();
				if (oID == null || oID.length() == 0) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_null_OID"));
					messageBox.open();
					return;
				}
				if (!oIDs.contains(oID)) {
					oIDs.add(oID);
					list1.add(oID);
				} else {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_exist_OID"));
					messageBox.open();
				}
				text_oID.setText("");
			}
		});
		new Label(group_132, SWT.NONE);
		list1 = new org.eclipse.swt.widgets.List(group_132, SWT.BORDER);
		GridData gd_list1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_list1.widthHint = 251;
		gd_list1.heightHint = 78;
		list1.setLayoutData(gd_list1);
		new Label(group_132, SWT.NONE);

		btn_delete = new Button(group_132, SWT.NONE);
		GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_delete.widthHint = 84;
		btn_delete.setLayoutData(gd_btn_delete);
		btn_delete.setText(l.getString("delete"));
		btn_delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (list1.getSelectionIndex() == -1) {
					MessageBox messageBox = new MessageBox(composite_24.getShell(), SWT.ERROR); // composite_24
					messageBox.setMessage(l.getString("Notice_not_chooseOID"));
					messageBox.open();
					return;
				}
				String oID = list1.getSelection()[0];
				oIDs.remove(oID);
				list1.remove(list1.getSelectionIndex());
			}
		});

	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		container.setLayout(gl_composite);

		this.panel_enKeyUsage(container);

		return container;

	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_enKeyUsage window = new Panel_enKeyUsage();
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
