package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.encry;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_CRLPublish_enc extends ApplicationWindow {
	
	private Table table_261_enc;
	private Text text_basicPub_enc;
	private Combo combo_CRLType_enc;
	private Button btn_CRLPubIscritical_enc, btn_add_261_enc, btn_delete_262_enc, btn_addDN_enc;

	private static ResourceBundle l = Env.getLanguage();
	
	/**
	 * Create the application window.
	 */
	public Panel_CRLPublish_enc() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}


	// CRL 发布点视图
	protected void panel_CRLPublish_enc(Composite comp_CRLPublish_enc) {
		Group group_261 = new Group(comp_CRLPublish_enc, SWT.NONE);
		GridData gd_group_261 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_group_261.widthHint = 500;
		gd_group_261.heightHint = 493;
		group_261.setLayoutData(gd_group_261);
		group_261.setText(l.getString("CRLPublishPoint"));
		GridLayout gl_group_261 = new GridLayout(4, false);
		gl_group_261.marginTop = 10;
		gl_group_261.marginBottom = 10;
		gl_group_261.marginLeft = 10;
		gl_group_261.marginRight = 10;
		gl_group_261.horizontalSpacing = 20;
		gl_group_261.verticalSpacing = 10;
		group_261.setLayout(gl_group_261);

		btn_CRLPubIscritical_enc = new Button(group_261, SWT.CHECK);
		GridData gd_btn_CRLPubIscritical_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btn_CRLPubIscritical_enc.widthHint = 79;
		btn_CRLPubIscritical_enc.setLayoutData(gd_btn_CRLPubIscritical_enc);
		btn_CRLPubIscritical_enc.setText(l.getString("critical"));
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		Label label_3 = new Label(group_261, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_label_3.widthHint = 480;
		label_3.setLayoutData(gd_label_3);

		Label lblNewLabel = new Label(group_261, SWT.NONE);
		lblNewLabel.setText(l.getString("type") + ":");

		combo_CRLType_enc = new Combo(group_261, SWT.NONE);
		GridData gd_combo_CRLType_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_CRLType_enc.widthHint = 55;
		combo_CRLType_enc.setLayoutData(gd_combo_CRLType_enc);
		combo_CRLType_enc.add("DIR");
		combo_CRLType_enc.add("URI");
		combo_CRLType_enc.setText("DIR");

		btn_addDN_enc = new Button(group_261, SWT.CHECK);
		GridData gd_btn_addDN_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btn_addDN_enc.widthHint = 197;
		btn_addDN_enc.setLayoutData(gd_btn_addDN_enc);
		btn_addDN_enc.setText(l.getString("addSystemDN"));

		Label lblNewLabel_1 = new Label(group_261, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblNewLabel_1.setText(l.getString("basicPublishPoint") + ":");

		text_basicPub_enc = new Text(group_261, SWT.BORDER);
		GridData gd_text_basicPub_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_basicPub_enc.widthHint = 143;
		text_basicPub_enc.setLayoutData(gd_text_basicPub_enc);

		btn_add_261_enc = new Button(group_261, SWT.NONE);
		GridData gd_btn_add_261_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add_261_enc.widthHint = 70;
		btn_add_261_enc.setLayoutData(gd_btn_add_261_enc);
		btn_add_261_enc.setText(l.getString("add"));
		btn_add_261_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String basic_Pub_enc = text_basicPub_enc.getText().trim();
				if (basic_Pub_enc == null || basic_Pub_enc.length() == 0) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_null_basicPublishPoint"));
					messageBox.open();
					return;
				}
				TableItem it = new TableItem(table_261_enc, SWT.NONE);
				String isAppendDN = l.getString("no");
				if (btn_addDN_enc.getSelection()) {
					isAppendDN = l.getString("yes");
				}
				it.setText(new String[] { combo_CRLType_enc.getText(), isAppendDN, text_basicPub_enc.getText().trim(), });
			}
		});
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		table_261_enc = new Table(group_261, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table_261_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_table_261_enc.widthHint = 455;
		gd_table_261_enc.heightHint = 271;
		table_261_enc.setLayoutData(gd_table_261_enc);
		table_261_enc.setHeaderVisible(true);
		table_261_enc.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table_261_enc, SWT.NONE);
		tblclmnNewColumn.setWidth(109);
		tblclmnNewColumn.setText(l.getString("type"));

		TableColumn tblclmnNewColumn_1 = new TableColumn(table_261_enc, SWT.NONE);
		tblclmnNewColumn_1.setWidth(131);
		tblclmnNewColumn_1.setText(l.getString("addSystemDN"));

		TableColumn tblclmnNewColumn_2 = new TableColumn(table_261_enc, SWT.NONE);
		tblclmnNewColumn_2.setWidth(186);
		tblclmnNewColumn_2.setText(l.getString("basicPublishPoint"));
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		btn_delete_262_enc = new Button(group_261, SWT.NONE);
		GridData gd_btn_delete_262_enc = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_delete_262_enc.widthHint = 85;
		btn_delete_262_enc.setLayoutData(gd_btn_delete_262_enc);
		btn_delete_262_enc.setText(l.getString("delete"));
		btn_delete_262_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table_261_enc.getSelection().length == -1) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.NONE);
					messageBox.setMessage(l.getString("Notice_not_chooseCRL"));
					messageBox.open();
					return;
				}
				table_261_enc.remove(table_261_enc.getSelectionIndex());
			}
		});
	}
	
	// 封装CRL发布点设置信息
	public Extension packageTempInfo() {
		TableItem[] items = table_261_enc.getItems();
		if (items.length > 0) {
			Extension ex_CRLPub = new Extension();
			List<ExtensionEntry> exList_CRLPub = new ArrayList<ExtensionEntry>();
			for (TableItem item : items) {
				ExtensionEntry entry_CRLPub = new ExtensionEntry();
				entry_CRLPub.setName(item.getText(0));
				if (l.getString("true").equalsIgnoreCase(item.getText(1))) {
					entry_CRLPub.setAppendbasedn("true");
				} else {
					entry_CRLPub.setAppendbasedn("false");
				}
				entry_CRLPub.setValue(item.getText(2));

				exList_CRLPub.add(entry_CRLPub);
			}
			ex_CRLPub.setOID("2.5.29.31");
			ex_CRLPub.setIscritical(btn_CRLPubIscritical_enc.getSelection());
			ex_CRLPub.setExtensionEntrys(exList_CRLPub);
			return ex_CRLPub;
		} else {
			return null;
		}
	}

	// 封装 CRL发布点信息
	public void loadCertTemplateInfo(Extension stand) {
		btn_CRLPubIscritical_enc.setSelection(stand.isIscritical());
		List<ExtensionEntry> exList = stand.getExtensionEntrys();
		for (ExtensionEntry ex : exList) {
			TableItem item = new TableItem(table_261_enc, SWT.NONE);
			String isAddDN = "";
			if ("true".equalsIgnoreCase(ex.getAppendbasedn())) {
				isAddDN = l.getString("true");
			} else {
				isAddDN = l.getString("false");
			}
			item.setText(new String[] { ex.getName(), isAddDN, ex.getValue() });
		}
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

		this.panel_CRLPublish_enc(container);

		return container;

	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_CRLPublish_enc window = new Panel_CRLPublish_enc();
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
