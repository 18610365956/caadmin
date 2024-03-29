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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import cn.com.infosec.netcert.caAdmin.ui.template.Add_CustomExtend;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

public class Panel_customExtend extends ApplicationWindow {

	// 签名模板属性
	public Table table_customExtend;
	private Panel_customExtend panel_diyExtend;
	
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_customExtend() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		panel_diyExtend = this;
	}

	// 封装增量CRL设置信息
	public ArrayList<Extension> packageTempInfo() {
		ArrayList<Extension> customExtensions = new ArrayList<Extension>();
		for (TableItem item : table_customExtend.getItems()) {	
			ArrayList<ExtensionEntry> exList_diyOID = new ArrayList<ExtensionEntry>();
			ExtensionEntry entry_diyOID = new ExtensionEntry();
			Extension ex_diyOID = new Extension();
			entry_diyOID.setName(item.getText(0));
			ex_diyOID.setOID(item.getText(1));
			entry_diyOID.setEncoding(item.getText(2));
			if (l.getString("true").equalsIgnoreCase(item.getText(3))) {
				ex_diyOID.setIscritical(true);
			} else {
				ex_diyOID.setIscritical(false);
			}
			ex_diyOID.setDatasource("CA");
			if (l.getString("true").equalsIgnoreCase(item.getText(5))) {
				ex_diyOID.setIsmust(true);
			} else {
				ex_diyOID.setIsmust(false);
			}
			entry_diyOID.setValue(item.getText(6));
			exList_diyOID.add(entry_diyOID);
			ex_diyOID.setExtensionEntrys(exList_diyOID);
		}
		return customExtensions;
	}

	// 加载密钥用法
	public void loadCertTemplateInfo(ArrayList<Extension> customerS) {
		for (Extension customer : customerS) {
			List<ExtensionEntry> cuEntryS = customer.getExtensionEntrys();
			for (ExtensionEntry ex : cuEntryS) {
				TableItem item = new TableItem(table_customExtend, SWT.NONE);
				String flag_1 = l.getString("false"), flag_2 = l.getString("false");
				if (customer.isIscritical()) {
					flag_1 = l.getString("true");
				}
				if (customer.isIsmust()) {
					flag_2 = l.getString("true");
				}
				item.setText(new String[] { ex.getName(), customer.getOID(), ex.getEncoding(), flag_1,
						customer.getDatasource(), flag_2, ex.getValue() });
			}
		}
	}
	
	// 增量CRL视图
	protected void panel_diyExtend(final Composite composite_31) {

		Group group_31 = new Group(composite_31, SWT.NONE);
		GridData gd_group_31 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_31.widthHint = 500;
		gd_group_31.heightHint = 493;
		group_31.setLayoutData(gd_group_31);

		group_31.setText(l.getString("customExt"));
		GridLayout gl_group_31 = new GridLayout(4, false);
		gl_group_31.horizontalSpacing = 15;
		gl_group_31.verticalSpacing = 20;

		group_31.setLayout(gl_group_31);

		table_customExtend = new Table(group_31, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table_diyExtend = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_table_diyExtend.heightHint = 303;
		gd_table_diyExtend.widthHint = 460;
		gd_table_diyExtend.horizontalSpan = 4;
		table_customExtend.setLayoutData(gd_table_diyExtend);

		table_customExtend.setHeaderVisible(true);
		table_customExtend.setLinesVisible(true);

		TableColumn tblclmnNewColumn_30 = new TableColumn(table_customExtend, SWT.NONE);
		tblclmnNewColumn_30.setWidth(70);
		tblclmnNewColumn_30.setText(l.getString("extName"));

		TableColumn tblclmnNewColumn_31 = new TableColumn(table_customExtend, SWT.NONE);
		tblclmnNewColumn_31.setWidth(46);
		tblclmnNewColumn_31.setText("OID");

		TableColumn tblclmnNewColumn_32 = new TableColumn(table_customExtend, SWT.NONE);
		tblclmnNewColumn_32.setWidth(58);
		tblclmnNewColumn_32.setText(l.getString("encodeMethod"));

		TableColumn tblclmnNewColumn_33 = new TableColumn(table_customExtend, SWT.NONE);
		tblclmnNewColumn_33.setWidth(78);
		tblclmnNewColumn_33.setText(l.getString("critical"));

		TableColumn tblclmnNewColumn_4 = new TableColumn(table_customExtend, SWT.NONE);
		tblclmnNewColumn_4.setWidth(69);
		tblclmnNewColumn_4.setText(l.getString("extValSource"));

		TableColumn tblclmnNewColumn_5 = new TableColumn(table_customExtend, SWT.NONE);
		tblclmnNewColumn_5.setWidth(97);
		tblclmnNewColumn_5.setText(l.getString("writeMust"));

		TableColumn tblclmnNewColumn_6 = new TableColumn(table_customExtend, SWT.NONE);
		tblclmnNewColumn_6.setWidth(62);
		tblclmnNewColumn_6.setText(l.getString("extValue"));
		new Label(group_31, SWT.NONE);

		Button btn_add = new Button(group_31, SWT.NONE);
		GridData gd_btnNewButton_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_2.widthHint = 82;
		btn_add.setLayoutData(gd_btnNewButton_2);
		btn_add.setText(l.getString("add"));
		btn_add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TableItem item = new TableItem(table_diyExtend,SWT.NONE);
				Add_CustomExtend add_DiyExtend = new Add_CustomExtend(table_customExtend, true);
				add_DiyExtend.setBlockOnOpen(true);
				add_DiyExtend.open();
			}
		});
		Button btn_delete = new Button(group_31, SWT.NONE);
		GridData gd_btnNewButton_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_3.widthHint = 71;
		btn_delete.setLayoutData(gd_btnNewButton_3);
		btn_delete.setText(l.getString("delete"));
		btn_delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table_customExtend.getSelection();
				if (items.length == 0) {
					MessageBox messageBox = new MessageBox(composite_31.getShell(), SWT.NONE);
					messageBox.setMessage(l.getString("Notice_not_chooseCustomExt"));
					messageBox.open();
					return;
				}
				table_customExtend.remove(table_customExtend.getSelectionIndex());
			}
		});
		Button btnNewButton_4 = new Button(group_31, SWT.NONE);
		GridData gd_btnNewButton_4 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_4.widthHint = 63;
		btnNewButton_4.setLayoutData(gd_btnNewButton_4);
		btnNewButton_4.setText(l.getString("modify"));
		btnNewButton_4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table_customExtend.getSelection();
				if (items.length == 0) {
					MessageBox mb = new MessageBox(composite_31.getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_not_chooseCustomExt"));
					mb.open();
					return;
				}
				Add_CustomExtend add_DiyExtend = new Add_CustomExtend(table_customExtend, false);
				add_DiyExtend.setBlockOnOpen(true);
				add_DiyExtend.open();
			}
		});

		Label lblNewLabel_2 = new Label(group_31, SWT.NONE);
		GridData gd_lblNewLabel_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_2.widthHint = 227;
		lblNewLabel_2.setLayoutData(gd_lblNewLabel_2);
		lblNewLabel_2.setText("");
		new Label(group_31, SWT.NONE);
		new Label(group_31, SWT.NONE);
		new Label(group_31, SWT.NONE);
	}

	// 为了查看方便
	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		container.setLayout(gl_composite);

		panel_diyExtend.panel_diyExtend(container);
		
		return container;

	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_customExtend window = new Panel_customExtend();
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
