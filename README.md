# compute-pi-rmi-client
RMI Client 
1) go to compute-pi-rmi-server-interfaces folder
2) build project with maven
mvn clean install
3) go to compute-pi-rmi-server-implementation folder
4) build project with maven
mvn clean install
5) go to compute-pi-rmi-client folder
6) build project with maven
mvn clean install
7) put two policy files and two .bat files to the parent folder for all modules
8) run server side from parent folder for all modules:
runServer.bat
9) run client side from parent folder for all modules:
runClient.bat

by default numberOfCorrectDigits is equals to 5, you can change it inside runClient.bat
