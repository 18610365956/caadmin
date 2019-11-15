package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.text.ParseException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**   
 * 审计日志详情：某条日志的申请情况
 * @Author 江岩    
 * @Time 2019-07-15 15:20
 */
public class Panel_AuditedLogDetail extends ApplicationWindow {
	private Table table;
	private Properties properties;
	private FileLogger log = FileLogger.getLogger(this.getClass());	
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * 构造方法
	 * @param
	 */
	public Panel_AuditedLogDetail(Properties properties) {
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
		container.setLayout(new FormLayout());

		Group group = new Group(container, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(0, 267);
		fd_group.right = new FormAttachment(0, 496);
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		group.setLayoutData(fd_group);
		group.setText(l.getString("AuditLogDetail"));
		group.setLayout(new GridLayout(1, false));

		table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_table.widthHint = 451;
		gd_table.heightHint = 205;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tbl_auditor = new TableColumn(table, SWT.NONE);
		tbl_auditor.setWidth(100);
		tbl_auditor.setText(l.getString("Auditor"));

		TableColumn tbl_auditTime = new TableColumn(table, SWT.NONE);
		tbl_auditTime.setWidth(143);
		tbl_auditTime.setText(l.getString("AuditTime"));

		TableColumn tbl_auditState = new TableColumn(table, SWT.NONE);
		tbl_auditState.setWidth(83);
		tbl_auditState.setText(l.getString("AuditStatus"));

		TableColumn tbl_auditOpinion = new TableColumn(table, SWT.NONE);
		tbl_auditOpinion.setWidth(100);
		tbl_auditOpinion.setText(l.getString("Audit_opinion"));

		// 完成查询操作
		try {
			table.removeAll();
			Response resp = Env.client.sendRequest("OPLOGAUDITHISTORY", properties);
			Properties[] ps = resp.getItemData();
			for (Properties p : ps) {
				String auditor = p.getProperty(PropertiesKeysRes.AUDITOR);
				String auditTime = Utils.format(Utils.parse(p.getProperty(PropertiesKeysRes.AUDITTIME)));
				String auditState = "1".equals(p.getProperty(PropertiesKeysRes.AUDITSTATE)) ? l.getString("normal")
						: l.getString("abnoramal");
				String auditMemo = p.getProperty(PropertiesKeysRes.AUDITMEMO);
	
				TableItem ti = new TableItem(table, SWT.NONE);
				ti.setText(new String[] { auditor, auditTime, auditState, auditMemo });
			}
		} catch (ParseException e) {
			log.errlog("Time parse error", e);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_error_timeFormat"));
			mb.open();
		} catch (ServerException se) {
			log.errlog("Query log fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryLog") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Query log fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryLog"));
			mb.open();
		}
		return container;
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("AuditLogDetail"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(520, 320);
	}
}
