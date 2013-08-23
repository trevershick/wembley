package com.railinc.r2dq.util;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;

import com.railinc.r2dq.domain.ApprovalDisposition;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.ImplementationDisposition;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.sourcesystem.SourceSystemService;

public class WebFormConstants {
	
	public static final PropertyEditor timestampPropertyEditor() {
		return new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true);	
	}
	
	public static final PropertyEditor identityTypeEditor() {
		return new IdentityTypeEditor();
	}
	
	public static PropertyEditor sourceSystemEditor(final SourceSystemService sourceSystemService) {
		return new SourceSystemEditor(sourceSystemService);
	}

	static class SourceSystemEditor extends PropertyEditorSupport {
		private SourceSystemService service;

		SourceSystemEditor(SourceSystemService s) {
			this.service = s;
		}
		@Override
		public String getAsText() {
			return String.valueOf(getValue());
		}
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			setValue(service.get(text));
		}
	}
	
	static class IdentityTypeEditor extends PropertyEditorSupport {
		@Override
		public String getAsText() {
			return String.valueOf(getValue());
		}
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			setValue(IdentityType.find(text));
		}
	}

	
	static class ApprovalDispositionEditor extends PropertyEditorSupport {
		@Override
		public String getAsText() {
			return String.valueOf(getValue());
		}
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			setValue(ApprovalDisposition.valueOf(text));
		}
	}
	static class ImplementationDispositionEditor extends PropertyEditorSupport {
		@Override
		public String getAsText() {
			return String.valueOf(getValue());
		}
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			setValue(ImplementationDisposition.valueOf(text));
		}
	}
	static class ImplementationTypeEditor extends PropertyEditorSupport {
		@Override
		public String getAsText() {
			return String.valueOf(getValue());
		}
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			setValue(ImplementationType.valueOf(text));
		}
	}

	
	public static PropertyEditor implementationTypeEditor() {
		return new ImplementationTypeEditor();
	}

	public static PropertyEditor implementationDispositionEditor() {
		return new ImplementationDispositionEditor();
	}

	public static PropertyEditor approvalDispositionTypeEditor() {
		return new ApprovalDispositionEditor();
	}
}