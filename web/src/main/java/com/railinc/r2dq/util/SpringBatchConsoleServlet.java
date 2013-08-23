package com.railinc.r2dq.util;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.railinc.common.oscar.OscarHandler;
import com.railinc.common.oscar.OscarHandlerMapping;
import com.railinc.common.oscar.json.OscarJsonServiceServlet;
import com.railinc.r2dq.util.bc.ExecutionOperation;
import com.railinc.r2dq.util.bc.ExecutionTransformer;
import com.railinc.r2dq.util.bc.InstanceTransformer;

/**
 * Servlet implementation class SpringBatchConsoleServlet
 */
public class SpringBatchConsoleServlet extends OscarJsonServiceServlet {

	private transient JobRepository repository = null;
	private transient JobExplorer explorer = null;
	private transient JobOperator operator = null;
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2590483813948012398L;


	
	

	@Override
	public String getBundlePrefix() {
		return "sbconsole";
	}


	


	
	

	@SuppressWarnings("unchecked")
	@OscarHandlerMapping(regex="GET:/api/jobs")
	public OscarHandler<JSONObject> apiJobs() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				Set<String> jobNames = operator().getJobNames();
				JSONArray arr = new JSONArray();
				arr.addAll(jobNames);
				outputEntity.put("jobs", arr);
				outputEntity.put("description", "Jobs which have registered definitions");
				
				return HttpServletResponse.SC_OK;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@OscarHandlerMapping(regex="GET:/api/history/jobs")
	public OscarHandler<JSONObject> apiHistoryJobs() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				List<String> jobNames = explorer().getJobNames();
				JSONArray arr = new JSONArray();
				arr.addAll(jobNames);
				outputEntity.put("jobs", arr);
				outputEntity.put("description", "Jobs which have been run, or at least started.");
				return HttpServletResponse.SC_OK;
			}
		};
	}
	
	@OscarHandlerMapping(regex="GET:/api/jobs/([^/]+)/start")
	public OscarHandler<JSONObject> apiJobsJobStart() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				
				String jobName = stringPathParameter(inputEntity, 0);
				try {
					Long start = operator().start(jobName, req.getQueryString());
					JobExecution jobExecution = explorer().getJobExecution(start);

					ArrayList<JobExecution> es = new ArrayList<JobExecution>();
					es.add(jobExecution);
					renderInstanceAndExecutions(outputEntity, jobExecution.getJobInstance(), es);
				} catch (NoSuchJobException e) {
					return HttpServletResponse.SC_NOT_FOUND;
				} catch (JobInstanceAlreadyExistsException e) {
					return HttpServletResponse.SC_CONFLICT;
				} catch (JobParametersInvalidException e) {
					return HttpServletResponse.SC_BAD_REQUEST;
				} catch (Exception e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
				return HttpServletResponse.SC_OK;
			}};
	}

	@OscarHandlerMapping(regex="POST:/api/jobs/([^/]+)")
	public OscarHandler<JSONObject> apiJobPost() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				String jobName = stringPathParameter(inputEntity, 0);
				try {
					StringBuilder params = new StringBuilder();
					JSONObject ps = (JSONObject) inputEntity.get("entity");
					for (Object k : ps.keySet()) {
						Object v = ps.get(k);
						params.append(String.format("%s=%s", String.valueOf(k), String.valueOf(v)));
					}
					Long start = operator().start(jobName, params.toString());
					JobExecution jobExecution = explorer().getJobExecution(start);
					ArrayList<JobExecution> es = new ArrayList<JobExecution>();
					es.add(jobExecution);
					renderInstanceAndExecutions(outputEntity, jobExecution.getJobInstance(), es);
				} catch (NoSuchJobException e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_NOT_FOUND;
				} catch (JobInstanceAlreadyExistsException e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_CONFLICT;
				} catch (JobParametersInvalidException e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_BAD_REQUEST;
				} catch (Exception e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
				return HttpServletResponse.SC_OK;
			}};
	}
	
	/**
	 * return running instances
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@OscarHandlerMapping(regex="GET:/api/jobs/([^/]+)")
	public OscarHandler<JSONObject> apiJobsGet() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				
				String jobName = stringPathParameter(inputEntity, 0);
				if (! operator().getJobNames().contains(jobName)) {
					return HttpServletResponse.SC_NOT_FOUND;
				}
				
				Set<JobExecution> executions = explorer().findRunningJobExecutions(jobName);
				ExecutionTransformer xformer = new ExecutionTransformer();
				JSONArray arr = new JSONArray();
				for (JobExecution je : executions) {
					arr.add(xformer.apply(je));
				}
				outputEntity.put("description", "Running Executions for " + jobName);
				outputEntity.put("executions", arr);
				return HttpServletResponse.SC_OK;
			}};
	}


	@SuppressWarnings("unchecked")
	@OscarHandlerMapping(regex="GET:/api/history/jobs/([^/]+)")
	public OscarHandler<JSONObject> apiHistoryJobInstances() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				String jobName = stringPathParameter(inputEntity, 0);
				int start = intParameter(inputEntity, "s",0);
				int count = intParameter(inputEntity, "c",20);
				
				List<JobInstance> instances = explorer().getJobInstances(jobName, start, count);
				InstanceTransformer xformer = new InstanceTransformer();
				JSONArray arr = new JSONArray();
				Set<JobExecution> running = explorer().findRunningJobExecutions(jobName);
				
				
				
				for (JobInstance je : instances) {
					JSONObject io = xformer.apply(je);
					boolean instanceIsRunning = false;
					for (JobExecution r : running) {
						if (je.getId() == r.getJobId()) {
							instanceIsRunning = true;
							break;
						}
					}
					io.put("running", instanceIsRunning);
					arr.add(io);
				}
				outputEntity.put("description", "Instances for " + jobName);
				outputEntity.put("instances", arr);

				
				return HttpServletResponse.SC_OK;
			}};
	}
	
	protected int intParameter(JSONObject inputEntity, String string, int i) {
		try {
			JSONObject params = (JSONObject) inputEntity.get("params");
			return Integer.valueOf((String) params.get(string));
		} catch (Throwable e) {
			return i;
		}
	}








	@SuppressWarnings("unchecked")
	@OscarHandlerMapping(regex="GET:/api/history/instances/(\\d+)")
	public OscarHandler<JSONObject> apiHistoryInstance() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				long instanceId = intPathParameter(inputEntity, 0);
				JobInstance instance = explorer().getJobInstance(instanceId);
				List<JobExecution> executions = explorer().getJobExecutions(instance);

				renderInstanceAndExecutions(outputEntity, instance, executions);
				outputEntity.put("description", "Executions for " + instance.getJobName() + " instance id " + instanceId);
				return HttpServletResponse.SC_OK;
			}

		};
	}

	

	
	@OscarHandlerMapping(regex="(?:POST|GET):/api/executions/(\\d+)/pause")
	public OscarHandler<JSONObject> apiHistoryJobExecution() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				long executionId = intPathParameter(inputEntity, 0);

				JobExecution jobExecution = explorer().getJobExecution(executionId);
				ExecutionOperation availableCommand = ExecutionOperation.getAvailableCommand(jobExecution.getStatus());
				if (availableCommand != ExecutionOperation.PAUSE) {
					onError(outputEntity, "'Pause' is not a valid command for this execution");
					return HttpServletResponse.SC_FORBIDDEN;
				}

				try {
					operator().stop(executionId);
				} catch (NoSuchJobExecutionException e1) {
					onException(outputEntity, e1);
					return HttpServletResponse.SC_NOT_FOUND;
				} catch (JobExecutionNotRunningException e1) {
					onException(outputEntity, e1);
					return HttpServletResponse.SC_FORBIDDEN;
				} catch (Exception e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
				return HttpServletResponse.SC_OK;
			}};
	}

	@OscarHandlerMapping(regex="(?:POST|GET):/api/executions/(\\d+)/abandon")
	public OscarHandler<JSONObject> apiExecutionAbandon() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				long executionId = intPathParameter(inputEntity, 0);

				JobExecution jobExecution = explorer().getJobExecution(executionId);
				ExecutionOperation availableCommand = ExecutionOperation.getAvailableCommand(jobExecution.getStatus());
				if (availableCommand != ExecutionOperation.ABANDON) {
					onError(outputEntity, "'Abandon' is not a valid command for this execution");
					return HttpServletResponse.SC_FORBIDDEN;
				}
				// pause
				JobRepository repository = repository();
				
				try {
					operator().stop(executionId);
				} catch (NoSuchJobExecutionException e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_NOT_FOUND;
				} catch (JobExecutionNotRunningException e) {
					jobExecution.setEndTime(new Date());
					jobExecution.upgradeStatus(BatchStatus.ABANDONED);
					repository.update(jobExecution);
					Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
					for (StepExecution se : stepExecutions) {
						se.setEndTime(new Date());
						se.upgradeStatus(BatchStatus.ABANDONED);
						repository.update(se);
					}
				} catch (Exception e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
				return HttpServletResponse.SC_OK;
			}};
	}
	
	
	@OscarHandlerMapping(regex="(?:POST|GET):/api/executions/(\\d+)/restart")
	public OscarHandler<JSONObject> apiExecutionRestart() {
		return new OscarHandler<JSONObject>() {
			@Override
			public int handle(HttpServletRequest req, HttpServletResponse resp,
					JSONObject inputEntity, JSONObject outputEntity) throws IOException {
				long executionId = intPathParameter(inputEntity, 0);

				JobExecution jobExecution = explorer().getJobExecution(executionId);
				ExecutionOperation availableCommand = ExecutionOperation.getAvailableCommand(jobExecution.getStatus());
				if (availableCommand != ExecutionOperation.RESTART) {
					onError(outputEntity, "'Restart' is not a valid command for this execution");
					return HttpServletResponse.SC_FORBIDDEN;
				}
				// pause
				JobRepository repository = repository();
				
				try {
					operator().restart(executionId);
				} catch (JobInstanceAlreadyCompleteException e1) {
					onException(outputEntity, e1);
					return HttpServletResponse.SC_CONFLICT;
				} catch (NoSuchJobExecutionException e1) {
					onException(outputEntity, e1);
					return HttpServletResponse.SC_NOT_FOUND;
				} catch (NoSuchJobException e1) {
					onException(outputEntity, e1);
					return HttpServletResponse.SC_NOT_FOUND;
				} catch (JobRestartException e1) {
					onException(outputEntity, e1);
					return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				} catch (JobParametersInvalidException e1) {
					onException(outputEntity, e1);
					return HttpServletResponse.SC_BAD_REQUEST;
				} catch (Exception e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}

				try {
					operator().stop(executionId);
				} catch (NoSuchJobExecutionException e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_NOT_FOUND;
				} catch (JobExecutionNotRunningException e) {
					jobExecution.setEndTime(new Date());
					jobExecution.upgradeStatus(BatchStatus.ABANDONED);
					repository.update(jobExecution);
					Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
					for (StepExecution se : stepExecutions) {
						se.setEndTime(new Date());
						se.upgradeStatus(BatchStatus.ABANDONED);
						repository.update(se);
					}

				} catch (Exception e) {
					onException(outputEntity, e);
					return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				}
				return HttpServletResponse.SC_OK;
			}
		};
	}

	
	@SuppressWarnings("unchecked")
	private void renderInstanceAndExecutions(JSONObject outputEntity,
			JobInstance instance, List<JobExecution> executions) {
		InstanceTransformer ixformer = new InstanceTransformer();
		ExecutionTransformer exformer = new ExecutionTransformer(true,true);
		JSONObject instanceEntity = ixformer.apply(instance);
		JSONArray arr = new JSONArray();
		for (JobExecution je : executions) {
			arr.add(exformer.apply(je));
		}

		outputEntity.put("instance", instanceEntity);
		instanceEntity.put("executions", arr);
	}

	



	private JobExplorer explorer() {
		JobExplorer tmp = explorer;
		if (tmp == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
			tmp = ctx.getBean(JobExplorer.class);
			explorer = tmp;
		}
		return tmp;
	}


	private JobOperator operator() {
		JobOperator tmp = operator;
		if (tmp == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
			tmp = ctx.getBean(JobOperator.class);
			operator = tmp;
		}
		return tmp;
	}


	private JobRepository repository() {
		JobRepository tmp = repository;
		if (tmp == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
			tmp = ctx.getBean(JobRepository.class);
			repository = tmp;
		}
		return tmp;
	}
	



}
