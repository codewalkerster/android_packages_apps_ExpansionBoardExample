#include "ioboard-spi.h"

#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ioctl.h>
#include <linux/ioctl.h>
#include <cutils/log.h>
#include "utils/Log.h"

#define LOG_TAG "SPI-Util"
//#define LOGE(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

#define SIZE    16

struct ioboard_spi_iocreg iocreg;

int fd = -1;

extern "C" JNIEXPORT jint Java_com_hardkernel_odroid_expansionboardexample_MainActivity_SPIOpen(JNIEnv* env, jobject obj) {

    fd = -1;
    if ((fd = open("/dev/ioboard-spi-misc", O_RDWR)) < 0)
        ALOGE("/dev/ioboard-spi-misc Open Fail!\n");
    else
        ALOGE("/dev/ioboard-spi-misc Open Success!\n");

    return fd;
}

extern "C" JNIEXPORT void Java_com_hardkernel_odroid_expansionboardexample_MainActivity_SPIClose(JNIEnv* env, jobject obj) {
    if (fd > 0)
        close(fd);
}

extern "C" JNIEXPORT void Java_com_hardkernel_odroid_expansionboardexample_MainActivity_SPIWrite(JNIEnv* env, jobject obj, jbyteArray arr) {

    jbyte *buf = env->GetByteArrayElements(arr, NULL);

    iocreg.cmd = IOBOARD_CMD_SPI_ERASE;
    iocreg.addr = 0x0;
    iocreg.size = ERASE_SIZE_ALL;
    iocreg.rwdata = NULL;

    if (ioctl(fd, IOBOARD_IOCGSTATUS, &iocreg) < 0)
        ALOGE("erase IOCTL Error!\n");

    iocreg.cmd = IOBOARD_CMD_SPI_WRITE;
    iocreg.addr = 0x0;
    iocreg.size = SIZE;
    iocreg.rwdata = (unsigned char*)buf;

    if (ioctl(fd, IOBOARD_IOCSREG, &iocreg) < 0)
        ALOGE("write IOCTL Error!\n");

    env->ReleaseByteArrayElements(arr, buf, 0);
}

extern "C" JNIEXPORT jbyteArray Java_com_hardkernel_odroid_expansionboardexample_MainActivity_SPIRead(JNIEnv* env, jobject obj) {

    unsigned char temp[SIZE];

    memset(temp, 0x00, SIZE);

    iocreg.cmd = IOBOARD_CMD_SPI_READ;
    iocreg.addr = 0x0;
    iocreg.size = SIZE;
    iocreg.rwdata = temp;

    if (ioctl(fd, IOBOARD_IOCGREG, &iocreg) < 0)
        ALOGE("read IOCTL Error!\n");

    jbyte* data;
    jbyteArray ret = (jbyteArray)env->NewObjectArray(SIZE, env->FindClass("java/lang/Byte"), NULL);
    env->SetByteArrayRegion(ret, 0, SIZE, (jbyte*)temp);
    return ret;
}
