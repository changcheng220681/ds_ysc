//服务层
app.service('addressService',function($http){

    this.findByProvinces = function(){
        return $http.get("../address/findByProvinces.do");
    }
    this.findByCitiesId = function(parentId){
        return $http.get("../address/findByCitiesId.do?parentId="+parentId);
    }
    this.findByAreasId = function(parentId){
        return $http.get("../address/findByAreasId.do?parentId="+parentId);
    }
    //获取当前登录账号的收货地址
    this.findAddressList=function(){
        return $http.get('../address/findListByLoginUser.do');
    }
    this.add = function(entity){
        return $http.post("../address/add.do",entity);
    }

    this.update=function(entity){
        return $http.post("../address/update.do",entity);
    }
    this.findOne=function(id){
        return $http.get("../address/findOne.do?id="+id);
    }
});
