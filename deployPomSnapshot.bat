call mvn deploy:deploy-file -Dfile=pom.xml -Durl=http://maven.berbon.com/nexus/content/repositories/snapshots -DpomFile=pom.xml -Dmaven.test.skip=true -DrepositoryId=snapshots
pause