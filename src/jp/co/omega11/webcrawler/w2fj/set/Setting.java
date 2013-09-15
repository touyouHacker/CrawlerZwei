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




//�V���A���C�Y���p������XML�`���ɂł���悤��
//���W�b�N���\�b�h�͌p�����Ďq�N���X�ɂ���

public class Setting {

	private String settingFilename = "/setting.xml";

	/********** ADMIN MAIN **********************/
	private String URL;

	private String datURL;

	private String currentDirectory;

	// URL����http:// �𔲂�������
	private String itaRootAdress;

	/**
	 * �����URL���o�����s���邩���Ȃ����̃t���O
	 * ���Ԃ�������W2FJ���N�������Ƃ��R���e���cURL�𒊏o���Ă�404�΂�����ɂȂ�̂�
	 * ����̓p�[�X������URL�𒊏o���Ȃ��悤�ɂ���B
	 *
	 * �ŏ��ɋN�������Ƃ�(DDL���s��Ȃ�)�͎�����true��
	 * TODO ��������
	 */
	private boolean noParseExe;


	/**
	 * BBS PINK��bg20(�N���[���[�p��Dat�T�[�o�[)���Ȃ�����2ch�u���E�U�Ɠ����悤��Dat�擾���W�b�N��
	 * �s���K�v������B
	 * BBSPINK�n�̔ł����True��
	 */
	private boolean pinkFlag;


	/**
	 * BBSPINK��Root�T�[�o�[ ��BBSPINK���ǂ����̔���Ɏg�p
	 * TODO  �ݒ�XML�ɈڐA���Ă���
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

	/** ��(�p��) */
	private String Itaname;


	//���� IndividualSet -> �e�X���b�h�̋��ʐݒ荀�ڂ��i�[


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
	 * �R���X�g���N�^
	 */
	public Setting() {
		//�I�u�W�F�N�g���쐬����K�v��������̂��쐬���Ă���
		setInvSubject(new IndividualSetModel());
		setInvDat(new IndividualSetModel());
		setInvContents(new IndividualSetModel());

		setPop3Model(new POP3Model());
		setSmtpModel(new SmtpModel());
	}


	/**
	 * ���[����M�@�\�̏��������s���܂�
	 * �X���b�h�N�����Ȃ��Ƃ��͕s�v�Ȃ̂Ń��\�b�h��
	 * @param set
	 */
	public void setRemoteControlForMail(){
		pop3Model.setReceiveSubject(Itaname);
	}


	public void setInitialize(Setting set) throws Exception {

		// Java & OS���̐ݒ��ǂݍ��݂܂�
		JavaEnviroment.initJavaProperties();

		File fileCurrent = new File(".");
		String cs = fileCurrent.getAbsolutePath();
		currentDirectory = cs.substring(0, cs.length() - 1);


		//XML�ݒ�t�@�C�����[�h
		SettingXmlReader settingXmlReader = new SettingXmlReader();
		settingXmlReader.load(set);

		//URL����̉p�����o
		String [] urls = URL.split("/");
		setItaname(urls[urls.length -1 ]);
		// ita�̃T�[�o�[�A�h���X�擾
		setItaRootAdress(urls[urls.length -2 ]);


		if(pinkRootServer.equals(itaRootAdress)) {
			pinkFlag = true;
			Loger.print("BBS-PINK�n�̔Ɣ���BDat���N���[���[�p�T�[�o�[����擾���܂���");
		}


		//dbURL����x���_�[�̒��o(�h���C�o�ǂݍ��݂̂���)
		String [] dbUrlSplied = dbURL.split(":");
		vender = dbUrlSplied[1];

		// TODO �o�b�N�]�˃T�[�o�[ bg20���w�l�k�ɏ����Ă���

		//DAT��URL
		// datURL = URL + "dat/"; ���{����DAT URL

		if (pinkFlag) {

			// GET /ascii2d/dat/1247303678.dat HTTP/1.1

			datURL = "http://" + itaRootAdress + "/"+ Itaname +"/dat/";

		} else {
			// PINK�ȊO�̃T�[�o�[�̂Ƃ�
			/*
			 * ���Ƃ����̐K��
			 *
			 * �Ⴆ�΁Ahttp://society6.2ch.net/test/read.cgi/gline/1169964276/ �̏ꍇ�A
			 * http://bg20.2ch.net/test/r.so/society6.2ch.net/gline/1169964276/ ����dat���擾�ł��܂��B
			 */
			// http://bg20.2ch.net/test/r.so/tsushima.2ch.net/news/1241320872/

			datURL = "http://bg20.2ch.net/test/r.so/" + getItaRootAdress()
					+ "/" + getItaname() + "/";
		}


		//�K�v�ȃf�B���N�g���̍쐬

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

		// �K�v�ȃf�B���N�g���̍쐬(�x�[�X�t�H���_)
		UniversalUtil.createDirectryExistCheak(subjectFolderFullPath);
		UniversalUtil.createDirectryExistCheak(datFolderFullPath);
		UniversalUtil.createDirectryExistCheak(contentsFolderFullPath);

		//�K�v�ȃf�B���N�g���̍쐬(�ŗL�̃t�H���_)


		subjectFolderFullPath += "/" + Itaname;
		datFolderFullPath +=  "/" + Itaname;
		contentsFolderFullPath +=  "/" + Itaname;

		UniversalUtil.createDirectryExistCheak(subjectFolderFullPath);
		UniversalUtil.createDirectryExistCheak(datFolderFullPath);
		UniversalUtil.createDirectryExistCheak(contentsFolderFullPath);

		try {
			loadJDBCdriver();
		} catch (Exception e) {
			Loger.print("JDBC�h���C�o�̃��[�h�Ɏ��s���܂����A�������I�����܂��B");
			throw e;
		}

		try {
			checkTable();
		} catch (SQLException e) {
			Loger.print("�e�[�u�����݃`�F�b�N�A����DDL���������Ŏ��s���܂����B�������I�����܂��B");
			throw e;
		}

		// TODO ����ȑO�̃��O�͕W���o�͂ɂłĂ��܂�


		//���O�V�X�e�������������܂�
		logInit();
	}

	/**
	 * JDBC�h���C�o�����[�h���܂�
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void loadJDBCdriver() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
	}

	/**
	 * �e�[�u�������݂��Ă��邩���`�F�b�N���܂��B���Ă��Ȃ���΍쐬���܂�
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


