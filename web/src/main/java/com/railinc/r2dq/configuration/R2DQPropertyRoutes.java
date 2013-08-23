package com.railinc.r2dq.configuration;

import com.railinc.r2dq.web.Routes;

public class R2DQPropertyRoutes extends Routes {
	private static final String PREFIX = "r2dqproperty";

	public static final String ROOT_PATH = "/s/support/r2dqproperty";
	public static final String DELETE_PATH = "/{id}/delete";
	public static final String UNDELETE_PATH = "/{id}/undelete";
	public static final String EDIT_PATH = "/{id}";
	public static final String NEW_PATH = "/new";
	public static final String LIST_PATH = "/list";

	static {
		Routes.register(new R2DQPropertyRoutes());
	}

	public R2DQPropertyRoutes() {
		super(PREFIX);
		register(Routes.STANDARD_ROOT, simpleRoute(ROOT_PATH));
		register(Routes.STANDARD_LIST, simpleRoute(ROOT_PATH, LIST_PATH));
		register(Routes.STANDARD_NEW, simpleRoute(ROOT_PATH, NEW_PATH));
		register(Routes.STANDARD_EDIT, simpleRoute(ROOT_PATH, EDIT_PATH));
		register(Routes.STANDARD_DELETE, simpleRoute(ROOT_PATH, DELETE_PATH));
		register(Routes.STANDARD_UNDELETE, simpleRoute(ROOT_PATH, UNDELETE_PATH));
	}

	@Override
	public String prefix() {
		return PREFIX;
	}

}
