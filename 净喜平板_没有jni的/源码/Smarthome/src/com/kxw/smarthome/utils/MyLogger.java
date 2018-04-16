package com.kxw.smarthome.utils;

import java.util.Hashtable;

import android.util.Log;

public class MyLogger {
	private final static boolean logFlag = false;

	public final static String TAG = " LOG ";
	private final static int logLevel = Log.VERBOSE;

	private static Hashtable<String, MyLogger> sLoggerTable = new Hashtable<String, MyLogger>();
	private String mClassName;
	private static MyLogger mylog; 
	private static final String FLAG = "```"; 

	private MyLogger(String name) {
		mClassName = name;
	}

	@SuppressWarnings("unused")
	private static MyLogger getLogger(String className) {
		MyLogger classLogger = (MyLogger) sLoggerTable.get(className);
		if (classLogger == null) {
			classLogger = new MyLogger(className);
			sLoggerTable.put(className, classLogger);
		}
		return classLogger;
	}

	public static MyLogger getInstance(){
		if (mylog == null) {
			mylog = new MyLogger(TAG);
		}
		return mylog;
	}

	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			return mClassName + "[ " + Thread.currentThread().getName() + ": "
			+ st.getFileName() + ":" + st.getLineNumber() + " "
			+ st.getMethodName() + " ]";
		}
		return null;
	}

	public void i(Object str) {
		if (logFlag) {
			if (logLevel <= Log.INFO) {
				String name = getFunctionName();
				if (name != null) {
					Log.i(TAG, FLAG + name + " - " + str);
				} else {
					Log.i(TAG, FLAG + str.toString());
				}
			}
		}

	}

	public void d(Object str) {
		if (logFlag) {
			if (logLevel <= Log.DEBUG) {
				String name = getFunctionName();
				if (name != null) {
					Log.d(TAG, FLAG + name + " - " + str);
				} else {
					Log.d(TAG, FLAG + str.toString());
				}
			}
		}
	}

	public void v(Object str) {
		if (logFlag) {
			if (logLevel <= Log.VERBOSE) {
				String name = getFunctionName();
				if (name != null) {
					Log.v(TAG, FLAG + name + " - " + str);
				} else {
					Log.v(TAG, FLAG + str.toString());
				}
			}
		}
	}

	public void w(Object str) {
		if (logFlag) {
			if (logLevel <= Log.WARN) {
				String name = getFunctionName();
				if (name != null) {
					Log.w(TAG, FLAG + name + " - " + str);
				} else {
					Log.w(TAG, FLAG + str.toString());
				}
			}
		}
	}

	public void e(Object str) {
		if (logFlag) {
			if (logLevel <= Log.ERROR) {
				String name = getFunctionName();
				if (name != null) {
					Log.e(TAG, FLAG + name + " - " + str);
				} else {
					Log.e(TAG, FLAG + str.toString());
				}
			}
		}
	}

	public void e(Exception ex) {
		if (logFlag) {
			if (logLevel <= Log.ERROR) {
				Log.e(TAG, FLAG + "error", ex);
			}
		}
	}

	public void e(String log, Throwable tr) {
		if (logFlag) {
			String line = getFunctionName();
			Log.e(TAG, FLAG + "{Thread:" + Thread.currentThread().getName() + "}"
					+ "[" + mClassName + line + ":] " + log + "\n", tr);
		}
	}
}