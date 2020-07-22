package com.shopping.util;

import android.os.Environment;

/**
 * 常量类
 */
public final class Constants {
	/** 系统初始化配置文件名 */
	public static final String SYSTEM_INIT_FILE_NAME = "sysini";
	/** 本地缓存目录 */
	public static final String CACHE_DIR;
	
	public static final int PAGESIZE=10;
	/** 表情缓存目录 */
	public static final String CACHE_DIR_SMILEY;
	/** 图片缓存目录 */
	public static final String CACHE_DIR_IMAGE;
	/** 待上传图片缓存目录 */
	public static final String CACHE_DIR_UPLOADING_IMG;


	static {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CangShengWang/";
		} else {
			CACHE_DIR = Environment.getRootDirectory().getAbsolutePath() + "/CangShengWang/";
		}
		
		CACHE_DIR_SMILEY = CACHE_DIR + "/smiley";
		CACHE_DIR_IMAGE = CACHE_DIR + "/pic";
		CACHE_DIR_UPLOADING_IMG = CACHE_DIR + "/uploading_img";
	}
	
	
}
