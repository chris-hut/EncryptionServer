cd src/

rm -f *.so
javah -jni com.hut.util.TeaEncryptionHelper
cp com_hut_util_TeaEncryptionHelper.h com/hut/util

gcc -o libtea_encryption_helper.so -lc -shared -I $JAVA_HOME/include -I $JAVA_HOME/include/linux -fPIC com/hut/util/tea_encryption_helper.c
export LD_LIBRARY_PATH=.

#cp libtea_encryption_helper.so ../out/production/EncryptionServer/com/hut/util
