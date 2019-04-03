//服务层
app.service('loginService',function($http){
	//读取列表数据绑定到表单中
	this.showName=function(){
		return $http.get('../login/name.do');
	}
	this.findByOrderList=function (status) {
		return $http.get('../user/findByOrderList.do?status='+status);
    }

    this.update=function(entity){
        return $http.post("../user/update.do",entity);
    }
    this.submitOrder=function(orderId){
		return $http.get("../user/submitOrder.do?orderId="+orderId);
	}


});