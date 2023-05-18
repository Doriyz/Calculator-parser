@echo off
cd bin
java test.ExprEvalTest ..\testcases\super.xml > ..\testcases\super.txt
cd ..
type testcases\super.txt
pause
@echo on
