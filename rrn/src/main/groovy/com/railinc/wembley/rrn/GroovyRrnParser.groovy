package com.railinc.wembley.rrn

import java.text.ParseException
import java.util.concurrent.Future


class GroovyRrnParser implements RrnParser {

	@Override
	public Rrn parse(String v) {
		def rrnAndQuerystring = v.split(/\?/);
		def s = rrnAndQuerystring[0].split(/:/)
		rrnAndQuerystring = rrnAndQuerystring.drop(1) 
		def l = s.length
		
		def String rrn = v
		def x ;
		
		def service
		def account
		def resourceType
		def resourceSubType
		def params = [:]
		
		switch (l) {
			case 0..3:
				throw new ParseException(v)

			case 4:
				(x, service, account, resourceType) = s
				break
		}

		rrnAndQuerystring.each { it -> 
			it.split("&").inject(params) { map, kv ->
				def (key,value) = kv.split("=");
				map[key] = URLEncoder.encode(value, "UTF8")
				map
			}
		}

		def args = [rrn:v, service: service, account: account, resourceType : resourceType, params:params]
		
		return new SmtpRrn(args);
	}
	
	
	

}
