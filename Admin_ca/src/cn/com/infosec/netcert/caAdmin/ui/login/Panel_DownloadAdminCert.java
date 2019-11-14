package cn.com.infosec.netcert.caAdmin.ui.login;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
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

import cn.com.infosec.asn1.x509.X509Name;
import cn.com.infosec.netcert.caAdmin.ui.admin.Panel_VerifyPin;
import cn.com.infosec.netcert.caAdmin.utils.AdminClient;
import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Env.ALG;
import cn.com.infosec.netcert.framework.crypto.CryptoException;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.NeedPinException;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.UsbKeyCSPImpl;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.UsbKeySKFImpl;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;
import cn.com.infosec.util.Base64;

/**
 * 下载管理员证书
 * @Author 江岩
 * @Time 2019-06-04 20:29
 */
public class Panel_DownloadAdminCert extends ApplicationWindow {
	private Combo combo_driverName, combo_skf_devSN, combo_len;
	private Text txt_ref, txt_authcode;
	private UsbKeySKFImpl skf;
	private UsbKeyCSPImpl csp;
	private String[] csr;
	private String keyDriverLibPath;
	
	public static AdminClient anonymousClient;
	
	private Properties pro;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * 构造方法
	 */
	public Panel_DownloadAdminCert() {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		try {
			pro = Env.getProperties(new File(Env.keyConfigFile));
		} catch (IOException e) {
			log.errlog("Load keyConfigFile fail.", e);
			Panel_MessageDialog dialog = new Panel_MessageDialog("error", "Load keyConfigFile fail.");
			dialog.setBlockOnOpen(true);
			dialog.open();
			return;
		}
	}

	/**
	 * 视图页面绘画
	 * 
	 * @Author 江岩
	 * @Time 2019-06-04 20:30
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginTop = 30;
		gridLayout.marginLeft = 30;
		gridLayout.marginRight = 50;
		gridLayout.marginBottom = 30;
		gridLayout.verticalSpacing = 15;
		container.setLayout(gridLayout);

		Label lbl_ref = new Label(container, SWT.NONE);
		lbl_ref.setAlignment(SWT.RIGHT);
		lbl_ref.setText(l.getString("refno") + ":");

		txt_ref = new Text(container, SWT.BORDER);
		GridData gd_txt_ref = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		txt_ref.setTextLimit(20);
		gd_txt_ref.widthHint = 210;
		txt_ref.setLayoutData(gd_txt_ref);

		Label lbl_auth = new Label(container, SWT.NONE);
		lbl_auth.setAlignment(SWT.RIGHT);
		lbl_auth.setText(l.getString("authcode") + ":");

		txt_authcode = new Text(container, SWT.BORDER);
		GridData gd_txt_authcode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_authcode.widthHint = 210;
		txt_authcode.setTextLimit(20);
		txt_authcode.setLayoutData(gd_txt_authcode);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label lblUsbkey = new Label(container, SWT.NONE);
		lblUsbkey.setAlignment(SWT.RIGHT);
		lblUsbkey.setText(l.getString("driverName") + ":");

		combo_driverName = new Combo(container, SWT.READ_ONLY);
		GridData gd_combo_dev = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_dev.widthHint = 185;
		combo_driverName.setLayoutData(gd_combo_dev);
		combo_driverName.add(l.getString("Please_choose_keyFile"));
		
		if (ALG.SM2 == Env.alg) {
			@SuppressWarnings("unchecked")
			Enumeration<String> keyList = (Enumeration<String>) pro.propertyNames();
			while (keyList.hasMoreElements()) {
				combo_driverName.add(keyList.nextElement());
			}
			if(combo_driverName.getItemCount() != 0){
				combo_driverName.select(0);
				keyDriverLibPath = pro.getProperty(combo_driverName.getText());
			}
			
			Label lbl_SN = new Label(container, SWT.NONE);
			lbl_SN.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			lbl_SN.setAlignment(SWT.RIGHT);
			lbl_SN.setText(l.getString("SN") + ":");

			combo_skf_devSN = new Combo(container, SWT.READ_ONLY);
			GridData gd_combo_devSN = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_combo_devSN.widthHint = 185;
			combo_skf_devSN.setLayoutData(gd_combo_devSN);

			combo_driverName.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					if (ALG.SM2 == Env.alg) {
						combo_skf_devSN.removeAll();
						if (combo_driverName.getSelectionIndex() != 0) {
							keyDriverLibPath = pro.getProperty(combo_driverName.getText());
							try {
								String[] devItems = UsbKeySKFImpl.enumDev(keyDriverLibPath);
								for (String s : devItems) {
									combo_skf_devSN.add(s);
								}
								if(devItems.length != 0) {
									combo_skf_devSN.select(0);
								}
							} catch (CryptoException e) {
								log.errlog("Enum device fail", e);
								MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
								mb.setMessage(l.getString("Notice_fail_enumDev"));
								mb.open();
								return;
							}
						}
					}
				}
			});
		} else {
			String[] pro = UsbKeyCSPImpl.listProvider();
			for (String s : pro) {
				combo_driverName.add(s);
			}
			if(pro.length != 0){
				combo_driverName.select(0);
			}
		}
		
		Label lbl_len_enc = new Label(container, SWT.NONE);
		lbl_len_enc.setText(l.getString("keyLength") + ":");

		combo_len = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_len = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo_len.widthHint = 70;
		combo_len.setLayoutData(gd_combo_len);
		if (ALG.SM2 == Env.alg) {
			combo_len.add("256");
			combo_len.setText("256");
		} else {
			combo_len.add("1024");
			combo_len.add("2048");
			combo_len.setText("1024");
		}
		Button btn_downCert = new Button(container, SWT.NONE);
		btn_downCert.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btn_downCert.setText(l.getString("downloadCert"));
		btn_downCert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.isInited()) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_not_initAdmin"));
					mb.open();
					return;
				}
				String refS = txt_ref.getText().trim();
				String authS = txt_authcode.getText().trim();
				if (refS == null || refS.length() == 0) {
					MessageBox mb = new MessageBox(getShell());
					mb.setMessage(l.getString("Notice_null_refno"));
					mb.open();
					return;
				}
				if (authS == null || authS.length() == 0) {
					MessageBox mb = new MessageBox(getShell());
					mb.setMessage(l.getString("Notice_null_authcode"));
					mb.open();
					return;
				}
				if (ALG.SM2 == Env.alg) {
					String devSN = combo_skf_devSN.getText().trim();
					if (devSN == null || devSN.length() == 0) {
						MessageBox mb = new MessageBox(getShell());
						mb.setMessage(l.getString("Notice_null_SN"));
						mb.open();
						return;
					}
					try {
						skf = new UsbKeySKFImpl(keyDriverLibPath, devSN);
						//Env.getMap().put(pro.getProperty(combo_driverName.getText()) + devSN, skf);
						try {
							csr = skf.genCSR(256, new X509Name("cn=test"));
						} catch (NeedPinException e1) {
							Panel_VerifyPin verifyPin = new Panel_VerifyPin();
							verifyPin.setBlockOnOpen(true);
							int w = verifyPin.open();
							if (w != 0) {
								skf.release();
								return;
							}
							int result = skf.verifyPIN(verifyPin.pin);
							if (result == 0) {
								csr = skf.genCSR(256, new X509Name("cn=test"));
							}
						}
					} catch (CryptoException e1) {
						log.errlog("Generate CSR fail", e1);
						MessageBox mb = new MessageBox(getShell());
						mb.setMessage(l.getString("Notice_fail_genCSR"));
						mb.open();
						return;
					}
				} else { // RSA
					String keyLen = combo_len.getText().trim();
					if (keyLen == null || keyLen.length() == 0) {
						MessageBox mb = new MessageBox(getShell(), SWT.ICON_WARNING);
						mb.setMessage(l.getString("Notice_null_keyLength"));
						mb.open();
						return;
					}
					csp = new UsbKeyCSPImpl(combo_driverName.getText().trim());
					try {
						csr = csp.genP10(Integer.parseInt(keyLen), "cn=test");
					} catch (CryptoException e1) {
						log.errlog("generate CSR fail", e1);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_genCSR"));
						mb.open();
						return;
					}
				}
				String container = csr[0];
				String p10 = csr[1];
				Properties p = new Properties();
				p.setProperty(PropertiesKeysRes.REFNO, refS);
				p.setProperty(PropertiesKeysRes.AUTHCODE, authS);
				p.setProperty(PropertiesKeysRes.PUBLICKEY, p10);
				String cerStr = null;
				AdminClient anonyClient = null;
				try {
					anonyClient = Env.getClient(null);
					Properties resp = anonyClient.sendRequest("DOWNADMINCERT", p).getP();
					cerStr = resp.getProperty(PropertiesKeysRes.P7DATA);
				} catch (Exception ee) {
					log.errlog("download Admin Cert fail", ee);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(ee.getMessage());
					mb.open();
					if (Env.alg == ALG.SM2) {
						skf.release();
					}
					return;
				} finally {
					if (anonyClient != null) {
						anonyClient.close();
					}
				}
				try {
					byte[] b = Base64.decode(cerStr);
					if (ALG.SM2 == Env.alg) {
						try {
							skf.importCert(container, b);
						} catch (NeedPinException e1) {
							Panel_VerifyPin verifyPin = new Panel_VerifyPin();
							verifyPin.setBlockOnOpen(true);
							int w = verifyPin.open();
							if (w != 0) {
								skf.release();
								return;
							}
							int result = skf.verifyPIN(verifyPin.pin);
							if (result == 0) {
								skf.importCert(container, b);
							}
							skf.release();
						}
					} else {
						csp.importCert(csr[0], cerStr);
					}
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage(l.getString("Notice_succ_downloadCert"));
					mb.open();
					close();
				} catch (IOException e2) {
					log.errlog("Base64 decode fail", e2);
					MessageBox mb = new MessageBox(getShell());
					mb.setMessage(l.getString("Notice_fail_Base64Decode"));
					mb.open();
					if (Env.alg == ALG.SM2) {
						skf.release();
					}
					return;
				} catch (CryptoException e1) {
					log.errlog("import cert fail", e1);
					MessageBox mb = new MessageBox(getShell());
					mb.setMessage(l.getString("Notice_fail_importCert") + e1.getMessage());
					mb.open();
					if (Env.alg == ALG.SM2) {
						skf.release();
					}
					return;
				}
			}
		});
		getShell().setDefaultButton(btn_downCert);
		return container;
	}
	/**
	 * 视图标题栏命名
	 * @param (Shell)
	 * @Author 江岩
	 * @Time 2019-06-04 20:35
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("downAdminCert"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

}
