package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**   
 * 日志审计查看请求证书
 * @Author 江岩    
 * @Time 2019-07-15 15:20
 */
public class Panel_ViewReqCert extends ApplicationWindow {
	private Table table;
	private Properties properties;
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * 构造方法
	 * @param
	 */
	public Panel_ViewReqCert(Properties properties) {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		addMenuBar();
		this.properties = properties;
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		Group group = new Group(container, SWT.NONE);
		group.setBounds(10, 0, 507, 256);

		table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 20, 487, 227);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tbl_sn = new TableColumn(table, SWT.NONE);
		tbl_sn.setWidth(84);
		tbl_sn.setText(l.getString("SN"));

		TableColumn tbl_SubjectDN = new TableColumn(table, SWT.NONE);
		tbl_SubjectDN.setWidth(110);
		tbl_SubjectDN.setText(l.getString("subject"));

		TableColumn tbl_IssuerDN = new TableColumn(table, SWT.NONE);
		tbl_IssuerDN.setWidth(111);
		tbl_IssuerDN.setText(l.getString("issuer"));

		TableColumn tbl_NotBefore = new TableColumn(table, SWT.NONE);
		tbl_NotBefore.setWidth(81);
		tbl_NotBefore.setText(l.getString("notBefore"));

		TableColumn tbl_NotAfter = new TableColumn(table, SWT.NONE);
		tbl_NotAfter.setWidth(84);
		tbl_NotAfter.setText(l.getString("notAfter"));

		table.removeAll();
		String SN = properties.getProperty(PropertiesKeysRes.CERTSN);
		String SubjectDN = properties.getProperty(PropertiesKeysRes.SUBJECTDN);
		String IssuerDN = properties.getProperty("IssuerDN");
		String NotBefore = properties.getProperty(PropertiesKeysRes.NOTBEFORE);
		String NotAfter = properties.getProperty(PropertiesKeysRes.NOTAFTER);

		TableItem ti = new TableItem(table, SWT.NONE);
		SN = new BigDecimal(SN).toBigInteger().toString(16);
		ti.setText(new String[] { SN, SubjectDN, IssuerDN, NotBefore, NotAfter });

		return container;
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("ReqCertDetail"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(533, 297);
	}
}
