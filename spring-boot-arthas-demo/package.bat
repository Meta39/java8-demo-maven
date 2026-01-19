@echo off
set THREAD=1C
set JAVA_HOME=%JAVA_HOME%
set PACKAGE_CMD=mvn clean package dockerfile:build -D maven.test.skip=true -T %THREAD% -q -f pom.xml

echo %PACKAGE_CMD%......
REM 必须用start /B /WAIT cmd /c 调用mvn命令，因为mvn命令执行后会直接退出，后面的命令就不会执行了。
start /B /WAIT cmd /c "%PACKAGE_CMD%"

exit