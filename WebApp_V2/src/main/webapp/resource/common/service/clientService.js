"use strict";

angular.module("dtucenter").factory('clientService', ['httpService', function ( httpService) {
    return {
        first:function (context) {
            return httpService.get("/client/first", context);
        },
        //获取分页
        changePage:function (context) {
            return httpService.get("/client/index?index="+context.pageIndex, context);
        },
        //改变每页的记录条数
        changePerPageCount:function (context) {
            return httpService.get("/client/change?perPageCount="+context.perPageCount, context);
        }
    }
}]);
