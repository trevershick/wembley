package com.railinc.common.messages;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import com.railinc.r2dq.i18n.I18nUpdatedEvent;

public class I18nServiceBackedMessageSource implements MessageSource, HierarchicalMessageSource, ApplicationListener<I18nUpdatedEvent>, I18nServiceBackedMessageSourceMBean {
	I18nService<I18nMessage> service;
	
	private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<String, String>();
	
	private final List<Pattern> defaultingPatterns = newArrayList();

	private MessageSource parent;

	public I18nServiceBackedMessageSource() { 
		registerPattern(".+\\.(\\d+)");
	}
	
	private void registerPattern(String string) {
		defaultingPatterns.add(Pattern.compile(string));
	}
	

	private String defaultKey(String code) {
		for (Pattern p : defaultingPatterns) {
			Matcher matcher = p.matcher(code);
			if (matcher.find()) {
				return code.replaceAll(matcher.group(1), "default");
			}
		}
		return null;

		
	}

	public I18nService<I18nMessage> getService() {
		return service;
	}
	@Required
	public void setService(I18nService<I18nMessage> service) {
		this.service = service;
	}

	private String cacheKey(Locale locale, String key) {
		return (locale == null ? "null" : locale.toString()).concat(":").concat(key);
	}
	private String cacheKey(String locale, String key) {
		return (locale == null ? "null" : locale.toString()).concat(":").concat(key);
	}

	@Override
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		String defaultCode = defaultKey(code);
		
		String fmt = null;
		
		// straight up value with locale
		fmt = cached().get(cacheKey(locale, code));
		if (fmt != null) {return fmt;}
		
		// default value with locale
		if (defaultCode != null) {
			fmt = cached().get(cacheKey(locale, defaultCode));
			if (fmt != null) {return fmt;}
		}
		
		// straight up value without locale
		fmt = cached().get(cacheKey((Locale) null, code));
		if (fmt != null) {return fmt;}

		// default value without locale
		if (defaultCode != null) {
			fmt = cached().get(cacheKey((Locale) null, defaultCode));
			if (fmt != null) {return fmt;}
		}

		if (this.parent != null) {
			fmt =  this.parent.getMessage(code, args, null, locale);
			if (fmt != null) {return fmt;}
			
			if (defaultCode != null) {
				fmt =  this.parent.getMessage(defaultCode, args, null, locale);
				if (fmt != null) {return fmt;}
			}
		}
		return defaultMessage;
	}


	@Override
	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		return getMessage(code, args, null, locale);
	}






	@Override
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		String[] codes = resolvable.getCodes();
		for (String c : codes) {
			String msg = getMessage(c, resolvable.getArguments(), locale);
			if (msg != null) {
				return msg;
			}
		}
		return resolvable.getDefaultMessage();
	}

	@Override
	public void setParentMessageSource(MessageSource parent) {
		this.parent= parent;
	}

	@Override
	public MessageSource getParentMessageSource() {
		return this.parent;
	}

	public Map<String,String> cached() {
		synchronized(cache) {
			if (cache.isEmpty()) {
				onApplicationEvent(new I18nUpdatedEvent(this));
			}
			return cache;
		}
	}
	@Override
	public void onApplicationEvent(I18nUpdatedEvent event) {
		reloadCache();
	}

	private void reloadCache() {
		Collection<I18nMessage> all = this.service.all();
		Map<String,String> tmp = newHashMap();
		for (I18nMessage m : all) {
			tmp.put(cacheKey(m.getLocale(), m.getCode()), m.getText());
		}
		cache.clear();
		cache.putAll(tmp);
	}

	@Override
	public int getCacheSize() {
		return this.cache.size();
	}

	@Override
	public void reload() {
		reloadCache();		
	}

}
