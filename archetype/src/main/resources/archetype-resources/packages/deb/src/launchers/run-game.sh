#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound}!/usr/bin/env sh
if [ -e /usr/lib/jvm/java-7-openjdk-`/usr/share/jarwrapper/java-arch.sh`/bin/java ]
then
    /usr/lib/jvm/java-7-openjdk-`/usr/share/jarwrapper/java-arch.sh`/bin/java -jar /opt/${rootArtifactId}/${rootArtifactId}-game-${symbol_dollar}{project.version}.jar ${symbol_dollar}*
else
    /opt/${rootArtifactId}/${rootArtifactId}-game-${symbol_dollar}{project.version}.jar ${symbol_dollar}*
fi