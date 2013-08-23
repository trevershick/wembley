
//		return "${oscarContextPath}${oscarServletPath}/api/list";


angular.module('sbconsole', [])
	.config(function($routeProvider,$locationProvider) {
		console.log("OK, setup route...");
		$routeProvider.when('/jobs', {});
		$routeProvider.when('/jobs/:jobName', {});
		$routeProvider.when('/history/jobs/:historicalJobName', {});
		$routeProvider.when('/history/jobs/:historicalJobName/:instanceId', {});
		$routeProvider.otherwise({redirectTo: 'jobs'});
	}).directive("raFadeOut", function() {
		return {
			scope:false,
		    link: function(scope, element, attrs) {
		    	var delay = attrs.delay || 1000;
		    	var speed = attrs.speed || 500;
		    	$(element).delay(delay).fadeOut(speed);
		    }
		};
	}).service("messageService", function($timeout) {
		this.messages = [];
		this.error = function(o) {
			var msgs = this.messages;
			if (angular.isString(o)) {
				msgs.push({ text:o, error:true });
				$timeout(function(){ msgs.shift(); }, 5000);
			}
			if (angular.isObject(o) && o.errors) {
				for (idx in o.errors) {
					var e = o.errors[idx];
					if (e.message) {
						this.error(e.message);
					}
				}
			}
		};
		this.info = function(txt) {
			var self = this;
			this.messages.push({ text:txt, error:false });
			$timeout(function(){ self.messages.shift(); }, 5000);
		};
	}).service("batchService", function($http,$q,$log, messageService) {
		this.jobs = [];
		this.historicalJobs = [];
		
		this.loadCurrentJobs = function() {
			var self = this;
			$log.info("URL ", this.currentJobsUrl);
			$http.get(this.currentJobsUrl).success(function(data){
				$log.info(data.jobs);
				self.jobs.length = 0;
				angular.forEach(data.jobs, function(j) { self.jobs.push(j); });
			});
		};
		this.loadHistoricalJobs = function() {
			var self = this;
			$log.info("URL ", this.historicJobsUrl);
			$http.get(this.historicJobsUrl).success(function(data){
				$log.info(data.jobs);
				self.historicalJobs.length = 0;
				angular.forEach(data.jobs, function(j) { self.historicalJobs.push(j); });
			}).error(function(x) { messageService.error(x); });
		};
		this.instances = function(jobName,start,count) {
			var deferred = $q.defer();
			$http.get(this.historicInstancesUrl(jobName,start,count)).success(function(data){
				deferred.resolve({ name: jobName, instances: data.instances });
			}).error(function(data) {
				deferred.reject(data);
			});
			return deferred.promise;
		};
		this.executions = function(instanceId) {
			var deferred = $q.defer();
			$http.get(this.historicExecutionsUrl(instanceId)).success(function(data){
				$log.info(data);
				deferred.resolve(data.instance);
			}).error(function(data) {
				deferred.reject(data);
			});
			return deferred.promise;
		};
		this.startJob = function(jobName, parameters) {
			var deferred = $q.defer();
			$http.post(this.jobStartUrl(jobName), parameters).success(function(data){
				$log.info(data);
				deferred.resolve(data.instance);
			}).error(function(data) {
				deferred.reject(data);
			});
			return deferred.promise;
		};
		this.restart = function(executionId) {
			return this.executeCommand(executionId, "restart");
		};
		this.abandon = function(executionId) {
			return this.executeCommand(executionId, "abandon");
		};
		this.pause = function(executionId) {
			return this.executeCommand(executionId, "pause");
		};
		this.executeCommand = function(executionId, command) {
			var url = this.executionCommandUrl(executionId, command);
			var deferred = $q.defer();
			$http.get(url).success(function(data){
				$log.info(data);
				deferred.resolve(data.instance);
			}).error(function(data) {
				deferred.reject(data);
			});
			return deferred.promise;
		};
		
		
		this.pauseExecution = function(eid){};
		this.restartExecution = function(eid){};
		this.abandonExecution = function(eid){};
		this.currentJobsUrl = "${oscarContextPath}${oscarServletPath}/api/jobs";
		this.historicJobsUrl = "${oscarContextPath}${oscarServletPath}/api/history/jobs";
		this.historicInstancesUrl = function(jobName,s,c) { return "${oscarContextPath}${oscarServletPath}/api/history/jobs/" + jobName+ "?s=" + s + "&c=" + c; };
		this.historicExecutionsUrl = function(instanceId) { return "${oscarContextPath}${oscarServletPath}/api/history/instances/" + instanceId ; };
		this.jobStartUrl = function(jobName) { return "${oscarContextPath}${oscarServletPath}/api/jobs/" + jobName; };
		
		this.executionCommandUrl = function(executionId, command) { return "${oscarContextPath}${oscarServletPath}/api/executions/" + executionId + "/" + command; };
		this.loadCurrentJobs();
		this.loadHistoricalJobs();
	}).controller("ConsoleController", function($scope, $http, $log, $route, $routeParams, $location, $interpolate, batchService, messageService) {
		$scope.jobs = batchService.jobs;
		$scope.historicalJobs = batchService.historicalJobs;
		$scope.selectedHistoricJob = null;
		$scope.selectedJob = null;
		$scope.jobParameters = "x=x";
		$scope.instancePage = {
			counts : [25,10,5],
			count : 5 ,
			start : 0,
			starts: [0],
			prev: function() { 
				var i = this.start-1;
				i = i< 0 ? 0 :i;
				this.start = i;
			},
			next: function() { 
				if ($scope.instances.length < this.count) { return; }
				var i = this.start+1;
				this.start = i;
				this.starts.push(i);
			},
			go: function(idx) { this.start = idx; }
		};
		$scope.instances = [];
		$scope.executions = [];
		$scope.selectedInstance = null;
		$scope.messages = messageService.messages;
		
		$scope.$watch("instancePage.start", function(){
			$scope.selectHistoricJob($scope.selectedHistoricJob,true);
		});
		$scope.$watch("instancePage.count", function(){
			$scope.instancePage.start = 0;
			$scope.instancePage.starts.length = 0;
			$scope.instancePage.starts.push(0);
			$scope.selectHistoricJob($scope.selectedHistoricJob,true);
		});
		$scope.$on("$routeChangeSuccess", function( $evt, $current, $previous ){
                $log.info("on render", $current);
                var p = $current.params;
                if (p.jobName) {
               		$scope.selectJob(p.jobName);
                }
                if (p.historicalJobName) {
               		$scope.selectHistoricJob(p.historicalJobName);
                }
                if (p.instanceId) {
                	$scope.selectHistoricInstance(p.instanceId);
                }
		});
		
		$scope.submitJob = function() {
			var data = {};
			var jp = $scope.jobParameters;
			var kvs = jp.split(/\s+/);
			for (kv in kvs) {
				var kvsplit = kvs[kv].split(/=/);
				if (kvsplit.length != 2) {
					alert("Invalid Parameter value : " + kvs[kv]);
				}
				data[kvsplit[0]] = kvsplit[1];
			}
			$log.info("selectedJob = ", $scope.selectedJob);
			$log.info("data=",data);
			$log.info(batchService.startJob);
			batchService.startJob($scope.selectedJob, data).then(
					function(x) { 
						$log.info("result=", x); 
						var exp = $interpolate("/history/jobs/{{jobName}}/{{instanceId}}");
						var p = exp({jobName:$scope.selectedJob, instanceId:x.id});
				        $location.path(p); 
						messageService.info("Successfully started the job instance"); 
					},
					function(x) { messageService.error(x); }
			);
		};
		$scope.pause = function(execution) {
			batchService.pause(execution.id).then(
				function(x) { $log.info("result=", x); messageService.info("Successfully paused the execution"); },
				function(x) { messageService.error(x); }
			);
		};
		$scope.abandon = function(execution) {
			batchService.abandon(execution.id).then(
					function(x) { $log.info("result=", x); messageService.info("Successfully abandoned the execution"); },
					function(x) { messageService.error(x); }
				);
		};
		$scope.restart = function(execution) {
			batchService.restart(execution.id).then(
					function(x) { $log.info("result=", x); messageService.info("Successfully restarted the execution"); },
					function(x) { messageService.error(x); }
				);		
		};
		$scope.selectHistoricInstance = function(instanceId) {
			$scope.executions.length = 0;
			batchService.executions(instanceId).then(function(instance) {
				$log.info("instance=", instance);
				$scope.executions = instance.executions;
				$scope.selectedInstance = instance;
			});
		};
		$scope.selectHistoricJob = function(jobName,force) {
			var forceIt = angular.isDefined(force) && force === true;
			if (!forceIt && $scope.selectedHistoricJob === jobName) return;
			$scope.selectedJob = null;
			$scope.selectedInstance = null;
			$scope.instances.length = 0;
			$scope.executions.length = 0;
			$log.info($scope.instancePage.start * $scope.instancePage.count);
			$log.info($scope.instancePage.count);
			batchService.instances(jobName, $scope.instancePage.start * $scope.instancePage.count, $scope.instancePage.count).then(function(job) {
				$log.info(job);
				$scope.selectedHistoricJob = jobName;
				$scope.instances = job.instances;
			});
		};
		$scope.selectJob = function(jobName) {
			if ($scope.selectedJob === jobName) return;
			$scope.selectedJob = jobName;
			$scope.selectedHistoricJob = null;
			$scope.selectedInstance = null;
			$scope.instances.length = 0;
			$scope.executions.length = 0;
		};
	});
