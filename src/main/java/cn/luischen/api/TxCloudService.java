package cn.luischen.api;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public class TxCloudService {

    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";
    /**
     * 仓库
     */
    private static final String BUCKET = "";
    /**
     * 腾讯云外网访问地址
     */
    public static final String TX_UPLOAD_SITE = "";

    public static String upload(MultipartFile file, String fileName) throws IOException {

        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(ACCESS_KEY, SECRET_KEY);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig中包含了设置region, https(默认http), 超时, 代理等set方法, 使用可参见源码或者接口文档FAQ中说明
        ClientConfig clientConfig = new ClientConfig(new Region(""));
        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式

        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        //File localFile = new File("D:\\erpljq\\桌面\\about-image-0.png");
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置输入流长度
        objectMetadata.setContentLength(file.getInputStream().available());

        // 指定要上传到 COS 上对象键
        // 对象键（Key）是对象在存储桶中的唯一标识。例如，在对象的访问域名 `bucket1-1250000000.cos.ap-guangzhou.myqcloud.com/doc1/pic1.jpg` 中，对象键为 doc1/pic1.jpg, 详情参考 [对象键](https://cloud.tencent.com/document/product/436/13324)
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, fileName, file.getInputStream(), objectMetadata);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        cosClient.shutdown();

        return TX_UPLOAD_SITE + fileName;
    }
}
