package jp.co.omega11.webcrawler.w2fj;

import java.util.ArrayList;
import java.util.List;

import jp.co.omega11.universal.controller.receivecommand.ReceiveCommandThread;
import jp.co.omega11.universal.controller.receivecommand.component.console.ControlFromConsole;
import jp.co.omega11.universal.controller.receivecommand.component.mail.ControlFromMail;
import jp.co.omega11.universal.util.log.Log;
import jp.co.omega11.webcrawler.w2fj.component.contents.ContentsDownloderThread;
import jp.co.omega11.webcrawler.w2fj.component.dat.DatDownloderThread;
import jp.co.omega11.webcrawler.w2fj.component.remotecontrol.ExecuteCommandForW2fj;
import jp.co.omega11.webcrawler.w2fj.component.subject.SubjectTextGet;
import jp.co.omega11.webcrawler.w2fj.component.subject.SubjectTextGetThread;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.RootInfo;
import jp.co.omega11.webcrawler.w2fj.set.Setting;

/**
 * W2FJ[Watch 2ch For Java]�̂��ׂĂ��Ǘ����郋�[�g�N���X
 * ���̃N���X�ł͂P���������ł��Ȃ�����
 * �����̔���������ꍇ�A���̃N���X�𕡐���т����悤�Ăяo���N���X�Ŏ�������
 * ���ۂ̑z��ł�1��1�v���Z�X�Ƃ��Ď����A�e�X�g���s���Ă���B
 *
 * 1�v���Z�X�ŕ������̃N���X����΂�Ă��������邱�Ƃ͏����I�ɂ͂Ȃ����ݒ�t�@�C���̋L�q�ɂ���Ă�
 * ���Ԃ��d�Ȃ��ď������u�b�L���O�����肷��\���͂���B
 * �܂��o�̓t�H���_���d�Ȃ�̂œ����𓯎��ɕ�����΂Ȃ����Ɓi�N���X���b�h�𐧌�����΃u�b�L���O�͂�����Ȃ��j
 *
 * @author Wizard1
 *
 */
public class W2fjMainLogic {

	/**
	 * �R�A�ƂȂ�X���b�h�N���X�Q
	 * �X���b�h��~�A�ċN���𐧌䂷�邽�߃t�B�[���h�ɂ���
	 */
	private SubjectTextGetThread subjectGetThread;
	private DatDownloderThread datDownloderThread;
	private ContentsDownloderThread contentsDownloderThread;
	private List <ReceiveCommandThread> receiveCommandThreads = new ArrayList<ReceiveCommandThread>();

	// false�������l
	private boolean subjectGetThread_ExeFlag;
	private boolean datDownloderThread_ExeFlag;
	private boolean contentsDownloderThread_ExeFlag;
	private boolean noThread;

	// �R�}���h��t�@�\�̃X���b�h���s�ۃt���O
	// TODO �Ƃ肠����True
	private boolean controlFromConsole_ExeFlag = true;
	private boolean controlFromMail_ExeFlag;

	public W2fjMainLogic() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �N�����\�b�h
	 * @param args
	 */
	public void start(String args[]) {
		/**
		 * �X���b�h�R���g���[�����C���N���X �e�X���b�h�͐ݒ肳�ꂽ���ԊԊu�ŋN������ �e�X���b�h�͓������Ƃ炸���ꂼ��DB�l�����ď������s��
		 * �܂�e�X���b�h�͓Ɨ����ē����Ă��菇�Ԃ͊�{�I�ɑ��݂��Ȃ�
		 *
		 * �R�l�N�V�����͊e�X���b�h�łP�Âێ�����
		 *
		 *�@�r���I���̏����A�ُ�I���ADB�̃��J�o���Ƃ������@�\�͌ォ��ǉ�����Ƃ���
		 *
		 * �}���`�v���b�g�t�H�[����ΏƂƂ� Windows Linux MacOSX �Ńo�b�`�Ƃ��ē���ł���悤���
		 * �����DB�\�t�g�����̊��œ������̂��^�[�Q�b�g�Ƃ��� �}���`�v���b�g�t�H�[���œ����Ȃ�JAVA ���C�u�����͎g��Ȃ�
		 * GUI��EclipseGUI or QT or SWT���g��
		 *
		 * �������R�[�h ���f�B���N�g���A�t�@�C���Ȃǂ̃p�X
		 *
		 * �Ȃǂ͒��ӂ���
		 *
		 * .NET�ł�����悤JAVA�ŗL�̏������͂Ȃ�ׂ������� Interface�łi�`�u�`�N���X�𒊏ۉ����ăC���^�[�t�F�[�X���\�b�h���g�p
		 *
		 * DB���
		 * ��Apache Derby
		 * ��MySQl
		 * ��PostgreSQL
		 * ��Oracle11g
		 * ��http://ja.wikipedia.org/wiki/SQLite
		 * ��PlainFile
		 *
		 *
		 * �e�X���b�h�͕ʃ}�V������ł��N���ł���悤�ɂ��� ���U����
		 *
		 *
		 * �X���b�h�Z�[�t���\�b�h�A�N���X���ǂ������C�ɂ��č���ĂȂ��̂Ō�Ő���
		 *
		 * ������g������ WebHyperSplider�������悤�� Amazon�̉摜���W�Ƃ��A�b�v���[�_�[�N���[���[�Ƃ�
		 *
		 * �^�[�Q�b�g�̓j���[�X���񂨂��+ ������
		 *
		 * [�e�X�g�����{]
		 * �EDB�T�[�o�[�����s�t�@�C���ƕʒ[���ɂ��Ẳ^�p
		 * �E�R�l�N�V�����̕ێ��̗L����
		 *
		 * [�������A����������]]
		 * �E�O���X�g���[�W�ɉ摜��]���ł���I�v�V����������
		 *   DAO�Ł@�X�^�[�g��������ŏI���܂ł�I�����Ă��̊Ԃ̉摜���X�g���[�W�h���C�u�̎w�肳�ꂽ�t�H���_�ɂ�����
		 *   �@�d�ԂƂ��ło�r�o�ŉ摜���m�F�ł���̂ŕ֗�
		 *   �@�摜�̃��T�C�Y�͌ォ��Ή�
		 * �E�������̂��߂ɂ͌��݂����Ă�t�@�C���n���h���̖����������������K�v������
		 * �E��񃂃f���͂�����new����i�P�̃X���b�h�̂Ƃ����̃X���b�h���擾�ł����Ȃ��悤�Ɂj
		 * �ERemote�P�̋N���̂Ƃ��i���̃X���b�h�͕ʃv���Z�X�j�ǂ�����ď����Ƃ邩
		 * �EPOP�Y�����[���f���[�g[�����e�X�g��]
		 * �E�N�����Ԃ�x�点��@�����ŋN���ҋ@���Ԃ𕪁A���ԂŐݒ�ł���悤��(���ԃ��[�e���K�v)
		 * �EIMAP
		 * �E��O�̂Ƃ��ɔ������Ԃƈ����̕\��
		 * �ESQL�̃o�C���h�ϐ��̃��O�\��
		 * �Ejavadoc�̐���
		 * �E�f�v���C�@�\�A���I�A�b�v�f�[�g
		 * �E�ꗗ�̎擾
		 * �E2ch�̏�Ԃ��`�F�b�N���邽�ߐڑ��e�X�g ping�̓��[�^�[�ł͂�����邱�Ƃ������̂ŕs��
		 * �E�t�@�C���擾���A���s���A�Ȃǂ̃��M���O
		 * �E�T�[�o�[�Ǘ��@�\
		 * �E�f�t�h���
		 * �E�^�X�N�̎��Ԃ��L�^���ăO���t������@�\�����[���ł�������悤��
		 * �E�Q���� User���O�C���@�\
		 * �E�t�q�k�t�B���^�[�@�u���N���@�\
		 * �E�t�q�k�d���̔r��
		 * �E�悤�ׁA�j�R�j�R�Ή�
		 * �E�g�n�r�s�}�X�^�e�[�u����Web�N���[���[�̂���
		 * �EJDBC�R�l�N�V�����̃��J�o��
		 * �E�m�[�h�_�E���[�h�@�\
		 * �E�R���e���c�_�E�����[�_�[�̕����X���b�h�N���Ǘ�
		 * �E�����v���Z�X��w2fj���N�������Ƃ��̏W���Ǘ��@�\[GUI/PHP? �Ƃɂ����P�N���X�͕K�v]
		 * �E�R�l�N�V�����}�l�[�W���[(getconnection���N���X�ŊǗ����A�c�a�P���A�N�Z�X�ł��Ȃ��Ƃ��c�a�Q�փA�N�Z�X����悤�ɂ���)
		 * �@���̂���DB1�Ƃc�a�Q�̓����͂`�o�ł͕ۏ؂��Ȃ��A�`�o�œ������Ƃ��悤�ȏ������ɓ�d�A�N�Z�X����I�v�V�����͕K�v
		 * �E�O���V�X�e���ւ�DB���R�[�h�̃R�s�[�iAP�T�[�o�[���O�����J���邽�߂ɕK�v,FTP��SQL�𑗐M����E�E�Ȃ�)
		 *  	�傩����ȃV�X�e���ł`�o���̂�PHP/RoR�ō\�z����
		 * �ECELL TV�݂����ɃR���e���c�i�X�����j���Ƃɉ摜�����ł���悤�ɂ���JavaFX
		 *
		 *
		 * [�����ς�]
		 *�@�E
		 *  �E
		 * [�e�X�g�ς�]
		 * �E2ch���������Ƃ����v�`�h�s���邾���ŗ����Ȃ�����
		 * �E4�T�Ԃ̘A���ғ�
		 * �E�d������ꂽ��A�ċN����N�����Ă��㑱�������s�������ƁB�^�ǂ��c�a�A�N�Z�X���Ă����^�C�~���O�łȂ�������������
		 *
		 */

		Setting set = new Setting();



		// �������p�[�X���Đݒ�t�@�C������I��
		argumentParser(args, set);

		try {
			set.setInitialize(set);
		} catch (Exception e) {
			Log.print("�������������s");
			Log.print(e);
			return;
		}

		RootInfo rootInfo = new RootInfo(this.getClass().getSimpleName(), set.getItaname());

		if (subjectGetThread_ExeFlag) {
			// subject.txtGet �X���b�h�N�� subject.txt���_�E�����[�h���ăp�[�X���X���b�hDB�o�^
			subjectGetThread = new SubjectTextGetThread(set, rootInfo.getSubjectInfo());
			subjectGetThread.start();
		}

		if (subjectGetThread_ExeFlag && datDownloderThread_ExeFlag) {

			// �e�[�u���̐���������������60�b�܂�
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.print(e);
			}
		}

		if (datDownloderThread_ExeFlag) {
			// DAT�_�E�����[�h�X���b�h�N�����X���b�htable�����ĕK�v�Ȃ̂��_�E�����[�h���摜URL���o���摜DB�o�^�܂�
			// DatDownloader
			datDownloderThread = new DatDownloderThread(set , rootInfo.getDatInfo());
			datDownloderThread.start();
		}

		if (contentsDownloderThread_ExeFlag) {
			// �摜�_�E�����[�h�X���b�h�N�����摜table�����ā��摜�_�E�����[�h��DB�̃��R�[�h���_�E�����[�h�ς݂�UPDATE
			// ContentsDownloader
			contentsDownloderThread = new ContentsDownloderThread(set , rootInfo.getContentsInfo());
			contentsDownloderThread.start();
		}

		// �O���R���g���[�� �R���\�[����Mail�Ȃ�[�����\]
		if (controlFromConsole_ExeFlag) {
			receiveCommandThreads.add (new ReceiveCommandThread(
					new ControlFromConsole(), new ExecuteCommandForW2fj(rootInfo)));
		}

		if(controlFromMail_ExeFlag) {

			//���[����M�ŕK�v�ȏ�����
			set.setRemoteControlForMail();

			receiveCommandThreads.add (new ReceiveCommandThread(
					new ControlFromMail(set.getPop3Model(), set.getSmtpModel(), set.getMailSleepTime()) , new ExecuteCommandForW2fj(rootInfo)));
		}


		for (ReceiveCommandThread receiveCommandThread : receiveCommandThreads) {
			receiveCommandThread.start();
		}

		// �c�a�̃o�b�`������ �폜�t���O���R�[�h�̗����e�[�u���ւ̈ڍs
		// DBBatchLogic

	}

	/**
	 * �R�}���h���C���������p�[�X���Đݒ�I�u�W�F�N�g�̃f�t�H���g�l���㏑�����܂�
	 * �ݒ�͂��ׂ�XML�t�@�C���ōs�����߂����Őݒ�ł���̂͐ݒ�t�@�C�����̂�
	 *
	 * ���Ƃ͋N������X���b�h��I���ł���悤�ɂ���
	 *
	 * @param args
	 * @param set
	 */
	private void argumentParser(String args[], Setting set) {
		/**
		 * �N�������ŋN������X���b�h�����肷��
		 *
		 * -S Subjecttext�̂�
		 * -D DatDownloder�̂�
		 * -C ContentDownloder�̂�
		 *
		 * -S -D (-S/-D�̂�)
		 * -ALL �f�t�H���g�A���ׂċN��
		 *
		 * -NT No Thread �i�f�o�b�O�p�A�N���v���Z�X���M�@�\���f�o�b�O�������Ƃ��Ɏg�p�j
		 * 	   �X���b�h�R���|�[�l���g���N�����܂���
		 *
		 * -F �ݒ�w�l�k�t�@�C�������w��
		 *
		 * -O �O������@�\���w�肵�܂�(�����\)
		 *    -Console
		 *    -Mail
		 *    -Gui
		 *    -TCP [TCP Server]
		 *
		 *
		 * -Re Reboot �ċN�����܂��@�V�K�v���Z�X���N�����@���݃v���Z�X���I�����܂�
		 * -WS WaitStart �N���J�n�܂ł̃E�F�C�g�b�����w�肵�܂��@�ċN���p
		 * -Shutdown �I�����܂�
		 * -fexit Forse Exit �����I�����܂�
		 *
		 */
		for(int i=0;i<args.length;i++){
			if("-F".equals(args[i])){
				set.setSettingFilename(args[i+1]);
				// �ݒ�t�@�C���������̈����Ȃ̂ŉ��Z
				i++;
			}

			if("-S".equals(args[i])) {
				Log.print("-S�E�EsubjectGetThread���N��ON�ɂ��܂�");
				subjectGetThread_ExeFlag = true;
			}

			if("-D".equals(args[i])) {
				Log.print("-D�E�EdatDownloderThread_ExeFlag���N��ON�ɂ��܂�");
				datDownloderThread_ExeFlag = true;
			}

			if("-C".equals(args[i])) {
				Log.print("-C�E�EcontentsDownloderThread_ExeFlag���N��ON�ɂ��܂�");
				contentsDownloderThread_ExeFlag = true;
			}

			if("-NP".equals(args[i])) {
				Log.print("-NP�E�ENoParseExe URL���o��DB�o�^�����񃋁[�v���Ɏ��s���܂���");
				set.setNoParseExe(true);
			}


			if("-NT".equals(args[i])) {
				Log.print("-NT�E�ENoThread �X���b�h�R���|�[�l���g���N�����܂���B�����v���Z�X�̂ݎ��{");
				noThread = true;
			}


			if("-O".equals(args[i])){

				if("-Console".equals(args[i+1])){
					controlFromConsole_ExeFlag = true;
				}
				else if ("-Mail".equals(args[i+1])) {
					/**
					 * Mail�̎�M�@�\
					 * ���[���̌����ɔ�
					 * �{���ɃR�}���h���i�[����ƕԐM�����܂�
					 */
					controlFromMail_ExeFlag = true;
				}

				// �ݒ�t�@�C���������̈����Ȃ̂ŉ��Z
				i++;
			}

		}

		if (subjectGetThread_ExeFlag == false &&
				datDownloderThread_ExeFlag == false &&
				contentsDownloderThread_ExeFlag == false &&
				noThread == false) {
			// ���ׂ�false�̏ꍇ�͋N���X���b�h�w�薳��= ALL�N���Ƃ݂Ȃ�

			Log.print("�N���X���b�h���w�肳��ĂȂ����߁A���ׂċN�����܂��B");
			subjectGetThread_ExeFlag = true;
			datDownloderThread_ExeFlag = true;
			contentsDownloderThread_ExeFlag = true;
		}

	}

	/**
	 * GUI ��@���[���R�}���h�ŊO����X���b�h���~�߂鎞�Ɏg�p
	 * @param className�@
	 */
	public void threadStop(String className){
		if(className.equals(SubjectTextGet.class.getCanonicalName())){
			// TODO �X���b�h���ŗ�O�̃L���b�`���䂪�K�v
			subjectGetThread.interrupt();
		}

	}


	public void threadReStart(){

	}
}
