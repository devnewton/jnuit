#!/bin/bash
mvn clean package -Pwin,deb,rpm
mkdir -p target/release
cp packages/generic/target/jnuit-basic-game-example-installer*.jar target/release/
cp packages/win/target/jnuit-basic-game-example-installer*.exe target/release/
cp packages/deb/target/jnuit-basic-game-example*.deb target/release/
cp packages/rpm32/target/rpm/jnuit-basic-game-example/RPMS/*/jnuit-basic-game-example-*.rpm target/release/
cp packages/rpm64/target/rpm/jnuit-basic-game-example/RPMS/*/jnuit-basic-game-example-*.rpm target/release/
