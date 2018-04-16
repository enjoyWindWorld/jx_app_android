LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := jxsmart
LOCAL_SRC_FILES := jxinteligent.cpp

TARGET_PLATFORM := android-3
LOCAL_PRELINK_MODULE := false
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_PATH := $(TARGET_OUT)/bin

LOCAL_C_INCLUDES := \
	inline \
	.

LOCAL_LDFLAGS :=
LOCAL_SHARED_LIBRARIES := \
	libcutils \

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
