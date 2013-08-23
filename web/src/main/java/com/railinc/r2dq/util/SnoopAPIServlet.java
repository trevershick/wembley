package com.railinc.r2dq.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.railinc.common.oscar.OscarHandler;
import com.railinc.common.oscar.OscarHandlerMapping;
import com.railinc.common.oscar.json.OscarJsonServiceServlet;

public class SnoopAPIServlet extends OscarJsonServiceServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6250655396926959212L;

	private final Logger log = Logger.getLogger(getClass().getName());
	
	@Override
	protected String getBundlePrefix() {
		return "snoop";
	}



	@OscarHandlerMapping(regex="GET:/api/.*")
	public OscarHandler<JSONObject> snoop() {
		return new OscarHandler<JSONObject>() {
			@SuppressWarnings("unchecked")
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity)
					throws IOException {
				JSONObject basics = new JSONObject();
				basics.put("localName", req.getLocalName());
				basics.put("localPort", req.getLocalPort());
				basics.put("remoteAddress", req.getRemoteAddr());
				basics.put("remotePort", req.getRemotePort());
				basics.put("remoteHost", req.getRemoteHost());
				basics.put("remoteUser", req.getRemoteUser());

				
				
				JSONArray headers = new JSONArray();
				Enumeration<?> names = req.getHeaderNames();
				while (names.hasMoreElements()) {
					String nm = (String) names.nextElement();
					String v = req.getHeader(nm);
					JSONObject o = new JSONObject();
					o.put("name", nm);
					o.put("value", v);
					headers.add(o);
				}
				
				JSONArray cookies = new JSONArray();
				Cookie[] cs = req.getCookies();
				for (Cookie c : cs) {
					JSONObject o = new JSONObject();
					o.put("domain", c.getDomain());
					o.put("path", c.getPath());
					o.put("name", c.getName());
					o.put("value", c.getValue());
					cookies.add(o);
				}
				outputEntity.put("basics", basics);
				outputEntity.put("headers", headers);
				outputEntity.put("cookies", cookies);
				log.info(outputEntity.toString());
				return ok();
			}
		};
	}

}
