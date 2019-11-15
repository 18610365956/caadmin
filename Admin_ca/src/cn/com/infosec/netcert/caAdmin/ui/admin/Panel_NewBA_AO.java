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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**
 * 新建 BA AO 管理员
 * @Author 江岩
 * @Time 2019-06-04 20:49
 */
public class Panel_NewBA_AO extends ApplicationWindow {
	private Text txt_userName;
	private Text txt_email;
	private Text txt_tel;
	private Text txt_memo;
	private Spinner certValid;
	private Combo certValidUnit;

	private String currRole;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * 构造方法
	 * @parma String
	 */
	public Panel_NewBA_AO(String role) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.currRole = role;
	}

	/**
	 * 视图页面绘画
	 * 
	 * @Author 江岩
	 * @Time 2019-06-04 20:50
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.marginLeft = 15;
		gridLayout.marginTop = 30;
		gridLayout.marginRight = 45;
		gridLayout.marginBottom = 20;
		gridLayout.verticalSpacing = 20;
		container.setLayout(gridLayout);
		Label lbl_uname = new Label(container, SWT.NONE);
		GridData gd_lbl_uname = new GridData(SWT.RIGHT);
		gd_lbl_uname.horizontalAlignment = SWT.RIGHT;
		lbl_uname.setLayoutData(gd_lbl_uname);
		lbl_uname.setAlignment(SWT.RIGHT);
		lbl_uname.setText(l.getString("username") + ":");

		txt_userName = new Text(container, SWT.BORDER);
		GridData gd_txt_userName = new GridData(SWT.LEFT);
		gd_txt_userName.horizontalSpan = 2;
		gd_txt_userName.widthHint = 228;
		txt_userName.setTextLimit(20);
		txt_userName.setLayoutData(gd_txt_userName);

		Label label = new Label(container, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label.setText("*");

		Label lbl_email = new Label(container, SWT.NONE);
		GridData gd_lbl_email = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_email.widthHint = 59;
		lbl_email.setLayoutData(gd_lbl_email);
		lbl_email.setAlignment(SWT.RIGHT);
		lbl_email.setText(l.getString("email") + ":");

		txt_email = new Text(container, SWT.BORDER);
		GridData gd_txt_email = new GridData(SWT.LEFT);
		gd_txt_email.horizontalSpan = 2;
		gd_txt_email.widthHint = 227;
		txt_email.setTextLimit(30);
		txt_email.setLayoutData(gd_txt_email);
		new Label(container, SWT.NONE);

		Label lbl_tel = new Label(container, SWT.NONE);
		GridData gd_lbl_tel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_tel.widthHint = 51;
		lbl_tel.setLayoutData(gd_lbl_tel);
		lbl_tel.setAlignment(SWT.RIGHT);
		lbl_tel.setText(l.getString("phone") + ":");

		txt_tel = new Text(container, SWT.BORDER);
		GridData gd_txt_tel = new GridData(SWT.LEFT);
		gd_txt_tel.horizontalSpan = 2;
		gd_txt_tel.widthHint = 225;
		txt_tel.setTextLimit(15);
		txt_tel.setLayoutData(gd_txt_tel);
		new Label(container, SWT.NONE);

		Label lbl_valid = new Label(container, SWT.NONE);
		lbl_valid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_valid.setAlignment(SWT.RIGHT);
		lbl_valid.setText(l.getString("validityDate") + ":");

		certValid = new Spinner(container, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_certValid = new GridData(SWT.LEFT);
		gd_certValid.widthHint = 47;
		certValid.setLayoutData(gd_certValid);
		certValid.setMaximum(99);
		certValid.setMinimum(1);
		certValid.setIncrement(1);

		certValidUnit = new Combo(container, SWT.READ_ONLY);
		GridData gd_certValidUnit = new GridData(SWT.LEFT);
		gd_certValidUnit.widthHint = 48;
		certValidUnit.setLayoutData(gd_certValidUnit);
		certValidUnit.add(l.getString("day"));
		certValidUnit.add(l.getString("month"));
		certValidUnit.add(l.getString("year"));
		certValidUnit.select(2);
		new Label(container, SWT.NONE);

		Label lbl_memo = new Label(container, SWT.NONE);
		lbl_memo.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lbl_memo.setAlignment(SWT.RIGHT);
		lbl_memo.setText(l.getString("memo") + ":");

		txt_memo = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gridDatamemo = new GridData();
		gridDatamemo.horizontalSpan = 2;
		gridDatamemo.heightHint = 61;
		gridDatamemo.widthHint = 205;
		txt_memo.setTextLimit(120);
		txt_memo.setLayoutData(gridDatamemo);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Button btn_ok = new Button(container, SWT.NONE);
		GridData gd_btn_ok = new GridData(SWT.CENTER);
		gd_btn_ok.horizontalAlignment = SWT.RIGHT;
		gd_btn_ok.horizontalSpan = 2;
		gd_btn_ok.widthHint = 80;
		btn_ok.setLayoutData(gd_btn_ok);
		btn_ok.setText(l.getString("OK"));
		new Label(container, SWT.NONE);
		btn_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = txt_userName.getText();
				String email = txt_email.getText();
				String tel = txt_tel.getText().trim();
				String memo = txt_memo.getText();
				if (name == null || name.length() == 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_null_username"));
					mb.open();
					return;
				}
				if (email != null && email.length() != 0) {
					if (!Utils.checkEmail(email)) {
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_error_email"));
						mb.open();
						return;
					}
				} else {
					email = " ";
				}
				if (tel != null && tel.length() != 0) {
					if (!Utils.checkTel(tel)) {
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_error_phone"));
						mb.open();
						return;
					}
				} else {
					tel = " ";
				}
				if (memo == null || memo.length() <= 0) {
					memo = " ";
				}
				Properties p = new Properties();
				p.setProperty(PropertiesKeysRes.ADMIN_USERNAME, name);
				p.setProperty(PropertiesKeysRes.ADMIN_VALIDATELEN, certValid.getText().trim());
				String unit = "d";
				switch (certValidUnit.getSelectionIndex()) {
				case 1:
					unit = "m";
					break;
				case 2:
					unit = "y";
					break;
				}
				p.setProperty(PropertiesKeysRes.VALIDITYLEN_UNIT, unit);
				p.setProperty(PropertiesKeysRes.ADMIN_EMAIL, email);
				p.setProperty(PropertiesKeysRes.ADMIN_TELEPHONE, tel);
				p.setProperty(PropertiesKeysRes.ADMIN_REMARK, memo);

				String reqType = "REGISTERBA";
				if ("AA".equalsIgnoreCase(currRole)) {
					reqType = "REGISTERAO";
				}
				try {
					Response resp = Env.client.sendRequest(reqType, p);
					FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
					fd.setFilterExtensions(new String[] { "*.txt" });
					fd.setFileName("Ref_AuthCode");
					String f = fd.open();
					if (f != null) {
						FileWriter fw = new FileWriter(f);
						fw.write(name);
						fw.write(Utils.newLine);
						fw.write(l.getString("refno") + ":" + resp.getP().getProperty(PropertiesKeysRes.REFNO));
						fw.write(Utils.newLine);
						fw.write(l.getString("authcode") + ":" + resp.getP().getProperty(PropertiesKeysRes.AUTHCODE));
						fw.close();
					}
					close();
				} catch (ServerException se) {
					log.errlog(reqType +" fail", se);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(
							l.getString("Notice_fail_newAdmin") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
					mb.open();
				} catch (Exception ee) {
					log.errlog(reqType +" fail", ee);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_newAdmin"));
					mb.open();
				}
			}
		});
		return container;
	}

	/**
	 * 视图标题栏命名
	 * 
	 * @param (Shell)
	 * @Author 江岩
	 * @Time 2019-06-04 20:50
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if ("SA".equalsIgnoreCase(currRole)) {
			shell.setText(l.getString("newBA"));
		} else if ("AA".equalsIgnoreCase(currRole)) {
			shell.setText(l.getString("newAO"));
		}
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

}
