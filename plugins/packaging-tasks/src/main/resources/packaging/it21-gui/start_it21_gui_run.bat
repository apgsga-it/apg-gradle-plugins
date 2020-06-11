@echo off

set java_gui_directory=%~dp0


for /f "" %%i in ('dir /b /o:n /a:d %~dp0java_gui*') do (

  set java_gui_directory=%~dp0%%i
)

powershell Start-Process -FilePath "start_gui.bat" -WorkingDirectory "%java_gui_directory%"

exit