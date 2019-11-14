package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplate;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.utils.Env;

/**
 * @Desc 签名模板视图 以及 逻辑： 从模板管理页面获取 CertTemplate 对象，并对其赋值
 * @Author 江岩
 * @Date 2019年7月20日 上午11:18:36
 */
public class Sign_Template extends ApplicationWindow {

	// 签名模板属性
	private CertTemplate certTemplate;

	private Panel_basic_Limit panel_basic_Limit;
	private Panel_certPolicy panel_certPolicy;
	private Panel_keyUsage panel_keyUsage;
	private Panel_enKeyUsage panel_enKeyUsage;
	private Panel_CRLPublish panel_CRLPublish;
	private Panel_incrementalCRL panel_incrementalCRL;
	private Panel_subAltName panel_subAltName;
	private Panel_pubInfo panel_pubInfo;
	private Panel_perAndCompany panel_perAndCompany;
	private Panel_customExtend panel_customExtend;
	private Panel_userIDEx panel_userIdEx;
	private Panel_others panel_others;
	private Panel_certPolicy_1 panel_certPolicy_1;
	private Panel_publishPolicy panel_publishPolicy;
	private ResourceBundle l = Env.getLanguage();
	/**
	 * @Desc 构造对象
	 * @When 新增 和 修改模板
	 * @Authod 江岩
	 * @Date 2019年7月20日 下午4:02:39
	 */
	public Sign_Template(CertTemplate certTemplate) {
		super(null);
		this.certTemplate = certTemplate;
		panel_basic_Limit = new Panel_basic_Limit(certTemplate);
	}

	/**
	 * 视图页面绘画
	 * @Author 江岩      
	 * @Time 2019-03-20 14:35
	 * @version 1.0
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
	 * 签名模板视图
	 * @Desc 加载完成，判断是否是 修改操作，如果是，则加载模板对象中的信息到视图表单中
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:00:22
	 */
	protected TabItem createContents(TabFolder tabFolder) {

		TabItem tabItem_signal = new TabItem(tabFolder, SWT.NONE);
		tabItem_signal.setText(l.getString("signTempalte"));

		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem_signal.setControl(composite_1);
		FormLayout fl_composite_1 = new FormLayout();
		composite_1.setLayout(fl_composite_1);
		// tree 的 位置
		Group group_11 = new Group(composite_1, SWT.NONE);
		group_11.setLayoutData(new FormData());
		group_11.setLayout(new GridLayout());

		// Tree 功能栏
		final Tree tree = new Tree(group_11, SWT.NONE);
		GridData gridtree = new GridData();
		gridtree.heightHint = 470;
		tree.setLayoutData(gridtree);

		TreeItem tItem_2 = new TreeItem(tree, SWT.NONE);
		tItem_2.setText(l.getString("standardExtRegion"));
		TreeItem tItem_21 = new TreeItem(tItem_2, SWT.NONE);
		tItem_21.setText(l.getString("basic_limit"));
		tItem_21.setExpanded(true);
		TreeItem tItem_22 = new TreeItem(tItem_2, SWT.NONE);
		tItem_22.setText(l.getString("certPolicy"));
		TreeItem tItem_23 = new TreeItem(tItem_2, SWT.NONE);
		tItem_23.setText(l.getString("keyUsage"));
		TreeItem tItem_24 = new TreeItem(tItem_2, SWT.NONE);
		tItem_24.setText(l.getString("en_keyUsage"));
		TreeItem tItem_26 = new TreeItem(tItem_2, SWT.NONE);
		tItem_26.setText(l.getString("CRLPublishPoint"));
		TreeItem tItem_26_i = new TreeItem(tItem_2, SWT.NONE);
		tItem_26_i.setText(l.getString("incrementalCRL"));
		TreeItem tItem_27 = new TreeItem(tItem_2, SWT.NONE);
		tItem_27.setText(l.getString("subjectAltName"));
		TreeItem tItem_28 = new TreeItem(tItem_2, SWT.NONE);
		tItem_28.setText(l.getString("publishInfoAccess"));
		tItem_28.setExpanded(true);
		TreeItem tItem_29 = new TreeItem(tItem_2, SWT.NONE);
		tItem_29.setText(l.getString("userIDext"));
		TreeItem tItem_210 = new TreeItem(tItem_2, SWT.NONE);
		tItem_210.setText(l.getString("personalAndCompany"));
		TreeItem tItem_211 = new TreeItem(tItem_2, SWT.NONE);
		tItem_211.setText(l.getString("othersExt"));
		tItem_2.setExpanded(true);
		TreeItem tItem_3 = new TreeItem(tree, SWT.NONE);
		tItem_3.setText(l.getString("customExt"));
		tItem_3.setExpanded(true);
		TreeItem tItem_4 = new TreeItem(tree, SWT.NONE);
		tItem_4.setText(l.getString("certPolicy_1"));
		TreeItem tItem_5 = new TreeItem(tree, SWT.NONE);
		tItem_5.setText(l.getString("publishPolicy"));

		// 右侧的视图组件
		final Composite comp_basicLimit = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_21 = new FormData();
		fd_composite_21.left = new FormAttachment(group_11, 10);
		fd_composite_21.right = new FormAttachment(100, -10);
		comp_basicLimit.setLayoutData(fd_composite_21);
		GridLayout gl_composite_21 = new GridLayout(1, false);
		comp_basicLimit.setLayout(gl_composite_21);

		final Composite comp_certPolicy = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_22 = new FormData();
		fd_composite_22.left = new FormAttachment(group_11, 10);
		comp_certPolicy.setLayoutData(fd_composite_22);
		GridLayout gl_composite_22 = new GridLayout(1, false);
		comp_certPolicy.setLayout(gl_composite_22);

		final Composite comp_keyUsage = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_23 = new FormData();
		fd_composite_23.left = new FormAttachment(group_11, 10);
		comp_keyUsage.setLayoutData(fd_composite_23);
		GridLayout gl_composite_23 = new GridLayout(1, false);
		comp_keyUsage.setLayout(gl_composite_23);

		final Composite comp_enKeyUsage = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_24 = new FormData();
		fd_composite_24.left = new FormAttachment(group_11, 10);
		comp_enKeyUsage.setLayoutData(fd_composite_24);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		comp_enKeyUsage.setLayout(gl_composite_24);

		final Composite comp_CRLPublish = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_26 = new FormData();
		fd_composite_26.left = new FormAttachment(group_11, 10);
		comp_CRLPublish.setLayoutData(fd_composite_26);
		GridLayout gl_composite_26 = new GridLayout(1, false);
		comp_CRLPublish.setLayout(gl_composite_26);

		final Composite comp_incrementalCRL = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_26_i = new FormData();
		fd_composite_26_i.left = new FormAttachment(group_11, 10);
		comp_incrementalCRL.setLayoutData(fd_composite_26_i);
		GridLayout gl_composite_26_i = new GridLayout(1, false);
		comp_incrementalCRL.setLayout(gl_composite_26_i);

		final Composite comp_subAltName = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_27 = new FormData();
		fd_composite_27.left = new FormAttachment(group_11, 10);
		comp_subAltName.setLayoutData(fd_composite_27);
		GridLayout gl_composite_27 = new GridLayout(1, false);
		comp_subAltName.setLayout(gl_composite_27);

		final Composite comp_pubInfo = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_28 = new FormData();
		fd_composite_28.left = new FormAttachment(group_11, 10);
		comp_pubInfo.setLayoutData(fd_composite_28);
		GridLayout gl_composite_28 = new GridLayout(1, false);
		comp_pubInfo.setLayout(gl_composite_28);

		final Composite comp_userIdEx = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_29 = new FormData();
		fd_composite_29.left = new FormAttachment(group_11, 10);
		comp_userIdEx.setLayoutData(fd_composite_29);
		GridLayout gl_composite_29 = new GridLayout(1, false);
		comp_userIdEx.setLayout(gl_composite_29);

		final Composite comp_perAndCompany = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_210 = new FormData();
		fd_composite_210.left = new FormAttachment(group_11, 10);
		comp_perAndCompany.setLayoutData(fd_composite_210);
		GridLayout gl_composite_210 = new GridLayout(1, false);
		comp_perAndCompany.setLayout(gl_composite_210);

		final Composite comp_others = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_211 = new FormData();
		fd_composite_211.left = new FormAttachment(group_11, 10);
		comp_others.setLayoutData(fd_composite_211);
		GridLayout gl_composite_211 = new GridLayout(1, false);
		comp_others.setLayout(gl_composite_211);

		final Composite comp_customExtend = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_31 = new FormData();
		fd_composite_31.left = new FormAttachment(group_11, 10);
		comp_customExtend.setLayoutData(fd_composite_31);
		GridLayout gl_composite_31 = new GridLayout(1, false);
		comp_customExtend.setLayout(gl_composite_31);

		final Composite comp_certPolicy_1 = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_41 = new FormData();
		fd_composite_41.right = new FormAttachment(100, -10);
		fd_composite_41.left = new FormAttachment(group_11, 10);
		comp_certPolicy_1.setLayoutData(fd_composite_41);
		GridLayout gl_composite_41 = new GridLayout(1, false);
		comp_certPolicy_1.setLayout(gl_composite_41);

		final Composite comp_publishPolicy = new Composite(composite_1, SWT.NONE);
		FormData fd_composite_51 = new FormData();
		fd_composite_51.left = new FormAttachment(group_11, 10);
		comp_publishPolicy.setLayoutData(fd_composite_51);
		GridLayout gl_composite_51 = new GridLayout(1, false);
		comp_publishPolicy.setLayout(gl_composite_51);

		// 所有 的 composite面板
		panel_basic_Limit.panel_basic_Limit(comp_basicLimit);
		panel_certPolicy.panel_certPolicy(comp_certPolicy);
		panel_keyUsage.panel_keyUsage(comp_keyUsage);
		panel_enKeyUsage.panel_enKeyUsage(comp_enKeyUsage);
		panel_CRLPublish.panel_CRLPublic(comp_CRLPublish);
		panel_incrementalCRL.panel_incrementalCRL(comp_incrementalCRL);
		panel_subAltName.panel_subAltName(comp_subAltName);
		panel_pubInfo.panel_pubInfo(comp_pubInfo);
		panel_userIdEx.panel_userIdEx(comp_userIdEx);
		panel_perAndCompany.panel_perAndCompany(comp_perAndCompany);
		panel_others.panel_others(comp_others);
		panel_customExtend.panel_diyExtend(comp_customExtend);
		panel_certPolicy_1.panel_certPolicy_1(comp_certPolicy_1);
		panel_publishPolicy.panel_publishPolicy(comp_publishPolicy);
		
		// 选中的 treeItem
		tree.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				TreeItem[] items = tree.getSelection();
				String tItemSelected = items[0].getText();
				// 所有 面板 隐藏
				Control[] controls = composite_1.getChildren();
				for (int i = 1; i < controls.length; i++) {
					controls[i].setVisible(false);
				}
				if (l.getString("basic_limit").equals(tItemSelected)
						|| l.getString("standardExtRegion").equals(tItemSelected)) {
					comp_basicLimit.setVisible(true);
				}
				if (l.getString("certPolicy").equals(tItemSelected)) {
					comp_certPolicy.setVisible(true);
				}
				if (l.getString("keyUsage").equals(tItemSelected)) {
					comp_keyUsage.setVisible(true);
				}
				if (l.getString("en_keyUsage").equals(tItemSelected)) {
					comp_enKeyUsage.setVisible(true);
				}
				if (l.getString("CRLPublishPoint").equals(tItemSelected)) {
					comp_CRLPublish.setVisible(true);
				}
				if (l.getString("IncrementalCRL").equals(tItemSelected)) {
					comp_incrementalCRL.setVisible(true);
				}
				if (l.getString("subjectAltName").equals(tItemSelected)) {
					comp_subAltName.setVisible(true);
				}
				if (l.getString("publishInfoAccess").equals(tItemSelected)) {
					comp_pubInfo.setVisible(true);
				}
				if (l.getString("userIDext").equals(tItemSelected)) {
					comp_userIdEx.setVisible(true);
				}
				if (l.getString("personalAndCompany").equals(tItemSelected)) {
					comp_perAndCompany.setVisible(true);
				}
				if (l.getString("othersExt").equals(tItemSelected)) {
					comp_others.setVisible(true);
				}
				if (l.getString("customExt").equals(tItemSelected)) {
					comp_customExtend.setVisible(true);
				}
				if (l.getString("certPolicy_1").equals(tItemSelected)) {
					comp_certPolicy_1.setVisible(true);
				}
				if (l.getString("publishPolicy").equals(tItemSelected)) {
					comp_publishPolicy.setVisible(true);
				}
			}
		});
		if (!certTemplate.isAdd()) { // 如果是修改模板，加载模板对象信息
			loadCertTemplateInfo(certTemplate);
		} else { // 新增模板 在此处 针对模板类型 赋值
			diyTemplateType(certTemplate.getSpecialType());
		}
		return tabItem_signal;
	}

	/**
	 * @Desc  根据 选择的模板类型，对一些特殊值做 勾选操作
	 * @When 新增模板 操作中
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:02:39
	 *    密钥用途   CRL  发布策略  
	 */
	private void diyTemplateType(String specialType) {
		if("ocsp".equals(specialType)){
			panel_keyUsage.btn_digitalSign.setSelection(true);
			panel_keyUsage.btn_nonRepudiation.setSelection(true);
			panel_enKeyUsage.btn_OCSPSign.setSelection(true);
		}else if("scep".equals(specialType)){
			panel_publishPolicy.btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
		}else if("ee_sign".equals(specialType)){
			panel_keyUsage.btn_digitalSign.setSelection(true);
			panel_keyUsage.btn_nonRepudiation.setSelection(true);
			panel_publishPolicy.btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
			TableItem item1 = new TableItem(panel_customExtend.table_customExtend, SWT.NONE);
			item1.setText(new String[] { "DIR", l.getString("true"), "cn=crl*,ou=crl" });
		}else if("ms_smartCard".equals(specialType)){
			panel_keyUsage.btn_digitalSign.setSelection(true);
			panel_keyUsage.btn_keyEn.setSelection(true);
			panel_enKeyUsage.btn_clientAuth.setSelection(true);
			panel_publishPolicy.btn_pubCert.setSelection(Boolean.valueOf(certTemplate.getIspublishCert()));
			panel_publishPolicy.btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
		}else if("ms_domainController".equals(specialType)){
			panel_keyUsage.btn_digitalSign.setSelection(true);
			panel_keyUsage.btn_keyEn.setSelection(true);
			panel_enKeyUsage.btn_serverAuth.setSelection(true);
			panel_enKeyUsage.btn_clientAuth.setSelection(true);
			panel_publishPolicy.btn_pubCert.setSelection(Boolean.valueOf(certTemplate.getIspublishCert()));
			panel_publishPolicy.btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
		}else if("code_sign".equals(specialType)){
			panel_keyUsage.btn_digitalSign.setSelection(true);
			panel_keyUsage.btn_nonRepudiation.setSelection(true);
			panel_enKeyUsage.btn_codeSign.setSelection(true);
			TableItem item2 = new TableItem(panel_customExtend.table_customExtend, SWT.NONE);
			item2.setText(new String[] { "DIR", l.getString("true"), "cn=crl*,ou=crl" });
			panel_publishPolicy.btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
		}
	}

	/**
	 * @Desc  封装签名模板信息，到模板对象中
	 * @When 提交信息到 Server 过程中
	 * @Authod 江岩
	 * @Date 2019年3月18日 上午11:42:12
	 */
	public void packageTempInfo(CertTemplate certTemplate) {
		certTemplate.setKeyGeneratPlace("CLIENT"); // 固定值
		// 标准扩展
		ArrayList<Extension> standardExtensions = certTemplate.getStandardExtensions();
		// 封装 基本限制
		Extension ex_basic = panel_basic_Limit.packageTempInfo();
		standardExtensions.add(ex_basic);
		// 封装 证书策略
		Extension ex_certPolicy = panel_certPolicy.packageTempInfo();
		standardExtensions.add(ex_certPolicy);
		// 密钥用法
		Extension ex_keyUsage = panel_keyUsage.packageTempInfo();
		standardExtensions.add(ex_keyUsage);
		// 增强密钥用法
		Extension ex_enKeyUsage = panel_enKeyUsage.packageTempInfo();
		if ("2.5.29.37".equals(ex_enKeyUsage.getOID())) {
			standardExtensions.add(ex_enKeyUsage);
		}
		// CRL发布点设置
		Extension ex_CRLPub = panel_CRLPublish.packageTempInfo();
		if ("2.5.29.31".equals(ex_CRLPub.getOID())) {
			standardExtensions.add(ex_CRLPub);
		}
		// 增量CRL 发布点设置
		Extension ex_CRLPub_i = panel_incrementalCRL.packageTempInfo();
		if ("2.5.29.46".equals(ex_CRLPub_i.getOID())) {
			standardExtensions.add(ex_CRLPub_i);
		}
		// 主题备用名称
		Extension ex_subjectAltName = panel_subAltName.packageTempInfo();
		if ("2.5.29.17".equals(ex_subjectAltName.getOID())) {
			standardExtensions.add(ex_subjectAltName);
		}
		// 颁发机构信息访问
		Extension ex_pubInfoAccess = panel_pubInfo.packageTempInfo();
		if ("1.3.6.1.5.5.7.1.1".equals(ex_pubInfoAccess.getOID())) {
			standardExtensions.add(ex_pubInfoAccess);
		}
		// 用户ID扩展
		Extension ex_userIdEx = panel_userIdEx.packageTempInfo();
		if (ex_userIdEx != null) {
			standardExtensions.add(ex_userIdEx);
		}
		// 颁发机构密钥标识符 
		Extension ex_pubKeyIdentity = panel_others.packageTempInfo_pub();
		if (ex_pubKeyIdentity != null) {
			standardExtensions.add(ex_pubKeyIdentity);
		}
		// 主题密钥标识符 
		Extension ex_subKeyIdentityC = panel_others.packageTempInfo_sub();
		if (ex_subKeyIdentityC != null) {
			standardExtensions.add(ex_subKeyIdentityC);
		}
		certTemplate.setStandardExtensions(standardExtensions);

		// 个人和企业信息
		ArrayList<Extension> otherExtensions = panel_perAndCompany.packageTempInfo();
		certTemplate.setOtherExtensions(otherExtensions);
		// 自定义扩展
		ArrayList<Extension> customExtensions = panel_customExtend.packageTempInfo();
		certTemplate.setCustomerExtensions(customExtensions);
		// 证书相关策略 和 发布策略
		panel_certPolicy_1.packageTempInfo(certTemplate);
		panel_publishPolicy.packageTempInfo(certTemplate);	
	}

	/**
	 * @Desc 加载 模板信息，将模板信息填充到表单对应项中
	 * @When 
	 * @Authod 江岩
	 * @Date 2019年3月7日 上午10:26:12
	 */
	private void loadCertTemplateInfo(CertTemplate certTemplate) {

		ArrayList<Extension> standS = certTemplate.getStandardExtensions();
		for (Extension stand : standS) {
			String OID = stand.getOID();
			if("2.5.29.19".equals(OID)) // 基本限制
				panel_basic_Limit.loadCertTemplateInfo(stand);
			else if("2.5.29.32".equals(OID)) // 证书策略
				panel_certPolicy.loadCertTemplateInfo(stand);
			else if("2.5.29.15".equals(OID)) // 密钥用法
				panel_keyUsage.loadCertTemplateInfo(stand);
			else if("2.5.29.37".equals(OID)) // 增强密钥用法
				panel_enKeyUsage.loadCertTemplateInfo(stand);
			else if("2.5.29.31".equals(OID)) // CRL发布点设置
				panel_CRLPublish.loadCertTemplateInfo(stand);
			else if("2.5.29.46".equals(OID)) // 增量 CRL发布点设置
				panel_incrementalCRL.loadCertTemplateInfo(stand);
			else if("2.5.29.17".equals(OID)) // 主题备用名
				panel_subAltName.loadCertTemplateInfo(stand);
			else if("1.3.6.1.5.5.7.1.1".equals(OID)) // 颁发者信息访问
				panel_pubInfo.loadCertTemplateInfo(stand);
			else if("1.3.6.1.4.1.27971.35.2".equals(OID)) // 用户ID扩展
				panel_userIdEx.loadCertTemplateInfo(stand);
			else if("2.5.29.14".equals(OID)) 		     // 主题密钥扩展
				panel_others.loadCertTemplateInfo_sub(stand);
			else if("2.5.29.35".equals(OID))			 // 颁发者密钥扩展
				panel_others.loadCertTemplateInfo_pub(stand);
		}
		// 个人与企业信息
		ArrayList<Extension> otherS = certTemplate.getOtherExtensions();
		panel_perAndCompany.loadCertTemplateInfo(otherS);
		// 自定义扩展
		ArrayList<Extension> customerS = certTemplate.getCustomerExtensions();
		panel_customExtend.loadCertTemplateInfo(customerS);
		// 证书相关策略和发布策略
		panel_certPolicy_1.loadCertTemplateInfo(certTemplate);
		panel_publishPolicy.loadCertTemplateInfo(certTemplate);
	}


	/**
	 *  视图页面命名
	 * @Author 江岩      
	 * @Time 2019-06-06 14:37
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("SignTemplate_Info"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}
}
