package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
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

import cn.com.infosec.netcert.caAdmin.ui.template.Panel_BasicTemplate;
import cn.com.infosec.netcert.caAdmin.ui.template.Panel_Template;
import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.PageUtil;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;
import cn.com.infosec.util.Base64;

/**
 * BO管理员
 * @Author 江岩
 * @Time 2019-06-04 20:00
 */
public class BO extends ApplicationWindow {
	private Panel_Template panel_Template;

	private ApplicationWindow self;
	private Control[] controls_Page;
	private Composite comp_p, comp_RA, comp_Cert, comp_Template;
	private Table table, tableRA, tableTemplate;
	private TabFolder tabFolder;
	private Label lbl_hex;
	private Tree treeRA, tree_op_ra;
	private Button btnSubmitPerm_ra, btn_modify;
	private TreeItem rootRA, trtm_opRootRA, treeItem_temp;
	private Combo combo_qryTmpl, combo_currPage, combo_state_RA;
	private MenuItem item_regain2Code, item_updateCert, item_lockCert, item_unlockCert, item_revokeCert, item_exportCert;
	private Text txt_email_RA, txt_tel_RA, txt_memo_RA, txt_userName_RA, text_templateName, txt_DN, txt_SN, text_IP;

	private int pageSize = 15;
	private Properties[] rows;
	private PageUtil pageUtil_Cert;
	private org.eclipse.swt.widgets.List list_raIp;
	private Set<String> ips = new HashSet<String>();
	private boolean isCertMPermissions = false, isAddtemplateM = false, isRAPermissions = false;
	private String g_adminName = null, curr_Page = "0", total_Page = "0", total_Row = "0", permStr, subject;
	private HashMap<String, String[]> certInfoRA = new HashMap<String, String[]>();
	private HashMap<String, String[]> userInfoRA = new HashMap<String, String[]>();
	private HashMap<String, HashMap<String, HashSet<String>>> tempPermission = new HashMap<String, HashMap<String, HashSet<String>>>();
	private Properties templateList = new Properties(); // 有授权的模板
	private Map<String, List<String>> templateListMap = new HashMap<String, List<String>>();
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	//private Label lbl_user;
	//private Properties[] row_temp_byUserName = null;
	//private int row_Count_byUserName = 0;

	/**
	 * BO构造方法
	 */
	public BO(String subject, String permStr) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.MIN);
		addMenuBar();
		pageUtil_Cert = new PageUtil();
		self = this;
		this.permStr = permStr;
		this.subject = subject;
		SAXReader saxReader = new SAXReader();
		try {
			Document doc = saxReader.read(new ByteArrayInputStream(permStr.getBytes("UTF-8")));
			Element root = doc.getRootElement();
			List operations = root.elements();

			if (operations != null) {
				Iterator it = operations.iterator();
				while (it.hasNext()) {
					Element r = (Element) it.next();
					if (r.elements() != null && r.elements().size() > 0) {
						String template = r.elementTextTrim("certtype");
						String basedn = r.elementTextTrim("basedn");
						this.templateList.setProperty(template, basedn);
						if (templateListMap.containsKey(template)) {
							List<String> list = templateListMap.get(template);
							list.add(basedn);
						} else {
							List<String> list = new ArrayList<String>();
							list.add(basedn);
							templateListMap.put(template, list);
						}
						isCertMPermissions = true;
					} else {
						String value = r.attributeValue("name");
						if (value.indexOf("CREATERA") != -1) {
							isRAPermissions = true;
						}
						if (value.indexOf("ADDTEMPLATE") != -1) {
							isAddtemplateM = true;
						}
					}
				}
			}
		} catch (Exception e) {
			log.errlog("saxReader read fail", e);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_error_saxReader"));
			mb.open();
		}
	}

	/**
	 * 绘画页面内容
	 * @Author 江岩
	 * @Time 2019-06-04 20:01
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginBottom = 5;
		container.setLayout(gridLayout);

		tabFolder = new TabFolder(container, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_tabFolder.heightHint = 545;
		gd_tabFolder.widthHint = 1097;
		tabFolder.setLayoutData(gd_tabFolder);
		// 证书管理  isCertMPermissions
		if (isCertMPermissions) {
			TabItem tabItem_Cert = new TabItem(tabFolder, SWT.NONE);
			tabItem_Cert.setText(l.getString("cert_manage"));
			comp_Cert = new Composite(tabFolder, SWT.NONE);
			tabItem_Cert.setControl(comp_Cert);
			panel_Cert(comp_Cert);

			refresh("Cert");
		}
		// tabItem_RA RA  isRAPermissions
		if (isRAPermissions) {
			TabItem tabItem_RA = new TabItem(tabFolder, SWT.NONE);
			tabItem_RA.setText(l.getString("RA_manage"));
			comp_RA = new Composite(tabFolder, SWT.NONE);
			tabItem_RA.setControl(comp_RA);
			panel_RA(comp_RA);
			refresh("RA");
		}
		// Template 模板管理  isAddtemplateM
		if (isAddtemplateM) {
			TabItem tabItem_Template = new TabItem(tabFolder, SWT.NONE);
			tabItem_Template.setText(l.getString("template_manage"));
			comp_Template = new Composite(tabFolder, SWT.NONE);
			tabItem_Template.setControl(comp_Template);
			panel_Template(comp_Template);
			refresh("Template");
		}
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				tabFolder.getSelection()[0].getText();
				if (l.getString("cert_manage").equalsIgnoreCase(tabFolder.getSelection()[0].getText())) {
					refresh("Cert");
				} else if (l.getString("RA_manage").equalsIgnoreCase(tabFolder.getSelection()[0].getText())) {
					refresh("RA");
				} else if (l.getString("template_manage").equalsIgnoreCase(tabFolder.getSelection()[0].getText())) {
					refresh("Template");
					queryTemplate();
				}
			}
		});

		return container;
	}

	/**
	 * 证书管理
	 * @Description 证书申请，下载，查询
	 * @Author 江岩
	 * @Time 2019-06-04 20:01
	 * @version 1.0
	 */
	private void panel_Cert(Composite compositeCert) {
		comp_Cert.setLayout(new FormLayout());

		Button btnApplyCert = new Button(compositeCert, SWT.NONE);
		FormData fd_btnApplyCert = new FormData();
		fd_btnApplyCert.bottom = new FormAttachment(0, 61);
		fd_btnApplyCert.right = new FormAttachment(0, 181);
		fd_btnApplyCert.top = new FormAttachment(0, 32);
		fd_btnApplyCert.left = new FormAttachment(0, 44);
		btnApplyCert.setLayoutData(fd_btnApplyCert);
		btnApplyCert.setText(l.getString("apply_cert"));
		btnApplyCert.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					Panel_ReqCert panel_Req = new Panel_ReqCert(templateListMap);
					panel_Req.setBlockOnOpen(true);
					panel_Req.open();
				}
			}
		});

		Button btnDownCert = new Button(compositeCert, SWT.NONE);
		FormData fd_btnDownCert = new FormData();
		fd_btnDownCert.bottom = new FormAttachment(0, 106);
		fd_btnDownCert.right = new FormAttachment(0, 181);
		fd_btnDownCert.top = new FormAttachment(0, 77);
		fd_btnDownCert.left = new FormAttachment(0, 44);
		btnDownCert.setLayoutData(fd_btnDownCert);
		btnDownCert.setText(l.getString("downloadCert"));
		btnDownCert.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					Panel_DownCert pd = new Panel_DownCert();
					pd.setBlockOnOpen(true);
					pd.open();
				}
			}
		});

		Label lbl_DN = new Label(compositeCert, SWT.NONE);
		FormData fd_lbl_DN = new FormData();
		fd_lbl_DN.left = new FormAttachment(btnApplyCert, 113);
		fd_lbl_DN.top = new FormAttachment(0, 30);
		lbl_DN.setLayoutData(fd_lbl_DN);
		lbl_DN.setAlignment(SWT.RIGHT);
		lbl_DN.setText(l.getString("subject") + ":");

		txt_DN = new Text(compositeCert, SWT.BORDER);
		fd_lbl_DN.right = new FormAttachment(100, -735);
		FormData fd_txt_DN = new FormData();
		fd_txt_DN.left = new FormAttachment(lbl_DN, 11);
		fd_txt_DN.top = new FormAttachment(lbl_DN, -3, SWT.TOP);
		txt_DN.setTextLimit(200);
		txt_DN.setLayoutData(fd_txt_DN);

		Label lbl_SN = new Label(compositeCert, SWT.NONE);
		FormData fd_lbl_SN = new FormData();
		fd_lbl_SN.top = new FormAttachment(btnDownCert, 0, SWT.TOP);
		fd_lbl_SN.left = new FormAttachment(btnDownCert, 96);
		fd_lbl_SN.right = new FormAttachment(lbl_DN, 0, SWT.RIGHT);
		lbl_SN.setLayoutData(fd_lbl_SN);
		lbl_SN.setAlignment(SWT.RIGHT);
		lbl_SN.setText(l.getString("SN") + ":");

		txt_SN = new Text(compositeCert, SWT.BORDER);
		FormData fd_txt_SN = new FormData();
		fd_txt_SN.left = new FormAttachment(lbl_SN, 11);
		fd_txt_SN.top = new FormAttachment(lbl_SN, -3, SWT.TOP);
		txt_SN.setTextLimit(20);
		txt_SN.setLayoutData(fd_txt_SN);

		lbl_hex = new Label(compositeCert, SWT.NONE);
		fd_txt_SN.right = new FormAttachment(lbl_hex, -6);
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.left = new FormAttachment(0, 516);
		fd_lblNewLabel_1.top = new FormAttachment(btnDownCert, 0, SWT.TOP);
		lbl_hex.setLayoutData(fd_lblNewLabel_1);
		lbl_hex.setText("(" + l.getString("Hexadecimal") + ")");
		/*
				lbl_user = new Label(compositeCert, SWT.NONE);
				FormData fd_lbl_user = new FormData();
				fd_lbl_user.right = new FormAttachment(0, 365);
				fd_lbl_user.top = new FormAttachment(0, 95);
				fd_lbl_user.left = new FormAttachment(0, 305);
				lbl_user.setLayoutData(fd_lbl_user);
				lbl_user.setAlignment(SWT.RIGHT);
				lbl_user.setText(l.getString("username") + ":");
		
				txt_user = new Text(compositeCert, SWT.BORDER);
				fd_lbl_user.bottom = new FormAttachment(txt_user, 0, SWT.BOTTOM);
				FormData fd_txt_user = new FormData();
				fd_txt_user.bottom = new FormAttachment(0, 116);
				fd_txt_user.right = new FormAttachment(0, 516);
				fd_txt_user.top = new FormAttachment(0, 94);
				fd_txt_user.left = new FormAttachment(0, 379);
				txt_user.setTextLimit(20);
				txt_user.setLayoutData(fd_txt_user);
		
				final Label lbl_notice = new Label(comp_Cert, SWT.NONE);
				FormData fd_lbl_notice = new FormData();
				fd_lbl_notice.right = new FormAttachment(txt_user, 61, SWT.RIGHT);
				fd_lbl_notice.bottom = new FormAttachment(lbl_user, -3, SWT.BOTTOM);
				fd_lbl_notice.left = new FormAttachment(txt_user, 11);
				lbl_notice.setLayoutData(fd_lbl_notice);
		
				String notice = l.getString("Notice_tip_queryByUserName");
				lbl_notice.setToolTipText(notice);
				lbl_notice.setBounds(267, 67, 27, 17);
				lbl_notice.setImage(new Image(getShell().getDisplay(), "res/tip.png"));
		*/
		Label lbl_tmpl = new Label(compositeCert, SWT.NONE);
		fd_txt_DN.right = new FormAttachment(100, -467);
		FormData fd_lbl_tmpl = new FormData();
		fd_lbl_tmpl.left = new FormAttachment(txt_DN, 29);
		fd_lbl_tmpl.top = new FormAttachment(lbl_DN, 0, SWT.TOP);
		lbl_tmpl.setLayoutData(fd_lbl_tmpl);
		lbl_tmpl.setAlignment(SWT.RIGHT);
		lbl_tmpl.setText(l.getString("template") + ":");

		combo_qryTmpl = new Combo(compositeCert, SWT.READ_ONLY);
		fd_lbl_tmpl.right = new FormAttachment(100, -370);
		FormData fd_combo_qryTmpl = new FormData();
		fd_combo_qryTmpl.top = new FormAttachment(lbl_DN, -3, SWT.TOP);
		fd_combo_qryTmpl.left = new FormAttachment(lbl_tmpl, 6);
		fd_combo_qryTmpl.right = new FormAttachment(100, -166);
		combo_qryTmpl.setLayoutData(fd_combo_qryTmpl);
		combo_qryTmpl.add("");
		for (Object o : this.templateList.keySet()) {
			combo_qryTmpl.add((String) o);
		}
		combo_qryTmpl.select(0);

		Button btnQry = new Button(compositeCert, SWT.NONE);
		fd_lblNewLabel_1.right = new FormAttachment(100, -439);
		FormData fd_btnQry = new FormData();
		fd_btnQry.top = new FormAttachment(btnDownCert, 1, SWT.TOP);
		fd_btnQry.left = new FormAttachment(lbl_hex, 166);
		fd_btnQry.right = new FormAttachment(combo_qryTmpl, 0, SWT.RIGHT);
		btnQry.setLayoutData(fd_btnQry);
		btnQry.setText(l.getString("queryCert"));

		// 绑定到 查询按钮
		btnQry.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listCert("1", false);
					if (!"0".equalsIgnoreCase(total_Row)) {
						MessageBox mb = new MessageBox(getShell(), SWT.NONE);
						mb.setMessage(l.getString("Notice_number_queryCert") + ":" + total_Row);
						mb.open();
						pageUtil_Cert.btn_Change(curr_Page, total_Page);
					}
				}
			}
		});

		table = new Table(compositeCert, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(0, 482);
		fd_table.right = new FormAttachment(0, 1087);
		fd_table.top = new FormAttachment(0, 132);
		fd_table.left = new FormAttachment(0, 10);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		// 右键菜单
		final Menu menu = new Menu(table);
		table.setMenu(menu);

		item_regain2Code = new MenuItem(menu, SWT.PUSH);
		item_regain2Code.setText(l.getString("regain2Code"));
		item_regain2Code.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Properties p = new Properties();
				TableItem item = table.getSelection()[0];
				String certSN = item.getText(2);
				p.setProperty(PropertiesKeysRes.ADMIN_CERT_SN, certSN);
				p.setProperty(PropertiesKeysRes.SUBJECTDN, item.getText(3));
				p.setProperty(PropertiesKeysRes.TEMPLATENAME, item.getText(7));
				try {
					Response resp = Env.client.sendRequest("GET2CODE", p);
					FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
					fd.setFilterExtensions(new String[] { "*.txt" });
					fd.setFileName("Ref_AuthCode");
					String f = fd.open();
					if (f != null) {
						FileWriter fw = new FileWriter(f);
						fw.write(item.getText(1));
						fw.write(Utils.newLine);
						fw.write(l.getString("refno") + ":" + resp.getP().getProperty(PropertiesKeysRes.REFNO));
						fw.write(Utils.newLine);
						fw.write(l.getString("authcode") + ":" + resp.getP().getProperty(PropertiesKeysRes.AUTHCODE));
						fw.close();
					}
				} catch (ServerException se) {
					log.errlog("Regain2Code fail", se);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(
							l.getString("Notice_fail_regain2Code") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
					mb.open();
				} catch (Exception ee) {
					log.errlog("Get2CODE fail", ee);
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_regain2Code"));
					mb.open();
				}
			}
		});
		item_lockCert = new MenuItem(menu, SWT.PUSH);
		item_lockCert.setText(l.getString("lockCert"));
		item_lockCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				MessageBox confirm = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				confirm.setMessage(l.getString("Notice_lockCert"));
				int r = confirm.open();
				if (SWT.YES == r) {
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.CERTSN,
							rows[table.getSelectionIndex()].getProperty(PropertiesKeysRes.CERTSN));
					try {
						Env.client.sendRequest("LOCKCERT", p);
						Properties row = rows[table.getSelectionIndex()];
						row.setProperty(PropertiesKeysRes.CERTSTATUS, "3");
						String seq = table.getItem(table.getSelectionIndex()).getText(0);
						table.getItem(table.getSelectionIndex())
								.setText(new String[] { seq, row.getProperty(PropertiesKeysRes.USERNAME),
										row.getProperty(PropertiesKeysRes.CERTSN),
										row.getProperty(PropertiesKeysRes.SUBJECTDN),
										Utils.changeStatus(row.getProperty(PropertiesKeysRes.CERTSTATUS)),
										row.getProperty(PropertiesKeysRes.NOTBEFORE),
										row.getProperty(PropertiesKeysRes.NOTAFTER),
										row.getProperty(PropertiesKeysRes.TEMPLATENAME) });
					} catch (ServerException se) {
						log.errlog("Lock cert fail", se);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(
								l.getString("Notice_fail_lockCert") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
						mb.open();
					} catch (Exception ee) {
						log.errlog("Lock cert fail", ee);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_lockCert"));
						mb.open();
					}
				}
			}
		});
		item_unlockCert = new MenuItem(menu, SWT.PUSH);
		item_unlockCert.setText(l.getString("unlockCert"));
		item_unlockCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				MessageBox confirm = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				confirm.setMessage(l.getString("Notice_unlockCert"));
				int r = confirm.open();
				if (SWT.YES == r) {
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.CERTSN,
							rows[table.getSelectionIndex()].getProperty(PropertiesKeysRes.CERTSN));
					try {
						Env.client.sendRequest("UNLOCKCERT", p);
						Properties row = rows[table.getSelectionIndex()];
						row.setProperty(PropertiesKeysRes.CERTSTATUS, "2");
						String seq = table.getItem(table.getSelectionIndex()).getText(0);
						table.getItem(table.getSelectionIndex())
								.setText(new String[] { seq, row.getProperty(PropertiesKeysRes.USERNAME),
										row.getProperty(PropertiesKeysRes.CERTSN),
										row.getProperty(PropertiesKeysRes.SUBJECTDN),
										Utils.changeStatus(row.getProperty(PropertiesKeysRes.CERTSTATUS)),
										row.getProperty(PropertiesKeysRes.NOTBEFORE),
										row.getProperty(PropertiesKeysRes.NOTAFTER),
										row.getProperty(PropertiesKeysRes.TEMPLATENAME) });
					} catch (ServerException se) {
						log.errlog("Unlock cert fail", se);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_unlockCert") + "[" + se.getErrorNum() + "]:"
								+ se.getErrorMsg());
						mb.open();
					} catch (Exception ee) {
						log.errlog("Unlock cert fail", ee);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_unlockCert"));
						mb.open();
					}
				}
			}
		});
		item_updateCert = new MenuItem(menu, SWT.PUSH);
		item_updateCert.setText(l.getString("updateCert"));
		item_updateCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Panel_UpdCert pu = new Panel_UpdCert(rows[table.getSelectionIndex()]);
				pu.setBlockOnOpen(true);
				int w = pu.open();
				if (w == 0) {
					listCert(curr_Page, false);
					pageUtil_Cert.btn_Change(curr_Page, total_Page);
				}
			}
		});
		item_revokeCert = new MenuItem(menu, SWT.PUSH);
		item_revokeCert.setText(l.getString("revokeCert"));
		item_revokeCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String certSN = rows[table.getSelectionIndex()].getProperty(PropertiesKeysRes.CERTSN);
				String certDN = rows[table.getSelectionIndex()].getProperty(PropertiesKeysRes.SUBJECTDN);
				Panel_RevokeUserCert revokeUserCert = new Panel_RevokeUserCert(certSN, certDN);
				revokeUserCert.setBlockOnOpen(true);
				int w = revokeUserCert.open();
				if (w == 0) {
					Properties row = rows[table.getSelectionIndex()];
					row.setProperty(PropertiesKeysRes.CERTSTATUS, "4");
					String seq = table.getItem(table.getSelectionIndex()).getText(0);
					table.getItem(table.getSelectionIndex()).setText(new String[] { seq,
							row.getProperty(PropertiesKeysRes.USERNAME), row.getProperty(PropertiesKeysRes.CERTSN),
							row.getProperty(PropertiesKeysRes.SUBJECTDN),
							Utils.changeStatus(row.getProperty(PropertiesKeysRes.CERTSTATUS)),
							row.getProperty(PropertiesKeysRes.NOTBEFORE), row.getProperty(PropertiesKeysRes.NOTAFTER),
							row.getProperty(PropertiesKeysRes.TEMPLATENAME) });
				}
			}
		});
		item_exportCert = new MenuItem(menu, SWT.PUSH);
		item_exportCert.setText(l.getString("exportCert"));
		item_exportCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					Properties p = new Properties();
					TableItem item = (TableItem) table.getSelection()[0];

					p.setProperty(PropertiesKeysRes.CERTSN, item.getText(2));
					try {
						Response resp = Env.client.sendRequest("GETCERTENTITY", p);
						Properties resp_p = resp.getP();
						String cer = resp_p.getProperty(PropertiesKeysRes.P7DATA);
						
						FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
						fd.setFilterExtensions(new String[] { "*.cer" });
						fd.setFileName(item.getText(1));
						String f = fd.open();
						if (f != null) {
							byte[] bs_der_cer = Base64.decode(cer);
							FileOutputStream fos = new FileOutputStream(f);
							fos.write(bs_der_cer);
							fos.close();
							MessageBox mb = new MessageBox(getShell(), SWT.OK);
							mb.setMessage(l.getString("Notice_succ_exportCert"));
							mb.open();
						}
					} catch (Exception e) {
						log.errlog("GETCERTENTITY fail", e);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_exportCert"));
						mb.open();
					}
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent me) {
				int idx = table.getSelectionIndex();
				if (idx == -1) {
					item_lockCert.setEnabled(false);
					item_unlockCert.setEnabled(false);
					item_regain2Code.setEnabled(false);
					item_updateCert.setEnabled(false);
					item_revokeCert.setEnabled(false);
					item_exportCert.setEnabled(false);
				} else {
					String status = rows[idx].getProperty(PropertiesKeysRes.CERTSTATUS);
					switch (Integer.parseInt(status)) {
					case 1: //未下载
						item_lockCert.setEnabled(false);
						item_unlockCert.setEnabled(false);
						item_regain2Code.setEnabled(true);
						item_updateCert.setEnabled(false);
						item_revokeCert.setEnabled(true);
						item_exportCert.setEnabled(false);
						break;
					case 2: // 正常
						item_lockCert.setEnabled(true);
						item_unlockCert.setEnabled(false);
						item_regain2Code.setEnabled(false);
						item_updateCert.setEnabled(true);
						item_revokeCert.setEnabled(true);
						item_exportCert.setEnabled(true);
						break;
					case 3: // 锁定
						item_lockCert.setEnabled(false);
						item_unlockCert.setEnabled(true);
						item_regain2Code.setEnabled(false);
						item_updateCert.setEnabled(false);
						item_revokeCert.setEnabled(true);
						item_exportCert.setEnabled(false);
						break;
					case 4: //作废
						item_lockCert.setEnabled(false);
						item_unlockCert.setEnabled(false);
						item_regain2Code.setEnabled(false);
						item_updateCert.setEnabled(false);
						item_revokeCert.setEnabled(false);
						item_exportCert.setEnabled(false);
						break;
					}
				}
			}
		});

		TableColumn clm_seq = new TableColumn(table, SWT.NONE);
		clm_seq.setWidth(55);
		clm_seq.setText(l.getString("table_sn"));

		TableColumn clm_user = new TableColumn(table, SWT.NONE);
		clm_user.setWidth(100);
		clm_user.setText(l.getString("username"));

		TableColumn clm_sn = new TableColumn(table, SWT.NONE);
		clm_sn.setWidth(130);
		clm_sn.setText(l.getString("SN"));

		TableColumn clm_dn = new TableColumn(table, SWT.NONE);
		clm_dn.setWidth(305);
		clm_dn.setText(l.getString("subject"));

		TableColumn clm_state = new TableColumn(table, SWT.NONE);
		clm_state.setWidth(82);
		clm_state.setText(l.getString("status"));

		TableColumn clm_notBefore = new TableColumn(table, SWT.NONE);
		clm_notBefore.setWidth(118);
		clm_notBefore.setText(l.getString("notBefore"));

		TableColumn clm_notAfter = new TableColumn(table, SWT.NONE);
		clm_notAfter.setWidth(118);
		clm_notAfter.setText(l.getString("notAfter"));

		TableColumn clm_tmpl = new TableColumn(table, SWT.NONE);
		clm_tmpl.setWidth(128);
		clm_tmpl.setText(l.getString("template"));

		// 分页模块
		Composite comp_Page = new Composite(compositeCert, SWT.NONE);
		FormData fd_comp_Page = new FormData();
		fd_comp_Page.bottom = new FormAttachment(100, -25);
		fd_comp_Page.top = new FormAttachment(table, 6);
		fd_comp_Page.right = new FormAttachment(100, -29);
		fd_comp_Page.left = new FormAttachment(0, 865);
		comp_Page.setLayoutData(fd_comp_Page);
		comp_p = (Composite) pageUtil_Cert.page(comp_Page);
		GridLayout gd_comp_p = new GridLayout(7, false);
		gd_comp_p.horizontalSpacing = 8;
		comp_Page.setLayout(gd_comp_p);
		controls_Page = comp_p.getChildren();

		// 修改当前页
		combo_currPage = (Combo) controls_Page[5];
		GridData gridData = new GridData();
		gridData.widthHint = 30;
		gridData.heightHint = 25;
		combo_currPage.setLayoutData(gridData);
		combo_currPage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listCert(combo_currPage.getText().trim(), true);
					pageUtil_Cert.btn_Change(curr_Page, total_Page);
				}
			}
		});
		// lblBegin 第一页
		controls_Page[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listCert("1", true);
					pageUtil_Cert.btn_Change(curr_Page, total_Page);
				}
			}
		});
		// lblBack 上一页
		controls_Page[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listCert(String.valueOf(Integer.parseInt(curr_Page) - 1), true);
					pageUtil_Cert.btn_Change(curr_Page, total_Page);
				}
			}
		});
		// lblForward 下一页
		controls_Page[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listCert(String.valueOf(Integer.parseInt(curr_Page) + 1), true);
					pageUtil_Cert.btn_Change(curr_Page, total_Page);
				}
			}
		});
		// lblEnd 最后一页
		controls_Page[3].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					listCert(total_Page, true);
					pageUtil_Cert.btn_Change(curr_Page, total_Page);
				}
			}
		});
	}

	/**
	 * RA管理
	 * @Author 江岩
	 * @Time 2019-06-04 20:02
	 * @version 1.0
	 */
	private void panel_RA(Composite compositeRA) {
		comp_RA.setLayout(new FormLayout());

		Group groupRA = new Group(compositeRA, SWT.NONE);
		groupRA.setLayout(null);
		FormData fd_groupRA = new FormData();
		fd_groupRA.bottom = new FormAttachment(100, -18);
		fd_groupRA.top = new FormAttachment(0);
		fd_groupRA.left = new FormAttachment(0);
		groupRA.setLayoutData(fd_groupRA);
		groupRA.setText(l.getString("list"));

		treeRA = new Tree(groupRA, SWT.BORDER);
		treeRA.setBounds(10, 21, 214, 459);

		rootRA = new TreeItem(treeRA, SWT.NULL);
		rootRA.setText(l.getString("RA"));
		rootRA.setExpanded(false);

		treeRA.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent me) {
				if (me.button == 1) {
					TreeItem item = treeRA.getItem(new Point(me.x, me.y));
					if (item == null || item.getParentItem() == null) {
						treeRA.deselectAll();
						return;
					}
					g_adminName = item.getText();
					tableRA.removeAll();
					String[] certInfo = certInfoRA.get(g_adminName);
					if (certInfo != null && certInfo.length > 0) {
						TableItem tci = new TableItem(tableRA, SWT.NULL);
						tci.setText(certInfo);
					}
					txt_userName_RA.setText(g_adminName);
					txt_tel_RA.setText(userInfoRA.get(g_adminName)[0].trim());
					txt_email_RA.setText(userInfoRA.get(g_adminName)[1].trim());
					txt_memo_RA.setText(userInfoRA.get(g_adminName)[2].trim());
					combo_state_RA.select(Integer.parseInt(userInfoRA.get(g_adminName)[3]));
					// ip白名单
					String ip_str = userInfoRA.get(g_adminName)[4];
					list_raIp.removeAll();
					ips.clear();
					if (ip_str != null && ip_str.length() > 0) {
						String[] ip_strs = ip_str.split(",");
						for (String ip : ip_strs) {
							ips.add(ip);
							list_raIp.add(ip);
						}
					}
					// 查询并显示 授权信息
					if (l.getString("normal").equals(certInfoRA.get(g_adminName)[3])) {
						getPermission(g_adminName);
						showPermission(g_adminName);
					}
				}
			}
		});

		Button btnNewRA = new Button(groupRA, SWT.NONE);
		btnNewRA.setBounds(144, 490, 80, 27);
		btnNewRA.setText(l.getString("new"));
		btnNewRA.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					Panel_NewRA panel_NewRA = new Panel_NewRA(self);
					panel_NewRA.setBlockOnOpen(true);
					int w = panel_NewRA.open();
					if (w == 0) {
						refresh("RA");
					}
				}
			}
		});
		Group group_ra = new Group(compositeRA, SWT.NONE);
		fd_groupRA.right = new FormAttachment(group_ra, -6);
		FormData fd_group_ra = new FormData();
		fd_group_ra.left = new FormAttachment(0, 242);
		fd_group_ra.top = new FormAttachment(0);
		group_ra.setLayoutData(fd_group_ra);
		group_ra.setText(l.getString("cert"));
		group_ra.setLayout(new FormLayout());

		tableRA = new Table(group_ra, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_tableRA = new FormData();
		fd_tableRA.bottom = new FormAttachment(0, 220);
		fd_tableRA.right = new FormAttachment(0, 625);
		fd_tableRA.top = new FormAttachment(0, 9);
		fd_tableRA.left = new FormAttachment(0, 7);
		tableRA.setLayoutData(fd_tableRA);
		tableRA.setHeaderVisible(true);
		tableRA.setLinesVisible(true);

		final Menu menu_RA = new Menu(tableRA);

		final MenuItem item_revokeAdminCert = new MenuItem(menu_RA, SWT.PUSH);
		item_revokeAdminCert.setText(l.getString("revokeAdminCert"));
		final MenuItem item_regainAdminCert = new MenuItem(menu_RA, SWT.PUSH);
		item_regainAdminCert.setText(l.getString("regainAdminCert"));

		tableRA.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent me) {
				if (me.button == 3) {
					TableItem[] items = tableRA.getSelection();
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
					tableRA.setMenu(menu_RA);
				}
			}
		});

		item_revokeAdminCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				mb.setMessage(l.getString("Notice_revokeAdminCert") + "?");
				int r = mb.open();
				if (SWT.YES == r) {
					TableItem item = (TableItem) tableRA.getSelection()[0];
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.ADMIN_CERT_SN, item.getText());
					try {
						Env.client.sendRequest("REVOKERA", p);
						MessageBox mb_1 = new MessageBox(getShell(), SWT.NONE);
						mb_1.setMessage(l.getString("Notice_succ_revokeAdminCert"));
						mb_1.open();
						refresh("RA");
					} catch (ServerException se) {
						log.errlog("Revoke RA fail", se);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_revokeAdminCert") + "[" + se.getErrorNum() + "]:"
								+ se.getErrorMsg());
						mb.open();
					} catch (Exception ee) {
						log.errlog("Revoke RA fail", ee);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_revokeAdminCert"));
						mb.open();
					}
				}
			}
		});

		item_regainAdminCert.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Panel_GetRACert getAdminCert = new Panel_GetRACert(g_adminName);
				getAdminCert.setBlockOnOpen(true);
				int w = getAdminCert.open();
				if (w == 0) {
					refresh("RA");
				}
			}
		});

		TableColumn tblclmn_sn_ra = new TableColumn(tableRA, SWT.NONE);
		tblclmn_sn_ra.setWidth(119);
		tblclmn_sn_ra.setText(l.getString("SN"));

		TableColumn tblclmn_dn_ra = new TableColumn(tableRA, SWT.NONE);
		tblclmn_dn_ra.setWidth(241);
		tblclmn_dn_ra.setText(l.getString("subject"));

		TableColumn tblclmn_notAfter_ra = new TableColumn(tableRA, SWT.NONE);
		tblclmn_notAfter_ra.setWidth(149);
		tblclmn_notAfter_ra.setText(l.getString("validityDate"));

		TableColumn tblclmn_state_ra = new TableColumn(tableRA, SWT.NONE);
		tblclmn_state_ra.setWidth(88);
		tblclmn_state_ra.setText(l.getString("status"));

		Group group_2_ra = new Group(compositeRA, SWT.NONE);
		fd_group_ra.bottom = new FormAttachment(group_2_ra, -6);
		FormData fd_group_2_ra = new FormData();
		fd_group_2_ra.left = new FormAttachment(groupRA, 6);

		Button btn_refresh = new Button(groupRA, SWT.NONE);
		btn_refresh.setBounds(10, 490, 80, 27);
		btn_refresh.setText(l.getString("refresh"));
		btn_refresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					refresh("RA");
				}
			}
		});

		fd_group_2_ra.bottom = new FormAttachment(100, -18);
		fd_group_2_ra.top = new FormAttachment(0, 252);
		group_2_ra.setLayoutData(fd_group_2_ra);
		group_2_ra.setText(l.getString("basicInfo"));

		GridLayout gridL = new GridLayout(4, false);
		gridL.marginTop = 10;
		gridL.marginBottom = 10;
		gridL.marginLeft = 10;
		gridL.marginRight = 10;
		gridL.verticalSpacing = 10;
		group_2_ra.setLayout(gridL);

		Label lbl_uname_ra = new Label(group_2_ra, SWT.NONE);
		GridData gd_lbl_uname_ra = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_uname_ra.widthHint = 66;
		lbl_uname_ra.setLayoutData(gd_lbl_uname_ra);
		lbl_uname_ra.setAlignment(SWT.RIGHT);
		lbl_uname_ra.setText(l.getString("username") + ":");

		txt_userName_RA = new Text(group_2_ra, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_txt_userName_RA = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_userName_RA.widthHint = 210;
		txt_userName_RA.setLayoutData(gd_txt_userName_RA);
		txt_userName_RA.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent paramModifyEvent) {
				if (txt_userName_RA.getText() == null || txt_userName_RA.getText().length() <= 0) {
					btn_modify.setEnabled(false);
				} else {
					btn_modify.setEnabled(true);
				}
			}
		});

		Label lblNewLabel = new Label(group_2_ra, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 17;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("");

		Group grpIpWhiteList = new Group(group_2_ra, SWT.NONE);
		GridData gd_grpIpWhiteList = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 5);
		gd_grpIpWhiteList.widthHint = 289;
		gd_grpIpWhiteList.heightHint = 172;
		grpIpWhiteList.setLayoutData(gd_grpIpWhiteList);
		grpIpWhiteList.setText(l.getString("IPwhiteList"));

		Label lbl_ip_ra = new Label(grpIpWhiteList, SWT.NONE);
		lbl_ip_ra.setBounds(10, 25, 69, 17);
		lbl_ip_ra.setAlignment(SWT.RIGHT);
		lbl_ip_ra.setText(l.getString("IP_list") + ":");

		list_raIp = new org.eclipse.swt.widgets.List(grpIpWhiteList, SWT.BORDER | SWT.V_SCROLL);
		list_raIp.setBounds(85, 21, 201, 93);

		Label lbl_ip = new Label(grpIpWhiteList, SWT.NONE);
		lbl_ip.setBounds(56, 123, 23, 17);
		lbl_ip.setText("IP:");

		text_IP = new Text(grpIpWhiteList, SWT.BORDER);
		text_IP.setBounds(85, 120, 201, 23);
		text_IP.setTextLimit(15);

		Button btn_add = new Button(grpIpWhiteList, SWT.NONE);
		btn_add.setBounds(121, 149, 72, 27);
		btn_add.setText(l.getString("add"));
		btn_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String ip = text_IP.getText().trim();
				if (!Utils.checkIp(ip)) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_error_IP"));
					mb.open();
					return;
				}
				if (!ips.contains(ip)) {
					ips.add(ip);
					list_raIp.add(ip);
				} else {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_isExist_IP"));
					mb.open();
				}
				text_IP.setText("");
			}
		});

		Button btn_delete = new Button(grpIpWhiteList, SWT.NONE);
		btn_delete.setBounds(206, 149, 80, 27);
		btn_delete.setText(l.getString("delete"));
		btn_delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (list_raIp.getSelectionIndex() == -1) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_not_chooseIP"));
					mb.open();
					return;
				}
				String ip = list_raIp.getSelection()[0];
				ips.remove(ip);
				list_raIp.remove(list_raIp.getSelectionIndex());
			}
		});

		Label lbl_email_ra = new Label(group_2_ra, SWT.NONE);
		lbl_email_ra.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_email_ra.setAlignment(SWT.RIGHT);
		lbl_email_ra.setText(l.getString("email") + ":");

		txt_email_RA = new Text(group_2_ra, SWT.BORDER);
		GridData gd_txt_email_RA = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_email_RA.widthHint = 210;
		txt_email_RA.setTextLimit(30);
		txt_email_RA.setLayoutData(gd_txt_email_RA);
		new Label(group_2_ra, SWT.NONE);

		Label lbl_tel_ra = new Label(group_2_ra, SWT.NONE);
		lbl_tel_ra.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_tel_ra.setAlignment(SWT.RIGHT);
		lbl_tel_ra.setText(l.getString("phone") + ":");

		txt_tel_RA = new Text(group_2_ra, SWT.BORDER);
		GridData gd_txt_tel_RA = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_tel_RA.widthHint = 210;
		txt_tel_RA.setTextLimit(15);
		txt_tel_RA.setLayoutData(gd_txt_tel_RA);
		new Label(group_2_ra, SWT.NONE);

		Label lbl_memo_ra = new Label(group_2_ra, SWT.NONE);
		lbl_memo_ra.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lbl_memo_ra.setAlignment(SWT.RIGHT);
		lbl_memo_ra.setText(l.getString("memo") + ":");

		txt_memo_RA = new Text(group_2_ra, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		GridData gd_txt_memo_RA = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_txt_memo_RA.heightHint = 48;
		gd_txt_memo_RA.widthHint = 192;
		txt_memo_RA.setTextLimit(120);
		txt_memo_RA.setLayoutData(gd_txt_memo_RA);
		new Label(group_2_ra, SWT.NONE);

		Label lbl_state = new Label(group_2_ra, SWT.NONE);
		lbl_state.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_state.setText(l.getString("status") + ":");

		combo_state_RA = new Combo(group_2_ra, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 40;
		combo_state_RA.setLayoutData(gd_combo);
		combo_state_RA.add(l.getString("normal"));
		combo_state_RA.add(l.getString("disable"));

		new Label(group_2_ra, SWT.NONE);
		new Label(group_2_ra, SWT.NONE);
		new Label(group_2_ra, SWT.NONE);
		new Label(group_2_ra, SWT.NONE);

		btn_modify = new Button(group_2_ra, SWT.NONE);
		btn_modify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					String name = txt_userName_RA.getText();
					String email = txt_email_RA.getText();
					String tel = txt_tel_RA.getText().trim();
					String memo = txt_memo_RA.getText();
					int countIP = list_raIp.getItemCount();
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
					if (countIP < 1) {
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_null_IPList"));
						mb.open();
						return;
					}
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.ADMIN_USERNAME, txt_userName_RA.getText().trim());
					p.setProperty(PropertiesKeysRes.ADMIN_TELEPHONE, tel);
					p.setProperty(PropertiesKeysRes.ADMIN_EMAIL, email);
					p.setProperty(PropertiesKeysRes.ADMIN_REMARK, memo);
					p.setProperty(PropertiesKeysRes.STATUS, String.valueOf(combo_state_RA.getSelectionIndex()));

					StringBuffer sb_ips = new StringBuffer();
					for (String s : list_raIp.getItems()) {
						sb_ips.append(s).append(",");
					}
					p.setProperty(PropertiesKeysRes.IP_LIST, sb_ips.substring(0, sb_ips.length() - 1));

					try {
						Env.client.sendRequest("UPDATERA", p);
						MessageBox mb = new MessageBox(getShell(), SWT.OK);
						mb.setMessage(l.getString("Notice_succ_updateRA"));
						mb.open();
						userInfoRA.get(name)[0] = tel;
						userInfoRA.get(name)[1] = email;
						userInfoRA.get(name)[2] = memo;
						userInfoRA.get(name)[3] = String.valueOf(combo_state_RA.getSelectionIndex());
						userInfoRA.get(name)[4] = sb_ips.toString();

						//refresh("RA");

						//tableRA.removeAll();
						/*
						String[] certInfo = certInfoRA.get(name);
						if (certInfo != null && certInfo.length > 0) {
							TableItem tci = new TableItem(tableRA, SWT.NULL);
							tci.setText(certInfo);
						}
						*/
						txt_userName_RA.setText(name);
						txt_tel_RA.setText(userInfoRA.get(name)[0].trim());
						txt_email_RA.setText(userInfoRA.get(name)[1].trim());
						txt_memo_RA.setText(userInfoRA.get(name)[2].trim());
						combo_state_RA.select(Integer.parseInt(userInfoRA.get(name)[3]));
						// ip白名单
						String ip_str = userInfoRA.get(name)[4];
						list_raIp.removeAll();
						ips.clear();
						if (sb_ips != null && ip_str.length() > 0) {
							String[] ip_strs = ip_str.toString().split(",");
							for (String ip : ip_strs) {
								ips.add(ip);
								list_raIp.add(ip);
							}
						}
						// 查询并显示 授权信息
						if (l.getString("normal").equals(certInfoRA.get(name)[3])) {
							getPermission(name);
							showPermission(name);
						}

					} catch (ServerException se) {
						log.errlog("Update RA fail", se);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(
								l.getString("Notice_fail_updateRA") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
						mb.open();
					} catch (Exception e1) {
						log.errlog("Update RA fail", e1);
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_updateRA"));
						mb.open();
					}
				}
			}
		});

		GridData gd_btn_modify = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_modify.widthHint = 66;
		btn_modify.setLayoutData(gd_btn_modify);
		btn_modify.setText(l.getString("modify"));

		Group group_3_ra = new Group(compositeRA, SWT.NONE);
		fd_group_2_ra.right = new FormAttachment(group_3_ra, -6);
		fd_group_ra.right = new FormAttachment(group_3_ra, -6);
		FormData fd_group_3_ra = new FormData();
		fd_group_3_ra.bottom = new FormAttachment(100, -18);
		fd_group_3_ra.top = new FormAttachment(0);
		fd_group_3_ra.left = new FormAttachment(0, 886);
		fd_group_3_ra.right = new FormAttachment(100, -5);
		group_3_ra.setLayoutData(fd_group_3_ra);
		group_3_ra.setText(l.getString("grant"));

		tree_op_ra = new Tree(group_3_ra, SWT.BORDER);
		tree_op_ra.setBounds(10, 27, 188, 448);

		trtm_opRootRA = new TreeItem(tree_op_ra, SWT.NONE);
		trtm_opRootRA.setText(l.getString("operation_grant"));

		treeItem_temp = new TreeItem(trtm_opRootRA, SWT.NONE);
		treeItem_temp.setText(l.getString("cert_permissions"));

		tree_op_ra.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent me) {
				if (me.button == 3) {
					if (g_adminName != null && (certInfoRA.containsKey(g_adminName)
							&& l.getString("normal").equals(certInfoRA.get(g_adminName)[3]))) {
						Menu menu_RA = new Menu(tree_op_ra);
						if (tree_op_ra.getItem(new Point(me.x, me.y)) == null) {
							tree_op_ra.deselectAll();
							tree_op_ra.setMenu(null);
							return;
						}
						TreeItem node = tree_op_ra.getSelection()[0];
						if (trtm_opRootRA.equals(node)) { // 根节点
							MenuItem del_all = new MenuItem(menu_RA, SWT.PUSH);
							del_all.setText(l.getString("del_permissions")); //"删除所有权限"
							del_all.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									tempPermission.remove(g_adminName);
									showPermission(g_adminName);
									btnSubmitPerm_ra.setEnabled(true);
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							tree_op_ra.setMenu(menu_RA);
						} else if (treeItem_temp.equals(node)) {
							MenuItem temp_grant = new MenuItem(menu_RA, SWT.PUSH);
							temp_grant.setText(l.getString("add_temp_Permissions")); //"添加模板操作权限"
							temp_grant.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									String tmpName = null;
									Panel_AddPermission pa = new Panel_AddPermission(tmpName);
									pa.setBlockOnOpen(true);
									int w = pa.open();
									if (w == 0) {
										String pa_tmpName = pa.selectedTempName;
										String pa_basedn = pa.baseDN;
										HashMap<String, HashSet<String>> perm = tempPermission.get(g_adminName);
										if (perm.containsKey(pa_tmpName)) {
											perm.get(pa_tmpName).add(pa_basedn);
										} else {
											HashSet<String> set = new HashSet<String>();
											set.add(pa_basedn);
											perm.put(pa_tmpName, set);
										}
										showPermission(g_adminName);
										btnSubmitPerm_ra.setEnabled(true);
									}
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							MenuItem del_all = new MenuItem(menu_RA, SWT.PUSH);
							del_all.setText(l.getString("del_tempPs")); // "删除所有模板操作权限"
							if (!tempPermission.containsKey(g_adminName)) {
								del_all.setEnabled(false);
							} else {
								del_all.setEnabled(true);
							}
							del_all.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									tempPermission.remove(g_adminName);
									showPermission(g_adminName);
									btnSubmitPerm_ra.setEnabled(true);
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							tree_op_ra.setMenu(menu_RA);
						} else if (node.getParentItem() != null) {
							if (treeItem_temp.equals(node.getParentItem())) { // 模板名称
								MenuItem add_grant = new MenuItem(menu_RA, SWT.PUSH);
								add_grant.setText(l.getString("add_grant")); // "添加授权"
								add_grant.addSelectionListener(new SelectionListener() {
									@Override
									public void widgetSelected(SelectionEvent arg0) {
										String tmpName = null;
										TreeItem node = tree_op_ra.getSelection()[0];
										tmpName = node.getText();
										Panel_AddPermission pa = new Panel_AddPermission(tmpName);
										pa.setBlockOnOpen(true);
										int w = pa.open();
										if (w == 0) {
											String pa_tmpName = pa.selectedTempName;
											String pa_basedn = pa.baseDN;
											HashMap<String, HashSet<String>> perm = tempPermission.get(g_adminName);
											if (perm.containsKey(pa_tmpName)) {
												perm.get(pa_tmpName).add(pa_basedn);
											} else {
												HashSet<String> set = new HashSet<String>();
												set.add(pa_basedn);
												perm.put(pa_tmpName, set);
											}
											showPermission(g_adminName);
											btnSubmitPerm_ra.setEnabled(true);
										}
									}

									@Override
									public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
									}
								});
								MenuItem del_grant = new MenuItem(menu_RA, SWT.PUSH);
								del_grant.setText(l.getString("del_temp_permissions")); // "删除某模板下所有授权"
								del_grant.addSelectionListener(new SelectionListener() {
									@Override
									public void widgetSelected(SelectionEvent arg0) {
										TreeItem node = tree_op_ra.getSelection()[0];
										HashMap<String, HashSet<String>> perm = tempPermission.get(g_adminName);
										HashSet<String> set = perm.get(node.getText());
										perm.remove(node.getText());
										set.clear();
										if (set.isEmpty()) {
											perm.remove(node.getText());
										}
										showPermission(g_adminName);
										btnSubmitPerm_ra.setEnabled(true);
									}

									@Override
									public void widgetDefaultSelected(SelectionEvent arg0) {
									}
								});
								tree_op_ra.setMenu(menu_RA);
							} else if (node.getParentItem().getParentItem() != null
									&& treeItem_temp.equals(node.getParentItem().getParentItem())) { // baseDN
								MenuItem del_grant = new MenuItem(menu_RA, SWT.PUSH);
								del_grant.setText(l.getString("delete_grant")); // "删除授权"
								del_grant.addSelectionListener(new SelectionListener() {
									@Override
									public void widgetSelected(SelectionEvent arg0) {
										TreeItem node = tree_op_ra.getSelection()[0];
										HashMap<String, HashSet<String>> perm = tempPermission.get(g_adminName);
										HashSet<String> set = perm.get(node.getParentItem().getText());
										set.remove(node.getText());
										if (set.isEmpty()) {
											perm.remove(node.getParentItem().getText());
										}
										showPermission(g_adminName);
										btnSubmitPerm_ra.setEnabled(true);
									}

									@Override
									public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
									}
								});
								tree_op_ra.setMenu(menu_RA);
							}
						}
					}
				}
			}
		});

		btnSubmitPerm_ra = new Button(group_3_ra, SWT.NONE);
		btnSubmitPerm_ra.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					boolean flag = setPermission("RA");
					if (flag) {
						btnSubmitPerm_ra.setEnabled(false);
					}
				}
			}
		});
		btnSubmitPerm_ra.setEnabled(false);
		btnSubmitPerm_ra.setBounds(102, 490, 96, 27);
		btnSubmitPerm_ra.setText(l.getString("modify_submit"));
	}

	/**
	 * 模板视图
	 * @Author 江岩
	 * @Time 2019-06-04 20:03
	 * @version 1.0
	 */
	private void panel_Template(Composite compTemplate) {
		comp_Template.setLayout(new FormLayout());

		Composite composite_1 = new Composite(compTemplate, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(0, 129);
		fd_composite_1.right = new FormAttachment(0, 1090);
		fd_composite_1.top = new FormAttachment(0, 10);
		fd_composite_1.left = new FormAttachment(0, 10);
		composite_1.setLayoutData(fd_composite_1);

		Group group_5 = new Group(composite_1, SWT.NONE);
		group_5.setBounds(22, 10, 291, 99);
		group_5.setText(l.getString("queryTemplate"));

		Label lbl_templateName = new Label(group_5, SWT.NONE);
		lbl_templateName.setBounds(10, 24, 66, 22);
		lbl_templateName.setText(l.getString("templateName") + ":");

		text_templateName = new Text(group_5, SWT.BORDER);
		text_templateName.setBounds(82, 21, 146, 25);
		text_templateName.setTextLimit(20);

		// 查询模板
		Button btn_queryTemplate = new Button(group_5, SWT.NONE);
		btn_queryTemplate.setBounds(135, 61, 93, 28);
		btn_queryTemplate.setText(l.getString("queryTemplate"));
		btn_queryTemplate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					queryTemplate();
				}
			}
		});

		Group group_4 = new Group(composite_1, SWT.NONE);
		group_4.setText(l.getString("addTemplate"));
		group_4.setBounds(362, 10, 219, 99);

		Button btn_addTemp = new Button(group_4, SWT.NONE);
		btn_addTemp.setBounds(65, 62, 133, 27);
		btn_addTemp.setText(l.getString("addTemplate"));
		btn_addTemp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();
					Panel_BasicTemplate panel_BasicTemp = new Panel_BasicTemplate();
					panel_BasicTemp.setBlockOnOpen(true);
					int w = panel_BasicTemp.open();
					if (w == 0) {
						panel_Template = new Panel_Template(panel_BasicTemp);
						panel_Template.setBlockOnOpen(true);
						panel_Template.open();
						queryTemplate();
					}
				}
			}
		});

		tableTemplate = new Table(compTemplate, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		FormData fd_tableTemplate = new FormData();
		fd_tableTemplate.left = new FormAttachment(0, 10);
		fd_tableTemplate.right = new FormAttachment(100, -10);
		fd_tableTemplate.top = new FormAttachment(0, 135);
		fd_tableTemplate.bottom = new FormAttachment(0, 535);
		tableTemplate.setLayoutData(fd_tableTemplate);
		tableTemplate.setHeaderVisible(true);
		tableTemplate.setLinesVisible(true);

		final Menu menu_root = new Menu(tableTemplate);

		MenuItem item_view = new MenuItem(menu_root, SWT.PUSH);
		item_view.setText(l.getString("viewTemplate"));
		MenuItem item_clone = new MenuItem(menu_root, SWT.PUSH);
		item_clone.setText(l.getString("cloneTemplate"));
		final MenuItem item_modify = new MenuItem(menu_root, SWT.PUSH);
		item_modify.setText(l.getString("modifyTemplate"));
		final MenuItem item_revoke = new MenuItem(menu_root, SWT.PUSH);
		item_revoke.setText(l.getString("revokeTemplate"));
		final MenuItem item_delete = new MenuItem(menu_root, SWT.PUSH);
		item_delete.setText(l.getString("deleteTemplate"));
		tableTemplate.setMenu(menu_root);
		menu_root.addMenuListener(new MenuListener() {
			@Override
			public void menuHidden(MenuEvent e) {
			}

			@Override
			public void menuShown(MenuEvent e) { // 如果是在使用模板，不让删除，修改可以先导出 待下载证书信息，作废之后不能再恢复
				item_view.setEnabled(true);
				item_modify.setEnabled(true);
				item_clone.setEnabled(true);
				item_revoke.setEnabled(true);
				item_delete.setEnabled(true);
				String isUsed = tableTemplate.getSelection()[0].getText(2);
				String status = tableTemplate.getSelection()[0].getText(3);
				if (l.getString("yes").equalsIgnoreCase(isUsed)) {
					item_delete.setEnabled(false);
					item_revoke.setEnabled(false);
				}
				if (l.getString("revoke").equalsIgnoreCase(status)) {
					item_modify.setEnabled(false);
					item_revoke.setEnabled(false);
				}
			}
		});
		// 查看模板
		item_view.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				panel_Template = new Panel_Template(tableTemplate.getSelection()[0].getText(1), "view");
				panel_Template.open();
			}
		});
		// 修改模板
		item_modify.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String isUsed = tableTemplate.getSelection()[0].getText(2);
				if (l.getString("yes").equalsIgnoreCase(isUsed)) {
					String message = l.getString("Notice_modifyTemplate_1") + Utils.newLine
							+ l.getString("Notice_modifyTemplate_2") + Utils.newLine
							+ l.getString("Notice_modifyTemplate_3");
					MessageBox mb = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					mb.setMessage(message);
					int w = mb.open();
					if (w == 64) {
						panel_Template = new Panel_Template(tableTemplate.getSelection()[0].getText(1),
								"modify_isUsed");
						panel_Template.open();
					}
				} else {
					panel_Template = new Panel_Template(tableTemplate.getSelection()[0].getText(1), "modify");
					panel_Template.open();
				}
			}
		});
		// 克隆模板
		item_clone.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				panel_Template = new Panel_Template(tableTemplate.getSelection()[0].getText(1), "clone");
				panel_Template.open();
			}
		});
		// 作废模板
		item_revoke.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				mb.setMessage(l.getString("Notice_revokeTemplate"));
				int r = mb.open();
				if (SWT.YES == r) {
					TableItem item = (TableItem) tableTemplate.getSelection()[0];
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.TEMPLATENAME, item.getText(1));
					try {
						Env.client.sendRequest("REVOKETEMPLATE", p);
						MessageBox mb_1 = new MessageBox(getShell(), SWT.NONE);
						mb_1.setMessage(l.getString("Notice_succ_revokeTemplate"));
						mb_1.open();
						queryTemplate();
					} catch (ServerException se) {
						log.errlog("Revoke template fail", se);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_revokeTemplate") + "[" + se.getErrorNum() + "]:"
								+ se.getErrorMsg());
						mb.open();
					} catch (Exception ee) {
						log.errlog("Revoke template fail", ee);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_revokeTemplate"));
						mb.open();
					}
				}
			}
		});
		// 删除模板
		item_delete.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				MessageBox mb = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				mb.setMessage(l.getString("Notice_deleteTemplate") + "?");
				int r = mb.open();
				if (SWT.YES == r) {
					TableItem item = (TableItem) tableTemplate.getSelection()[0];
					Properties p = new Properties();
					p.setProperty(PropertiesKeysRes.TEMPLATENAME, item.getText(1));
					try {
						Env.client.sendRequest("DELETETEMPLATE", p);
						MessageBox mb_1 = new MessageBox(getShell(), SWT.NONE);
						mb_1.setMessage(l.getString("Notice_succ_deleteTemplate"));
						mb_1.open();
						queryTemplate();
					} catch (ServerException se) {
						log.errlog("Delete template fail", se);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_deleteTemplate") + "[" + se.getErrorNum() + "]:"
								+ se.getErrorMsg());
						mb.open();
					} catch (Exception ee) {
						log.errlog("Delete template fail", ee);
						mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_fail_deleteTemplate"));
						mb.open();
					}
				}
			}
		});
		TableColumn tblclmnNewColumn_ = new TableColumn(tableTemplate, SWT.NONE);
		tblclmnNewColumn_.setWidth(105);
		tblclmnNewColumn_.setText(l.getString("SN"));

		TableColumn tblclmnNewColumn = new TableColumn(tableTemplate, SWT.NONE);
		tblclmnNewColumn.setWidth(153);
		tblclmnNewColumn.setText(l.getString("templateName"));

		TableColumn tblclmnNewColumn_1 = new TableColumn(tableTemplate, SWT.NONE);
		tblclmnNewColumn_1.setWidth(118);
		tblclmnNewColumn_1.setText(l.getString("isUsed"));

		TableColumn tblclmnNewColumn_2 = new TableColumn(tableTemplate, SWT.NONE);
		tblclmnNewColumn_2.setWidth(140);
		tblclmnNewColumn_2.setText(l.getString("templateStatus"));

		TableColumn tblclmnNewColumn_3 = new TableColumn(tableTemplate, SWT.NONE);
		tblclmnNewColumn_3.setWidth(163);
		tblclmnNewColumn_3.setText(l.getString("templateType"));

	}

	/**
	 * 查询模板列表 & 查询指定模板
	 * @Author 江岩
	 * @Time 2019-07-02 11:00
	 * @version 1.1  将分页查询修改为普通查询 
	 */
	private void queryTemplate() {
		Properties p = new Properties();
		p.setProperty(PropertiesKeysRes.TEMPLATENAME, text_templateName.getText().trim());
		try {
			Response resp = Env.client.sendRequest("QUERYTEMPLATELIST", p);
			Properties[] rows = resp.getItemData();

			tableTemplate.removeAll();
			int n = 1;
			for (Properties row : rows) {
				TableItem ti = new TableItem(tableTemplate, SWT.NONE);
				String isUsed = l.getString("no"), status = l.getString("revoke"), type = l.getString("singleTemplate");
				if ("1".equalsIgnoreCase(row.getProperty(PropertiesKeysRes.TEMPLATE_ISUSED))) {
					isUsed = l.getString("yes");
				}
				if ("0".equalsIgnoreCase(row.getProperty(PropertiesKeysRes.TEMPLATE_STATUS))) {
					status = l.getString("normal");
				}
				if ("1".equalsIgnoreCase(row.getProperty(PropertiesKeysRes.TEMPLATE_ISDUAL))) {
					type = l.getString("doubleTemplate");
				}
				ti.setText(new String[] { String.valueOf(n++), row.getProperty(PropertiesKeysRes.TEMPLATENAME), isUsed,
						status, type });
			}

		} catch (ServerException se) {
			log.errlog("Query template fail");
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryTemplate") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Query template fail");
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryTemplate"));
			mb.open();
		}
	}

	/**
	 * 分页查询查询证书信息
	 * @Author 江岩
	 * @Time 2019-06-04 20:04
	 * @version 1.0
	 */
	private void listCert(String pageNum, boolean isTurnPage) {
		table.removeAll();
		total_Row = "0";
		String subjectName = txt_DN.getText().trim();
		String certSN = txt_SN.getText().trim();
		//String userName = txt_user.getText().trim();
		String templateName = combo_qryTmpl.getText();
		if (subjectName == "" || subjectName.length() < 0) {
			if (certSN == "" || certSN.length() < 0) {
				//if (userName == "" || userName.length() < 0) {
				if (templateName == "" || templateName.length() < 0) {
					MessageBox mBox = new MessageBox(getShell(), SWT.ICON_INFORMATION);
					mBox.setMessage(l.getString("Notice_null_queryConditions"));
					mBox.open();
					return;
				}
				//}
			}
		}

		Properties p_searchCert = new Properties();
		p_searchCert.setProperty(PropertiesKeysRes.PAGENUM, pageNum);
		p_searchCert.setProperty(PropertiesKeysRes.PAGESIZE, String.valueOf(pageSize));
		p_searchCert.setProperty(PropertiesKeysRes.CERTSN, txt_SN.getText().trim());
		p_searchCert.setProperty(PropertiesKeysRes.TEMPLATENAME, combo_qryTmpl.getText());
		p_searchCert.setProperty(PropertiesKeysRes.SUBJECTDN, txt_DN.getText().trim());

		try {
			//String uName = userName;
			Response resp = null;
			List<Properties[]> cert_List = new ArrayList<Properties[]>();
			/*
			if (uName.length() > 0) { // 用户名查询证书信息
				if (Env.IS_USER_AUTH_BY_CA) { // CA管理用户，通过用户名查询 uuid ，通过uuid查询证书
			
					if (!isTurnPage) {
						MessageBox mb_1 = new MessageBox(getShell(), SWT.NONE);
						mb_1.setMessage(l.getString("Notice_queryCertByUserName"));
						mb_1.open();
						Properties p_searchUser = new Properties();
						p_searchUser.setProperty(PropertiesKeysRes.USERNAME, uName);
						p_searchUser.setProperty(PropertiesKeysRes.PAGESIZE, "10000"); // pageSize
						Response respUuid = Env.client.sendRequest("SEARCHUSER", p_searchUser);
						Properties[] pps_resp = respUuid.getItemData();
						if (pps_resp.length == 0) {
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_null_queryCert"));
							mb.open();
							return;
						}
						row_Count_byUserName = 0;
			
						// 创建进度条对话框的处理过程对象
						IRunnableWithProgress runnable = new IRunnableWithProgress() {
							public void run(IProgressMonitor monitor) {
								monitor.beginTask("开始执行......", pps_resp.length / 2);
								for (int i = 0; i < pps_resp.length; i++) {
									p_searchCert.setProperty(PropertiesKeysRes.UUID,
											pps_resp[i].getProperty(PropertiesKeysRes.UUID));
									try {
										Response resp_1 = Env.client.sendRequest("SEARCHCERT", p_searchCert);
										Properties[] rows_certInfos = resp_1.getItemData();
										row_Count_byUserName += rows_certInfos.length;
										cert_List.add(rows_certInfos); // 保存
										i++;
									} catch (Exception e) {
										e.printStackTrace();
									}
									monitor.setTaskName("已完成" + (i + 1) + "次查询,一共需要" + pps_resp.length + "次。");// 提示信息
									monitor.worked(1);// 进度条前进一步
								}
								monitor.done();// 进度条前进到完成
							}
						};
			
						try {
							// 创建一个进度条对话框，并将runnable传入
							// 第一个参数推荐设为true，如果设为false则处理程序会运行在UI线程里，界面将有一点停滞感。
							// 第二个参数：true＝对话框的“取消”按钮有效
							new ProgressMonitorDialog(getShell()).run(true, true, runnable);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
			
						if (row_Count_byUserName <= 0) {
							MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
							mb.setMessage(l.getString("Notice_null_queryCert"));
							mb.open();
							return;
						}
						row_temp_byUserName = new Properties[row_Count_byUserName]; // 一共多少条
						int i = 0;
						for (Properties[] pros_temp : cert_List) {
							for (int j = 0; j < pros_temp.length; j++) {
								row_temp_byUserName[i] = pros_temp[j];
								i++;
							}
						}
						int total_Page_ = 0;
						if (row_Count_byUserName % pageSize == 0) {
							total_Page_ = row_Count_byUserName / pageSize;
						} else {
							total_Page_ = row_Count_byUserName / pageSize + 1;
						}
						total_Page = String.valueOf(total_Page_);
						total_Row = String.valueOf(row_Count_byUserName);
						curr_Page = pageNum;
					} else {
						curr_Page = pageNum;
					}
			
					// 获取 第几页的 pageSize 条 数据
					int index = (Integer.valueOf(pageNum) - 1) * pageSize; // 某一页的 开始记录值
					int end = (index + pageSize) > row_Count_byUserName ? row_Count_byUserName : (index + pageSize); // 某一页的结束记录值
					Properties[] row_temp01 = new Properties[end - index]; // 一共多少条
					int i1 = 0;
					for (; index < end; index++) {
						row_temp01[i1] = row_temp_byUserName[index];
						i1++;
					}
					rows = row_temp01;
					searchCertByName = true;
			
				} else { // RA管理用户 使用 用户名作为 uuid 查询 证书
					p_searchCert.setProperty(PropertiesKeysRes.UUID, uName);
					resp = Env.client.sendRequest("SEARCHCERT", p_searchCert);
					rows = resp.getItemData();
					total_Page = resp.getP().getProperty(PropertiesKeysRes.TOTALPAGES);
					total_Row = resp.getP().getProperty(PropertiesKeysRes.TOTALROW);
					curr_Page = pageNum;
					searchCertByName = false;
				}
				*/
			// 没有用户名的情况下，查询 证书信息
			resp = Env.client.sendRequest("SEARCHCERT", p_searchCert);
			rows = resp.getItemData();
			total_Page = resp.getP().getProperty(PropertiesKeysRes.TOTALPAGES);
			total_Row = resp.getP().getProperty(PropertiesKeysRes.TOTALROW);
			curr_Page = pageNum;

			if (rows.length <= 0) {
				MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
				mb.setMessage(l.getString("Notice_null_queryCert"));
				mb.open();
				return;
			}

			int n = 1;
			for (int i = 0; i < rows.length; i++) {
				// 转换日期格式
				String nb = rows[i].getProperty(PropertiesKeysRes.NOTBEFORE);
				String na = rows[i].getProperty(PropertiesKeysRes.NOTAFTER);
				if (nb.length() > 0) {
					nb = Utils.format(Utils.parse(nb));
				}
				if (na.length() > 0) {
					na = Utils.format(Utils.parse(na));
				}

				TableItem ti = new TableItem(table, SWT.NONE);
				ti.setText(new String[] { String.valueOf((Integer.valueOf(pageNum) - 1) * pageSize + n++),
						rows[i].getProperty(PropertiesKeysRes.USERNAME), rows[i].getProperty(PropertiesKeysRes.CERTSN),
						rows[i].getProperty(PropertiesKeysRes.SUBJECTDN),
						Utils.changeStatus(rows[i].getProperty(PropertiesKeysRes.CERTSTATUS)), nb, na,
						rows[i].getProperty(PropertiesKeysRes.TEMPLATENAME) });
			}

		} catch (ServerException se) {
			log.errlog("Query Cert fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_querycert") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Query template fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_querycert"));
			mb.open();
		}
	}

	/**
	 * 查询管理员 权限 
	 * @param name
	 * @Author 江岩
	 * @Time 2019-06-04 20:05
	 * @version 1.0
	 */
	private void getPermission(String name) {
		try {
			Properties p = new Properties();
			p.setProperty(PropertiesKeysRes.ADMIN_USERNAME, name);
			Response res = Env.client.sendRequest("GETPERMISSION", p);
			String priviStr = res.getP().getProperty(PropertiesKeysRes.ADMIN_BO_POWERS);

			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new ByteArrayInputStream(priviStr.getBytes("UTF-8")));
			Element root = doc.getRootElement();
			List operations = root.elements();

			if (tempPermission.containsKey((name))) {
				tempPermission.get(name).clear();
			} else {
				tempPermission.put(name, new HashMap<String, HashSet<String>>());
			}
			if (operations != null) {
				Iterator it = operations.iterator();
				while (it.hasNext()) {
					Element e = (Element) it.next();
					if (e.elements() != null && e.elements().size() > 0) {
						String template = e.elementTextTrim("certtype");
						String basedn = e.elementTextTrim("basedn");
						HashMap<String, HashSet<String>> perm = tempPermission.get(name);
						if (!perm.containsKey(template))
							perm.put(template, new HashSet<String>());
						perm.get(template).add(basedn);
					}
				}
			}
		} catch (ServerException se) {
			log.errlog("Query permission fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(
					l.getString("Notice_fail_queryPermission") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Query permission fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryPermission"));
			mb.open();
		}
	}

	/**
	 * 给管理员授权
	 * @param string
	 * @Author 江岩
	 * @Time 2019-06-04 20:05
	 * @version 1.0
	 */
	private boolean setPermission(String role) {
		StringBuffer sb = new StringBuffer(
				"<permission><operations name =\"APPLYUSER,MODIFYUSER,MAKECACERTREQ,ADDTEMPLATE,UPDATETEMPLATE,DELETETEMPLATE,REVOKETEMPLATE,QUERYTEMPLATEDETAIL,QUERYTEMPLATELIST\"/>");

		TreeItem ti = treeItem_temp;
		TreeItem[] templates = ti.getItems();
		for (TreeItem t : templates) {
			for (TreeItem b : t.getItems()) {
				sb.append(
						"<operations name=\"REQUESTCERT,GET2CODE,LOCKCERT,UNLOCKCERT,UPDATECERT,REVOKECERT,RECOVERENCCERTREQ\">")
						.append("<certtype>").append(t.getText()).append("</certtype>").append("<basedn>")
						.append(b.getText()).append("</basedn>").append("</operations>");
			}
		}
		sb.append("</permission>");

		Properties p = new Properties();
		p.setProperty(PropertiesKeysRes.ADMIN_USERNAME, g_adminName);
		p.setProperty(PropertiesKeysRes.ADMIN_BO_POWERS, sb.toString());
		try {
			Env.client.sendRequest("PRIVILEGEBO", p);
			MessageBox mb = new MessageBox(getShell(), SWT.NONE);
			mb.setMessage(l.getString("Notice_succ_grant"));
			mb.open();
			return true;
		} catch (ServerException se) {
			log.errlog("Grant RA fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_grant") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
			return false;
		} catch (Exception ee) {
			log.errlog("Grant RA fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_grant"));
			mb.open();
			return false;
		}
	}

	/**
	 * 查询RA权限
	 * @param (String,String)
	 * @Author 江岩
	 * @Time 2019-06-04 20:06
	 * @version 1.0
	 */
	private void showPermission(String adminName) {
		btnSubmitPerm_ra.setEnabled(false);
		TreeItem root_temp = treeItem_temp;
		root_temp.removeAll();
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

	/**
	 * 查询RA管理员列表
	 * @param (String)
	 * @Author 江岩
	 * @Time 2019-06-04 20:10
	 * @version 1.0
	 */
	private void getAdmins() {
		try {
			Properties prop = new Properties();
			prop.setProperty(PropertiesKeysRes.ADMIN_ROLE, "RA");
			Response res = Env.client.sendRequest("QUERYBOLIST", prop);
			Properties[] ps = res.getItemData();
			certInfoRA.clear();
			userInfoRA.clear();
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
				String status = p.getProperty(PropertiesKeysRes.STATUS, "");

				String[] certInfo = new String[] { sn, dn, valid, Utils.changeStatus(st) };
				String[] userInfo = new String[] { tel, email, memo, status, ips };
				certInfoRA.put(name, certInfo);
				userInfoRA.put(name, userInfo);
			}
		} catch (ServerException se) {
			log.errlog("Query RA list fail", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryRAList") + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Query RA list fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryRAList"));
			mb.open();
		}
	}

	/**
	 * 刷新页面内容
	 * @Author 江岩
	 * @Time 2019-06-04 20:12
	 * @version 1.0
	 */
	void refresh(String role) {
		if ("Cert".equals(role)) {
			table.removeAll();
			txt_DN.setText("");
			txt_SN.setText("");
			//txt_user.setText("");
			if (combo_qryTmpl.getItemCount() > 0) {
				combo_qryTmpl.select(0);
			}
			curr_Page = "0";
			total_Page = "0";
			pageUtil_Cert.btn_Change("0", "0");
		}
		if ("RA".equals(role)) {
			g_adminName = null;
			getAdmins();
			rootRA.removeAll();
			for (String name : certInfoRA.keySet()) {
				TreeItem ti = new TreeItem(rootRA, SWT.NULL);
				ti.setText(name);
			}
			tableRA.removeAll();
			txt_userName_RA.setText("");
			txt_tel_RA.setText("");
			txt_email_RA.setText("");
			txt_memo_RA.setText("");
			list_raIp.removeAll();
			treeItem_temp.removeAll();
		}
		if ("Template".equals(role)) {
			tableTemplate.removeAll();
			text_templateName.setText("");
		}
	}

	/**
	 * 菜单栏
	 * @Author 江岩
	 * @Time 2019-06-04 20:13
	 * @version 1.0
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager mm = new MenuManager();

		MenuManager menu = new MenuManager(l.getString("menu"));
		menu.add(new genCSRAction());
		menu.add(new queryPermissionsAction());
		menu.add(new Separator());
		menu.add(new ExitAction());

		mm.add(menu);
		return mm;
	}

	/**
	 * 申请证书事件
	 * @Author 江岩
	 * @Time 2019-06-04 20:13
	 */
	class genCSRAction extends Action {
		public genCSRAction() {
			setText(l.getString("genCSR_ca"));
		}

		public void run() {
			if (!Env.validSession()) {
				handleShellCloseEvent();
			} else {
				Env.lastOperationTime();
				Properties p = new Properties();
				try {
					Response resp = Env.client.sendRequest("GENCERTCSR", p);
					Properties resp_p = resp.getP();
					String csr = resp_p.getProperty(PropertiesKeysRes.CACERTCSR);

					FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
					fd.setFilterExtensions(new String[] { "*.csr" });
					fd.setFileName("ca_csr");
					String f = fd.open();
					if (f != null) {
						byte[] bs = csr.getBytes();
						FileOutputStream fos = new FileOutputStream(f);
						fos.write(bs);
						fos.close();
						MessageBox mb = new MessageBox(getShell(), SWT.OK);
						mb.setMessage(l.getString("Notice_succ_saveFile"));
						mb.open();
					}
				} catch (Exception e) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_fail_genCSR"));
					mb.open();
				}
			}
		}
	}

	/**
	 * 查看权限
	 * @Author 江岩
	 * @Time 2019-06-04 20:13
	 */
	class queryPermissionsAction extends Action {
		public queryPermissionsAction() {
			setText(l.getString("queryPermissions"));
		}

		public void run() {
			Panel_QueryBOPermissions queryPerm = new Panel_QueryBOPermissions(permStr);
			queryPerm.setBlockOnOpen(true);
			queryPerm.open();
		}
	}

	/**
	 * 退出窗口事件
	 * @Author 江岩
	 * @Time 2019-06-04 20:13
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
	 * 视图标题栏命名
	 * @param (Shell)
	 * @Author 江岩
	 * @Time 2019-06-04 20:15
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("BO") + " [" + subject + "]");
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	/**
	 * 重写窗口关闭事件
	 * @Description 释放占用的资源，并关闭视图
	 * @Author 江岩
	 * @Time 2019-06-04 19:58
	 * @version 1.0
	 */
	@Override
	protected void handleShellCloseEvent() {
		int closeCode = -1;
		if (!Env.validSession()) { // session超时
			MessageBox mb = new MessageBox(getShell());
			mb.setMessage(l.getString("Notice_invalidSession"));
			mb.open();
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
