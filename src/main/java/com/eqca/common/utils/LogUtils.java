package com.eqca.common.utils;

public class LogUtils {
	
	//todo : Thread.currentThread().getStackTrace()[2].getMethodName() 성능 검증 필요
	// → 비용이 많이 드는 경우 에러에만 FunctionName을 넣도록 변경
	
	public static String basic(String funcName, String template, Object... args) {

		/*return MsgUtils.format(
				"[FUNC:{}][{}]", Thread.currentThread().getStackTrace()[2].getMethodName(),
				MsgUtils.format(template, args));*/

		return MsgUtils.format(
				"[{}][{}]", funcName, MsgUtils.format(template, args));
	}

	public static String errorLog(Exception e, String template, Object... args) {

		return MsgUtils.format(
				"[FUNC:{}][{}]\r\n{}", Thread.currentThread().getStackTrace()[2].getMethodName(),
				MsgUtils.format(template, args),
				e);
	}
	
}
