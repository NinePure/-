//服务层
app.service('newitemcatService',function($http){

	//增加 
	this.itemcatadd=function(entity){
		return  $http.post('../newitemcat/itemcatadd.do?',entity );
	}

});
