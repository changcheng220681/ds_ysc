//控制层
app.controller('seckillgoodsController' ,function($scope,$controller,seckillgoodsService){

    $controller('baseController',{$scope:$scope});//继承

    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        seckillgoodsService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    //分页
    $scope.findPage=function(page,rows){
        seckillgoodsService.findPage(page,rows).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne=function(id){
        seckillgoodsService.findOne(id).success(
            function(response){
                $scope.entity= response;
            }
        );
    }

    //保存
    $scope.save=function(){
        var serviceObject;//服务层对象
        if($scope.entity.id!=null){//如果有ID
            serviceObject=seckillgoodsService.update( $scope.entity ); //修改
        }else{
            serviceObject=seckillgoodsService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.flag){
                    //重新查询
                    $scope.reloadList();//重新加载
                }else{
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele=function(){
        //获取选中的复选框
        seckillgoodsService.dele( $scope.selectIds ).success(
            function(response){
                if(response.flag){
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity={};//定义搜索对象

    //搜索
    $scope.search=function(page,rows){
        seckillgoodsService.search(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    // 显示状态
    $scope.status = ["未审核","审核通过","审核未通过","关闭"];



    // 审核的方法:
    $scope.updateStatus = function(status){
        seckillgoodsService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.flag){
                $scope.reloadList();//刷新列表
               // $scope.selectIds = [];
                alert(response.message);
            }else{
                alert(response.message);
            }
        });
    }
});
