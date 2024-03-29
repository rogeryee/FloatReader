@echo off

rem SET JAVA_HOME=D:\jdk1.6.0_10
set EXE_JAVA="%JAVA_HOME%"\bin\javaw
SET APPLICATION_DIR=D:\floatreader

SET APPLICATION_BIN_DIR=%APPLICATION_DIR%\bin
SET PROPERTIES_ARGS=-Dcfg.file=%APPLICATION_BIN_DIR%\floatReader.properties

SET APPLICATION_LIB_DIR=%APPLICATION_DIR%\lib
SET CLASS_PATH=%CLASS_PATH%;%APPLICATION_LIB_DIR%\floatreader.jar
SET CLASS_PATH=%CLASS_PATH%;%APPLICATION_LIB_DIR%\yeepBasis.jar
SET CLASS_PATH=%CLASS_PATH%;%APPLICATION_LIB_DIR%\jintellitype-1.3.4.jar
rem SET CLASS_PATH=%CLASS_PATH%;%APPLICATION_LIB_DIR%\Quaqua.jar
rem SET CLASS_PATH=%CLASS_PATH%;%APPLICATION_LIB_DIR%\libquaqua64.jnilib
rem SET CLASS_PATH=%CLASS_PATH%;%APPLICATION_LIB_DIR%\libquaqua.jnilib

%EXE_JAVA% -Xms30m -Xmx100m %PROPERTIES_ARGS% -cp %CLASS_PATH%  com.yeep.floatreader.Main

