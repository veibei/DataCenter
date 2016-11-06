/**
 * Created by songxu on 2016/5/8.
 */
'use strict'
angular.module("dtucenter").controller('dtuController', ['$scope', '$rootScope', 'dtuService', function ($scope, $rootScope, dtuService) {
    //更新url 为左侧导航栏添加标识
    $rootScope.url = "dtu";

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
        dtuService.first({
            succCallback: function (data) {
                refreshData(data);
                console.log(data);
            }
        });
    }
    init();
   
}]);