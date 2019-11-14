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

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplate;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_basic_Limit extends ApplicationWindow {

	// 基本限制
	private Text text_maxPathLen;
	private Button btn_useBasicLimitIscritical, btn_useCASign, btn_noLenLimit, btn_maxPathLen;

	private CertTemplate certTemplate;
	private Panel_basic_Limit panel_basic_Limit;
	
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_basic_Limit(CertTemplate certTemplate) {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		this.certTemplate = certTemplate;
	}

	// 基本限制视图
	protected void panel_basic_Limit(Composite composite_21) {

		Group group_21 = new Group(composite_21, SWT.NONE);
		GridLayout gl_group_21 = new GridLayout(2, false);
		gl_group_21.marginTop = 10;
		gl_group_21.marginBottom = 10;
		gl_group_21.marginLeft = 10;
		gl_group_21.marginRight = 10;
		gl_group_21.horizontalSpacing = 50;
		gl_group_21.verticalSpacing = 10;

		group_21.setLayout(gl_group_21);
		group_21.setText(l.getString("basic_limit"));

		GridData gd_group_21 = new GridData();
		gd_group_21.heightHint = 493;
		gd_group_21.widthHint = 500;
		group_21.setLayoutData(gd_group_21);

		btn_useCASign = new Button(group_21, SWT.CHECK);
		GridData gd_btn_useCASign = new GridData();
		gd_btn_useCASign.widthHint = 140;
		btn_useCASign.setLayoutData(gd_btn_useCASign);
		// btn_useCASign.setBounds(25, 35, 150, 21);
		btn_useCASign.setText(l.getString("useCAsign"));
		btn_useCASign.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_useCASign.getSelection();
				useCASignSelection(flag);
			}
		});

		btn_useBasicLimitIscritical = new Button(group_21, SWT.CHECK);
		GridData gd_btn_useBasicLimitIscritical = new GridData();
		gd_btn_useBasicLimitIscritical.widthHint = 120;
		btn_useBasicLimitIscritical.setLayoutData(gd_btn_useBasicLimitIscritical);
		// btn_useBasicLimitIscritical.setBounds(210, 35, 100, 21);
		btn_useBasicLimitIscritical.setText(l.getString("critical"));

		if (!certTemplate.isSignal()) {
			btn_useCASign.setEnabled(false);
			btn_useBasicLimitIscritical.setEnabled(false);
		}

		Label label = new Label(group_21, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData();
		gd_label.widthHint = 480;
		gd_label.horizontalSpan = 2;
		label.setLayoutData(gd_label);
		// label.setBounds(23, 68, 550, 2);

		Group group_211 = new Group(group_21, SWT.NONE);
		GridLayout gl_group_211 = new GridLayout(2, false);
		gl_group_211.marginTop = 10;
		gl_group_211.marginBottom = 10;
		gl_group_211.marginLeft = 10;
		gl_group_211.marginRight = 10;
		gl_group_211.verticalSpacing = 15;
		gl_group_211.horizontalSpacing = 10;
		group_211.setLayout(gl_group_211);

		GridData gd_group_211 = new GridData();
		gd_group_211.horizontalSpan = 2;
		gd_group_211.widthHint = 464;
		group_211.setLayoutData(gd_group_211);
		// group_211.setBounds(23, 75, 540, 120);
		group_211.setText(l.getString("pathLengthLimit"));

		btn_noLenLimit = new Button(group_211, SWT.RADIO);
		GridData gd_btn_noLenLimit = new GridData();
		gd_btn_noLenLimit.widthHint = 170;
		gd_btn_noLenLimit.horizontalSpan = 2;
		btn_noLenLimit.setLayoutData(gd_btn_noLenLimit);
		// btn_noLenLimit.setBounds(10, 28, 120, 21);
		btn_noLenLimit.setText(l.getString("LengthNoLimit"));
		btn_noLenLimit.setEnabled(false);
		btn_noLenLimit.setSelection(true);

		btn_maxPathLen = new Button(group_211, SWT.RADIO);
		GridData gd_btn_maxPathLen = new GridData();
		gd_btn_maxPathLen.widthHint = 220;
		btn_maxPathLen.setLayoutData(gd_btn_maxPathLen);
		// btn_maxPathLen.setBounds(10, 70, 180, 21);
		btn_maxPathLen.setText(l.getString("maxPathLength"));
		btn_maxPathLen.setEnabled(false);

		text_maxPathLen = new Text(group_211, SWT.BORDER);
		GridData gd_text_maxPathLen = new GridData();
		gd_text_maxPathLen.widthHint = 188;
		text_maxPathLen.setLayoutData(gd_text_maxPathLen);
		// text_maxPathLen.setBounds(200, 70, 130, 25);
		text_maxPathLen.setEnabled(false);

	}

	// 选中基本限制，使其他设置可选
	private void useCASignSelection(boolean flag) {
		btn_useBasicLimitIscritical.setEnabled(flag);
		btn_noLenLimit.setEnabled(flag);
		btn_maxPathLen.setEnabled(flag);
		text_maxPathLen.setEnabled(flag);
	}

	// 封装基本限制
	public Extension packageTempInfo() {
		Extension ex_basic = new Extension();
		if (btn_useCASign.getSelection()) {
			List<ExtensionEntry> exList_basic = new ArrayList<ExtensionEntry>();

			ExtensionEntry entry_CASignCert = new ExtensionEntry();
			entry_CASignCert.setName("ISCA");
			entry_CASignCert.setValue("TRUE");
			exList_basic.add(entry_CASignCert);

			if (btn_maxPathLen.getSelection()) {
				ExtensionEntry entry_maxPathLen = new ExtensionEntry();
				entry_maxPathLen.setName("MAXPATHLEN");
				entry_maxPathLen.setValue(text_maxPathLen.getText().trim());
				exList_basic.add(entry_maxPathLen);
			}
			ex_basic.setOID("2.5.29.19");
			ex_basic.setIscritical(btn_useBasicLimitIscritical.getSelection());
			ex_basic.setDatasource("CA");
			ex_basic.setIsmust(false);
			ex_basic.setExtensionEntrys(exList_basic);
		}
		return ex_basic;
	}

	// 加载基本限制
	public void loadCertTemplateInfo(Extension stand) {

		btn_useBasicLimitIscritical.setSelection(stand.isIscritical());
		List<ExtensionEntry> exList1 = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exList1) {
			if ("isca".equalsIgnoreCase(ex.getName())) {
				btn_useCASign.setSelection(Boolean.valueOf(ex.getValue()));
				useCASignSelection(Boolean.valueOf(ex.getValue()));
			}
			if ("maxPathLen".equalsIgnoreCase(ex.getName())) {
				btn_noLenLimit.setSelection(false);
				btn_maxPathLen.setSelection(true);
				text_maxPathLen.setText(ex.getValue());
			}
		}
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		container.setLayout(gl_composite_24);

		panel_basic_Limit.panel_basic_Limit(container);

		return container;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			CertTemplate certTemplate = null;
			Panel_basic_Limit window = new Panel_basic_Limit(certTemplate);
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
