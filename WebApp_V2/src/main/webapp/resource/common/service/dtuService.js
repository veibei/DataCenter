"use strict";

angular.module("dtucenter").factory('dtuService', ['httpService', function ( httpService) {
    return {
        first:function (context) {
            return httpService.get("/dtu/first", context);
        },
        //获取分页
        changePage:function (context) {
            return httpService.get("/dtu/index?index="+context.pageIndex, context);
        },
        //改变每页的记录条数
        changePerPageCount:function (context) {
            return httpService.get("/dtu/change?perPageCount="+context.perPageCount, context);
        }
    }
}]);
