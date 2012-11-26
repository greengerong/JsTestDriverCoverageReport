/**
 * Created by JetBrains RubyMine.
 * User: whitewolf
 * Date: 12-9-22
 * Time: 上午9:41
 * To change this template use File | Settings | File Templates.
 */
;
String.format = function () {
    var s = arguments[0];
    for (var i = 0; i < arguments.length - 1; i++) {
        var reg = new RegExp("\\{" + i + "\\}", "gm");
        s = s.replace(reg, arguments[i + 1]);
    }

    return s;
};
function Uri(urlstr) {
    var uri = urlstr;
    if (uri == undefined || uri == null || uri == "") {
        uri = window.location.href;
    }
    ;
    this.Host = function () {
        var r = uri.split("?");
        if (r.length > 0) {
            return r[0];
        }
        return "";
    };
    this.searchString = function () {
        var r = uri.split("?");
        if (r.length > 1) {
            return unescape(r[1]);
        }
        return "";
    };
    this.Params = function () {
        var search = this.searchString();
        if (search == "")
            return null;
        var obj = new Array();
        var pair = search.split("&");
        if (pair.length > 0) {

            for (var i = 0; i < pair.length; i++) {

                var pairArr = pair[i].split("=");
                obj[pairArr[0]] = pairArr[1];
            }
        }
        return obj;
    };
    this.QueryParam = function (key, def) {
        var obj = this.Params();
        if (obj != null) {
            var value = obj[key];
            if (value != undefined && value != null) {
                return value;
            }
        }
        return def;
    };
}

var app = angular.module("coverage", []);
//var backTop = function () {
//    return function ($scope, $element, $attrs) {
//        var $target = jQuery($element);
//        var $body = jQuery("body");
//        $target.css({left:($body.width() - 50) + "px", top:($body.height()) + "px"});
//    };
//};
//app.directive("backTop", backTop);

var coverage_report = function ($scope, $window) {
    $scope.coverageData = $window.coverageData
    $scope.coverageLimteRate = $window.coverageLimteRate;
    $scope.fileUrlParam = new Uri($window.location.href).QueryParam("file", null);
    $scope.isTotalCoverage = $scope.fileUrlParam == null;

    if (!$scope.isTotalCoverage) {
        $scope.currentFile = underscore.filter($scope.coverageData, function (file) {
            return file.file == $scope.fileUrlParam;
        })[0];
    }
    $scope.getCoverageData = function (file) {
        var hitRow = underscore.filter(file.lines, function (data) {
            return data.hit > 0;
        });
        var rate = (hitRow.length / file.lines.length) * 100;
        file.rate = rate.toFixed(2);
        return file.rate;
    };
    $scope.enSureRate = function (file) {
        if (!file.rate) {
            this.getCoverageData(file);
        }
        return file;
    };
    $scope.getStatus = function (file) {
        this.enSureRate(file);
        return (file.rate >= this.coverageLimteRate) ? "success" : "error";
    }
    $scope.hasPassThought = function (line) {
        var lines = underscore.filter(this.currentFile.lines, function (file) {
            return file.line == line;
        });
        
        return (lines.length == 0 || lines[0].hit > 0) ? "" : "error";
    };
    $scope.getProgressWidth = function (file) {
        return {"width":file.rate + "%", "min-width":file.rate + "%"};
    };
}
app.controller("coverage_report", coverage_report);