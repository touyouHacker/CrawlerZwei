/**
 * �R�}���h�����s���܂�
 */
package jp.co.omega11.webcrawler.w2fj.component.remotecontrol;

import jp.co.omega11.universal.controller.receivecommand.IExecuteCommand;
import jp.co.omega11.universal.controller.receivecommand.model.CommandModel;
import jp.co.omega11.universal.util.UniversalUtil;
import jp.co.omega11.universal.util.system.JavaEnviroment;
import jp.co.omega11.webcrawler.w2fj.model.systemInfomation.RootInfo;

/**
 * @author Wizard1 2009
 *
 */
public class ExecuteCommandForW2fj implements IExecuteCommand {

	private RootInfo rootInfo;

	private String sendMsg = null;

	public ExecuteCommandForW2fj(RootInfo info) {
		rootInfo = info;
	}

	// TODO StringBuilder�őS�̓I�ɏ�������

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * jp.co.omega11.universal.controller.receivecommand.IExecuteCommand#cmommandExe
	 * (java.lang.String)
	 */
	@Override
	public boolean cmommandExe(CommandModel commandModel) {

		// �N���A
		sendMsg = "";

		if ("-v".equals(commandModel.getCommand())) {
			verbose();
			commandModel.setResultMsg(sendMsg);
		} else if ("-fexit".equals(commandModel.getCommand())) {
			// ���S���𖳎����ċ����I��
			System.exit(0);
		} else {
			commandModel.setResultMsg("�R�}���h�s��" + "["
					+ commandModel.getCommand() + "]");
		}

		return true;
	}

	private void verbose() {

		addLine(rootInfo.getClassName());
		addLine(rootInfo.getItaName());

		addLine("--------------------------------------------------------");

		if (rootInfo.getSubjectInfo() != null) {

			addLine(rootInfo.getSubjectInfo().getClassName());
			addLine("���[�v��"
					+ String.valueOf(rootInfo.getSubjectInfo()
							.getThreadLoopCount()));
			addLine("�J�n���� "
					+ UniversalUtil.nowDate(rootInfo.getSubjectInfo()
							.getStartTime()));
			addLine("�ŏI���s���� "
					+ UniversalUtil.nowDate(rootInfo.getSubjectInfo()
							.getLastWorkTime()));

			// �R���|�[�l���g�����̏�Ԃ̕�����\��
			addLine(rootInfo.getSubjectInfo().getComponentInfo().toString());

		}

		addLine("------");
		if (rootInfo.getDatInfo() != null) {
			addLine(rootInfo.getDatInfo().getClassName());
			addLine("���[�v��"
					+ String
							.valueOf(rootInfo.getDatInfo().getThreadLoopCount()));
			addLine("�J�n���� "
					+ UniversalUtil.nowDate(rootInfo.getDatInfo()
							.getStartTime()));
			addLine("�ŏI���s���� "
					+ UniversalUtil.nowDate(rootInfo.getDatInfo()
							.getLastWorkTime()));

			// �R���|�[�l���g�����̏�Ԃ̕�����\��
			addLine(rootInfo.getDatInfo().getComponentInfo().toString());
		}
		addLine("------");
		if (rootInfo.getContentsInfo() != null) {
			addLine(rootInfo.getContentsInfo().getClassName());
			addLine("���[�v��"
					+ String.valueOf(rootInfo.getContentsInfo()
							.getThreadLoopCount()));
			addLine("�J�n���� "
					+ UniversalUtil.nowDate(rootInfo.getContentsInfo()
							.getStartTime()));
			addLine("�ŏI���s���� "
					+ UniversalUtil.nowDate(rootInfo.getContentsInfo()
							.getLastWorkTime()));

			// �R���|�[�l���g�����̏�Ԃ̕�����\��
			addLine(rootInfo.getContentsInfo().getComponentInfo().toString());
		}
		addLine("--------------------------------------------------------");

		addLine(printAllThreads());

	}

	/**
	 * �X���b�h�̕�����\�����ڍׂɕԂ��܂�
	 *
	 * @param thread
	 */
	private String threadInfoToString(Thread thread) {
		if (thread == null) {
			return null;
		}

		String msg = null;
		msg = "��";

		msg += "Id " + thread.getId() + " / ";

		msg += thread.toString() + JavaEnviroment.line;

		return msg;
	}

	private void addLine(String msg) {
		this.sendMsg += msg + JavaEnviroment.line;
	}

	private String printAllThreads() {
		String msg = "";
		Thread current = Thread.currentThread();
		int count = Thread.activeCount();
		Thread[] list = new Thread[count];
		int n = Thread.enumerate(list);
		for (int i = 0; i < n; i++) {
			if (list[i].equals(current)) {
				msg += "*";
			} else {
				msg += " ";
			}
			msg += list[i] + "In" + " / ";
			msg += "Id " + list[i].getId() + "/ ";
			msg += "State " + list[i].getState().toString() + "/ ";
			msg += "�D��x " + list[i].getPriority() + "/ ";
			msg += "Alive " + list[i].isAlive() + "/ ";
			msg += "Daemon " + list[i].isDaemon() + "/ ";
			msg += JavaEnviroment.line;
		}
		return msg;
	}

}
