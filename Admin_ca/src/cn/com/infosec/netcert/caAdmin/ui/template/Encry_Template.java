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
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
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

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridLayout;

/**
 * 加密模板视图 以及 逻辑
 * @Description  从模板管理页面获取 CertTemplate 对象，并对其赋值
 * @Author 江岩    
 * @Time 2019-06-06 14:20
 * @add 2019/07/24 新增 增量crl页面，OID : 2.4.29.46 ，没有是否关键选项，提交时 设置为 false,其他与crl发布点一致
 */
public class Encry_Template extends ApplicationWindow {

	// 加密模板
	private Composite composite_1_enc, comp_certPolicy_enc, comp_keyUsage_enc, comp_enkeyUsage_enc, comp_CRLPublic_enc,
			comp_incrementalCRL_enc, comp_subAltName_enc;
	private Tree tree_enc;
	private Table table_261_enc, table_261_i_enc;
	private Text text_specifyPolicyOID_enc, text_CPS_enc, text_userNotice_enc, text_oID_enc, text_basicPub_enc,
			text_basicPub_i_enc;
	private Combo combo_CRLType_enc, combo_CRLType_i_enc;
	private Button btn_certPolicyIscritical_enc, btn_noPolicy_enc, btn_anyPolicy_enc, btn_specifyPolicyOID_enc,
			btn_CPS_enc, btn_userNotice_enc, btn_keyUsageIscritical_enc, btn_digitalSign_enc, btn_nonRepudiation_enc,
			btn_keyEn_enc, btn_dataEn_enc, btn_keyAgree_enc, btn_keyCertSign_enc, btn_CRLSign_enc, btn_enci_enc,
			btn_deci_enc, btn_enKeyUsageIscritical_enc, btn_serverAuth_enc, btn_clientAuth_enc, btn_codeSign_enc,
			btn_emailPro_enc, btn_timeStamp_enc, btn_OCSPSign_enc, btn_CRLPubIscritical_enc, btn_subjectAltNameM_enc,
			btn_dNSName_enc, btn_ipAddress_enc, btn_rfc822Name_enc, btn_otherName_enc, btn_add_enc, btn_delete_enc,
			btn_add_261_enc, btn_add_261_i_enc, btn_delete_262_enc, btn_delete_262_i_enc, btn_addDN_enc,
			btn_addDN_i_enc;
	private String OID;
	private org.eclipse.swt.widgets.List list1_enc;
	private Set<String> oIDs_enc = new HashSet<String>();
	private CertTemplate certTemplate_enc;
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * @Desc 构造方法
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:02:39
	 */
	public Encry_Template(CertTemplate certTemplate_enc) {
		super(null);
		this.certTemplate_enc = certTemplate_enc;
	}

	/**
	 * @Desc 视图页面绘画
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:02:39
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
	 * @Desc 加密模板视图 , 并判断是否需要加载模板信息
	 * @When 新增模板选择 双证 或者 修改模板查询存在 加密模板
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午4:11:26
	 */
	protected TabItem createContents(TabFolder tabFolder) {

		TabItem tabItem_enc = new TabItem(tabFolder, SWT.NONE);
		tabItem_enc.setText(l.getString("encTemplate"));
		composite_1_enc = new Composite(tabFolder, SWT.NONE);
		tabItem_enc.setControl(composite_1_enc);
		composite_1_enc.setLayout(new FormLayout());

		// tree 的 位置
		Group group_11_enc = new Group(composite_1_enc, SWT.NONE);
		group_11_enc.setLayoutData(new FormData());
		group_11_enc.setLayout(new GridLayout());

		// 左侧 功能栏
		tree_enc = new Tree(group_11_enc, SWT.NONE);
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
		tItem_2_enc.setExpanded(true);
		// 右侧的视图组件
		comp_certPolicy_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_22_enc = new FormData();
		fd_composite_22_enc.left = new FormAttachment(group_11_enc, 10);
		comp_certPolicy_enc.setLayoutData(fd_composite_22_enc);
		GridLayout gl_composite_22 = new GridLayout(1, false);
		comp_certPolicy_enc.setLayout(gl_composite_22);

		comp_keyUsage_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_23_enc = new FormData();
		fd_composite_23_enc.left = new FormAttachment(group_11_enc, 10);
		comp_keyUsage_enc.setLayoutData(fd_composite_23_enc);
		GridLayout gl_composite_23 = new GridLayout(1, false);
		comp_keyUsage_enc.setLayout(gl_composite_23);

		comp_enkeyUsage_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_24_enc = new FormData();
		fd_composite_24_enc.left = new FormAttachment(group_11_enc, 10);
		comp_enkeyUsage_enc.setLayoutData(fd_composite_24_enc);
		GridLayout gl_composite_24 = new GridLayout(1, false);
		comp_enkeyUsage_enc.setLayout(gl_composite_24);

		comp_CRLPublic_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_26_enc = new FormData();
		fd_composite_26_enc.left = new FormAttachment(group_11_enc, 10);
		comp_CRLPublic_enc.setLayoutData(fd_composite_26_enc);
		GridLayout gl_composite_26 = new GridLayout(1, false);
		comp_CRLPublic_enc.setLayout(gl_composite_26);

		comp_incrementalCRL_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_26_i_enc = new FormData();
		fd_composite_26_i_enc.left = new FormAttachment(group_11_enc, 10);
		comp_incrementalCRL_enc.setLayoutData(fd_composite_26_i_enc);
		GridLayout gl_composite_26_i = new GridLayout(1, false);
		comp_incrementalCRL_enc.setLayout(gl_composite_26_i);

		comp_subAltName_enc = new Composite(composite_1_enc, SWT.NONE);
		FormData fd_composite_27_enc = new FormData();
		fd_composite_27_enc.left = new FormAttachment(group_11_enc, 10);
		comp_subAltName_enc.setLayoutData(fd_composite_27_enc);
		GridLayout gl_composite_27 = new GridLayout(1, false);
		comp_subAltName_enc.setLayout(gl_composite_27);

		// 所有 的 composite面板
		panel_certPolicy_enc(comp_certPolicy_enc);
		panel_keyUsage_enc(comp_keyUsage_enc);
		panel_enkeyUsage_enc(comp_enkeyUsage_enc);
		panel_CRLPublic_enc(comp_CRLPublic_enc);
		panel_incrementalCRL_enc(comp_incrementalCRL_enc);
		panel_subAltName_enc(comp_subAltName_enc);


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
					comp_certPolicy_enc.setVisible(true);
				}
				if (l.getString("keyUsage").equals(tItemSelected)) {
					comp_keyUsage_enc.setVisible(true);
				}
				if (l.getString("en_keyUsage").equals(tItemSelected)) {
					comp_enkeyUsage_enc.setVisible(true);
				}
				if (l.getString("CRLPublishPoint").equals(tItemSelected)) {
					comp_CRLPublic_enc.setVisible(true);
				}
				if (l.getString("incrementalCRL").equals(tItemSelected)) {
					comp_incrementalCRL_enc.setVisible(true);
				}
				if (l.getString("subjectAltName").equals(tItemSelected)) {
					comp_subAltName_enc.setVisible(true);
				}
			}
		});
		if (!certTemplate_enc.isAdd()) { // 查询模板信息时，提前设置了此值
			loadCertTemplateInfo(certTemplate_enc);
		}
		return tabItem_enc;
	}

	/**
	 * @throws Exception 
	 * @Desc 封装加密模板信息
	 * @When 提交信息到 Server 时
	 * @Authod 江岩
	 * @Date 2019年3月18日 上午11:42:12
	 */
	public void packageTempInfo(CertTemplate certTemplate) throws Exception {
		certTemplate.setKeyGeneratPlace("KMC");
		// 标准扩展
		ArrayList<Extension> standardExtensions = certTemplate.getStandardExtensions();
		// 封装 证书策略
		if (btn_specifyPolicyOID_enc.getSelection()) {
			Extension ex_certPolicy = new Extension();
			List<ExtensionEntry> exList_certPolicy = new ArrayList<ExtensionEntry>();
			String oid = text_specifyPolicyOID_enc.getText().trim();
			if (oid == null || oid.length() == 0) {
				throw new Exception(l.getString("Notice_null_oid"));
			}
			Matcher m = Pattern.compile("^[0-9.]+$").matcher(oid);
			if (!m.matches()) {
				throw new Exception(l.getString("Notice_specifyPolicyOID"));
			}
			ExtensionEntry entry_specifyPolicyOID = new ExtensionEntry();
			entry_specifyPolicyOID.setType("POLICYOID");
			entry_specifyPolicyOID.setValue(text_specifyPolicyOID_enc.getText());
			exList_certPolicy.add(entry_specifyPolicyOID);

			if (btn_CPS_enc.getSelection()) {
				String cps = text_CPS_enc.getText().trim();
				if (cps == null || cps.length() == 0) {
					throw new Exception(l.getString("Notice_null_cps"));
				}
				ExtensionEntry entry_CPS = new ExtensionEntry();
				entry_CPS.setType("CPSURI");
				entry_CPS.setValue(text_CPS_enc.getText());
				exList_certPolicy.add(entry_CPS);
			}
			if (btn_userNotice_enc.getSelection()) {
				String userNotice = text_userNotice_enc.getText().trim();
				if (userNotice == null || userNotice.length() == 0) {
					throw new Exception(l.getString("Notice_null_userNotice"));
				}
				ExtensionEntry entry_userNotice = new ExtensionEntry();
				entry_userNotice.setType("USERNOTICE");
				entry_userNotice.setValue(text_userNotice_enc.getText());
				exList_certPolicy.add(entry_userNotice);
			}
			ex_certPolicy.setOID("2.5.29.32");
			ex_certPolicy.setIscritical(btn_certPolicyIscritical_enc.getSelection());
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
		if (btn_digitalSign_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_nonRepudiation_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyEn_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_dataEn_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyAgree_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_keyCertSign_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_CRLSign_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_enci_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		if (btn_deci_enc.getSelection()) {
			s.append("1");
		} else {
			s.append("0");
		}
		entry_keyUsage.setValue(s.toString());
		exList_keyUsage.add(entry_keyUsage);

		ex_keyUsage.setOID("2.5.29.15");
		ex_keyUsage.setIscritical(btn_keyUsageIscritical_enc.getSelection());
		ex_keyUsage.setDatasource("CA");
		ex_keyUsage.setExtensionEntrys(exList_keyUsage);
		standardExtensions.add(ex_keyUsage);

		// 增强密钥用法
		List<ExtensionEntry> exList_enKeyUsage = new ArrayList<ExtensionEntry>();
		if (btn_serverAuth_enc.getSelection()) {
			ExtensionEntry entry_serverAuth = new ExtensionEntry();
			entry_serverAuth.setOid("1.3.6.1.5.5.7.3.1");
			exList_enKeyUsage.add(entry_serverAuth);
		}
		if (btn_clientAuth_enc.getSelection()) {
			ExtensionEntry entry_clientAuth = new ExtensionEntry();
			entry_clientAuth.setOid("1.3.6.1.5.5.7.3.2");
			exList_enKeyUsage.add(entry_clientAuth);
		}
		if (btn_codeSign_enc.getSelection()) {
			ExtensionEntry entry_codeSign = new ExtensionEntry();
			entry_codeSign.setOid("1.3.6.1.5.5.7.3.3");
			exList_enKeyUsage.add(entry_codeSign);
		}
		if (btn_emailPro_enc.getSelection()) {
			ExtensionEntry entry_emailPro = new ExtensionEntry();
			entry_emailPro.setOid("1.3.6.1.5.5.7.3.4");
			exList_enKeyUsage.add(entry_emailPro);
		}
		if (btn_timeStamp_enc.getSelection()) {
			ExtensionEntry entry_timeStamp = new ExtensionEntry();
			entry_timeStamp.setOid("1.3.6.1.5.5.7.3.8");
			exList_enKeyUsage.add(entry_timeStamp);
		}
		if (btn_OCSPSign_enc.getSelection()) {
			ExtensionEntry entry_OCSPSign = new ExtensionEntry();
			entry_OCSPSign.setOid("1.3.6.1.5.5.7.3.9");
			exList_enKeyUsage.add(entry_OCSPSign);
		}
		if (oIDs_enc.size() != 0) {
			for (String ss : oIDs_enc) {
				ExtensionEntry entry_oid = new ExtensionEntry();
				entry_oid.setOid(ss);
				exList_enKeyUsage.add(entry_oid);
			}
		}
		if (exList_enKeyUsage.size() > 0) {
			Extension ex_enKeyUsage = new Extension();
			ex_enKeyUsage.setOID("2.5.29.37");
			ex_enKeyUsage.setIscritical(btn_enKeyUsageIscritical_enc.getSelection());
			ex_enKeyUsage.setDatasource("CA");
			ex_enKeyUsage.setIsmust(false);
			ex_enKeyUsage.setExtensionEntrys(exList_enKeyUsage);
			standardExtensions.add(ex_enKeyUsage);
		}
		// CRL发布点设置
		TableItem[] items = table_261_enc.getItems();
		if (items.length > 0) {
			Extension ex_CRLPub = new Extension();
			List<ExtensionEntry> exList_CRLPub = new ArrayList<ExtensionEntry>();
			for (TableItem item : items) {
				ExtensionEntry entry_CRLPub = new ExtensionEntry();
				entry_CRLPub.setName(item.getText(0));
				if (l.getString("yes").equalsIgnoreCase(item.getText(1))) {
					entry_CRLPub.setAppendbasedn("true");
				} else {
					entry_CRLPub.setAppendbasedn("false");
				}
				entry_CRLPub.setValue(item.getText(2));

				exList_CRLPub.add(entry_CRLPub);
			}
			ex_CRLPub.setOID("2.5.29.31");
			ex_CRLPub.setIscritical(btn_CRLPubIscritical_enc.getSelection());
			ex_CRLPub.setExtensionEntrys(exList_CRLPub);
			standardExtensions.add(ex_CRLPub);
		}
		// 增量 CRL发布点设置
		TableItem[] items_i = table_261_i_enc.getItems();
		if (items.length > 0) {
			Extension ex_CRLPub = new Extension();
			List<ExtensionEntry> exList_CRLPub = new ArrayList<ExtensionEntry>();
			for (TableItem item : items_i) {
				ExtensionEntry entry_CRLPub = new ExtensionEntry();
				entry_CRLPub.setName(item.getText(0));
				if (l.getString("yes").equalsIgnoreCase(item.getText(1))) {
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
		if (btn_dNSName_enc.getSelection()) {
			ExtensionEntry entry_DNSName = new ExtensionEntry();
			entry_DNSName.setName("DNSNAME");
			exList_subjectAltName.add(entry_DNSName);
		}
		if (btn_ipAddress_enc.getSelection()) {
			ExtensionEntry entry_IPAddress = new ExtensionEntry();
			entry_IPAddress.setName("IPADDRESS");
			exList_subjectAltName.add(entry_IPAddress);
		}
		if (btn_rfc822Name_enc.getSelection()) {
			ExtensionEntry entry_rfc822Name = new ExtensionEntry();
			entry_rfc822Name.setName("RFC822NAME");
			exList_subjectAltName.add(entry_rfc822Name);
		}
		if (btn_otherName_enc.getSelection()) {
			ExtensionEntry entry_otherName = new ExtensionEntry();
			entry_otherName.setName("OtherName.principalName");
			exList_subjectAltName.add(entry_otherName);
		}
		if (exList_subjectAltName.size() > 0) {
			Extension ex_subjectAltName = new Extension();
			ex_subjectAltName.setOID("2.5.29.17");
			ex_subjectAltName.setIscritical(btn_keyUsageIscritical_enc.getSelection());
			ex_subjectAltName.setDatasource("CA");
			ex_subjectAltName.setIsmust(false);
			ex_subjectAltName.setExtensionEntrys(exList_subjectAltName);
			standardExtensions.add(ex_subjectAltName);
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
			OID = stand.getOID();
			if ("2.5.29.32".equalsIgnoreCase(OID)) { // 证书策略 
				btn_certPolicyIscritical_enc.setSelection(stand.isIscritical());
				btn_specifyPolicyOID_enc.setSelection(true);
				specifyPolicyOIDSelection_enc(true);
				List<ExtensionEntry> exList2 = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exList2) {
					if ("policyOid".equalsIgnoreCase(ex.getType())) {
						btn_noPolicy_enc.setSelection(false);
						btn_anyPolicy_enc.setSelection(false);
						text_specifyPolicyOID_enc.setText(ex.getValue());
					}
					if ("CPSURI".equalsIgnoreCase(ex.getType())) {
						btn_CPS_enc.setSelection(true);
						text_CPS_enc.setText(ex.getValue());
					}
					if ("userNotice".equalsIgnoreCase(ex.getType())) {
						btn_userNotice_enc.setSelection(true);
						text_userNotice_enc.setText(ex.getValue());
					}
				}
			} else if ("2.5.29.15".equalsIgnoreCase(OID)) { // 密钥用法
				btn_keyUsageIscritical_enc.setSelection(stand.isIscritical());
				List<ExtensionEntry> exS = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exS) {
					char[] exC = ex.getValue().toCharArray();
					if ('1' == exC[0]) {
						btn_digitalSign_enc.setSelection(true);
					}
					if ('1' == exC[1]) {
						btn_nonRepudiation_enc.setSelection(true);
					}
					if ('1' == exC[2]) {
						btn_keyEn_enc.setSelection(true);
					}
					if ('1' == exC[3]) {
						btn_dataEn_enc.setSelection(true);
					}
					if ('1' == exC[4]) {
						btn_keyAgree_enc.setSelection(true);
					}
					if ('1' == exC[5]) {
						btn_keyCertSign_enc.setSelection(true);
					}
					if ('1' == exC[6]) {
						btn_CRLSign_enc.setSelection(true);
					}
					if ('1' == exC[7]) {
						btn_enci_enc.setSelection(true);
					}
					if ('1' == exC[8]) {
						btn_deci_enc.setSelection(true);
					}
				}
			} else if ("2.5.29.37".equalsIgnoreCase(OID)) { // 增强密钥用法
				btn_enKeyUsageIscritical_enc.setSelection(stand.isIscritical());
				List<ExtensionEntry> exS_2 = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exS_2) {

					if ("1.3.6.1.5.5.7.3.1".equalsIgnoreCase(ex.getOid())) {
						btn_serverAuth_enc.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.2".equalsIgnoreCase(ex.getOid())) {
						btn_clientAuth_enc.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.3".equalsIgnoreCase(ex.getOid())) {
						btn_codeSign_enc.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.4".equalsIgnoreCase(ex.getOid())) {
						btn_emailPro_enc.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.8".equalsIgnoreCase(ex.getOid())) {
						btn_timeStamp_enc.setSelection(true);
					} else if ("1.3.6.1.5.5.7.3.9".equalsIgnoreCase(ex.getOid())) {
						btn_OCSPSign_enc.setSelection(true);
					} else {
						oIDs_enc.add(ex.getOid());
						list1_enc.add(ex.getOid());
					}
				}
			} else if ("2.5.29.31".equalsIgnoreCase(OID)) { // CRL发布点设置
				btn_CRLPubIscritical_enc.setSelection(stand.isIscritical());
				List<ExtensionEntry> exList = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exList) {
					TableItem item = new TableItem(table_261_enc, SWT.NONE);
					String isAddDN = "";
					if ("true".equalsIgnoreCase(ex.getAppendbasedn())) {
						isAddDN = l.getString("true");
					} else {
						isAddDN = l.getString("false");
					}
					item.setText(new String[] { ex.getName(), isAddDN, ex.getValue() });
				}
			} else if ("2.5.29.46".equalsIgnoreCase(OID)) { // 增量CRL发布点设置
				List<ExtensionEntry> exList_i = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exList_i) {
					TableItem item = new TableItem(table_261_i_enc, SWT.NONE);
					String isAddDN = "";
					if ("true".equalsIgnoreCase(ex.getAppendbasedn())) {
						isAddDN = l.getString("true");
					} else {
						isAddDN = l.getString("false");
					}
					item.setText(new String[] { ex.getName(), isAddDN, ex.getValue() });
				}
			} else if ("2.5.29.17".equalsIgnoreCase(OID)) { // 主题备用名
				btn_subjectAltNameM_enc.setSelection(stand.isIscritical());
				List<ExtensionEntry> exS_3 = stand.getExtensionEntrys();
				for (ExtensionEntry ex : exS_3) {
					if ("DNSName".equalsIgnoreCase(ex.getName())) {
						btn_dNSName_enc.setSelection(true);
					}
					if ("ipAddress".equalsIgnoreCase(ex.getName())) {
						btn_ipAddress_enc.setSelection(true);
					}
					if ("rfc822Name".equalsIgnoreCase(ex.getName())) {
						btn_rfc822Name_enc.setSelection(true);
					}
					if ("otherName.principalName".equalsIgnoreCase(ex.getName())) {
						btn_otherName_enc.setSelection(true);
					}
				}
			}
		}
	}

	// 证书策略
	private void panel_certPolicy_enc(Composite composite_22_enc) {
		Group group_22 = new Group(composite_22_enc, SWT.NONE);
		GridData gd_group_22 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_22.widthHint = 500;
		gd_group_22.heightHint = 493;
		group_22.setLayoutData(gd_group_22);
		group_22.setText(l.getString("certPolicy"));

		GridLayout gl_group_22 = new GridLayout(1, false);
		gl_group_22.marginTop = 10;
		gl_group_22.marginBottom = 10;
		gl_group_22.marginLeft = 10;
		gl_group_22.marginRight = 10;
		gl_group_22.horizontalSpacing = 50;
		gl_group_22.verticalSpacing = 10;
		group_22.setLayout(gl_group_22);

		btn_certPolicyIscritical_enc = new Button(group_22, SWT.CHECK);
		GridData gd_btn_certPolicyIscritical_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_certPolicyIscritical_enc.widthHint = 115;
		gd_btn_certPolicyIscritical_enc.heightHint = 27;
		btn_certPolicyIscritical_enc.setLayoutData(gd_btn_certPolicyIscritical_enc);
		btn_certPolicyIscritical_enc.setText(l.getString("critical"));

		Label label_6 = new Label(group_22, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_6 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_6.widthHint = 480;
		label_6.setLayoutData(gd_label_6);

		Group group_221 = new Group(group_22, SWT.NONE);
		GridData gd_group_221 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_221.heightHint = 127;
		gd_group_221.widthHint = 470;
		group_221.setLayoutData(gd_group_221);

		GridLayout gl_group_221 = new GridLayout(2, false);
		gl_group_221.marginTop = 10;
		gl_group_221.marginBottom = 10;
		gl_group_221.marginLeft = 10;
		gl_group_221.marginRight = 10;
		gl_group_221.horizontalSpacing = 30;
		gl_group_221.verticalSpacing = 10;
		group_221.setLayout(gl_group_221);

		btn_noPolicy_enc = new Button(group_221, SWT.RADIO);
		GridData gd_btn_noPolicy_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_noPolicy_enc.heightHint = 24;
		btn_noPolicy_enc.setLayoutData(gd_btn_noPolicy_enc);
		btn_noPolicy_enc.setText(l.getString("none"));
		btn_noPolicy_enc.setSelection(true);
		btn_noPolicy_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				policyOID_false();
			}
		});
		new Label(group_221, SWT.NONE);

		btn_anyPolicy_enc = new Button(group_221, SWT.RADIO);
		GridData gd_btn_anyPolicy_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_anyPolicy_enc.heightHint = 23;
		btn_anyPolicy_enc.setLayoutData(gd_btn_anyPolicy_enc);
		btn_anyPolicy_enc.setText(l.getString("anyPolicy"));
		btn_anyPolicy_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				policyOID_false();
			}
		});
		new Label(group_221, SWT.NONE);

		btn_specifyPolicyOID_enc = new Button(group_221, SWT.RADIO);
		GridData gd_btn_specifyPolicyOID_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_specifyPolicyOID_enc.heightHint = 25;
		gd_btn_specifyPolicyOID_enc.widthHint = 185;
		btn_specifyPolicyOID_enc.setLayoutData(gd_btn_specifyPolicyOID_enc);
		btn_specifyPolicyOID_enc.setText(l.getString("specifyOID"));
		btn_specifyPolicyOID_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_specifyPolicyOID_enc.getSelection();
				specifyPolicyOIDSelection_enc(flag);
			}
		});

		text_specifyPolicyOID_enc = new Text(group_221, SWT.BORDER);
		GridData gd_text_specifyPolicyOID_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_specifyPolicyOID_enc.widthHint = 196;
		text_specifyPolicyOID_enc.setLayoutData(gd_text_specifyPolicyOID_enc);
		text_specifyPolicyOID_enc.setEnabled(false);
		text_specifyPolicyOID_enc.setTextLimit(50);

		Group group_222 = new Group(group_22, SWT.NONE);
		group_222.setLayout(new GridLayout(2, false));
		GridData gd_group_222 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_222.heightHint = 105;
		gd_group_222.widthHint = 468;
		group_222.setLayoutData(gd_group_222);

		btn_CPS_enc = new Button(group_222, SWT.CHECK);
		GridData gd_btn_CPS_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_CPS_enc.heightHint = 40;
		btn_CPS_enc.setLayoutData(gd_btn_CPS_enc);
		btn_CPS_enc.setText("CPS");
		btn_CPS_enc.setEnabled(false);
		btn_CPS_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_CPS_enc.getSelection();
				text_CPS_enc.setEnabled(flag);
			}
		});

		text_CPS_enc = new Text(group_222, SWT.BORDER);
		GridData gd_text_CPS_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_CPS_enc.widthHint = 195;
		text_CPS_enc.setLayoutData(gd_text_CPS_enc);
		text_CPS_enc.setTextLimit(120);
		text_CPS_enc.setEnabled(false);

		btn_userNotice_enc = new Button(group_222, SWT.CHECK);
		GridData gd_btn_userNotice_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_userNotice_enc.heightHint = 43;
		gd_btn_userNotice_enc.widthHint = 224;
		btn_userNotice_enc.setLayoutData(gd_btn_userNotice_enc);
		btn_userNotice_enc.setText(l.getString("userNotice"));
		btn_userNotice_enc.setEnabled(false);
		btn_userNotice_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean flag = btn_userNotice_enc.getSelection();
				text_userNotice_enc.setEnabled(flag);
			}
		});

		text_userNotice_enc = new Text(group_222, SWT.BORDER);
		GridData gd_text_userNotice_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_userNotice_enc.widthHint = 195;
		text_userNotice_enc.setLayoutData(gd_text_userNotice_enc);
		text_userNotice_enc.setEnabled(false);
		text_userNotice_enc.setTextLimit(120);
	}
	
	// 选中证书策略，使其他设置可选
	private void specifyPolicyOIDSelection_enc(boolean flag) {
		text_specifyPolicyOID_enc.setEnabled(flag);
		btn_CPS_enc.setEnabled(flag);
		btn_userNotice_enc.setEnabled(flag);
	}

	// 取消所有的证书策略
	private void policyOID_false() {
		text_specifyPolicyOID_enc.setText("");
		text_specifyPolicyOID_enc.setEnabled(false);
		btn_CPS_enc.setSelection(false);
		btn_userNotice_enc.setSelection(false);
		btn_CPS_enc.setEnabled(false);
		btn_userNotice_enc.setEnabled(false);
		text_CPS_enc.setText("");
		text_CPS_enc.setEnabled(false);
		text_userNotice_enc.setText("");
		text_userNotice_enc.setEnabled(false);
	}

	// 密钥用法
	private void panel_keyUsage_enc(Composite composite_23_enc) {
		Group group_23 = new Group(composite_23_enc, SWT.NONE);

		GridLayout gl_group_23 = new GridLayout(2, false);
		gl_group_23.marginTop = 10;
		gl_group_23.marginBottom = 10;
		gl_group_23.marginLeft = 10;
		gl_group_23.marginRight = 10;
		gl_group_23.horizontalSpacing = 50;
		gl_group_23.verticalSpacing = 15;
		group_23.setLayout(gl_group_23);

		GridData gd_group_23 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_23.heightHint = 493;
		gd_group_23.widthHint = 500;
		group_23.setLayoutData(gd_group_23);

		group_23.setText(l.getString("keyUsage"));

		btn_keyUsageIscritical_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyUsageIscritical_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyUsageIscritical_enc.heightHint = 25;
		gd_btn_keyUsageIscritical_enc.widthHint = 123;
		btn_keyUsageIscritical_enc.setLayoutData(gd_btn_keyUsageIscritical_enc);
		btn_keyUsageIscritical_enc.setText(l.getString("critical"));
		new Label(group_23, SWT.NONE);

		Label label_1 = new Label(group_23, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_label_1.widthHint = 480;
		label_1.setLayoutData(gd_label_1);

		btn_digitalSign_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_digitalSign_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_digitalSign_enc.heightHint = 40;
		btn_digitalSign_enc.setLayoutData(gd_btn_digitalSign_enc);
		btn_digitalSign_enc.setText("digitalSignature");
		btn_digitalSign_enc.setEnabled(false);

		btn_keyCertSign_enc = new Button(group_23, SWT.CHECK);
		btn_keyCertSign_enc.setText("keyCertSign");
		btn_keyCertSign_enc.setEnabled(false);

		btn_nonRepudiation_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_nonRepudiation_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_nonRepudiation_enc.heightHint = 37;
		btn_nonRepudiation_enc.setLayoutData(gd_btn_nonRepudiation_enc);
		btn_nonRepudiation_enc.setText("nonRepudiation");
		btn_nonRepudiation_enc.setEnabled(false);

		btn_CRLSign_enc = new Button(group_23, SWT.CHECK);
		btn_CRLSign_enc.setText("CRLSign");
		btn_CRLSign_enc.setEnabled(false);

		btn_keyEn_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyEn_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyEn_enc.heightHint = 35;
		btn_keyEn_enc.setLayoutData(gd_btn_keyEn_enc);
		btn_keyEn_enc.setText("keyEncipherment");

		btn_enci_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_enci_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_enci_enc.widthHint = 159;
		btn_enci_enc.setLayoutData(gd_btn_enci_enc);
		btn_enci_enc.setText("encipherOnly");

		btn_dataEn_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_dataEn_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_dataEn_enc.heightHint = 34;
		gd_btn_dataEn_enc.widthHint = 244;
		btn_dataEn_enc.setLayoutData(gd_btn_dataEn_enc);
		btn_dataEn_enc.setText("dataEncipherment");

		btn_deci_enc = new Button(group_23, SWT.CHECK);
		btn_deci_enc.setText("decipherOnly");

		btn_keyAgree_enc = new Button(group_23, SWT.CHECK);
		GridData gd_btn_keyAgree_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_keyAgree_enc.heightHint = 47;
		btn_keyAgree_enc.setLayoutData(gd_btn_keyAgree_enc);
		btn_keyAgree_enc.setText("keyAgreement");
		new Label(group_23, SWT.NONE);
	}

	// 增强密钥算法
	private void panel_enkeyUsage_enc(Composite composite_24_enc) {
		Group group_24 = new Group(composite_24_enc, SWT.NONE);
		GridData gd_group_24 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_24.heightHint = 493;
		gd_group_24.widthHint = 500;
		group_24.setLayoutData(gd_group_24);
		group_24.setBounds(0, 0, 480, 412);
		group_24.setText(l.getString("en_keyUsage"));
		GridLayout gl_group_24 = new GridLayout(1, false);
		gl_group_24.marginTop = 10;
		gl_group_24.marginBottom = 10;
		gl_group_24.marginLeft = 10;
		gl_group_24.marginRight = 10;
		gl_group_24.horizontalSpacing = 50;
		gl_group_24.verticalSpacing = 10;

		group_24.setLayout(gl_group_24);

		btn_enKeyUsageIscritical_enc = new Button(group_24, SWT.CHECK);
		GridData gd_btn_enKeyUsageIscritical_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_enKeyUsageIscritical_enc.heightHint = 24;
		gd_btn_enKeyUsageIscritical_enc.widthHint = 143;
		btn_enKeyUsageIscritical_enc.setLayoutData(gd_btn_enKeyUsageIscritical_enc);
		btn_enKeyUsageIscritical_enc.setText(l.getString("critical"));

		Label label_2 = new Label(group_24, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_2.widthHint = 480;
		label_2.setLayoutData(gd_label_2);

		Group group_241 = new Group(group_24, SWT.NONE);
		GridData gd_group_241 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_241.widthHint = 472;
		gd_group_241.heightHint = 182;
		group_241.setLayoutData(gd_group_241);
		group_241.setText(l.getString("standardExtKeyUsage"));
		group_241.setLayout(new GridLayout(2, false));

		btn_serverAuth_enc = new Button(group_241, SWT.CHECK);
		GridData gd_btn_serverAuth_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_serverAuth_enc.heightHint = 44;
		btn_serverAuth_enc.setLayoutData(gd_btn_serverAuth_enc);
		btn_serverAuth_enc.setText("serverAuth");

		btn_emailPro_enc = new Button(group_241, SWT.CHECK);
		btn_emailPro_enc.setText("emailProtection");

		btn_clientAuth_enc = new Button(group_241, SWT.CHECK);
		GridData gd_btn_clientAuth_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_clientAuth_enc.heightHint = 48;
		btn_clientAuth_enc.setLayoutData(gd_btn_clientAuth_enc);
		btn_clientAuth_enc.setText("clientAuth");

		btn_timeStamp_enc = new Button(group_241, SWT.CHECK);
		btn_timeStamp_enc.setText("timeStamping");

		btn_codeSign_enc = new Button(group_241, SWT.CHECK);
		GridData gd_btn_codeSign_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_codeSign_enc.widthHint = 269;
		gd_btn_codeSign_enc.heightHint = 40;
		btn_codeSign_enc.setLayoutData(gd_btn_codeSign_enc);
		btn_codeSign_enc.setText("codeSigning");

		btn_OCSPSign_enc = new Button(group_241, SWT.CHECK);
		btn_OCSPSign_enc.setText("OCSPSigning");

		Group group_132 = new Group(group_24, SWT.NONE);
		GridLayout gl_group_132 = new GridLayout(4, false);

		gl_group_132.verticalSpacing = 15;
		group_132.setLayout(gl_group_132);
		GridData gd_group_132 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_132.heightHint = 165;
		gd_group_132.widthHint = 469;
		group_132.setLayoutData(gd_group_132);

		Label lblNewLabel_132 = new Label(group_132, SWT.NONE);
		lblNewLabel_132.setText("OID:");

		text_oID_enc = new Text(group_132, SWT.BORDER);
		GridData gd_text_oID_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_oID_enc.widthHint = 258;
		text_oID_enc.setLayoutData(gd_text_oID_enc);
		text_oID_enc.setTextLimit(50);
		new Label(group_132, SWT.NONE);

		btn_add_enc = new Button(group_132, SWT.NONE);
		GridData gd_btn_add_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add_enc.widthHint = 78;
		btn_add_enc.setLayoutData(gd_btn_add_enc);
		btn_add_enc.setText(l.getString("add"));
		btn_add_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				String oID_enc = text_oID_enc.getText().trim();
				if (oID_enc == null || oID_enc.length() == 0) {
					Panel_MessageDialog dialog = new Panel_MessageDialog("error", l.getString("Notice_null_OID"));
					dialog.setBlockOnOpen(true);
					dialog.open();
					return;
				}
				Matcher m = Pattern.compile("^[0-9.]+$").matcher(oID_enc);
				if (!m.matches()) {
					Panel_MessageDialog dialog = new Panel_MessageDialog("error",
							l.getString("Notice_specifyPolicyOID"));
					dialog.setBlockOnOpen(true);
					dialog.open();
					return;
				}
				if (!oIDs_enc.contains(oID_enc)) {
					oIDs_enc.add(oID_enc);
					list1_enc.add(oID_enc);
				} else {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_exist_OID"));
					messageBox.open();
				}
				text_oID_enc.setText("");
			}
		});
		new Label(group_132, SWT.NONE);

		list1_enc = new org.eclipse.swt.widgets.List(group_132, SWT.BORDER);
		GridData gd_list1_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_list1_enc.widthHint = 260;
		gd_list1_enc.heightHint = 78;
		list1_enc.setLayoutData(gd_list1_enc);
		new Label(group_132, SWT.NONE);

		btn_delete_enc = new Button(group_132, SWT.NONE);
		GridData gd_btn_delete_enc = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btn_delete_enc.widthHint = 76;
		btn_delete_enc.setLayoutData(gd_btn_delete_enc);
		btn_delete_enc.setText(l.getString("delete"));
		btn_delete_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (list1_enc.getSelectionIndex() == -1) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_not_chooseOID"));
					messageBox.open();
					return;
				}
				String oID_enc = list1_enc.getSelection()[0];
				oIDs_enc.remove(oID_enc);
				list1_enc.remove(list1_enc.getSelectionIndex());
			}
		});

	}

	// CRL 发布点设置
	private void panel_CRLPublic_enc(Composite composite_26_enc) {
		Group group_261 = new Group(composite_26_enc, SWT.NONE);
		GridData gd_group_261 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_group_261.widthHint = 500;
		gd_group_261.heightHint = 493;
		group_261.setLayoutData(gd_group_261);
		group_261.setText(l.getString("CRLPublishPoint"));
		GridLayout gl_group_261 = new GridLayout(4, false);
		gl_group_261.marginTop = 10;
		gl_group_261.marginBottom = 10;
		gl_group_261.marginLeft = 10;
		gl_group_261.marginRight = 10;
		gl_group_261.horizontalSpacing = 20;
		gl_group_261.verticalSpacing = 10;
		group_261.setLayout(gl_group_261);

		btn_CRLPubIscritical_enc = new Button(group_261, SWT.CHECK);
		GridData gd_btn_CRLPubIscritical_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btn_CRLPubIscritical_enc.widthHint = 79;
		btn_CRLPubIscritical_enc.setLayoutData(gd_btn_CRLPubIscritical_enc);
		btn_CRLPubIscritical_enc.setText(l.getString("critical"));
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		Label label_3 = new Label(group_261, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_label_3.widthHint = 480;
		label_3.setLayoutData(gd_label_3);

		Label lblNewLabel = new Label(group_261, SWT.NONE);
		lblNewLabel.setText(l.getString("type") + ":");

		combo_CRLType_enc = new Combo(group_261, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_CRLType_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_CRLType_enc.widthHint = 55;
		combo_CRLType_enc.setLayoutData(gd_combo_CRLType_enc);
		combo_CRLType_enc.add("DIR");
		combo_CRLType_enc.add("URI");
		combo_CRLType_enc.setText("DIR");

		btn_addDN_enc = new Button(group_261, SWT.CHECK);
		GridData gd_btn_addDN_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btn_addDN_enc.widthHint = 197;
		btn_addDN_enc.setLayoutData(gd_btn_addDN_enc);
		btn_addDN_enc.setText(l.getString("addSystemDN"));

		Label lblNewLabel_1 = new Label(group_261, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblNewLabel_1.setText(l.getString("basicPublishPoint") + ":");

		text_basicPub_enc = new Text(group_261, SWT.BORDER);
		GridData gd_text_basicPub_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_basicPub_enc.widthHint = 143;
		text_basicPub_enc.setTextLimit(120);
		text_basicPub_enc.setLayoutData(gd_text_basicPub_enc);

		btn_add_261_enc = new Button(group_261, SWT.NONE);
		GridData gd_btn_add_261_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add_261_enc.widthHint = 70;
		btn_add_261_enc.setLayoutData(gd_btn_add_261_enc);
		btn_add_261_enc.setText(l.getString("add"));
		btn_add_261_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String basic_Pub_enc = text_basicPub_enc.getText().trim();
				if (basic_Pub_enc == null || basic_Pub_enc.length() == 0) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_null_basicPublishPoint"));
					messageBox.open();
					return;
				}
				TableItem it = new TableItem(table_261_enc, SWT.NONE);
				String isAppendDN = l.getString("no");
				if (btn_addDN_enc.getSelection()) {
					isAppendDN = l.getString("yes");
				}
				it.setText(new String[] { combo_CRLType_enc.getText(), isAppendDN, text_basicPub_enc.getText(), });
			}
		});
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		table_261_enc = new Table(group_261, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table_261_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_table_261_enc.widthHint = 455;
		gd_table_261_enc.heightHint = 271;
		table_261_enc.setLayoutData(gd_table_261_enc);
		table_261_enc.setHeaderVisible(true);
		table_261_enc.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table_261_enc, SWT.NONE);
		tblclmnNewColumn.setWidth(109);
		tblclmnNewColumn.setText(l.getString("type"));

		TableColumn tblclmnNewColumn_1 = new TableColumn(table_261_enc, SWT.NONE);
		tblclmnNewColumn_1.setWidth(131);
		tblclmnNewColumn_1.setText(l.getString("addSystemDN"));

		TableColumn tblclmnNewColumn_2 = new TableColumn(table_261_enc, SWT.NONE);
		tblclmnNewColumn_2.setWidth(186);
		tblclmnNewColumn_2.setText(l.getString("basicPublishPoint"));
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		btn_delete_262_enc = new Button(group_261, SWT.NONE);
		GridData gd_btn_delete_262_enc = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_delete_262_enc.widthHint = 85;
		btn_delete_262_enc.setLayoutData(gd_btn_delete_262_enc);
		btn_delete_262_enc.setText(l.getString("delete"));
		btn_delete_262_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table_261_enc.getSelection().length == -1) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.NONE);
					messageBox.setMessage(l.getString("Notice_not_chooseCRL"));
					messageBox.open();
					return;
				}
				table_261_enc.remove(table_261_enc.getSelectionIndex());
			}
		});
	}

	// 增量CRL 发布点设置
	private void panel_incrementalCRL_enc(Composite composite_26_i_enc) {
		Group group_261 = new Group(composite_26_i_enc, SWT.NONE);
		GridData gd_group_261 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_group_261.widthHint = 500;
		gd_group_261.heightHint = 493;
		group_261.setLayoutData(gd_group_261);
		group_261.setText(l.getString("incrementalCRLPublishP"));
		GridLayout gl_group_261 = new GridLayout(4, false);
		gl_group_261.marginTop = 10;
		gl_group_261.marginBottom = 10;
		gl_group_261.marginLeft = 10;
		gl_group_261.marginRight = 10;
		gl_group_261.horizontalSpacing = 20;
		gl_group_261.verticalSpacing = 10;
		group_261.setLayout(gl_group_261);

		Label label_3 = new Label(group_261, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_label_3.widthHint = 480;
		label_3.setLayoutData(gd_label_3);

		Label lblNewLabel = new Label(group_261, SWT.NONE);
		lblNewLabel.setText(l.getString("type") + ":");

		combo_CRLType_i_enc = new Combo(group_261, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_CRLType_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_CRLType_enc.widthHint = 55;
		combo_CRLType_i_enc.setLayoutData(gd_combo_CRLType_enc);
		combo_CRLType_i_enc.add("DIR");
		combo_CRLType_i_enc.add("URI");
		combo_CRLType_i_enc.setText("DIR");

		btn_addDN_i_enc = new Button(group_261, SWT.CHECK);
		GridData gd_btn_addDN_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btn_addDN_enc.widthHint = 197;
		btn_addDN_i_enc.setLayoutData(gd_btn_addDN_enc);
		btn_addDN_i_enc.setText(l.getString("addSystemDN"));

		Label lblNewLabel_1 = new Label(group_261, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblNewLabel_1.setText(l.getString("basicPublishPoint") + ":");

		text_basicPub_i_enc = new Text(group_261, SWT.BORDER);
		GridData gd_text_basicPub_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_basicPub_enc.widthHint = 143;
		text_basicPub_i_enc.setLayoutData(gd_text_basicPub_enc);
		text_basicPub_i_enc.setTextLimit(120);

		btn_add_261_i_enc = new Button(group_261, SWT.NONE);
		GridData gd_btn_add_261_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add_261_enc.widthHint = 70;
		btn_add_261_i_enc.setLayoutData(gd_btn_add_261_enc);
		btn_add_261_i_enc.setText(l.getString("add"));
		btn_add_261_i_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String basic_Pub_enc = text_basicPub_i_enc.getText().trim();
				if (basic_Pub_enc == null || basic_Pub_enc.length() == 0) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_null_basicPublishPoint"));
					messageBox.open();
					return;
				}
				TableItem it = new TableItem(table_261_i_enc, SWT.NONE);
				String isAppendDN = l.getString("no");
				if (btn_addDN_i_enc.getSelection()) {
					isAppendDN = l.getString("yes");
				}
				it.setText(new String[] { combo_CRLType_i_enc.getText(), isAppendDN, text_basicPub_i_enc.getText(), });
			}
		});
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		table_261_i_enc = new Table(group_261, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table_261_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_table_261_enc.widthHint = 455;
		gd_table_261_enc.heightHint = 271;
		table_261_i_enc.setLayoutData(gd_table_261_enc);
		table_261_i_enc.setHeaderVisible(true);
		table_261_i_enc.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table_261_i_enc, SWT.NONE);
		tblclmnNewColumn.setWidth(109);
		tblclmnNewColumn.setText(l.getString("type"));

		TableColumn tblclmnNewColumn_1 = new TableColumn(table_261_i_enc, SWT.NONE);
		tblclmnNewColumn_1.setWidth(131);
		tblclmnNewColumn_1.setText(l.getString("addSystemDN"));

		TableColumn tblclmnNewColumn_2 = new TableColumn(table_261_i_enc, SWT.NONE);
		tblclmnNewColumn_2.setWidth(186);
		tblclmnNewColumn_2.setText(l.getString("basicPublishPoint"));
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);
		new Label(group_261, SWT.NONE);

		btn_delete_262_i_enc = new Button(group_261, SWT.NONE);
		GridData gd_btn_delete_262_enc = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btn_delete_262_enc.widthHint = 85;
		btn_delete_262_i_enc.setLayoutData(gd_btn_delete_262_enc);
		btn_delete_262_i_enc.setText(l.getString("delete"));
		btn_delete_262_i_enc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table_261_i_enc.getSelection().length == -1) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.NONE);
					messageBox.setMessage(l.getString("Notice_not_chooseCRL"));
					messageBox.open();
					return;
				}
				table_261_i_enc.remove(table_261_i_enc.getSelectionIndex());
			}
		});
	}

	// 主题备用名称
	private void panel_subAltName_enc(Composite composite_27_enc) {
		Group group_27 = new Group(composite_27_enc, SWT.NONE);
		GridLayout gl_group_27 = new GridLayout(1, false);
		gl_group_27.marginLeft = 10;
		group_27.setLayout(gl_group_27);
		GridData gd_group_27 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_27.heightHint = 493;
		gd_group_27.widthHint = 500;
		group_27.setLayoutData(gd_group_27);
		group_27.setText(l.getString("subjectAltName"));

		btn_subjectAltNameM_enc = new Button(group_27, SWT.CHECK);
		GridData gd_btn_subjectAltNameM_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_subjectAltNameM_enc.heightHint = 31;
		gd_btn_subjectAltNameM_enc.widthHint = 118;
		btn_subjectAltNameM_enc.setLayoutData(gd_btn_subjectAltNameM_enc);
		btn_subjectAltNameM_enc.setText(l.getString("critical"));

		Label label_5 = new Label(group_27, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_5 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_5.widthHint = 480;
		label_5.setLayoutData(gd_label_5);
		new Label(group_27, SWT.NONE);

		Group group_28 = new Group(group_27, SWT.NONE);
		GridData gd_group_28 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group_28.heightHint = 237;
		gd_group_28.widthHint = 472;
		group_28.setLayoutData(gd_group_28);

		GridLayout gl_group_28 = new GridLayout(1, false);
		gl_group_28.marginTop = 10;
		gl_group_28.marginBottom = 10;
		gl_group_28.marginLeft = 10;
		gl_group_28.marginRight = 10;
		gl_group_28.horizontalSpacing = 20;
		gl_group_28.verticalSpacing = 15;
		group_28.setLayout(gl_group_28);
		group_28.setText(l.getString("allowSubjectAltName"));

		btn_dNSName_enc = new Button(group_28, SWT.CHECK);
		GridData gd_btn_dNSName_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_dNSName_enc.heightHint = 39;
		btn_dNSName_enc.setLayoutData(gd_btn_dNSName_enc);
		btn_dNSName_enc.setText(l.getString("DNSName"));

		btn_ipAddress_enc = new Button(group_28, SWT.CHECK);
		GridData gd_btn_ipAddress_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_ipAddress_enc.heightHint = 35;
		btn_ipAddress_enc.setLayoutData(gd_btn_ipAddress_enc);
		btn_ipAddress_enc.setText(l.getString("IPAddress"));

		btn_rfc822Name_enc = new Button(group_28, SWT.CHECK);
		GridData gd_btn_rfc822Name_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_rfc822Name_enc.heightHint = 36;
		btn_rfc822Name_enc.setLayoutData(gd_btn_rfc822Name_enc);
		btn_rfc822Name_enc.setText(l.getString("rfc822Name"));

		btn_otherName_enc = new Button(group_28, SWT.CHECK);
		GridData gd_btn_otherName_enc = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_otherName_enc.widthHint = 175;
		gd_btn_otherName_enc.heightHint = 39;
		btn_otherName_enc.setLayoutData(gd_btn_otherName_enc);
		btn_otherName_enc.setText(l.getString("otherName"));
	}

}
