package com.railinc.wembley.domain;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Collections2.transform;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.common.base.Function;
import com.railinc.wembley.api.Intent;

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
	

