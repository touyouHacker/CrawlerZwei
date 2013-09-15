package jp.co.omega11.webcrawler.w2fj.component.dat;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;

import jp.co.omega11.universal.util.UniversalUtil;
import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.webcrawler.w2fj.component.AbstrarctComponentThread;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.DatComponentInfo;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.ThreadComponentInfo;
import jp.co.omega11.webcrawler.w2fj.set.Setting;


public class DatDownloderThread extends AbstrarctComponentThread {



	public DatDownloderThread(Setting _setting, ThreadComponentInfo _threadComponentInfo) {
		super(_setting, _threadComponentInfo);
		this.threadComponentInfo.init(this.getClass().getSimpleName(),
				Thread.currentThread(),  new DatComponentInfo());
		this.setName(this.getClass().getSimpleName());
	}

	@Override
	public void run() {
		try {
			// �R�l�N�V�����̊m�� (1�X���b�h1�R�l�N�V�����j
			con = DriverManager.getConnection(setting.getDbURL(), setting
					.getDbUser(), setting.getDbPass());
		} catch (SQLException e) {
			Loger.print("[DAT] DB�ڑ����s�@�� " + e.toString());
		}

		// �����N���X�쐬
		DatDownloder datDownloder = new DatDownloder(setting, con, (DatComponentInfo)threadComponentInfo.getComponentInfo());

		int loopCount = 0;

		threadComponentInfo.setStartTime(Calendar.getInstance());

		while (true) {

			try {
				loopCount++;
				threadComponentInfo.setThreadLoopCount(loopCount);

				// Dat�_�E�����[�h
				datDownloder.execute();

				threadComponentInfo.setLastWorkTime(Calendar.getInstance());

				Runtime runtime = Runtime.getRuntime();

				Loger.print("[DAT] �ҋ@���[�h�ֈڍs���܂� ," + setting.getItaname()
						+ " " + loopCount);
				Loger.print("�󂫃����� " + runtime.freeMemory());

				Loger.print(UniversalUtil.nowDate());

				Loger.flushAll();

				// TODO 10�������Ə������̎��Ԃ��l���ł��Ȃ��̂ŏ������Ԃ������������Ԃ��v�Z����
				Thread.sleep(setting.getInvDat().getSleepTime());

			} catch (Exception e) {
				Loger.print("[DAT]��O������");
				Loger.print(e);
				// ��O����������Ƃ肠�����I��
				Loger.print(UniversalUtil.nowDate());

				return;
			}
		}
	}
}
