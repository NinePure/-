// 定义控制器:
app.controller("userController",function($scope,$controller,$http,userService){
	// AngularJS中的继承:伪继承
	$controller('baseController',{$scope:$scope});

	
	// 查询一个:
	$scope.findById = function(id){
        userService.findById(id).success(function(response){
            // {id:xx,name:yy,firstChar:zz}
            $scope.entity = response;
        });
    }
	
	// 冻结用户:
	$scope.frozenUser = function(){
		userService.frozenUser($scope.selectIds).success(function(response){
			// 判断冻结是否成功:
			if(response.success==true){
				// 冻结成功
				alert(response.message);
				$scope.reloadList();
				$scope.selectIds = [];
			}else{
				// 冻结失败
				alert(response.message);
			}
		});
	}
	
	$scope.searchEntity={};
	
	// 假设定义一个查询的实体：searchEntity
	$scope.search = function(page,rows){
		// 向后台发送请求获取数据:
		userService.search(page,rows,$scope.searchEntity).success(function(response){
			$scope.paginationConf.totalItems = response.total;
			$scope.list = response.rows;
		});
	}


    $scope.wau = function(){
        userService.wau().success(function(response){

            $scope.number = response;
        });
    }

    $scope.noWau = function(){
        userService.noWau().success(function(response){

            $scope.noNumber = response;
        });
    }

});
