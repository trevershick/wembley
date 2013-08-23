package com.railinc.r2dq.web;

import static com.google.common.collect.Maps.newHashMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class Routes {
	private final Logger log = LoggerFactory.getLogger(getClass());
	public static final String STANDARD_LIST = "list_path";
	public static final String STANDARD_VIEW = "view_path";
	public static final String STANDARD_DELETE = "delete_path";
	public static final String STANDARD_UNDELETE = "undelete_path";
	public static final String STANDARD_EDIT = "edit_path";
	public static final String STANDARD_NEW = "new_path";
	
	public static final String STANDARD_ROOT = "root_path";

	/**
	 * global registry of route objects
	 */
	private static final Map<String, Routes> prefixToRoutes = newHashMap();
	
	
	private final Map<String, Function<Object,String>> functions = newHashMap();
	

	private String prefix;
	
	public Routes(String prefix) {
		this.prefix = prefix;
	}

	public static Routes routes(String prefix) {
		Routes routes = prefixToRoutes.get(prefix);
		Preconditions.checkNotNull(routes, "Unable to find routes for prefix '%s'", prefix);
		return routes;
	}
	
	public static final String basePath(String base, String plus) {
		return String.format("%s%s", base, plus);
	}
	
	public static final String redirect(String path) { 
		return String.format("redirect:%s", path);
	}

	
	public String prefix() {
		return prefix;
	}
	
	protected static Function<Object, String> simpleRoute(final String base) {
		return new Function<Object, String>() {
			@Override
			public String apply(Object input) {
				return base;
			}
			public String toString() {
				return apply("{arg}");
			}
		};
	}
	
	protected static Function<Object, String> simpleRoute(final String base, final String path) {
		return new Function<Object, String>() {
			@Override
			public String apply(Object input) {
				// replace {anything} with input
				try {
					input = URLEncoder.encode(String.valueOf(input), "UTF-8");
				} catch (UnsupportedEncodingException e) {
				}
				String x = path.replaceAll("\\{.+\\}", String.valueOf(input));
				return basePath(base, x);
			}
			public String toString() {
				return apply("{arg}");
			}
		};
	}
	public static void register(Routes routes) {
		prefixToRoutes.put(routes.prefix(), routes);
	}
	
	/**
	 * overridden
	 */
	public void register(String func, Function<Object,String> routeFunction) {
		this.functions.put(func, routeFunction);
	}
	


	public String redirectRoute(String string) {
		return redirectRoute(string,null);
	}
	public String redirectRoute(String string,Object obj) {
		return  redirect(route(string,obj));
	}
	public String route(String func) {
		return route(func,null);
	}
	
	public String route(String func, Object forObject) {
		String apply = routeFunction(func).apply(forObject);
		log.debug("returning {} for route {}/{}", apply, func,forObject);
		return apply;
	}
	
	public Function<Object,String> routeFunction(String name) {
		log.debug("Routes {}", functions);
		return Optional.fromNullable(functions.get(name)).or(new Function<Object, String>() {
			@Override
			public String apply(Object input) {
				return null;
			}});
	}
}
