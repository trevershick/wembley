package com.railinc.wembley.rrn;

import java.text.ParseException;


public interface RrnParser {
	Rrn parse(String in) throws ParseException;
}
