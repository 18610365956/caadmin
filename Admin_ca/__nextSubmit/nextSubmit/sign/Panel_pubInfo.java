package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign;

import java.util.ArrayList;
import java.util.List;
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
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_pubInfo extends ApplicationWindow {

	// 签名模板属性
	private Text text_locationURI, text_OCSPURI;
	private Button btn_pubInfoAccessM, btn_locationURI, btn_OCSPURI;

	private Panel_pubInfo panel_pubInfo;
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_pubInfo() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		panel_pubInfo = this;
	}

	// 封装信息
	public Extension packageTempInfo() {
		Extension ex_pubInfoAccess = new Extension();
		
		List<ExtensionEntry> exList_pubInfoAccess = new ArrayList<ExtensionEntry>();
		if (btn_locationURI.getSelection()) {
			ExtensionEntry entry_locationURI = new ExtensionEntry();
			entry_locationURI.setName("locationURI");
			entry_locationURI.setValue(text_locationURI.getText().trim());
			exList_pubInfoAccess.add(entry_locationURI);
		}
		if (btn_OCSPURI.getSelection()) {
			ExtensionEntry entry_OCSPURI = new ExtensionEntry();
			entry_OCSPURI.setName("OCSPURI");
			entry_OCSPURI.setValue(text_OCSPURI.getText().trim());
			exList_pubInfoAccess.add(entry_OCSPURI);
		}
		if (exList_pubInfoAccess.size() > 0) {
			ex_pubInfoAccess.setOID("1.3.6.1.5.5.7.1.1");
			ex_pubInfoAccess.setIscritical(btn_pubInfoAccessM.getSelection());
			ex_pubInfoAccess.setExtensionEntrys(exList_pubInfoAccess);
		
		}
		return ex_pubInfoAccess;
	}

	// 加载密钥用法
	public void loadCertTemplateInfo(Extension stand) {
		btn_pubInfoAccessM.setSelection(stand.isIscritical());
		List<ExtensionEntry> exS_4 = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exS_4) {
			if ("LocationURI".equalsIgnoreCase(ex.getName())) {
				btn_locationURI.setSelection(true);
				text_locationURI.setText(ex.getValue());
			}
			if ("OCSPURI".equalsIgnoreCase(ex.getName())) {
				btn_OCSPURI.setSelection(true);
				text_OCSPURI.setText(ex.getValue());
			}
		}
	}
	
	// 视图
	protected void panel_pubInfo(Composite composite_28) {

		Group group_28 = new Group(composite_28, SWT.NONE);
		GridData gd_group_28 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_28.heightHint = 493;
		gd_group_28.widthHint = 500;
		group_28.setLayoutData(gd_group_28);
		group_28.setBounds(0, 0, 480, 412);
		group_28.setText(l.getString("publishInfoAccess"));

		GridLayout gl_group_28 = new GridLayout(2, false);
		gl_group_28.marginTop = 10;
		gl_group_28.marginBottom = 10;
		gl_group_28.marginLeft = 10;
		gl_group_28.marginHeight = 10;
		gl_group_28.horizontalSpacing = 10;
		gl_group_28.verticalSpacing = 10;
		group_28.setLayout(gl_group_28);

		btn_pubInfoAccessM = new Button(group_28, SWT.CHECK);
		GridData gd_btn_pubInfoAccessM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_pubInfoAccessM.heightHint = 27;
		gd_btn_pubInfoAccessM.widthHint = 133;
		btn_pubInfoAccessM.setLayoutData(gd_btn_pubInfoAccessM);
		btn_pubInfoAccessM.setText(l.getString("critical"));
		new Label(group_28, SWT.NONE);

		Label label_4 = new Label(group_28, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_4 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_label_4.widthHint = 480;
		label_4.setLayoutData(gd_label_4);

		btn_locationURI = new Button(group_28, SWT.CHECK);
		GridData gd_btn_locationURI = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_locationURI.heightHint = 42;
		gd_btn_locationURI.widthHint = 231;
		btn_locationURI.setLayoutData(gd_btn_locationURI);
		btn_locationURI.setText("LocationURI");
		btn_locationURI.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				text_locationURI.setEnabled(btn_locationURI.getSelection());
			}
		});

		text_locationURI = new Text(group_28, SWT.BORDER);
		GridData gd_text_locationURI = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_locationURI.widthHint = 209;
		text_locationURI.setLayoutData(gd_text_locationURI);
		text_locationURI.setEnabled(false);

		btn_OCSPURI = new Button(group_28, SWT.CHECK);
		GridData gd_btn_OCSPURI = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_OCSPURI.heightHint = 39;
		gd_btn_OCSPURI.widthHint = 125;
		btn_OCSPURI.setLayoutData(gd_btn_OCSPURI);
		btn_OCSPURI.setText("OCSPURI");
		btn_OCSPURI.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				text_OCSPURI.setEnabled(btn_OCSPURI.getSelection());
			}
		});
		text_OCSPURI = new Text(group_28, SWT.BORDER);
		GridData gd_text_OCSPURI = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_OCSPURI.widthHint = 210;
		text_OCSPURI.setLayoutData(gd_text_OCSPURI);
		text_OCSPURI.setEnabled(false);

	}

	// 为了查看方便
	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		container.setLayout(gl_composite);

		panel_pubInfo.panel_pubInfo(container);
		
		return container;

	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_pubInfo window = new Panel_pubInfo();
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
