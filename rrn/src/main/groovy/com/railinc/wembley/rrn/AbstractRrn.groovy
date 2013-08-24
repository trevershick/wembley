package com.railinc.wembley.rrn

import java.util.Map
		
class AbstractRrn implements Rrn {
	String rrn
	String service
	String account
	String resourceType
	String resource
	Map params = [:]
	Map attributes = [:]
}
