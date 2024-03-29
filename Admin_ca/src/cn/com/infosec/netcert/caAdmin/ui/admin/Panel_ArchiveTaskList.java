package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.security.Security;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import cn.com.infosec.jce.provider.InfosecProvider;
import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**   
 * 查询、暂停、启动 未完成的归档任务 
 * @Author 江岩    
 * @Time 2019-07-15 15:20
 */
public class Panel_ArchiveTaskList extends ApplicationWindow {
	private Table table;
	private Properties properties;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private Button btn_pause, btn_resume;
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * 构造方法
	 * @param
	 */
	public Panel_ArchiveTaskList() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		addMenuBar();
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
		fd_group.bottom = new FormAttachment(0, 271);
		fd_group.right = new FormAttachment(0, 500);
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		group.setLayoutData(fd_group);
		group.setText(l.getString("ArchiveTaskList"));
		group.setLayout(new GridLayout(1, false));

		table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_table.widthHint = 457;
		gd_table.heightHint = 211;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tbl_TaskID = new TableColumn(table, SWT.NONE);
		tbl_TaskID.setWidth(73);
		tbl_TaskID.setText(l.getString("TaskID"));

		TableColumn tbl_ArchiveDate = new TableColumn(table, SWT.NONE);
		tbl_ArchiveDate.setWidth(82);
		tbl_ArchiveDate.setText(l.getString("ArchiveDate"));

		TableColumn tbl_ArchiveSt = new TableColumn(table, SWT.NONE);
		tbl_ArchiveSt.setWidth(81);
		tbl_ArchiveSt.setText(l.getString("ArchiveSt"));

		TableColumn tbl_ArchiveEt = new TableColumn(table, SWT.NONE);
		tbl_ArchiveEt.setWidth(81);
		tbl_ArchiveEt.setText(l.getString("ArchiveEt"));

		TableColumn tbl_Schedule = new TableColumn(table, SWT.NONE);
		tbl_Schedule.setWidth(70);
		tbl_Schedule.setText(l.getString("Schedule"));

		TableColumn tbl_State = new TableColumn(table, SWT.NONE);
		tbl_State.setWidth(67);
		tbl_State.setText(l.getString("status"));

		btn_pause = new Button(container, SWT.NONE);
		FormData fd_btn_pause = new FormData();
		fd_btn_pause.right = new FormAttachment(0, 378);
		fd_btn_pause.top = new FormAttachment(0, 277);
		fd_btn_pause.left = new FormAttachment(0, 298);
		btn_pause.setLayoutData(fd_btn_pause);
		btn_pause.setText(l.getString("pause"));

		btn_resume = new Button(container, SWT.NONE);
		FormData fd_btn_resume = new FormData();
		fd_btn_resume.right = new FormAttachment(0, 480);
		fd_btn_resume.top = new FormAttachment(0, 277);
		fd_btn_resume.left = new FormAttachment(0, 400);
		btn_resume.setLayoutData(fd_btn_resume);
		btn_resume.setText(l.getString("resume"));

		btn_pause.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					Env.client.sendRequest("OPLOGARCHTASKPAUSE", properties);
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage(l.getString("Notice_succ_pauseArchTask"));
					mb.open();
					btn_pause.setEnabled(false);
					btn_resume.setEnabled(true);
				} catch (ServerException se) {
					log.errlog("Pause archive task fail", se);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_pauseArchTask") + "[" + se.getErrorNum() + "]:"
							+ se.getErrorMsg());
					mb.open();
				} catch (Exception ee) {
					log.errlog("Pause archive task fail", ee);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_pauseArchTask"));
					mb.open();
				}
				queryArchTaskList(); // 刷新当前列表
			}
		});

		btn_resume.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					Env.client.sendRequest("OPLOGARCHTASKRESUME", properties);
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage(l.getString("Notice_succ_resumeArchTask"));
					mb.open();
					btn_pause.setEnabled(true);
					btn_resume.setEnabled(false);
				} catch (ServerException se) {
					log.errlog("Resume archive task fail", se);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_resumeArchTask") + "[" + se.getErrorNum() + "]:"
							+ se.getErrorMsg());
					mb.open();
				} catch (Exception ee) {
					log.errlog("Resume archive task fail", ee);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_resumeArchTask"));
					mb.open();
				}
				queryArchTaskList(); // 刷新当前列表
			}
		});
		queryArchTaskList(); // 查询归档任务列表	
		return container;
	}

	/**
	 *  查询归档任务列表
	 * @param     
	 * @return        
	 * @throws   
	 * @Author 江岩 
	 * @Time   2019-07-16 16:33
	 * @version 1.0
	 */
	private void queryArchTaskList() {
		try {
			table.removeAll();
			Response resp = Env.client.sendRequest("OPLOGARCHTASKVIEW", null);
			properties = resp.getP();
			String taskID = properties.getProperty(PropertiesKeysRes.TASKID);
			String auditTime = Utils.format_archiveTime(Utils.parse_archiveTime(properties.getProperty(PropertiesKeysRes.AUDITTIME)));// 20190723-2019-07-23
			String archive_st = properties.getProperty(PropertiesKeysRes.OPLOGARCHSTARTTIME);
			String archive_et = properties.getProperty(PropertiesKeysRes.OPLOGARCHSTOPTIME);
			String archive_state = properties.getProperty(PropertiesKeysRes.OPLOGARCHSTATE);
			String archive_schedule = properties.getProperty(PropertiesKeysRes.OPLOGARCHPROGESS) + "%";

			String state = "";
			if ("0".equals(archive_state)) { // 未完成
				state = l.getString("unfinished");
				btn_resume.setEnabled(false);
				btn_pause.setEnabled(true);
			} else if ("2".equals(archive_state)) { // 暂停
				state = l.getString("pause");
				btn_resume.setEnabled(true);
				btn_pause.setEnabled(false);
			} else if ("1".equals(archive_state)) { // 完成
				state = l.getString("finished");
				btn_resume.setEnabled(false);
				btn_pause.setEnabled(false);
			} else {
				state = l.getString("unknown");
			}

			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(new String[] { taskID, auditTime, archive_st, archive_et, archive_schedule, state });

		} catch (ServerException se) {
			log.errlog("Query archive task list fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(
					l.getString("Notice_fail_queryArchTaskList") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Query archive task list fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryArchTaskList"));
			mb.open();
		}

	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("ArchiveTaskList"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	public static void main(String args[]) {
		Security.addProvider(new InfosecProvider());
		Panel_ArchiveTaskList archiveTaskList = new Panel_ArchiveTaskList();
		archiveTaskList.setBlockOnOpen(true);
		archiveTaskList.open();
		Display.getCurrent().dispose();

	}
	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(521, 351);
	}
}
