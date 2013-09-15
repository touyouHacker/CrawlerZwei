/**
 *
 */
package jp.co.omega11.universal.util.system;

import java.util.Properties;

/**
 * @author Wizard1 2009
 *
 * Javaの環境変数を取り扱うクラスです
 */
public class JavaEnviroment {

	public static Properties properties = null;

	public static String line = null;


	// Javaのプロパティクラスをフィールドに読み込みます
	public static void initJavaProperties () {

		if (properties == null ) {
			properties = System.getProperties();
		}


		line = properties.getProperty("line.separator");
	}

}

