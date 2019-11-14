package cn.com.infosec.netcert.caAdmin.ui.login;

import java.text.SimpleDateFormat;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Env.ALG;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.CertContainer;
import cn.com.infosec.netcert.framework.crypto.impl.ukey.CspCertInfo;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.utils.DNItemReverseUtil;

/**
 * 选择登陆证书
 * @Author 江岩
 * @Time 2019-06-04 20:36
 */
public class Panel_ChooseCert extends ApplicationWindow {
	private Table tbl_login_cert;
	private CertContainer[] cc;
	private CspCertInfo[] cspList;
	public CertContainer chooseCert;
	public CspCertInfo chooseCsp;
	public String subject;

	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * 构造方法 sm2
	 * @param (CertContainer[])
	 * @wbp.parser.constructor
	 */
	public Panel_ChooseCert(CertContainer[] cc) {
		super(null);
		setShellStyle(SWT.MIN | SWT.RESIZE); // 支持最小化 和 拖动
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.cc = cc;
	}

	/**
	 * 构造方法 rsa
	 * @param (CspCertInfo[])
	 */
	public Panel_ChooseCert(CspCertInfo[] cerList) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.cspList = cerList;

	}

	/**
	 * 视图页面绘画
	 * @Author 江岩
	 * @Time 2019-06-04 20:37
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		tbl_login_cert = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tbl_login_cert = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_tbl_login_cert.heightHint = 119;
		tbl_login_cert.setLayoutData(gd_tbl_login_cert);
		tbl_login_cert.setHeaderVisible(true);
		tbl_login_cert.setLinesVisible(true);

		TableColumn tblclmn_sn = new TableColumn(tbl_login_cert, SWT.NONE);
		tblclmn_sn.setWidth(100);
		tblclmn_sn.setText(l.getString("SN"));

		TableColumn tblclmn_dn = new TableColumn(tbl_login_cert, SWT.NONE);
		tblclmn_dn.setWidth(153);
		tblclmn_dn.setText(l.getString("subject"));

		TableColumn tblclmn_issuer = new TableColumn(tbl_login_cert, SWT.NONE);
		tblclmn_issuer.setWidth(152);
		tblclmn_issuer.setText(l.getString("issuer"));

		TableColumn tblclmn_date = new TableColumn(tbl_login_cert, SWT.NONE);
		tblclmn_date.setWidth(157);
		tblclmn_date.setText(l.getString("validityDate"));

		TableColumn tblclmn_container = new TableColumn(tbl_login_cert, SWT.NONE);
		tblclmn_container.setWidth(0);
		tblclmn_container.setText(l.getString("container"));

		if (ALG.SM2 == Env.alg) {
			addData();
		} else {
			addRSAData();
		}

		Button btn_chooseCert = new Button(container, SWT.NONE);
		GridData gd_btn_chooseCert = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_chooseCert.widthHint = 69;
		btn_chooseCert.setText(l.getString("OK"));
		btn_chooseCert.setLayoutData(gd_btn_chooseCert);
		btn_chooseCert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = tbl_login_cert.getSelection();
				if (items.length == 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_not_chooseCert"));
					mb.open();
					return;
				}
				if (ALG.SM2 == Env.alg) {
					for (int i = 0; i < cc.length; i++) {
						if (items[0].getText(4).equalsIgnoreCase(cc[i].getContainerName())) {
							chooseCert = cc[i];
							String subjectDN = items[0].getText(1);
							int beginIndex = subjectDN.indexOf("cn=");
							if (beginIndex == -1) { beginIndex = subjectDN.indexOf("CN="); }
							int endIndex = subjectDN.indexOf(",");
							if (endIndex > beginIndex) {
								subject = subjectDN.substring(beginIndex+3, endIndex);
							} else {
								subject = subjectDN.substring(beginIndex+3);
							}
						}
					}
				} else {
					for (int i = 0; i < cspList.length; i++) {
						if (items[0].getText(4).equalsIgnoreCase(cspList[i].container)) {
							chooseCsp = cspList[i];
							String subjectDN = items[0].getText(1);
							int beginIndex = subjectDN.indexOf("cn=");
							if (beginIndex == -1) { beginIndex = subjectDN.indexOf("CN="); }
							int endIndex = subjectDN.indexOf(",");
							if (endIndex > beginIndex) {
								subject = subjectDN.substring(beginIndex+3, endIndex);
							} else {
								subject = subjectDN.substring(beginIndex+3);
							}
						}
					}
				}
				close();
			}
		});
		getShell().setDefaultButton(btn_chooseCert);
		return container;
	}

	/**
	 * 添加 国密 证书信息到 视图中
	 * @Author 江岩
	 * @Time 2019-07-08 18:26
	 * @version 1.1
	 * @add 通过 根证书 颁发者 DN 过滤无效证书  
	 */
	private void addData() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < cc.length; i++) {
			String[] d = new String[5];
			d[0] = cc[i].getCert().getSerialNumber().toString(16).toUpperCase();
			try {
				d[1] = DNItemReverseUtil.bigEnding(cc[i].getCert().getSubjectDN().getName());
				d[2] = DNItemReverseUtil.bigEnding(cc[i].getCert().getIssuerDN().getName());
			} catch (Exception ee) {
				log.errlog("DN bigEnding", ee);
				MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
				mb.setMessage(l.getString("Notice_fail_DNItemReverse"));
				mb.open();
			}
			d[3] = df.format(cc[i].getCert().getNotAfter());
			d[4] = cc[i].getContainerName();
			TableItem item = new TableItem(tbl_login_cert, SWT.NONE);
			item.setText(d);
		}
	}

	/**
	 * 添加 RSA 证书信息到视图中
	 * @Author 江岩
	 * @Time 2019-06-04 20:38
	 * @version 1.0
	 * @Add  07/22  
	 */
	private void addRSAData() {
		for (int i = 0; i < cspList.length; i++) {
			String[] d = new String[5];
			d[0] = cspList[i].sn;
			try {
				d[1] = DNItemReverseUtil.bigEnding(cspList[i].subject);
				d[2] = DNItemReverseUtil.bigEnding(cspList[i].issuer);
			} catch (Exception ee) {
				log.errlog("DN bigEnding", ee);
				MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
				mb.setMessage(l.getString("Notice_fail_DNItemReverse"));
				mb.open();
			}
			d[3] = cspList[i].notAfter;
			d[4] = cspList[i].container;
			TableItem item = new TableItem(tbl_login_cert, SWT.NONE);
			item.setText(d);
		}
	}

	/**
	 * 视图标题栏命名
	 * @param (Shell)
	 * @Author 江岩
	 * @Time 2019-06-04 20:39
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("chooseCert"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

}
