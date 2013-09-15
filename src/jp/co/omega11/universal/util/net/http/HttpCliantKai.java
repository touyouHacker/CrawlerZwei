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
 * [HTTP�N���C�A���g��] �_�E�����[�_�[�Ƃ��Ĉ����₷������HTTP�N���C�A���g �N���X�ł�
 *
 * @author Wizard1
 *
 */

public class HttpCliantKai {

	// �t�@�C���������ݎ��̃o�b�t�@�����O��ݒ� Windows�̏ꍇALU�ɍ��킹���ق����x�^�[?
	public static int FILE_WRITE_BYTE_SIZE = 4096;

	// �t�@�C�����㏑�����Ȃ� �V�K�ɍ쐬
	public static final int FILE_WRITE_OPTION_NEWFILE = 0;

	// �t�@�C�����㏑������ �V�[�N�Ȃ�
	public static final int FILE_REWRITE_NOSEEK = 1;

	// �t�@�C�����㏑������@�V�[�N����
	public static final int FILE_REWRITE_SEEK = 2;


	public HttpCliantKai() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * GET /news/dat/1239535870.dat HTTP/1.1
	 *
	 * If-Modified-Since: Sun, 12 Apr 2009 13:18:18 GMT�@���K�v�@long
	 *
	 * Host: tsushima.2ch.net
	 *
	 * Accept: text/html,
	 *
	 * User-Agent: Monazilla/1.00 (JaneView/0.1.12.1)�@���K�v
	 *
	 * Range: bytes=214944-�@���K�v long
	 *
	 *
	 * IF_MODIFIED_SINCE
	 */

	// TODO �^�C���A�E�g��setConnectTimeout

	/**
	 * HTTP�w�b�_�[���쐬���܂� w2fj�ł�XML�ɏ������Ƃ��z��@<HTTP�I�v�V����>�l</HTTP�I�v�V����>
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

			String stMidified = ""; // TODO Calender���Ȃ񂩂Ő��`�@2ch�̏ꍇ�A�����J���Ԃɂ��Ȃ��Ƃ��߁H

			map.put("If-Modified-Since", stMidified);
		}

		return map;

	}

	/**
	 * �w�肵���t�q�k���_�E�����[�h���܂�
	 * HttpURLConnection�ɑ΂���ݒ�l���Ȃ��o�[�W����
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
	 *download���\�b�h�̃t���I�v�V����
	 *�w�肵��URL���_�E�����[�h���Ďw�肵���t�H���_�[�Ɏw�肵���t�@�C�����̃t�@�C�����쐬���܂�
	 *�t�@�C�������łɑ��݂��Ă���Ȃ疖������o�C�g�𑫂��čX�V���܂� HTTP�T�[�o�[����̕ԓ��͕ԋp�N���X�Ɋi�[���܂�
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

			// HttpURLConnection�ւ̐ݒ�
			if(httpOptionSetModel != null){
				connect.setConnectTimeout(httpOptionSetModel.getConnectTimeout());
				connect.setReadTimeout(httpOptionSetModel.getReadTimeout());
			}

			// HTTP�I�v�V�����ݒ�
			if (map != null) {
				for (String s : map.keySet()) {

					connect.setRequestProperty(s, map.get(s));

					Loger.print(s + " : " + map.get(s));
				}
			}

			connect.connect();

			// TODO ������/������΂��Ȃ��悤�ɂ���
			String fileFullPath = outFolder + "/"
					+ fileName;


			Loger.print("HTTP-Kai File�쐬  " + fileFullPath);
			File file = new File(fileFullPath);



			// �t�@�C�����쐬����A�������݂���Ȃ�false
			boolean fileExist = file.createNewFile();

			randomAccessFile = new RandomAccessFile(fileFullPath, "rw");

			// �t�@�C�������łɂ���ꍇ

			// TODO �t�@�C���I�v�V�����̐���
			if (!fileExist) {

				/*
				if (!(file.length() < connect.getContentLength())) {

					Loger.print("���[�J���t�@�C���T�C�Y���T�[�o�[��̃t�@�C���̃T�C�Y���傫���Ȃ����ߏI��");

					return null;
				}*/

				if (fileReWriteOption == FILE_REWRITE_SEEK) {
					// �t�@�C�����V�[�N���Ĉړ�
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
	 * �w�肵��URL���_�E�����[�h���Ďw�肵���t�H���_�[�Ƀt�@�C�����쐬���܂� �t�@�C������URL�̖����̖��O�ɂȂ�܂�
	 * �t�@�C�������łɑ��݂��Ă���Ȃ疖������o�C�g�𑫂��čX�V���܂� HTTP�T�[�o�[����̕ԓ��͕ԋp�N���X�Ɋi�[���܂�
	 *
	 *
	 * @param httpUrl
	 *            �@URL
	 * @param outFolder
	 *            �t�@�C���ۑ��f�B���N�g��
	 * @param map
	 *            HTTP ���M�w�b�_�[��ێ�����}�b�v
	 */
	public static HttpHeaderSave download(String httpUrl, String outFolder, int fileReWriteOption,
			Map<String, String> map) {

		String fileName = UniversalUtil.urlLastElement(httpUrl);

		return download(httpUrl, outFolder, fileName, fileReWriteOption, map);
	}

	/**
	 * URL�̃��X�g��ǂݍ��݂��ׂă_�E�����[�h���܂� [HTTP�I�v�V�����Œ�]
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
	 * URL�̃��X�g��ǂݍ��݂��ׂă_�E�����[�h���܂� [HTTP�I�v�V�����ʃ��X�g]
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
	 * URL���X�g�̍ڂ����t�@�C�������Ă�����String�z����쐬���܂�
	 *
	 * @param fileName
	 * @return
	 */
	public List<String> urlListCreate(String fileName) {
		List<String> urlList = new ArrayList<String>();
		return urlList;

	}

}
