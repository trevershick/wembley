package com.railinc.r2dq.web;

import com.google.gson.Gson;

public class Option<V> {
	private V value;
	private String label;

	public Option() {
	}

	public Option(V v, String lbl) {
		this.value = v;
		this.label = lbl;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
