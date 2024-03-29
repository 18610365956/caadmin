package cn.com.infosec.netcert.caAdmin.ui.admin;

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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.asn1.x509.X509Name;
import cn.com.infosec.netcert.caAdmin.ui.login.Panel_MessageDialog;
import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Env.ALG;
import cn.com.infosec.netcert.caAdmin.utils.Env.RETURNTYPE;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.crypto.CryptoException;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.NeedPinException;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.UsbKeyCSPImpl;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.UsbKeySKFImpl;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;
import cn.com.infosec.util.Base64;

/**
 * 证书下载：单证 双证 
 * @Author 江岩
 * @Time 2019-06-04 20:46
 */
public class Panel_DownCert extends ApplicationWindow {
	private Text ref, auth, p10;
	private Button r_csr, r_key, btn_p7;

	private Combo combo_devName, combo_devSN, combo_len, combo_alg;
	private UsbKeySKFImpl userSkf;
	private UsbKeyCSPImpl csp;
	private Properties pro;
	private String[] cc;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private Label lblSN;
	private Button btn_single, btn_double, btn_DownCert;
	private String keyDriverLibPath;
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * 构造方法
	 */
	public Panel_DownCert() {
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
	 * @Author 江岩
	 * @Time 2019-06-04 20:47
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());
		// 下载凭证
		Group group = new Group(container, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		fd_group.bottom = new FormAttachment(0, 70);
		group.setLayoutData(fd_group);

		Label lbl_ref = new Label(group, SWT.NONE);
		lbl_ref.setBounds(23, 24, 49, 17);
		lbl_ref.setAlignment(SWT.RIGHT);
		lbl_ref.setText(l.getString("refno") + " :");

		ref = new Text(group, SWT.BORDER);
		ref.setBounds(78, 21, 132, 23);
		ref.setTextLimit(20);

		Label lbl_auth = new Label(group, SWT.NONE);
		lbl_auth.setBounds(245, 24, 70, 17);
		lbl_auth.setAlignment(SWT.RIGHT);
		lbl_auth.setText(l.getString("authcode") + ":");

		auth = new Text(group, SWT.BORDER);
		auth.setBounds(321, 21, 132, 23);
		auth.setTextLimit(20);

		// 单双证
		Group group_1 = new Group(container, SWT.NONE);
		fd_group.right = new FormAttachment(group_1, 0, SWT.RIGHT);
		group_1.setText(l.getString("type"));
		FormData fd_group_1 = new FormData();
		fd_group_1.top = new FormAttachment(group, 17);
		fd_group_1.right = new FormAttachment(100, -28);
		fd_group_1.left = new FormAttachment(0, 10);
		group_1.setLayoutData(fd_group_1);

		btn_single = new Button(group_1, SWT.RADIO);
		btn_single.setBounds(41, 26, 97, 17);
		btn_single.setText(l.getString("singleCert"));
		btn_single.setSelection(true);

		btn_double = new Button(group_1, SWT.RADIO);
		btn_double.setBounds(41, 56, 61, 17);
		btn_double.setText(l.getString("doubleCert"));

		// 下载方式
		Group grpDownType = new Group(container, SWT.NONE);
		fd_group_1.bottom = new FormAttachment(grpDownType, -6);

		Label lblNewLabel = new Label(group_1, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(146, 56, 122, 17);
		lblNewLabel.setText(l.getString("Symmetric_pro_alg") + ":");

		combo_alg = new Combo(group_1, SWT.NONE | SWT.READ_ONLY);
		combo_alg.setBounds(274, 53, 88, 25);
		combo_alg.setEnabled(false);

		grpDownType.setText(l.getString("downloadType"));
		FormData fd_grpDownType = new FormData();
		fd_grpDownType.top = new FormAttachment(0, 176);
		fd_grpDownType.left = new FormAttachment(0, 10);
		fd_grpDownType.right = new FormAttachment(100, -28);
		grpDownType.setLayoutData(fd_grpDownType);

		Group grpCsr = new Group(grpDownType, SWT.NONE);
		grpCsr.setBounds(10, 21, 459, 180);

		r_csr = new Button(grpCsr, SWT.RADIO);
		r_csr.setBounds(10, 21, 423, 17);
		r_csr.setText(l.getString("Input_P10"));

		p10 = new Text(grpCsr, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		p10.setBounds(30, 44, 376, 90);
		p10.setEnabled(false);

		btn_p7 = new Button(grpCsr, SWT.CHECK);
		btn_p7.setBounds(10, 153, 161, 17);
		btn_p7.setText(l.getString("Cert_return_Chain"));
		btn_p7.setEnabled(false);
		btn_p7.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				r_csr.setSelection(true);
			}
		});
		Group grpKey = new Group(grpDownType, SWT.NONE);
		grpKey.setBounds(10, 207, 459, 166);

		r_key = new Button(grpKey, SWT.RADIO);
		r_key.setBounds(10, 22, 218, 17);
		r_key.setText(l.getString("Gen_P10_local"));

		r_key.setSelection(true);

		Label lblUsbkey = new Label(grpKey, SWT.NONE | SWT.RIGHT);
		lblUsbkey.setBounds(10, 48, 90, 17);
		lblUsbkey.setAlignment(SWT.RIGHT);
		lblUsbkey.setText(l.getString("driverName") + ":");

		combo_devName = new Combo(grpKey, SWT.READ_ONLY);
		combo_devName.setBounds(106, 45, 195, 25);
		combo_devName.add(l.getString("Please_choose_keyFile"));

		if (ALG.SM2 == Env.alg) {
			@SuppressWarnings("unchecked")
			Enumeration<String> keyList = (Enumeration<String>) pro.propertyNames();

			while (keyList.hasMoreElements()) {
				combo_devName.add(keyList.nextElement());
			}
			if (combo_devName.getItemCount() != 0) {
				combo_devName.select(0);
				keyDriverLibPath = pro.getProperty(combo_devName.getText());
			}

			lblSN = new Label(grpKey, SWT.NONE | SWT.RIGHT);
			lblSN.setBounds(10, 85, 90, 17);
			lblSN.setAlignment(SWT.RIGHT);
			lblSN.setText(l.getString("SN") + ":");

			combo_devSN = new Combo(grpKey, SWT.READ_ONLY);
			combo_devSN.setBounds(106, 82, 195, 25);

			combo_devName.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					if (ALG.SM2 == Env.alg) {
						combo_devSN.removeAll();
						if (combo_devName.getSelectionIndex() != 0) {
							keyDriverLibPath = pro.getProperty(combo_devName.getText());
							try {
								String[] devItems = UsbKeySKFImpl.enumDev(keyDriverLibPath);
								for (String s : devItems) {
									combo_devSN.add(s);
								}
								if (devItems.length != 0) {
									combo_devSN.select(0);
								}
							} catch (CryptoException e) {
								log.errlog("Enum device fail", e);
								MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
								mb.setMessage(l.getString("Notice_fail_enumDev"));
								mb.open();
							}
						}
					}
				}
			});

			Label lbl_keyLength = new Label(grpKey, SWT.NONE | SWT.RIGHT);
			lbl_keyLength.setBounds(37, 125, 63, 17);
			lbl_keyLength.setText(l.getString("keyLength") + ":");

			combo_len = new Combo(grpKey, SWT.NONE | SWT.READ_ONLY);
			combo_len.setBounds(106, 122, 88, 25);
		} else {
			String[] pro = UsbKeyCSPImpl.listProvider();
			for (String s : pro) {
				combo_devName.add(s);
			}
			if (pro.length != 0) {
				combo_devName.select(0);
			}
			Label lbl_keyLength = new Label(grpKey, SWT.NONE | SWT.RIGHT);
			lbl_keyLength.setBounds(35, 89, 61, 17);
			lbl_keyLength.setText(l.getString("keyLength") + ":");

			combo_len = new Combo(grpKey, SWT.NONE | SWT.READ_ONLY);
			combo_len.setBounds(106, 86, 88, 25);
		}
		if (ALG.SM2 == Env.alg) {
			combo_len.add("256");
			combo_len.setText("256");
			combo_alg.add("SM4");
			combo_alg.setText("SM4");
		} else {
			combo_len.add("1024");
			combo_len.add("2048");
			combo_len.setText("1024");
			combo_alg.add("RC4");
			combo_alg.setText("RC4");
		}

		r_csr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				r_key.setSelection(false);
				combo_devName.setText("");
				combo_devName.setEnabled(false);
				if (ALG.SM2 == Env.alg) {
					combo_devSN.setText("");
					combo_devSN.setEnabled(false);
				}
				combo_len.setEnabled(false);
				p10.setEnabled(true);
				btn_p7.setEnabled(true);
			}
		});
		r_key.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				r_csr.setSelection(false);
				p10.setText("");
				p10.setEnabled(false);
				btn_p7.setSelection(false);
				btn_p7.setEnabled(false);
				combo_devName.setEnabled(true);
				combo_len.setEnabled(true);
				if (ALG.SM2 == Env.alg) {
					combo_devSN.setEnabled(true);
				} else {
				}
			}
		});

		btn_single.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				btn_single.setSelection(true);
				btn_double.setSelection(false);
				combo_alg.setEnabled(false);
				if (ALG.RSA == Env.alg) {
					r_csr.setEnabled(true);
					//p10.setEnabled(true);
					//btn_p7.setEnabled(true);
				}
			}
		});
		btn_double.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				btn_single.setSelection(false);
				btn_double.setSelection(true);
				combo_alg.setEnabled(true);
				if (ALG.RSA == Env.alg) {
					r_csr.setSelection(false);
					r_csr.setEnabled(false);
					p10.setText("");
					p10.setEnabled(false);
					btn_p7.setSelection(false);
					btn_p7.setEnabled(false);
					r_key.setSelection(true);
					combo_devName.setEnabled(true);
					combo_len.setEnabled(true);
				}
			}
		});

		btn_DownCert = new Button(container, SWT.NONE);
		fd_grpDownType.bottom = new FormAttachment(100, -68);
		FormData fd_btnDownCert = new FormData();
		fd_btnDownCert.top = new FormAttachment(grpDownType, 20);
		fd_btnDownCert.right = new FormAttachment(100, -39);
		fd_btnDownCert.bottom = new FormAttachment(100, -21);
		fd_btnDownCert.left = new FormAttachment(100, -119);
		btn_DownCert.setLayoutData(fd_btnDownCert);
		btn_DownCert.setText(l.getString("downloadCert"));

		btn_DownCert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String refS = ref.getText().trim();
				String authS = auth.getText().trim();
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
				String keyLen = null;
				String containerOfKey = null;
				Properties p = new Properties();
				p.setProperty(PropertiesKeysRes.REFNO, refS);
				p.setProperty(PropertiesKeysRes.AUTHCODE, authS);

				if (r_csr.getSelection()) { //CSR
					String _p10 = p10.getText().trim();
					p.setProperty(PropertiesKeysRes.PUBLICKEY, _p10);
					if (btn_p7.getSelection()) {
						p.setProperty(PropertiesKeysRes.RETURNTYPE, RETURNTYPE.P7CERT.name()); //  申请证书返回P7
					}
					if (!btn_single.getSelection()) {
						if (ALG.SM2 == Env.alg) {
							p.setProperty(PropertiesKeysRes.KMC_KEYLEN, "256"); // 双证要有 算法和密钥长度参数
							p.setProperty(PropertiesKeysRes.RETSYMALG, combo_alg.getText().trim());
						}
						
					}
					Response resp = null;
					try {
						resp = Env.client.sendRequest("DOWNCERT", p);
						Panel_DownloadCertResult panel_showCert = new Panel_DownloadCertResult(resp, Env.alg.toString(),
								btn_single.getSelection(), btn_p7.getSelection());
						panel_showCert.setBlockOnOpen(true);
						panel_showCert.open();
						close();
						return;
					} catch (Exception e2) {
						log.errlog("Download cert fail", e2);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_downloadCert"));
						mb.open();
						return;
					}
				}

				if (!r_csr.getSelection()) { // r_key 产生 csr			
					String devName = combo_devName.getText().trim();
					if (devName == null || devName.length() == 0) {
						MessageBox mb = new MessageBox(getShell());
						mb.setMessage(l.getString("Notice_null_driverName"));
						mb.open();
						return;
					}
					if (ALG.SM2 == Env.alg) {
						String devSN = combo_devSN.getText().trim();
						if (devSN == null || devSN.length() == 0) {
							MessageBox mb = new MessageBox(getShell(), SWT.ICON_WARNING);
							mb.setMessage(l.getString("Notice_null_devSN"));
							mb.open();
							return;
						}
						try {
							if (Env.getMap().containsKey(pro.getProperty(devName) + combo_devSN.getText().trim())) {
								userSkf = Env.getMap().get(pro.getProperty(devName) + combo_devSN.getText().trim());
								//System.out.println("old");
							} else {
								userSkf = new UsbKeySKFImpl(keyDriverLibPath, combo_devSN.getText().trim());
								Env.getMap().put(pro.getProperty(devName) + combo_devSN.getText().trim(), userSkf);
								//System.out.println("new");
							}
						} catch (CryptoException e3) {
							log.errlog("New SKF fail", e3);
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_fail_newSKF"));
							mb.open();
							return;
						}
						try {
							try {
								//System.out.println("download:" + userSkf.getHApp() +","+ userSkf.getHDev()); // 测试 中标环境 写用户证书失败
								cc = userSkf.genCSR(256, new X509Name("cn=test"));
							} catch (NeedPinException e1) {
								Panel_VerifyPin verifyPin = new Panel_VerifyPin();
								verifyPin.setBlockOnOpen(true);
								int w = verifyPin.open();
								if (w != 0) {
									return;
								}
								int result = userSkf.verifyPIN(verifyPin.pin);
								if (result == 0) {
									cc = userSkf.genCSR(256, new X509Name("cn=test"));
								} else {
									MessageBox mb = new MessageBox(getShell());
									mb.setMessage(l.getString("Notice_error_PIN"));
									mb.open();
									return;
								}
							}
						} catch (CryptoException e2) {
							log.errlog("Generate CSR fail", e2);
							MessageBox mb = new MessageBox(getShell());
							mb.setMessage(l.getString("Notice_fail_genCSR"));
							mb.open();
							return;
						}
					} else {
						csp = new UsbKeyCSPImpl(combo_devName.getText().trim());
						try {
							cc = csp.genP10(Integer.parseInt(combo_len.getText().trim()), "cn=test");
						} catch (CryptoException e1) {
							log.errlog("Generate CSR fail", e1);
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_fail_genCSR"));
							mb.open();
							return;
						}
					}
					keyLen = combo_len.getText().trim();
					containerOfKey = cc[0];
					p.setProperty(PropertiesKeysRes.PUBLICKEY, cc[1]);

					if (!btn_single.getSelection()) {
						p.setProperty(PropertiesKeysRes.KMC_KEYLEN, keyLen); // 双证要有 算法和密钥长度参数
						p.setProperty(PropertiesKeysRes.RETSYMALG, combo_alg.getText().trim());
						if (ALG.RSA == Env.alg) {
							try {
								p.setProperty(PropertiesKeysRes.RSA_TMP_PUB_KEY, ((UsbKeyCSPImpl) csp).genTmpKeyPair());
							} catch (CryptoException e1) {
								log.errlog("Generate TmpKeyPair fail", e1);
								MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
								mb.setMessage(l.getString("Notice_error_genTmpKeyPair"));
								mb.open();
								return;
							}
						}
					}
					Response resp = null;
					try {
						resp = Env.client.sendRequest("DOWNCERT", p);
					} catch (Exception e2) {
						log.errlog("Download cert fail", e2);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_downloadCert"));
						mb.open();
						return;
					}
					try {
						String cer = resp.getP().getProperty(PropertiesKeysRes.P7DATA);
						if (ALG.SM2 == Env.alg) {
							try {
								byte[] cerBin = Base64.decode(cer);
								try {					
									//System.out.println(containerOfKey);
									userSkf.importCert(containerOfKey, cerBin);
								} catch (NeedPinException e1) {
									Panel_VerifyPin verifyPin = new Panel_VerifyPin();
									verifyPin.setBlockOnOpen(true);
									int w = verifyPin.open();
									if (w != 0) {
										return;
									}
									int result = userSkf.verifyPIN(verifyPin.pin);
									if (result == 0) {
										//System.out.println(containerOfKey);
										userSkf.importCert(containerOfKey, cerBin);
									} else {
										MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
										mb.setMessage(l.getString("Notice_error_PIN"));
										mb.open();
										return;
									}
								}
								MessageBox mb = new MessageBox(getShell(), SWT.NONE);
								mb.setMessage(l.getString("Notice_succ_downloadSignCert"));
								mb.open();
							} catch (CryptoException ce) {
								log.errlog("Import cert fail", ce);
								MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
								mb.setMessage(l.getString("Notice_fail_downloadSignCert"));
								mb.open();
								return;
							}
							try {
								if (!btn_single.getSelection()) {
									String cer_enc = resp.getP().getProperty(PropertiesKeysRes.P7DATA_ENC);
									String encPrikeyBase64 = resp.getP().getProperty(PropertiesKeysRes.ENCPRIVATEKEY);
									if (cer_enc != null && cer_enc.length() > 0) {
										byte[] cerBin_enc = Base64.decode(cer_enc);
										byte[] encPrikeyBase64_b = Base64.decode(encPrikeyBase64);
										try {
											userSkf.importEncKeyPair(containerOfKey, cerBin_enc, encPrikeyBase64_b);
										} catch (NeedPinException e1) {
											Panel_VerifyPin verifyPin = new Panel_VerifyPin();
											verifyPin.setBlockOnOpen(true);
											int w = verifyPin.open();
											if (w != 0) {
												return;
											}
											int result = userSkf.verifyPIN(verifyPin.pin);
											if (result == 0) {
												userSkf.importEncKeyPair(containerOfKey, cerBin_enc, encPrikeyBase64_b);
											} else {
												MessageBox mb_1 = new MessageBox(getShell(), SWT.ERROR);
												mb_1.setMessage(l.getString("Notice_error_PIN"));
												mb_1.open();
												return;
											}
										}
										MessageBox mb_2 = new MessageBox(getShell(), SWT.NONE);
										mb_2.setMessage(l.getString("Notice_succ_downloadEncCert"));
										mb_2.open();
									} else {
										MessageBox mb_2 = new MessageBox(getShell(), SWT.ICON_WARNING);
										mb_2.setMessage(l.getString("Notice_not_foundEncCert"));
										mb_2.open();
										return;
									}
								}
							} catch (CryptoException ce) {
								log.errlog("Import cert fail", ce);
								MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
								mb.setMessage(l.getString("Notice_fail_downloadEncCert"));
								mb.open();
								return;
							}
						} else {
							csp.importCert(containerOfKey, cer);
							MessageBox mb = new MessageBox(getShell(), SWT.NONE);
							mb.setMessage(l.getString("Notice_succ_downloadSignCert"));
							mb.open();
							if (!btn_single.getSelection()) {
								String cer_enc = resp.getP().getProperty(PropertiesKeysRes.P7DATA_ENC);
								if (cer_enc != null && cer_enc.length() > 0) {
									String encPrikeyBase64 = resp.getP().getProperty(PropertiesKeysRes.ENCPRIVATEKEY);
									String tmpKeyEncData = resp.getP().getProperty(PropertiesKeysRes.TEMPUKEK);
									csp.importEncKeyPair(containerOfKey, cer_enc, encPrikeyBase64, tmpKeyEncData);
									MessageBox mb_1 = new MessageBox(getShell(), SWT.NONE);
									mb_1.setMessage(l.getString("Notice_succ_downloadEncCert"));
									mb_1.open();
								} else {
									MessageBox mb_2 = new MessageBox(getShell(), SWT.ICON_WARNING);
									mb_2.setMessage(l.getString("Notice_not_foundEncCert"));
									mb_2.open();
								}
							}
						}
					} catch (IOException e3) {
						log.errlog("Base64 decode fail", e3);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_Base64Decode"));
						mb.open();
						return;
					}
				}
				handleShellCloseEvent();
			}
		});
		getShell().setDefaultButton(btn_DownCert);
		return container;
	}

	/**
	 * 视图标题栏命名
	 * @param (Shell)
	 * @Author 江岩
	 * @Time 2019-06-04 20:47
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("downloadCert"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}
}
