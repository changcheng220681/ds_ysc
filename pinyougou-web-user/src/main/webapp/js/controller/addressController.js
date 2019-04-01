 //控制层 
app.controller('addressController' ,function($scope,addressService){
	$scope.entity = {};

    // 查询一级分类列表:
    $scope.findByProvinces = function(){
        //商品分类服务层  查询所有商品分类（父ID为0）
        addressService.findByProvinces().success(function(response){//List<ItemCat>
            $scope.provincesList = response;//List<ItemCat>
        });
    };

    // 查询二级分类列表:
    $scope.$watch("entity.provinceid",function(newValue,oldValue){
        addressService.findByCitiesId(newValue).success(function(response){
            $scope.citiesList = response;
        });
    });

    // 查询三级分类列表:
    $scope.$watch("entity.cityId",function(newValue,oldValue){
        addressService.findByAreasId(newValue).success(function(response){
            $scope.areasList = response;
        });
    });


    // 保存品牌的方法:
    $scope.save = function(){
        // 区分是保存还是修改
        var object;
        if($scope.entity.id != null){
            // 更新
            object = addressService.update($scope.entity);
        }else{
            // 保存
            object = addressService.add($scope.entity);
        }
        object.success(function(response){
            // {flag:true,message:xxx}
            // 判断保存是否成功:
            if(response.flag){
                // 保存成功
                alert(response.message);
                $scope.findAddressList();
            }else{
                // 保存失败
                alert(response.message);
            }
        });
    };
    // 查询一个:
    $scope.findById = function(id){
        addressService.findOne(id).success(function(response){
            // {id:xx,name:yy,firstChar:zz}
            $scope.entity = response;
        });
    };



    //获取当前用户的地址列表
    $scope.findAddressList=function(){

        addressService.findAddressList().success(
            function(response){
                $scope.addressList=response;
                for(var i=0;i<$scope.addressList.length;i++){
                    if($scope.addressList[i].isDefault=='1'){
                        $scope.address=$scope.addressList[i];
                        break;
                    }
                }

            }
        );
    }

    //选择地址
    $scope.selectAddress=function(address){
        $scope.address=address;
    }

    //判断某地址对象是不是当前选择的地址
    $scope.isSeletedAddress=function(address){
        if(address==$scope.address){
            return true;
        }else{
            return false;
        }
    }
	
});	
