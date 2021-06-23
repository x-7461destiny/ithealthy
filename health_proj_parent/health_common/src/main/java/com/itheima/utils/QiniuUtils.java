package com.itheima.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 七牛云工具类
 */
public class QiniuUtils {
//    public  static String accessKey = "jYzMK8v-KJf5lXH4GgAxyBdJll58DpMw6Z32gqw4";
//    public  static String secretKey = "0aHooATrWd9oNZwkm03ERzFASzkNrEiFEdU1Spb_";
//    public  static String bucket = "itcast-space-by-arutoria";
    public  static String accessKey = "LTAI5t5k4iMaRPYakWmDPBKU";
    public  static String secretKey = "tSmKsxkbvwZPLWewhvBFO3XIpAxEDU";
    public  static String bucket = "itcast-space-by-arutoria";
    public static  String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";

//    public static void upload2Qiniu(String filePath,String fileName){
//        //构造一个带指定Zone对象的配置类
//        Configuration cfg = new Configuration(Zone.zone0());
//        UploadManager uploadManager = new UploadManager(cfg);
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//        try {
//            Response response = uploadManager.put(filePath, fileName, upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        }
//    }
    public static void upload2Qiniu(String filePath, String fileName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKey);
        PutObjectRequest putObjectRequest = new PutObjectRequest("bucket",
                "fileName",
                new File("filePath"));
        ossClient.putObject(putObjectRequest);

// 关闭OSSClient。
        ossClient.shutdown();
    }

    //上传文件
    public static void upload2Qiniu(byte[] bytes, String fileName){
//        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//
//        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }


// 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey,accessKey);
//
//// 填写Byte数组。
////        byte[] content = "Hello OSS".getBytes();
//// 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
//        ossClient.putObject("bucket", "fileName", bytes.to);
//
//// 关闭OSSClient。
//        ossClient.shutdown();
    }


    //删除文件
    public static void deleteFileFromQiniu(String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }


}
