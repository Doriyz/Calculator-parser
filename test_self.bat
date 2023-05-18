@echo off
cd bin
java test.ExprEvalTest ..\testcases\mytest.xml > ..\testcases\mytest.txt
cd ..
type testcases\mytest.txt
pause
@echo on
