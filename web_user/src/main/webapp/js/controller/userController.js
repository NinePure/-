 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService,loginService){
    $controller('baseController',{$scope:$scope});//继承
	//注册用户

    $scope.uploadPic = function() {
        alert(123);
        // 调用userService的方法完成文件的上传
        userService.uploadFile().success(function (response) {
            if (response.success) {
                // 获得url
                alert(response.message);
            } else {
                alert(response.message);
            }
        });
    };

    $scope.showName=function(){
        loginService.showName().success(
            function(response){
                $scope.loginName=response.loginName;
            }
        );
    }
    $scope.addSpec=function(){
        userService.addSpec($scope.entity).success(
            function(response){
                alert(response.message);
            }
        );
    }
    //注册
	$scope.reg=function(){
		
		//比较两次输入的密码是否一致
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入");
			$scope.entity.password="";
			$scope.password="";
			return ;			
		}
		//新增
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				alert(response.message);
			}		
		);
	}
    
	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}
		
		userService.sendCode($scope.entity.phone  ).success(
			function(response){
				alert(response.message);
			}
		);		
	}
	
});	
