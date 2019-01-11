 //控制层 
app.controller('newitemcatController' ,function($scope,$controller   ,newitemcatService){
	
	$controller('baseController',{$scope:$scope});//继承

	$scope.itemcatadd = function(){
        newitemcatService.itemcatadd( $scope.entity).success(
			function(response){
				if(response.success){
					// 重新查询 
		        	// $scope.reloadList();//重新加载
                    alert(response.message);
					location.href="itemcat.html";
				}else{
					alert(response.message);
				}
			}		
		);	
	}

});	
