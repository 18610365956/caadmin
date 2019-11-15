package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;
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
 * 申请证书
 * @Author 江岩
 * @Time 2019-06-04 20:55
 */
public class Panel_ReqCert extends ApplicationWindow {
	private Text txt_uname;
	private Text txt_subjectDN;
	private Combo combo_tmplName, validUnit;
	private Spinner valid;
	private Map<String, List<String>> templateListMap;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();

	// 申请证书
	public Panel_ReqCert(Map<String, List<String>> templateListMap) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.templateListMap = templateListMap;
	}

	/**
	 * 视图页面绘画
	 * @Author 江岩
	 * @Time 2019-06-04 20:56
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(null);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(10, 28, 74, 17);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setText(l.getString("username") + ":");

		txt_uname = new Text(container, SWT.BORDER);
		txt_uname.setBounds(89, 25, 172, 23);
		txt_uname.setTextLimit(20);

		Label label = new Label(container, SWT.NONE);
		label.setBounds(267, 28, 14, 17);
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label.setText("*");

		Label lbl_template = new Label(container, SWT.NONE);
		lbl_template.setBounds(20, 67, 64, 17);
		lbl_template.setAlignment(SWT.RIGHT);
		lbl_template.setText(l.getString("templateName") + ":");

		combo_tmplName = new Combo(container, SWT.READ_ONLY);
		combo_tmplName.setBounds(89, 63, 172, 25);
		
		final Label lbl_notice = new Label(container, SWT.NONE);
		lbl_notice.setBounds(267, 67, 27, 17);
		lbl_notice.setImage(new Image(getShell().getDisplay(), "res/tip.png"));

		for (String s : templateListMap.keySet()) {
			combo_tmplName.add((String) s);
		}
		if (combo_tmplName.getItemCount() != 0) {
			combo_tmplName.select(0);
			StringBuffer sb = new StringBuffer();
			List<String> list = templateListMap.get(combo_tmplName.getText().trim());
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i) + Utils.newLine);
			}
			String notice = l.getString("Notice_SupportBaseDN") + ":" + Utils.newLine + sb.toString();
			lbl_notice.setToolTipText(notice);
		}

		combo_tmplName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StringBuffer sb = new StringBuffer();
				List<String> list = templateListMap.get(combo_tmplName.getText().trim());
				for (int i = 0; i < list.size(); i++) {
					sb.append(list.get(i) + Utils.newLine);
				}
				String notice = l.getString("Notice_SupportBaseDN") + ":" + Utils.newLine + sb.toString();
				lbl_notice.setToolTipText(notice);
			}
		});
		Label lbl_subject = new Label(container, SWT.NONE);
		lbl_subject.setBounds(20, 106, 64, 17);
		lbl_subject.setAlignment(SWT.RIGHT);
		lbl_subject.setText(l.getString("subject") + ":");

		txt_subjectDN = new Text(container, SWT.BORDER);
		txt_subjectDN.setBounds(89, 103, 172, 23);
		txt_subjectDN.setTextLimit(200);

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(267, 106, 14, 17);
		label_1.setText("*");
		label_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));

		Label lbl_valid = new Label(container, SWT.NONE);
		lbl_valid.setBounds(10, 145, 74, 17);
		lbl_valid.setAlignment(SWT.RIGHT);
		lbl_valid.setText(l.getString("validity") + ":");

		valid = new Spinner(container, SWT.BORDER | SWT.READ_ONLY);
		valid.setBounds(89, 142, 74, 23);
		valid.setMaximum(99);
		valid.setMinimum(1);

		validUnit = new Combo(container, SWT.READ_ONLY);
		validUnit.setBounds(174, 142, 71, 25);
		validUnit.add(l.getString("day"));
		validUnit.add(l.getString("month"));
		validUnit.add(l.getString("year"));
		validUnit.select(2);

		Button btnReqCert = new Button(container, SWT.NONE);
		btnReqCert.setBounds(174, 181, 88, 27);
		btnReqCert.setText(l.getString("op_applyCert"));
		btnReqCert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = txt_uname.getText().trim();
				String subjectDN = txt_subjectDN.getText().trim();
				if (name == null || name.length() == 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_null_username"));
					mb.open();
					return;
				}
				if (subjectDN == null || subjectDN.length() == 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_null_subject"));
					mb.open();
					return;
				}
				String uuid;
				try {
					Properties p = new Properties();
					Response resp = null;
					if (Env.IS_USER_AUTH_BY_CA()) {
						p.setProperty(PropertiesKeysRes.USERNAME, name);
						resp = Env.client.sendRequest("SEARCHUSER", p);
						Properties[] ps = resp.getItemData();
						if (ps.length == 0) {
							p.clear();
							p.setProperty(PropertiesKeysRes.USERNAME, name);
							resp = Env.client.sendRequest("APPLYUSER", p);
							uuid = resp.getP().getProperty(PropertiesKeysRes.UUID);
						} else {
							uuid = ps[0].getProperty(PropertiesKeysRes.UUID);
						}
					} else {
						uuid = name;
					}
					p.clear();
					p.setProperty(PropertiesKeysRes.UUID, uuid);
					p.setProperty(PropertiesKeysRes.TEMPLATENAME, combo_tmplName.getText());
					p.setProperty(PropertiesKeysRes.SUBJECTDN, subjectDN);
					p.setProperty(PropertiesKeysRes.VALIDITYLEN, valid.getText());

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
					resp = Env.client.sendRequest("REQUESTCERT", p);
					String ref = resp.getP().getProperty(PropertiesKeysRes.REFNO);
					String auth = resp.getP().getProperty(PropertiesKeysRes.AUTHCODE);
					FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
					fd.setFilterExtensions(new String[] { "*.txt" });	
					fd.setFileName("Ref_AuthCode");
					String f = fd.open();
					if (f != null) {
						FileWriter fw = new FileWriter(f);
						fw.write(name);
						fw.write(Utils.newLine);
						fw.write(l.getString("refno") + ":" + ref);
						fw.write(Utils.newLine);
						fw.write(l.getString("authcode") + ":" + auth);
						fw.close();
					}
					MessageBox mb = new MessageBox(getShell(), SWT.NONE);
					mb.setMessage(l.getString("Notice_succ_applyCert"));
					mb.open();
					close();
				} catch (ServerException se) {
					log.errlog("Request cert fail", se);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(
							l.getString("Notice_fail_applyCert") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
					mb.open();
				} catch (Exception ee) {
					log.errlog("Request cert fail", ee);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_applyCert"));
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
	 * @Time 2019-06-04 20:56
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("applyCert"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}
	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(317, 274);
	}

}
