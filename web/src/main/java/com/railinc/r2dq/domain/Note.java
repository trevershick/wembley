package com.railinc.r2dq.domain;

import javax.persistence.Basic;
import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;

public class Note {
	public static final int MAX_LENGTH_NOTES = 1000;

	@Basic
	@Column(name="NOTE_TEXT",length=MAX_LENGTH_NOTES)
	private String notes;

	public Note(String v) {
		this.notes = v;
	}
	public Note() {}
	

	public String toString() {
		return notes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		return true;
	}

	public String getText() {
		return this.notes;
	}
	public void setText(String s) {
		this.notes = StringUtils.trimToNull(s);
	}
}
