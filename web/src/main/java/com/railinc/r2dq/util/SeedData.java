package com.railinc.r2dq.util;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;
import com.railinc.r2dq.dataexception.DataExceptionService;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.InboundSource;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.Responsibility;
import com.railinc.r2dq.domain.RuleNumberType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.domain.tasks.ExceptionRemediationTask;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.messages.MessageService;
import com.railinc.r2dq.responsibility.ResponsibilityCriteria;
import com.railinc.r2dq.responsibility.ResponsibilityService;
import com.railinc.r2dq.sourcesystem.SourceSystemService;
import com.railinc.r2dq.sso.SsoRoleType;
import com.railinc.r2dq.task.TaskService;
import com.railinc.r2dq.usergroup.UserGroupService;

@Component
@Profile("seed")
public class SeedData implements InitializingBean {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	SourceSystemService sourceSystems;
	
	UserGroupService userGroups;
	
	ResponsibilityService responsibilityService;
	
	DataExceptionService dataExceptionService;
	
	TaskService taskService;
	
	MessageService messageService;
	
	
	
	@Required
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}


	private final Set<SourceSystem> ss = new HashSet<SourceSystem>();
	private final Set<Responsibility> responsibilities = new HashSet<Responsibility>();
	private final Set<DataException> exceptions = new HashSet<DataException>();
	@SuppressWarnings("unused")
	private final Set<Task> tasks = new HashSet<Task>();
	
	
	@Required
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	@Required
	public void setDataExceptionService(DataExceptionService dataExceptionService) {
		this.dataExceptionService = dataExceptionService;
	}

	@Required
	public void setSourceSystems(SourceSystemService sourceSystems) {
		this.sourceSystems = sourceSystems;
	}

	@Required
	public void setUserGroups(UserGroupService userGroups) {
		this.userGroups = userGroups;
	}

	@Required
	public void setResponsibilityService(ResponsibilityService responsibilityService) {
		this.responsibilityService = responsibilityService;
	}

	public SeedData() {
		add(sourceSystem("FUR","FindUs.Rail"));
		add(sourceSystem("RAPID","Rapid"));
		SourceSystem sso = add(sourceSystem("SSO","Single Sign On"));
		add(sourceSystem("CRM","Customer Relationship Management"));
		add(responsibility(null,RuleNumberType.DEFAULT, null, null,IdentityType.SsoRole,SsoRoleType.DataSteward.roleId(), 10000));
		add(responsibility(sso, RuleNumberType.DEFAULT, null, null, IdentityType.SsoId, "sdtxs01", 900));
		add(responsibility(sso, RuleNumberType.SINGLE, 1L, null, IdentityType.SsoId, "itvxm01", 600));
		
		
		
		
//		for (int i=0;i < 10;i++) {
//			add(dataException(fur, i));
//		}
	}
	
	@SuppressWarnings("unused")
	private void add(DataException dataException) {
		this.exceptions.add(dataException);
	}

	private void add(Responsibility responsibility) {
		responsibilities.add(responsibility);
	}

	private SourceSystem add(SourceSystem sourceSystem) {
		ss.add(sourceSystem);
		return sourceSystem;
	}

	private SourceSystem sourceSystem(String id, String name) {
		return new SourceSystem(id,name);
	}

	private Responsibility responsibility(SourceSystem sourceSystem, RuleNumberType ruleNumberType,Long ruleNumberFrom, Long ruleNumberThru, IdentityType identityType, String identityId, int precedence) {
		Responsibility r = new Responsibility();
		r.setResponsiblePerson(new Identity(identityType, identityId));
		r.setRuleNumberType(ruleNumberType);
		r.setRuleNumberFrom(ruleNumberFrom);
		r.setRuleNumberThru(ruleNumberThru);
		r.setSourceSystem(sourceSystem);
		r.setResponsiblePersonType(identityType);
		r.setResponsiblePersonId(identityId);
		r.setPrecedence(precedence);
		return r;
	}

	
	
	@SuppressWarnings("unused")
	private DataException dataException(SourceSystem fur, long idx) {
		String suffix = String.valueOf(idx);
		if (idx == Long.MIN_VALUE) {
			suffix = "";
		}
		InboundMessage rim = new InboundMessage();
		rim.setData("seed data...");
		rim.setSource(InboundSource.MDMException);
		
		DataException de1 = new DataException();
		de1.setMdmExceptionId(System.currentTimeMillis());
		de1.setSource(rim);
		de1.setId(idx);
		de1.setSourceSystem(fur);
		de1.setResponsiblePerson(new Identity(IdentityType.SsoId, "sso" + suffix));
		de1.setSourceSystemValue("ssvalue" + suffix);
		de1.setSourceSystemKey("sskeyval" + suffix);
		de1.setSourceSystemKeyColumn("sskeycol" + suffix);
		de1.setSourceSystemObjectData("ssobjdata" + suffix);
		de1.setRuleNumber(idx+1000);
		de1.setMdmObjectType("mdmobjtype" + suffix);
		de1.setMdmObjectAttribute("mdmobjattr" + suffix);
		de1.setMdmAttributeValue("mdmattvalue" + suffix);
		
		return de1;
	}

	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			for (SourceSystem s : ss) {
				SourceSystem x = sourceSystems.get(s.getIdentifier());
				if (x == null) {
					logger.info("saving {}", s);
					sourceSystems.save(s);
				}
			}
			for (Responsibility s : responsibilities) {
				if (s.getSourceSystem() != null) {
					s.setSourceSystem(sourceSystems.get(s.getSourceSystem().getIdentifier()));
				}
				ResponsibilityCriteria queryByExample = new ResponsibilityCriteria(s);
				Responsibility x = Iterables.getFirst( responsibilityService.all(queryByExample), null);
				if (x == null) {
					logger.info("saving {}", s);
					responsibilityService.save(s);
				}
			}
			for (DataException d : exceptions) {
				DataException r = dataExceptionService.get(d.getId());
				if (r == null) {
					d.setSourceSystem(sourceSystems.get(d.getSourceSystem().getIdentifier()));
					messageService.save(d.getSource());

					Task task = new ExceptionRemediationTask();
					task.addDataException(d);
					task.setCandidate(new Identity(IdentityType.SsoRole, SsoRoleType.DataSteward.roleId()));
					taskService.save(task);

					dataExceptionService.save(d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error during afterPropertiesSet", e);
		}
	}
	
}
