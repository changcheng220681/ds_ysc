//首页控制器
app.controller('indexController', function ($scope, loginService) {
    $scope.showName = function () {

        loginService.showName().success(
            function (response) {
                $scope.loginName = response.loginName;
            }
        );
    };
    $scope.findByOrderList = function (status) {
        loginService.findByOrderList(status).success(
            function (response) {
                $scope.orderVoList = response;
            }
        )
    };
    /**
     * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
     */
    // 显示状态
    $scope.status = ["", "未付款", "已付款", "未发货", "已发货", "交易成功", "交易关闭", "待评价"];


    //修改用户个人信息
    $scope.update = function () {
        loginService.update($scope.entity).success(
            function (response) {
                if (response.flag) {
                    alert(response.message);
                } else {
                    alert(response.message);
                }
            }
        )

    }
    //地区三级查询
    // 查询一级分类列表:
    $scope.selectItemCat1List = function () {
        //商品分类服务层  查询所有商品分类（父ID为0）
        itemCatService.findByParentId(0).success(function (response) {//List<ItemCat>
            $scope.itemCat1List = response;//List<ItemCat>
        });
    }

    // 查询二级分类列表:
    $scope.$watch("entity.goods.category1Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat2List = response;
        });
    });

    // 查询三级分类列表:
    $scope.$watch("entity.goods.category2Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat3List = response;
        });
    });

    $scope.submitOrder = function (orderId) {
        loginService.submitOrder(orderId).success(function (response) {
            if (response.flag) {
                location.href="pay.html";
            } else {
                alert(response.message);
            }
        })
    }
})
;