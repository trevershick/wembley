package com.railinc.r2dq.messages;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.WebDataBinder;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.railinc.r2dq.domain.InboundSource;
import com.railinc.r2dq.domain.views.RawInboundMessageView;
import com.railinc.r2dq.util.PagedSearchForm;
import com.railinc.r2dq.util.WebFormConstants;

public class MessageSearchForm extends PagedSearchForm<MessageSearchCriteria, RawInboundMessageView>{


	public static final String DEFAULT_FORM_NAME = "messagesearch";

	/**
	 * 
	 */
	private static final long serialVersionUID = 304639385271754558L;
	
	private InboundSource source;
	private String query;
	private Boolean processed = Boolean.FALSE;
	private String type = "I";


	
	@Override
	public MessageSearchCriteria getCriteriaInternal() {
		MessageSearchCriteria c = new MessageSearchCriteria();
		// build up the criteria from the fields
		if (processed != null) {
			c.setProcessed(this.processed);
		}
		if (source != null){
			c.addInboundSource(source);
		}
		if (StringUtils.isNotBlank(query)) {
			c.addData(query);
		}
		if(StringUtils.isNotBlank(type)){
			c.setType(type);
		}
		return c;
	}


	public InboundSource getSource() {
		return source;
	}
	
	public void setSource(InboundSource source) {
		this.source = source;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public Boolean getProcessed() {
		return processed;
	}
	
	public void setProcessed(Boolean value) {
		this.processed = value;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public Collection<String> getSourceOptions() {
		List<InboundSource> asList = Arrays.asList(InboundSource.values());
		return Lists.transform(asList, Functions.toStringFunction());
	}
	
	public Collection<String> getProcessedOptions() {
		return Arrays.asList(new String[]{"Yes","No"});
	}
	
	public Collection<String> getTypeOptions(){
		return Arrays.asList(new String[]{"Inbound","Outbound"});
	}
	
	public static final void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Boolean.class, "processed", TRISTATE_BOOLEAN);
		binder.registerCustomEditor(InboundSource.class, "source", NULLABLE_SOURCE);
		binder.registerCustomEditor(Date.class, WebFormConstants.timestampPropertyEditor());
		binder.registerCustomEditor(String.class, "type", NULLABLE_TYPE);
	}
	
	public static final PropertyEditorSupport NULLABLE_SOURCE = new PropertyEditorSupport() {
		
		@Override
		public String getAsText() {
			return String.valueOf(getValue());
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			try {
				setValue(InboundSource.valueOf(text));
			} catch (Exception e) {
				setValue(null);
			}
		}
	};
	
	
	public static final PropertyEditorSupport TRISTATE_BOOLEAN = new PropertyEditorSupport() {
		@Override
		public String getAsText() {
			Boolean b = (Boolean) getValue();
			return (b == null) ? "" : (b.booleanValue() ? "Yes" : "No");
		}
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (StringUtils.equals("Yes", text)) {
				setValue(Boolean.TRUE);
			} else if (StringUtils.equals("No", text)) {
				setValue(Boolean.FALSE);
			} else {
				setValue(null);
			}
		}
	};
	
	public static final PropertyEditorSupport NULLABLE_TYPE = new PropertyEditorSupport() {
		@Override
		public String getAsText() {
			String b = (String) getValue();
			return (b == null) ? "" : "I".equalsIgnoreCase(b)? "Inbound" : "Outbound";
		}
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (StringUtils.equals("Inbound", text)) {
				setValue("I");
			} else if (StringUtils.equals("Outbound", text)) {
				setValue("O");
			} else {
				setValue(null);
			}
		}
	};
	
}
