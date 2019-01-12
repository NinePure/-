 //控制层 
app.controller('brandController' ,function($scope,$controller   ,brandService){
	
	$controller('baseController',{$scope:$scope});//继承
	

	//保存
	$scope.brandadd = function(){
        brandService.brandadd( $scope.entity).success(
			function(response){
				if(response.success){
					// 重新查询 
		        	// $scope.reloadList();//重新加载
                    alert(response.message);
					location.href="brand.html";
				}else{
					alert(response.message);
				}
			}		
		);	
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		sellerService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds = [];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		sellerService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
});	
