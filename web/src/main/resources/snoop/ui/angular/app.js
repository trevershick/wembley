angular.module('snoop', []).controller("SnoopController", function($scope, $http, $log) {
	$scope.snoop = {};
		

	$scope.snoopUrl = "${oscarContextPath}${oscarServletPath}/api/snoop";
	$http.get($scope.snoopUrl).success(function(data){
		$log.info("data=", data);
		$scope.snoop = data;
	});

});
