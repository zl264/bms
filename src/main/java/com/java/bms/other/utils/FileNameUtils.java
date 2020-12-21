package com.java.bms.other.utils;


import java.util.UUID;

/**
 * 生成新的文件名（利用 UUID 防止重名）
 */
public class FileNameUtils {
    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName){
        return UUID.randomUUID().toString().replace("-", "") + FileNameUtils.getSuffix(fileOriginName);
    }
}
