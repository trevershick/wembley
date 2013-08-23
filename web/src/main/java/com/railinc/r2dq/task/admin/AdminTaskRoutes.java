package com.railinc.r2dq.task.admin;

import com.railinc.r2dq.web.Routes;

public class AdminTaskRoutes extends Routes {
	private static final String PREFIX = "admintasks";

	public static final String ROOT_PATH = "/s/admin/tasks";
//	public static final String DELETE_PATH = "/{id}/delete";
//	public static final String UNDELETE_PATH = "/{id}/undelete";
//	public static final String EDIT_PATH = "/{id}";
//	public static final String NEW_PATH = "/new";
	public static final String LIST_PATH = "/list";

	public static final String CANCEL_PATH = "/{id}/cancel";

	public static final String NOTIFY_PATH = "/{id}/notify";

	public static final String RO_PATH = "/{id}/view";


	
	static {
		Routes.register(new AdminTaskRoutes());
	}

	public AdminTaskRoutes() {
		super(PREFIX);
		register(Routes.STANDARD_ROOT, simpleRoute(ROOT_PATH));
		register(Routes.STANDARD_LIST, simpleRoute(ROOT_PATH, LIST_PATH));
//		register(Routes.STANDARD_NEW, simpleRoute(ROOT_PATH, NEW_PATH));
//		register(Routes.STANDARD_EDIT, simpleRoute(ROOT_PATH, EDIT_PATH));
//		register(Routes.STANDARD_DELETE, simpleRoute(ROOT_PATH, DELETE_PATH));
//		register(Routes.STANDARD_UNDELETE, simpleRoute(ROOT_PATH, UNDELETE_PATH));
		register(Routes.STANDARD_VIEW, simpleRoute(ROOT_PATH, RO_PATH));
		register("cancel", simpleRoute(ROOT_PATH, CANCEL_PATH));
		register("notify", simpleRoute(ROOT_PATH, NOTIFY_PATH));
	}

	@Override
	public String prefix() {
		return PREFIX;
	}

}
