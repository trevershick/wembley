package com.railinc.r2dq.correspondence;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;

import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;

public class Template2Correspondence implements Function<CorrespondenceTemplate,Correspondence> {
	
	
	public static final String ENCODING = "UTF-8";
	public static final String CONTEXT_ATTR_LOGORESOURCENAME = "imageResourceName";
	public static final String CONTEXT_ATTR_RECIPIENTS = "recipients";
	public static final String CONTEXT_ATTR_PARAMS = "params";
	public static final String CONTEXT_ATTR_NOW = "now";
	
//	private static final String IMAGE_RESOURCE_NAME = "logo";
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private VelocityEngine engine;
	private final Map<String,Object> parameters = newHashMap();
	private Supplier<Map<String, Object>> parametersSupplier;


	public Template2Correspondence() {}

	
	
	@Required
	public void setEngine(VelocityEngine engine) {
		this.engine = engine;
	}
	
	
	public void setParameterSupplier(Supplier<Map<String,Object>> supplier) {
		this.parametersSupplier = supplier;
	}
	
	public void setParameters(Map<String,Object> ps) {
		if (parameters != null) {
			this.parameters.putAll(ps);
		}
	}
	


	private String htmlTemplatePath(String templateBaseName) {
		return format("/correspondence/%1$s/%1$s.html", templateBaseName);
	}
	
	private String subjectTemplatePath(String templateBaseName) {
		return format("/correspondence/%1$s/%1$s.subject", templateBaseName);
	}

	private String textTemplatePath(String templateBaseName) {
		return format("/correspondence/%1$s/%1$s.txt", templateBaseName);
	}
	
	@Override
	public Correspondence apply(CorrespondenceTemplate template) {
		VelocityContext ctx = new VelocityContext(newHashMap(template.data()));
		ctx.put(CONTEXT_ATTR_NOW, new Date());
		ctx.put(CONTEXT_ATTR_PARAMS, buildParameters());
		ctx.put(CONTEXT_ATTR_RECIPIENTS, template.getRecipients());
//		ctx.put(CONTEXT_ATTR_LOGORESOURCENAME, IMAGE_RESOURCE_NAME); // so that we can reference it from HTML 

		StringWriter textWriter = new StringWriter();
		StringWriter subjectWriter = new StringWriter();
		StringWriter htmlWriter = new StringWriter();
		String encoding = ENCODING;
		String templateBaseName = template.getTemplateName();
		logger.debug("Template base name : {}", templateBaseName);
		logger.debug("Merge templates with context {}", ctx);
		try {
			String subjectTemplatePath = subjectTemplatePath(templateBaseName);
			logger.debug("mergeTemplate {}", subjectTemplatePath);
			this.engine.mergeTemplate(subjectTemplatePath, encoding, ctx, subjectWriter);
			
			String textTemplatePath = textTemplatePath(templateBaseName);
			logger.debug("mergeTemplate {}", textTemplatePath);
			this.engine.mergeTemplate(textTemplatePath, encoding, ctx, textWriter);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
		try {
			String htmlTemplatePath = htmlTemplatePath(templateBaseName);
			logger.debug("mergeTemplate {}", htmlTemplatePath);
			this.engine.mergeTemplate(htmlTemplatePath, encoding, ctx, htmlWriter);
		} catch (Exception rnfe) {
			logger.error("Unable to merge HTML template",rnfe);
		}
		String textContent = textWriter.toString();
		String htmlContent = htmlWriter.toString();
		String subject = subjectWriter.toString();
		Correspondence c = new Correspondence();
		c.setSubject(subject);
		c.setTextHtml(htmlContent);
		c.setTextPlain(textContent);
		c.setFrom(template.getFrom());
		c.setReplyTo(template.getReplyTo());
		c.addRecipients(template.getRecipients());
		

		return c;
	}



	private Map<String, Object> buildParameters() {
		final Map<String, Object> p = newHashMap(this.parameters);
		Supplier<Map<String, Object>> x = this.parametersSupplier;
		parametersFromSupplier(new Predicate<Map<String, Object>>(){
			@Override
			public boolean apply(Map<String, Object> input) {
				p.putAll(input);return true;
			}});
		return p;
	}



	private void parametersFromSupplier(Predicate<Map<String, Object>> predicate) {
		Map<String, Object> results = (this.parametersSupplier == null) 
				? null
				: this.parametersSupplier.get();
		if (results != null){ 
			predicate.apply(results);
		}
	}
}
