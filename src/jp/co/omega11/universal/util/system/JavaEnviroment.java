/**
 *
 */
package jp.co.omega11.universal.util.system;

import java.util.Properties;

/**
 * @author Wizard1 2009
 *
 * Java�̊��ϐ�����舵���N���X�ł�
 */
public class JavaEnviroment {

	public static Properties properties = null;

	public static String line = null;


	// Java�̃v���p�e�B�N���X���t�B�[���h�ɓǂݍ��݂܂�
	public static void initJavaProperties () {

		if (properties == null ) {
			properties = System.getProperties();
		}


		line = properties.getProperty("line.separator");
	}

}

