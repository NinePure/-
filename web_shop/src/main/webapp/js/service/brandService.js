//服务层
app.service('brandService',function($http){

	//增加 
	this.brandadd=function(entity){
		return  $http.post('../brand/brandadd.do?',entity );
	}

});
