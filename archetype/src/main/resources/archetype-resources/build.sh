#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${symbol_pound}!/bin/bash
mvn clean package -Pwin,deb,rpm
mkdir -p target/release
cp packages/generic/target/${game-name}-installer*.jar target/release/
cp packages/win/target/${game-name}-installer*.exe target/release/
cp packages/deb/target/${game-name}*.deb target/release/
cp packages/rpm32/target/rpm/${game-name}/RPMS/*/${game-name}-*.rpm target/release/
cp packages/rpm64/target/rpm/${game-name}/RPMS/*/${game-name}-*.rpm target/release/
