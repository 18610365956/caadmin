package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.util.Properties;
import java.util.ResourceBundle;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**
 * 对管理员授权
 * @Author 江岩
 * @Time 2019-06-04 20:26
 */
public class Panel_AddPermission extends ApplicationWindow {
	private Text txt_basedn;
	private Combo combo_tmpName;
	private String tmpName;
	public String selectedTempName, baseDN;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * 构造方法
	 */
	public Panel_AddPermission(String tmpName) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.tmpName = tmpName;
	}

	/**
	 * 视图页面绘画
	 * @Author 江岩
	 * @Time 2019-06-04 20:26
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginLeft = 15;
		gridLayout.marginRight = 40;
		gridLayout.marginTop = 15;
		gridLayout.marginBottom = 15;
		gridLayout.verticalSpacing = 15;
		container.setLayout(gridLayout);

		Label lbl_tempName = new Label(container, SWT.NONE);
		GridData gd_lbl_tempName = new GridData(SWT.RIGHT);
		gd_lbl_tempName.horizontalAlignment = SWT.LEFT;
		lbl_tempName.setLayoutData(gd_lbl_tempName);
		lbl_tempName.setText(l.getString("templateName") + ":");

		combo_tmpName = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_tmp = new GridData(SWT.LEFT);
		gd_combo_tmp.widthHint = 206;
		combo_tmpName.setLayoutData(gd_combo_tmp);

		if (tmpName == null) {
			try {
				Response resp = Env.client.sendRequest("QUERYTEMPLATELIST", new Properties());
				Properties[] ts = resp.getItemData();
				for (Properties t : ts) {
					if ("0".equals(t.getProperty(PropertiesKeysRes.TEMPLATE_STATUS))) {
						combo_tmpName.add(t.getProperty(PropertiesKeysRes.TEMPLATENAME));
					}
				}
			} catch (ServerException se) {
				log.errlog("Query template list fail", se);
				MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
				mb.setMessage(
						l.getString("Notice_fail_queryTemplate") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
				mb.open();
			} catch (Exception ee) {
				log.errlog("Query template list fail", ee);
				MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
				mb.setMessage(l.getString("Notice_fail_queryTemplate"));
				mb.open();
			}
		} else {
			combo_tmpName.add(tmpName);
			combo_tmpName.select(0);
			combo_tmpName.setEnabled(false);
		}

		Label label = new Label(container, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label.setText("*");

		Label lbl_BaseDN = new Label(container, SWT.NONE);
		GridData gd_lbl_BaseDN = new GridData(SWT.RIGHT);
		gd_lbl_BaseDN.horizontalAlignment = SWT.LEFT;
		lbl_BaseDN.setLayoutData(gd_lbl_BaseDN);
		lbl_BaseDN.setText("BaseDN:");

		txt_basedn = new Text(container, SWT.BORDER);
		GridData gd_txt_basedn = new GridData(SWT.LEFT);
		gd_txt_basedn.widthHint = 225;
		txt_basedn.setTextLimit(150);
		txt_basedn.setLayoutData(gd_txt_basedn);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Button btn_OK = new Button(container, SWT.NONE);
		GridData gd_btn_OK = new GridData(SWT.CENTER);
		gd_btn_OK.horizontalAlignment = SWT.RIGHT;
		gd_btn_OK.widthHint = 81;
		btn_OK.setText(l.getString("OK"));
		btn_OK.setLayoutData(gd_btn_OK);
		new Label(container, SWT.NONE);
		btn_OK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String baseDN_in = txt_basedn.getText().trim();
				if (baseDN_in == null || baseDN_in.length() == 0) {
					MessageBox mb = new MessageBox(getShell());
					mb.setMessage(l.getString("Notice_null_BaseDN"));
					mb.open();
					return;
				}
				if (combo_tmpName.getText() == null || combo_tmpName.getText().trim().length() == 0) {
					MessageBox mb = new MessageBox(getShell());
					mb.setMessage(l.getString("Notice_null_tempName"));
					mb.open();
					return;
				}
				selectedTempName = combo_tmpName.getText();
				baseDN = baseDN_in;
				close();
			}
		});
		return container;
	}

	/**
	 * 视图标题栏命名
	 * 
	 * @param (Shell)
	 * @Author 江岩
	 * @Time 2019-06-04 20:27
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("add_grant"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}
}
