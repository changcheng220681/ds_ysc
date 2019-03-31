//服务层
app.service('loginService',function($http){
	//读取列表数据绑定到表单中
	this.showName=function(){
		return $http.get('../login/name.do');
	}
	this.findByOrderList=function (status) {
		return $http.get('../user/findByOrderList.do?status='+status);
    }
    this.add = function(entity){
        return $http.post("../brand/add.do",entity);
    }
    this.update=function(entity){
        return $http.post("../brand/update.do",entity);
    }
    this.findOne=function(){
        return $http.get("../brand/findOne.do");
    }

});