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

public class Panel_userIDEx extends ApplicationWindow {

	// 用户ID扩展
	private Text text_customOID;
	private Button btn_userIdExM, btn_customOID;

	private Panel_userIDEx panel_userIdEx;
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_userIDEx() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		panel_userIdEx = this;
	}

	// 用户ID扩展视图
	protected void panel_userIdEx(Composite comp_userIdEx) {

		Group group_29 = new Group(comp_userIdEx, SWT.NONE);
		GridData gd_group_29 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_29.heightHint = 493;
		gd_group_29.widthHint = 500;
		group_29.setLayoutData(gd_group_29);
		group_29.setBounds(0, 0, 480, 412);
		group_29.setText(l.getString("userIDext"));

		GridLayout gl_group_29 = new GridLayout(2, false);
		gl_group_29.marginTop = 10;
		gl_group_29.marginBottom = 10;
		gl_group_29.marginLeft = 10;
		gl_group_29.marginHeight = 10;
		gl_group_29.horizontalSpacing = 10;
		gl_group_29.verticalSpacing = 10;
		group_29.setLayout(gl_group_29);

		btn_userIdExM = new Button(group_29, SWT.CHECK);
		GridData gd_btn_userIdExM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_userIdExM.heightHint = 33;
		gd_btn_userIdExM.widthHint = 124;
		btn_userIdExM.setLayoutData(gd_btn_userIdExM);
		btn_userIdExM.setText(l.getString("critical"));
		new Label(group_29, SWT.NONE);

		Label label_291 = new Label(group_29, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_291 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_label_291.widthHint = 480;
		label_291.setLayoutData(gd_label_291);
		new Label(group_29, SWT.NONE);
		new Label(group_29, SWT.NONE);

		btn_customOID = new Button(group_29, SWT.CHECK);
		GridData gd_btn_customOID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_customOID.heightHint = 40;
		gd_btn_customOID.widthHint = 191;
		btn_customOID.setLayoutData(gd_btn_customOID);
		btn_customOID.setText(l.getString("customOID"));
		btn_customOID.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				text_customOID.setEnabled(btn_customOID.getSelection());
			}
		});
		text_customOID = new Text(group_29, SWT.BORDER);
		GridData gd_text_customOID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_customOID.widthHint = 218;
		text_customOID.setLayoutData(gd_text_customOID);
	}

	// 封装用户ID扩展
	public Extension packageTempInfo() {
		if (btn_customOID.getSelection()) {
			Extension ex_userIdEx = new Extension();

			List<ExtensionEntry> exList_userIdEx = new ArrayList<ExtensionEntry>();

			ExtensionEntry entry_diyOID = new ExtensionEntry();
			entry_diyOID.setOid((text_customOID.getText().trim()));
			exList_userIdEx.add(entry_diyOID);
			ex_userIdEx.setOID("1.3.6.1.4.1.27971.35.2");
			ex_userIdEx.setIscritical(btn_userIdExM.getSelection());
			ex_userIdEx.setDatasource("CA");
			ex_userIdEx.setIsmust(false);
			ex_userIdEx.setExtensionEntrys(exList_userIdEx);
			return ex_userIdEx;
		} else {
			return null;
		}
	}

	// 加载用户ID扩展
	public void loadCertTemplateInfo(Extension stand) {
		btn_userIdExM.setSelection(stand.isIscritical());
		List<ExtensionEntry> exS_5 = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exS_5) {
			btn_customOID.setSelection(true);
			text_customOID.setText(ex.getOid());
		}
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		container.setLayout(gl_composite_24);

		panel_userIdEx.panel_userIdEx(container);

		return container;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_userIDEx window = new Panel_userIDEx();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
