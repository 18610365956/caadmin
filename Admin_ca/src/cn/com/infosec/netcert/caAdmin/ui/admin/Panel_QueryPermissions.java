package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.framework.log.FileLogger;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 查看权限
 * @Author 江岩
 * @Time 2019-06-04 20:26
 */
public class Panel_QueryPermissions extends ApplicationWindow {
	public String selectedTempName, baseDN, permStr;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private static ResourceBundle l = Env.getLanguage();
	private TreeItem treeItem_tempM, treeItem_RA, treeItem_temp;
	private boolean isRAPermissions = false, isAddtemplateM = false;
	/**
	 * 构造方法
	 */
	public Panel_QueryPermissions(String permStr) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.permStr = permStr;
	}

	/**
	 * 视图页面绘画
	 * @Author 江岩
	 * @Time 2019-06-04 20:26
	 * @version 1.0
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		Tree tree_op_ra = new Tree(container, SWT.BORDER);
		tree_op_ra.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tree_op_ra.setBounds(0, 0, 299, 507);

		TreeItem trtm_opRoot = new TreeItem(tree_op_ra, SWT.NONE);
		trtm_opRoot.setText(l.getString("operation_grant"));
		trtm_opRoot.setExpanded(true);
		
		// 模板管理
		treeItem_tempM = new TreeItem(trtm_opRoot, SWT.NONE);
		treeItem_tempM.setText(l.getString("temp_manage"));
		treeItem_tempM.setExpanded(true);
		// RA管理
		treeItem_RA = new TreeItem(trtm_opRoot, SWT.NONE);
		treeItem_RA.setText(l.getString("ra_manage"));
		treeItem_RA.setExpanded(true);
		// "模板操作权限"
		treeItem_temp = new TreeItem(trtm_opRoot, SWT.NONE);
		treeItem_temp.setText(l.getString("cert_permissions"));
		treeItem_temp.setExpanded(true);
		treeItem_temp.setExpanded(true);

		getPermission(permStr);
		return container;
	}

	/**
	 * 查询管理员 权限 
	 * @param name
	 * @Author 江岩
	 * @Time 2019-06-04 20:05
	 * @version 1.0
	 */
	private void getPermission(String permStr) {
		try {
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new ByteArrayInputStream(permStr.getBytes("UTF-8")));
			Element root = doc.getRootElement();
			List operations = root.elements();
			if (operations != null) {
				Iterator it = operations.iterator();
				HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
				while (it.hasNext()) {
					Element e = (Element) it.next();
					if (e.elements() != null && e.elements().size() > 0) {
						String template = e.elementTextTrim("certtype");
						String basedn = e.elementTextTrim("basedn");
						HashSet<String> set = new HashSet<String>();
						if (!map.containsKey(template)) {
							map.put(template, new HashSet<String>());
						}
						set = map.get(template);
						set.add(basedn);
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
				Set<String> templates = map.keySet();
				for (String temp : templates) {
					if (map.get(temp) == null || map.get(temp).size() == 0) {
						return;
					} else {
						TreeItem ti = new TreeItem(treeItem_temp, SWT.NULL);
						ti.setText(temp);
						for (String basedn : map.get(temp)) {
							TreeItem tiBasedn = new TreeItem(ti, SWT.NULL);
							tiBasedn.setText(basedn);
						}
					}
				}
				if (isAddtemplateM) {
					TreeItem ti_Add = new TreeItem(treeItem_tempM, SWT.NULL);
					ti_Add.setText(l.getString("op_addTemplate"));
					TreeItem ti_del = new TreeItem(treeItem_tempM, SWT.NULL);
					ti_del.setText(l.getString("op_delTemplate"));
					TreeItem ti_update = new TreeItem(treeItem_tempM, SWT.NULL);
					ti_update.setText(l.getString("op_updateTemplate"));
					TreeItem ti_query = new TreeItem(treeItem_tempM, SWT.NULL);
					ti_query.setText(l.getString("QueryTemplate"));
					TreeItem ti_revoke = new TreeItem(treeItem_tempM, SWT.NULL);
					ti_revoke.setText(l.getString("RevokeTemplate"));
				}
				if (isRAPermissions) {
					TreeItem ti_newRA = new TreeItem(treeItem_RA, SWT.NULL);
					ti_newRA.setText(l.getString("op_newRA"));
					TreeItem ti_updateRA = new TreeItem(treeItem_RA, SWT.NULL);
					ti_updateRA.setText(l.getString("op_modifyRA"));
					TreeItem ti_revokeRA = new TreeItem(treeItem_RA, SWT.NULL);
					ti_revokeRA.setText(l.getString("op_revokeRACert"));
					TreeItem ti_reqRAcert = new TreeItem(treeItem_RA, SWT.NULL);
					ti_reqRAcert.setText(l.getString("op_requestRACert"));
					TreeItem ti_grantRA = new TreeItem(treeItem_RA, SWT.NULL);
					ti_grantRA.setText(l.getString("op_grantRA"));
				}
			}
		} catch (Exception e1) {
			log.errlog("Query permissions fail.");
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_queryPerm"));
			mb.open();
		}
	}

	/**
	 * 视图标题栏命名
	 * 
	 * @param (Shell)
	 * @Author 江岩
	 * @Time 2019-06-04 20:27
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("queryPermissions"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}
}
