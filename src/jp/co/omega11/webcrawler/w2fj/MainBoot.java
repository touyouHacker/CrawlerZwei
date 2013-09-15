package jp.co.omega11.webcrawler.w2fj;

class MainBoot {
	
	//W2FJを呼びたすだけのメインクラス、処理はすべてMainLogicで行う
	//将来的にW2FJはGUIやJSPから外部よびだしするためメイン関数にはなにもかかずに依存しないようにする
	public static void main(String args[]) {

		// Main 処理を作成
		W2fjMainLogic w2MainLogic = new W2fjMainLogic();
		w2MainLogic.start(args);
		
	}
}