package jp.co.omega11.webcrawler.w2fj.component.contents;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;

import jp.co.omega11.universal.util.UniversalUtil;
import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.webcrawler.w2fj.component.AbstrarctComponentThread;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.ContentsComponentInfo;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.ThreadComponentInfo;
import jp.co.omega11.webcrawler.w2fj.set.Setting;


public class ContentsDownloderThread extends AbstrarctComponentThread {



	/*
	 * ���Q�̃X���b�h�N���X�ƈႢ�����ŃX���b�h�𕡐��N�������� ContentsDownloder���Ō�ɋN��������΂Ƃ肠�����\��
	 */

	public ContentsDownloderThread(Setting _setting, ThreadComponentInfo _threadComponentInfo) {
		super(_setting, _threadComponentInfo);
		this.threadComponentInfo.init(this.getClass().getSimpleName(),
				Thread.currentThread(), new ContentsComponentInfo());
		this.setName(this.getClass().getSimpleName());
	}

	// PRE�Ń_�E�����[�h���ׂ��X����get
	// ���̌�S�������x�Ƀ��X�g���킯�Ċe�X���b�h�Ń_�E�����[�h���s
	@Override
	public void run() {
		try {
			// �R�l�N�V�����̊m�� (1�X���b�h1�R�l�N�V�����j
			con = DriverManager.getConnection(setting.getDbURL(), setting
					.getDbUser(), setting.getDbPass());
		} catch (SQLException e) {
			Loger.print("[" + this.getClass().getName() + "] DB�ڑ����s�@�� "
					+ e.toString());
		}

		// �����N���X�쐬
		ContentsDownloder contentsDownloder = new ContentsDownloder(setting,
				con, (ContentsComponentInfo)threadComponentInfo.getComponentInfo());

		int loopCount = 0;

		threadComponentInfo.setStartTime(Calendar.getInstance());

		while (true) {

			loopCount++;
			threadComponentInfo.setThreadLoopCount(loopCount);

			threadComponentInfo.setLastWorkTime(Calendar.getInstance());

			// �����f�[�^�擾
			if (!contentsDownloder.preExecute()) {
				// �@�����f�[�^���Ȃ��̂őҋ@(DAT�R���|�[�l���g�����삷��̂�҂�)

				Loger.print("[ContentsDown] �ҋ@���[�h�ֈڍs���܂� ,"
						+ setting.getItaname() + " " + loopCount);

				Loger.print("[ContentsDown] " + UniversalUtil.nowDate());

				// �Ȃ����Sleep
				try {
					Thread.sleep(setting.getInvContents().getSleepTime());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Loger.print(e);
					// �������[�v�͒v���I�Ȃ̂�return
					return;
				}

			} else {

				// �����t�q�k�����܂�4����
				// �X���b�h��4�ғ�

				// �����f�[�^������Ȃ�_�E�����[�h
				contentsDownloder.execute();

				Runtime runtime = Runtime.getRuntime();

				Loger.print("[ContentsDown] �_�E�����[�h���� ," + setting.getItaname()
						+ " " + loopCount);
				Loger.print("�󂫃����� " + runtime.freeMemory());

				Loger.print("[ContentsDown] " + UniversalUtil.nowDate());

				Loger.flushAll();
			}

		}
	}

}
