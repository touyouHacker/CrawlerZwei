package jp.co.omega11.webcrawler.w2fj.component.subject;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;

import jp.co.omega11.universal.util.UniversalUtil;
import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.webcrawler.w2fj.component.AbstrarctComponentThread;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.SubjectComponentInfo;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.ThreadComponentInfo;
import jp.co.omega11.webcrawler.w2fj.set.Setting;

public class SubjectTextGetThread extends AbstrarctComponentThread {

	/**
	 * �R���X�g���N�^
	 * @param _setting
	 * @param threadComponentInfo
	 */
	public SubjectTextGetThread(Setting _setting,
			ThreadComponentInfo _threadComponentInfo) {
		super(_setting, _threadComponentInfo);
		this.threadComponentInfo.init(this.getClass()
				.getSimpleName(), Thread.currentThread(), new SubjectComponentInfo());
		this.setName(this.getClass().getSimpleName());
	}

	/**
	 * �X���b�h�����s���܂�
	 */
	@Override
	public final void run() {
		try {
			// �R�l�N�V�����̊m�� (1�X���b�h1�R�l�N�V�����j
			con = DriverManager.getConnection(setting.getDbURL(), setting
					.getDbUser(), setting.getDbPass());
		} catch (SQLException e) {
			Loger.print("[SUBJECT] DB�ڑ����s�@�� " + e.toString());
		}

		int loopCount = 0;

		// �����N���X�쐬
		SubjectTextGet stg = new SubjectTextGet(setting, con,
				(SubjectComponentInfo)threadComponentInfo.getComponentInfo());

		// TODO
		// �R�l�N�V�������؂ꂽ�Ƃ��ēx�R�l�N�V�������Ƃ�ɂ����������K�v

		threadComponentInfo.setStartTime(Calendar.getInstance());

		while (true) {

			try {

				// subject�_�E�����[�h
				stg.download();
				// DB�o�^
				stg.regist();

				loopCount++;
				threadComponentInfo.setThreadLoopCount(loopCount);
				threadComponentInfo.setLastWorkTime(Calendar.getInstance());

				Loger.print(UniversalUtil.nowDate());

				Runtime runtime = Runtime.getRuntime();

				Loger.print("[SUBJECT] �ҋ@���[�h�ֈڍs���܂� ," + setting.getItaname()
						+ " " + loopCount);

				Loger.print("�󂫃����� " + runtime.freeMemory());

				Loger.flushAll();

				Thread.sleep(setting.getInvSubject().getSleepTime());
			} catch (Exception e) {

				Loger.print(e);

				// ��O����������Ƃ肠�����I��
				return;

			}
		}

	}
}
