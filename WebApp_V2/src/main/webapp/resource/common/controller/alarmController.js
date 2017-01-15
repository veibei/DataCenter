/**
 * Created by songxu on 2016/5/8.
 */
'use strict'
angular.module("dtucenter").controller('alarmController', ['$scope', '$rootScope', 'dtuService', function ($scope, $rootScope, dtuService) {
    //更新url 为左侧导航栏添加标识
    $rootScope.url = "alarm";

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
                'a':'2017-1-10 17:23:46',
                'b':'凌水码头一号监测点',
                'c':'大连海事大学凌水码头',
                'd':'张三',
                'e':'13900001111',
                'f':'未处理'
            },
            {
                'a':'2017-1-09 01:24:54',
                'b':'凌水码头二号监测点',
                'c':'大连海事大学凌水码头',
                'd':'张三',
                'e':'13900001111',
                'f':'未处理'
            },
            {
                'a':'2017-1-08 10:32:08',
                'b':'大连石化一号监测点',
                'c':'大连石化',
                'd':'李四',
                'e':'15812341111',
                'f':'已处理'
            },
            {
                'a':'2016-11-30 16:49:23',
                'b':'大连石化二号监测点',
                'c':'大连石化',
                'd':'李四',
                'e':'15812341111',
                'f':'已处理'

            },
            {
                'a':'2016-10-23 16:04:29',
                'b':'大连石化五号监测点',
                'c':'大连石化',
                'd':'李四',
                'e':'15812341111',
                'f':'已处理'
            },
            
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
        $scope.rowsCount = 5;//所有的记录数
    }
    init();
   
}]);