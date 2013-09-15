package jp.co.omega11.universal.util.net.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.omega11.universal.util.UniversalUtil;
import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.webcrawler.w2fj.model.HttpOptionSetModel;


/**
 * [HTTPクライアント改] ダウンローダーとして扱いやすくしたHTTPクライアント クラスです
 *
 * @author Wizard1
 *
 */

public class HttpCliantKai {

	// ファイル書き込み時のバッファリングを設定 Windowsの場合ALUに合わせたほうがベター?
	public static int FILE_WRITE_BYTE_SIZE = 4096;

	// ファイルを上書きしない 新規に作成
	public static final int FILE_WRITE_OPTION_NEWFILE = 0;

	// ファイルを上書きする シークなし
	public static final int FILE_REWRITE_NOSEEK = 1;

	// ファイルを上書きする　シークあり
	public static final int FILE_REWRITE_SEEK = 2;


	public HttpCliantKai() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * GET /news/dat/1239535870.dat HTTP/1.1
	 *
	 * If-Modified-Since: Sun, 12 Apr 2009 13:18:18 GMT　★必要　long
	 *
	 * Host: tsushima.2ch.net
	 *
	 * Accept: text/html,
	 *
	 * User-Agent: Monazilla/1.00 (JaneView/0.1.12.1)　★必要
	 *
	 * Range: bytes=214944-　★必要 long
	 *
	 *
	 * IF_MODIFIED_SINCE
	 */

	// TODO タイムアウトはsetConnectTimeout

	/**
	 * HTTPヘッダーを作成します w2fjではXMLに書くことも想定　<HTTPオプション>値</HTTPオプション>
	 * <User-Agent>Mogera Super1</User-Agent>
	 */
	public static Map<String, String> createHeader(String userAgent, long range, long ifmodified) {
		//Range: bytes=214944-

		String stRange = "bytes=" + String.valueOf(range) +  "-";

		Map<String, String> map = new HashMap<String, String>();
		map.put("User-Agent", userAgent);

		if (0 < range) {
			map.put("Range", stRange);
		}

		//TODO Accept-Encoding: gzip,deflate
		map.put("Accept-Encoding", "gzip,deflate");

		if (ifmodified != 0) {

			Date date = new Date(ifmodified);

			String stMidified = ""; // TODO Calenderかなんかで整形　2chの場合アメリカ時間にしないとだめ？

			map.put("If-Modified-Since", stMidified);
		}

		return map;

	}

	/**
	 * 指定したＵＲＬをダウンロードします
	 * HttpURLConnectionに対する設定値がないバージョン
	 * @param httpUrl
	 * @param outFolder
	 * @param fileName
	 * @param fileReWriteOption
	 * @param map
	 * @return
	 */
	public static HttpHeaderSave download(String httpUrl, String outFolder, String fileName, int fileReWriteOption,
			Map<String, String> map)  {
		return download(httpUrl,outFolder, fileName,fileReWriteOption, map,null);
	}



	/**
	 *downloadメソッドのフルオプション
	 *指定したURLをダウンロードして指定したフォルダーに指定したファイル名のファイルを作成します
	 *ファイルがすでに存在しているなら末尾からバイトを足して更新します HTTPサーバーからの返答は返却クラスに格納します
	 *
	 * @param httpUrl
	 * @param outFolder
	 * @param fileName
	 * @param fileReWriteOption
	 * @param map
	 * @param httpOptionSetModel
	 * @return
	 */
	public static HttpHeaderSave download(String httpUrl, String outFolder, String fileName, int fileReWriteOption,
			Map<String, String> map, HttpOptionSetModel httpOptionSetModel)  {
		HttpHeaderSave httpHeaderSave = new HttpHeaderSave();

		int num;

		byte buf[] = new byte[FILE_WRITE_BYTE_SIZE];
		RandomAccessFile randomAccessFile = null;

		try {

			Loger.print(httpUrl);
			URL url = new URL(httpUrl);
			HttpURLConnection connect = (HttpURLConnection) url
					.openConnection();

			// HttpURLConnectionへの設定
			if(httpOptionSetModel != null){
				connect.setConnectTimeout(httpOptionSetModel.getConnectTimeout());
				connect.setReadTimeout(httpOptionSetModel.getReadTimeout());
			}

			// HTTPオプション設定
			if (map != null) {
				for (String s : map.keySet()) {

					connect.setRequestProperty(s, map.get(s));

					Loger.print(s + " : " + map.get(s));
				}
			}

			connect.connect();

			// TODO 末尾に/があればつけないようにする
			String fileFullPath = outFolder + "/"
					+ fileName;


			Loger.print("HTTP-Kai File作成  " + fileFullPath);
			File file = new File(fileFullPath);



			// ファイルを作成する、もし存在するならfalse
			boolean fileExist = file.createNewFile();

			randomAccessFile = new RandomAccessFile(fileFullPath, "rw");

			// ファイルがすでにある場合

			// TODO ファイルオプションの制御
			if (!fileExist) {

				/*
				if (!(file.length() < connect.getContentLength())) {

					Loger.print("ローカルファイルサイズよりサーバー上のファイルのサイズが大きくないため終了");

					return null;
				}*/

				if (fileReWriteOption == FILE_REWRITE_SEEK) {
					// ファイルをシークして移動
					randomAccessFile.seek(file.length());
				}
			}

			InputStream di = connect.getInputStream();
			while ((num = di.read(buf)) != -1) {

				randomAccessFile.write(buf, 0, num);

			}

			randomAccessFile.close();

			httpHeaderSave.setResponshash(connect.getHeaderFields());
			httpHeaderSave.printRespoons();

			httpHeaderSave.setContentLength(connect.getContentLength());
			httpHeaderSave.setLastModified(connect.getLastModified());
			httpHeaderSave.setResponseCode(connect.getResponseCode());

			Loger.print("Respons  " + httpHeaderSave.getResponseCode());
			Loger.print("Length  " + httpHeaderSave.getContentLength());
			Loger.print("Mofified  " + httpHeaderSave.getLastModified());



		}
		catch (Throwable e) {

			if(randomAccessFile != null ){
				try {
					randomAccessFile.close();
				} catch (IOException e1) {
					Loger.print(e1);
				}
			}


			Loger.print(e);
			httpHeaderSave.setException(true);
			httpHeaderSave.setExceptionMsg(e.getLocalizedMessage());
		}

		return httpHeaderSave;
	}


	/**
	 * 指定したURLをダウンロードして指定したフォルダーにファイルを作成します ファイル名はURLの末尾の名前になります
	 * ファイルがすでに存在しているなら末尾からバイトを足して更新します HTTPサーバーからの返答は返却クラスに格納します
	 *
	 *
	 * @param httpUrl
	 *            　URL
	 * @param outFolder
	 *            ファイル保存ディレクトリ
	 * @param map
	 *            HTTP 送信ヘッダーを保持するマップ
	 */
	public static HttpHeaderSave download(String httpUrl, String outFolder, int fileReWriteOption,
			Map<String, String> map) {

		String fileName = UniversalUtil.urlLastElement(httpUrl);

		return download(httpUrl, outFolder, fileName, fileReWriteOption, map);
	}

	/**
	 * URLのリストを読み込みすべてダウンロードします [HTTPオプション固定]
	 *
	 * @param urls
	 * @param outFolder
	 * @param map
	 */
	public static List<HttpHeaderSave> download(List<String> urls,
			String outFolder, int fileReWriteOption, Map<String, String> map) {

		List<HttpHeaderSave> HttpHeaderSaveList = new ArrayList<HttpHeaderSave>();

		for (String url : urls) {
			HttpHeaderSaveList.add(download(url, outFolder, fileReWriteOption, map));
		}

		return HttpHeaderSaveList;

	}

	/**
	 * URLのリストを読み込みすべてダウンロードします [HTTPオプション個別リスト]
	 * @param map
	 * @param outFolder
	 * @return
	 */
	public static List<HttpHeaderSave> download(
			List <HttpOptionAndUrl> list, String outFolder, int fileReWriteOption) {
		List<HttpHeaderSave> httpHeaderSaveList = new ArrayList<HttpHeaderSave>();


		for (HttpOptionAndUrl httpOptionAndUrl: list) {
			httpHeaderSaveList.add(download(httpOptionAndUrl.getUrl(), outFolder, fileReWriteOption,httpOptionAndUrl.getHttpOptionMap()));
		}

		return httpHeaderSaveList;

	}

	/**
	 * URLリストの載ったファイルをしていしてString配列を作成します
	 *
	 * @param fileName
	 * @return
	 */
	public List<String> urlListCreate(String fileName) {
		List<String> urlList = new ArrayList<String>();
		return urlList;

	}

}
