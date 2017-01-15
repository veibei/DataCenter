/**
 * Created by songxu on 2016/5/8.
 */
'use strict'
angular.module("dtucenter").config(['$routeProvider',function ($routeProvider) {
    $routeProvider
        .when('/main', {
            templateUrl: 'resource/html/main.html',
            controller: 'mainController'
        })
        .when('/client', {
            templateUrl: 'resource/html/client.html',
            controller: 'clientController'
        })
        .when('/dtu', {
            templateUrl: 'resource/html/dtu.html',
            controller: 'dtuController'
        })
        .when('/log', {
            templateUrl: 'resource/html/log.html',
            controller: 'logController'
        })
        .when('/user', {
            templateUrl: 'resource/html/user.html',
            controller: 'userPageController'
        })
        .when('/rate', {
            templateUrl: 'resource/html/rate.html',
            controller: 'rateController'
        })
        .when('/realtime', {
            templateUrl: 'resource/html/realtime.html',
            controller: 'realtimeController'
        })
        .when('/history', {
            templateUrl: 'resource/html/history.html',
            controller: 'historyController'
        })
        .when('/img', {
            templateUrl: 'resource/html/image.html',
            controller: 'imageController'
        }).when('/alarm', {
            templateUrl: 'resource/html/alarm.html',
            controller: 'alarmController'
        }).when('/port', {
            templateUrl: 'resource/html/port.html',
            controller: 'portController'
        }).when('/group', {
            templateUrl: 'resource/html/group.html',
            controller: 'groupController'
        })
        .otherwise({
            redirectTo: '/main'
        });
}]);


