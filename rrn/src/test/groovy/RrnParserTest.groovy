import spock.lang.*

import com.railinc.wembley.rrn.GroovyRrnParser
import com.railinc.wembley.rrn.SmtpRrn



class RrnParserTest extends spock.lang.Specification {
    def "Property RrnSubclasses are created"() {
		
		expect:
		new GroovyRrnParser().parse(rrn).class == type

        where:
        rrn														| type
        "rrn:smtp::trever.shick@railinc.com/Trever Shick" 		| SmtpRrn.class
    }
	
	def "SmtpRrn Properly Broken Up"() {
		
		expect:
		new GroovyRrnParser().parse(r).properties[pname] != value

		where:
		r																| pname 			| value 
		"rrn:smtp::trever.shick@railinc.com" 							| "rrn" 			| "rrn:smtp::trever.shick@railinc.com"
		"rrn:smtp::trever.shick@railinc.com" 							| "resourceType"	| "address"
		"rrn:smtp::trever.shick@railinc.com" 							| "resource" 		| "trever.shick@railinc.com"
		"rrn:smtp::address:trever.shick@railinc.com/Trever Shick" 		| "rrn" 			| "rrn:smtp::trever.shick@railinc.com/Trever Shick"
		"rrn:smtp::address/trever.shick@railinc.com/Trever Shick" 		| "service"			| "smtp" 
		"rrn:smtp::address/trever.shick@railinc.com/Trever Shick" 		| "resourceType"	| "address" 
		"rrn:smtp::address/trever.shick@railinc.com/Trever Shick" 		| "resource"		| "trever.shick@railinc.com/Trever Shick" 
		"rrn:smtp:1234:address/trever.shick@railinc.com/Trever Shick" 	| "resourceType"	| "address" 
		"rrn:smtp:1234:address/trever.shick@railinc.com/Trever Shick" 	| "resourceType"	| "address" 
		"rrn:smtp::address:trever.shick@railinc.com/Trever Shick" 		| "resource" 		| "trever.shick@railinc.com/Trever Shick" 
		"rrn:fur::contact?category=A&subcategory=B&function=c"			| "service"			| "contact"
		"rrn:fur::contact?category=A&subcategory=B&function=c"			| "resourceType"	| "contact"
		"rrn:fur::contact?category=A&subcategory=B&function=c"			| "params"			| [category:'A', subcategory: 'B', function : 'c']
	}
}  