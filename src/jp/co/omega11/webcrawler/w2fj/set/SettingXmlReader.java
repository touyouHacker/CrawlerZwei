package jp.co.omega11.webcrawler.w2fj.set;


import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jp.co.omega11.universal.util.log.Loger;
import jp.co.omega11.webcrawler.w2fj.model.IndividualSetModel;


import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 設定XMLを読みこむクラス
 * TODO JXPathを使用して改造すること
 * @author Wizard1 2009
 *
 */
public class SettingXmlReader {

	public SettingXmlReader() {
		// TODO Auto-generated constructor stub
	}

	public void load(Setting set) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(new FileInputStream(
					set.getCurrentDirectory()+set.getSettingFilename()));


			// RemoteControlを設定します
			parseRemoteControl(doc, set);

			Node connection = doc.getFirstChild();
			NodeList childs = connection.getChildNodes();

			String URL = null;
			String Password = null;
			String User = null;

			String ITAURL = null;

			for (int i = 0; i < childs.getLength(); i++) {
				Node node = childs.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String tag = node.getNodeName();

					/* ----------------- DB ------------------------------ */
					if (tag.equals("Database")) {

						NodeList dbList = node.getChildNodes();

						for (int x = 0; x < dbList.getLength(); x++) {

							Node dbnode = dbList.item(x);

							if (dbnode.getNodeType() == Node.ELEMENT_NODE) {

								tag = dbnode.getNodeName();
								if (tag.equals("URL")) {
									URL = dbnode.getTextContent();

								}
								if (tag.equals("User")) {
									User = dbnode.getTextContent();
								}
								if (tag.equals("Password")) {
									Password = dbnode.getTextContent();
								}
							}
						}

					}

					/*
					 * ----------------- Nichannel
					 * ------------------------------
					 */
					if (tag.equals("Nichannel")) {

						NodeList dbList = node.getChildNodes();

						for (int x = 0; x < dbList.getLength(); x++) {

							Node dbnode = dbList.item(x);

							if (dbnode.getNodeType() == Node.ELEMENT_NODE) {

								tag = dbnode.getNodeName();
								if (tag.equals("ITA")) {

									NodeList ccList = dbnode.getChildNodes();

									for (int y = 0; y < ccList.getLength(); y++) {

										Node ccnode = ccList.item(y);

										if (ccnode.getNodeType() == Node.ELEMENT_NODE) {

											tag = ccnode.getNodeName();
											if (tag.equals("URL")) {
												ITAURL = ccnode
														.getTextContent();

											}

										}
									}
								}
							}

						}
					}
					/* ----------------- Nichannel ------------------------------*/

					if (tag.equals("SubjectGet")) {
						parseInvTag(node.getChildNodes(), tag, set.getInvSubject());
					}

					if (tag.equals("DatDownloder")) {
						parseInvTag(node.getChildNodes(), tag ,set.getInvDat());
					}


					if (tag.equals("ContentsDownloder")) {
						parseInvTag(node.getChildNodes(), tag, set.getInvContents());
					}
				}
			}

			Loger.print(URL);
			Loger.print(User);
			Loger.print(Password);
			Loger.print(ITAURL);

			set.setDbPass(Password);
			set.setDbURL(URL);
			set.setDbUser(User);
			set.setURL(ITAURL);

		} catch (Exception e) {
			Loger.print(e);
		}
	}

	/**
	 * IndividualSet タグを設定します
	 * @param nodeList
	 * @param tag
	 * @param inv
	 */
	private void parseInvTag(NodeList nodeList, String tag, IndividualSetModel inv){


		String LoopTimeString = null;

			for (int x = 0; x < nodeList.getLength(); x++) {

				Node node = nodeList.item(x);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					tag = node.getNodeName();
					if (tag.equals("SaveDirectory")) {
						inv.setSaveDownload(node.getTextContent());
					}

					if (tag.equals("LoopTimeMinute")) {
						LoopTimeString = node.getTextContent();
						Long in = new Long(LoopTimeString);
						 inv.setSleepTime(in.longValue() * 60000);
					}

					if (tag.equals("HTTP")) {
						NodeList ccList = node.getChildNodes();

						for (int y = 0; y < ccList.getLength(); y++) {

							Node ccnode = ccList.item(y);

							if (ccnode.getNodeType() == Node.ELEMENT_NODE) {

								tag = ccnode.getNodeName();
								if (tag.equals("UserAgent")) {
									inv.getHttp().setUserAgent(ccnode.getTextContent());
								}

								if (tag.equals("ReTryDownload")) {
									inv.getHttp().setReTryDownload(Integer.parseInt(ccnode.getTextContent()));
								}

								if (tag.equals("ConnectTimeout")) {
									inv.getHttp().setConnectTimeout(Integer.parseInt(ccnode.getTextContent()));
								}

								if (tag.equals("ReadTimeout")) {
									inv.getHttp().setReadTimeout(Integer.parseInt(ccnode.getTextContent()));
								}
							}
						}
					}
				}
			}
		}

	/**
	 * RemoteControlを設定します
	 * JXPathContextを使用してます
	 * TODO JXPathContextのラッパーは必要
	 *
	 * @param document
	 * @param set
	 */
	private void parseRemoteControl(Document document, Setting set) {
		JXPathContext context = JXPathContext.newContext(document);


		set.getSmtpModel().setMailToAdress( (String)context.getValue("/W2FSET/RemoteControl/Mail/ReceptMailAdress"));
		set.getPop3Model().setReceiveAdress( (String)context.getValue("/W2FSET/RemoteControl/Mail/ReceptMailAdress"));
		set.setMailSleepTime( Long.valueOf(  (String)context.getValue("/W2FSET/RemoteControl/Mail/MailSleepTime")));
		set.setReceiveProtocol( (String)context.getValue("/W2FSET/RemoteControl/Mail/ReceiveMailProtocol"));


		set.getSmtpModel().setHost( (String)context.getValue("/W2FSET/RemoteControl/Mail/SMTP/Host"));
		set.getSmtpModel().setPort( Integer.valueOf( (String)context.getValue("/W2FSET/RemoteControl/Mail/SMTP/Port")));
		set.getSmtpModel().setUsername( (String)context.getValue("/W2FSET/RemoteControl/Mail/SMTP/UserName"));
		set.getSmtpModel().setPassword( (String)context.getValue("/W2FSET/RemoteControl/Mail/SMTP/Password"));

		if ("1".equals(context.getValue("/W2FSET/RemoteControl/Mail/SMTP/SSL"))) {
			set.getSmtpModel().setSsl(true);
		}

		set.getPop3Model().setHost( (String)context.getValue("/W2FSET/RemoteControl/Mail/POP/Host"));
		set.getPop3Model().setPort( Integer.valueOf( (String)context.getValue("/W2FSET/RemoteControl/Mail/POP/Port")));
		set.getPop3Model().setUsername( (String)context.getValue("/W2FSET/RemoteControl/Mail/POP/UserName"));
		set.getPop3Model().setPassword( (String)context.getValue("/W2FSET/RemoteControl/Mail/POP/Password"));

		if ("1".equals(context.getValue("/W2FSET/RemoteControl/Mail/POP/SSL"))) {
			set.getPop3Model().setSsl(true);
		}

		// TODO IMAPは未実装
	}

}
