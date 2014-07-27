#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound}!/bin/bash
mvn clean package -Pwin,deb
[ $(which rpm) ] && mvn package -Prpm
mkdir -p target/release
cp packages/generic/target/${rootArtifactId}-installer*.jar target/release/
cp packages/win/target/${rootArtifactId}-installer*.exe target/release/
cp packages/deb/target/${rootArtifactId}*.deb target/release/
[ $(which rpm) ] && cp packages/rpm32/target/rpm/${rootArtifactId}/RPMS/*/${rootArtifactId}-*.rpm target/release/
[ $(which rpm) ] && cp packages/rpm64/target/rpm/${rootArtifactId}/RPMS/*/${rootArtifactId}-*.rpm target/release/
