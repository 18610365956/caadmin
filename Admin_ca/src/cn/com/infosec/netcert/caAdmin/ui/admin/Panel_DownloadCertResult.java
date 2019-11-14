package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**   
 * 使用 CSR下载证书时，显示下载结果
 * @Author 江岩    
 * @Time 2019-07-26 10:46
 */
public class Panel_DownloadCertResult extends ApplicationWindow {
	private Text text_signCert;
	private Text text_encCert;
	private Text text_encPriv;
	private Response resp;
	private boolean isSingle, isP7Cert;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * Create the application window.
	 */
	public Panel_DownloadCertResult(Response resp,String alg,boolean isSingle, boolean isP7Cert) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.resp = resp;
		this.isSingle = isSingle;
		this.isP7Cert = isP7Cert;
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());
		
		Group group = new Group(container, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.left = new FormAttachment(0, 10);
		fd_group.right = new FormAttachment(100, -10);
		fd_group.top = new FormAttachment(0);
		group.setLayoutData(fd_group);
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(10, 16, 122, 17);
		lblNewLabel.setText(l.getString("SignCert")+":");
		
		text_signCert = new Text(group, SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		text_signCert.setBounds(20, 39, 392, 112);
		
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setBounds(10, 153, 150, 17);
		lblNewLabel_1.setText(l.getString("EncCert")+":");
		
		text_encCert = new Text(group, SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		text_encCert.setBounds(20, 176, 392, 128);
		
		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setBounds(10, 308, 150, 17);
		lblNewLabel_2.setText(l.getString("EncPriv")+":");
		
		text_encPriv = new Text(group, SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		text_encPriv.setBounds(20, 331, 392, 112);
		
		// 加载信息
		String cer = resp.getP().getProperty(PropertiesKeysRes.P7DATA);
		if(isP7Cert) {
			text_signCert.setText("-----BEGIN PKCS7-----"+ Utils.newLine + cer + Utils.newLine + "-----END PKCS7-----");
		} else {
			text_signCert.setText("-----BEGIN CERTIFICATE-----"+ Utils.newLine + cer + Utils.newLine + "-----END CERTIFICATE-----");
		}
		if(!isSingle) {
			String cer_enc = resp.getP().getProperty(PropertiesKeysRes.P7DATA_ENC);
			text_encCert.setText("-----BEGIN CERTIFICATE-----"+ Utils.newLine + cer_enc + Utils.newLine +"-----END CERTIFICATE-----");
			
			String encPriv = resp.getP().getProperty(PropertiesKeysRes.ENCPRIVATEKEY);
			text_encPriv.setText(encPriv);
		}
		Button btn_saveFile = new Button(container, SWT.NONE);
		fd_group.bottom = new FormAttachment(100, -51);
		FormData fd_btnsaveFile = new FormData();
		fd_btnsaveFile.top = new FormAttachment(group, 14);
		fd_btnsaveFile.bottom = new FormAttachment(100, -10);
		fd_btnsaveFile.right = new FormAttachment(100, -10);
		fd_btnsaveFile.left = new FormAttachment(0, 316);
		btn_saveFile.setLayoutData(fd_btnsaveFile);
		btn_saveFile.setText(l.getString("saveFileto") + " ...");
		btn_saveFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				StringBuffer sb = new StringBuffer();		
				sb.append(l.getString("SignCert") + Utils.newLine);
				sb.append(text_signCert.getText() + Utils.newLine+Utils.newLine+Utils.newLine);
				if (!isSingle) {
					sb.append(l.getString("EncCert") + Utils.newLine);
					sb.append(text_encCert.getText() + Utils.newLine+Utils.newLine+Utils.newLine);
					
					sb.append(l.getString("EncPriv") + Utils.newLine);
					sb.append(text_encPriv.getText() + Utils.newLine+Utils.newLine+Utils.newLine);
				}			
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
				fd.setFilterExtensions(new String[] { "*.txt" });
				fd.setFileName("DownCertResult");
				String f = fd.open();
				if (f != null) {
					try {
						byte[] bs = sb.toString().getBytes();
						FileOutputStream fos = new FileOutputStream(f);
						fos.write(bs);
						fos.close();
						MessageBox mb = new MessageBox(getShell(), SWT.OK);
						mb.setMessage(l.getString("Notice_succ_saveFile"));
						mb.open();
						close();
					} catch (FileNotFoundException e1) {
						log.errlog("File not found", e1);
						MessageBox mb = new MessageBox(getShell());
						mb.setMessage(l.getString("Notice_not_foundFile"));
						mb.open();
						return;
					} catch (IOException e1) {
						log.errlog("Base64.decode fail/Save file file", e1);
						MessageBox mb = new MessageBox(getShell());
						mb.setMessage(l.getString("Notice_fail_saveFile"));
						mb.open();
						return;
					}
				}
			}
		});
		return container;
	}

	
	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("downloadCertResult"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

}
