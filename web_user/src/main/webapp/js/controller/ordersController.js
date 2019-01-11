 //控制层 
app.controller('ordersController' ,function($scope,$controller,ordersService,loginService){
	
	$controller('baseController',{$scope:$scope});//继承
	// $controller('indexController',{$scope:$scope});//继承

    $scope.showName=function(){
        loginService.showName().success(
            function(response){
                $scope.loginName=response.loginName;
            }
        );
    }
    $scope.searchEntity={};//定义搜索对象
    //搜索
    $scope.search=function(page,rows){
    	ordersService.search(page,rows,$scope.searchEntity).success(
    		function(response){
    			$scope.list=response.rows;
    			$scope.paginationConf.totalItems=response.total;//更新总记录数
    		}
    	);
    }



	//
	// //保存
	// $scope.save=function(){
	// 	var serviceObject;//服务层对象
	// 	if($scope.entity.id!=null){//如果有ID
	// 		serviceObject=goodsService.update( $scope.entity ); //修改
	// 	}else{
	// 		serviceObject=goodsService.add( $scope.entity  );//增加
	// 	}
	// 	serviceObject.success(
	// 		function(response){
	// 			if(response.success){
	// 				//重新查询
	// 	        	$scope.reloadList();//重新加载
	// 			}else{
	// 				alert(response.message);
	// 			}
	// 		}
	// 	);
	// }
	//
	//
	// //批量删除
	// $scope.dele=function(){
	// 	//获取选中的复选框
	// 	goodsService.dele( $scope.selectIds ).success(
	// 		function(response){
	// 			if(response.success){
	// 				$scope.reloadList();//刷新列表
	// 				$scope.selectIds = [];
	// 			}
	// 		}
	// 	);
	// }
	//

    //
	// // 显示状态
	// $scope.status = ["未审核","审核通过","审核未通过","关闭"];
	//
	// $scope.itemCatList = [];
	// // 显示分类:
	// $scope.findItemCatList = function(){
	//
	// 	itemCatService.findAll().success(function(response){
	// 		for(var i=0;i<response.length;i++){
	// 			$scope.itemCatList[response[i].id] = response[i].name;
	// 		}
	// 	});
	// }
	//
	// // 审核的方法:
	// $scope.updateStatus = function(status){
	// 	ordersService.updateStatus($scope.selectIds,status).success(function(response){
	// 		if(response.success){
	// 			$scope.reloadList();//刷新列表
	// 			$scope.selectIds = [];
	// 		}else{
	// 			alert(response.message);
	// 		}
	// 	});
	// }
});	
