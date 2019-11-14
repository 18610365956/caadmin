package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
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
import org.eclipse.swt.events.SelectionListener;
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
 * BA 业务管理员视图
 * @Author 江岩    
 * @Time 2019-06-04 19:40
 */
public class BA extends ApplicationWindow {
	private TreeItem treeItem_root, treeItem_temp, treeItem_tempM, treeItem_RA;
	private TabFolder tabFolder;
	private Table table;
	private Tree tree_adminName, tree_op;
	private TreeItem root_adminName;
	private Text txt_email, txt_tel, txt_memo, txt_userName;
	private Button btnSubmitPerm, btn_modify;
	private Composite composite_1;
	private ApplicationWindow self;
	private boolean isAddtemplateM = false, isRAPermissions = false;
	private String adminName = null, subject;
	private HashMap<String, String[]> certInfoMap = new HashMap<String, String[]>();
	private HashMap<String, String[]> userInfoMap = new HashMap<String, String[]>();
	private HashMap<String, HashMap<String, HashSet<String>>> tempPermission = new HashMap<String, HashMap<String, HashSet<String>>>();
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();

	//private HashMap<String, HashMap<String, String>> templateM = new HashMap<String, HashMap<String, String>>();
	//private HashMap<String, HashMap<String, String>> RAM = new HashMap<String, HashMap<String, String>>();
	/**
	 * BA 构造方法
	 */
	public BA(String subject) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.MIN);
		addMenuBar();
		self = this;
		this.subject = subject;
	}

	/**
	 *  视图页面绘画
	 * @Author 江岩      
	 * @Time 2019-06-04 19:41
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		tabFolder = new TabFolder(container, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_tabFolder.widthHint = 1087;
		tabFolder.setLayoutData(gd_tabFolder);
		// tab11 BO
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(l.getString("BO"));
		composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);
		panel_BO(composite_1);

		refresh();
		return container;
	}

	/**
	 * 主视图页面绘画     
	 * @Author 江岩 
	 * @Time   2019-06-04 19:55
	 * @version 1.0
	 */
	private void panel_BO(Composite composite) {
		composite_1.setLayout(new FormLayout());
		Group group = new Group(composite, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(0, 545);
		fd_group.right = new FormAttachment(0, 232);
		fd_group.top = new FormAttachment(0);
		fd_group.left = new FormAttachment(0);
		group.setLayoutData(fd_group);
		group.setText(l.getString("list"));
		group.setLayout(new FormLayout());

		tree_adminName = new Tree(group, SWT.BORDER);
		FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(0, 431);
		fd_tree.right = new FormAttachment(0, 219);
		fd_tree.top = new FormAttachment(0, 7);
		fd_tree.left = new FormAttachment(0, 7);
		tree_adminName.setLayoutData(fd_tree);

		root_adminName = new TreeItem(tree_adminName, SWT.NULL);
		root_adminName.setText(l.getString("BO"));
		root_adminName.setExpanded(true);

		tree_adminName.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent me) {
				if (me.button == 1) {
					TreeItem item = tree_adminName.getItem(new Point(me.x, me.y));
					if (item == null || item.getParentItem() == null) {
						tree_adminName.deselectAll();
						return;
					} else {
						adminName = item.getText();
						refreshBOInfo(adminName);
					}
				}
			}
		});

		Button btn_refresh = new Button(group, SWT.NONE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(tree_adminName, 32);
		fd_btnNewButton.left = new FormAttachment(0, 7);
		btn_refresh.setLayoutData(fd_btnNewButton);
		btn_refresh.setText(l.getString("refresh"));
		btn_refresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					refresh();
				}
			}
		});

		Button btn_create = new Button(group, SWT.NONE);
		fd_btnNewButton.right = new FormAttachment(btn_create, -71);
		FormData fd_btn_create = new FormData();
		fd_btn_create.top = new FormAttachment(btn_refresh, -1, SWT.TOP);
		fd_btn_create.right = new FormAttachment(tree_adminName, 0, SWT.RIGHT);
		fd_btn_create.left = new FormAttachment(0, 151);
		btn_create.setLayoutData(fd_btn_create);
		btn_create.setText(l.getString("new"));
		btn_create.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					Panel_NewBO newBO = new Panel_NewBO(self);
					newBO.setBlockOnOpen(true);
					newBO.open();
					refresh();
				}
			}
		});

		Group group_1 = new Group(composite, SWT.NONE);
		FormData fd_group_1 = new FormData();
		fd_group_1.top = new FormAttachment(group, 0, SWT.TOP);
		fd_group_1.left = new FormAttachment(group, 6);
		fd_group_1.bottom = new FormAttachment(0, 258);
		fd_group_1.right = new FormAttachment(0, 836);
		group_1.setLayoutData(fd_group_1);
		group_1.setText(l.getString("cert_group"));
		group_1.setLayout(new FormLayout());

		table = new Table(group_1, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(0, 231);
		fd_table.right = new FormAttachment(0, 585);
		fd_table.top = new FormAttachment(0, 9);
		fd_table.left = new FormAttachment(0, 7);
		table.setLayoutData(fd_table);
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
						Env.client.sendRequest("REVOKEBO", p);
						MessageBox mb_1 = new MessageBox(getShell(), SWT.NONE);
						mb_1.setMessage(l.getString("Notice_succ_revokeAdminCert"));
						mb_1.open();
						refresh();
					} catch (ServerException se) {
						log.errlog("Revoke BO fail", se);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_revokeAdminCert") + "[" + se.getErrorNum() + "]:"
								+ se.getErrorMsg());
						mb.open();
					} catch (Exception ee) {
						log.errlog("Revoke BO fail", ee);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_revokeAdminCert"));
						mb.open();
					}
				}
			}
		});

		item_regainAdminCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Panel_RegainAdminCert getAdminCert = new Panel_RegainAdminCert(adminName, "BO");
				getAdminCert.setBlockOnOpen(true);
				getAdminCert.open();
				refresh();
			}
		});

		TableColumn tblclmn_serialNumber = new TableColumn(table, SWT.NONE);
		tblclmn_serialNumber.setWidth(88);
		tblclmn_serialNumber.setText(l.getString("SN"));

		TableColumn tblclmn_subject = new TableColumn(table, SWT.NONE);
		tblclmn_subject.setWidth(224);
		tblclmn_subject.setText(l.getString("subject"));

		TableColumn tblclmn_notAfter = new TableColumn(table, SWT.NONE);
		tblclmn_notAfter.setWidth(143);
		tblclmn_notAfter.setText(l.getString("validityDate"));

		TableColumn tblclmn_state = new TableColumn(table, SWT.NONE);
		tblclmn_state.setWidth(75);
		tblclmn_state.setText(l.getString("status"));

		Group group_2 = new Group(composite, SWT.NONE);
		FormData fd_group_2 = new FormData();
		fd_group_2.bottom = new FormAttachment(group, 0, SWT.BOTTOM);
		fd_group_2.left = new FormAttachment(group, 6);
		fd_group_2.right = new FormAttachment(0, 836);
		fd_group_2.top = new FormAttachment(0, 259);
		group_2.setLayoutData(fd_group_2);
		group_2.setText(l.getString("basicInfo"));
		GridLayout gridL = new GridLayout(2, false);
		gridL.marginTop = 10;
		gridL.marginBottom = 10;
		gridL.marginLeft = 20;
		gridL.marginRight = 10;
		gridL.verticalSpacing = 10;
		group_2.setLayout(gridL);

		Label lbl_uname = new Label(group_2, SWT.NONE);
		GridData gd_lbl_uname = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_uname.widthHint = 100;
		lbl_uname.setLayoutData(gd_lbl_uname);
		lbl_uname.setAlignment(SWT.RIGHT);
		lbl_uname.setText(l.getString("username") + ":");

		txt_userName = new Text(group_2, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_userName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_userName.widthHint = 245;
		txt_userName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent paramModifyEvent) {
				if (txt_userName.getText() == null || txt_userName.getText().length() <= 0) {
					btn_modify.setEnabled(false);
				} else {
					btn_modify.setEnabled(true);
				}
			}
		});
		txt_userName.setLayoutData(gd_txt_userName);

		Label lbl_email = new Label(group_2, SWT.NONE);
		lbl_email.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_email.setAlignment(SWT.RIGHT);
		lbl_email.setText(l.getString("email") + ":");

		txt_email = new Text(group_2, SWT.BORDER);
		GridData gd_txt_email = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_email.widthHint = 245;
		txt_email.setTextLimit(30);
		txt_email.setLayoutData(gd_txt_email);

		Label lbl_tel = new Label(group_2, SWT.NONE);
		lbl_tel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_tel.setAlignment(SWT.RIGHT);
		lbl_tel.setText(l.getString("phone") + ":");

		txt_tel = new Text(group_2, SWT.BORDER);
		GridData gd_txt_tel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_tel.widthHint = 245;
		txt_tel.setTextLimit(15);
		txt_tel.setLayoutData(gd_txt_tel);

		Label lbl_memo = new Label(group_2, SWT.NONE);
		lbl_memo.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lbl_memo.setAlignment(SWT.RIGHT);
		lbl_memo.setText(l.getString("memo") + ":");

		txt_memo = new Text(group_2, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_txt_memo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_memo.widthHint = 225;
		gd_txt_memo.heightHint = 89;
		txt_memo.setTextLimit(120);
		txt_memo.setLayoutData(gd_txt_memo);
		new Label(group_2, SWT.NONE);

		btn_modify = new Button(group_2, SWT.NONE);
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

					try {
						Env.client.sendRequest("UPDATEBO", p);
						MessageBox mb = new MessageBox(getShell(), SWT.OK);
						mb.setMessage(l.getString("Notice_succ_modifyAdmin"));
						mb.open();
						userInfoMap.get(adminName)[0] = tel;
						userInfoMap.get(adminName)[1] = email;
						userInfoMap.get(adminName)[2] = memo;
						//userInfoMap.get(adminName)[3] = memo;
						//refresh();
						TreeItem[] items = root_adminName.getItems();
						for (TreeItem item : items) {
							if (userName.equalsIgnoreCase(item.getText())) {
								refreshBOInfo(userName);
							}
						}

					} catch (ServerException se) {
						log.errlog("Update BO fail", se);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(
								l.getString("Notice_fail_updateBO") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
						mb.open();
					} catch (Exception e1) {
						log.errlog("Update BO fail", e1);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_updateBO"));
						mb.open();
					}
				}
			}
		});
		GridData gd_btn_modify = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_modify.widthHint = 72;
		btn_modify.setLayoutData(gd_btn_modify);
		btn_modify.setText(l.getString("modify"));

		Group group_3 = new Group(composite, SWT.NONE);
		FormData fd_group_3 = new FormData();
		fd_group_3.top = new FormAttachment(group, 0, SWT.TOP);
		fd_group_3.left = new FormAttachment(group_1, 6);
		fd_group_3.right = new FormAttachment(100, -10);
		fd_group_3.bottom = new FormAttachment(100);
		group_3.setLayoutData(fd_group_3);
		group_3.setText(l.getString("grant"));

		tree_op = new Tree(group_3, SWT.BORDER);
		tree_op.setBounds(10, 27, 212, 424);

		treeItem_root = new TreeItem(tree_op, SWT.NONE);
		treeItem_root.setText(l.getString("BO_permissions"));
		// 模板管理
		treeItem_tempM = new TreeItem(treeItem_root, SWT.NONE);
		treeItem_tempM.setText(l.getString("temp_manage"));
		// RA管理
		treeItem_RA = new TreeItem(treeItem_root, SWT.NONE);
		treeItem_RA.setText(l.getString("ra_manage"));
		// "模板操作权限"
		treeItem_temp = new TreeItem(treeItem_root, SWT.NONE);
		treeItem_temp.setText(l.getString("cert_permissions"));

		tree_op.addMouseListener(new MouseAdapter() {
			
			public void mouseDoubleClick(MouseEvent me) {
				tree_op.setMenu(null);
				System.out.println("222");
			}
			public void mouseDown(MouseEvent me) {
				/*
				if (me.button == 1) {
					tree_op.setMenu(null);
					selected_node = tree_op.getSelection()[0];
				}
				*/
				tree_op.setMenu(null);
				if (me.button == 3) {
					
					if (adminName != null && (certInfoMap.containsKey(adminName)
							&& l.getString("normal").equals(certInfoMap.get(adminName)[3]))) {
						if (tree_op.getItem(new Point(me.x, me.y)) == null) {
							tree_op.deselectAll();
							tree_op.setMenu(null);
							return;
						}
						/*
						if (selected_node != null && tree_op.getSelection()[0] != null && selected_node != tree_op.getSelection()[0]) {
							tree_op.setMenu(null);
							tree_op.setSelection(selected_node);
							return;
						}
						*/
						Menu menu = new Menu(tree_op);
						/*
						if (selected_node == null) {
							selected_node = tree_op.getSelection()[0];
						}
						*/
						TreeItem node = tree_op.getSelection()[0];
						

						if (treeItem_root.equals(node)) { // 根节点
							MenuItem del_all = new MenuItem(menu, SWT.PUSH);
							del_all.setText(l.getString("del_permissions")); //"删除所有权限"
							del_all.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									isAddtemplateM = false;
									isRAPermissions = false;
									tempPermission.remove(adminName);
									showPermission(adminName);
									btnSubmitPerm.setEnabled(true);
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							tree_op.setMenu(menu);
						} else if (treeItem_tempM.equals(node)) {
							MenuItem temp_grant = new MenuItem(menu, SWT.PUSH);
							temp_grant.setText(l.getString("add_tempM_Permissions")); //"添加模板管理权限"
							temp_grant.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									if (treeItem_temp.getItems().length > 0) {
										MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
										mb.setMessage(l.getString("Notice_temp_cert_grant"));
										mb.open();
										return;
									}
									isAddtemplateM = true;
									showPermission(adminName);
									btnSubmitPerm.setEnabled(true);
								}
								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							MenuItem del_all = new MenuItem(menu, SWT.PUSH);
							del_all.setText(l.getString("del_tempM_Permissions")); // "删除所有模板管理权限"
							del_all.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									isAddtemplateM = false;
									showPermission(adminName);
									btnSubmitPerm.setEnabled(true);
								}
								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							tree_op.setMenu(menu);
						} else if (treeItem_RA.equals(node)) {
							MenuItem temp_grant = new MenuItem(menu, SWT.PUSH);
							temp_grant.setText(l.getString("add_RA_Permissions")); //"添加RA管理权限"
							temp_grant.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									if (treeItem_temp.getItems().length > 0) {
										MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
										mb.setMessage(l.getString("Notice_ra_cert_grant"));
										mb.open();
										return;
									}
									isRAPermissions = true;
									showPermission(adminName);
									btnSubmitPerm.setEnabled(true);
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							MenuItem del_all = new MenuItem(menu, SWT.PUSH);
							del_all.setText(l.getString("del_RA_Permissions")); // "删除所有模板管理权限"
							del_all.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									isRAPermissions = false;
									showPermission(adminName);
									btnSubmitPerm.setEnabled(true);
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							tree_op.setMenu(menu);
						} else if (treeItem_temp.equals(node)) {
							MenuItem temp_grant = new MenuItem(menu, SWT.PUSH);
							temp_grant.setText(l.getString("add_temp_Permissions")); //"添加证书操作权限"
							temp_grant.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									if (isAddtemplateM) {
										MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
										mb.setMessage(l.getString("Notice_temp_cert_grant"));
										mb.open();
										return;
									}
									if (isRAPermissions) {
										MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
										mb.setMessage(l.getString("Notice_ra_cert_grant"));
										mb.open();
										return;
									}
									String tmpName = null;
									Panel_AddPermission pa = new Panel_AddPermission(tmpName);
									pa.setBlockOnOpen(true);
									int w = pa.open();
									if (w == 0) {
										String pa_tmpName = pa.selectedTempName;
										String pa_basedn = pa.baseDN;
										HashMap<String, HashSet<String>> perm = tempPermission.get(adminName);
										if (perm.containsKey(pa_tmpName)) {
											perm.get(pa_tmpName).add(pa_basedn);
										} else {
											HashSet<String> set = new HashSet<String>();
											set.add(pa_basedn);
											perm.put(pa_tmpName, set);
										}
										showPermission(adminName);
										btnSubmitPerm.setEnabled(true);
									}
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							MenuItem del_all = new MenuItem(menu, SWT.PUSH);
							del_all.setText(l.getString("del_tempPs")); // "删除所有模板操作权限"
							if (!tempPermission.containsKey(adminName)) {
								del_all.setEnabled(false);
							} else {
								del_all.setEnabled(true);
							}
							del_all.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									tempPermission.remove(adminName);
									showPermission(adminName);
									btnSubmitPerm.setEnabled(true);
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							tree_op.setMenu(menu);
						} else if (node.getParentItem() != null) {
							if (treeItem_temp.equals(node.getParentItem())) { // 模板名称
								MenuItem add_grant = new MenuItem(menu, SWT.PUSH);
								add_grant.setText(l.getString("add_grant")); // "添加授权"
								add_grant.addSelectionListener(new SelectionListener() {
									@Override
									public void widgetSelected(SelectionEvent arg0) {
										String tmpName = null;
										TreeItem node = tree_op.getSelection()[0];
										tmpName = node.getText();
										Panel_AddPermission pa = new Panel_AddPermission(tmpName);
										pa.setBlockOnOpen(true);
										int w = pa.open();
										if (w == 0) {
											String pa_tmpName = pa.selectedTempName;
											String pa_basedn = pa.baseDN;
											HashMap<String, HashSet<String>> perm = tempPermission.get(adminName);
											if (perm.containsKey(pa_tmpName)) {
												perm.get(pa_tmpName).add(pa_basedn);
											} else {
												HashSet<String> set = new HashSet<String>();
												set.add(pa_basedn);
												perm.put(pa_tmpName, set);
											}
											showPermission(adminName);
											btnSubmitPerm.setEnabled(true);
										}
									}

									@Override
									public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
									}
								});
								MenuItem del_grant = new MenuItem(menu, SWT.PUSH);
								del_grant.setText(l.getString("del_temp_permissions")); // "删除某模板下所有授权"
								del_grant.addSelectionListener(new SelectionListener() {
									@Override
									public void widgetSelected(SelectionEvent arg0) {
										TreeItem node = tree_op.getSelection()[0];
										HashMap<String, HashSet<String>> perm = tempPermission.get(adminName);
										HashSet<String> set = perm.get(node.getText());
										perm.remove(node.getText());
										set.clear();
										if (set.isEmpty()) {
											perm.remove(node.getText());
										}
										showPermission(adminName);
										btnSubmitPerm.setEnabled(true);
									}

									@Override
									public void widgetDefaultSelected(SelectionEvent arg0) {
									}
								});
								tree_op.setMenu(menu);
							} else if (node.getParentItem().getParentItem() != null
									&& treeItem_temp.equals(node.getParentItem().getParentItem())) { // baseDN
								MenuItem del_grant = new MenuItem(menu, SWT.PUSH);
								del_grant.setText(l.getString("delete_grant")); // "删除授权"
								del_grant.addSelectionListener(new SelectionListener() {
									@Override
									public void widgetSelected(SelectionEvent arg0) {
										TreeItem node = tree_op.getSelection()[0];
										HashMap<String, HashSet<String>> perm = tempPermission.get(adminName);
										HashSet<String> set = perm.get(node.getParentItem().getText());
										set.remove(node.getText());
										if (set.isEmpty()) {
											perm.remove(node.getParentItem().getText());
										}
										showPermission(adminName);
										btnSubmitPerm.setEnabled(true);
									}

									@Override
									public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
									}
								});
								tree_op.setMenu(menu);
							}
						}
					} else {
						tree_op.setMenu(null);
					}
				}
			}
		});

		btnSubmitPerm = new Button(group_3, SWT.NONE);
		btnSubmitPerm.setEnabled(false);
		btnSubmitPerm.setBounds(65, 466, 157, 29);
		btnSubmitPerm.setText(l.getString("modify_submit"));
		btnSubmitPerm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					setPermission("BO");
				}
			}
		});
	}

	// 对 BO管理员授权
	private void setPermission(String role) {
		StringBuffer sb = new StringBuffer("<permission><operations name=\"");

		if (isRAPermissions || isAddtemplateM) {
			if (isRAPermissions) {
				sb.append("PRIVILEGEBO,CREATERA,UPDATERA,REVOKERA,REQUESTRACERT,PRIVILEGERA");
			}
			if (isAddtemplateM) {
				if (isRAPermissions) {
					sb.append(",");
				}
				sb.append(
						"ADDTEMPLATE,UPDATETEMPLATE,DELETETEMPLATE,REVOKETEMPLATE,QUERYTEMPLATEDETAIL,QUERYTEMPLATELIST");
			}
			sb.append(" \"/></permission>");
		} else if (treeItem_temp.getItems().length > 0) {
			sb.append("APPLYUSER,MODIFYUSER \"/>");
			TreeItem ti = treeItem_temp;
			TreeItem[] templates = ti.getItems();
			for (TreeItem t : templates) {
				for (TreeItem b : t.getItems()) {
					sb.append(
							"<operations name=\"REQUESTCERT,GET2CODE,LOCKCERT,UNLOCKCERT,UPDATECERT,REVOKECERT,RECOVERENCCERTREQ \">")
							.append("<certtype>").append(t.getText()).append("</certtype>").append("<basedn>")
							.append(b.getText()).append("</basedn>").append("</operations>");
				}
			}
			sb.append("</permission>");
		} else {
			sb.append(" \"/></permission>");
		}

		Properties p = new Properties();
		p.setProperty(PropertiesKeysRes.ADMIN_USERNAME, adminName);
		p.setProperty(PropertiesKeysRes.ADMIN_BO_POWERS, sb.toString());
		try {
			Env.client.sendRequest("PRIVILEGEBO", p);
			MessageBox mb = new MessageBox(getShell(), SWT.OK);
			mb.setMessage(l.getString("Notice_succ_grant"));
			mb.open();
		} catch (ServerException se) {
			log.errlog("Grant BO fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_grant") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Grant BO fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_grant"));
			mb.open();
		}
		btnSubmitPerm.setEnabled(false);
	}

	// 填充管理员信息
	private void refreshBOInfo(String adminName) {
		table.removeAll();
		String[] certInfo = certInfoMap.get(adminName);
		if (certInfo != null && certInfo.length > 0) {
			TableItem tci = new TableItem(table, SWT.NULL);
			tci.setText(certInfo);
		}
		txt_userName.setText(adminName);
		txt_tel.setText(userInfoMap.get(adminName)[0].trim());
		txt_email.setText(userInfoMap.get(adminName)[1].trim());
		txt_memo.setText(userInfoMap.get(adminName)[2].trim());
		treeItem_root.setExpanded(true);
		// 查询并显示 授权信息
		getPermission(adminName); // 查询server的某个管理员权限
		showPermission(adminName);
	}

	// 重新加载页面信息
	void refresh() {
		getAdminsInfo(); // 查询CAServer
		root_adminName.removeAll();
		for (String adminName : userInfoMap.keySet()) {
			TreeItem ti = new TreeItem(root_adminName, SWT.NULL);
			ti.setText(adminName);
		}
		table.removeAll();
		txt_userName.setText("");
		txt_tel.setText("");
		txt_email.setText("");
		txt_memo.setText("");
		treeItem_temp.removeAll();
	}

	// 查询管理员信息
	private void getAdminsInfo() {
		try {
			Properties prop = new Properties();
			Response res = Env.client.sendRequest("QUERYBOLIST", prop);
			Properties[] ps = res.getItemData();
			certInfoMap.clear();
			userInfoMap.clear();
			for (Properties p : ps) {
				String name = p.getProperty(PropertiesKeysRes.ADMIN_USERNAME);
				String sn = p.getProperty(PropertiesKeysRes.ADMIN_CERT_SN).toUpperCase();
				String dn = p.getProperty(PropertiesKeysRes.ADMIN_CERT_SUBJECTDN);
				String valid = p.getProperty(PropertiesKeysRes.ADMIN_VALIDATELEN);
				String st = p.getProperty(PropertiesKeysRes.ADMIN_CERTSTATUS);
				String tel = p.getProperty(PropertiesKeysRes.ADMIN_TELEPHONE);
				String email = p.getProperty(PropertiesKeysRes.ADMIN_EMAIL);
				String memo = p.getProperty(PropertiesKeysRes.ADMIN_REMARK);
				String ips = p.getProperty(PropertiesKeysRes.IP_LIST, "");

				String[] certInfo = new String[] { sn, dn, valid, Utils.changeStatus(st) };
				String[] userInfo = new String[] { tel, email, memo, ips };
				certInfoMap.put(name, certInfo);
				userInfoMap.put(name, userInfo);
			}
		} catch (ServerException se) {
			log.errlog("Query BO list fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryBOList") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception e) {
			log.errlog("Query BO list fail", e);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryBOList"));
			mb.open();
		}
	}

	// 显示管理员权限
	private void showPermission(String adminName) {
		btnSubmitPerm.setEnabled(false);
		TreeItem root_RA = treeItem_RA;
		root_RA.removeAll();
		TreeItem root_tempM = treeItem_tempM;
		root_tempM.removeAll();
		TreeItem root_temp = treeItem_temp;
		root_temp.removeAll();
		if (isAddtemplateM) {
			TreeItem ti_Add = new TreeItem(root_tempM, SWT.NULL);
			ti_Add.setText(l.getString("op_addTemplate"));
			TreeItem ti_del = new TreeItem(root_tempM, SWT.NULL);
			ti_del.setText(l.getString("op_delTemplate"));
			TreeItem ti_update = new TreeItem(root_tempM, SWT.NULL);
			ti_update.setText(l.getString("op_updateTemplate"));
			TreeItem ti_query = new TreeItem(root_tempM, SWT.NULL);
			ti_query.setText(l.getString("QueryTemplate"));
			TreeItem ti_revoke = new TreeItem(root_tempM, SWT.NULL);
			ti_revoke.setText(l.getString("RevokeTemplate"));
		}
		if (isRAPermissions) {
			TreeItem ti_newRA = new TreeItem(root_RA, SWT.NULL);
			ti_newRA.setText(l.getString("op_newRA"));
			TreeItem ti_updateRA = new TreeItem(root_RA, SWT.NULL);
			ti_updateRA.setText(l.getString("op_modifyRA"));
			TreeItem ti_revokeRA = new TreeItem(root_RA, SWT.NULL);
			ti_revokeRA.setText(l.getString("op_revokeRACert"));
			TreeItem ti_reqRAcert = new TreeItem(root_RA, SWT.NULL);
			ti_reqRAcert.setText(l.getString("op_requestRACert"));
			TreeItem ti_grantRA = new TreeItem(root_RA, SWT.NULL);
			ti_grantRA.setText(l.getString("op_grantRA"));
		}
		if (tempPermission.containsKey(adminName)) {
			HashMap<String, HashSet<String>> perm = tempPermission.get(adminName);
			Set<String> templates = perm.keySet();
			for (String t : templates) {
				if (perm.get(t) == null || perm.get(t).size() == 0) {
					return;
				} else {
					TreeItem ti = new TreeItem(root_temp, SWT.NULL);
					ti.setText(t);
					for (String basedn : perm.get(t)) {
						TreeItem tiBasedn = new TreeItem(ti, SWT.NULL);
						tiBasedn.setText(basedn);
					}
				}
			}
		} else {
			HashMap<String, HashSet<String>> perm = new HashMap<String, HashSet<String>>();
			tempPermission.put(adminName, perm);
		}
	}

	// 查询管理员的权限
	private void getPermission(String name) {
		isRAPermissions = false;
		isAddtemplateM = false;
		if (tempPermission.containsKey((name))) {
			tempPermission.get(name).clear();
		} else {
			tempPermission.put(name, new HashMap<String, HashSet<String>>());
		}
		try {
			Properties p = new Properties();
			p.setProperty(PropertiesKeysRes.ADMIN_USERNAME, name);
			Response res = Env.client.sendRequest("GETPERMISSION", p);
			String priviStr = res.getP().getProperty(PropertiesKeysRes.ADMIN_BO_POWERS);

			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new ByteArrayInputStream(priviStr.getBytes("UTF-8")));
			Element root = doc.getRootElement();
			List operations = root.elements();

			if (operations != null) {
				Iterator it = operations.iterator();
				while (it.hasNext()) {
					Element e = (Element) it.next();
					if (e.elements() != null && e.elements().size() > 0) {
						String template = e.elementTextTrim("certtype");
						String basedn = e.elementTextTrim("basedn");
						HashMap<String, HashSet<String>> perm = tempPermission.get(name);
						if (!perm.containsKey(template)) {
							perm.put(template, new HashSet<String>());
						}
						perm.get(template).add(basedn);
					} else {
						String value = e.attributeValue("name");
						if (value.indexOf("CREATERA") != -1) {
							isRAPermissions = true;
						}
						if (value.indexOf("ADDTEMPLATE") != -1) {
							isAddtemplateM = true;
						}
					}
				}
			}
		} catch (ServerException se) {
			log.errlog("Query Permission fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(
					l.getString("Notice_fail_queryPermission") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Query Permission fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryPermission"));
			mb.open();
		}
	}

	// 菜单栏
	@Override
	protected MenuManager createMenuManager() {
		MenuManager mm = new MenuManager();
		MenuManager menu = new MenuManager(l.getString("exit"));
		menu.add(new ExitAction());
		mm.add(menu);
		return mm;
	}

	// 视图标题栏命名
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("BA") + " [" + subject + "]");
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	// 菜单栏退出事件
	class ExitAction extends Action {
		public ExitAction() {
			setText(l.getString("exit"));
		}

		public void run() {
			handleShellCloseEvent();
		}
	}

	/**
	 *  重写窗口关闭事件
	 * @Author 江岩      
	 * @Time 2019-06-04 19:58
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
			mb.setMessage(l.getString("Notice_exit") + "？");
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
