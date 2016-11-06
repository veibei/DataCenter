"use strict";

angular.module("dtucenter").factory('userPageService', ['httpService', function ( httpService) {
    return {
        first:function (context) {
            return httpService.get("/user/first?token="+context.token, context);
        },
        //获取分页
        changePage:function (context) {
            return httpService.get("/user/index?token="+context.token+"&index="+context.pageIndex, context);
        },
        //改变每页的记录条数
        changePerPageCount:function (context) {
            return httpService.get("/user/change?token="+context.token+"&perPageCount="+context.perPageCount, context);
        }
    }
}]);
