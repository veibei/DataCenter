/**
 * Created by songxu on 2016/5/8.
 */
'use strict'
var dtucenter = angular.module('dtucenter', [
    'ngRoute',
    'ngSanitize',
    'ngCookies',
    'ngMessages',
    'ngTouch',
    'ngAnimate',
    'angularMoment',
    'oitozero.ngSweetAlert',
    'ngTagsInput',
    'ui.bootstrap-slider',
    'ui.checkbox',
    'cgBusy',
    'angularPromiseButtons',
    'angularLazyImg',
    'angularAdminLTE'
]);

/**
 * 拦截错误
 */
angular.module("dtucenter").config(['$httpProvider',function ($httpProvider) {
    $httpProvider.interceptors.push(function ($q) {
        return {
            response: function (response) {
                if(response.data=='timeout'){
                    window.location.href = "/login.html"
                }
                if(response.data=='error')
                    alert("系统错误！");
                return response;
            },
        };
    });
}]);


angular.module("dtucenter").factory('httpService', ['$http', '$timeout', function ($http, $timeout) {
    var processSuccess = function (data, context) {
        if (context.succCallback) {
            context.succCallback(data);
        }
    };

    var processError = function (data, context) {
        if (!data || !data.error) {
            data = {error: "系统错误，请重试！"};
        }

        if (context.errorCallback) {
            context.errorCallback(data);
        } else if (angular.isUndefined(context.disableErrorAlert) || context.disableErrorAlert !== true) {
            if (data.error.indexOf("(0x0020)") >= 0) {//未登录
                self.location = "login";
                return;
            }

            /*$timeout(function () {
                SweetAlert.swal({
                    title: "操作失败",
                    text: data.error,
                    type: "error",
                    confirmButtonText: "确定"
                });
            }, 100);//避免前一个Alert没有关闭导致不显示的问题*/
        }
    };

    var processFinal = function (data, context) {
        if (context.finalCallback) {
            context.finalCallback(data);
        }
    };

    return {
        request: function (method, url, context) {
            var requestContext = {
                url: url,
                method: method
            };

            if (context.data) {
                requestContext.data = context.data;
            }

            if (context.beforeRequest) {
                context.beforeRequest(requestContext);
            }

            return $http(requestContext).success(function (data, header, config, status) {
                if (!data || data.error) {
                    processError(data, context);
                } else {
                    processSuccess(data, context);
                }

                processFinal(data, context);
            }).error(function (data, header, config, status) {
                processError(data, context);
                processFinal(data, context);
            });
        },
        get: function (url, context) {
            return this.request("GET", url, context);
        },
        post: function (url, context) {
            return this.request("POST", url, context);
        }
    };
}]);
