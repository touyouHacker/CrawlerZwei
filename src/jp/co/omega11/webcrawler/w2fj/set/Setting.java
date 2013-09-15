package jp.co.omega11.webcrawler.w2fj.set;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jp.co.omega11.universal.controller.receivecommand.model.POP3Model;
import jp.co.omega11.universal.controller.receivecommand.model.SmtpModel;
import jp.co.omega11.universal.util.UniversalUtil;
import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.universal.util.system.JavaEnviroment;
import jp.co.omega11.webcrawler.w2fj.constant.Constant;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.ContentsDAO;
import jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.SubjectTextDAO;
import jp.co.omega11.webcrawler.w2fj.model.IndividualSetModel;

import org.apache.commons.lang.StringUtils;




//シリアライズを継承してXML形式にできるように
//ロジックメソッドは継承して子クラスにする

public class Setting {

	private String settingFilename = "/setting.xml";

	/********** ADMIN MAIN **********************/
	private String URL;

	private String datURL;

	private String currentDirectory;

	// URLからhttp:// を抜いたもの
	private String itaRootAdress;

	/**
	 * 初回にURL抽出を実行するかしないかのフラグ
	 * 時間をあけてW2FJを起動したときコンテンツURLを抽出しても404ばっかりになるので
	 * 初回はパースをせずURLを抽出しないようにする。
	 *
	 * 最初に起動したとき(DDL実行後など)は自動でtrueに
	 * TODO ↑未実装
	 */
	private boolean noParseExe;


	/**
	 * BBS PINKはbg20(クローラー用のDatサーバー)がないため2chブラウザと同じようにDat取得ロジックを
	 * 行う必要がある。
	 * BBSPINK系の板であればTrueに
	 */
	private boolean pinkFlag;


	/**
	 * BBSPINKのRootサーバー 板がBBSPINKかどうかの判定に使用
	 * TODO  設定XMLに移植しておく
	 *
	 */
	private String pinkRootServer = "pele.bbspink.com";



	private String subjectFolderFullPath;
	private String datFolderFullPath;
	private String contentsFolderFullPath;

	/********** DB ******************************/

	private String dbURL;

	private String dbUser;

	private String dbPass;

	private String vender;

	private String schema;

	/** 板名(英名) */
	private String Itaname;


	//■■ IndividualSet -> 各スレッドの共通設定項目を格納


	/*********** SUBJECT TEXT *****************/
	private IndividualSetModel invSubject;


	/*********** Dat **************************/
	private IndividualSetModel invDat;

	/*********** Contents ********************/
	private IndividualSetModel invContents;


	/*********** HTTP OPTION *****************/
	private String UsetAgent;
	/*
		Mozilla/5.0 (Windows; U; Windows NT 6.0; ja; rv:1.9.0.7) Gecko/2009021910 Firefox/3.0.7 (.NET CLR 3.5.30729)
		Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US) AppleWebKit/525.19 (KHTML, like Gecko) Chrome/1.0.154.53 Safari/525.19
	*/


	/********** Mail ***********************/
	private POP3Model pop3Model;
	private SmtpModel smtpModel;
	private String receiveMailProtocol;
	private long mailSleepTime;

	/**
	 * コンストラクタ
	 */
	public Setting() {
		//オブジェクトを作成する必要があるものを作成しておく
		setInvSubject(new IndividualSetModel());
		setInvDat(new IndividualSetModel());
		setInvContents(new IndividualSetModel());

		setPop3Model(new POP3Model());
		setSmtpModel(new SmtpModel());
	}


	/**
	 * メール受信機構の初期化を行います
	 * スレッド起動しないときは不要なのでメソッド化
	 * @param set
	 */
	public void setRemoteControlForMail(){
		pop3Model.setReceiveSubject(Itaname);
	}


	public void setInitialize(Setting set) throws Exception {

		// Java & OS環境の設定を読み込みます
		JavaEnviroment.initJavaProperties();

		File fileCurrent = new File(".");
		String cs = fileCurrent.getAbsolutePath();
		currentDirectory = cs.substring(0, cs.length() - 1);


		//XML設定ファイルロード
		SettingXmlReader settingXmlReader = new SettingXmlReader();
		settingXmlReader.load(set);

		//URLから板の英名抽出
		String [] urls = URL.split("/");
		setItaname(urls[urls.length -1 ]);
		// itaのサーバーアドレス取得
		setItaRootAdress(urls[urls.length -2 ]);


		if(pinkRootServer.equals(itaRootAdress)) {
			pinkFlag = true;
			Loger.print("BBS-PINK系の板と判定。Datをクローラー用サーバーから取得しません");
		}


		//dbURLからベンダーの抽出(ドライバ読み込みのため)
		String [] dbUrlSplied = dbURL.split(":");
		vender = dbUrlSplied[1];

		// TODO バック江戸サーバー bg20もＸＭＬに書いておく

		//DATのURL
		// datURL = URL + "dat/"; ☆本来のDAT URL

		if (pinkFlag) {

			// GET /ascii2d/dat/1247303678.dat HTTP/1.1

			datURL = "http://" + itaRootAdress + "/"+ Itaname +"/dat/";

		} else {
			// PINK以外のサーバーのとき
			/*
			 * ■とかげの尻尾
			 *
			 * 例えば、http://society6.2ch.net/test/read.cgi/gline/1169964276/ の場合、
			 * http://bg20.2ch.net/test/r.so/society6.2ch.net/gline/1169964276/ からdatを取得できます。
			 */
			// http://bg20.2ch.net/test/r.so/tsushima.2ch.net/news/1241320872/

			datURL = "http://bg20.2ch.net/test/r.so/" + getItaRootAdress()
					+ "/" + getItaname() + "/";
		}


		//必要なディレクトリの作成

		subjectFolderFullPath = set.invSubject.getSaveDownload();
		datFolderFullPath = set.invDat.getSaveDownload();
		contentsFolderFullPath = set.invContents.getSaveDownload();


		if (StringUtils.isBlank(subjectFolderFullPath)) {
			subjectFolderFullPath = Constant.SUBJECTTEXTFOLDER;
		}

		if (StringUtils.isBlank(datFolderFullPath)) {
			datFolderFullPath = Constant.DATFOLDER;
		}

		if (StringUtils.isBlank(contentsFolderFullPath)) {
			contentsFolderFullPath = Constant.CONTENTSFOLDER;
		}

		// 必要なディレクトリの作成(ベースフォルダ)
		UniversalUtil.createDirectryExistCheak(subjectFolderFullPath);
		UniversalUtil.createDirectryExistCheak(datFolderFullPath);
		UniversalUtil.createDirectryExistCheak(contentsFolderFullPath);

		//必要なディレクトリの作成(板固有のフォルダ)


		subjectFolderFullPath += "/" + Itaname;
		datFolderFullPath +=  "/" + Itaname;
		contentsFolderFullPath +=  "/" + Itaname;

		UniversalUtil.createDirectryExistCheak(subjectFolderFullPath);
		UniversalUtil.createDirectryExistCheak(datFolderFullPath);
		UniversalUtil.createDirectryExistCheak(contentsFolderFullPath);

		try {
			loadJDBCdriver();
		} catch (Exception e) {
			Loger.print("JDBCドライバのロードに失敗しました、処理を終了します。");
			throw e;
		}

		try {
			checkTable();
		} catch (SQLException e) {
			Loger.print("テーブル存在チェック、又はDDL投入処理で失敗しました。処理を終了します。");
			throw e;
		}

		// TODO これ以前のログは標準出力にでてしまう


		//ログシステムを初期化します
		logInit();
	}

	/**
	 * JDBCドライバをロードします
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void loadJDBCdriver() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
	}

	/**
	 * テーブルが存在しているかをチェックします。していなければ作成します
	 * @param set
	 * @throws SQLException
	 */
	public void checkTable() throws SQLException {
		Connection con = null;
		try {
			con = DriverManager.getConnection(dbURL, dbUser, dbPass);
		} catch (SQLException e) {
			Loger.print(e);
			throw e;
		}

		SubjectTextDAO subjectTextDAO = new SubjectTextDAO(con, Itaname);
		if(!subjectTextDAO.checkExsistTable()){
			subjectTextDAO.createTable(con);
		}

		ContentsDAO contentsDAO = new ContentsDAO(con, Itaname);
		if(!contentsDAO.checkExsistTable()){
			contentsDAO.createTable(con);
		}

		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				Loger.print(e);
			}
		}
	}


	public void logInit() {
		try {
			Loger.setFile( Itaname + UniversalUtil.nowDate(), Itaname + UniversalUtil.nowDate());
		} catch (IOException e) {
			Loger.print(e);
		}
	}


	public String getURL() {
		return URL;
	}

	public void setURL(String url) {
		URL = url;
	}

	public String getItaname() {
		return Itaname;
	}

	public void setItaname(String itaname) {
		Itaname = itaname;
	}

	public String getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(String currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	public String getDbURL() {
		return dbURL;
	}

	public void setDbURL(String dbRL) {
		this.dbURL = dbRL;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}


	/**
	 * @return the vender
	 */
	public String getVender() {
		return vender;
	}


	/**
	 * @param vender the vender to set
	 */
	public void setVender(String vender) {
		this.vender = vender;
	}


	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}






	/**
	 * @return the noParseExe
	 */
	public boolean isNoParseExe() {
		return noParseExe;
	}




	/**
	 * @param noParseExe the noParseExe to set
	 */
	public void setNoParseExe(boolean noParseExe) {
		this.noParseExe = noParseExe;
	}




	/**
	 * @param schema the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUsetAgent() {
		return UsetAgent;
	}

	public void setUsetAgent(String usetAgent) {
		UsetAgent = usetAgent;
	}

	public String getSettingFilename() {
		return settingFilename;
	}


	public void setSettingFilename(String settingFilename) {
		this.settingFilename = settingFilename;
	}


	public String getDatURL() {
		return datURL;
	}


	public void setDatURL(String datURL) {
		this.datURL = datURL;
	}


	public String getItaRootAdress() {
		return itaRootAdress;
	}


	public void setItaRootAdress(String itaRootAdress) {
		this.itaRootAdress = itaRootAdress;
	}



	/**
	 * @return the pinkFlag
	 */
	public boolean isPinkFlag() {
		return pinkFlag;
	}




	/**
	 * @param pinkFlag the pinkFlag to set
	 */
	public void setPinkFlag(boolean pinkFlag) {
		this.pinkFlag = pinkFlag;
	}




	/**
	 * @return the pinkRootServer
	 */
	public String getPinkRootServer() {
		return pinkRootServer;
	}




	/**
	 * @param pinkRootServer the pinkRootServer to set
	 */
	public void setPinkRootServer(String pinkRootServer) {
		this.pinkRootServer = pinkRootServer;
	}




	public IndividualSetModel getInvSubject() {
		return invSubject;
	}


	public void setInvSubject(IndividualSetModel invSubject) {
		this.invSubject = invSubject;
	}


	public IndividualSetModel getInvDat() {
		return invDat;
	}


	public void setInvDat(IndividualSetModel invDat) {
		this.invDat = invDat;
	}


	public IndividualSetModel getInvContents() {
		return invContents;
	}


	public void setInvContents(IndividualSetModel invContents) {
		this.invContents = invContents;
	}




	/**
	 * @return the subjectFolderFullPath
	 */
	public String getSubjectFolderFullPath() {
		return subjectFolderFullPath;
	}




	/**
	 * @param subjectFolderFullPath the subjectFolderFullPath to set
	 */
	public void setSubjectFolderFullPath(String subjectFolderFullPath) {
		this.subjectFolderFullPath = subjectFolderFullPath;
	}




	/**
	 * @return the datFolderFullPath
	 */
	public String getDatFolderFullPath() {
		return datFolderFullPath;
	}




	/**
	 * @param datFolderFullPath the datFolderFullPath to set
	 */
	public void setDatFolderFullPath(String datFolderFullPath) {
		this.datFolderFullPath = datFolderFullPath;
	}




	/**
	 * @return the contentsFolderFullPath
	 */
	public String getContentsFolderFullPath() {
		return contentsFolderFullPath;
	}




	/**
	 * @param contentsFolderFullPath the contentsFolderFullPath to set
	 */
	public void setContentsFolderFullPath(String contentsFolderFullPath) {
		this.contentsFolderFullPath = contentsFolderFullPath;
	}




	/**
	 * @return the pop3Model
	 */
	public POP3Model getPop3Model() {
		return pop3Model;
	}




	/**
	 * @param pop3Model the pop3Model to set
	 */
	public void setPop3Model(POP3Model pop3Model) {
		this.pop3Model = pop3Model;
	}




	/**
	 * @return the smtpModel
	 */
	public SmtpModel getSmtpModel() {
		return smtpModel;
	}




	/**
	 * @param smtpModel the smtpModel to set
	 */
	public void setSmtpModel(SmtpModel smtpModel) {
		this.smtpModel = smtpModel;
	}




	/**
	 * @return the mailSleepTime
	 */
	public long getMailSleepTime() {
		return mailSleepTime;
	}




	/**
	 * @param mailSleepTime the mailSleepTime to set
	 */
	public void setMailSleepTime(long mailSleepTime) {
		this.mailSleepTime = mailSleepTime;
	}




	/**
	 * @return the receiveProtocol
	 */
	public String getReceiveProtocol() {
		return receiveMailProtocol;
	}




	/**
	 * @param receiveProtocol the receiveProtocol to set
	 */
	public void setReceiveProtocol(String receiveProtocol) {
		this.receiveMailProtocol = receiveProtocol;
	}
}


