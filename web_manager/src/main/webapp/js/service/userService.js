// 定义服务层:
app.service("userService",function($http){

	
	this.findById=function(id){
		return $http.get("../manageUser/findOne.do?id="+id);
	}
	
	this.frozenUser = function(ids){
		return $http.get("../manageUser/frozenUser.do?ids="+ids);
	}
	
	this.search = function(page,rows,searchEntity){
		return $http.post("../manageUser/search.do?page="+page+"&rows="+rows,searchEntity);
	}


    this.userCount = function(){
        return $http.get("../manageUser/userCount.do");
    }

    this.wau = function(){
        return $http.get("../manageUser/wau.do");
    }

    this.noWau = function(){
        return $http.get("../manageUser/noWau.do");
    }

});