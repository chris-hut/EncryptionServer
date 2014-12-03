#!/bin/sh

USR="charles_widmore"
PASS="what_are_you_doing_on_our_island"
export LD_LIBRARY_PATH=../../out/production/EncryptionServer

echo "Signing in as $USR with password: $PASS"
java -cp ../../out/production/EncryptionServer com.hut.drivers.ClientDriver localhost $USR $PASS