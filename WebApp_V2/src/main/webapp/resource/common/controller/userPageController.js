/**
 * Created by songxu on 2016/5/8.
 */
'use strict'
angular.module("dtucenter").controller('userPageController', ['$scope', '$rootScope', 'userPageService', function ($scope, $rootScope, userPageService) {
    //更新url 为左侧导航栏添加标识
    $rootScope.url = "userPage";
    $rootScope.userToken = getCookie("token");
    /**
     * 更改每页的记录数量
     * @param x
     */
    $scope.changePerPage=function (x) {
        userPageService.changePerPageCount({
            token:$rootScope.userToken,
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
        userPageService.changePage({
            token:$rootScope.userToken,
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
        userPageService.first({
            token:$rootScope.userToken,
            succCallback: function (data) {
                refreshData(data);
                console.log(data);
            }
        });
    }
    init();

}]);