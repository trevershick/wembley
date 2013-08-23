package com.railinc.r2dq.log;

import com.railinc.r2dq.task.MyTaskRoutes;
import com.railinc.r2dq.web.Routes;

public class SystemAuditLogRoutes extends Routes {
	
	private static final String ROUTE_AUDITLOG = "auditlog";

	public static final String PREFIX = "auditlog";

	public static final String ROOT_PATH = "/s/support";
//	public static final String DELETE_PATH = "/{id}/delete";
//	public static final String UNDELETE_PATH = "/{id}/undelete";
//	public static final String EDIT_PATH = "/{id}";
//	public static final String NEW_PATH = "/new";
	public static final String LIST_PATH = "/list";

	public static final String VIEW_PATH = "/{logId}/show";
	
	
	static {
		Routes.register(new MyTaskRoutes());
	}

	public SystemAuditLogRoutes() {
		super(PREFIX);
		register(Routes.STANDARD_ROOT, simpleRoute(ROOT_PATH));
		register(Routes.STANDARD_LIST, simpleRoute(ROOT_PATH, LIST_PATH));
		register(Routes.STANDARD_VIEW, simpleRoute(ROOT_PATH, VIEW_PATH));
//		register(Routes.STANDARD_NEW, simpleRoute(ROOT_PATH, NEW_PATH));
//		register(Routes.STANDARD_EDIT, simpleRoute(ROOT_PATH, EDIT_PATH));
//		register(Routes.STANDARD_DELETE, simpleRoute(ROOT_PATH, DELETE_PATH));
//		register(Routes.STANDARD_UNDELETE, simpleRoute(ROOT_PATH, UNDELETE_PATH));
		register(ROUTE_AUDITLOG, simpleRoute(ROOT_PATH, LIST_PATH));
		
	}

	@Override
	public String prefix() {
		return PREFIX;
	}

}
