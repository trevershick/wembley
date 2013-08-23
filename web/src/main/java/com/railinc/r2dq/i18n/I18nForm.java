package com.railinc.r2dq.i18n;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.base.Function;
import com.railinc.common.messages.hibernate.HibernateI18nMessage;
import com.railinc.r2dq.domain.AuditData;
import com.railinc.r2dq.domain.I18nMessage;

public class I18nForm {
	public static final String DEFAULT_FORM_NAME = "i18n";

	
	Long id;
	
	@NotNull
	@Size(min=1,max=HibernateI18nMessage.MAX_LENGTH_CODE)
	String code;
	
	@NotNull
	@Size(min=1,max=HibernateI18nMessage.MAX_LENGTH_TEXT)
	String text;

	@NotNull
	@Size(min=0,max=HibernateI18nMessage.MAX_LENGTH_LOCALE)
	String locale;
	
	
	private AuditData auditData;
	
	public AuditData getAuditData() {
		return auditData;
	}

	public void setAuditData(AuditData auditData) {
		this.auditData = auditData;
	}

	public I18nForm() {
	}


	public static Function<I18nMessage,I18nForm> PROPERTY_TO_FORM = new Function<I18nMessage,I18nForm>() {
		@Override
		public I18nForm apply(I18nMessage input) {
			I18nForm f = new I18nForm();
			f.setId(input.getId());
			f.setCode(input.getCode());
			f.setText(input.getText());
			f.setLocale(input.getLocale());
			f.setAuditData((AuditData) input.getAuditData().clone());
			return f;
		}
	};
	public static final Function<I18nForm, I18nMessage> FORM_TO_NEW_PROPERTY = new Function<I18nForm,I18nMessage>() {
		@Override
		public I18nMessage apply(I18nForm i) {
			I18nMessage p = new I18nMessage();
			p.setLocale(i.getLocale());
			p.setCode(i.getCode());
			p.setText(i.getText());
			return p;
		}
		
	};


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
