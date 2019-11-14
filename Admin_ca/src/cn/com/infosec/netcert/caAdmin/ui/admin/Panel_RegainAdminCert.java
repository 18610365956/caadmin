package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.io.FileWriter;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**
 * 重获管理员证书
 * @Author 江岩
 * @Time 2019-06-04 20:26
 */
public class Panel_RegainAdminCert extends ApplicationWindow {
	private Text txt_validTime;
	private String adminName;
	public String selectedTempName, baseDN;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	private Text text_adminName;
	private String role;

	/**
	 * 构造方法
	 */
	public Panel_RegainAdminCert(String adminName, String role) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.adminName = adminName;
		this.role = role;
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
		gd_lbl_tempName.horizontalAlignment = SWT.RIGHT;
		lbl_tempName.setLayoutData(gd_lbl_tempName);
		lbl_tempName.setText(l.getString("admin") + ":");

		text_adminName = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1);
		gd_text.widthHint = 162;
		text_adminName.setLayoutData(gd_text);
		text_adminName.setText(adminName);

		Label lbl_validTime = new Label(container, SWT.NONE);
		lbl_validTime.setLayoutData(new GridData(SWT.RIGHT));
		lbl_validTime.setText(l.getString("validityDate"));

		txt_validTime = new Text(container, SWT.BORDER);
		GridData gd_txt_basedn = new GridData(SWT.LEFT);
		gd_txt_basedn.widthHint = 70;
		txt_validTime.setTextLimit(4);
		txt_validTime.setLayoutData(gd_txt_basedn);
		txt_validTime.setFocus();

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(l.getString("day"));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Button btn_OK = new Button(container, SWT.NONE);
		GridData gd_btn_OK = new GridData(SWT.CENTER);
		gd_btn_OK.horizontalAlignment = SWT.LEFT;
		gd_btn_OK.widthHint = 81;
		btn_OK.setText(l.getString("OK"));
		btn_OK.setLayoutData(gd_btn_OK);
		btn_OK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String validTime = txt_validTime.getText().trim();
				if (validTime == null || validTime.length() == 0) {
					MessageBox mb = new MessageBox(getShell());
					mb.setMessage(l.getString("Notice_null_validTime"));
					mb.open();
					return;
				}
				try {
					String reqType = null;
					if ("BA".equalsIgnoreCase(role)) {
						reqType = "REQUESTBACERT";
					} else if ("BO".equalsIgnoreCase(role)) {
						reqType = "REQUESTBOCERT";
					} else if ("RA".equalsIgnoreCase(role)) {
						reqType = "REQUESTRACERT";
					} else if ("AO".equalsIgnoreCase(role)) {
						reqType = "REQUESTAOCERT";
					}
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.ADMIN_USERNAME, text_adminName.getText().trim());
					p.setProperty(PropertiesKeysRes.ADMIN_VALIDATELEN, txt_validTime.getText().trim());
					Response resp = Env.client.sendRequest(reqType, p);
					Properties pro = resp.getP();
					String refNo = pro.getProperty(PropertiesKeysRes.REFNO);
					String authCode = pro.getProperty(PropertiesKeysRes.AUTHCODE);
					FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
					fd.setFilterExtensions(new String[] { "*.txt" });
					fd.setFileName("Ref_AuthCode");
					String f = fd.open();
					if (f != null) {
						FileWriter fw = new FileWriter(f);
						fw.write(adminName);
						fw.write(Utils.newLine);
						fw.write(l.getString("refno") + ":" + refNo);
						fw.write(Utils.newLine);
						fw.write(l.getString("authcode") + ":" + authCode);
						fw.close();
					}
					close();
				} catch (ServerException se) {
					log.errlog("Regain Admin Certificate fail", se);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(
							l.getString("Notice_fail_RegainAdminCert") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
					mb.open();
				} catch (Exception ee) {
					log.errlog("Regain Admin Certificate fail", ee);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_RegainAdminCert"));
					mb.open();
				}
			}
		});
		return container;
	}

	/**
	 * 视图标题栏命名
	 * @param (Shell)
	 * @Author 江岩
	 * @Time 2019-06-04 20:27
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("regainAdminCert"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}
}
