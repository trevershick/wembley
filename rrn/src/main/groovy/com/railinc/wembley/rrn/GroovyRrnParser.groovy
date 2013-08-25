package com.railinc.wembley.rrn

import java.text.ParseException
import java.util.concurrent.Future


class GroovyRrnParser implements RrnParser {


	@Override
	public Rrn parse(String v) {
		/* http://regexpal.com http://gskinner.com/RegExr/ */
		// regex = /rrn:([a-z]+):([a-z0-9]+)?:(([a-z]+):)?([^/]+)(/([^/]+))*\?([&]?[a-z]+=[^&]*)*/
		resourceRegex = /rrn:([a-z]+):([a-z0-9]*):(([a-z]*):|([^?]+))\??(.*)/
		matcher = ( v =~ regex )
		if (!matcher.matches()) {
			throw new ParseException(v, 0);
		}
		
		groupCount = matcher[0].length
		print "Group Count $groupCount"
		
		
		switch (groupCount) {
			service = matcher[0][0];
			account = matcher[0][1]
			case 4:
				(x, service, account, resource) = s
				break
			case 4:
			case 5:
				(x, service, account, resourceType, resource) = s
				break
		}

		rrnAndQuerystring.each { it -> 
			it.split("&").inject(params) { map, kv ->
				def (key,value) = kv.split("=");
				map[key] = URLEncoder.encode(value, "UTF8")
				map
			}
		}

		def args = [rrn:v, service: service, account: account, resource:resource, params:params]
		if (resourceType) args << [resourceType : resourceType]

		return this."$service"(args);
	}
	
	
	SmtpRrn smtp(args) { 
		return new SmtpRrn(args);
	}
	
	def methodMissing(String name, args) {
		throw new ParseException("$name is not supported as a service", 0)
	}

}
