package com.github.trevershick.wembley.domain;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Collections2.transform;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.github.trevershick.wembley.api.Intent;
import com.google.common.base.Function;

@Entity
public class TemplatePack {
	
	public static final TemplatePack NULL = new TemplatePack();
	
	@GeneratedValue
	@Id
	Long id;
	
	private final Collection<Template> templates = newArrayList();
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Collection<Intent> intents() {
		return transform(templates, new Function<Template, Intent>(){
			@Override
			public Intent apply(Template input) {
				return input.intent();
			}});
	}
	
}
	

