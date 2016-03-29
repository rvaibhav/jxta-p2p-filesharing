mvn install:install-file -Dfile=bouncycastle/bcprov-jdk15-145.jar -DgroupId=bouncycastle -DartifactId=bcprov-jdk15 -Dversion=145 -Dpackaging=jar

mvn install:install-file -Dfile=jetty/javax.servlet.jar -DgroupId=javax.servlet -DartifactId=servlet-api -Dversion=2.3 -Dpackaging=jar

mvn install:install-file -Dfile=felix/felix.jar -DgroupId=org.apache.felix -DartifactId=org.apache.felix.main -Dversion=2.0.1 -Dpackaging=jar

mvn install:install-file -Dfile=jetty/org.mortbay.jetty.jar -DgroupId=org.mortbay.jetty -DartifactId=jetty -Dversion=4.2.25 -Dpackaging=jar

mvn install:install-file -Dfile=derby/derby-10.5.1.1.jar -DgroupId=org.apache.derby -DartifactId=derby -Dversion=10.5.3.0_1 -Dpackaging=jar

mvn install:install-file -Dfile=h2/h2-1.2.127.jar -DgroupId=com.h2database -DartifactId=h2 -Dversion=1.1.118 -Dpackaging=jar

mvn install:install-file -Dfile=junit/junit-4.4.jar -DgroupId=junit -DartifactId=junit -Dversion=4.8.1 -Dpackaging=jar

mvn install:install-file -Dfile=jmock-2.5.1/jmock-2.5.1.jar -DgroupId=org.jmock -DartifactId=jmock-junit4 -Dversion=2.5.1 -Dpackaging=jar

mvn install:install-file -Dfile=jmock-2.5.1/jmock-legacy-2.5.1.jar -DgroupId=org.jmock -DartifactId=jmock-legacy -Dversion=2.5.1 -Dpackaging=jar

mvn install:install-file -Dfile=jmock-2.5.1/jmock-legacy-2.5.1.jar -DgroupId=org.jmock -DartifactId=jmock-legacy -Dversion=2.5.1 -Dpackaging=jar

mvn install:install-file -Dfile=jxse/jxse-2.6.jar -DgroupId=com.kenai.jxse -DartifactId=jxse -Dversion=2.6 -Dpackaging=jar