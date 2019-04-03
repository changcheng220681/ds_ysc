 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
	$scope.entity = {};
	//注册用户
	$scope.reg=function(){

	/*	if($scope.password == null || $scope.entity.password == null || $scope.entity.username == null){
            alert("密码不能为空");
            return ;
        }*/


		//比较两次输入的密码是否一致
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入");
			$scope.entity.password="";
			$scope.password="";
			return ;			
		}
		//新增
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				alert(response.message);
			}		
		);
	}
    
	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}
		
		userService.sendCode($scope.entity.phone  ).success(
			function(response){
				alert(response.message);
			}
		);		
	}
    //保存订单
    $scope.submitOrder=function(id){
        cartService.submitOrder(id).success(
            function(response){
                //alert(response.message);
                if(response.flag){
                    //页面跳转
                    if($scope.order.paymentType=='1'){//如果是微信支付，跳转到支付页面
                        location.href="pay.html";
                    }else{//如果货到付款，跳转到提示页面
                        location.href="paysuccess.html";
                    }
                }else{
                    alert(response.message);	//也可以跳转到提示页面
                }

            }
        );
    }
	
});	
