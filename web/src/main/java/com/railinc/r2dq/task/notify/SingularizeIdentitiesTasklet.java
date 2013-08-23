package com.railinc.r2dq.task.notify;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.identity.IdentityService;

public class SingularizeIdentitiesTasklet implements Tasklet {


	private final Logger log = LoggerFactory.getLogger(getClass());
	private IdentityService identityService;
	private Resource resource;
	private Resource output;



	public Resource getOutput() {
		return output;
	}
	@Required
	public void setOutput(Resource output) {
		this.output = output;
	}

	public Resource getResource() {
		return resource;
	}
	
	@Required
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		FlatFileItemReader<Identity> reader = new FlatFileItemReader<Identity>();
		DefaultLineMapper<Identity> dlm = new DefaultLineMapper<Identity>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(',');
		tokenizer.setNames(new String[]{"type","id"});
		dlm.setLineTokenizer(tokenizer);
		
		
		dlm.setFieldSetMapper(new IdentityFieldSetMapper());
		reader.setLineMapper(dlm);
		reader.setResource(resource);
		reader.open(new ExecutionContext());
		Identity i = null;
		
		Collection<Identity> identities = newHashSet();
		while ((i =reader.read()) != null) {
			identities.add(i);
		}
		reader.close();
		
		
		int sz = identities.size();
		for (int idx=0;idx<sz;idx++) {
			contribution.incrementReadCount();
		}
		log.info("Singularize {}", identities);
		
		Collection<Identity> singularIdentities = this.identityService.resolveToSingularIdentities(identities);
		contribution.incrementFilterCount(singularIdentities.size() - sz);
		contribution.incrementWriteCount(singularIdentities.size());
		
		FlatFileItemWriter<Identity> w = new FlatFileItemWriter<Identity>();
		DelimitedLineAggregator<Identity> a = new DelimitedLineAggregator<Identity>();
		BeanWrapperFieldExtractor<Identity> e = new BeanWrapperFieldExtractor<Identity>();
		e.setNames(new String[]{"type","id"});
		a.setFieldExtractor(e);
		w.setLineAggregator(a);
		w.setResource(output);
		w.open(new ExecutionContext());
		w.write(newArrayList(singularIdentities));
		w.close();
		
		return RepeatStatus.FINISHED;
	}

	public IdentityService getIdentityService() {
		return identityService;
	}

	@Required
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}


}
