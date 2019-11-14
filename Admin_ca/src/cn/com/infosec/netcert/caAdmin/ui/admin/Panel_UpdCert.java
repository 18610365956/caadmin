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

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**
 * 更新证书
 * 
 * @Author 江岩
 * @Time 2019-06-04 20:57
 */
public class Panel_UpdCert extends ApplicationWindow {
	private Text txt_uname;
	private Text txt_dn;
	private Combo validUnit;
	private Spinner valid;
	private Properties data;
	private Text text;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * 构造方法
	 */
	public Panel_UpdCert(Properties data) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.data = data;
	}

	/**
	 * 视图页面绘画
	 * @Author 江岩
	 * @Time 2019-06-04 20:58
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.marginTop = 10;
		gridLayout.marginBottom = 10;
		gridLayout.marginLeft = 10;
		gridLayout.marginRight = 45;
		gridLayout.verticalSpacing = 15;
		container.setLayout(gridLayout);

		Label lbl_username = new Label(container, SWT.NONE);
		lbl_username.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_username.setAlignment(SWT.RIGHT);
		lbl_username.setText(l.getString("username") + ":");

		txt_uname = new Text(container, SWT.BORDER);
		GridData gd_txt_uname = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_txt_uname.widthHint = 165;
		txt_uname.setLayoutData(gd_txt_uname);
		txt_uname.setEnabled(false);
		txt_uname.setText(data.getProperty(PropertiesKeysRes.USERNAME));

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setAlignment(SWT.RIGHT);
		lblNewLabel_1.setText(l.getString("templateName") + ":");

		text = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_text.widthHint = 165;
		text.setLayoutData(gd_text);
		text.setEnabled(false);
		text.setText(data.getProperty(PropertiesKeysRes.TEMPLATENAME));

		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setAlignment(SWT.RIGHT);
		lblNewLabel_2.setText(l.getString("subject") + ":");

		txt_dn = new Text(container, SWT.BORDER);
		GridData gd_txt_dn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_txt_dn.widthHint = 165;
		txt_dn.setLayoutData(gd_txt_dn);
		txt_dn.setEnabled(false);
		txt_dn.setText(data.getProperty(PropertiesKeysRes.SUBJECTDN));

		Label lbl_valid = new Label(container, SWT.NONE);
		GridData gd_lbl_valid = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_valid.widthHint = 55;
		lbl_valid.setLayoutData(gd_lbl_valid);
		lbl_valid.setAlignment(SWT.RIGHT);
		lbl_valid.setText(l.getString("validity") + ":");

		valid = new Spinner(container, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_valid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_valid.widthHint = 51;
		valid.setLayoutData(gd_valid);
		valid.setMaximum(99);
		valid.setMinimum(1);

		validUnit = new Combo(container, SWT.READ_ONLY);
		GridData gd_validUnit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_validUnit.widthHint = 57;
		validUnit.setLayoutData(gd_validUnit);
		validUnit.add(l.getString("day"));
		validUnit.add(l.getString("month"));
		validUnit.add(l.getString("year"));
		validUnit.select(2);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Button btnReqCert = new Button(container, SWT.NONE);
		GridData gd_btnReqCert = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
		gd_btnReqCert.widthHint = 70;
		btnReqCert.setLayoutData(gd_btnReqCert);
		btnReqCert.setText(l.getString("updateCert"));
		btnReqCert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.CERTSN, data.getProperty(PropertiesKeysRes.CERTSN));
					p.setProperty(PropertiesKeysRes.VALIDITYLEN, valid.getText().trim());
					switch (validUnit.getSelectionIndex()) {
					case 0:
						p.setProperty(PropertiesKeysRes.VALIDITYLEN_UNIT, "d");
						break;
					case 1:
						p.setProperty(PropertiesKeysRes.VALIDITYLEN_UNIT, "m");
						break;
					case 2:
						p.setProperty(PropertiesKeysRes.VALIDITYLEN_UNIT, "y");
						break;
					}
					Response resp = Env.client.sendRequest("UPDATECERT", p);
					String ref = resp.getP().getProperty(PropertiesKeysRes.REFNO);
					String auth = resp.getP().getProperty(PropertiesKeysRes.AUTHCODE);

					FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
					fd.setFilterExtensions(new String[] { "*.txt" });
					fd.setFileName("Ref_AuthCode");
					String f = fd.open();
					if (f != null) {
						FileWriter fw = new FileWriter(f);
						fw.write(txt_uname.getText().trim());
						fw.write(Utils.newLine);
						fw.write(l.getString("refno") + ":" + ref + Utils.newLine);
						fw.write(l.getString("authcode") + ":" + auth);
						fw.close();
					}
					close();
				} catch (ServerException se) {
					log.errlog("Update cert fail", se);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(
							l.getString("Notice_fail_updateCert") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
					mb.open();
				} catch (Exception ee) {
					log.errlog("Update cert fail", ee);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_updateCert"));
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
	 * @Time 2019-06-04 20:58
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("updateCert"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

}
