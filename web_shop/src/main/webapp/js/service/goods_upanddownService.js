//服务层
app.service('goods_upanddownService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../goods_upanddown/findAll.do');
	}
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../goods_upanddown/findPage.do?page='+page+'&rows='+rows);
	}
	//查询实体
	this.findOne=function(id){
		return $http.get('../goods_upanddown/findOne.do?id='+id);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../goods_upanddown/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../goods_upanddown/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../goods_upanddown/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../goods_upanddown/search.do?page='+page+"&rows="+rows, searchEntity);
	}
	/*上架状态修改*/
    this.updateIsMarketable = function(ids,isMarketable){
        return $http.get('../goods_upanddown/updateIsMarketable.do?ids='+ids+"&isMarketable="+isMarketable);
    }
});
