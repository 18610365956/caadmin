package cn.com.infosec.netcert.caAdmin.ui.login;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import cn.com.infosec.netcert.framework.log.FileLogger;

/**
 * CA信息配置视图
 * @Author 江岩
 * @Time 2019-06-04 20:40
 */
public class Panel_Config extends ApplicationWindow {
	private Text host, port, caCerFile, timeout;
	private Button chkSSL;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * 构造方法
	 */
	public Panel_Config() {
		super(null);
		setShellStyle(SWT.MIN | SWT.RESIZE); // 支持最小化 和 拖动
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
	}

	/**
	 * 视图页面绘画
	 * @Author 江岩
	 * @Time 2019-06-04 20:40
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_container = new GridLayout(5, false);
		gl_container.verticalSpacing = 15;
		gl_container.horizontalSpacing = 8;
		gl_container.marginTop = 25;
		gl_container.marginLeft = 25;
		gl_container.marginRight = 10;
		container.setLayout(gl_container);

		Label lbl_host = new Label(container, SWT.NONE);
		lbl_host.setAlignment(SWT.RIGHT);
		lbl_host.setText(l.getString("host") + ":");

		host = new Text(container, SWT.BORDER);
		GridData gd_host = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_host.widthHint = 173;
		host.setTextLimit(15);
		host.setLayoutData(gd_host);
		new Label(container, SWT.NONE);

		Label lbl_port = new Label(container, SWT.NONE);
		lbl_port.setAlignment(SWT.RIGHT);
		lbl_port.setText(l.getString("port") + ":");

		port = new Text(container, SWT.BORDER);
		GridData gd_port = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_port.widthHint = 80;
		port.setTextLimit(5);
		port.setLayoutData(gd_port);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText(l.getString("timeout") + ":");

		timeout = new Text(container, SWT.BORDER);
		GridData gd_text_timeout = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_timeout.widthHint = 52;
		timeout.setTextLimit(3);
		timeout.setLayoutData(gd_text_timeout);
		timeout.setToolTipText("");
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText("(" + l.getString("seconds") + ")");
		new Label(container, SWT.NONE);

		chkSSL = new Button(container, SWT.CHECK);
		chkSSL.setText("SSL");

		Label lbl_caCerFile = new Label(container, SWT.NONE);
		lbl_caCerFile.setAlignment(SWT.RIGHT);
		lbl_caCerFile.setText(l.getString("CACert") + ":");

		caCerFile = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_caCerFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_caCerFile.widthHint = 208;
		caCerFile.setLayoutData(gd_caCerFile);
		caCerFile.setText("");

		Button btnBrowse = new Button(container, SWT.NONE);
		GridData gd_btnBrowse = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBrowse.widthHint = 70;
		btnBrowse.setLayoutData(gd_btnBrowse);
		btnBrowse.setText(l.getString("browse") + "...");
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
				fd.setFilterExtensions(new String[] { "*.cer", "*.*" });
				String f = fd.open();
				if (f != null) {
					caCerFile.setText(f);
				}
			}
		});
		new Label(container, SWT.NONE);

		Button btnOk = new Button(container, SWT.NONE);
		GridData gd_btnOk = new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1);
		gd_btnOk.widthHint = 67;
		btnOk.setLayoutData(gd_btnOk);
		btnOk.setText(l.getString("OK"));
		new Label(container, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String hostS = host.getText().trim();
				String portS = port.getText().trim();
				String timeoutS = timeout.getText().trim();
				String caCerS = caCerFile.getText().trim();
				if (!Utils.checkIp(hostS)) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_error_IPAddress"));
					mb.open();
					return;
				}
				int result = Utils.checkPort(portS);
				if (result == 1) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_error_port"));
					mb.open();
					return;
				} else if (result == 2) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_portRange"));
					mb.open();
					return;
				}
				if (timeoutS == null || timeoutS.length() == 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_null_timeout"));
					mb.open();
					return;
				}
				try {
					int timeInt = Integer.valueOf(timeoutS);
					if (timeInt < 30 || timeInt > 300) {
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_error_timeoutRange"));
						mb.open();
						return;
					}
				} catch (NumberFormatException e1) {
					log.errlog("String_to_int", e1);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_error_timeoutFormat"));
					mb.open();
					return;
				}
				if (caCerS == null || caCerS.length() == 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_not_chooseCert"));
					mb.open();
					return;
				}
				try {
					Properties p = new Properties();
					p.setProperty("host", hostS);
					p.setProperty("port", portS);
					p.setProperty("timeout", timeoutS);
					p.setProperty("useSSL", String.valueOf(chkSSL.getSelection()));
					p.setProperty("caCer", caCerS);
					FileOutputStream os = new FileOutputStream(Env.ConfigFile);
					p.store(os, null);
					os.close();
				} catch (FileNotFoundException ex) {
					log.errlog("load configFile", ex);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_not_foundConfigFile"));
					mb.open();
				} catch (IOException e1) {
					log.errlog("write data to configFile", e1);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_saveFile"));
					mb.open();
				}
				try {
					Env.initCAConfig(); // 立即加载
					close();
				} catch (Exception ex) {
					log.errlog("init Admin config", ex);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(ex.getMessage());
					mb.open();
				}
			}
		});
		if (Env.isInited()) {
			host.setText(Env.host);
			port.setText(String.valueOf(Env.port));
			timeout.setText(String.valueOf(Env.timeout));
			chkSSL.setSelection(Env.isSSL);
			caCerFile.setText(Env.caCerFile);
		}
		getShell().setDefaultButton(btnOk);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		return container;
	}

	/**
	 * 视图标题命名
	 * @Author 江岩
	 * @Time 2019-06-04 20:41
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("config"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

}
