LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_JNI_SHARED_LIBRARIES := libSPIUtil

LOCAL_REQUIRED_MODULES := libSPIUtil

LOCAL_SRC_FILES := \
        $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := ExpansionBoardExample

LOCAL_CERTIFICATE := platform

LOCAL_RESOURCE_DIR += $(LOCAL_PATH)/res 

include $(BUILD_PACKAGE)

include $(call all-makefiles-under,$(LOCAL_PATH))
