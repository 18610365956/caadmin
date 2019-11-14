package cn.com.infosec.netcert.caAdmin.ui.template;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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

import cn.com.infosec.netcert.caAdmin.ui.login.Panel_MessageDialog;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplate;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.ExtensionEntry;
import cn.com.infosec.netcert.caAdmin.utils.Env;

/**
 * @Desc 签名模板视图 以及 逻辑: 从模板管理页面获取 CertTemplate 对象，并对其赋值
 * @Author 江岩
 * @Date 2019年3月18日 上午11:18:36 
 * @add 2019/07/24 新增 增量crl页面，OID : 2.4.29.46 ，没有是否关键选项，提交时 设置为 false,其他与crl发布点一致
 */
public class Sign_Template extends ApplicationWindow {

	// 签名模板属性
	private Tree tree;
	private Table table_diyExtend, table_261, table_261_i;
	private Text text_maxPathLen, text_specifyPolicyOID, text_CPS, text_userNotice, text_oID, text_locationURI,
			text_OCSPURI, text_customOID, text_basicPub, text_basicPub_i;
	private Composite comp_basicLimit, comp_certPolicy, comp_keyUsage, comp_enKeyUsage, comp_CRLPublish,
			comp_incrementalCRL, comp_subAltName, comp_pubInfo, comp_userIdEx, comp_perAndCompany, comp_others,
			comp_customExtend, comp_certPolicy_1, comp_publishPolicy;

	private Combo combo_CRLType, combo_CRLType_i;
	private Button btn_useBasicLimitIscritical, btn_useCASign, btn_noLenLimit, btn_maxPathLen, btn_certPolicyIscritical,
			btn_noPolicy, btn_anyPolicy, btn_specifyPolicyOID, btn_CPS, btn_userNotice, btn_keyUsageIscritical,
			btn_digitalSign, btn_nonRepudiation, btn_keyEn, btn_dataEn, btn_keyAgree, btn_keyCertSign, btn_CRLSign,
			btn_enci, btn_deci, btn_enKeyUsageIscritical, btn_serverAuth, btn_clientAuth, btn_codeSign, btn_emailPro,
			btn_timeStamp, btn_OCSPSign, btn_CRLPub, btn_CRLPubIscritical, btn_subjectAltNameM, btn_dNSName,
			btn_ipAddress, btn_rfc822Name, btn_otherName, btn_pubInfoAccessM, btn_locationURI, btn_OCSPURI,
			btn_userIdExM, btn_customOID, btn_identityCode, btn_identityCodeMust, btn_socialInsuranceCode,
			btn_socialInsuranceCodeM, btn_companyRegistCode, btn_companyRegistCodeM, btn_companyOrganCode,
			btn_companyOrganCodeM, btn_companyTaxCode, btn_companyTaxCodeM, btn_subKeyIdentity, btn_subKeyIdentityM,
			btn_pubKeyIdentity, btn_pubKeyIdentityM, btn_allowAppend, btn_allowDNRepeat, btn_appleCertChgKey,
			btn_useCertDN, btn_pubCert, btn_pubCRLList, btn_add, btn_delete, btn_add_261, btn_add_261_i, btn_delete_262,
			btn_delete_262_i, btn_addDN, btn_addDN_i;

	private org.eclipse.swt.widgets.List list1;
	private Set<String> oIDs = new HashSet<String>();
	private CertTemplate certTemplate;
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * @Desc 构造对象
	 * @When 新增 和 修改模板
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:02:39
	 */
	public Sign_Template(CertTemplate certTemplate) {
		super(null);
		this.certTemplate = certTemplate;
	}

	/**
	 * 视图页面绘画
	 * @Author 江岩      
	 * @Time 2019-03-20 14:35
	 * @version 1.0
	 */
	protected Control createContents(Composite parent) {
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
	 * @Desc 加载视图完成，判断是否是 修改操作，如果是，则加载模板对象中的信息到视图表单中
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
		tree = new Tree(group_11, SWT.NONE);
		GridData gridtree = new GridData();
		gridtree.heightHint = 470;
		tree.setLayoutData(gridtree);

		TreeItem tItem_2 = new TreeItem(tree, SWT.NONE);
		tItem_2.setText(l.getString("standardExtRegion"));
		TreeItem tItem_21 = new TreeItem(tItem_2, SWT.NONE);
		tItem_21.setText(l.getString("basic_limit"));
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
		TreeItem tItem_29 = new TreeItem(tItem_2, SWT.NONE);
		tItem_29.setText(l.getString("userIDext"));
		TreeItem tItem_210 = new TreeItem(tItem_2, SWT.NONE);
		tItem_210.setText(l.getString("personalAndCompany"));
		TreeItem tItem_211 = new TreeItem(tItem_2, SWT.NONE);
		tItem_211.setText(l.getString("otherExt"));
		TreeItem tItem_3 = new TreeItem(tree, SWT.NONE);
		tItem_3.setText(l.getString("customExt"));
		TreeItem tItem_4 = new TreeItem(tree, SWT.NONE);
		tItem_4.setText(l.getString("certPolicy_1"));
		TreeItem tItem_5 = new TreeItem(tree, SWT.NONE);
		tItem_5.setText(l.getString("publishPolicy"));
		tItem_2.setExpanded(true);
		
		// 右侧的视图组件
		comp_basicLimit = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_basicLimit = new FormData();
		fd_comp_basicLimit.left = new FormAttachment(group_11, 10);
		fd_comp_basicLimit.right = new FormAttachment(100, -10);
		comp_basicLimit.setLayoutData(fd_comp_basicLimit);
		GridLayout gl_composite_21 = new GridLayout(1, false);
		comp_basicLimit.setLayout(gl_composite_21);

		comp_certPolicy = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_certPolicy = new FormData();
		fd_comp_certPolicy.left = new FormAttachment(group_11, 10);
		comp_certPolicy.setLayoutData(fd_comp_certPolicy);
		GridLayout gl_composite_22 = new GridLayout(1, false);
		comp_certPolicy.setLayout(gl_composite_22);

		comp_keyUsage = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_keyUsage = new FormData();
		fd_comp_keyUsage.left = new FormAttachment(group_11, 10);
		comp_keyUsage.setLayoutData(fd_comp_keyUsage);
		GridLayout gl_composite_23 = new GridLayout(1, false);
		comp_keyUsage.setLayout(gl_composite_23);

		comp_enKeyUsage = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_enKeyUsage = new FormData();
		fd_comp_enKeyUsage.left = new FormAttachment(group_11, 10);
		comp_enKeyUsage.setLayoutData(fd_comp_enKeyUsage);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		comp_enKeyUsage.setLayout(gl_composite_24);

		comp_CRLPublish = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_CRLPublish = new FormData();
		fd_comp_CRLPublish.left = new FormAttachment(group_11, 10);
		comp_CRLPublish.setLayoutData(fd_comp_CRLPublish);
		GridLayout gl_composite_26 = new GridLayout(1, false);
		comp_CRLPublish.setLayout(gl_composite_26);

		comp_incrementalCRL = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_IncrementalCRL = new FormData();
		fd_comp_IncrementalCRL.left = new FormAttachment(group_11, 10);
		comp_incrementalCRL.setLayoutData(fd_comp_IncrementalCRL);
		GridLayout gl_composite_26_i = new GridLayout(1, false);
		comp_incrementalCRL.setLayout(gl_composite_26_i);

		comp_subAltName = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_subAltName = new FormData();
		fd_comp_subAltName.left = new FormAttachment(group_11, 10);
		comp_subAltName.setLayoutData(fd_comp_subAltName);
		GridLayout gl_composite_27 = new GridLayout(1, false);
		comp_subAltName.setLayout(gl_composite_27);

		comp_pubInfo = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_pubInfo = new FormData();
		fd_comp_pubInfo.left = new FormAttachment(group_11, 10);
		comp_pubInfo.setLayoutData(fd_comp_pubInfo);
		GridLayout gl_composite_28 = new GridLayout(1, false);
		comp_pubInfo.setLayout(gl_composite_28);

		comp_userIdEx = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_userIdEx = new FormData();
		fd_comp_userIdEx.left = new FormAttachment(group_11, 10);
		comp_userIdEx.setLayoutData(fd_comp_userIdEx);
		GridLayout gl_composite_29 = new GridLayout(1, false);
		comp_userIdEx.setLayout(gl_composite_29);

		comp_perAndCompany = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_perAndCompany = new FormData();
		fd_comp_perAndCompany.left = new FormAttachment(group_11, 10);
		comp_perAndCompany.setLayoutData(fd_comp_perAndCompany);
		GridLayout gl_composite_210 = new GridLayout(1, false);
		comp_perAndCompany.setLayout(gl_composite_210);

		comp_others = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_others = new FormData();
		fd_comp_others.left = new FormAttachment(group_11, 10);
		comp_others.setLayoutData(fd_comp_others);
		GridLayout gl_composite_211 = new GridLayout(1, false);
		comp_others.setLayout(gl_composite_211);

		comp_customExtend = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_customExtend = new FormData();
		fd_comp_customExtend.left = new FormAttachment(group_11, 10);
		comp_customExtend.setLayoutData(fd_comp_customExtend);
		GridLayout gl_composite_31 = new GridLayout(1, false);
		comp_customExtend.setLayout(gl_composite_31);

		comp_certPolicy_1 = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_certPolicy_1 = new FormData();
		fd_comp_certPolicy_1.right = new FormAttachment(100, -10);
		fd_comp_certPolicy_1.left = new FormAttachment(group_11, 10);
		comp_certPolicy_1.setLayoutData(fd_comp_certPolicy_1);
		GridLayout gl_composite_41 = new GridLayout(1, false);
		comp_certPolicy_1.setLayout(gl_composite_41);

		comp_publishPolicy = new Composite(composite_1, SWT.NONE);
		FormData fd_comp_publicPolicy = new FormData();
		fd_comp_publicPolicy.left = new FormAttachment(group_11, 10);
		comp_publishPolicy.setLayoutData(fd_comp_publicPolicy);
		GridLayout gl_composite_51 = new GridLayout(1, false);
		comp_publishPolicy.setLayout(gl_composite_51);

		// 所有 的 composite面板
		panel_basic_Limit(comp_basicLimit);
		panel_certPolicy(comp_certPolicy);
		panel_keyUsage(comp_keyUsage);
		panel_enkeyUsage(comp_enKeyUsage);
		panel_CRLPublish(comp_CRLPublish);
		panel_incrementalCRL(comp_incrementalCRL);
		panel_subAltName(comp_subAltName);
		panel_pubInfo(comp_pubInfo);
		panel_userIdEx(comp_userIdEx);
		panel_perAndCompany(comp_perAndCompany);
		panel_others(comp_others);
		panel_diyExtend(comp_customExtend);
		panel_certPolicy_1(comp_certPolicy_1);
		panel_publishPolicy(comp_publishPolicy);
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
				if (l.getString("incrementalCRL").equals(tItemSelected)) {
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
				if (l.getString("otherExt").equals(tItemSelected)) {
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
			loadSpecialType(certTemplate.getSpecialType());
		}

		return tabItem_signal;
	}

	/**
	 * @Desc  根据 选择的模板类型，对一些特殊值做 勾选操作
	 * @When 新增模板 操作中
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:02:39
	 */
	private void loadSpecialType(String specialType) {
		if ("ocsp".equalsIgnoreCase(specialType)) {
			btn_digitalSign.setSelection(true);
			btn_nonRepudiation.setSelection(true);
			btn_OCSPSign.setSelection(true);
		} else if ("scep".equalsIgnoreCase(specialType)) {
			btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
		} else if ("ee_sign".equalsIgnoreCase(specialType)) {
			btn_digitalSign.setSelection(true);
			btn_nonRepudiation.setSelection(true);
			btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
			TableItem item1 = new TableItem(table_diyExtend, SWT.NONE);
			item1.setText(new String[] { "DIR", l.getString("true"), "cn=crl*,ou=crl" });
			btn_CRLPub.setSelection(true);
		} else if ("ms_smartCard".equalsIgnoreCase(specialType)) {
			btn_digitalSign.setSelection(true);
			btn_keyEn.setSelection(true);
			btn_clientAuth.setSelection(true);
			btn_pubCert.setSelection(Boolean.valueOf(certTemplate.getIspublishCert()));
			btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
		} else if ("ms_domainController".equalsIgnoreCase(specialType)) {
			btn_digitalSign.setSelection(true);
			btn_keyEn.setSelection(true);
			btn_serverAuth.setSelection(true);
			btn_clientAuth.setSelection(true);
			btn_pubCert.setSelection(Boolean.valueOf(certTemplate.getIspublishCert()));
			btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
		} else if ("code_sign".equalsIgnoreCase(specialType)) {
			btn_digitalSign.setSelection(true);
			btn_nonRepudiation.setSelection(true);
			btn_codeSign.setSelection(true);
			TableItem item2 = new TableItem(table_diyExtend, SWT.NONE);
			item2.setText(new String[] { "DIR", l.getString("true"), "cn=crl*,ou=crl" });
			btn_pubCRLList.setSelection(Boolean.valueOf(certTemplate.getIspublishCrl()));
		} else {
		}

	}

	/**
	 * @throws Exception 
	 * @Desc  封装签名模板信息，到模板对象中
	 * @When 提交信息到 Server 过程中
	 * @Authod 江岩
	 * @Date 2019年3月18日 上午11:42:12
	 */
	public void packageTempInfo(CertTemplate certTemplate) throws Exception {
		certTemplate.setKeyGeneratPlace("CLIENT"); // 固定值
		// 标准扩展
		ArrayList<Extension> standardExtensions = certTemplate.getStandardExtensions();
		// 封装 基本限制
		if (btn_useCASign.getSelection()) {
			Extension ex_basic = new Extension();
			List<ExtensionEntry> exList_basic = new ArrayList<ExtensionEntry>();

			ExtensionEntry entry_CASignCert = new ExtensionEntry();
			entry_CASignCert.setName("ISCA");
			entry_CASignCert.setValue("TRUE");
			exList_basic.add(entry_CASignCert);

			ExtensionEntry entry_maxPathLen = new ExtensionEntry();
			entry_maxPathLen.setName("MAXPATHLEN");
			if (btn_maxPathLen.getSelection()) {
				try {
					int maxPath = Integer.parseInt(text_maxPathLen.getText().trim());
					if (maxPath < 0) {
						throw new Exception(l.getString("Notice_maxPathLenRange"));
					}
				} catch (NumberFormatException e) {
					throw new Exception(l.getString("Notice_error_maxPathLenFormat"));
				}
				entry_maxPathLen.setValue(text_maxPathLen.getText());
			} else {
				entry_maxPathLen.setValue("-1");
			}
			exList_basic.add(entry_maxPathLen);
			
			ex_basic.setOID("2.5.29.19");
			ex_basic.setIscritical(btn_useBasicLimitIscritical.getSelection());
			ex_basic.setDatasource("CA");
			ex_basic.setIsmust(false);
			ex_basic.setExtensionEntrys(exList_basic);
			standardExtensions.add(ex_basic);
		}

		// 封装 证书策略
		if (btn_specifyPolicyOID.getSelection()) {
			Extension ex_certPolicy = new Extension();
			List<ExtensionEntry> exList_certPolicy = new ArrayList<ExtensionEntry>();
			String oid = text_specifyPolicyOID.getText().trim();
			if (oid == null || oid.length() == 0) {
				throw new Exception(l.getString("Notice_null_OID"));
			}
			Matcher m = Pattern.compile("^[0-9.]+$").matcher(oid);
			if (!m.matches()) {
				throw new Exception(l.getString("Notice_specifyPolicyOID"));
			}
			ExtensionEntry entry_specifyPolicyOID = new ExtensionEntry();
			entry_specifyPolicyOID.setType("POLICYOID");
			entry_specifyPolicyOID.setValue(oid);
			exList_certPolicy.add(entry_specifyPolicyOID);

			if (btn_CPS.getSelection()) {
				String cps = text_CPS.getText().trim();
				if (cps == null || cps.length() == 0) {
					throw new Exception(l.getString("Notice_null_cps"));
				}
				ExtensionEntry entry_CPS = new ExtensionEntry();
				entry_CPS.setType("CPSURI");
				entry_CPS.setValue(text_CPS.getText());
				exList_certPolicy.add(entry_CPS);
			}
			if (btn_userNotice.getSelection()) {
				String userNotice = text_userNotice.getText().trim();
				if (userNotice == null || userNotice.length() == 0) {
					throw new Exception(l.getString("Notice_null_userNotice"));
				}
				ExtensionEntry entry_userNotice = new ExtensionEntry();
				entry_userNotice.setType("USERNOTICE");
				entry_userNotice.setValue(text_userNotice.getText());
				exList_certPolicy.add(entry_userNotice);
			}
			ex_certPolicy.setOID("2.5.29.32");
			ex_certPolicy.setIscritical(btn_certPolicyIscritical.getSelection());
			ex_certPolicy.setDatasource("CA");
			ex_certPolicy.setIsmust(false);
			ex_certPolicy.setExtensionEntrys(exList_certPolicy);
			standardExtensions.add(ex_certPolicy);
		}
		// 密钥用法
		Extension ex_keyUsage = new Extension();
		List<ExtensionEntry> exList_keyUsage = new ArrayList<ExtensionEntry>();

		ExtensionEntry entry_keyUsage = new ExtensionEntry();
		entry_keyUsage.setName("USAGE");
		StringBuffer s = new StringBuffer();
		if (btn_digitalSign.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_nonRepudiation.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyEn.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_dataEn.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyAgree.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyCertSign.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_CRLSign.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_enci.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_deci.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		entry_keyUsage.setValue(s.toString());
		exList_keyUsage.add(entry_keyUsage);

		ex_keyUsage.setOID("2.5.29.15");
		ex_keyUsage.setIscritical(btn_keyUsageIscritical.getSelection());
		ex_keyUsage.setDatasource("CA");
		ex_keyUsage.setIsmust(false);
		ex_keyUsage.setExtensionEntrys(exList_keyUsage);
		standardExtensions.add(ex_keyUsage);

		// 增强密钥用法
		List<ExtensionEntry> exList_enKeyUsage = new ArrayList<ExtensionEntry>();
		if (btn_serverAuth.getSelection()) {
			ExtensionEntry entry_serverAuth = new ExtensionEntry();
			entry_serverAuth.setOid("1.3.6.1.5.5.7.3.1");
			exList_enKeyUsage.add(entry_serverAuth);
		}
		if (btn_clientAuth.getSelection()) {
			ExtensionEntry entry_clientAuth = new ExtensionEntry();
			entry_clientAuth.setOid("1.3.6.1.5.5.7.3.2");
			exList_enKeyUsage.add(entry_clientAuth);
		}
		if (btn_codeSign.getSelection()) {
			ExtensionEntry entry_codeSign = new ExtensionEntry();
			entry_codeSign.setOid("1.3.6.1.5.5.7.3.3");
			exList_enKeyUsage.add(entry_codeSign);
		}
		if (btn_emailPro.getSelection()) {
			ExtensionEntry entry_emailPro = new ExtensionEntry();
			entry_emailPro.setOid("1.3.6.1.5.5.7.3.4");
			exList_enKeyUsage.add(entry_emailPro);
		}
		if (btn_timeStamp.getSelection()) {
			ExtensionEntry entry_timeStamp = new ExtensionEntry();
			entry_timeStamp.setOid("1.3.6.1.5.5.7.3.8");
			exList_enKeyUsage.add(entry_timeStamp);
		}
		if (btn_OCSPSign.getSelection()) {
			ExtensionEntry entry_OCSPSign = new ExtensionEntry();
			entry_OCSPSign.setOid("1.3.6.1.5.5.7.3.9");
			exList_enKeyUsage.add(entry_OCSPSign);
		}
		if (oIDs.size() != 0) {
			for (String ss : oIDs) {
				ExtensionEntry entry_oid = new ExtensionEntry();
				entry_oid.setOid(ss);
				exList_enKeyUsage.add(entry_oid);
			}
		}
		if (exList_enKeyUsage.size() > 0) {
			Extension ex_enKeyUsage = new Extension();
			ex_enKeyUsage.setOID("2.5.29.37");
			ex_enKeyUsage.setIscritical(btn_enKeyUsageIscritical.getSelection());
			ex_enKeyUsage.setDatasource("CA");
			ex_enKeyUsage.setIsmust(false);
			ex_enKeyUsage.setExtensionEntrys(exList_enKeyUsage);
			standardExtensions.add(ex_enKeyUsage);
		}
		// CRL发布点设置
		TableItem[] items = table_261.getItems();
		if (items.length > 0) {
			Extension ex_CRLPub = new Extension();
			List<ExtensionEntry> exList_CRLPub = new ArrayList<ExtensionEntry>();
			for (TableItem item : items) {
				ExtensionEntry entry_CRLPub = new ExtensionEntry();
				entry_CRLPub.setName(item.getText(0));
				if (l.getString("true").equalsIgnoreCase(item.getText(1))) {
					entry_CRLPub.setAppendbasedn("true");
				} else {
					entry_CRLPub.setAppendbasedn("false");
				}
				entry_CRLPub.setValue(item.getText(2));
				exList_CRLPub.add(entry_CRLPub);
			}
			ex_CRLPub.setOID("2.5.29.31");
			ex_CRLPub.setIscritical(btn_CRLPubIscritical.getSelection());
			ex_CRLPub.setExtensionEntrys(exList_CRLPub);
			standardExtensions.add(ex_CRLPub);
		}
		// 增量CRL 发布点设置
		TableItem[] items_26_i = table_261_i.getItems();
		if (items_26_i.length > 0) {
			Extension ex_CRLPub = new Extension();
			List<ExtensionEntry> exList_CRLPub = new ArrayList<ExtensionEntry>();
			for (TableItem item : items_26_i) {
				ExtensionEntry entry_CRLPub = new ExtensionEntry();
				entry_CRLPub.setName(item.getText(0));
				if (l.getString("true").equalsIgnoreCase(item.getText(1))) {
					entry_CRLPub.setAppendbasedn("true");
				} else {
					entry_CRLPub.setAppendbasedn("false");
				}
				entry_CRLPub.setValue(item.getText(2));
				exList_CRLPub.add(entry_CRLPub);
			}
			ex_CRLPub.setOID("2.5.29.46");
			ex_CRLPub.setIscritical(false);
			ex_CRLPub.setExtensionEntrys(exList_CRLPub);
			standardExtensions.add(ex_CRLPub);
		}
		// 主题备用名称
		List<ExtensionEntry> exList_subjectAltName = new ArrayList<ExtensionEntry>();
		if (btn_dNSName.getSelection()) {
			ExtensionEntry entry_DNSName = new ExtensionEntry();
			entry_DNSName.setName("DNSNAME");
			exList_subjectAltName.add(entry_DNSName);
		}
		if (btn_ipAddress.getSelection()) {
			ExtensionEntry entry_IPAddress = new ExtensionEntry();
			entry_IPAddress.setName("IPADDRESS");
			exList_subjectAltName.add(entry_IPAddress);
		}
		if (btn_rfc822Name.getSelection()) {
			ExtensionEntry entry_rfc822Name = new ExtensionEntry();
			entry_rfc822Name.setName("RFC822NAME");
			exList_subjectAltName.add(entry_rfc822Name);
		}
		if (btn_otherName.getSelection()) {
			ExtensionEntry entry_otherName = new ExtensionEntry();
			entry_otherName.setName("OtherName.principalName");
			exList_subjectAltName.add(entry_otherName);
		}
		if (exList_subjectAltName.size() > 0) {
			Extension ex_subjectAltName = new Extension();
			ex_subjectAltName.setOID("2.5.29.17");
			ex_subjectAltName.setIscritical(btn_keyUsageIscritical.getSelection());
			ex_subjectAltName.setDatasource("CLIENT");
			ex_subjectAltName.setIsmust(false);
			ex_subjectAltName.setExtensionEntrys(exList_subjectAltName);
			standardExtensions.add(ex_subjectAltName);
		}
		// 颁发机构信息访问
		List<ExtensionEntry> exList_pubInfoAccess = new ArrayList<ExtensionEntry>();
		if (btn_locationURI.getSelection()) {
			String locationURI = text_locationURI.getText().trim();
			if (locationURI == null || locationURI.length() == 0) {
				throw new Exception(l.getString("Notice_null_locationURI"));
			}
			ExtensionEntry entry_locationURI = new ExtensionEntry();
			entry_locationURI.setName("locationURI");
			entry_locationURI.setValue(text_locationURI.getText());
			exList_pubInfoAccess.add(entry_locationURI);
		}
		if (btn_OCSPURI.getSelection()) {
			String OCSPURI = text_OCSPURI.getText().trim();
			if (OCSPURI == null || OCSPURI.length() == 0) {
				throw new Exception(l.getString("Notice_null_OCSPURI"));
			}
			ExtensionEntry entry_OCSPURI = new ExtensionEntry();
			entry_OCSPURI.setName("OCSPURI");
			entry_OCSPURI.setValue(text_OCSPURI.getText());
			exList_pubInfoAccess.add(entry_OCSPURI);
		}
		if (exList_pubInfoAccess.size() > 0) {
			Extension ex_pubInfoAccess = new Extension();
			ex_pubInfoAccess.setOID("1.3.6.1.5.5.7.1.1");
			ex_pubInfoAccess.setIscritical(btn_pubInfoAccessM.getSelection());
			ex_pubInfoAccess.setExtensionEntrys(exList_pubInfoAccess);
			standardExtensions.add(ex_pubInfoAccess);
		}
		// 用户ID扩展
		List<ExtensionEntry> exList_userIdEx = new ArrayList<ExtensionEntry>();

		if (btn_customOID.getSelection()) {
			String oid = text_customOID.getText().trim();
			if (oid == null || oid.length() == 0) {
				throw new Exception(l.getString("Notice_null_OID"));
			}
			Matcher m = Pattern.compile("^[0-9.]+$").matcher(oid);
			if (!m.matches()) {
				throw new Exception(l.getString("Notice_specifyPolicyOID"));
			}
			ExtensionEntry entry_diyOID = new ExtensionEntry();
			entry_diyOID.setOid((text_customOID.getText()));
			exList_userIdEx.add(entry_diyOID);
			Extension ex_userIdEx = new Extension();
			ex_userIdEx.setOID("1.3.6.1.4.1.27971.35.2");
			ex_userIdEx.setIscritical(btn_userIdExM.getSelection());
			ex_userIdEx.setDatasource("CA");
			ex_userIdEx.setIsmust(false);
			ex_userIdEx.setExtensionEntrys(exList_userIdEx);
			standardExtensions.add(ex_userIdEx);
		}
		// 主题密钥标识符
		if (btn_pubKeyIdentity.getSelection()) {
			Extension ex_pubKeyIdentity = new Extension();
			ex_pubKeyIdentity.setOID("2.5.29.35");
			ex_pubKeyIdentity.setIscritical(btn_pubKeyIdentityM.getSelection());
			ex_pubKeyIdentity.setDatasource("CA");
			ex_pubKeyIdentity.setIsmust(false);
			standardExtensions.add(ex_pubKeyIdentity);
		}
		// 颁发机构密钥标识符
		if (btn_subKeyIdentity.getSelection()) {
			Extension ex_subKeyIdentityC = new Extension();
			ex_subKeyIdentityC.setOID("2.5.29.14");
			ex_subKeyIdentityC.setIscritical(btn_subKeyIdentityM.getSelection());
			ex_subKeyIdentityC.setDatasource("CA");
			ex_subKeyIdentityC.setIsmust(false);
			standardExtensions.add(ex_subKeyIdentityC);
		}
		certTemplate.setStandardExtensions(standardExtensions);

		// 其他扩展，个人和企业信息
		ArrayList<Extension> otherExtensions = certTemplate.getOtherExtensions();
		if (btn_identityCode.getSelection()) {
			Extension ex_identityCode = new Extension();
			ex_identityCode.setOID("1.2.156.10260.4.1.1");
			ex_identityCode.setDatasource("CLIENT");
			ex_identityCode.setIsmust(btn_identityCodeMust.getSelection());
			otherExtensions.add(ex_identityCode);
		}
		if (btn_socialInsuranceCode.getSelection()) {
			Extension ex_socialInsuranceCode = new Extension();
			ex_socialInsuranceCode.setOID("1.2.156.10260.4.1.2");
			ex_socialInsuranceCode.setDatasource("CLIENT");
			ex_socialInsuranceCode.setIsmust(btn_socialInsuranceCodeM.getSelection());
			otherExtensions.add(ex_socialInsuranceCode);
		}
		if (btn_companyRegistCode.getSelection()) {
			Extension ex_companyRegistCode = new Extension();
			ex_companyRegistCode.setOID("1.2.156.10260.4.1.3");
			ex_companyRegistCode.setDatasource("CLIENT");
			ex_companyRegistCode.setIsmust(btn_companyRegistCodeM.getSelection());
			otherExtensions.add(ex_companyRegistCode);
		}
		if (btn_companyOrganCode.getSelection()) {
			Extension ex_companyOrganCode = new Extension();
			ex_companyOrganCode.setOID("1.2.156.10260.4.1.4");
			ex_companyOrganCode.setDatasource("CLIENT");
			ex_companyOrganCode.setIsmust(btn_companyOrganCodeM.getSelection());
			otherExtensions.add(ex_companyOrganCode);
		}
		if (btn_companyTaxCode.getSelection()) {
			Extension ex_companyTaxCode = new Extension();
			ex_companyTaxCode.setOID("1.2.156.10260.4.1.5");
			ex_companyTaxCode.setDatasource("CLIENT");
			ex_companyTaxCode.setIsmust(btn_companyTaxCodeM.getSelection());
			otherExtensions.add(ex_companyTaxCode);
		}

		// 自定义扩展
		ArrayList<Extension> customExtensions = certTemplate.getCustomerExtensions();

		for (TableItem item : table_diyExtend.getItems()) {
			Extension ex_diyOID = new Extension();
			ArrayList<ExtensionEntry> exList_diyOID = new ArrayList<ExtensionEntry>();
			ExtensionEntry entry_diyOID = new ExtensionEntry();

			entry_diyOID.setName(item.getText(0));
			ex_diyOID.setOID(item.getText(1));
			entry_diyOID.setEncoding(item.getText(2));
			if (l.getString("true").equalsIgnoreCase(item.getText(3))) {
				ex_diyOID.setIscritical(true);
			} else {
				ex_diyOID.setIscritical(false);
			}
			ex_diyOID.setDatasource("CA");
			if (l.getString("true").equalsIgnoreCase(item.getText(5))) {
				ex_diyOID.setIsmust(true);
			} else {
				ex_diyOID.setIsmust(false);
			}
			entry_diyOID.setValue(item.getText(6));
			exList_diyOID.add(entry_diyOID);
			ex_diyOID.setExtensionEntrys(exList_diyOID);
			customExtensions.add(ex_diyOID);
		}
		// 证书相关策略 和 发布策略
		certTemplate.setCheckCsrSubject(btn_useCertDN.getSelection());
		if (btn_appleCertChgKey.getSelection()) {
			certTemplate.setKeyPolicy("1");
		} else {
			certTemplate.setKeyPolicy("0");
		}
		if (btn_allowDNRepeat.getSelection()) {
			certTemplate.setPermitSameDN("1");
		} else {
			certTemplate.setPermitSameDN("0");
		}
		if (btn_pubCert.getSelection()) {
			certTemplate.setIspublishCert("1");
		} else {
			certTemplate.setIspublishCert("0");
		}
		if (btn_pubCRLList.getSelection()) {
			certTemplate.setIspublishCrl("1");
		} else {
			certTemplate.setIspublishCrl("0");
		}
	}

	/**
	 * @Desc 加载 模板信息，将模板信息填充到表单对应项中
	 * @When 修改模板 操作过程中
	 * @Authod 江岩
	 * @Date 2019年3月7日 上午10:26:12
	 * 补充  加载信息到 表单中
	 */
	private void loadCertTemplateInfo(CertTemplate certTemplate) {
		btn_allowDNRepeat.setSelection("1".equalsIgnoreCase(certTemplate.getPermitSameDN()));
		btn_appleCertChgKey.setSelection("1".equalsIgnoreCase(certTemplate.getKeyPolicy())); // rekey
		btn_useCertDN.setSelection(certTemplate.isCheckCsrSubject());
		btn_pubCRLList.setSelection("1".equalsIgnoreCase(certTemplate.getIspublishCrl()) ? true : false);
		btn_pubCert.setSelection("1".equalsIgnoreCase(certTemplate.getIspublishCert()) ? true : false);

		ArrayList<Extension> standS = certTemplate.getStandardExtensions();
		for (Extension stand : standS) {
			String OID = stand.getOID();
			if ("2.5.29.19".equalsIgnoreCase(OID)) { // 基本限制
				btn_useBasicLimitIscritical.setSelection(stand.isIscritical());
				List<ExtensionEntry> exList1 = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exList1) {
					if ("isca".equalsIgnoreCase(ex.getName())) {
						btn_useCASign.setSelection(Boolean.valueOf(ex.getValue()));
						useCASignSelection(Boolean.valueOf(ex.getValue()));
					}
					if ("maxPathLen".equalsIgnoreCase(ex.getName()) && ex.getValue() != null && ex.getValue().length() != 0 && !("-1".equalsIgnoreCase(ex.getValue()))) {
						btn_noLenLimit.setSelection(false);
						btn_maxPathLen.setSelection(true);
						text_maxPathLen.setText(ex.getValue());
					}
				}
			} else if ("2.5.29.32".equalsIgnoreCase(OID)) { // 证书策略
				btn_certPolicyIscritical.setSelection(stand.isIscritical());
				btn_specifyPolicyOID.setSelection(true);
				specifyPolicyOIDSelection(true);
				List<ExtensionEntry> exList2 = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exList2) {
					if ("policyOid".equalsIgnoreCase(ex.getType())) {
						btn_noPolicy.setSelection(false);
						btn_anyPolicy.setSelection(false);
						text_specifyPolicyOID.setText(ex.getValue());
					}
					if ("CPSURI".equalsIgnoreCase(ex.getType())) {
						btn_CPS.setSelection(true);
						text_CPS.setText(ex.getValue());
					}
					if ("userNotice".equalsIgnoreCase(ex.getType())) {
						btn_userNotice.setSelection(true);
						text_userNotice.setText(ex.getValue());
					}
				}
			} else if ("2.5.29.15".equalsIgnoreCase(OID)) { // 密钥用法
				btn_keyUsageIscritical.setSelection(stand.isIscritical());
				List<ExtensionEntry> exS = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exS) {
					char[] exC = ex.getValue().toCharArray();
					if ('1' == exC[0]) {
						btn_digitalSign.setSelection(true);
					}
					if ('1' == exC[1]) {
						btn_nonRepudiation.setSelection(true);
					}
					if ('1' == exC[2]) {
						btn_keyEn.setSelection(true);
					}
					if ('1' == exC[3]) {
						btn_dataEn.setSelection(true);
					}
					if ('1' == exC[4]) {
						btn_keyAgree.setSelection(true);
					}
					if ('1' == exC[5]) {
						btn_keyCertSign.setSelection(true);
					}
					if ('1' == exC[6]) {
						btn_CRLSign.setSelection(true);
					}
					if ('1' == exC[7]) {
						btn_enci.setSelection(true);
					}
					if ('1' == exC[8]) {
						btn_deci.setSelection(true);
					}
				}
			} else if ("2.5.29.37".equalsIgnoreCase(OID)) { // 增强密钥用法
				btn_enKeyUsageIscritical.setSelection(stand.isIscritical());
				List<ExtensionEntry> exS_2 = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exS_2) {
					if ("1.3.6.1.5.5.7.3.1".equalsIgnoreCase(ex.getOid())) {
						btn_serverAuth.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.2".equalsIgnoreCase(ex.getOid())) {
						btn_clientAuth.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.3".equalsIgnoreCase(ex.getOid())) {
						btn_codeSign.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.4".equalsIgnoreCase(ex.getOid())) {
						btn_emailPro.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.8".equalsIgnoreCase(ex.getOid())) {
						btn_timeStamp.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.9".equalsIgnoreCase(ex.getOid())) {
						btn_OCSPSign.setSelection(true);
					} else {
						oIDs.add(ex.getOid());
						list1.add(ex.getOid());
					}
				}
			} else if ("2.5.29.31".equalsIgnoreCase(OID)) { // CRL发布点设置
				btn_CRLPubIscritical.setSelection(stand.isIscritical());
				List<ExtensionEntry> exList = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exList) {
					TableItem item = new TableItem(table_261, SWT.NONE);
					String isAddDN = "";
					if ("true".equalsIgnoreCase(ex.getAppendbasedn())) {
						isAddDN = l.getString("true");
					} else {
						isAddDN = l.getString("false");
					}
					item.setText(new String[] { ex.getName(), isAddDN, ex.getValue() });
				}
			} else if ("2.5.29.46".equalsIgnoreCase(OID)) { // 增量 CRL发布点设置
				List<ExtensionEntry> exList_i = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exList_i) {
					TableItem item = new TableItem(table_261_i, SWT.NONE);
					String isAddDN = "";
					if ("true".equalsIgnoreCase(ex.getAppendbasedn())) {
						isAddDN = l.getString("true");
					} else {
						isAddDN = l.getString("false");
					}
					item.setText(new String[] { ex.getName(), isAddDN, ex.getValue() });
				}
			} else if ("2.5.29.17".equalsIgnoreCase(OID)) { // 主题备用名
				btn_subjectAltNameM.setSelection(stand.isIscritical());
				List<ExtensionEntry> exS_3 = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exS_3) {
					if ("DNSName".equalsIgnoreCase(ex.getName())) {
						btn_dNSName.setSelection(true);
					}
					if ("ipAddress".equalsIgnoreCase(ex.getName())) {
						btn_ipAddress.setSelection(true);
					}
					if ("rfc822Name".equalsIgnoreCase(ex.getName())) {
						btn_rfc822Name.setSelection(true);
					}
					if ("otherName.principalName".equalsIgnoreCase(ex.getName())) {
						btn_otherName.setSelection(true);
					}
				}
			} else if ("1.3.6.1.5.5.7.1.1".equalsIgnoreCase(OID)) { // 颁发者信息访问
				btn_pubInfoAccessM.setSelection(stand.isIscritical());
				List<ExtensionEntry> exS_4 = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exS_4) {
					if ("LocationURI".equalsIgnoreCase(ex.getName())) {
						btn_locationURI.setSelection(true);
						text_locationURI.setEnabled(true);
						text_locationURI.setText(ex.getValue());
					}
					if ("OCSPURI".equalsIgnoreCase(ex.getName())) {
						btn_OCSPURI.setSelection(true);
						text_OCSPURI.setEnabled(true);
						text_OCSPURI.setText(ex.getValue());
					}
				}
			} else if ("1.3.6.1.4.1.27971.35.2".equalsIgnoreCase(OID)) { // 用户ID扩展
				btn_userIdExM.setSelection(stand.isIscritical());
				List<ExtensionEntry> exS_5 = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exS_5) {
					btn_customOID.setSelection(true);
					text_customOID.setText(ex.getOid());
				}
			} else if ("2.5.29.14".equalsIgnoreCase(OID)) { // subjectKeyIdentifier
				btn_subKeyIdentity.setSelection(true);
				btn_subKeyIdentityM.setEnabled(true);
				btn_subKeyIdentityM.setSelection(stand.isIscritical());
			} else if ("2.5.29.35".equalsIgnoreCase(OID)) { // authorityKeyIdentifier
				btn_pubKeyIdentity.setSelection(true);
				btn_pubKeyIdentityM.setEnabled(true);
				btn_pubKeyIdentityM.setSelection(stand.isIscritical());
			}
		}
		// 其他信息
		ArrayList<Extension> otherS = certTemplate.getOtherExtensions();
		for (Extension other : otherS) {
			String oid = other.getOID();
			boolean flag = other.isIsmust();
			if ("1.2.156.10260.4.1.1".equalsIgnoreCase(oid)) { // 身份识别码
				btn_identityCode.setSelection(true);
				btn_identityCodeMust.setEnabled(true);
				btn_identityCodeMust.setSelection(flag);
			}
			if ("1.2.156.10260.4.1.2".equalsIgnoreCase(oid)) { // 社会保险号
				btn_socialInsuranceCode.setSelection(true);
				btn_socialInsuranceCodeM.setEnabled(true);
				btn_socialInsuranceCodeM.setSelection(flag);
			}
			if ("1.2.156.10260.4.1.3".equalsIgnoreCase(oid)) { // 企业工商注册号
				btn_companyRegistCode.setSelection(true);
				btn_companyRegistCodeM.setEnabled(true);
				btn_companyRegistCodeM.setSelection(flag);
			}
			if ("1.2.156.10260.4.1.4".equalsIgnoreCase(oid)) { // 企业组织机构代码
				btn_companyOrganCode.setSelection(true);
				btn_companyOrganCodeM.setEnabled(true);
				btn_companyOrganCodeM.setSelection(flag);
			}
			if ("1.2.156.10260.4.1.5".equalsIgnoreCase(oid)) { // 企业税号
				btn_companyTaxCode.setSelection(true);
				btn_companyTaxCodeM.setEnabled(true);
				btn_companyTaxCodeM.setSelection(flag);
			}
		}
		// 自定义扩展
		ArrayList<Extension> customerS = certTemplate.getCustomerExtensions();
		for (Extension customer : customerS) {
			List<ExtensionEntry> cuEntryS = customer.getExtensionEntrys();
			for (ExtensionEntry ex : cuEntryS) {
				TableItem item = new TableItem(table_diyExtend, SWT.NONE);
				String flag_1 = l.getString("false"), flag_2 = l.getString("false");
				if (customer.isIscritical()) {
					flag_1 = l.getString("true");
				}
				if (customer.isIsmust()) {
					flag_2 = l.getString("true");
				}
				item.setText(new String[] { ex.getName(), customer.getOID(), ex.getEncoding(), flag_1,
						customer.getDatasource(), flag_2, ex.getValue() });
			}
		}
	}

	// 基本限制视图
	private void panel_basic_Limit(Composite composite) {

		Group group_21 = new Group(composite, SWT.NONE);
		GridLayout gl_group_21 = new GridLayout(2, false);
		gl_group_21.marginTop = 10;
		gl_group_21.marginBottom = 10;
		gl_group_21.marginLeft = 10;
		gl_group_21.marginRight = 10;
		gl_group_21.horizontalSpacing = 50;
		gl_group_21.verticalSpacing = 10;

		group_21.setLayout(gl_group_21);
		group_21.setText(l.getString("basic_limit"));

		GridData gd_group_21 = new GridData();
		gd_group_21.heightHint = 493;
		gd_group_21.widthHint = 500;
		group_21.setLayoutData(gd_group_21);

		btn_useCASign = new Button(group_21, SWT.CHECK);
		GridData gd_btn_useCASign = new GridData();
		gd_btn_useCASign.widthHint = 160;
		btn_useCASign.setLayoutData(gd_btn_useCASign);
		btn_useCASign.setText(l.getString("useCAsign"));
		btn_useCASign.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btn_useCASign.getSelection()) {
					btn_useBasicLimitIscritical.setEnabled(true);
					btn_noLenLimit.setEnabled(true);
					btn_maxPathLen.setEnabled(true);
					btn_noLenLimit.setSelection(true);
				} else {
					btn_useBasicLimitIscritical.setSelection(false);
					btn_useBasicLimitIscritical.setEnabled(false);
					btn_noLenLimit.setEnabled(false);
					btn_maxPathLen.setEnabled(false);
					btn_noLenLimit.setSelection(true);
					btn_maxPathLen.setSelection(false);
					text_maxPathLen.setText("");
					text_maxPathLen.setEnabled(false);
				}
			}
		});

		btn_useBasicLimitIscritical = new Button(group_21, SWT.CHECK);
		GridData gd_btn_useBasicLimitIscritical = new GridData();
		gd_btn_useBasicLimitIscritical.widthHint = 120;
		btn_useBasicLimitIscritical.setLayoutData(gd_btn_useBasicLimitIscritical);
		btn_useBasicLimitIscritical.setText(l.getString("critical"));

		if (!certTemplate.isSignal()) {
			btn_useCASign.setEnabled(false);
			btn_useBasicLimitIscritical.setEnabled(false);
		}

		Label label = new Label(group_21, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData();
		gd_label.widthHint = 480;
		gd_label.horizontalSpan = 2;
		label.setLayoutData(gd_label);

		Group group_211 = new Group(group_21, SWT.NONE);
		GridLayout gl_group_211 = new GridLayout(2, false);
		gl_group_211.marginTop = 10;
		gl_group_211.marginBottom = 10;
		gl_group_211.marginLeft = 10;
		gl_group_211.marginRight = 10;
		gl_group_211.verticalSpacing = 15;
		gl_group_211.horizontalSpacing = 10;
		group_211.setLayout(gl_group_211);

		GridData gd_group_211 = new GridData();
		gd_group_211.horizontalSpan = 2;
		gd_group_211.widthHint = 464;
		group_211.setLayoutData(gd_group_211);
		group_211.setText(l.getString("pathLengthLimit"));

		btn_noLenLimit = new Button(group_211, SWT.RADIO);
		GridData gd_btn_noLenLimit = new GridData();
		gd_btn_noLenLimit.widthHint = 170;
		gd_btn_noLenLimit.horizontalSpan = 2;
		btn_noLenLimit.setLayoutData(gd_btn_noLenLimit);
		btn_noLenLimit.setText(l.getString("LengthNoLimit"));
		btn_noLenLimit.setEnabled(false);
		btn_noLenLimit.setSelection(true);
		btn_noLenLimit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btn_noLenLimit.getSelection()) {
					text_maxPathLen.setText("");
					text_maxPathLen.setEnabled(false);
				}
			}
		});

		btn_maxPathLen = new Button(group_211, SWT.RADIO);
		GridData gd_btn_maxPathLen = new GridData();
		gd_btn_maxPathLen.widthHint = 220;
		btn_maxPathLen.setLayoutData(gd_btn_maxPathLen);
		btn_maxPathLen.setText(l.getString("maxPathLength"));
		btn_maxPathLen.setEnabled(false);
		btn_maxPathLen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btn_useCASign.getSelection()) {
					text_maxPathLen.setEnabled(true);
				}
			}
		});

		text_maxPathLen = new Text(group_211, SWT.BORDER);
		GridData gd_text_maxPathLen = new GridData();
		gd_text_maxPathLen.widthHint = 188;
		text_maxPathLen.setTextLimit(3);
		text_maxPathLen.setLayoutData(gd_text_maxPathLen);
		text_maxPathLen.setEnabled(false);
		
	}
	

	// 选中基本限制，使其他设置可选
	private void useCASignSelection(boolean flag) {
		if (!btn_useCASign.getSelection()) {
			btn_useBasicLimitIscritical.setSelection(false);
			btn_noLenLimit.setEnabled(true);
			btn_maxPathLen.setSelection(false);
			text_maxPathLen.setText("");
		}
		btn_useBasicLimitIscritical.setEnabled(flag);
		btn_noLenLimit.setEnabled(flag);
		btn_maxPathLen.setEnabled(flag);
		text_maxPathLen.setEnabled(flag);
	}

	// 证书策略视图
	private void panel_certPolicy(Composite composite) {
		Group group_22 = new Group(composite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginTop = 10;
		gridLayout.marginBottom = 10;
		gridLayout.marginLeft = 10;
		gridLayout.marginRight = 10;
		group_22.setLayout(gridLayout);
		group_22.setText(l.getString("certPolicy"));
		GridData gd_group_22 = new GridData();
		gd_group_22.heightHint = 493;
		gd_group_22.widthHint = 500;
		group_22.setLayoutData(gd_group_22);

		btn_certPolicyIscritical = new Button(group_22, SWT.CHECK);
		GridData gd_btn_certPolicyIscritical = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_certPolicyIscritical.widthHint = 70;
		gd_btn_certPolicyIscritical.heightHint = 35;
		btn_certPolicyIscritical.setLayoutData(gd_btn_certPolicyIscritical);
		btn_certPolicyIscritical.setText(l.getString("critical"));

		Label label_6 = new Label(group_22, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_6 = new GridData();
		gd_label_6.widthHint = 480;
		label_6.setLayoutData(gd_label_6);

		Group group_221 = new Group(group_22, SWT.NONE);
		GridLayout gl_group_221 = new GridLayout(2, false);
		gl_group_221.marginTop = 10;
		gl_group_221.marginBottom = 10;
		gl_group_221.marginLeft = 10;
		gl_group_221.marginRight = 10;
		gl_group_221.horizontalSpacing = 10;
		gl_group_221.verticalSpacing = 15;
		group_221.setLayout(gl_group_221);

		GridData gd_group_221 = new GridData();
		gd_group_221.widthHint = 470;
		group_221.setLayoutData(gd_group_221);

		btn_noPolicy = new Button(group_221, SWT.RADIO);
		GridData gd_btn_noPolicy = new GridData();
		gd_btn_noPolicy.horizontalSpan = 2;
		btn_noPolicy.setLayoutData(gd_btn_noPolicy);
		btn_noPolicy.setText(l.getString("none"));
		btn_noPolicy.setSelection(true);
		btn_noPolicy.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				policyOID_false();
			}
		});

		btn_anyPolicy = new Button(group_221, SWT.RADIO);
		GridData gd_btn_anyPolicy = new GridData();
		gd_btn_anyPolicy.horizontalSpan = 2;
		gd_btn_anyPolicy.heightHint = 25;
		btn_anyPolicy.setLayoutData(gd_btn_anyPolicy);
		btn_anyPolicy.setText(l.getString("anyPolicy"));
		btn_anyPolicy.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				policyOID_false();
			}
		});

		btn_specifyPolicyOID = new Button(group_221, SWT.RADIO);
		GridData gd_btn_specifyPolicyOID = new GridData();
		btn_specifyPolicyOID.setLayoutData(gd_btn_specifyPolicyOID);
		btn_specifyPolicyOID.setText(l.getString("specifyOID"));
		btn_specifyPolicyOID.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_specifyPolicyOID.getSelection();
				specifyPolicyOIDSelection(flag);
			}
		});

		text_specifyPolicyOID = new Text(group_221, SWT.BORDER);
		GridData gd_text_specifyPolicyOID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_specifyPolicyOID.widthHint = 226;
		text_specifyPolicyOID.setLayoutData(gd_text_specifyPolicyOID);
		gd_btn_specifyPolicyOID.widthHint = 186;
		text_specifyPolicyOID.setTextLimit(50);
		text_specifyPolicyOID.setEnabled(false);

		Group group_222 = new Group(group_22, SWT.NONE);
		GridLayout gl_group_222 = new GridLayout(2, false);
		gl_group_222.horizontalSpacing = 2;
		gl_group_222.marginLeft = 10;
		gl_group_222.verticalSpacing = 15;
		group_222.setLayout(gl_group_222);

		GridData gd_group_222 = new GridData();
		gd_group_222.heightHint = 102;
		gd_group_222.widthHint = 470;
		group_222.setLayoutData(gd_group_222);

		btn_CPS = new Button(group_222, SWT.CHECK);
		GridData gd_btn_CPS = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_CPS.widthHint = 60;
		btn_CPS.setLayoutData(gd_btn_CPS);
		btn_CPS.setText("CPS");
		btn_CPS.setEnabled(false);
		btn_CPS.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_CPS.getSelection();
				text_CPS.setEnabled(flag);
			}
		});

		text_CPS = new Text(group_222, SWT.BORDER);
		GridData gd_text_CPS = new GridData();
		gd_text_CPS.widthHint = 230;
		text_CPS.setLayoutData(gd_text_CPS);
		text_CPS.setTextLimit(120);
		text_CPS.setEnabled(false);

		btn_userNotice = new Button(group_222, SWT.CHECK);
		GridData gd_btn_userNotice = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_userNotice.heightHint = 34;
		gd_btn_userNotice.widthHint = 195;
		btn_userNotice.setLayoutData(gd_btn_userNotice);
		btn_userNotice.setBounds(10, 75, 120, 21);
		btn_userNotice.setText(l.getString("userNotice"));
		btn_userNotice.setEnabled(false);
		btn_userNotice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_userNotice.getSelection();
				text_userNotice.setEnabled(flag);
			}
		});

		text_userNotice = new Text(group_222, SWT.BORDER);
		GridData gd_text_userNotice = new GridData();
		gd_text_userNotice.widthHint = 230;
		text_userNotice.setLayoutData(gd_text_userNotice);
		text_userNotice.setTextLimit(120);
		text_userNotice.setEnabled(false);
	}

	// 选中证书策略，使其他设置可选
	private void specifyPolicyOIDSelection(boolean flag) {
		text_specifyPolicyOID.setEnabled(flag);
		btn_CPS.setEnabled(flag);
		btn_userNotice.setEnabled(flag);
	}
	
	// 取消所有的证书策略
	private void policyOID_false() {
		text_specifyPolicyOID.setText("");
		text_specifyPolicyOID.setEnabled(false);
		btn_CPS.setSelection(false);
		btn_userNotice.setSelection(false);
		btn_CPS.setEnabled(false);
		btn_userNotice.setEnabled(false);
		text_CPS.setText("");
		text_CPS.setEnabled(false);
		text_userNotice.setText("");
		text_userNotice.setEnabled(false);
	}

	// 密钥用法视图
	private void panel_keyUsage(Composite composite) {
		Group group_23 = new Group(composite, SWT.NONE);
		GridData gd_group_23 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_23.heightHint = 493;
		gd_group_23.widthHint = 500;
		group_23.setLayoutData(gd_group_23);
		group_23.setBounds(0, 0, 480, 412);
		group_23.setText(l.getString("keyUsage"));

		GridLayout gl_group_23 = new GridLayout(2, false);
		gl_group_23.marginTop = 10;
		gl_group_23.marginBottom = 10;
		gl_group_23.marginLeft = 10;
		gl_group_23.marginRight = 10;
		gl_group_23.horizontalSpacing = 10;
		gl_group_23.verticalSpacing = 10;

		group_23.setLayout(gl_group_23);

		btn_keyUsageIscritical = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyUsageIscritical = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyUsageIscritical.widthHint = 120;
		btn_keyUsageIscritical.setLayoutData(gd_btn_keyUsageIscritical);
		btn_keyUsageIscritical.setText(l.getString("critical"));
		btn_keyUsageIscritical.setSelection(true);
		new Label(group_23, SWT.NONE);

		Label label_1 = new Label(group_23, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_label_1.widthHint = 480;
		label_1.setLayoutData(gd_label_1);
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_digitalSign = new Button(group_23, SWT.CHECK);
		GridData gd_btn_digitalSign = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_digitalSign.widthHint = 249;
		btn_digitalSign.setLayoutData(gd_btn_digitalSign);
		btn_digitalSign.setText("digitalSignature");

		btn_keyCertSign = new Button(group_23, SWT.CHECK);
		btn_keyCertSign.setText("keyCertSign");
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_nonRepudiation = new Button(group_23, SWT.CHECK);
		btn_nonRepudiation.setText("nonRepudiation");

		btn_CRLSign = new Button(group_23, SWT.CHECK);
		btn_CRLSign.setText("CRLSign");
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_keyEn = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyEn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyEn.widthHint = 129;
		btn_keyEn.setLayoutData(gd_btn_keyEn);
		btn_keyEn.setText("keyEncipherment");

		btn_enci = new Button(group_23, SWT.CHECK);
		btn_enci.setText("encipherOnly");
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_dataEn = new Button(group_23, SWT.CHECK);
		btn_dataEn.setText("dataEncipherment");

		btn_deci = new Button(group_23, SWT.CHECK);
		btn_deci.setText("decipherOnly");
		new Label(group_23, SWT.NONE);
		new Label(group_23, SWT.NONE);

		btn_keyAgree = new Button(group_23, SWT.CHECK);
		btn_keyAgree.setText("keyAgreement");
		new Label(group_23, SWT.NONE);
	}

	// 增强密钥算法视图
	private void panel_enkeyUsage(Composite composite) {
		Group group_24 = new Group(composite, SWT.NONE);
		GridData gd_group_24 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_24.heightHint = 493;
		gd_group_24.widthHint = 500;
		group_24.setLayoutData(gd_group_24);
		group_24.setText(l.getString("en_keyUsage"));

		GridLayout gl_group_24 = new GridLayout(1, false);
		gl_group_24.marginTop = 10;
		gl_group_24.marginBottom = 10;
		gl_group_24.marginLeft = 10;
		gl_group_24.marginHeight = 10;
		gl_group_24.horizontalSpacing = 10;
		gl_group_24.verticalSpacing = 10;

		group_24.setLayout(gl_group_24);

		btn_enKeyUsageIscritical = new Button(group_24, SWT.CHECK);
		GridData gd_btn_enKeyUsageIscritical = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_enKeyUsageIscritical.heightHint = 23;
		gd_btn_enKeyUsageIscritical.widthHint = 100;
		btn_enKeyUsageIscritical.setLayoutData(gd_btn_enKeyUsageIscritical);
		btn_enKeyUsageIscritical.setText(l.getString("critical"));

		Label label_2 = new Label(group_24, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_2.widthHint = 480;
		label_2.setLayoutData(gd_label_2);

		Group group_241 = new Group(group_24, SWT.NONE);
		GridData gd_group_241 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_241.heightHint = 181;
		gd_group_241.widthHint = 469;

		group_241.setLayoutData(gd_group_241);
		group_241.setText(l.getString("standardExtKeyUsage"));
		GridLayout gl_group_241 = new GridLayout(2, false);
		gl_group_241.marginTop = 10;
		gl_group_241.marginBottom = 10;
		gl_group_241.marginLeft = 10;
		gl_group_241.marginRight = 10;
		gl_group_241.horizontalSpacing = 10;
		gl_group_241.verticalSpacing = 15;

		group_241.setLayout(gl_group_241);

		btn_serverAuth = new Button(group_241, SWT.CHECK);
		GridData gd_btn_serverAuth = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_serverAuth.heightHint = 38;
		btn_serverAuth.setLayoutData(gd_btn_serverAuth);
		btn_serverAuth.setText("serverAuth");

		btn_emailPro = new Button(group_241, SWT.CHECK);
		GridData gd_btn_emailPro = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_emailPro.widthHint = 177;
		btn_emailPro.setLayoutData(gd_btn_emailPro);
		btn_emailPro.setText("emailProtection");

		btn_clientAuth = new Button(group_241, SWT.CHECK);
		GridData gd_btn_clientAuth = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_clientAuth.heightHint = 34;
		btn_clientAuth.setLayoutData(gd_btn_clientAuth);
		btn_clientAuth.setText("clientAuth");

		btn_timeStamp = new Button(group_241, SWT.CHECK);
		btn_timeStamp.setText("timeStamping");

		btn_codeSign = new Button(group_241, SWT.CHECK);
		GridData gd_btn_codeSign = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_codeSign.heightHint = 40;
		gd_btn_codeSign.widthHint = 234;
		btn_codeSign.setLayoutData(gd_btn_codeSign);
		btn_codeSign.setText("codeSigning");

		btn_OCSPSign = new Button(group_241, SWT.CHECK);
		btn_OCSPSign.setText("OCSPSigning");

		Group group_132 = new Group(group_24, SWT.NONE);

		GridLayout gl_group_132 = new GridLayout(4, false);
		gl_group_132.marginTop = 10;
		gl_group_132.marginBottom = 10;
		gl_group_132.marginLeft = 10;
		gl_group_132.marginRight = 10;
		gl_group_132.horizontalSpacing = 10;
		gl_group_132.verticalSpacing = 10;

		group_132.setLayout(gl_group_132);
		GridData gd_group_132 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_132.widthHint = 471;
		gd_group_132.heightHint = 145;
		group_132.setLayoutData(gd_group_132);

		Label lblNewLabel_132 = new Label(group_132, SWT.NONE);
		lblNewLabel_132.setText("OID:");

		text_oID = new Text(group_132, SWT.BORDER);
		GridData gd_text_oID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_oID.widthHint = 252;
		text_oID.setLayoutData(gd_text_oID);
		text_oID.setTextLimit(50);
		new Label(group_132, SWT.NONE);

		btn_add = new Button(group_132, SWT.NONE);
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add.widthHint = 81;
		btn_add.setLayoutData(gd_btn_add);
		btn_add.setText(l.getString("add"));
		btn_add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				String oid = text_oID.getText().trim();
				if (oid == null || oid.length() == 0) {
					Panel_MessageDialog dialog = new Panel_MessageDialog("error", l.getString("Notice_null_OID"));
					dialog.setBlockOnOpen(true);
					dialog.open();
					return;
				}
				Matcher m = Pattern.compile("^[0-9.]+$").matcher(oid);
				if (!m.matches()) {
					Panel_MessageDialog dialog = new Panel_MessageDialog("error",
							l.getString("Notice_specifyPolicyOID"));
					dialog.setBlockOnOpen(true);
					dialog.open();
					return;
				}
				if (!oIDs.contains(oid)) {
					oIDs.add(oid);
					list1.add(oid);
				} else {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_exist_OID"));
					messageBox.open();
				}
				text_oID.setText("");
			}
		});
		new Label(group_132, SWT.NONE);
		list1 = new org.eclipse.swt.widgets.List(group_132, SWT.BORDER);
		GridData gd_list1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_list1.widthHint = 251;
		gd_list1.heightHint = 78;
		list1.setLayoutData(gd_list1);
		new Label(group_132, SWT.NONE);

		btn_delete = new Button(group_132, SWT.NONE);
		GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_delete.widthHint = 84;
		btn_delete.setLayoutData(gd_btn_delete);
		btn_delete.setText(l.getString("delete"));
		btn_delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (list1.getSelectionIndex() == -1) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_not_chooseOID"));
					messageBox.open();
					return;
				}
				String oID = list1.getSelection()[0];
				oIDs.remove(oID);
				list1.remove(list1.getSelectionIndex());
			}
		});
	}
	

	// CRL发布点设置
	private void panel_CRLPublish(Composite composite) {
		Group group_261 = new Group(composite, SWT.NONE);
		GridData gd_group_261 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_261.heightHint = 493;
		gd_group_261.widthHint = 500;
		group_261.setLayoutData(gd_group_261);
		group_261.setBounds(0, 0, 480, 412);
		group_261.setText(l.getString("CRLPublishPoint"));

		GridLayout gl_group_261 = new GridLayout(4, false);
		gl_group_261.marginTop = 10;
		gl_group_261.marginBottom = 10;
		gl_group_261.marginLeft = 10;
		gl_group_261.marginHeight = 10;
		gl_group_261.horizontalSpacing = 10;
		gl_group_261.verticalSpacing = 10;

		group_261.setLayout(gl_group_261);

		btn_CRLPubIscritical = new Button(group_261, SWT.CHECK);
		GridData gd_btn_CRLPubIscritical = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btn_CRLPubIscritical.widthHint = 123;
		btn_CRLPubIscritical.setLayoutData(gd_btn_CRLPubIscritical);
		btn_CRLPubIscritical.setText(l.getString("critical"));
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		Label label_3 = new Label(group_261, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_label_3.widthHint = 480;
		label_3.setLayoutData(gd_label_3);

		Label lbl_type = new Label(group_261, SWT.NONE);
		GridData gd_lbl_type = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_type.widthHint = 42;
		lbl_type.setLayoutData(gd_lbl_type);
		lbl_type.setText(l.getString("type") + ":");

		combo_CRLType = new Combo(group_261, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_CRLType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_CRLType.widthHint = 66;
		combo_CRLType.setLayoutData(gd_combo_CRLType);
		combo_CRLType.add("DIR");
		combo_CRLType.add("URI");
		combo_CRLType.setText("DIR");

		btn_addDN = new Button(group_261, SWT.CHECK);
		GridData gd_btn_addDN = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_addDN.widthHint = 175;
		btn_addDN.setLayoutData(gd_btn_addDN);
		btn_addDN.setText(l.getString("addSystemDN"));
		new Label(group_261, SWT.NONE);

		Label lbl_basicPublish = new Label(group_261, SWT.NONE);
		lbl_basicPublish.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lbl_basicPublish.setText(l.getString("basicPublishPoint") + ":");

		text_basicPub = new Text(group_261, SWT.BORDER);
		GridData gd_text_basicPub = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_basicPub.widthHint = 186;
		text_basicPub.setTextLimit(120);
		text_basicPub.setLayoutData(gd_text_basicPub);

		btn_add_261 = new Button(group_261, SWT.NONE);
		GridData gd_btn_add_261 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add_261.widthHint = 74;
		btn_add_261.setLayoutData(gd_btn_add_261);
		btn_add_261.setText(l.getString("add"));
		btn_add_261.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String basic_Pub = text_basicPub.getText().trim();
				if (basic_Pub == null || basic_Pub.length() == 0) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_null_basicPublishPoint"));
					messageBox.open();
					return;
				}
				TableItem it = new TableItem(table_261, SWT.NONE);
				String s = l.getString("false");
				if (btn_addDN.getSelection()) {
					s = l.getString("true");
				}
				it.setText(new String[] { combo_CRLType.getText(), s, text_basicPub.getText(), });
			}
		});
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		table_261 = new Table(group_261, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table_261 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_table_261.widthHint = 462;
		gd_table_261.heightHint = 198;
		table_261.setLayoutData(gd_table_261);
		table_261.setHeaderVisible(true);
		table_261.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table_261, SWT.NONE);
		tblclmnNewColumn.setWidth(98);
		tblclmnNewColumn.setText(l.getString("type"));

		TableColumn tblclmnNewColumn_1 = new TableColumn(table_261, SWT.NONE);
		tblclmnNewColumn_1.setWidth(163);
		tblclmnNewColumn_1.setText(l.getString("addSystemDN"));

		TableColumn tblclmnNewColumn_2 = new TableColumn(table_261, SWT.NONE);
		tblclmnNewColumn_2.setWidth(183);
		tblclmnNewColumn_2.setText(l.getString("basicPublishPoint"));
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		btn_delete_262 = new Button(group_261, SWT.NONE);
		GridData gd_btn_delete_262 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_delete_262.widthHint = 73;
		btn_delete_262.setLayoutData(gd_btn_delete_262);
		btn_delete_262.setText(l.getString("delete"));
		btn_delete_262.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table_261.getSelection().length == -1) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.NONE);
					messageBox.setMessage(l.getString("Notice_not_chooseCRL"));
					messageBox.open();
					return;
				}
				table_261.remove(table_261.getSelectionIndex());
			}
		});
	}

	// 增量 CRL发布点设置
	private void panel_incrementalCRL(Composite composite) {
		Group group_261 = new Group(composite, SWT.NONE);
		GridData gd_group_261 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_261.heightHint = 493;
		gd_group_261.widthHint = 500;
		group_261.setLayoutData(gd_group_261);
		group_261.setBounds(0, 0, 480, 412);
		group_261.setText(l.getString("incrementalCRL"));

		GridLayout gl_group_261 = new GridLayout(4, false);
		gl_group_261.marginTop = 10;
		gl_group_261.marginBottom = 10;
		gl_group_261.marginLeft = 10;
		gl_group_261.marginHeight = 10;
		gl_group_261.horizontalSpacing = 10;
		gl_group_261.verticalSpacing = 10;

		group_261.setLayout(gl_group_261);

		Label lbl_type = new Label(group_261, SWT.NONE);
		GridData gd_lbl_type = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_type.widthHint = 42;
		lbl_type.setLayoutData(gd_lbl_type);
		lbl_type.setText(l.getString("type") + ":");

		combo_CRLType_i = new Combo(group_261, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_CRLType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_CRLType.widthHint = 65;
		combo_CRLType_i.setLayoutData(gd_combo_CRLType);
		combo_CRLType_i.add("DIR");
		combo_CRLType_i.add("URI");
		combo_CRLType_i.setText("DIR");

		btn_addDN_i = new Button(group_261, SWT.CHECK);
		GridData gd_btn_addDN = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_addDN.widthHint = 175;
		btn_addDN_i.setLayoutData(gd_btn_addDN);
		btn_addDN_i.setText(l.getString("addSystemDN"));
		new Label(group_261, SWT.NONE);

		Label lbl_basicPublish = new Label(group_261, SWT.NONE);
		lbl_basicPublish.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lbl_basicPublish.setText(l.getString("basicPublishPoint") + ":");

		text_basicPub_i = new Text(group_261, SWT.BORDER);
		GridData gd_text_basicPub = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_basicPub.widthHint = 186;
		text_basicPub_i.setTextLimit(120);
		text_basicPub_i.setLayoutData(gd_text_basicPub);

		btn_add_261_i = new Button(group_261, SWT.NONE);
		GridData gd_btn_add_261 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add_261.widthHint = 74;
		btn_add_261_i.setLayoutData(gd_btn_add_261);
		btn_add_261_i.setText(l.getString("add"));
		btn_add_261_i.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String basic_Pub = text_basicPub_i.getText().trim();
				if (basic_Pub == null || basic_Pub.length() == 0) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_null_basicPublishPoint"));
					messageBox.open();
					return;
				}
				TableItem it = new TableItem(table_261_i, SWT.NONE);
				String s = l.getString("false");
				if (btn_addDN_i.getSelection()) {
					s = l.getString("true");
				}
				it.setText(new String[] { combo_CRLType_i.getText(), s, text_basicPub_i.getText(), });
			}
		});
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		table_261_i = new Table(group_261, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table_261 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_table_261.widthHint = 446;
		gd_table_261.heightHint = 198;
		table_261_i.setLayoutData(gd_table_261);
		table_261_i.setHeaderVisible(true);
		table_261_i.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table_261_i, SWT.NONE);
		tblclmnNewColumn.setWidth(84);
		tblclmnNewColumn.setText(l.getString("type"));

		TableColumn tblclmnNewColumn_1 = new TableColumn(table_261_i, SWT.NONE);
		tblclmnNewColumn_1.setWidth(146);
		tblclmnNewColumn_1.setText(l.getString("addSystemDN"));

		TableColumn tblclmnNewColumn_2 = new TableColumn(table_261_i, SWT.NONE);
		tblclmnNewColumn_2.setWidth(171);
		tblclmnNewColumn_2.setText(l.getString("basicPublishPoint"));
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		btn_delete_262_i = new Button(group_261, SWT.NONE);
		GridData gd_btn_delete_262 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_delete_262.widthHint = 73;
		btn_delete_262_i.setLayoutData(gd_btn_delete_262);
		btn_delete_262_i.setText(l.getString("delete"));
		btn_delete_262_i.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table_261_i.getSelection().length == -1) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.NONE);
					messageBox.setMessage(l.getString("Notice_not_chooseCRL"));
					messageBox.open();
					return;
				}
				table_261_i.remove(table_261_i.getSelectionIndex());
			}
		});
	}

	// 主题备用名称
	private void panel_subAltName(Composite composite) {
		Group group_27 = new Group(composite, SWT.NONE);
		GridData gd_group_27 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_27.heightHint = 493;
		gd_group_27.widthHint = 500;
		group_27.setLayoutData(gd_group_27);
		group_27.setBounds(0, 0, 480, 412);
		group_27.setText(l.getString("subjectAltName"));

		GridLayout gl_group_27 = new GridLayout(1, false);
		gl_group_27.marginTop = 10;
		gl_group_27.marginBottom = 10;
		gl_group_27.marginLeft = 10;
		gl_group_27.marginHeight = 10;
		gl_group_27.horizontalSpacing = 10;
		gl_group_27.verticalSpacing = 10;

		group_27.setLayout(gl_group_27);

		btn_subjectAltNameM = new Button(group_27, SWT.CHECK);
		GridData gd_btn_subjectAltNameM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_subjectAltNameM.heightHint = 23;
		gd_btn_subjectAltNameM.widthHint = 111;
		btn_subjectAltNameM.setLayoutData(gd_btn_subjectAltNameM);
		btn_subjectAltNameM.setText(l.getString("critical"));

		Label label_5 = new Label(group_27, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_5 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_5.widthHint = 480;
		label_5.setLayoutData(gd_label_5);

		Group group_28 = new Group(group_27, SWT.NONE);
		GridData gd_group_28 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_28.widthHint = 477;
		gd_group_28.heightHint = 228;
		group_28.setLayoutData(gd_group_28);

		GridLayout gl_group_28 = new GridLayout(1, false);
		gl_group_28.marginTop = 10;
		gl_group_28.marginBottom = 10;
		gl_group_28.marginLeft = 10;
		gl_group_28.marginHeight = 10;
		gl_group_28.horizontalSpacing = 10;
		gl_group_28.verticalSpacing = 15;
		group_28.setLayout(gl_group_28);
		group_28.setText(l.getString("allowSubjectAltName"));

		btn_dNSName = new Button(group_28, SWT.CHECK);
		GridData gd_btn_dNSName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_dNSName.heightHint = 34;
		gd_btn_dNSName.widthHint = 153;
		btn_dNSName.setLayoutData(gd_btn_dNSName);
		btn_dNSName.setText(l.getString("DNSName"));

		btn_ipAddress = new Button(group_28, SWT.CHECK);
		GridData gd_btn_ipAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_ipAddress.heightHint = 34;
		gd_btn_ipAddress.widthHint = 150;
		btn_ipAddress.setLayoutData(gd_btn_ipAddress);
		btn_ipAddress.setText(l.getString("IPAddress"));

		btn_rfc822Name = new Button(group_28, SWT.CHECK);
		GridData gd_btn_rfc822Name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_rfc822Name.heightHint = 33;
		gd_btn_rfc822Name.widthHint = 180;
		btn_rfc822Name.setLayoutData(gd_btn_rfc822Name);
		btn_rfc822Name.setText(l.getString("rfc822Name"));

		btn_otherName = new Button(group_28, SWT.CHECK);
		GridData gd_btn_otherName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_otherName.heightHint = 34;
		gd_btn_otherName.widthHint = 177;
		btn_otherName.setLayoutData(gd_btn_otherName);
		btn_otherName.setText(l.getString("otherName"));
	}

	// 颁发机构信息访问
	private void panel_pubInfo(Composite composite) {
		Group group_28 = new Group(composite, SWT.NONE);
		GridData gd_group_28 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_28.heightHint = 493;
		gd_group_28.widthHint = 500;
		group_28.setLayoutData(gd_group_28);
		group_28.setBounds(0, 0, 480, 412);
		group_28.setText(l.getString("publishInfoAccess"));

		GridLayout gl_group_28 = new GridLayout(2, false);
		gl_group_28.marginTop = 10;
		gl_group_28.marginBottom = 10;
		gl_group_28.marginLeft = 10;
		gl_group_28.marginHeight = 10;
		gl_group_28.horizontalSpacing = 10;
		gl_group_28.verticalSpacing = 10;
		group_28.setLayout(gl_group_28);

		btn_pubInfoAccessM = new Button(group_28, SWT.CHECK);
		GridData gd_btn_pubInfoAccessM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_pubInfoAccessM.heightHint = 27;
		gd_btn_pubInfoAccessM.widthHint = 133;
		btn_pubInfoAccessM.setLayoutData(gd_btn_pubInfoAccessM);
		btn_pubInfoAccessM.setText(l.getString("critical"));
		new Label(group_28, SWT.NONE);

		Label label_4 = new Label(group_28, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_4 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_label_4.widthHint = 480;
		label_4.setLayoutData(gd_label_4);

		btn_locationURI = new Button(group_28, SWT.CHECK);
		GridData gd_btn_locationURI = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_locationURI.heightHint = 42;
		gd_btn_locationURI.widthHint = 231;
		btn_locationURI.setLayoutData(gd_btn_locationURI);
		btn_locationURI.setText("LocationURI");
		btn_locationURI.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				text_locationURI.setEnabled(btn_locationURI.getSelection());
			}
		});

		text_locationURI = new Text(group_28, SWT.BORDER);
		GridData gd_text_locationURI = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_locationURI.widthHint = 209;
		text_locationURI.setLayoutData(gd_text_locationURI);
		text_locationURI.setTextLimit(120);
		text_locationURI.setEnabled(false);

		btn_OCSPURI = new Button(group_28, SWT.CHECK);
		GridData gd_btn_OCSPURI = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_OCSPURI.heightHint = 39;
		gd_btn_OCSPURI.widthHint = 125;
		btn_OCSPURI.setLayoutData(gd_btn_OCSPURI);
		btn_OCSPURI.setText("OCSPURI");
		btn_OCSPURI.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				text_OCSPURI.setEnabled(btn_OCSPURI.getSelection());
			}
		});
		text_OCSPURI = new Text(group_28, SWT.BORDER);
		GridData gd_text_OCSPURI = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_OCSPURI.widthHint = 210;
		text_OCSPURI.setLayoutData(gd_text_OCSPURI);
		text_OCSPURI.setTextLimit(120);
		text_OCSPURI.setEnabled(false);

	}

	// 用户ID扩展
	private void panel_userIdEx(Composite composite) {
		Group group_29 = new Group(composite, SWT.NONE);
		GridData gd_group_29 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_29.heightHint = 493;
		gd_group_29.widthHint = 500;
		group_29.setLayoutData(gd_group_29);
		group_29.setBounds(0, 0, 480, 412);
		group_29.setText(l.getString("userIDext"));

		GridLayout gl_group_29 = new GridLayout(2, false);
		gl_group_29.marginTop = 10;
		gl_group_29.marginBottom = 10;
		gl_group_29.marginLeft = 10;
		gl_group_29.marginHeight = 10;
		gl_group_29.horizontalSpacing = 10;
		gl_group_29.verticalSpacing = 10;
		group_29.setLayout(gl_group_29);

		btn_userIdExM = new Button(group_29, SWT.CHECK);
		GridData gd_btn_userIdExM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_userIdExM.heightHint = 33;
		gd_btn_userIdExM.widthHint = 124;
		btn_userIdExM.setLayoutData(gd_btn_userIdExM);
		btn_userIdExM.setText(l.getString("critical"));
		new Label(group_29, SWT.NONE);

		Label label_291 = new Label(group_29, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_291 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_label_291.widthHint = 480;
		label_291.setLayoutData(gd_label_291);
		new Label(group_29, SWT.NONE);
		new Label(group_29, SWT.NONE);

		btn_customOID = new Button(group_29, SWT.CHECK);
		GridData gd_btn_diyOID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_diyOID.heightHint = 40;
		gd_btn_diyOID.widthHint = 191;
		btn_customOID.setLayoutData(gd_btn_diyOID);
		btn_customOID.setText(l.getString("customOID"));
		btn_customOID.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btn_customOID.getSelection()) {
					text_customOID.setEnabled(true);
				} else {
					text_customOID.setText("");
					text_customOID.setEnabled(false);
				}
			}
		});

		text_customOID = new Text(group_29, SWT.BORDER);
		GridData gd_text_diyOID = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_diyOID.widthHint = 218;
		text_customOID.setLayoutData(gd_text_diyOID);
		text_customOID.setEnabled(false);
		text_customOID.setTextLimit(50);

	}

	// 个人和企业信息
	private void panel_perAndCompany(Composite composite) {
		Group group_210 = new Group(composite, SWT.NONE);
		GridData gd_group_210 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_210.heightHint = 493;
		gd_group_210.widthHint = 500;
		group_210.setLayoutData(gd_group_210);
		group_210.setText(l.getString("personalAndCompany"));

		GridLayout gl_group_210 = new GridLayout(1, false);
		gl_group_210.marginTop = 10;
		gl_group_210.marginBottom = 10;
		gl_group_210.marginLeft = 10;
		gl_group_210.marginHeight = 10;
		gl_group_210.horizontalSpacing = 10;

		group_210.setLayout(gl_group_210);

		Group group_2101 = new Group(group_210, SWT.NONE);
		GridData gd_group_2101 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2101.heightHint = 103;
		gd_group_2101.widthHint = 475;
		group_2101.setLayoutData(gd_group_2101);
		group_2101.setText(l.getString("personalInfo"));
		group_2101.setLayout(new GridLayout(2, false));

		btn_identityCode = new Button(group_2101, SWT.CHECK);
		GridData gd_btn_identityCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_identityCode.heightHint = 46;
		btn_identityCode.setLayoutData(gd_btn_identityCode);
		btn_identityCode.setText(l.getString("identifyCode"));
		btn_identityCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_identityCode.getSelection();
				if (!flag) {
					btn_identityCodeMust.setSelection(false);
				}
				btn_identityCodeMust.setEnabled(flag);
			}
		});
		btn_identityCodeMust = new Button(group_2101, SWT.CHECK);
		GridData gd_btn_identityCodeMust = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_identityCodeMust.widthHint = 112;
		btn_identityCodeMust.setLayoutData(gd_btn_identityCodeMust);
		btn_identityCodeMust.setText(l.getString("writeMust"));
		btn_identityCodeMust.setEnabled(false);

		btn_socialInsuranceCode = new Button(group_2101, SWT.CHECK);
		GridData gd_btn_socialInsuranceCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_socialInsuranceCode.widthHint = 309;
		gd_btn_socialInsuranceCode.heightHint = 37;
		btn_socialInsuranceCode.setLayoutData(gd_btn_socialInsuranceCode);
		btn_socialInsuranceCode.setText(l.getString("socialInsuranceCode"));
		btn_socialInsuranceCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_socialInsuranceCode.getSelection();
				if (!flag) {
					btn_socialInsuranceCodeM.setSelection(false);
				}
				btn_socialInsuranceCodeM.setEnabled(flag);
			}
		});
		btn_socialInsuranceCodeM = new Button(group_2101, SWT.CHECK);
		GridData gd_btn_socialInsuranceCodeM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_socialInsuranceCodeM.widthHint = 117;
		btn_socialInsuranceCodeM.setLayoutData(gd_btn_socialInsuranceCodeM);
		btn_socialInsuranceCodeM.setText(l.getString("writeMust"));
		btn_socialInsuranceCodeM.setEnabled(false);
		new Label(group_210, SWT.NONE);

		Group group_2102 = new Group(group_210, SWT.NONE);
		GridData gd_group_2102 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2102.heightHint = 148;
		gd_group_2102.widthHint = 473;
		group_2102.setLayoutData(gd_group_2102);
		group_2102.setText(l.getString("companyInfo"));
		group_2102.setLayout(new GridLayout(2, false));

		btn_companyRegistCode = new Button(group_2102, SWT.CHECK);
		GridData gd_btn_companyRegistCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_companyRegistCode.widthHint = 306;
		gd_btn_companyRegistCode.heightHint = 40;
		btn_companyRegistCode.setLayoutData(gd_btn_companyRegistCode);
		btn_companyRegistCode.setText(l.getString("companyRegistCode"));
		btn_companyRegistCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_companyRegistCode.getSelection();
				if (!flag) {
					btn_companyRegistCodeM.setSelection(false);
				}
				btn_companyRegistCodeM.setEnabled(flag);
			}
		});
		btn_companyRegistCodeM = new Button(group_2102, SWT.CHECK);
		GridData gd_btn_companyRegistCodeM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_companyRegistCodeM.widthHint = 92;
		btn_companyRegistCodeM.setLayoutData(gd_btn_companyRegistCodeM);
		btn_companyRegistCodeM.setText(l.getString("writeMust"));
		btn_companyRegistCodeM.setEnabled(false);

		btn_companyOrganCode = new Button(group_2102, SWT.CHECK);
		GridData gd_btn_companyOrganCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_companyOrganCode.widthHint = 314;
		gd_btn_companyOrganCode.heightHint = 39;
		btn_companyOrganCode.setLayoutData(gd_btn_companyOrganCode);
		btn_companyOrganCode.setText(l.getString("companyOrganCode"));
		btn_companyOrganCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_companyOrganCode.getSelection();
				if (!flag) {
					btn_companyOrganCodeM.setSelection(false);
				}
				btn_companyOrganCodeM.setEnabled(flag);
			}
		});
		btn_companyOrganCodeM = new Button(group_2102, SWT.CHECK);
		btn_companyOrganCodeM.setText(l.getString("writeMust"));
		btn_companyOrganCodeM.setEnabled(false);

		btn_companyTaxCode = new Button(group_2102, SWT.CHECK);
		GridData gd_btn_companyTaxCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_companyTaxCode.heightHint = 36;
		btn_companyTaxCode.setLayoutData(gd_btn_companyTaxCode);
		btn_companyTaxCode.setText(l.getString("companyTaxCode"));
		btn_companyTaxCode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_companyTaxCode.getSelection();
				if (!flag) {
					btn_companyTaxCodeM.setSelection(false);
				}
				btn_companyTaxCodeM.setEnabled(flag);
			}
		});
		btn_companyTaxCodeM = new Button(group_2102, SWT.CHECK);
		btn_companyTaxCodeM.setText(l.getString("writeMust"));
		btn_companyTaxCodeM.setEnabled(false);

	}

	// 其他扩展
	private void panel_others(Composite composite) {
		Group group_2110 = new Group(composite, SWT.NONE);
		GridData gd_group_2110 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2110.widthHint = 500;
		gd_group_2110.heightHint = 493;
		group_2110.setLayoutData(gd_group_2110);
		group_2110.setText(l.getString("othersExt"));
		group_2110.setLayout(new GridLayout(1, false));

		Group group_2111 = new Group(group_2110, SWT.NONE);
		GridData gd_group_2111 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_2111.heightHint = 88;
		gd_group_2111.widthHint = 484;
		group_2111.setLayoutData(gd_group_2111);
		group_2111.setLayout(new GridLayout(2, false));

		btn_subKeyIdentity = new Button(group_2111, SWT.CHECK);
		GridData gd_btn_subKeyIdentity = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_subKeyIdentity.widthHint = 263;
		gd_btn_subKeyIdentity.heightHint = 34;
		btn_subKeyIdentity.setLayoutData(gd_btn_subKeyIdentity);
		btn_subKeyIdentity.setText(l.getString("subKeyIdentity"));
		btn_subKeyIdentity.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_subKeyIdentity.getSelection();
				btn_subKeyIdentityM.setEnabled(flag);
			}
		});
		btn_subKeyIdentityM = new Button(group_2111, SWT.CHECK);
		GridData gd_btn_subKeyIdentityM = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_subKeyIdentityM.widthHint = 120;
		btn_subKeyIdentityM.setLayoutData(gd_btn_subKeyIdentityM);
		btn_subKeyIdentityM.setText(l.getString("critical"));

		btn_pubKeyIdentity = new Button(group_2111, SWT.CHECK);
		GridData gd_btn_pubKeyIdentity = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_pubKeyIdentity.widthHint = 288;
		gd_btn_pubKeyIdentity.heightHint = 32;
		btn_pubKeyIdentity.setLayoutData(gd_btn_pubKeyIdentity);
		btn_pubKeyIdentity.setText(l.getString("btn_pubKeyIdentity"));
		btn_pubKeyIdentity.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_pubKeyIdentity.getSelection();
				btn_pubKeyIdentityM.setEnabled(flag);
			}
		});
		btn_pubKeyIdentityM = new Button(group_2111, SWT.CHECK);
		btn_pubKeyIdentityM.setText(l.getString("critical"));

		btn_allowAppend = new Button(group_2110, SWT.CHECK);
		GridData gd_btn_allowAppend = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_allowAppend.widthHint = 366;
		gd_btn_allowAppend.heightHint = 46;
		btn_allowAppend.setLayoutData(gd_btn_allowAppend);
		btn_allowAppend.setText(l.getString("allowAppend"));
	}

	// 自定义扩展
	private void panel_diyExtend(Composite composite) {
		Group group_31 = new Group(composite, SWT.NONE);
		GridData gd_group_31 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_31.widthHint = 500;
		gd_group_31.heightHint = 493;
		group_31.setLayoutData(gd_group_31);

		group_31.setText(l.getString("customExt"));
		GridLayout gl_group_31 = new GridLayout(4, false);
		gl_group_31.horizontalSpacing = 15;
		gl_group_31.verticalSpacing = 20;

		group_31.setLayout(gl_group_31);

		table_diyExtend = new Table(group_31, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table_diyExtend = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_table_diyExtend.heightHint = 303;
		gd_table_diyExtend.widthHint = 460;
		gd_table_diyExtend.horizontalSpan = 4;
		table_diyExtend.setLayoutData(gd_table_diyExtend);

		table_diyExtend.setHeaderVisible(true);
		table_diyExtend.setLinesVisible(true);

		TableColumn tblclmnNewColumn_30 = new TableColumn(table_diyExtend, SWT.NONE);
		tblclmnNewColumn_30.setWidth(94);
		tblclmnNewColumn_30.setText(l.getString("extName"));

		TableColumn tblclmnNewColumn_31 = new TableColumn(table_diyExtend, SWT.NONE);
		tblclmnNewColumn_31.setWidth(56);
		tblclmnNewColumn_31.setText("OID");

		TableColumn tblclmnNewColumn_32 = new TableColumn(table_diyExtend, SWT.NONE);
		tblclmnNewColumn_32.setWidth(58);
		tblclmnNewColumn_32.setText(l.getString("encodeMethod"));

		TableColumn tblclmnNewColumn_33 = new TableColumn(table_diyExtend, SWT.NONE);
		tblclmnNewColumn_33.setWidth(57);
		tblclmnNewColumn_33.setText(l.getString("critical"));

		TableColumn tblclmnNewColumn_4 = new TableColumn(table_diyExtend, SWT.NONE);
		tblclmnNewColumn_4.setWidth(65);
		tblclmnNewColumn_4.setText(l.getString("extValSource"));

		TableColumn tblclmnNewColumn_5 = new TableColumn(table_diyExtend, SWT.NONE);
		tblclmnNewColumn_5.setWidth(49);
		tblclmnNewColumn_5.setText(l.getString("writeMust"));

		TableColumn tblclmnNewColumn_6 = new TableColumn(table_diyExtend, SWT.NONE);
		tblclmnNewColumn_6.setWidth(82);
		tblclmnNewColumn_6.setText(l.getString("extValue"));
		new Label(group_31, SWT.NONE);

		Button btn_add = new Button(group_31, SWT.NONE);
		GridData gd_btnNewButton_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_2.widthHint = 72;
		btn_add.setLayoutData(gd_btnNewButton_2);
		btn_add.setText(l.getString("add"));
		btn_add.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				Add_CustomExtend add_DiyExtend = new Add_CustomExtend(table_diyExtend, true);
				add_DiyExtend.setBlockOnOpen(true);
				add_DiyExtend.open();
			}
		});
		Button btn_delete = new Button(group_31, SWT.NONE);
		GridData gd_btnNewButton_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_3.widthHint = 71;
		btn_delete.setLayoutData(gd_btnNewButton_3);
		btn_delete.setText(l.getString("delete"));
		btn_delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table_diyExtend.getSelection();
				if (items.length == 0) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.NONE);
					messageBox.setMessage(l.getString("Notice_not_chooseCustomExt"));
					messageBox.open();
					return;
				}
				table_diyExtend.remove(table_diyExtend.getSelectionIndex());
			}
		});
		Button btn_modify = new Button(group_31, SWT.NONE);
		GridData gd_btnNewButton_4 = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_4.widthHint = 71;
		btn_modify.setLayoutData(gd_btnNewButton_4);
		btn_modify.setText(l.getString("modify"));
		btn_modify.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table_diyExtend.getSelection();
				if (items.length == 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_not_chooseCustomExt"));
					mb.open();
					return;
				}
				Add_CustomExtend add_DiyExtend = new Add_CustomExtend(table_diyExtend, false);
				add_DiyExtend.setBlockOnOpen(true);
				add_DiyExtend.open();
			}
		});

		Label lblNewLabel_2 = new Label(group_31, SWT.NONE);
		GridData gd_lblNewLabel_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel_2.widthHint = 227;
		lblNewLabel_2.setLayoutData(gd_lblNewLabel_2);
		lblNewLabel_2.setText("");
		new Label(group_31, SWT.NONE);
		new Label(group_31, SWT.NONE);
		new Label(group_31, SWT.NONE);
	}

	// 证书相关策略
	private void panel_certPolicy_1(Composite composite) {
		Group group_6 = new Group(composite, SWT.NONE);
		GridData gd_group_6 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_6.heightHint = 493;
		gd_group_6.widthHint = 500;
		group_6.setLayoutData(gd_group_6);
		group_6.setBounds(0, 0, 480, 412);
		group_6.setText(l.getString("certPolicy_1"));

		GridLayout gl_group_6 = new GridLayout(1, false);
		gl_group_6.marginTop = 10;
		gl_group_6.marginBottom = 10;
		gl_group_6.marginLeft = 10;
		gl_group_6.marginHeight = 10;
		gl_group_6.horizontalSpacing = 10;
		gl_group_6.verticalSpacing = 10;

		group_6.setLayout(gl_group_6);
		new Label(group_6, SWT.NONE);

		btn_allowDNRepeat = new Button(group_6, SWT.CHECK);
		GridData gd_btn_allowDNRepeat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_allowDNRepeat.heightHint = 29;
		btn_allowDNRepeat.setLayoutData(gd_btn_allowDNRepeat);
		btn_allowDNRepeat.setText(l.getString("allowDNRepeat"));

		btn_appleCertChgKey = new Button(group_6, SWT.CHECK);
		GridData gd_btn_appleCertChgKey = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_appleCertChgKey.widthHint = 465;
		gd_btn_appleCertChgKey.heightHint = 30;
		btn_appleCertChgKey.setLayoutData(gd_btn_appleCertChgKey);
		btn_appleCertChgKey.setText(l.getString("appleCertChgKey"));

		btn_useCertDN = new Button(group_6, SWT.CHECK);
		GridData gd_btn_useCertDN = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_useCertDN.heightHint = 32;
		btn_useCertDN.setLayoutData(gd_btn_useCertDN);
		btn_useCertDN.setText(l.getString("useCertDN"));

	}

	// 发布策略
	private void panel_publishPolicy(Composite composite) {
		Group group_7 = new Group(composite, SWT.NONE);
		GridLayout gl_group_7 = new GridLayout(1, false);
		gl_group_7.marginTop = 10;
		gl_group_7.marginBottom = 10;
		gl_group_7.marginLeft = 10;
		gl_group_7.marginHeight = 10;
		gl_group_7.horizontalSpacing = 10;
		gl_group_7.verticalSpacing = 10;

		group_7.setLayout(gl_group_7);
		GridData gd_group_7 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_7.heightHint = 493;
		gd_group_7.widthHint = 500;
		group_7.setLayoutData(gd_group_7);
		group_7.setBounds(0, 0, 480, 412);
		group_7.setText(l.getString("publishPolicy"));
		new Label(group_7, SWT.NONE);

		btn_pubCert = new Button(group_7, SWT.CHECK);
		GridData gd_btn_pubCert = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_pubCert.widthHint = 152;
		gd_btn_pubCert.heightHint = 39;
		btn_pubCert.setLayoutData(gd_btn_pubCert);
		btn_pubCert.setText(l.getString("pubCert"));

		btn_pubCRLList = new Button(group_7, SWT.CHECK);
		GridData gd_btn_pubCRLList = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_pubCRLList.widthHint = 212;
		gd_btn_pubCRLList.heightHint = 39;
		btn_pubCRLList.setLayoutData(gd_btn_pubCRLList);
		btn_pubCRLList.setText(l.getString("pubCRLList"));
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
