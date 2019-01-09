//服务层
app.service('sellMoneyService',function($http){
	    	

	//搜索
	this.search=function(page,rows,startTime,endTime){
		return $http.post('../order/findSellMoney.do?page='+page+"&rows="+rows+"&startTime="+startTime+"&endTime="+endTime);
	}    	
});
