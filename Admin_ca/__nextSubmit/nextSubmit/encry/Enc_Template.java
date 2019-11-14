package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.encry;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplate;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

/**
 * 加密模板视图 以及 逻辑
 * @Description  从模板管理页面获取 CertTemplate 对象，并对其赋值
 * @Author 江岩    
 * @Time 2019-06-06 14:20
 */
public class Enc_Template extends ApplicationWindow {

	private CertTemplate certTemplate_enc;

	private Panel_certPolicy_enc panel_certPolicy_enc;
	private Panel_keyUsage_enc panel_keyUsage_enc;
	private Panel_enKeyUsage_enc panel_enKeyUsage_enc;
	private Panel_CRLPublish_enc panel_CRLPublish_enc;
	private Panel_subAltName_enc panel_subAltName_enc;
	private Panel_incrementalCRL_enc panel_incrementalCRL_enc;

	private static ResourceBundle l = Env.getLanguage();

	/**
	 * @Desc 构造方法
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:02:39
	 */
	public Enc_Template(CertTemplate certTemplate_enc) {
		super(null);
		this.certTemplate_enc = certTemplate_enc;
	}

	/**
	 * @Desc 视图页面绘画
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:02:39
	 */
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 20;
		container.setLayout(formLayout);
		// 页面 tab 导航栏
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setLayoutData(new FormData());
		FormLayout fl_tabFolder = new FormLayout();
		tabFolder.setLayout(fl_tabFolder);

		createContents(tabFolder);

		return parent;
	}

	/**
	 * @Desc 加密模板视图 , 并判断是否需要加载模板信息
	 * @When 新增模板选择 双证 或者 修改模板查询存在 加密模板
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:11:26
	 */
	protected TabItem createContents(TabFolder tabFolder) {

		TabItem tabItem_enc = new TabItem(tabFolder, SWT.NONE);
		tabItem_enc.setText(l.getString("encTemplate"));
		final Composite composite_1_enc = new Composite(tabFolder, SWT.NONE);
		tabItem_enc.setControl(composite_1_enc);
		composite_1_enc.setLayout(new FormLayout());

		// tree 的 位置
		Group group_11_enc = new Group(composite_1_enc, SWT.NONE);
		group_11_enc.setLayoutData(new FormData());
		group_11_enc.setLayout(new GridLayout());

		// 左侧 功能栏
		final Tree tree_enc = new Tree(group_11_enc, SWT.NONE);
		GridData gridtree = new GridData();
		gridtree.heightHint = 470;
		gridtree.widthHint = 158;
		tree_enc.setLayoutData(gridtree);

		TreeItem tItem_2_enc = new TreeItem(tree_enc, SWT.NONE);
		tItem_2_enc.setText(l.getString("standardExtRegion"));
		TreeItem tItem_22_enc = new TreeItem(tItem_2_enc, SWT.NONE);
		tItem_22_enc.setText(l.getString("certPolicy"));
		TreeItem tItem_23_enc = new TreeItem(tItem_2_enc, SWT.NONE);
		tItem_23_enc.setText(l.getString("keyUsage"));
		TreeItem tItem_24_enc = new TreeItem(tItem_2_enc, SWT.NONE);
		tItem_24_enc.setText(l.getString("en_keyUsage"));
		TreeItem tItem_26_enc = new TreeItem(tItem_2_enc, SWT.NONE);
		tItem_26_enc.setText(l.getString("CRLPublishPoint"));
		TreeItem tItem_26_i_enc = new TreeItem(tItem_2_enc, SWT.NONE);
		tItem_26_i_enc.setText(l.getString("incrementalCRL"));
		TreeItem tItem_27_enc = new TreeItem(tItem_2_enc, SWT.NONE);
		tItem_27_enc.setText(l.getString("subjectAltName"));

		// 右侧的视图组件
		final Composite composite_certPolicy_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_certPolicy_enc = new FormData();
		fd_composite_certPolicy_enc.left = new FormAttachment(group_11_enc, 10);
		composite_certPolicy_enc.setLayoutData(fd_composite_certPolicy_enc);
		GridLayout gl_composite_22 = new GridLayout(1, false);
		composite_certPolicy_enc.setLayout(gl_composite_22);

		final Composite composite_keyUsage_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_keyUsage_enc = new FormData();
		fd_composite_keyUsage_enc.left = new FormAttachment(group_11_enc, 10);
		composite_keyUsage_enc.setLayoutData(fd_composite_keyUsage_enc);
		GridLayout gl_composite_23 = new GridLayout(1, false);
		composite_keyUsage_enc.setLayout(gl_composite_23);

		final Composite composite_enKeyUsage_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_enkeyUsage_enc = new FormData();
		fd_composite_enkeyUsage_enc.left = new FormAttachment(group_11_enc, 10);
		composite_enKeyUsage_enc.setLayoutData(fd_composite_enkeyUsage_enc);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		composite_enKeyUsage_enc.setLayout(gl_composite_24);

		final Composite composite_CRLPublish_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_CRLPublish_enc = new FormData();
		fd_composite_CRLPublish_enc.left = new FormAttachment(group_11_enc, 10);
		composite_CRLPublish_enc.setLayoutData(fd_composite_CRLPublish_enc);
		GridLayout gl_composite_26 = new GridLayout(1, false);
		composite_CRLPublish_enc.setLayout(gl_composite_26);

		final Composite comp_incrementalCRL_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_26_i = new FormData();
		fd_composite_26_i.left = new FormAttachment(group_11_enc, 10);
		comp_incrementalCRL_enc.setLayoutData(fd_composite_26_i);
		GridLayout gl_composite_26_i = new GridLayout(1, false);
		comp_incrementalCRL_enc.setLayout(gl_composite_26_i);

		final Composite composite_subAltName_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_subAltName_enc = new FormData();
		fd_composite_subAltName_enc.left = new FormAttachment(group_11_enc, 10);
		composite_subAltName_enc.setLayoutData(fd_composite_subAltName_enc);
		GridLayout gl_composite_27 = new GridLayout(1, false);
		composite_subAltName_enc.setLayout(gl_composite_27);

		// 所有 的 composite面板
		panel_certPolicy_enc.panel_certPolicy_enc(composite_certPolicy_enc);
		panel_keyUsage_enc.panel_keyUsage_enc(composite_keyUsage_enc);
		panel_enKeyUsage_enc.panel_enKeyUsage_enc(composite_enKeyUsage_enc);
		panel_CRLPublish_enc.panel_CRLPublish_enc(composite_CRLPublish_enc);
		panel_incrementalCRL_enc.panel_incrementalCRL_enc(comp_incrementalCRL_enc);
		panel_subAltName_enc.panel_subAltName_enc(composite_subAltName_enc);

		// 选中的 treeItem
		tree_enc.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				TreeItem[] items_enc = tree_enc.getSelection();
				String tItemSelected = items_enc[0].getText();
				// 所有 面板 隐藏
				Control[] controls = composite_1_enc.getChildren();
				for (int i = 1; i < controls.length; i++) {
					controls[i].setVisible(false);
				}
				if (l.getString("certPolicy").equals(tItemSelected)
						|| l.getString("standardExtRegion").equals(tItemSelected)) {
					composite_certPolicy_enc.setVisible(true);
				}
				if (l.getString("keyUsage").equals(tItemSelected)) {
					composite_keyUsage_enc.setVisible(true);
				}
				if (l.getString("en_keyUsage").equals(tItemSelected)) {
					composite_enKeyUsage_enc.setVisible(true);
				}
				if (l.getString("CRLPublishPoint").equals(tItemSelected)) {
					composite_CRLPublish_enc.setVisible(true);
				}
				if (l.getString("IncrementalCRL").equals(tItemSelected)) {
					comp_incrementalCRL_enc.setVisible(true);
				}
				if (l.getString("subjectAltName").equals(tItemSelected)) {
					composite_subAltName_enc.setVisible(true);
				}
			}
		});
		if (!certTemplate_enc.isAdd()) { // 查询模板信息时，提前设置了此值
			loadCertTemplateInfo(certTemplate_enc);
		}
		return tabItem_enc;
	}

	/**
	 * @Desc 封装加密模板信息
	 * @When 提交信息到 Server 时
	 * @Authod 江岩
	 * @Date 2019年3月18日 上午11:42:12
	 */
	public void packageTempInfo(CertTemplate certTemplate) {
		certTemplate.setKeyGeneratPlace("KMC");
		// 标准扩展
		ArrayList<Extension> standardExtensions = certTemplate.getStandardExtensions();
		// 封装 证书策略
		Extension ex_certPolicy_enc = panel_certPolicy_enc.packageTempInfo();
		if (ex_certPolicy_enc != null) {
			standardExtensions.add(ex_certPolicy_enc);
		}
		// 密钥用法
		Extension ex_keyUsage_enc = panel_keyUsage_enc.packageTempInfo();
		standardExtensions.add(ex_keyUsage_enc);
		// 增强密钥用法
		List<ExtensionEntry> exList_enKeyUsage = panel_enKeyUsage_enc.packageTempInfo();
		if (exList_enKeyUsage.size() > 0) {
			Extension ex_enKeyUsage = new Extension();
			ex_enKeyUsage.setOID("2.5.29.37");
			ex_enKeyUsage.setIscritical(panel_enKeyUsage_enc.btn_enKeyUsageIscritical_enc.getSelection());
			ex_enKeyUsage.setDatasource("CA");
			ex_enKeyUsage.setIsmust(false);
			ex_enKeyUsage.setExtensionEntrys(exList_enKeyUsage);
			standardExtensions.add(ex_enKeyUsage);
		}
		// CRL发布点设置
		Extension ex_CRLPub_enc = panel_CRLPublish_enc.packageTempInfo();
		if (ex_CRLPub_enc != null) {
			standardExtensions.add(ex_CRLPub_enc);
		}
		// 增量CRL 发布点设置
		Extension ex_CRLPub_i_enc = panel_incrementalCRL_enc.packageTempInfo();
		if (ex_CRLPub_i_enc != null) {
			standardExtensions.add(ex_CRLPub_i_enc);
		}
		// 主题备用名称
		List<ExtensionEntry> exList_subjectAltName_enc = panel_subAltName_enc.packageTempInfo();
		if (exList_subjectAltName_enc.size() > 0) {
			Extension ex_subjectAltName_enc = new Extension();
			ex_subjectAltName_enc.setOID("2.5.29.17");
			ex_subjectAltName_enc.setIscritical(panel_subAltName_enc.btn_keyUsageIscritical_enc.getSelection());
			ex_subjectAltName_enc.setDatasource("CA");
			ex_subjectAltName_enc.setIsmust(false);
			ex_subjectAltName_enc.setExtensionEntrys(exList_subjectAltName_enc);
			standardExtensions.add(ex_subjectAltName_enc);
		}
	}

	/**
	 * @Desc 加载 模板信息
	 * @When 修改模板时，将查询的模板信息 填充到 表单中
	 * @Authod 江岩
	 * @Date 2019年3月7日 上午10:26:12
	 */
	private void loadCertTemplateInfo(CertTemplate certTemplate) {
		ArrayList<Extension> standS = certTemplate.getStandardExtensions();
		for (Extension stand : standS) {
			String OID = stand.getOID();
			if("2.5.29.32".equals(OID)) // 证书策略
				panel_certPolicy_enc.loadCertTemplateInfo(stand);
			else if("2.5.29.15".equals(OID))	// 密钥用法
				panel_keyUsage_enc.loadCertTemplateInfo(stand);
			else if("2.5.29.37".equals(OID)) // 增强密钥用法
				panel_enKeyUsage_enc.loadCertTemplateInfo(stand);
			else if("2.5.29.31".equals(OID)) // CRL发布点设置
				panel_CRLPublish_enc.loadCertTemplateInfo(stand);
			else if("2.5.29.46".equals(OID)) // 增量 CRL发布点设置
				panel_incrementalCRL_enc.loadCertTemplateInfo(stand);
			else if("2.5.29.17".equals(OID)) // 主题备用名
				panel_subAltName_enc.loadCertTemplateInfo(stand);
		}
	}
}
