package com.github.trevershick.wembley.rrn;

import java.text.ParseException;


public interface RrnParser {
	Rrn parse(String in) throws ParseException;
}
