@echo off
set path=%~dp0.\dll\moyosoft;%path%
start javaw -client -Xms100m -Xmx2560m -Xincgc -XX:NewRatio=2 -cp "./bin;./lib/*;./shared/*;./conf" com.affichage.it21.ui.runtime.It21GuiRuntimeStarter