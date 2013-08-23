package com.railinc.wembley.rrn

class AbstractRrn implements Rrn {
	String rrn
	String service
	String account
	String resourceType
	String resource
	def params = [:]
	def attributes = [:]
	
}
