package jp.co.omega11.webcrawler.w2fj;

class MainBoot {
	
	//W2FJ���Ăт��������̃��C���N���X�A�����͂��ׂ�MainLogic�ōs��
	//�����I��W2FJ��GUI��JSP����O����т������邽�߃��C���֐��ɂ͂Ȃɂ��������Ɉˑ����Ȃ��悤�ɂ���
	public static void main(String args[]) {

		// Main �������쐬
		W2fjMainLogic w2MainLogic = new W2fjMainLogic();
		w2MainLogic.start(args);
		
	}
}