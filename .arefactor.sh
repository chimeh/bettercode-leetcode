  744  for i in `/bin/ls  /d/code/cicd/cicd-java-multi/src/main/java`;do cp /d/code/cicd/cicd-java-multi/datetime/pom.xml  /d/code/cicd/cicd-java-multi/$i/ -v ;done
  745  for i in `/bin/ls  /d/code/cicd/cicd-java-multi/.src/main/java`;do cp /d/code/cicd/cicd-java-multi/datetime/pom.xml  /d/code/cicd/cicd-java-multi/$i/ -v ;done
  746  for i in  `/bin/ls  /d/code/cicd/cicd-java-multi/.src/main/java`;do perl -ni -e "s@<name>datetime</name>@<name>$i</name>@g;print" /d/code/cicd/cicd-java-multi/$i/pom.xml;done
  747  for i in  `/bin/ls  /d/code/cicd/cicd-java-multi/.src/main/java`;do perl -ni -e "s@<artifactId>datetime</artifactId>@<artifactId>$i</artifactId>@g;print" /d/code/cicd/cicd-java-multi/$i/pom.xml;done

