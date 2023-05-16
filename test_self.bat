@echo off
cd bin
java test.ExprEvalTest ..\testcases\self.xml > ..\testcases\self.txt
cd ..
type testcases\self.txt
pause
@echo on
