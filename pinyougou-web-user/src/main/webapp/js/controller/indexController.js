//首页控制器
app.controller('indexController',function($scope,loginService){
	$scope.showName=function(){

			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;
					}
			);
	};
	$scope.findByOrderList=function (status) {
			loginService.findByOrderList(status).success(
					function (response) {
						$scope.orderVoList=response;
                    }
			)
    };
    /**
     * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
     */
    // 显示状态
    $scope.status = ["","未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"];

    //修改个人信息
    $scope.save = function(){
        // 区分是保存还是修改
        var object;
        if($scope.entity.id != null){
            // 更新
            object = loginService.update($scope.entity);
        }else{
            // 保存
            object = loginService.add($scope.entity);
        }
        object.success(function(response){
            // {flag:true,message:xxx}
            // 判断保存是否成功:
            if(response.flag){
                // 保存成功
                alert(response.message);
                location.href="http://localhost:9004/home-index.html";
            }else{
                // 保存失败
                alert(response.message);
            }
        });
    }
    // 查询一个:
    $scope.findById = function(){
        brandService.findOne().success(function(response){
            // {id:xx,name:yy,firstChar:zz}
            $scope.entity = response;
        });
    }

});