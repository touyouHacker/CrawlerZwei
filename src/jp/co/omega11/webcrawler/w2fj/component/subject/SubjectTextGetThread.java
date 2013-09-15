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
	 * コンストラクタ
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
	 * スレッドを実行します
	 */
	@Override
	public final void run() {
		try {
			// コネクションの確立 (1スレッド1コネクション）
			con = DriverManager.getConnection(setting.getDbURL(), setting
					.getDbUser(), setting.getDbPass());
		} catch (SQLException e) {
			Loger.print("[SUBJECT] DB接続失敗　→ " + e.toString());
		}

		int loopCount = 0;

		// 処理クラス作成
		SubjectTextGet stg = new SubjectTextGet(setting, con,
				(SubjectComponentInfo)threadComponentInfo.getComponentInfo());

		// TODO
		// コネクションが切れたとき再度コネクションをとりにいく処理が必要

		threadComponentInfo.setStartTime(Calendar.getInstance());

		while (true) {

			try {

				// subjectダウンロード
				stg.download();
				// DB登録
				stg.regist();

				loopCount++;
				threadComponentInfo.setThreadLoopCount(loopCount);
				threadComponentInfo.setLastWorkTime(Calendar.getInstance());

				Loger.print(UniversalUtil.nowDate());

				Runtime runtime = Runtime.getRuntime();

				Loger.print("[SUBJECT] 待機モードへ移行します ," + setting.getItaname()
						+ " " + loopCount);

				Loger.print("空きメモリ " + runtime.freeMemory());

				Loger.flushAll();

				Thread.sleep(setting.getInvSubject().getSleepTime());
			} catch (Exception e) {

				Loger.print(e);

				// 例外発生したらとりあえず終了
				return;

			}
		}

	}
}
