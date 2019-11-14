package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.io.FileOutputStream;
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
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;
import cn.com.infosec.util.Base64;

/**   
 * @Description 重获RA证书
 * @Author 江岩    
 * @Time 2019-08-26 17:56
 */
public class Panel_GetRACert extends ApplicationWindow {
	private String adminName;
	public String selectedTempName, baseDN;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	private Text text_adminName;

	private Text text_csr;
	private Text text_validTimeLen;

	/**
	 * 构造方法
	 */
	public Panel_GetRACert(String adminName) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.adminName = adminName;
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
		GridLayout gridLayout = new GridLayout(4, false);
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
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_text.widthHint = 224;
		text_adminName.setLayoutData(gd_text);
		text_adminName.setText(adminName);

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblNewLabel_1.setText("CSR:");

		text_csr = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_text_1.heightHint = 74;
		gd_text_1.widthHint = 224;
		text_csr.setLayoutData(gd_text_1);

		Label lbl_validTime = new Label(container, SWT.NONE);
		GridData gd_lbl_validTime = new GridData(SWT.RIGHT);
		gd_lbl_validTime.widthHint = 81;
		gd_lbl_validTime.horizontalAlignment = SWT.RIGHT;
		lbl_validTime.setLayoutData(gd_lbl_validTime);
		lbl_validTime.setText(l.getString("validityDate") + ":");

		text_validTimeLen = new Text(container, SWT.BORDER);
		GridData gd_text_2 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_2.widthHint = 80;
		text_validTimeLen.setTextLimit(3);
		text_validTimeLen.setLayoutData(gd_text_2);

		final Combo combo_unit = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_combo.widthHint = 85;
		combo_unit.setLayoutData(gd_combo);
		combo_unit.add(l.getString("day"));
		combo_unit.add(l.getString("month"));
		combo_unit.add(l.getString("year"));
		combo_unit.select(0);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
				Button btn_OK = new Button(container, SWT.NONE);
				GridData gd_btn_OK = new GridData(SWT.CENTER);
				gd_btn_OK.horizontalSpan = 2;
				gd_btn_OK.horizontalAlignment = SWT.RIGHT;
				gd_btn_OK.widthHint = 81;
				btn_OK.setText(l.getString("OK"));
				btn_OK.setLayoutData(gd_btn_OK);
				btn_OK.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						String adminName = text_adminName.getText().trim();
						String validTime = text_validTimeLen.getText();
						String csr_RA = text_csr.getText();
						if (validTime == null || validTime.length() == 0) {
							MessageBox mb = new MessageBox(getShell());
							mb.setMessage(l.getString("Notice_null_validTime"));
							mb.open();
							return;
						}
						if (csr_RA == null || csr_RA.length() <= 0) {
							MessageBox mb = new MessageBox(getShell());
							mb.setMessage(l.getString("Notice_null_csr"));
							mb.open();
							return;
						}
						try {
							Properties p = new Properties();
							p.setProperty(PropertiesKeysRes.ADMIN_USERNAME, adminName);
							p.setProperty(PropertiesKeysRes.ADMIN_VALIDATELEN, validTime);
							p.setProperty(PropertiesKeysRes.VALIDITYLEN_UNIT, combo_unit.getText());
							p.setProperty(PropertiesKeysRes.PUBLICKEY, csr_RA);
							Response resp = Env.client.sendRequest("REQUESTRACERT", p);
							FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
							fd.setFilterExtensions(new String[] { "*.cer" });
							fd.setFileName(adminName);
							String f = fd.open();
							if (f != null) {
								FileOutputStream fos = new FileOutputStream(f);
								fos.write(Base64.decode(resp.getP().getProperty(PropertiesKeysRes.P7DATA)));
								fos.close();
							}
							close();
						} catch (ServerException se) {
							log.errlog("Requset RA Cert fail", se);
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_fail_requestAdminCert") + "[" + se.getErrorNum() + "]:"
									+ se.getErrorMsg());
							mb.open();
						} catch (Exception ee) {
							log.errlog("Requset RA Cert fail", ee);
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_fail_requestAdminCert"));
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
	 * @Time 2019-06-04 20:27
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("re_getAdminCert"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}
}
