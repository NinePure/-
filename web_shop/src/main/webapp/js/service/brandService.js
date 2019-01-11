//服务层
app.service('brandService',function($http){

	//增加 
	this.add=function(entity){
		return  $http.post('../brand/add.do?',entity );
	}

});
