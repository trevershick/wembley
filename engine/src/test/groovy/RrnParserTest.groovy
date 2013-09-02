import java.text.ParseException

import spock.lang.*

import com.railinc.wembley.rrn.DefaultRrnParser
import com.railinc.wembley.rrn.SmtpRrn


class RrnParserTest extends spock.lang.Specification {
	
	
	
    def "Property RrnSubclasses are created"() {
		
		expect:
		new DefaultRrnParser().parse(rrn).class == type

        where:
        rrn														| type
        "rrn:smtp::trever.shick@railinc.com/Trever Shick" 		| SmtpRrn
    }
	
	@Unroll
	def "#rrn should not work"() {
		
		when:
		new DefaultRrnParser().parse(rrn)

		then:
		thrown(java.text.ParseException)

		where:
		rrn << [
			"rrn:x:"
		];
	}
	
	
	

	@Unroll
	def "#rrn should throw a parse exception"() {
		
		when:
		new DefaultRrnParser().parse(rrn)

		then:
		def e = thrown(ParseException)
		e.message == 'fur is not supported as a service'

		where:
		rrn << [
			"rrn:fur::contact?category=A&subcategory=B&function=c"
		];
	}
	
	
	@Unroll
	def "#r should have a property #pname of #value"() {
		
		expect:
		new DefaultRrnParser().parse(r).properties[pname] == value

		where:
		r																| pname 			| value 
		"rrn:smtp::trever.shick@railinc.com" 							| "rrn" 			| "rrn:smtp::address:trever.shick@railinc.com"
		"rrn:smtp::address:trever.shick@railinc.com/Trever Shick" 		| "rrn" 			| "rrn:smtp::address:trever.shick@railinc.com/Trever Shick"
		"rrn:smtp::trever.shick@railinc.com" 							| "resourceType"	| "address"
		"rrn:smtp::address:trever.shick@railinc.com/Trever Shick" 		| "resourceType"	| "address" 
		"rrn:smtp:1234:address:trever.shick@railinc.com/Trever Shick" 	| "resourceType"	| "address" 
		"rrn:smtp:1234:address:trever.shick@railinc.com/Trever Shick" 	| "resourceType"	| "address" 
		"rrn:smtp::trever.shick@railinc.com" 							| "resource" 		| "trever.shick@railinc.com"
		"rrn:smtp::address:trever.shick@railinc.com/Trever Shick" 		| "service"			| "smtp" 
		"rrn:smtp::address:trever.shick@railinc.com/Trever Shick" 		| "resource" 		| "trever.shick@railinc.com/Trever Shick" 
		//"rrn:fur::contact?category=A&subcategory=B&function=c"			| "service"			| "contact"
		//"rrn:fur::contact?category=A&subcategory=B&function=c"			| "resourceType"	| "contact"
		//"rrn:fur::contact?category=A&subcategory=B&function=c"			| "params"			| [category:'A', subcategory: 'B', function : 'c']
	}
}  