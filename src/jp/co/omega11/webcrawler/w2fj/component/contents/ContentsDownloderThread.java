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
	 * 他２つのスレッドクラスと違いここでスレッドを複数起動させる ContentsDownloderを最後に起動させればとりあえず可能か
	 */

	public ContentsDownloderThread(Setting _setting, ThreadComponentInfo _threadComponentInfo) {
		super(_setting, _threadComponentInfo);
		this.threadComponentInfo.init(this.getClass().getSimpleName(),
				Thread.currentThread(), new ContentsComponentInfo());
		this.setName(this.getClass().getSimpleName());
	}

	// PREでダウンロードすべきスレをget
	// その後４分割程度にリストをわけて各スレッドでダウンロード実行
	@Override
	public void run() {
		try {
			// コネクションの確立 (1スレッド1コネクション）
			con = DriverManager.getConnection(setting.getDbURL(), setting
					.getDbUser(), setting.getDbPass());
		} catch (SQLException e) {
			Loger.print("[" + this.getClass().getName() + "] DB接続失敗　→ "
					+ e.toString());
		}

		// 処理クラス作成
		ContentsDownloder contentsDownloder = new ContentsDownloder(setting,
				con, (ContentsComponentInfo)threadComponentInfo.getComponentInfo());

		int loopCount = 0;

		threadComponentInfo.setStartTime(Calendar.getInstance());

		while (true) {

			loopCount++;
			threadComponentInfo.setThreadLoopCount(loopCount);

			threadComponentInfo.setLastWorkTime(Calendar.getInstance());

			// 処理データ取得
			if (!contentsDownloder.preExecute()) {
				// 　処理データがないので待機(DATコンポーネントが動作するのを待つ)

				Loger.print("[ContentsDown] 待機モードへ移行します ,"
						+ setting.getItaname() + " " + loopCount);

				Loger.print("[ContentsDown] " + UniversalUtil.nowDate());

				// なければSleep
				try {
					Thread.sleep(setting.getInvContents().getSleepTime());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Loger.print(e);
					// 無限ループは致命的なのでreturn
					return;
				}

			} else {

				// 処理ＵＲＬをうまく4分割
				// スレッドを4つ稼働

				// 処理データがあるならダウンロード
				contentsDownloder.execute();

				Runtime runtime = Runtime.getRuntime();

				Loger.print("[ContentsDown] ダウンロード完了 ," + setting.getItaname()
						+ " " + loopCount);
				Loger.print("空きメモリ " + runtime.freeMemory());

				Loger.print("[ContentsDown] " + UniversalUtil.nowDate());

				Loger.flushAll();
			}

		}
	}

}
