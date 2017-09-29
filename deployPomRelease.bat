call mvn deploy:deploy-file -Dfile=pom.xml -Durl=http://maven.berbon.com/nexus/content/repositories/releases -DpomFile=pom.xml -Dmaven.test.skip=true -DrepositoryId=releases
pause