/**
 * 
 */
package jp.co.omega11.webcrawler.w2fj.dbaccesser.dao.vendor;

import java.sql.Connection;

/**
 * 
 * �x���_�[�Ōʎ������邠��R�[�h�͂�����p�����Ď���
 * 
 * @author Wizard1 2009
 * 
 *
 */
public interface IindividualSQL {

	/**
	 * �e�[�u�������݂��Ă��邩���`�F�b�N���܂�
	 * @return
	 */
	public boolean checkExsistTable(Connection con, String table);
}

