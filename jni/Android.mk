LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := libSPIUtil

LOCAL_SRC_FILES := spiutil.cpp

LOCAL_SHARED_LIBRARIES := \
    libandroid_runtime \
    libnativehelper \
    libcutils \
    libutils \
    liblog

include $(BUILD_SHARED_LIBRARY)
