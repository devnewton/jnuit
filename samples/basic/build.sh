#!/bin/bash
mvn clean package -Pwin,deb,rpm
mkdir -p target/release
cp packages/generic/target/jnuit-basic-installer*.jar target/release/
cp packages/win/target/jnuit-basic-installer*.exe target/release/
cp packages/deb/target/jnuit-basic*.deb target/release/
cp packages/rpm32/target/rpm/jnuit-basic/RPMS/*/jnuit-basic-*.rpm target/release/
cp packages/rpm64/target/rpm/jnuit-basic/RPMS/*/jnuit-basic-*.rpm target/release/
