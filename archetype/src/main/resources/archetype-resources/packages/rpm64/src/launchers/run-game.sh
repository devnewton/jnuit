#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound}!/usr/bin/env sh
java -jar /opt/${rootArtifactId}/${rootArtifactId}-game-${symbol_dollar}{project.version}.jar ${symbol_dollar}*
