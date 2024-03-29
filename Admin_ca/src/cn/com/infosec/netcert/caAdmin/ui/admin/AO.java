package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.PageUtil;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**
 *  AO 审计员视图
 * @Author 江岩    
 * @Time 2019-06-04 19:33
 */
public class AO extends ApplicationWindow {
	private Table table; // 列表
	private TabFolder tabFolder;
	private Text qryOper, txt_Auditor;
	private Combo combo_rslt, combo_opType, combo_currPage, cobmo_Stime, cobmo_Etime, combo_state, combo_archive_st;
	private DateTime startDate, endDate, startTime, endTime, date_st_audit, date_et_audit, time_st_audit, time_et_audit,
			date_time_archive, d_archive_st, t_archive_st, d_archive_et, t_archive_et;

	private List<String> opTypes = new ArrayList<String>(), opDesc = new ArrayList<String>();
	private Properties[] rows;
	private String count, subject;
	private int pageSize = 15; // 每页15条,否则页面有侧边下拉条

	private PageUtil pageUtil;
	private String curr_Page, total_Page, logType;
	private SelectionListener LogDetail_Audit_Listener, AuditedLogDetail_Listener;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	private Text txt_archiver;

	/**
	 * 构造方法
	 */
	public AO(String subject) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.MIN);
		addMenuBar();
		this.subject = subject;
		pageUtil = new PageUtil();
		// opTypes.add("System");
		// opDesc.add(l.getString("systemLog"));
		opTypes.add("");
		opDesc.add(l.getString("all"));
		
		opTypes.add("LOGIN");
		opDesc.add(l.getString("op_login"));
		opTypes.add("ADDTEMPLATE");
		opDesc.add(l.getString("op_addTemplate"));
		opTypes.add("UPDATETEMPLATE");
		opDesc.add(l.getString("op_updateTemplate"));
		opTypes.add("DELETETEMPLATE");
		opDesc.add(l.getString("op_delTemplate"));
		opTypes.add("REVOKETEMPLATE");
		opDesc.add(l.getString("op_revokeTemplate"));
		opTypes.add("GENCERTCSR");
		opDesc.add(l.getString("op_genCSR"));

		opTypes.add("REGISTERBA");
		opDesc.add(l.getString("op_newBA"));
		opTypes.add("REQUESTBACERT");
		opDesc.add(l.getString("op_applyBACert"));
		opTypes.add("REVOKEBA");
		opDesc.add(l.getString("op_revokeBACert"));
		opTypes.add("UPDATEBA");
		opDesc.add(l.getString("op_modifyBA"));

		opTypes.add("REGISTERBO");
		opDesc.add(l.getString("op_newBO"));
		opTypes.add("REQUESTBOCERT");
		opDesc.add(l.getString("op_applyBOCert"));
		opTypes.add("REVOKEBO");
		opDesc.add(l.getString("op_revokeBOCert"));
		opTypes.add("UPDATEBO");
		opDesc.add(l.getString("op_modifyBO"));
		opTypes.add("PRIVILEGEBO");
		opDesc.add(l.getString("op_grant"));
		opTypes.add("GETPERMISSION");
		opDesc.add(l.getString("op_getPermission"));

		opTypes.add("CREATERA");
		opDesc.add(l.getString("op_newRA"));
		opTypes.add("REQUESTRACERT");
		opDesc.add(l.getString("op_requestRACert"));
		opTypes.add("REVOKERA");
		opDesc.add(l.getString("op_revokeRACert"));
		opTypes.add("UPDATERA");
		opDesc.add(l.getString("op_modifyRA"));

		opTypes.add("REGISTERAO");
		opDesc.add(l.getString("op_newAO"));
		opTypes.add("REQUESTAOCERT");
		opDesc.add(l.getString("op_applyAOCert"));
		opTypes.add("REVOKEAO");
		opDesc.add(l.getString("op_revokeAOCert"));
		opTypes.add("UPDATEAO");
		opDesc.add(l.getString("op_modifyAO"));

		opTypes.add("DOWNADMINCERT");
		opDesc.add(l.getString("op_downloadAdminCert"));

		opTypes.add("APPLYUSER");
		opDesc.add(l.getString("op_applyUser"));
		opTypes.add("MODIFYUSER");
		opDesc.add(l.getString("op_modifyUser"));
		opTypes.add("SEARCHUSER");
		opDesc.add(l.getString("op_searchUser"));

		opTypes.add("REQUESTCERT");
		opDesc.add(l.getString("op_applyCert"));
		opTypes.add("GET2CODE");
		opDesc.add(l.getString("op_get2Code"));
		opTypes.add("DOWNCERT");
		opDesc.add(l.getString("op_downloadCert"));
		opTypes.add("UPDATECERT");
		opDesc.add(l.getString("op_updateCert"));
		opTypes.add("SEARCHCERT");
		opDesc.add(l.getString("op_searchCert"));

		opTypes.add("GETCERTENTITY");
		opDesc.add(l.getString("op_getCert"));

		opTypes.add("LOCKCERT");
		opDesc.add(l.getString("op_lockCert"));
		opTypes.add("UNLOCKCERT");
		opDesc.add(l.getString("op_unlockCert"));
		opTypes.add("REVOKECERT");
		opDesc.add(l.getString("op_revokeCert"));
		opTypes.add("RECOVERENCCERT");
		opDesc.add(l.getString("op_recoverEncCert"));
		opTypes.add("RECOVERENCCERTREQ");
		opDesc.add(l.getString("op_recoverEncCertReq"));

		opTypes.add("OPLOGAUDIT");
		opDesc.add(l.getString("op_logAudit"));

		opTypes.add("OPLOGARCH");
		opDesc.add(l.getString("op_LogArch"));
		opTypes.add("OPLOGARCHTASKPAUSE");
		opDesc.add(l.getString("op_logArchTaskPause"));
		opTypes.add("OPLOGARCHTASKRESUME");
		opDesc.add(l.getString("op_logArchTaskResume"));
		opTypes.add("OPLOGAUDITHISTORY");
		opDesc.add(l.getString("op_logAuditHistory"));
	}

	/**
	 * 视图主页面绘画 
	 * @Author 江岩      
	 * @Time 2019-06-04 19:34
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());

		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(0, 518);
		fd_table.right = new FormAttachment(0, 1038);
		fd_table.top = new FormAttachment(0, 163);
		fd_table.left = new FormAttachment(0, 5);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		final Menu menu = new Menu(table);

		final MenuItem item_logDetail_audit = new MenuItem(menu, SWT.PUSH);
		item_logDetail_audit.setText(l.getString("logDetail(audit)"));
		final MenuItem item_auditedDetail = new MenuItem(menu, SWT.PUSH);
		item_auditedDetail.setText(l.getString("auditedDetail"));

		LogDetail_Audit_Listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Panel_LogDetail_Audit audit = new Panel_LogDetail_Audit(rows[table.getSelectionIndex()]);
				audit.setBlockOnOpen(true);
				int w = audit.open();
				if (w == 0) {
					refresh("AuditLog");
					listLog("1", "0");
				}
			}
		};

		AuditedLogDetail_Listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Panel_AuditedLogDetail auditLogDetail = new Panel_AuditedLogDetail(rows[table.getSelectionIndex()]);
				auditLogDetail.setBlockOnOpen(true);
				int w = auditLogDetail.open();
				if (w == 0) {
					refresh("AuditedLog");
					listLog("1", "1");
				}
			}
		};

		item_logDetail_audit.addSelectionListener(LogDetail_Audit_Listener);
		item_auditedDetail.addSelectionListener(AuditedLogDetail_Listener);
		table.setMenu(menu);

		table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent me) {
				if (me.button == 3) {
					if ("0".equalsIgnoreCase(logType)) { // 待审计日志
						item_logDetail_audit.setEnabled(true);
						item_auditedDetail.setEnabled(false);
					} else if ("1".equalsIgnoreCase(logType)) { // 已审计日志 
						item_logDetail_audit.setEnabled(true);
						item_auditedDetail.setEnabled(true);
					} else if ("2".equalsIgnoreCase(logType)) { // 已归档日志 
						item_logDetail_audit.setEnabled(false);
						item_auditedDetail.setEnabled(false);
					}
				}
			}
		});

		TableColumn clm_logId = new TableColumn(table, SWT.NONE);
		clm_logId.setWidth(78);
		clm_logId.setText(l.getString("logId"));

		TableColumn clm_operationTime = new TableColumn(table, SWT.NONE);
		clm_operationTime.setWidth(150);
		clm_operationTime.setText(l.getString("operation_time"));

		TableColumn clm_opertaionObject = new TableColumn(table, SWT.NONE);
		clm_opertaionObject.setWidth(220);
		clm_opertaionObject.setText(l.getString("operation_object"));

		TableColumn clm_operationResult = new TableColumn(table, SWT.NONE);
		clm_operationResult.setWidth(179);
		clm_operationResult.setText(l.getString("operation_result"));

		TableColumn clm_operationType = new TableColumn(table, SWT.NONE);
		clm_operationType.setWidth(118);
		clm_operationType.setText(l.getString("operation_type"));

		TableColumn clm_operationUser = new TableColumn(table, SWT.NONE);
		clm_operationUser.setWidth(118);
		clm_operationUser.setText(l.getString("operation_user"));

		TableColumn clm_requestAddress = new TableColumn(table, SWT.NONE);
		clm_requestAddress.setWidth(128);
		clm_requestAddress.setText(l.getString("request_IP"));

		tabFolder = new TabFolder(container, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.bottom = new FormAttachment(table, -6);
		fd_tabFolder.right = new FormAttachment(0, 1043);
		fd_tabFolder.top = new FormAttachment(0);
		fd_tabFolder.left = new FormAttachment(0, 10);
		tabFolder.setLayoutData(fd_tabFolder);

		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				table.removeAll();
				if (tabFolder.getSelectionIndex() == 0) {
					logType = "0";
				} else if (tabFolder.getSelectionIndex() == 1) {
					logType = "1";
				}
			}
		});

		// 未审计日志视图
		TabItem item_notAuditLog = new TabItem(tabFolder, SWT.NONE);
		item_notAuditLog.setText(l.getString("notAuditLog"));
		Composite comp_notAuditLog = new Composite(tabFolder, SWT.NONE);
		item_notAuditLog.setControl(comp_notAuditLog);
		comp_notAuditLog.setLayout(null);

		Label lbl_operationUser = new Label(comp_notAuditLog, SWT.NONE);
		lbl_operationUser.setBounds(61, 18, 51, 17);
		lbl_operationUser.setAlignment(SWT.RIGHT);
		lbl_operationUser.setText(l.getString("operation_user") + ":");

		qryOper = new Text(comp_notAuditLog, SWT.BORDER);
		qryOper.setBounds(118, 15, 140, 23);
		qryOper.setTextLimit(30);
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.DATE, -7);

		Label lbl_happenTime = new Label(comp_notAuditLog, SWT.NONE);
		lbl_happenTime.setBounds(320, 15, 51, 17);
		lbl_happenTime.setAlignment(SWT.RIGHT);
		lbl_happenTime.setText(l.getString("operation_time") + ":");

		startDate = new DateTime(comp_notAuditLog, SWT.DROP_DOWN);
		startDate.setBounds(377, 11, 103, 24);
		startDate.setDate(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DATE));
		startTime = new DateTime(comp_notAuditLog, SWT.BORDER | SWT.TIME);
		startTime.setBounds(486, 11, 81, 24);

		Label lbl_operationType = new Label(comp_notAuditLog, SWT.NONE);
		lbl_operationType.setBounds(61, 58, 51, 17);
		lbl_operationType.setAlignment(SWT.RIGHT);
		lbl_operationType.setText(l.getString("operation_type") + ":");

		combo_opType = new Combo(comp_notAuditLog, SWT.READ_ONLY);
		combo_opType.setBounds(118, 55, 140, 25);
		for (String s : opDesc) {
			combo_opType.add(s);
		}
		combo_opType.select(0);
		
		Label lblNewLabel_2 = new Label(comp_notAuditLog, SWT.NONE);
		lblNewLabel_2.setBounds(377, 41, 17, 17);
		lblNewLabel_2.setText("  ~");

		endDate = new DateTime(comp_notAuditLog, SWT.BORDER | SWT.DROP_DOWN);
		endDate.setBounds(377, 58, 103, 24);

		endTime = new DateTime(comp_notAuditLog, SWT.BORDER | SWT.TIME);
		endTime.setBounds(486, 58, 79, 24);

		Label lbl_operationResult = new Label(comp_notAuditLog, SWT.NONE);
		lbl_operationResult.setBounds(61, 100, 51, 17);
		lbl_operationResult.setAlignment(SWT.RIGHT);
		lbl_operationResult.setText(l.getString("operation_result") + ":");

		combo_rslt = new Combo(comp_notAuditLog, SWT.READ_ONLY);
		combo_rslt.setBounds(118, 97, 140, 25);
		combo_rslt.add(l.getString("all"));
		combo_rslt.add(l.getString("success"));
		combo_rslt.add(l.getString("failure"));
		combo_rslt.select(0);
		
		Button btnQry = new Button(comp_notAuditLog, SWT.NONE);
		btnQry.setBounds(591, 95, 74, 27);
		btnQry.setText(l.getString("query_log"));
		btnQry.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listLog("1", "0");
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage(l.getString("Notice_logNumber") + ":" + count);
					mb.open();
					pageUtil.btn_Change(curr_Page, total_Page);
				}
			}
		});

		// 已审计日志 视图
		TabItem item_AuditedLog = new TabItem(tabFolder, SWT.NONE);
		item_AuditedLog.setText(l.getString("AuditedLog"));

		Composite comp_AuditedLog = new Composite(tabFolder, SWT.NONE);
		item_AuditedLog.setControl(comp_AuditedLog);
		comp_AuditedLog.setLayout(null);

		Calendar cl_audit = Calendar.getInstance();
		cl_audit.add(Calendar.DATE, -7);

		Label lblNewLabel_3 = new Label(comp_AuditedLog, SWT.NONE);
		lblNewLabel_3.setBounds(15, 130, 75, 0);
		lblNewLabel_3.setText("");

		Group group = new Group(comp_AuditedLog, SWT.NONE);
		group.setBounds(0, 0, 1015, 124);
		group.setText(l.getString("AuditedLog"));

		Label lbl_auditor = new Label(group, SWT.NONE);
		lbl_auditor.setBounds(90, 21, 45, 17);
		lbl_auditor.setText(l.getString("Auditor") + ":");

		txt_Auditor = new Text(group, SWT.BORDER);
		txt_Auditor.setBounds(141, 18, 194, 23);
		txt_Auditor.setTextLimit(30);

		Label lbl_st_audit = new Label(group, SWT.NONE);
		lbl_st_audit.setBounds(80, 55, 51, 17);
		lbl_st_audit.setText(l.getString("startTime") + ":");

		date_st_audit = new DateTime(group, SWT.BORDER | SWT.DROP_DOWN);
		date_st_audit.setBounds(141, 52, 103, 24);
		date_st_audit.setDate(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DATE));

		time_st_audit = new DateTime(group, SWT.BORDER | SWT.TIME);
		time_st_audit.setBounds(254, 52, 81, 24);

		Label lbl_et_audit = new Label(group, SWT.NONE);
		lbl_et_audit.setBounds(80, 94, 51, 17);
		lbl_et_audit.setText(l.getString("endTime") + ":");

		date_et_audit = new DateTime(group, SWT.BORDER | SWT.DROP_DOWN);
		date_et_audit.setBounds(140, 90, 105, 24);

		time_et_audit = new DateTime(group, SWT.BORDER | SWT.TIME);
		time_et_audit.setBounds(256, 90, 79, 24);

		Label lbl_audit_state = new Label(group, SWT.NONE);
		lbl_audit_state.setBounds(379, 21, 51, 17);
		lbl_audit_state.setText(l.getString("AuditState") + ":");

		combo_state = new Combo(group, SWT.NONE | SWT.READ_ONLY);
		combo_state.setBounds(447, 18, 86, 25);
		combo_state.add(l.getString("all"));
		combo_state.add(l.getString("normal"));
		combo_state.add(l.getString("abnormal"));
		combo_state.select(0);

		Button btn_AuditedLog = new Button(group, SWT.NONE);
		btn_AuditedLog.setBounds(556, 89, 74, 27);
		btn_AuditedLog.setText(l.getString("query"));

		btn_AuditedLog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listLog("1", "1");
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage(l.getString("Notice_logNumber") + ":" + count);
					mb.open();
					pageUtil.btn_Change(curr_Page, total_Page);
				}
			}
		});

		TabItem tbtm_archiveLog = new TabItem(tabFolder, SWT.NONE);
		tbtm_archiveLog.setText(l.getString("ArchiveLog"));

		Composite composite_archiveLog = new Composite(tabFolder, SWT.NONE);
		tbtm_archiveLog.setControl(composite_archiveLog);

		// 归档视图 group
		Group group_1 = new Group(composite_archiveLog, SWT.NONE);
		group_1.setBounds(10, -11, 398, 138);

		Label lbl_st_archive = new Label(group_1, SWT.NONE);
		lbl_st_archive.setBounds(10, 30, 51, 17);
		lbl_st_archive.setText(l.getString("startTime") + ":");

		cobmo_Stime = new Combo(group_1, SWT.BORDER | SWT.READ_ONLY);
		cobmo_Stime.setBounds(72, 27, 76, 25);
		cobmo_Stime.select(0);

		Label lbl_et_archive = new Label(group_1, SWT.NONE);
		lbl_et_archive.setBounds(167, 30, 61, 17);
		lbl_et_archive.setText(l.getString("endTime") + ":");

		cobmo_Etime = new Combo(group_1, SWT.BORDER | SWT.READ_ONLY);
		cobmo_Etime.setBounds(233, 27, 64, 25);
		cobmo_Etime.select(0);

		Label lbl_time_archive = new Label(group_1, SWT.NONE);
		lbl_time_archive.setBounds(10, 76, 51, 17);
		lbl_time_archive.setText(l.getString("ArchiveDate") + ":");

		date_time_archive = new DateTime(group_1, SWT.DROP_DOWN);
		date_time_archive.setBounds(67, 73, 103, 24);

		for (int i = 0; i <= 24; i++) {
			cobmo_Stime.add(String.valueOf(i));
		}

		for (int i = 0; i <= 24; i++) {
			cobmo_Etime.add(String.valueOf(i));
		}

		Button btn_archive = new Button(group_1, SWT.NONE);
		btn_archive.setBounds(198, 101, 71, 27);
		btn_archive.setText(l.getString("Archive"));

		Button btn_archiveWork = new Button(group_1, SWT.NONE);
		btn_archiveWork.setBounds(293, 101, 84, 27);
		btn_archiveWork.setText(l.getString("query_ArchiveWork"));

		Label lbl_notice = new Label(group_1, SWT.NONE);
		lbl_notice.setBounds(176, 73, 61, 25);
		lbl_notice.setImage(new Image(getShell().getDisplay(), "res/help.png"));
		lbl_notice.setToolTipText(l.getString("Notice_AO_archiveDate") + ".");

		Group group_2 = new Group(composite_archiveLog, SWT.NONE);
		group_2.setBounds(533, -11, 482, 148);

		Label lblNewLabel_6 = new Label(group_2, SWT.NONE);
		lblNewLabel_6.setBounds(40, 31, 46, 17);
		lblNewLabel_6.setText(l.getString("Auditor") + ":");

		d_archive_st = new DateTime(group_2, SWT.DROP_DOWN);
		d_archive_st.setDate(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DATE));
		d_archive_st.setBounds(92, 69, 88, 24);

		t_archive_st = new DateTime(group_2, SWT.BORDER | SWT.TIME);
		t_archive_st.setBounds(193, 69, 88, 24);

		d_archive_et = new DateTime(group_2, SWT.BORDER | SWT.DROP_DOWN);
		d_archive_et.setBounds(92, 99, 88, 24);

		t_archive_et = new DateTime(group_2, SWT.BORDER | SWT.TIME);
		t_archive_et.setBounds(193, 99, 88, 24);

		Button btn_ok = new Button(group_2, SWT.NONE);
		btn_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listLog("1", "2");
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage(l.getString("Notice_logNumber") + ":" + count);
					mb.open();
					pageUtil.btn_Change(curr_Page, total_Page);
				}
			}
		});
		btn_ok.setBounds(329, 96, 80, 27);
		btn_ok.setText(l.getString("query"));

		Label lblNewLabel_1 = new Label(group_2, SWT.NONE);
		lblNewLabel_1.setBounds(10, 72, 76, 17);
		lblNewLabel_1.setText(l.getString("archiveStartTime") + ":");

		Label lblNewLabel_4 = new Label(group_2, SWT.NONE);
		lblNewLabel_4.setBounds(10, 104, 76, 17);
		lblNewLabel_4.setText(l.getString("archiveEndTime") + ":");

		txt_archiver = new Text(group_2, SWT.BORDER);
		txt_archiver.setBounds(92, 28, 179, 23);
		txt_archiver.setTextLimit(30);

		Label lblNewLabel_5 = new Label(group_2, SWT.NONE);
		lblNewLabel_5.setBounds(288, 31, 61, 17);
		lblNewLabel_5.setText(l.getString("auditStatus") + ":");

		combo_archive_st = new Combo(group_2, SWT.NONE | SWT.READ_ONLY);
		combo_archive_st.setBounds(355, 28, 88, 25);
		combo_archive_st.add(l.getString("all"));
		combo_archive_st.add(l.getString("normal"));
		combo_archive_st.add(l.getString("abnormal"));
		combo_archive_st.select(0);

		btn_archiveWork.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					try {
						Response resp = Env.client.sendRequest("OPLOGARCHTASKVIEW", null);
						Properties properties = resp.getP();

						if (properties.size() == 0) {
							MessageBox mb = new MessageBox(getShell(), SWT.OK);
							mb.setMessage(l.getString("Notice_completed_archTaskList"));
							mb.open();
						} else {
							Panel_ArchiveTaskList p_archTaskList = new Panel_ArchiveTaskList();
							p_archTaskList.setBlockOnOpen(true);
							p_archTaskList.open();
						}
					} catch (ServerException se) {
						log.errlog("View log_archive_task fail", se);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_queryArchiveTask") + ":" + "[" + se.getErrorNum() + "]:"
								+ se.getErrorMsg());
						mb.open();
					} catch (Exception ee) {
						log.errlog("View log_archive_task fail", ee);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_queryArchiveTask"));
						mb.open();
					}
				}
			}
		});
		btn_archive.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					try {
						Response resp = Env.client.sendRequest("OPLOGARCHTASKVIEW", null);
						Properties properties = resp.getP();

						if (properties.size() != 0) {
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_exist_archTask"));
							mb.open();
							return;
						} else {
							int stIndex = cobmo_Stime.getSelectionIndex();
							int etIndex = cobmo_Etime.getSelectionIndex();
							if (stIndex >= etIndex) {
								MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
								mb.setMessage(l.getString("Notice_error_archiveTime"));
								mb.open();
								return;
							}
							String _st_archive = "";
							Calendar c = Calendar.getInstance();
							c.set(Calendar.YEAR, date_time_archive.getYear());
							c.set(Calendar.MONTH, date_time_archive.getMonth());
							c.set(Calendar.DATE, date_time_archive.getDay());
							c.set(Calendar.HOUR, 0); // date_time_archive.getMonth()
							c.set(Calendar.MINUTE, 0);
							c.set(Calendar.SECOND, 0);

							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
							_st_archive = sdf.format(c.getTime());

							Properties p = new Properties();
							p.setProperty(PropertiesKeysRes.AUDITTIME, _st_archive);
							p.setProperty(PropertiesKeysRes.OPLOGARCHSTARTTIME, cobmo_Stime.getText().trim());
							p.setProperty(PropertiesKeysRes.OPLOGARCHSTOPTIME, cobmo_Etime.getText().trim());

							try {
								Env.client.sendRequest("OPLOGARCH", p);
								MessageBox mb = new MessageBox(getShell(), SWT.OK);
								mb.setMessage(l.getString("Notice_succ_setArchiveLogTask"));
								mb.open();
							} catch (ServerException se) {
								log.errlog("Set archive task fail", se);
								MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
								mb.setMessage(l.getString("Notice_fail_archiveLog") + "[" + se.getErrorNum() + "]:"
										+ se.getErrorMsg());
								mb.open();
							} catch (Exception e1) {
								log.errlog("Set archive task fail", e1);
								MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
								mb.setMessage(l.getString("Notice_fail_archiveLog"));
								mb.open();
							}
							
						}
					} catch (ServerException se) {
						log.errlog("View log_archive_task fail", se);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_queryArchiveTask") + ":" + "[" + se.getErrorNum() + "]:"
								+ se.getErrorMsg());
						mb.open();
					} catch (Exception ee) {
						log.errlog("View log_archive_task fail", ee);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_queryArchiveTask"));
						mb.open();
					}
				}
			}
		});

		// 分页模块
		Composite composite = new Composite(container, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 572);
		fd_composite.right = new FormAttachment(0, 1038);
		fd_composite.top = new FormAttachment(0, 523);
		fd_composite.left = new FormAttachment(0, 761);
		composite.setLayoutData(fd_composite);
		GridLayout grid_composite = new GridLayout(7, false);
		grid_composite.marginTop = 10;
		grid_composite.horizontalSpacing = 8;
		composite.setLayout(grid_composite);
		Composite comp = (Composite) pageUtil.page(composite);
		// 分页
		Control[] controls = comp.getChildren();
		// 修改当前页
		combo_currPage = (Combo) controls[5];
		GridData gd_combo_currPage = new GridData();
		gd_combo_currPage.widthHint = 30;
		gd_combo_currPage.heightHint = 25;
		combo_currPage.setLayoutData(gd_combo_currPage);

		combo_currPage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listLog(combo_currPage.getText().trim(), logType);
					pageUtil.btn_Change(curr_Page, total_Page);
				}
			}
		});
		// lblBegin 第一页
		controls[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listLog("1", logType);
					pageUtil.btn_Change("1", total_Page);
				}
			}
		});
		// lblBack 上一页
		controls[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					String page = String.valueOf(Integer.parseInt(curr_Page) - 1);
					listLog(page, logType);
					pageUtil.btn_Change(curr_Page, total_Page);
				}
			}
		});
		// lblForward 下一页
		controls[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					String page = String.valueOf(Integer.parseInt(curr_Page) + 1);
					listLog(page, logType);
					pageUtil.btn_Change(curr_Page, total_Page);
				}
			}
		});
		// lblEnd 最后一页
		controls[3].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listLog(total_Page, logType);
					pageUtil.btn_Change(total_Page, total_Page);
				}
			}
		});
		return container;
	}

	/**
	 * 分页查询日志
	 * @Author 江岩 
	 * @Time   2019-06-04 19:35
	 * @Desc logType : 0-待审计日志查询  1-已审计日志查询 
	 */
	private void listLog(String pageNum, String logType) {
		try {
			table.removeAll();
			if ("0".equals(logType)) {
				String _startTime = "", _endTime = "";
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, startDate.getYear());
				c.set(Calendar.MONTH, startDate.getMonth());
				c.set(Calendar.DATE, startDate.getDay());
				c.set(Calendar.HOUR, startTime.getHours());
				c.set(Calendar.MINUTE, startTime.getMinutes());
				c.set(Calendar.SECOND, 0);
				_startTime = Utils.formatZwithGMT(c.getTime());

				c = Calendar.getInstance();
				c.set(Calendar.YEAR, endDate.getYear());
				c.set(Calendar.MONTH, endDate.getMonth());
				c.set(Calendar.DATE, endDate.getDay());
				c.set(Calendar.HOUR, endTime.getHours());
				c.set(Calendar.MINUTE, endTime.getMinutes());
				c.set(Calendar.SECOND, 0);
				_endTime = Utils.formatZwithGMT(c.getTime());

				String _op = "";
				if (combo_opType.getSelectionIndex() > -1)
					_op = opTypes.get(combo_opType.getSelectionIndex());

				String _rslt = "";
				if (combo_rslt.getSelectionIndex() == 1) {
					_rslt = "0";
				} else if (combo_rslt.getSelectionIndex() == 2) {
					_rslt = "1";
				}

				Properties p = new Properties();
				p.setProperty(PropertiesKeysRes.PAGE, pageNum);
				p.setProperty(PropertiesKeysRes.PAGESIZE, String.valueOf(pageSize));
				p.setProperty(PropertiesKeysRes.USERNAME, qryOper.getText().trim());
				p.setProperty(PropertiesKeysRes.STARTTIME, _startTime);
				p.setProperty(PropertiesKeysRes.ENDTIME, _endTime);
				p.setProperty(PropertiesKeysRes.OPTYPE, _op);
				p.setProperty(PropertiesKeysRes.RETURNCODE, _rslt);

				Response resp = Env.client.sendRequest("QUERYLOG", p);
				rows = resp.getItemData();

				total_Page = resp.getP().getProperty(PropertiesKeysRes.TOTALPAGES);
				curr_Page = pageNum;

				for (Properties row : rows) {
					String opTime = Utils.format(Utils.parse(row.getProperty(PropertiesKeysRes.OPTIME)));
					TableItem ti = new TableItem(table, SWT.NONE);
					ti.setText(new String[] { row.getProperty(PropertiesKeysRes.LOGSN), opTime,
							row.getProperty(PropertiesKeysRes.TARGET),
							("0".equals(row.getProperty(PropertiesKeysRes.RETURNCODE)) ? l.getString("success")
									: l.getString("failure") + "[" + row.getProperty(PropertiesKeysRes.RETURNCODE)
											+ "]"),
							opDesc.get(opTypes.indexOf(row.getProperty(PropertiesKeysRes.OPTYPE))),
							row.getProperty(PropertiesKeysRes.OPERATOR),
							row.getProperty(PropertiesKeysRes.REQUESTADDR) });
				}
				count = resp.getP().getProperty(PropertiesKeysRes.TOTALROW);
			}
			if ("1".equals(logType)) { // 已审计日志

				String _st_auditedLog = "", _et_auditedLog = "";
				Calendar c_auditedLog = Calendar.getInstance();
				c_auditedLog.set(Calendar.YEAR, date_st_audit.getYear());
				c_auditedLog.set(Calendar.MONTH, date_st_audit.getMonth());
				c_auditedLog.set(Calendar.DATE, date_st_audit.getDay());
				c_auditedLog.set(Calendar.HOUR, time_st_audit.getHours());
				c_auditedLog.set(Calendar.MINUTE, time_st_audit.getMinutes());
				c_auditedLog.set(Calendar.SECOND, 0);
				_st_auditedLog = Utils.formatZwithGMT(c_auditedLog.getTime());

				c_auditedLog = Calendar.getInstance();
				c_auditedLog.set(Calendar.YEAR, date_et_audit.getYear());
				c_auditedLog.set(Calendar.MONTH, date_et_audit.getMonth());
				c_auditedLog.set(Calendar.DATE, date_et_audit.getDay());
				c_auditedLog.set(Calendar.HOUR, time_et_audit.getHours());
				c_auditedLog.set(Calendar.MINUTE, time_et_audit.getMinutes());
				c_auditedLog.set(Calendar.SECOND, 0);
				_et_auditedLog = Utils.formatZwithGMT(c_auditedLog.getTime());

				Properties p = new Properties();
				p.setProperty(PropertiesKeysRes.QUERYARCHTYPE, "0"); // 已审计日志
				p.setProperty(PropertiesKeysRes.PAGE, pageNum);
				p.setProperty(PropertiesKeysRes.PAGESIZE, String.valueOf(pageSize));
				p.setProperty(PropertiesKeysRes.AUDITOR, txt_Auditor.getText().trim());
				p.setProperty(PropertiesKeysRes.AUDITSTARTTIME, _st_auditedLog);
				p.setProperty(PropertiesKeysRes.AUDITENDTIME, _et_auditedLog);

				if (combo_state.getSelectionIndex() == 0) {
					p.setProperty(PropertiesKeysRes.AUDITSTATE, "");
				} else if (combo_state.getSelectionIndex() == 1) {
					p.setProperty(PropertiesKeysRes.AUDITSTATE, "1");
				} else {
					p.setProperty(PropertiesKeysRes.AUDITSTATE, "2");
				}

				Response resp = Env.client.sendRequest("QUERYLOGAUDITED", p);
				rows = resp.getItemData();
				total_Page = resp.getP().getProperty(PropertiesKeysRes.TOTALPAGES);
				curr_Page = pageNum;
				table.removeAll();
				for (Properties row : rows) {
					String opTime = Utils.format(Utils.parse(row.getProperty(PropertiesKeysRes.OPTIME)));
					TableItem ti = new TableItem(table, SWT.NONE);
					ti.setText(new String[] { row.getProperty(PropertiesKeysRes.LOGSN), opTime,
							row.getProperty(PropertiesKeysRes.TARGET),
							("0".equals(row.getProperty(PropertiesKeysRes.RETURNCODE)) ? l.getString("success")
									: l.getString("failure") + "[" + row.getProperty(PropertiesKeysRes.RETURNCODE)
											+ "]"),
							opDesc.get(opTypes.indexOf(row.getProperty(PropertiesKeysRes.OPTYPE))),
							row.getProperty(PropertiesKeysRes.OPERATOR),
							row.getProperty(PropertiesKeysRes.REQUESTADDR) });
				}
				count = resp.getP().getProperty(PropertiesKeysRes.TOTALROW);
			} else if ("2".equalsIgnoreCase(logType)) {

				String _st_archiveLog = "", _et_archiveLog = "";
				Calendar c_archiveLog = Calendar.getInstance();
				c_archiveLog.set(Calendar.YEAR, d_archive_st.getYear());
				c_archiveLog.set(Calendar.MONTH, d_archive_st.getMonth());
				c_archiveLog.set(Calendar.DATE, d_archive_st.getDay());
				c_archiveLog.set(Calendar.HOUR, t_archive_st.getHours());
				c_archiveLog.set(Calendar.MINUTE, t_archive_st.getMinutes());
				c_archiveLog.set(Calendar.SECOND, 0);
				_st_archiveLog = Utils.formatZwithGMT(c_archiveLog.getTime());

				c_archiveLog = Calendar.getInstance();
				c_archiveLog.set(Calendar.YEAR, d_archive_et.getYear());
				c_archiveLog.set(Calendar.MONTH, d_archive_et.getMonth());
				c_archiveLog.set(Calendar.DATE, d_archive_et.getDay());
				c_archiveLog.set(Calendar.HOUR, t_archive_et.getHours());
				c_archiveLog.set(Calendar.MINUTE, t_archive_et.getMinutes());
				c_archiveLog.set(Calendar.SECOND, 0);
				_et_archiveLog = Utils.formatZwithGMT(c_archiveLog.getTime());

				Properties p = new Properties();
				p.setProperty(PropertiesKeysRes.QUERYARCHTYPE, "1"); // 归档日志
				p.setProperty(PropertiesKeysRes.PAGE, pageNum);
				p.setProperty(PropertiesKeysRes.PAGESIZE, String.valueOf(pageSize));
				p.setProperty(PropertiesKeysRes.AUDITOR, txt_archiver.getText().trim());
				p.setProperty(PropertiesKeysRes.AUDITSTARTTIME, _st_archiveLog);
				p.setProperty(PropertiesKeysRes.AUDITENDTIME, _et_archiveLog);

				if (combo_archive_st.getSelectionIndex() == 0) {
					p.setProperty(PropertiesKeysRes.AUDITSTATE, "");
				} else if (combo_archive_st.getSelectionIndex() == 1) {
					p.setProperty(PropertiesKeysRes.AUDITSTATE, "1");
				} else {
					p.setProperty(PropertiesKeysRes.AUDITSTATE, "2");
				}

				Response resp = Env.client.sendRequest("QUERYLOGAUDITED", p);
				rows = resp.getItemData();
				total_Page = resp.getP().getProperty(PropertiesKeysRes.TOTALPAGES);
				curr_Page = pageNum;
				table.removeAll();
				for (Properties row : rows) {
					String opTime = Utils.format(Utils.parse(row.getProperty(PropertiesKeysRes.OPTIME)));
					TableItem ti = new TableItem(table, SWT.NONE);
					ti.setText(new String[] { row.getProperty(PropertiesKeysRes.LOGSN), opTime,
							row.getProperty(PropertiesKeysRes.TARGET),
							("0".equals(row.getProperty(PropertiesKeysRes.RETURNCODE)) ? l.getString("success")
									: l.getString("failure") + "[" + row.getProperty(PropertiesKeysRes.RETURNCODE)
											+ "]"),
							opDesc.get(opTypes.indexOf(row.getProperty(PropertiesKeysRes.OPTYPE))),
							row.getProperty(PropertiesKeysRes.OPERATOR),
							row.getProperty(PropertiesKeysRes.REQUESTADDR) });
				}
				count = resp.getP().getProperty(PropertiesKeysRes.TOTALROW);
			}
		} catch (ParseException e) {
			log.errlog("Operation time parse error", e);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_error_archiveTime"));
			mb.open();
		} catch (ServerException se) {
			log.errlog("Query archive log fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(
					l.getString("Notice_fail_queryArchivedLog") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception se) {
			log.errlog("Query audit log fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryArchivedLog") + se.getMessage());
			mb.open();
		}
	}

	// 菜单栏
	@Override
	protected MenuManager createMenuManager() {
		MenuManager mm = new MenuManager(l.getString("exit"));
		mm.add(new ExitAction());
		return mm;
	}

	// 菜单退出窗口事件
	class ExitAction extends Action {
		public ExitAction() {
			setText(l.getString("exit"));
		}

		public void run() {
			handleShellCloseEvent();
		}
	}

	// 刷新 table 日志信息  审计完成之后用
	private void refresh(String operation) {
		if ("AuditLog".equals(operation)) {
			table.removeAll();
			curr_Page = "0";
			total_Page = "0";
			logType = "0";
			pageUtil.btn_Change("0", "0");
		}
		if ("AuditedLog".equals(operation)) {
			table.removeAll();
			curr_Page = "0";
			total_Page = "0";
			logType = "1";
			pageUtil.btn_Change("0", "0");
		}
	}

	// 窗口标题栏设置
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("AO") + " [" + subject + "]");
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	// 重写窗口关闭事件
	@Override
	public void handleShellCloseEvent() {
		int closeCode = -1;
		if (!Env.validSession()) { // session超时
			MessageBox messagebox = new MessageBox(getShell());
			messagebox.setMessage(l.getString("Notice_invalidSession"));
			messagebox.open();
			closeCode = SWT.YES;
		} else {
			MessageBox mb = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			mb.setMessage(l.getString("Notice_exit") + "?");
			closeCode = mb.open();
		}
		if (closeCode == SWT.YES) {
			MessageBox mb = new MessageBox(getShell(), SWT.ICON_INFORMATION);
			mb.setMessage(l.getString("Notice_removeCert"));
			mb.open();
			super.handleShellCloseEvent();
		}
	}
}
