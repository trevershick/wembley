package com.github.trevershick.wembley.rrn;

import static java.lang.String.format;

import java.text.ParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;



public class DefaultRrnParser implements RrnParser {
	private Logger log = LoggerFactory.getLogger(getClass());
	

	@Override
	public Rrn parse(String v) throws ParseException {
		log.debug("parse {}", v);
		/* http://regexpal.com http://gskinner.com/RegExr/ */
		// regex = /rrn:([a-z]+):([a-z0-9]+)?:(([a-z]+):)?([^/]+)(/([^/]+))*\?([&]?[a-z]+=[^&]*)*/
		String[] s = v.split("\\?");
		String[] s1 = s[0].split(":");
		String rrn = s1[0];
		String queryString = null;
		if (s1.length > 1) {
			queryString = s1[1];
		}
		
		log.debug("Group Count {}", s1.length);
		
		if (s1.length < 4){
			throw new ParseException(v, 0);
		}
		
		String service = s1[1], account = s1[2];
		String resource = null;
		String resourceType = null;
		
		switch (s1.length) {
		case 4:
			resource = s1[3];
			break;
		case 5:
			resourceType = s1[3];
			resource = s1[4];
		}
		
		

		Map<String,String> params = Maps.newHashMap();
		
/*
		rrnAndQuerystring.each { it -> 
			it.split("&").inject(params) { map, kv ->
				def (key,value) = kv.split("=");
				map[key] = URLEncoder.encode(value, "UTF8")
				map
			}
		}

		def args = [rrn:v, service: service, account: account, resource:resource, params:params]
		if (resourceType) args << [resourceType : resourceType]

		return this."$service"(args);*/
		if ("smtp".equals(service)){
			return new SmtpRrn(account, resourceType, resource, params);
		}
		throw new ParseException(format("%s is not supported as a service", service), 0);
		
	}
	
	
}
