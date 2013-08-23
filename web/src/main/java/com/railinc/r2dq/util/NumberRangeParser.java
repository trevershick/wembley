package com.railinc.r2dq.util;

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberRange;

public class NumberRangeParser {

	
	private final StringBuilder from = new StringBuilder();
	private final StringBuilder to = new StringBuilder();
	
	private State initialState = new NumberState(from, 
			new AwaitingCharacter(',', 
					new NumberState(to, 
							new FinalState())));
	
	
	private String value;
	
	abstract class State {
		int expecting = 0;
		int startedExpectingAtIndex = 0;
		abstract State nextState(char c);
	}
	
	class FinalState extends State {
		@Override
		State nextState(char c) {
			return this;
		}
	}
	
	class AwaitingCharacter extends State {
		private final char waitingFor;
		private State next;
		AwaitingCharacter(char c,State next) {
			this.waitingFor = c;
			this.next = next;
		}
		@Override
		State nextState(char c) {
			if (c == waitingFor) {
				return next; // i am consuming, so just pass along the next state.
			}
			return this;
		}
	}
	


	class NumberState extends State {
		private StringBuilder buffer;
		private State next;

		NumberState(StringBuilder buffer, State nextState) {
			this.buffer = buffer;
			this.next = nextState;
		}
		@Override
		State nextState(char c) {
			if (buffer.length() == 0) {
				// any digit is good here
				if (Character.isDigit(c) || c == '-') {
					buffer.append(c);
				}
				return this;
			}
			
			if (Character.isDigit(c)) {
				buffer.append(c);
			} else {
				return next.nextState(c);  // i didn't consume it, so pass it along to the next state
			}
			return this;
		}
		
	}

	
	
	
	private NumberRangeParser(String value) throws ParseException {
		this.value = value;
		if (StringUtils.isBlank(value)) {
			throw new ParseException("The value cannot be blank", 0);
		}
	}


	public static NumberRange parse(String value) throws ParseException {
		NumberRangeParser p = new NumberRangeParser(value);
		return p.parse();
	}



	private NumberRange parse() throws ParseException {
		char[] cs = value.toCharArray();
		
		State state = initialState;
		for (int i=0;i< cs.length;i++) {
			state = state.nextState(cs[i]);
		}
		Long f = from();
		Long t = to();
		if (f == null) {
			throw new ParseException("Found no numbers", 0);
		}
		return t == null ? new NumberRange(f) : new NumberRange(f,t);
	}
	
	public static boolean isMinimumAndMaximumNumbersSame(String value) throws ParseException {
		NumberRangeParser p = new NumberRangeParser(value);
		NumberRange range= p.parse();
		return range.getMinimumNumber().equals(range.getMaximumNumber());
	}



	private Long to() {
		if (to.length() == 0) {
			return null; 
		}
		return Long.valueOf(to.toString());
	}



	private Long from() {
		if (from.length() == 0) {
			return null; 
		}
		return Long.valueOf(from.toString());
	}
}
