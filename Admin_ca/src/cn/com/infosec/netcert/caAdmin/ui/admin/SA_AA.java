package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**
 * 超级管理员 和 审计管理员的视图
 * @Author 江岩
 * @Time 2019-06-04 20:21
 */
public class SA_AA extends ApplicationWindow {
	private Table table;
	private Tree tree;
	private TreeItem root;
	private Button btn_modify;
	private String adminName;
	private HashMap<String, List<String[]>> dataCertInfo = new HashMap<String, List<String[]>>();
	private HashMap<String, String[]> dataUserInfo = new HashMap<String, String[]>();
	private Text txt_email, txt_tel, txt_memo, txt_userName;

	private String currRole, subject;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * 构造方法
	 */
	public SA_AA(String role, String subject) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.MIN);
		addMenuBar();
		this.currRole = role;
		this.subject = subject;
	}

	/**
	 * 视图页面绘画
	 * @Author 江岩
	 * @Time 2019-06-04 20:22
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_tabFolder.heightHint = 534;
		gd_tabFolder.widthHint = 975;
		tabFolder.setLayoutData(gd_tabFolder);
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		if ("SA".equalsIgnoreCase(currRole)) {
			tabItem.setText(l.getString("BA"));
		} else if ("AA".equalsIgnoreCase(currRole)) {
			tabItem.setText(l.getString("AO"));
		}
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite);
		composite.setLayout(new FormLayout());

		Group group = new Group(composite, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.right = new FormAttachment(0, 265);
		fd_group.top = new FormAttachment(0);
		fd_group.left = new FormAttachment(0);
		group.setLayoutData(fd_group);
		group.setText(l.getString("list"));

		tree = new Tree(group, SWT.BORDER);
		tree.setBounds(10, 24, 243, 429);

		root = new TreeItem(tree, SWT.NULL);
		if ("SA".equalsIgnoreCase(currRole)) {
			root.setText(l.getString("BA"));
		} else if ("AA".equalsIgnoreCase(currRole)) {
			root.setText(l.getString("AO"));
		}
		root.setExpanded(false);

		tree.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent me) {
				if (me.button == 1) {
					TreeItem item = tree.getItem(new Point(me.x, me.y));
					if (item == null || item.getParentItem() == null) {
						tree.deselectAll();
						return;
					} else {
						adminName = item.getText();
						refreshBAInfo(adminName);
					}
				}
			}
		});

		Button btn_refresh = new Button(group, SWT.NONE);
		btn_refresh.setBounds(10, 466, 68, 30);
		btn_refresh.setText(l.getString("refresh"));
		btn_refresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					refresh("SA".equalsIgnoreCase(currRole) ? "BA" : "AO", false);
				}
			}
		});
		Button btn_create = new Button(group, SWT.NONE);
		btn_create.setBounds(188, 466, 68, 30);
		btn_create.setText(l.getString("new"));
		btn_create.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					Panel_NewBA_AO panel = new Panel_NewBA_AO(currRole);
					panel.setBlockOnOpen(true);
					int w = panel.open();
					if (w == 0) {
						refresh("SA".equalsIgnoreCase(currRole) ? "BA" : "AO", false);
					}
				}
			}
		});

		Group group_1 = new Group(composite, SWT.NONE);
		FormData fd_group_1 = new FormData();
		fd_group_1.right = new FormAttachment(100, -10);
		fd_group_1.left = new FormAttachment(group, 6);
		fd_group_1.top = new FormAttachment(0);
		group_1.setLayoutData(fd_group_1);
		group_1.setText(l.getString("cert_group"));

		table = new Table(group_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 26, 673, 222);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		final Menu menu = new Menu(table);
		table.setMenu(menu);

		final MenuItem item_revokeAdminCert = new MenuItem(menu, SWT.PUSH);
		item_revokeAdminCert.setText(l.getString("revokeAdminCert"));
		final MenuItem item_regainAdminCert = new MenuItem(menu, SWT.PUSH);
		item_regainAdminCert.setText(l.getString("regainAdminCert"));

		
		table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent me) {
				if (me.button == 3) {
					TableItem[] items = table.getSelection();
					if (items.length <= 0) {
						return;
					}
					item_revokeAdminCert.setEnabled(true);
					item_regainAdminCert.setEnabled(false);
					if (items[0].getText(3).equalsIgnoreCase(l.getString("revoke"))) {
						item_revokeAdminCert.setEnabled(false);
						item_regainAdminCert.setEnabled(true);
					} else if (items[0].getText(3).equalsIgnoreCase(l.getString("normal"))) {
						item_revokeAdminCert.setEnabled(true);
						item_regainAdminCert.setEnabled(false);
					}
				}
			}
		});

		item_revokeAdminCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				mb.setMessage(l.getString("Notice_revokeAdminCert") + "?");
				int r = mb.open();
				if (SWT.YES == r) {
					TableItem item = (TableItem) table.getSelection()[0];
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.ADMIN_CERT_SN, item.getText());
					try {
						String reqType = "REVOKEBA";
						if ("AA".equalsIgnoreCase(currRole)) {
							reqType = "REVOKEAO";
						}
						Env.client.sendRequest(reqType, p);
						MessageBox mb_1 = new MessageBox(getShell(), SWT.NONE);
						mb_1.setMessage(l.getString("Notice_succ_revokeAdminCert"));
						mb_1.open();
						refresh("SA".equalsIgnoreCase(currRole) ? "BA" : "AO", false);
					} catch (ServerException se) {
						log.errlog("Revoke cert fail", se);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_revokeAdminCert") + "[" + se.getErrorNum() + "]:"
								+ se.getErrorMsg());
						mb.open();
					} catch (Exception ee) {
						log.errlog("Revoke cert fail", ee);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_revokeAdminCert"));
						mb.open();
					}
				}
			}
		});
		item_regainAdminCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String role = "BA";
				if ("AA".equalsIgnoreCase(currRole)) {
					role = "AO";
				}
				Panel_RegainAdminCert getAdminCert = new Panel_RegainAdminCert(adminName, role);
				getAdminCert.setBlockOnOpen(true);
				getAdminCert.open();
				refresh("SA".equalsIgnoreCase(currRole) ? "BA" : "AO", false);
			}
		});
		
		
		

		TableColumn tblclmn_sn = new TableColumn(table, SWT.NONE);
		tblclmn_sn.setWidth(150);
		tblclmn_sn.setText(l.getString("SN"));

		TableColumn tblclmn_dn = new TableColumn(table, SWT.NONE);
		tblclmn_dn.setWidth(248);
		tblclmn_dn.setText(l.getString("subject"));

		TableColumn tblclmn_notAfter = new TableColumn(table, SWT.NONE);
		tblclmn_notAfter.setWidth(163);
		tblclmn_notAfter.setText(l.getString("validityDate"));

		TableColumn tblclmn_state = new TableColumn(table, SWT.NONE);
		tblclmn_state.setWidth(101);
		tblclmn_state.setText(l.getString("status"));

		Group group_2 = new Group(composite, SWT.NONE);
		fd_group.bottom = new FormAttachment(group_2, 0, SWT.BOTTOM);
		fd_group_1.bottom = new FormAttachment(group_2, -7);
		FormData fd_group_2 = new FormData();
		fd_group_2.bottom = new FormAttachment(100, -10);
		fd_group_2.top = new FormAttachment(0, 265);
		fd_group_2.right = new FormAttachment(100, -10);
		fd_group_2.left = new FormAttachment(group, 6);
		group_2.setLayoutData(fd_group_2);
		group_2.setText(l.getString("basicInfo"));
		GridLayout gridL = new GridLayout(2, false);
		gridL.verticalSpacing = 10;
		gridL.marginLeft = 20;
		gridL.marginTop = 15;
		group_2.setLayout(gridL);

		Label lbl_username = new Label(group_2, SWT.NONE);
		GridData gd_lbl_username = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_username.widthHint = 91;
		lbl_username.setLayoutData(gd_lbl_username);
		lbl_username.setAlignment(SWT.RIGHT);
		lbl_username.setText(l.getString("username") + ":");

		txt_userName = new Text(group_2, SWT.BORDER | SWT.READ_ONLY);
		txt_userName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent paramModifyEvent) {
				if (txt_userName.getText() == null || txt_userName.getText().length() <= 0) {
					btn_modify.setEnabled(false);
				} else {
					btn_modify.setEnabled(true);
				}
			}
		});
		GridData gd_txt_userName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_userName.widthHint = 245;
		txt_userName.setLayoutData(gd_txt_userName);

		Label lbl_email = new Label(group_2, SWT.NONE);
		GridData gd_lbl_email = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_email.widthHint = 68;
		lbl_email.setLayoutData(gd_lbl_email);
		lbl_email.setAlignment(SWT.RIGHT);
		lbl_email.setText(l.getString("email") + ":");

		txt_email = new Text(group_2, SWT.BORDER);
		GridData gd_txt_email = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_email.widthHint = 245;
		txt_email.setTextLimit(30);
		txt_email.setLayoutData(gd_txt_email);

		Label lbl_phone = new Label(group_2, SWT.NONE);
		GridData gd_lbl_phone = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_phone.widthHint = 68;
		lbl_phone.setLayoutData(gd_lbl_phone);
		lbl_phone.setAlignment(SWT.RIGHT);
		lbl_phone.setText(l.getString("phone") + ":");

		txt_tel = new Text(group_2, SWT.BORDER);
		GridData gd_txt_tel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_tel.widthHint = 245;
		txt_tel.setTextLimit(15);
		txt_tel.setLayoutData(gd_txt_tel);

		Label lbl_memo = new Label(group_2, SWT.NONE);
		GridData gd_lbl_memo = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
		gd_lbl_memo.widthHint = 78;
		lbl_memo.setLayoutData(gd_lbl_memo);
		lbl_memo.setAlignment(SWT.RIGHT);
		lbl_memo.setText(l.getString("memo") + ":");

		txt_memo = new Text(group_2, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_txt_memo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_memo.heightHint = 58;
		gd_txt_memo.widthHint = 223;
		txt_memo.setTextLimit(120);
		txt_memo.setLayoutData(gd_txt_memo);
		new Label(group_2, SWT.NONE);

		btn_modify = new Button(group_2, SWT.NONE);
		GridData gd_btn_modify = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_modify.widthHint = 86;
		btn_modify.setLayoutData(gd_btn_modify);
		btn_modify.setText(l.getString("modify"));
		btn_modify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					String userName = txt_userName.getText().trim();
					String email = txt_email.getText();
					String tel = txt_tel.getText().trim();
					String memo = txt_memo.getText();
					if (email != null && email.length() != 0) {
						if (!Utils.checkEmail(email)) {
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_error_email"));
							mb.open();
							return;
						}
					} else {
						email = " ";
					}
					if (tel != null && tel.length() != 0) {
						if (!Utils.checkTel(tel)) {
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_error_phone"));
							mb.open();
							return;
						}
					} else {
						tel = " ";
					}
					if (memo == null || memo.length() <= 0) {
						memo = " ";
					}
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.ADMIN_USERNAME, userName);
					p.setProperty(PropertiesKeysRes.ADMIN_TELEPHONE, tel);
					p.setProperty(PropertiesKeysRes.ADMIN_EMAIL, email);
					p.setProperty(PropertiesKeysRes.ADMIN_REMARK, memo);
					String reqType = null;
					try {
						if ("SA".equalsIgnoreCase(currRole)) {
							reqType = "UPDATEBA";
						} else {
							reqType = "UPDATEAO";
						}
						Env.client.sendRequest(reqType, p);
						MessageBox mb = new MessageBox(getShell(), SWT.OK);
						mb.setMessage(l.getString("Notice_succ_modifyAdmin"));
						mb.open();
						refresh("SA".equalsIgnoreCase(currRole) ? "BA" : "AO", true);
						
						dataUserInfo.get(adminName)[0] = tel;
						dataUserInfo.get(adminName)[1] = email;
						dataUserInfo.get(adminName)[2] = memo;
				
						TreeItem[] items = root.getItems();
						for (TreeItem item : items) {
							if (userName.equalsIgnoreCase(item.getText())) {
								refreshBAInfo(userName);
							}
						}
					} catch (ServerException se) {
						log.errlog(reqType + " fail", se);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(
								l.getString("Notice_fail_modifyAdmin") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
						mb.open();
					} catch (Exception e1) {
						log.errlog(reqType + " fail", e1);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_modifyAdmin"));
						mb.open();
					}
				}
			}
		});
		refresh("SA".equalsIgnoreCase(currRole) ? "BA" : "AO", false);
		return container;
	}

	private void refreshBAInfo(String adminName) {
		table.removeAll();
		List<String[]> ci = dataCertInfo.get(adminName);
		for (String[] ss : ci) {
			TableItem tci = new TableItem(table, SWT.NULL);
			tci.setText(ss);
		}
		txt_userName.setText(adminName);
		txt_tel.setText(dataUserInfo.get(adminName)[0].trim());
		txt_email.setText(dataUserInfo.get(adminName)[1].trim());
		txt_memo.setText(dataUserInfo.get(adminName)[2].trim());
	}
	
	/**
	 *  刷新哪个角色信息，是否是本地刷新  
	 * @Author 江岩 
	 * @Time   2019-09-29 11:05
	 * @version 1.0
	 */
	void refresh(String role, boolean local) {
		if (!local) {
			getAdminInfo(role);
			root.removeAll();
			table.removeAll();
			for (String name : dataCertInfo.keySet()) {
				TreeItem ti = new TreeItem(root, SWT.NULL);
				ti.setText(name);
			}
			txt_userName.setText("");
		}
		txt_tel.setText("");
		txt_email.setText("");
		txt_memo.setText("");
	}

	

	/**
	 * 查询BA管理员列表
	 * @Author 江岩
	 * @Time 2019-06-04 20:23
	 * @version 1.0
	 */
	private void getAdminInfo(String role) {
		String reqType = "QUERYBALIST";
		if ("AO".equalsIgnoreCase(role)) {
			reqType = "QUERYAOLIST";
		}
		try {
			Response res = Env.client.sendRequest(reqType, new Properties());
			Properties[] ps = res.getItemData();
			dataCertInfo.clear();
			dataUserInfo.clear();
			for (Properties p : ps) {
				String name = p.getProperty(PropertiesKeysRes.ADMIN_USERNAME);
				String sn = p.getProperty(PropertiesKeysRes.ADMIN_CERT_SN).toUpperCase();
				String dn = p.getProperty(PropertiesKeysRes.ADMIN_CERT_SUBJECTDN);
				String valid = p.getProperty(PropertiesKeysRes.ADMIN_VALIDATELEN);
				String st = p.getProperty(PropertiesKeysRes.ADMIN_CERTSTATUS);
				String tel = p.getProperty(PropertiesKeysRes.ADMIN_TELEPHONE);
				String email = p.getProperty(PropertiesKeysRes.ADMIN_EMAIL);
				String memo = p.getProperty(PropertiesKeysRes.ADMIN_REMARK);

				String[] ci = new String[] { sn, dn, valid, Utils.changeStatus(st) };
				if (!dataCertInfo.containsKey(name))
					dataCertInfo.put(name, new ArrayList<String[]>());
				dataCertInfo.get(name).add(ci);

				if (!dataUserInfo.containsKey(name)) {
					String[] ui = new String[] { tel, email, memo };
					dataUserInfo.put(name, ui);
				}
			}
		} catch (ServerException se) {
			log.errlog(reqType + " fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryAdminList") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog(reqType + " fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryAdminList"));
			mb.open();
		}
	}

	/**
	 * 菜单栏
	 * 
	 * @Author 江岩
	 * @Time 2019-06-04 20:23
	 * @version 1.0
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager mm = new MenuManager();
		MenuManager menu = new MenuManager(l.getString("exit"));
		menu.add(new ExitAction());
		mm.add(menu);
		return mm;
	}

	/**
	 * 菜单栏视图退出方法
	 * @Author 江岩
	 * @Time 2019-06-04 20:24
	 */
	class ExitAction extends Action {
		public ExitAction() {
			setText(l.getString("exit"));
		}

		public void run() {
			handleShellCloseEvent();
		}
	}

	/**
	 * 视图窗口标题栏命名
	 * @Author 江岩
	 * @Time 2019-06-04 20:25
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if ("SA".equalsIgnoreCase(currRole)) {
			shell.setText(l.getString("SA") + " ["+ subject +"]");
		} else if ("AA".equalsIgnoreCase(currRole)) {
			shell.setText(l.getString("AA") + " ["+ subject +"]");
		}
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	/**
	 * 重写窗口关闭事件,主动关闭按钮触发
	 * @Description 释放占用资源，并关闭视图
	 * @Author 江岩
	 * @Time 2019-06-04 19:20
	 * @version 1.0
	 */
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
