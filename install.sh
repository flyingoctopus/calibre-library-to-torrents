mvn clean install -DskipTests
java -Xmx768m -jar target/calibre-library-to-torrents.jar $@
