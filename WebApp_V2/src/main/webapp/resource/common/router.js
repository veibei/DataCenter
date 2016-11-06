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
        .otherwise({
            redirectTo: '/main'
        });
}]);


