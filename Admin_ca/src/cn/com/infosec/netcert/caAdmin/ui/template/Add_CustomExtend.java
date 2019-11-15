package cn.com.infosec.netcert.caAdmin.ui.template;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.com.infosec.netcert.caAdmin.utils.Env;

/**
 * 添加自定义扩展视图
 * @Author 江岩    
 * @Time 2019-06-06 13:49
 */
public class Add_CustomExtend extends ApplicationWindow {

	private Text text_extendName, text_oID, text_valueRegion;
	private Combo combo_encodeType, combo_typeMatter, combo_writeMust, combo_resource;
	private Table table;
	private boolean isAdd;
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * 构造方法
	 */
	public Add_CustomExtend(Table table, boolean isAdd) {
		super(null);
		this.table = table;
		this.isAdd = isAdd;
		setShellStyle(SWT.MIN);
	}

	/**
	 * 视图页面绘画
	 * @Author 江岩      
	 * @Time 2019-06-06 13:49
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(6, false);
		gridLayout.marginBottom = 10;
		gridLayout.marginLeft = 15;
		gridLayout.marginTop = 30;
		gridLayout.marginRight = 25;
		gridLayout.verticalSpacing = 15;
		container.setLayout(gridLayout);
		
		Label lbl_extName = new Label(container, SWT.NONE);
		GridData gd_lbl_extName = new GridData(SWT.RIGHT);
		gd_lbl_extName.widthHint = 42;
		gd_lbl_extName.horizontalAlignment = SWT.RIGHT;
		lbl_extName.setLayoutData(gd_lbl_extName);
		lbl_extName.setText(l.getString("name") + ":");

		text_extendName = new Text(container, SWT.BORDER);
		GridData gridDataName = new GridData();
		gridDataName.horizontalSpan = 4;
		gridDataName.widthHint = 270;
		text_extendName.setTextLimit(120);
		text_extendName.setLayoutData(gridDataName);

		Label lbl_must = new Label(container, SWT.NONE);
		GridData gd_lbl_must = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_must.widthHint = 17;
		lbl_must.setLayoutData(gd_lbl_must);
		lbl_must.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lbl_must.setText("*");

		Label lbl_OID = new Label(container, SWT.NONE);
		GridData gd_lbl_OID = new GridData(SWT.RIGHT);
		gd_lbl_OID.horizontalAlignment = SWT.RIGHT;
		lbl_OID.setLayoutData(gd_lbl_OID);
		lbl_OID.setText("OID:");

		text_oID = new Text(container, SWT.BORDER);
		GridData gd_text_oID = new GridData(SWT.LEFT);
		gd_text_oID.horizontalSpan = 4;
		gd_text_oID.widthHint = 267;
		text_oID.setTextLimit(50);
		text_oID.setLayoutData(gd_text_oID);

		Label lbl_must2 = new Label(container, SWT.NONE);
		lbl_must2.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lbl_must2.setText("*");

		Label lbl_encodeMethod = new Label(container, SWT.NONE);
		lbl_encodeMethod.setText(l.getString("encodeMethod") + ":");
		GridData gd_lbl_encodeMethod = new GridData(SWT.LEFT);
		gd_lbl_encodeMethod.widthHint = 47;
		gd_lbl_encodeMethod.horizontalAlignment = SWT.RIGHT;
		lbl_encodeMethod.setLayoutData(gd_lbl_encodeMethod);

		combo_encodeType = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_encodeType = new GridData(SWT.LEFT);
		gd_combo_encodeType.widthHint = 74;
		combo_encodeType.setLayoutData(gd_combo_encodeType);
		combo_encodeType.add("Integer");
		combo_encodeType.add("Boolean");
		combo_encodeType.add("UTF8String");
		combo_encodeType.add("IA5String");
		combo_encodeType.add("OCTETString");
		combo_encodeType.add("BMPString");
		combo_encodeType.add("RAW");
		combo_encodeType.setText("Integer");
		new Label(container, SWT.NONE);

		Label lbl_critical = new Label(container, SWT.NONE);
		lbl_critical.setText(l.getString("critical") + ":");
		GridData gd_lbl_critical = new GridData(SWT.RIGHT);
		gd_lbl_critical.horizontalAlignment = SWT.RIGHT;
		gd_lbl_critical.widthHint = 45;
		lbl_critical.setLayoutData(gd_lbl_critical);

		combo_typeMatter = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_typeMatter = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_typeMatter.widthHint = 70;
		combo_typeMatter.setLayoutData(gd_combo_typeMatter);
		combo_typeMatter.add(l.getString("yes"));
		combo_typeMatter.add(l.getString("no"));
		combo_typeMatter.setText(l.getString("no"));
		new Label(container, SWT.NONE);

		Label lbl_extValSource = new Label(container, SWT.NONE | SWT.RIGHT);
		GridData gd_lbl_extValSource = new GridData(SWT.RIGHT);
		gd_lbl_extValSource.widthHint = 68;
		gd_lbl_extValSource.horizontalAlignment = SWT.RIGHT;
		lbl_extValSource.setLayoutData(gd_lbl_extValSource);
		lbl_extValSource.setText(l.getString("extValSource") + ":");

		combo_resource = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_resource = new GridData(SWT.LEFT);
		gd_combo_resource.widthHint = 74;
		combo_resource.setLayoutData(gd_combo_resource);
		combo_resource.add("CA");
		combo_resource.add("CLIENT");
		combo_resource.setText("CA");
		new Label(container, SWT.NONE);

		Label lbl_writeMust = new Label(container, SWT.NONE);
		GridData gd_lbl_writeMust = new GridData(SWT.RIGHT);
		gd_lbl_writeMust.widthHint = 37;
		gd_lbl_writeMust.horizontalAlignment = SWT.RIGHT;
		lbl_writeMust.setLayoutData(gd_lbl_writeMust);
		lbl_writeMust.setText(l.getString("writeMust") + ":");

		combo_writeMust = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_writeMust = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_writeMust.widthHint = 70;
		combo_writeMust.setLayoutData(gd_combo_writeMust);
		combo_writeMust.add(l.getString("yes"));
		combo_writeMust.add(l.getString("no"));
		combo_writeMust.setText(l.getString("no"));
		new Label(container, SWT.NONE);

		Label lbl_extValue = new Label(container, SWT.NONE);
		GridData gd_lbl_extValue = new GridData(SWT.RIGHT);
		gd_lbl_extValue.verticalAlignment = SWT.TOP;
		gd_lbl_extValue.widthHint = 62;
		gd_lbl_extValue.horizontalAlignment = SWT.RIGHT;
		lbl_extValue.setLayoutData(gd_lbl_extValue);
		lbl_extValue.setText(l.getString("extValue") + ":");

		text_valueRegion = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_text_valueRegion = new GridData(SWT.LEFT);
		gd_text_valueRegion.verticalSpan = 2;
		gd_text_valueRegion.horizontalSpan = 4;
		gd_text_valueRegion.widthHint = 251;
		gd_text_valueRegion.heightHint = 61;
		text_valueRegion.setLayoutData(gd_text_valueRegion);
		text_valueRegion.setBounds(100, 196, 269, 69);
		text_valueRegion.setTextLimit(120);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_1.widthHint = 92;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		lblNewLabel_1.setText("");
		new Label(container, SWT.NONE);

		Button btn_submit = new Button(container, SWT.NONE);
		GridData gd_btn_submit = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btn_submit.widthHint = 63;
		btn_submit.setLayoutData(gd_btn_submit);
		btn_submit.setBounds(180, 278, 80, 27);
		btn_submit.setText(l.getString("OK"));
		btn_submit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				String extendName = text_extendName.getText().trim();
				String extendOID = text_oID.getText().trim();
				String encodeType = combo_encodeType.getText().trim();
				String typeMatter = combo_typeMatter.getText().trim();
				String resource = combo_resource.getText().trim();
				String writeMust = combo_writeMust.getText().trim();
				String valueRegion = text_valueRegion.getText().trim();
				
				if (extendName == null || extendName.length() == 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_null_extName"));
					mb.open();
					return;
				}
				if (extendOID == null || extendOID.length() == 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_null_OID"));
					mb.open();
					return; // Notice_specifyPolicyOID
				}
				Matcher m = Pattern.compile("^[0-9.]+$").matcher(extendOID);
				if (!m.matches()) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_specifyPolicyOID"));
					mb.open();
					return;
				}
				if("CLIENT".equalsIgnoreCase(resource) && valueRegion.length() != 0){
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_error_valueRegion"));
					mb.open();
					return;
				}
				if("CA".equalsIgnoreCase(resource) && l.getString("yes").equalsIgnoreCase(writeMust) && (valueRegion == null || valueRegion.length() == 0)){
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_null_valueRegion"));
					mb.open();
					return;
				}
				// 判断是否存在
				TableItem add_item;
				TableItem[] items_selected = table.getSelection();
				if(isAdd) {  // 新增按钮触发
					TableItem[] items_all = table.getItems();
					String item_name = null;
					String item_oid = null;
					for(TableItem item : items_all) {
						item_name = item.getText(0);
						item_oid = item.getText(1);
					
						if(item_name.equals(text_extendName.getText().trim()) || item_oid.equals(text_oID.getText().trim())){
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_existed_customEx") + "!");
							mb.open();
							return;
						}
					}
					add_item = new TableItem(table, SWT.NONE);
				} else {   // 修改按钮触发
					add_item = items_selected[0];
				}

				encodeType = combo_encodeType.getText().trim();
				typeMatter = combo_typeMatter.getText().trim();
				writeMust = combo_writeMust.getText().trim();
				resource = combo_resource.getText().trim();
				valueRegion = text_valueRegion.getText().trim();
				String[] extends1 = new String[] { extendName, extendOID, encodeType, typeMatter, resource, writeMust,
						valueRegion };

				add_item.setText(extends1);
				close();
			}
		});
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Button btn_cancle = new Button(container, SWT.NONE);
		GridData gd_btn_cancle = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btn_cancle.widthHint = 74;
		btn_cancle.setLayoutData(gd_btn_cancle);
		btn_cancle.setBounds(281, 278, 80, 27);
		btn_cancle.setText(l.getString("cancle"));
		btn_cancle.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getShell().close();
			}
		});
		new Label(container, SWT.NONE);

		// 如果是编辑按钮触发，进行赋值
		if (!isAdd) {
			TableItem[] items = table.getSelection();
			if (items.length > 0) {
				if (items[0].getText(0) != null && items[0].getText(0).length() != 0) {
					text_extendName.setText(items[0].getText(0));
					text_oID.setText(items[0].getText(1));
					combo_encodeType.setText(items[0].getText(2));
					combo_typeMatter.setText(items[0].getText(3));
					combo_resource.setText(items[0].getText(4));
					combo_writeMust.setText(items[0].getText(5));
					text_valueRegion.setText(items[0].getText(6));
					text_extendName.setEnabled(false);
					text_oID.setEnabled(false);
				}
			}
		}
		return container;
	}

	/**
	 * 视图窗口标题命名
	 * @param (Shell)
	 * @Author 江岩      
	 * @Time 2019-06-06 14:08
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("addCustomExt"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}
}
