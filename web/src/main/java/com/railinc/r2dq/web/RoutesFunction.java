package com.railinc.r2dq.web;

public class RoutesFunction {
	
	public static final String rootPath(String pfx) {
		return Routes.routes(pfx).route(Routes.STANDARD_ROOT);
	}
	
	public static final String listPath(String pfx) {
		return Routes.routes(pfx).route(Routes.STANDARD_LIST);
	}
	public static final String newPath(String pfx) {
		return Routes.routes(pfx).route(Routes.STANDARD_NEW);
	}
	public static final String editPath(String pfx, Object o) {
		return Routes.routes(pfx).route(Routes.STANDARD_EDIT, o);
	}
	public static final String deletePath(String pfx, Object o) {
		return Routes.routes(pfx).route(Routes.STANDARD_DELETE, o);
	}
	public static final String viewPath(String pfx, Object o) {
		return Routes.routes(pfx).route(Routes.STANDARD_VIEW, o);
	}
	public static final String toPath(String pfx, String function, Object o) {
		return Routes.routes(pfx).route(function, o);
		
	}
	public static final String toPath(String pfx, String function) {
		return toPath(pfx,function,null);
	}
  	
}
