//控制层
app.controller('sellMoneyController', function ($scope, $controller, $location, sellMoneyService) {

    $controller('baseController', {$scope: $scope});//继承

    $scope.groupData = ['测试栏目1','测试栏目2','测试栏目3','测试栏目4','测试栏目5','测试栏目6'];
    $scope.sellNum = [10,20,30];
    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        sellMoneyService.search(page, rows, $scope.searchEntity.startTime, $scope.searchEntity.endTime).success(
            function (response) {
                $scope.list = response.pageResult.rows;
                $scope.paginationConf.totalItems = response.pageResult.total;//更新总记录数
            }
        );
    }


    $scope.deleteStatus = ["上架中", "已下架"];


});

