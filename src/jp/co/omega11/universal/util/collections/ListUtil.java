package jp.co.omega11.universal.util.collections;

import java.util.List;

public class ListUtil {

	
	/**
	 * 引数のListがNullかもしくはサイズ長が0ならTrue
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isNullSizeZero (List list) {
		
		if (list == null) {
			return true;
		}
		
		if(list.size() > 0) {
			return false;
		}
		
		return true;
	}
}

