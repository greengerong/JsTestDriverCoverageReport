/**
 * Created by JetBrains RubyMine.
 * User: whitewolf
 * Date: 12-9-22
 * Time: 上午9:52
 * To change this template use File | Settings | File Templates.
 */
describe("jasmine.util", function () {
    var $scope = {coverageData:null};
    var data;
    beforeEach(function () {
        data = [
            {
                file:"src/spec1.js", coverage:[
                {line:1, hit:3},
                {line:8, hit:1},
                {line:10, hit:0},
                {line:11, hit:0}
            ], codes:[
                "/** Created by JetBrains RubyMine.*/",
                "window.coverageData = [",
                "     {"     ,
                "        file:\"src/spec1.js\", coverage:[",
                "        {line:1, hit:3},",
                "        {line:8, hit:1},",
                "        {line:10, hit:0},",
                "        {line:11, hit:0},",
                "    ]",
                "    }",
                "];" ,
                "window.coverageLimteRate = 50;"
            ]
            },
            {
                file:"src/spec2.js", coverage:[
                {line:1, hit:3},
                {line:5, hit:0},
                {line:10, hit:0}  ,
                {line:11, hit:0}
            ], codes:[
                "/** Created by JetBrains RubyMine.*/",
                "window.coverageData = [",
                "     {"     ,
                "        file:\"src/spec1.js\", coverage:[",
                "        {line:1, hit:3},",
                "        {line:8, hit:1},",
                "        {line:10, hit:0},",
                "        {line:11, hit:0},",
                "    ]",
                "    }",
                "];" ,
                "window.coverageLimteRate = 50;"
            ]
            }
        ];
    });
    describe("jasmine.util", function () {
        beforeEach(function () {
            coverage_report($scope, {
                coverageData:data,
                coverageLimteRate:50,
                location:{
                    href:""
                }
            });
        });
        it("should return is total page ", function () {
            expect($scope.fileUrlParam).toBe(null);
            expect($scope.isTotalCoverage).toBe(true);
        });

        it("should return coverage rate ", function () {
            expect($scope.getCoverageData(data[0])).toBe("50.00");
            expect($scope.getCoverageData(data[1])).toBe("25.00");
            expect(data[0].rate).toBe("50.00");
            expect(data[1].rate).toBe("25.00");
        });

        it("should return coverage status ", function () {
            $scope.getCoverageData(data[0]);
            $scope.getCoverageData(data[1]);
            expect($scope.getStatus(data[0])).toBe("success");
            expect($scope.getStatus(data[1])).toBe("error");
        });
    });
    describe("jasmine.util", function () {
        beforeEach(function () {
            coverage_report($scope, {
                coverageData:data,
                coverageLimteRate:50,
                location:{
                    href:"?file=src/spec1.js"
                }
            });
        });
        it("should return current url param ", function () {
            expect($scope.fileUrlParam).toBe("src/spec1.js");
            expect($scope.isTotalCoverage).toBe(false);
        });

        it("should get current file ", function () {
            expect($scope.currentFile).toBe(data[0]);
        });

        it("should return right has pass thought ", function () {
            expect($scope.hasPassThought(1)).toBe("");
            expect($scope.hasPassThought(10)).toBe("error");
            expect($scope.hasPassThought(2)).toBe("");
        });
    });
});
