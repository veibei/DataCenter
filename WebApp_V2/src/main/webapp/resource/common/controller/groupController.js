/**
 * Created by songxu on 2016/5/8.
 */
'use strict'
angular.module("dtucenter").controller('groupController', ['$scope', '$rootScope', 'dtuService', function ($scope, $rootScope, dtuService) {
    //更新url 为左侧导航栏添加标识
    $rootScope.url = "group";

    /**
     * 更改每页的记录数量
     * @param x
     */
    $scope.changePerPage=function (x) {
        dtuService.changePerPageCount({
            perPageCount:x,
            succCallback: function (data) {
                refreshData(data);
            }
        });
    };


    /**
     * 更改分页
     * @param page
     */
    $scope.changePage = function (page) {
        dtuService.changePage({
            pageIndex:page,
            succCallback: function (data) {
                refreshData(data);
            }
        });

    };
    function getdata() {
        return [
            {
                'a':'凌水码头分组',
                'b':'大连海事大学凌水码头',
                'c':'2',
                'd':'1',
                'e':'10',
            },
            {
                'a':'大连石化分组一',
                'b':'中石油大连石化油品码头',
                'c':'4',
                'd':'4',
                'e':'5',

            },
            {
                'a':'大连石化分组二',
                'b':'中石油大连石化油品码头',
                'c':'2',
                'd':'1',
                'e':'2',

            }
            
        ];
    }
    /**
     * 刷新数据
     * @param data
     */
    var refreshData=function (data) {
        $scope.currentPage = data.currentPage;//当前页码
        $scope.listBtn = data.listBtn;//按钮标号 1,2..10
        $scope.rowData = data.rowData;//当前页面展示的数据
        $scope.pageCount = data.pageCount;//所有的页数
        $scope.perPageCount = data.perPageCount;//每页的数量
        $scope.rowsCount = data.rowsCount;//所有的记录数
    }
    var init = function () {
        $scope.currentPage = 1;//当前页码
        $scope.listBtn = [1];//按钮标号 1,2..10
        $scope.rowData = getdata();//当前页面展示的数据
        $scope.pageCount = 1;//所有的页数
        $scope.perPageCount = 10;//每页的数量
        $scope.rowsCount = 3;//所有的记录数
    }
    init();
   
}]);