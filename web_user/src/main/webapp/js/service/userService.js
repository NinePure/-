//服务层
app.service('userService', function ($http) {

    //上传图片
    this.uploadFile = function(){
        // 向后台传递数据: 创建form表单对象
        var formData = new FormData();
        // 向formData中添加数据:
        formData.append("file",up_img_WU_FILE_0.files[0]);

        return $http({
            method:'post',
            url:'../upload/uploadFile.do',
            data:formData,
            headers:{'Content-Type':undefined} ,// Content-Type : text/html  text/plain
            transformRequest: angular.identity
        });
    }
    //读取列表数据绑定到表单中
    this.findAll = function () {
        return $http.get('../user/findAll.do');
    }
    //分页
    this.findPage = function (page, rows) {
        return $http.get('../user/findPage.do?page=' + page + '&rows=' + rows);
    }
    //查询实体
    this.findOne = function (id) {
        return $http.get('../user/findOne.do?id=' + id);
    }
    //增加
    this.add = function (entity, smscode) {
        return $http.post('../user/add.do?smscode=' + smscode, entity);
    }
    //修改
    this.update = function (entity) {
        return $http.post('../user/update.do', entity);
    }
    //删除
    this.dele = function (ids) {
        return $http.get('../user/delete.do?ids=' + ids);
    }
    //搜索
    this.search = function (page, rows, searchEntity) {
        return $http.post('../user/search.do?page=' + page + "&rows=" + rows, searchEntity);
    }
    //发送验证码
    this.sendCode = function (phone) {
        return $http.get('../user/sendCode.do?phone=' + phone);
    }
    //添加用户详情
    this.addSpec = function (entity) {
        return $http.post('../user/addSpec.do', entity);
    }
    this.findAddress = function () {
        return $http.post('../user/findAddress.do');
    }
    this.saveAddress = function (entity) {
        return $http.post('../user/saveAddress.do', entity);
    }
    this.setDefault = function (id) {
        return $http.post('../user/setDefault.do?id='+id);
    }
    this.findCollect = function () {
        return $http.post('../user/findCollect.do');
    }

});
