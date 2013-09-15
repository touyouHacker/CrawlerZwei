/**
 * 
 */
package jp.co.omega11.universal.controller.receivecommand;

import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;

/**
 * @author Wizard1 2009
 *
 */
public interface IExecuteCommand {

	/**
	 * コマンドを実行します
	 * 継承した実装クラスでこのメソッドに業務ロジックを実装します
	 */
	abstract boolean cmommandExe(CommandModel commandModel);
}

