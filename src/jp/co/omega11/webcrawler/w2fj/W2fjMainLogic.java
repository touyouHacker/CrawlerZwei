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
 * W2FJ[Watch 2ch For Java]のすべてを管理するルートクラス
 * このクラスでは１板しか処理できないため
 * 複数の板を処理する場合、このクラスを複数よびだすよう呼び出しクラスで実装する
 * 実際の想定では1板1プロセスとして実装、テストを行っている。
 *
 * 1プロセスで複数このクラスがよばれても競合することは処理的にはないが設定ファイルの記述によっては
 * 時間が重なって処理がブッキングしたりする可能性はある。
 * また出力フォルダが重なるので同じ板を同時に複数よばないこと（起動スレッドを制限すればブッキングはおこらない）
 *
 * @author Wizard1
 *
 */
public class W2fjMainLogic {

	/**
	 * コアとなるスレッドクラス群
	 * スレッド停止、再起動を制御するためフィールドにもつ
	 */
	private SubjectTextGetThread subjectGetThread;
	private DatDownloderThread datDownloderThread;
	private ContentsDownloderThread contentsDownloderThread;
	private List <ReceiveCommandThread> receiveCommandThreads = new ArrayList<ReceiveCommandThread>();

	// falseが初期値
	private boolean subjectGetThread_ExeFlag;
	private boolean datDownloderThread_ExeFlag;
	private boolean contentsDownloderThread_ExeFlag;
	private boolean noThread;

	// コマンド受付機構のスレッド実行可否フラグ
	// TODO とりあえずTrue
	private boolean controlFromConsole_ExeFlag = true;
	private boolean controlFromMail_ExeFlag;

	public W2fjMainLogic() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 起動メソッド
	 * @param args
	 */
	public void start(String args[]) {
		/**
		 * スレッドコントロールメインクラス 各スレッドは設定された時間間隔で起動する 各スレッドは同期をとらずそれぞれDB値を見て処理を行う
		 * つまり各スレッドは独立して動いており順番は基本的に存在しない
		 *
		 * コネクションは各スレッドで１つづつ保持する
		 *
		 *　途中終了の処理、異常終了、DBのリカバリといった機能は後から追加するとする
		 *
		 * マルチプラットフォームを対照とし Windows Linux MacOSX でバッチとして動作できるよう作る
		 * よってDBソフトもこの環境で動くものをターゲットとする マルチプラットフォームで動かないJAVA ライブラリは使わない
		 * GUIはEclipseGUI or QT or SWTを使う
		 *
		 * ※文字コード ※ディレクトリ、ファイルなどのパス
		 *
		 * などは注意する
		 *
		 * .NET版も作れるようJAVA固有の書き方はなるべくさける InterfaceでＪＡＶＡクラスを抽象化してインターフェースメソッドを使用
		 *
		 * DB候補
		 * ☆Apache Derby
		 * ☆MySQl
		 * ☆PostgreSQL
		 * ☆Oracle11g
		 * ☆http://ja.wikipedia.org/wiki/SQLite
		 * ☆PlainFile
		 *
		 *
		 * 各スレッドは別マシンからでも起動できるようにする 分散処理
		 *
		 *
		 * スレッドセーフメソッド、クラスかどうかを気にして作ってないので後で精査
		 *
		 * これを拡張して WebHyperSpliderをつくれるように Amazonの画像収集とかアップローダークローラーとか
		 *
		 * ターゲットはニュース速報および+ 実況板
		 *
		 * [テスト未実施]
		 * ・DBサーバーを実行ファイルと別端末にしての運用
		 * ・コネクションの保持の有効性
		 *
		 * [未実装、将来実装希望]
		 * ・外部ストレージに画像を転送できるオプションをつける
		 *   DAOで　スタート時期から最終日までを選択してその間の画像をストレージドライブの指定されたフォルダにつっこむ
		 *   　電車とかでＰＳＰで画像を確認できるので便利
		 *   　画像のリサイズは後から対応
		 * ・★↑そのためには現在おきてるファイルハンドルの無解放問題を解決する必要がある
		 * ・情報モデルはちゃんとnewする（単体スレッドのとき他のスレッド情報取得でおちないように）
		 * ・Remote単体起動のとき（他のスレッドは別プロセス）どうやって情報をとるか
		 * ・POP該当メールデリート[実装テスト中]
		 * ・起動時間を遅らせる　引数で起動待機時間を分、時間で設定できるように(時間ユーテル必要)
		 * ・IMAP
		 * ・例外のときに発生時間と引数の表示
		 * ・SQLのバインド変数のログ表示
		 * ・javadocの整頓
		 * ・デプロイ機構、動的アップデート
		 * ・板一覧の取得
		 * ・2chの状態をチェックするため接続テスト pingはルーターではじかれることが多いので不可
		 * ・ファイル取得数、失敗数、などのロギング
		 * ・サーバー管理機能
		 * ・ＧＵＩ画面
		 * ・タスクの時間を記録してグラフ化する機能→メールでも見られるように
		 * ・２ｃｈ Userログイン機能
		 * ・ＵＲＬフィルター　ブラクラ機能
		 * ・ＵＲＬ重複の排除
		 * ・ようつべ、ニコニコ対応
		 * ・ＨＯＳＴマスタテーブル→Webクローラーのため
		 * ・JDBCコネクションのリカバリ
		 * ・ノードダウロード機能
		 * ・コンテンツダウンローダーの複数スレッド起動管理
		 * ・複数プロセスでw2fjを起動したときの集中管理機能[GUI/PHP? とにかく１クラスは必要]
		 * ・コネクションマネージャー(getconnectionをクラスで管理し、ＤＢ１がアクセスできないときＤＢ２へアクセスするようにする)
		 * 　そのさいDB1とＤＢ２の同期はＡＰでは保証しない、ＡＰで同期がとれるような処理や常に二重アクセスするオプションは必要
		 * ・外部システムへのDBレコードのコピー（APサーバーを外部公開するために必要,FTPでSQLを送信する・・など)
		 *  	大かがりなシステムでＡＰ自体はPHP/RoRで構築する
		 * ・CELL TVみたいにコンテンツ（スレ名）ごとに画像検索できるようにするJavaFX
		 *
		 *
		 * [実装済み]
		 *　・
		 *  ・
		 * [テスト済み]
		 * ・2chが落ちたときもＷＡＩＴするだけで落ちないこと
		 * ・4週間の連続稼働
		 * ・電源が壊れた後、再起動後起動しても後続処理が行えたこと。運良くＤＢアクセスしていたタイミングでなかっただけだが
		 *
		 */

		Setting set = new Setting();



		// 引数をパースして設定ファイル名を選択
		argumentParser(args, set);

		try {
			set.setInitialize(set);
		} catch (Exception e) {
			Log.print("初期化処理失敗");
			Log.print(e);
			return;
		}

		RootInfo rootInfo = new RootInfo(this.getClass().getSimpleName(), set.getItaname());

		if (subjectGetThread_ExeFlag) {
			// subject.txtGet スレッド起動 subject.txtをダウンロードしてパース→スレッドDB登録
			subjectGetThread = new SubjectTextGetThread(set, rootInfo.getSubjectInfo());
			subjectGetThread.start();
		}

		if (subjectGetThread_ExeFlag && datDownloderThread_ExeFlag) {

			// テーブルの整合性をたもつため60秒まつ
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.print(e);
			}
		}

		if (datDownloderThread_ExeFlag) {
			// DATダウンロードスレッド起動→スレッドtableを見て必要なのをダウンロード→画像URL抽出→画像DB登録まで
			// DatDownloader
			datDownloderThread = new DatDownloderThread(set , rootInfo.getDatInfo());
			datDownloderThread.start();
		}

		if (contentsDownloderThread_ExeFlag) {
			// 画像ダウンロードスレッド起動→画像tableを見て→画像ダウンロード→DBのレコードをダウンロード済みにUPDATE
			// ContentsDownloader
			contentsDownloderThread = new ContentsDownloderThread(set , rootInfo.getContentsInfo());
			contentsDownloderThread.start();
		}

		// 外部コントロール コンソールやMailなど[複数可能]
		if (controlFromConsole_ExeFlag) {
			receiveCommandThreads.add (new ReceiveCommandThread(
					new ControlFromConsole(), new ExecuteCommandForW2fj(rootInfo)));
		}

		if(controlFromMail_ExeFlag) {

			//メール受信で必要な初期化
			set.setRemoteControlForMail();

			receiveCommandThreads.add (new ReceiveCommandThread(
					new ControlFromMail(set.getPop3Model(), set.getSmtpModel(), set.getMailSleepTime()) , new ExecuteCommandForW2fj(rootInfo)));
		}


		for (ReceiveCommandThread receiveCommandThread : receiveCommandThreads) {
			receiveCommandThread.start();
		}

		// ＤＢのバッチ刈り取り 削除フラグレコードの履歴テーブルへの移行
		// DBBatchLogic

	}

	/**
	 * コマンドライン引数をパースして設定オブジェクトのデフォルト値を上書きします
	 * 設定はすべてXMLファイルで行うためここで設定できるのは設定ファイル名のみ
	 *
	 * あとは起動するスレッドを選択できるようにする
	 *
	 * @param args
	 * @param set
	 */
	private void argumentParser(String args[], Setting set) {
		/**
		 * 起動引数で起動するスレッドを限定する
		 *
		 * -S Subjecttextのみ
		 * -D DatDownloderのみ
		 * -C ContentDownloderのみ
		 *
		 * -S -D (-S/-Dのみ)
		 * -ALL デフォルト、すべて起動
		 *
		 * -NT No Thread （デバッグ用、起動プロセスや受信機構をデバッグしたいときに使用）
		 * 	   スレッドコンポーネントを起動しません
		 *
		 * -F 設定ＸＭＬファイル名を指定
		 *
		 * -O 外部制御機構を指定します(複数可能)
		 *    -Console
		 *    -Mail
		 *    -Gui
		 *    -TCP [TCP Server]
		 *
		 *
		 * -Re Reboot 再起動します　新規プロセスを起動し　現在プロセスを終了します
		 * -WS WaitStart 起動開始までのウェイト秒数を指定します　再起動用
		 * -Shutdown 終了します
		 * -fexit Forse Exit 強制終了します
		 *
		 */
		for(int i=0;i<args.length;i++){
			if("-F".equals(args[i])){
				set.setSettingFilename(args[i+1]);
				// 設定ファイル名が次の引数なので加算
				i++;
			}

			if("-S".equals(args[i])) {
				Log.print("-S・・subjectGetThreadを起動ONにします");
				subjectGetThread_ExeFlag = true;
			}

			if("-D".equals(args[i])) {
				Log.print("-D・・datDownloderThread_ExeFlagを起動ONにします");
				datDownloderThread_ExeFlag = true;
			}

			if("-C".equals(args[i])) {
				Log.print("-C・・contentsDownloderThread_ExeFlagを起動ONにします");
				contentsDownloderThread_ExeFlag = true;
			}

			if("-NP".equals(args[i])) {
				Log.print("-NP・・NoParseExe URL抽出→DB登録を初回ループ時に実行しません");
				set.setNoParseExe(true);
			}


			if("-NT".equals(args[i])) {
				Log.print("-NT・・NoThread スレッドコンポーネントを起動しません。初期プロセスのみ実施");
				noThread = true;
			}


			if("-O".equals(args[i])){

				if("-Console".equals(args[i+1])){
					controlFromConsole_ExeFlag = true;
				}
				else if ("-Mail".equals(args[i+1])) {
					/**
					 * Mailの受信機構
					 * メールの件名に板名
					 * 本文にコマンドを格納すると返信をします
					 */
					controlFromMail_ExeFlag = true;
				}

				// 設定ファイル名が次の引数なので加算
				i++;
			}

		}

		if (subjectGetThread_ExeFlag == false &&
				datDownloderThread_ExeFlag == false &&
				contentsDownloderThread_ExeFlag == false &&
				noThread == false) {
			// すべてfalseの場合は起動スレッド指定無し= ALL起動とみなす

			Log.print("起動スレッドが指定されてないため、すべて起動します。");
			subjectGetThread_ExeFlag = true;
			datDownloderThread_ExeFlag = true;
			contentsDownloderThread_ExeFlag = true;
		}

	}

	/**
	 * GUI や　メールコマンドで外からスレッドを止める時に使用
	 * @param className　
	 */
	public void threadStop(String className){
		if(className.equals(SubjectTextGet.class.getCanonicalName())){
			// TODO スレッド側で例外のキャッチ制御が必要
			subjectGetThread.interrupt();
		}

	}


	public void threadReStart(){

	}
}
