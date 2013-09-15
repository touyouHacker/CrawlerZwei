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
			// コネクションの確立 (1スレッド1コネクション）
			con = DriverManager.getConnection(setting.getDbURL(), setting
					.getDbUser(), setting.getDbPass());
		} catch (SQLException e) {
			Loger.print("[DAT] DB接続失敗　→ " + e.toString());
		}

		// 処理クラス作成
		DatDownloder datDownloder = new DatDownloder(setting, con, (DatComponentInfo)threadComponentInfo.getComponentInfo());

		int loopCount = 0;

		threadComponentInfo.setStartTime(Calendar.getInstance());

		while (true) {

			try {
				loopCount++;
				threadComponentInfo.setThreadLoopCount(loopCount);

				// Datダウンロード
				datDownloder.execute();

				threadComponentInfo.setLastWorkTime(Calendar.getInstance());

				Runtime runtime = Runtime.getRuntime();

				Loger.print("[DAT] 待機モードへ移行します ," + setting.getItaname()
						+ " " + loopCount);
				Loger.print("空きメモリ " + runtime.freeMemory());

				Loger.print(UniversalUtil.nowDate());

				Loger.flushAll();

				// TODO 10分毎だと処理中の時間を考慮できないので処理時間を加味した時間を計算する
				Thread.sleep(setting.getInvDat().getSleepTime());

			} catch (Exception e) {
				Loger.print("[DAT]例外発生→");
				Loger.print(e);
				// 例外発生したらとりあえず終了
				Loger.print(UniversalUtil.nowDate());

				return;
			}
		}
	}
}
