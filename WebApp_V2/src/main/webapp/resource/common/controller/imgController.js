/**
 * Created by songxu on 2016/5/8.
 */
'use strict'
angular.module("dtucenter").controller('imageController', ['$scope', '$rootScope', 'clientService', function ($scope, $rootScope, clientService) {
    //更新url 为左侧导航栏添加标识
    $rootScope.url = "img";
    $scope.imgArr=['20151112103606','20140321095604','20140418142511','20140423135325',
    '20140425103538','20150410140031',
    '20150414095645','20151112102606'];
    $scope.imgDate=[];
    for(var i=0;i<$scope.imgArr.length;i++){
        var dateStr=$scope.imgArr[i];
        var date_str=2016+'年';
        date_str+=dateStr.substring(4,6)+'月';
        date_str+=dateStr.substring(6,8)+'日 ';
        date_str+=dateStr.substring(8,10)+':';
        date_str+=dateStr.substring(10,12)+':';
        date_str+=dateStr.substring(12,14);
        $scope.imgDate.push(date_str);
    }

}]);