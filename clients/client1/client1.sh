#!/bin/sh

USR="john_locke"
PASS="4815162342108lost"

export LD_LIBRARY_PATH=../../out/production/EncryptionServer

echo "Signing in as $USR with password: $PASS"
java -cp ../../out/production/EncryptionServer com.hut.drivers.ClientDriver localhost $USR $PASS