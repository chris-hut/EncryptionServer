#!/bin/sh

USR="jack_shepard"
PASS="we_have_to_go_back"
export LD_LIBRARY_PATH=../../out/production/EncryptionServer

echo "Signing in as $USR with password: $PASS"
java -cp ../../out/production/EncryptionServer com.hut.drivers.ClientDriver localhost $USR $PASS