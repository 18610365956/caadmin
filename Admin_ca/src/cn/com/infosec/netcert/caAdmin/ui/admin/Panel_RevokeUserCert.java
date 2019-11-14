package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**   
 * @Description 用户证书作废
 * @Author 江岩    
 * @Time 2019-08-28 14:43
 */
public class Panel_RevokeUserCert extends ApplicationWindow {
	private Text text_certDN;
	private Text text_desc;
	private String certSN, certDN;
	private static ResourceBundle l = Env.getLanguage();
	private FileLogger log = FileLogger.getLogger(this.getClass());

	public Panel_RevokeUserCert(String certSN, String certDN) {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.certSN = certSN;
		this.certDN = certDN;
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		Label lbl_certDN = new Label(container, SWT.RIGHT);
		lbl_certDN.setBounds(20, 21, 56, 17);

		lbl_certDN.setText(l.getString("CertDN") + ":");

		text_certDN = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		text_certDN.setBounds(82, 18, 283, 23);
		text_certDN.setText(certDN);

		Label lbl_revokeReason = new Label(container, SWT.RIGHT);
		lbl_revokeReason.setBounds(20, 61, 56, 17);
		lbl_revokeReason.setText(l.getString("revokeReason") + ":");

		final Combo combo_revokeReason = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		combo_revokeReason.setBounds(82, 58, 284, 25);
		combo_revokeReason.add("1.keyCompromise(密钥泄露)");
		combo_revokeReason.add("2.cACompromise(CA密钥泄露)");
		combo_revokeReason.add("3.affiliationChanged(证书主体的从属关系改变)");
		combo_revokeReason.add("4.superseded(证书已被代替)");
		combo_revokeReason.add("5.cessationOfOperation(证书已经不再被需要)");
		combo_revokeReason.add("6.privilegeWithdrawn(收回权限)");
		combo_revokeReason.select(0);

		Label lbl_reasonDesc = new Label(container, SWT.RIGHT);
		lbl_reasonDesc.setBounds(20, 98, 56, 17);
		lbl_reasonDesc.setText(l.getString("reasonDesc") + ":");

		text_desc = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text_desc.setBounds(82, 95, 283, 48);
		text_desc.setTextLimit(120);

		Button btn_ok = new Button(container, SWT.NONE);
		btn_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Properties p = new Properties();
				p.setProperty(PropertiesKeysRes.CERTSN, certSN);
				String reasonCode = String.valueOf(combo_revokeReason.getSelectionIndex() + 1);
				if (combo_revokeReason.getSelectionIndex() == 5) {
					reasonCode = "7";
				}
				p.setProperty(PropertiesKeysRes.REVOKEREASON, reasonCode);
				p.setProperty(PropertiesKeysRes.REVOKEREASONDESC, text_desc.getText());
				try {
					Env.client.sendRequest("REVOKECERT", p);
					MessageBox mb = new MessageBox(getShell(), SWT.NONE);
					mb.setMessage(l.getString("Notice_succ_revokeCert"));
					mb.open();
					close();
				} catch (ServerException se) {
					log.errlog("Revoke cert fail", se);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(
							l.getString("Notice_fail_revokeCert") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
					mb.open();
				} catch (Exception ee) {
					log.errlog("Revoke cert fail", ee);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_revokeCert"));
					mb.open();
				}
			}
		});
		btn_ok.setBounds(174, 155, 80, 27);
		btn_ok.setText(l.getString("OK"));
		btn_ok.setFocus();

		Button btn_cancle = new Button(container, SWT.NONE);
		btn_cancle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		btn_cancle.setBounds(285, 155, 80, 27);
		btn_cancle.setText(l.getString("cancle"));
		getShell().setDefaultButton(btn_ok);
		return container;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("userCertRevoke"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	@Override
	protected Point getInitialSize() {
		return new Point(406, 239);
	}
}
