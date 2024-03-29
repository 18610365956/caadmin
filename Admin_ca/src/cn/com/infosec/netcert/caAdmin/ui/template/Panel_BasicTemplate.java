package cn.com.infosec.netcert.caAdmin.ui.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.com.infosec.netcert.caAdmin.ui.login.Panel_MessageDialog;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplate;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Keypolicy;
import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Env.ALG;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;

/**
 * @Desc 模板基本信息视图
 * @Author Infosec_jy
 * @Date 2019年3月15日 下午5:58:32
 */
public class Panel_BasicTemplate extends ApplicationWindow {

	public Text text_tempName, text_validTime;
	public Combo combo_tempType, combo_unit, combo_genLocation, combo_rsaMinLen, combo_signAlg;
	private Button btn_signalTemp, btn_doubleTemp;
	public String tempName, tempType, validTime, unit, rsaMinLen, signAlg;
	public boolean isSignal, isAdd;
	private CertTemplate certTemplate;
	private Composite composite;
	private String operationType;
	private static ResourceBundle l = Env.getLanguage();
	private FileLogger log = FileLogger.getLogger(this.getClass());
	/**
	 * 新增模板
	 * @wbp.parser.constructor
	 */
	public Panel_BasicTemplate() {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
		isAdd = true;
	}

	/**
	 * 修改模板，加载 模板对象信息到表单中
	 * @param certTemplate
	 */
	public Panel_BasicTemplate(CertTemplate certTemplate, String operationType) {
		super(null);
		isAdd = false;
		this.certTemplate = certTemplate;
		this.operationType = operationType;
	}

	/**
	 * @Desc 基本信息 新增模板时，调用视图
	 * @Authod 江岩
	 * @Date 2019年3月18日 下午3:47:59
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 5;
		formLayout.marginRight = 10;
		container.setLayout(formLayout);
		// Tab
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.left = new FormAttachment(0, 10);
		fd_tabFolder.right = new FormAttachment(100, -5);
		fd_tabFolder.bottom = new FormAttachment(0, 570);
		fd_tabFolder.top = new FormAttachment(0, 5);
		tabFolder.setLayoutData(fd_tabFolder);
		// 基本信息
		TabItem tbtmNewItem_0 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_0.setText(l.getString("basicInfo"));

		composite = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_0.setControl(composite);
		panel_basic(composite); // 内嵌 composite

		Button btn_submit = new Button(container, SWT.NONE);
		FormData fd_btn_submit = new FormData();
		fd_btn_submit.left = new FormAttachment(0, 369);
		fd_btn_submit.top = new FormAttachment(tabFolder, 10);
		btn_submit.setLayoutData(fd_btn_submit);
		btn_submit.setText(l.getString("OK"));
		btn_submit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (text_tempName.getText() == null || text_tempName.getText().length() == 0) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_null_templateName"));
					messageBox.open();
					return;
				}
				if (combo_signAlg.getText() == null || combo_signAlg.getText().length() == 0) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ERROR);
					messageBox.setMessage(l.getString("Notice_null_alg"));
					messageBox.open();
					return;
				}
				isSignal = btn_signalTemp.getSelection();
				tempName = text_tempName.getText();
				tempType = combo_tempType.getText();
				validTime = text_validTime.getText();
				unit = combo_unit.getText();
				rsaMinLen = combo_rsaMinLen.getText();
				signAlg = combo_signAlg.getText();
				
			try {	
				Properties p = new Properties();
				p.setProperty(PropertiesKeysRes.TEMPLATENAME, tempName);
				Response resp = Env.client.sendRequest("QUERYTEMPLATELIST", p);
				Properties[] rows = resp.getItemData();
				if (rows.length > 0) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_exist_template"));
					mb.open();
					return;
				}
			} catch (ServerException se) {
				log.errlog("Operating templates.", se);
				MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
				mb.setMessage(l.getString("Notice_fail_operationTemplate") + ":" + "[" + se.getErrorNum() + "]:"
						+ se.getErrorMsg());
				mb.open();
				return;
			} catch (Exception ee) {
				log.errlog("Add/Modify template fail", ee);
				MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
				mb.setMessage(l.getString("Notice_fail_operationTemplate"));
				mb.open();
				return;
			}
			close();
			}
		});

		Button btn_cancle = new Button(container, SWT.NONE);
		fd_btn_submit.right = new FormAttachment(btn_cancle, -32);
		FormData fd_btn_cancle = new FormData();
		fd_btn_cancle.top = new FormAttachment(btn_submit, 0, SWT.TOP);
		fd_btn_cancle.right = new FormAttachment(100, -23);
		fd_btn_cancle.left = new FormAttachment(0, 482);
		btn_cancle.setLayoutData(fd_btn_cancle);
		btn_cancle.setText(l.getString("cancle"));
		btn_cancle.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleShellCloseEvent();
			}
		});
		return container;
	}

	/**
	 * @Desc 修改模板时，嵌入页面
	 * @Authod 江岩
	 * @Date 2019年3月18日 下午3:47:59
	 */
	protected TabItem createBasicPanel(TabFolder tabFolder) {
		// 基本信息
		TabItem tabItem_basicInfo = new TabItem(tabFolder, SWT.NONE);
		tabItem_basicInfo.setText(l.getString("basicInfo"));
		Composite composite_0 = new Composite(tabFolder, SWT.NONE);
		tabItem_basicInfo.setControl(composite_0);

		panel_basic(composite_0);
		loadCertTemplate(); // 加载 信息到 视图表单中
		return tabItem_basicInfo;
	}

	/**
	 * @Desc 修改 和 克隆模板时，加载 模板的信息并显示在表单中,
	 * @Authod 江岩
	 * @Date 2019年3月18日 下午3:46:56
	 */
	private void loadCertTemplate() {
		if (certTemplate.isSignal()) {
			btn_signalTemp.setSelection(true);
		} else {
			btn_signalTemp.setSelection(false);
			btn_doubleTemp.setSelection(true);
		}
		text_tempName.setText(certTemplate.getCerttemplateName());
		combo_tempType.setText(certTemplate.getSpecialType());
		text_validTime.setText(String.valueOf(certTemplate.getValiditylimit()));
		if ("y".equalsIgnoreCase(certTemplate.getValidityUnit())) {
			combo_unit.setText(l.getString("year"));
		} else if ("m".equalsIgnoreCase(certTemplate.getValidityUnit())) {
			combo_unit.setText(l.getString("month"));
		} else {
			combo_unit.setText(l.getString("day"));
		}
		combo_rsaMinLen.setText(certTemplate.getPubKeyPolicy().get(0).getMinkeylength());
		combo_signAlg.setText(certTemplate.getSignAlg());
		if ("clone".equalsIgnoreCase(operationType)) {
			text_tempName.setText("");
			text_tempName.setFocus();
		}
		if ("modify".equalsIgnoreCase(operationType) || "modify_isUsed".equalsIgnoreCase(operationType)) {
			text_tempName.setEnabled(false);
		}
	}

	/**
	 * @throws Exception 
	 * @Desc 修改模板时，调用此方法，将表单信息封装到对象中
	 * @When Panel_Template 页面，确定按钮提交信息时
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午3:55:35
	 */
	public void tempAppendBasicInfo(CertTemplate certTemplate) throws Exception {
		if (btn_signalTemp.getSelection()) {
			certTemplate.setSignal(true); // 封装此项，为了在 确定按钮提交前，判断是否有加密模板
		}
		if (text_tempName.getText() != null && text_tempName.getText().length() != 0) {
			certTemplate.setCerttemplateName(text_tempName.getText());
		} else {
			throw new Exception(l.getString("Notice_null_tempName"));
		}
		certTemplate.setSpecialType(combo_tempType.getText());
		certTemplate.setValiditylimit(Integer.valueOf(text_validTime.getText()));
		if (l.getString("day").equalsIgnoreCase(combo_unit.getText())) {
			certTemplate.setValidityUnit("d");
		} else if (l.getString("month").equalsIgnoreCase(combo_unit.getText())) {
			certTemplate.setValidityUnit("m");
		} else if (l.getString("year").equalsIgnoreCase(combo_unit.getText())) {
			certTemplate.setValidityUnit("y");
		}
		Keypolicy keyPolicy_rsa = new Keypolicy();
		keyPolicy_rsa.setAlgorithmidentifier("RSA");
		keyPolicy_rsa.setMinkeylength(combo_rsaMinLen.getText());
		Keypolicy keyPolicy_sm2 = new Keypolicy();
		keyPolicy_sm2.setAlgorithmidentifier("SM2");
		keyPolicy_sm2.setMinkeylength("256");

		List<Keypolicy> pubKeyList = new ArrayList<Keypolicy>();
		pubKeyList.add(keyPolicy_rsa);
		pubKeyList.add(keyPolicy_sm2);

		certTemplate.setPubKeyPolicy(pubKeyList);
		certTemplate.setSignAlg((combo_signAlg.getText()));
	}

	/**
	 * @Desc 模板 基本信息 面板
	 * @Authod 江岩
	 * @Date 2019年3月18日 下午3:47:26
	 */
	private void panel_basic(Composite composite) {

		GridLayout gl_composite_1 = new GridLayout(7, false);
		gl_composite_1.marginTop = 20;
		gl_composite_1.marginBottom = 20;
		gl_composite_1.marginLeft = 15;
		gl_composite_1.marginRight = 5;
		gl_composite_1.verticalSpacing = 15;
		composite.setLayout(gl_composite_1);

		btn_signalTemp = new Button(composite, SWT.RADIO);
		btn_signalTemp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
		btn_signalTemp.setText(l.getString("singleTemplate"));
		btn_signalTemp.setSelection(true);
		btn_signalTemp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				combo_tempType.setEnabled(true);
			}
		});
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		btn_doubleTemp = new Button(composite, SWT.RADIO);
		GridData gd_btn_doubleTemp = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_doubleTemp.heightHint = 24;
		btn_doubleTemp.setLayoutData(gd_btn_doubleTemp);
		btn_doubleTemp.setText(l.getString("doubleTemplate"));
		btn_doubleTemp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				combo_tempType.setText("basic");
				combo_tempType.setEnabled(false);
			}
		});
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lbl_templateName = new Label(composite, SWT.NONE);
		lbl_templateName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_templateName.setAlignment(SWT.RIGHT);
		lbl_templateName.setText(l.getString("templateName") + ":");

		text_tempName = new Text(composite, SWT.BORDER);
		GridData gd_text_tempName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_text_tempName.widthHint = 215;
		text_tempName.setLayoutData(gd_text_tempName);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 19;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblNewLabel.setText("*");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lblNewLabel_10 = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel_10 = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 5, 1);
		gd_lblNewLabel_10.heightHint = 23;
		gd_lblNewLabel_10.widthHint = 290;
		lblNewLabel_10.setLayoutData(gd_lblNewLabel_10);
		lblNewLabel_10.setAlignment(SWT.LEFT);
		lblNewLabel_10.setText(l.getString("Notice_allowContent"));
		new Label(composite, SWT.NONE);

		Label lbl_tempFeatures = new Label(composite, SWT.NONE);
		lbl_tempFeatures.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_tempFeatures.setAlignment(SWT.RIGHT);
		lbl_tempFeatures.setText(l.getString("templateFeatures") + ":");

		combo_tempType = new Combo(composite, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_tempType = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_combo_tempType.widthHint = 199;
		combo_tempType.setLayoutData(gd_combo_tempType);
		combo_tempType.add("basic");
		combo_tempType.add("ocsp");
		combo_tempType.add("scep");
		combo_tempType.add("ms_smartCard");
		combo_tempType.add("ms_domainController");
		combo_tempType.setText("basic");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		if (!isAdd) { // 修改操作不能修改 单双证  不能 切换证书类型  
			btn_signalTemp.setEnabled(false);
			btn_doubleTemp.setEnabled(false);
			combo_tempType.setEnabled(false);
		}

		Label lbl_tempStatus = new Label(composite, SWT.NONE);
		//gd_lbl_tempStatus.heightHint = 20;
		lbl_tempStatus.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbl_tempStatus.setAlignment(SWT.RIGHT);
		lbl_tempStatus.setText(l.getString("templateStatus") + ":");

		Label lbl_normal = new Label(composite, SWT.NONE);
		GridData gd_lbl_normal = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lbl_normal.widthHint = 68;
		lbl_normal.setLayoutData(gd_lbl_normal);
		lbl_normal.setText(l.getString("normal"));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lbl_lenLimit = new Label(composite, SWT.NONE);
		GridData gd_lbl_lenLimit = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_lenLimit.widthHint = 102;
		lbl_lenLimit.setLayoutData(gd_lbl_lenLimit);
		lbl_lenLimit.setAlignment(SWT.RIGHT);
		lbl_lenLimit.setText(l.getString("validityDate") + ":");

		text_validTime = new Text(composite, SWT.BORDER);
		GridData gd_text_validTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_text_validTime.widthHint = 74;
		text_validTime.setLayoutData(gd_text_validTime);
		text_validTime.setTextLimit(3);
		text_validTime.setText("365");
		text_validTime.addListener(SWT.FocusOut, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				try {
					int number = Integer.parseInt(text_validTime.getText().trim());
					if (text_validTime.getText().trim().indexOf("0") == 0 || number <= 0 || number > 1000) {
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_validTimeRange"));
						mb.open();
					}
				} catch (NumberFormatException e) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_error_validTime"));
					mb.open();
				}	
			}
		});


		Label lbl_unit = new Label(composite, SWT.NONE);
		GridData gd_lbl_unit = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_unit.widthHint = 57;
		lbl_unit.setLayoutData(gd_lbl_unit);
		lbl_unit.setAlignment(SWT.RIGHT);
		lbl_unit.setText(l.getString("unit") + ":");

		combo_unit = new Combo(composite, SWT.NONE);
		GridData gd_combo_unit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_combo_unit.widthHint = 41;
		combo_unit.setLayoutData(gd_combo_unit);
		combo_unit.add(l.getString("day"));
		combo_unit.add(l.getString("month"));
		combo_unit.add(l.getString("year"));
		combo_unit.setText(l.getString("day"));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Group group_111 = new Group(composite, SWT.NONE);
		GridLayout gridL = new GridLayout(5, false);
		gridL.marginTop = 15;
		gridL.marginBottom = 15;
		gridL.marginLeft = 15;
		gridL.marginRight = 20;
		gridL.verticalSpacing = 10;
		group_111.setLayout(gridL);
		GridData gd_group_111 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);
		gd_group_111.heightHint = 227;
		gd_group_111.widthHint = 531;
		group_111.setLayoutData(gd_group_111);
		group_111.setText(l.getString("keyLength"));

		Group group = new Group(group_111, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1);
		gd_group.heightHint = 79;
		gd_group.widthHint = 400;
		group.setLayoutData(gd_group);
		group.setText(l.getString("keyLength"));

		Label lbl_RSAminLength = new Label(group, SWT.NONE);
		lbl_RSAminLength.setBounds(18, 30, 119, 17);
		lbl_RSAminLength.setAlignment(SWT.RIGHT);
		lbl_RSAminLength.setText(l.getString("RSAKeyLength_min") + ":");

		combo_rsaMinLen = new Combo(group, SWT.NONE | SWT.READ_ONLY);
		combo_rsaMinLen.setBounds(167, 27, 80, 25);
		combo_rsaMinLen.add("1024");
		combo_rsaMinLen.add("2048");
		combo_rsaMinLen.setText("1024");

		Label lbl_SM2Length = new Label(group, SWT.NONE);
		lbl_SM2Length.setBounds(10, 60, 129, 17);
		lbl_SM2Length.setAlignment(SWT.RIGHT);
		lbl_SM2Length.setText(l.getString("SM2KeyLength_min") + ":");

		Label lbl_256 = new Label(group, SWT.NONE);
		lbl_256.setBounds(167, 60, 34, 17);
		lbl_256.setText("256");
		new Label(group_111, SWT.NONE);

		Label lbl_null = new Label(group_111, SWT.NONE);
		GridData gd_lbl_null = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_null.widthHint = 150;
		lbl_null.setLayoutData(gd_lbl_null);
		lbl_null.setText("");

		Label lbl_null1 = new Label(group_111, SWT.NONE);
		GridData gd_lbl_null1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_null1.widthHint = 107;
		lbl_null1.setLayoutData(gd_lbl_null1);
		lbl_null1.setText("");
		new Label(group_111, SWT.NONE);
		new Label(group_111, SWT.NONE);

		Label lbl_signAlg = new Label(group_111, SWT.NONE);
		GridData gd_lbl_signAlg = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lbl_signAlg.widthHint = 67;
		lbl_signAlg.setLayoutData(gd_lbl_signAlg);
		lbl_signAlg.setAlignment(SWT.RIGHT);
		lbl_signAlg.setText(l.getString("SignAlg") + ":");

		combo_signAlg = new Combo(group_111, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo_signAlg = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_combo_signAlg.widthHint = 133;
		combo_signAlg.setLayoutData(gd_combo_signAlg);
		if (Env.alg == ALG.SM2) {
			combo_signAlg.add("Sm3withSm2");
		} else {
			combo_signAlg.add("SHA1withRSA");
			combo_signAlg.add("SHA256withRSA");
		}
		combo_signAlg.select(0);
		
		new Label(group_111, SWT.NONE);
		new Label(group_111, SWT.NONE);
		new Label(group_111, SWT.NONE);
	}

	/**
	 * @Desc 视图标题命名
	 * @Authod 江岩
	 * @Date 2019年3月18日 下午3:47:26
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("basicInfo"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	public static void main(String[] args) {
		Panel_BasicTemplate basic = new Panel_BasicTemplate();
		basic.setBlockOnOpen(true);
		basic.open();
		
	}
}
