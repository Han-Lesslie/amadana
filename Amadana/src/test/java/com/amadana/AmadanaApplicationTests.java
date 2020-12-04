package com.amadana;


import com.amadana.dao.ProductMapper;
import com.amadana.entity.Category;
import com.amadana.entity.Product;
import com.amadana.utils.DateFormat;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AmadanaApplicationTests {
    Logger LOGGER = LoggerFactory.getLogger(AmadanaApplicationTests.class);
    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testSaveProduct() {
        Product product = new Product();
        Category category = new Category();
        category.setId(1);
        product.setCategory(category);
        product.setCreateTime(DateFormat.dateFormat(new Date()));
        product.setDisplayImg("test");
        product.setPrice(22.2);
        product.setProductDesc("desc");
        product.setProductIcon("icon");
        product.setProductImg("productImg");
        product.setProductName("productName");

        System.out.println(productMapper.save(product));
        System.out.println(product.getId());

    }


    @Test
    public void testUploadFile() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(1000 * 30);
        ftpClient.setControlEncoding("utf-8");
        ftpClient.enterLocalPassiveMode();//设置被动模式，文件传输端口设置

        try {

            ftpClient.connect("106.52.108.173", 21 );
            boolean flag = ftpClient.login("ftpuser", "zsw123456");
            System.out.println(flag);
            //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                LOGGER.error("connect ftp {} failed");
                ftpClient.disconnect();
            }
            //设置文件传输模式为二进制，可以保证传输的内容不会被改变
            // 5. 设置上传的路径
            ftpClient.changeWorkingDirectory("/home/nginx/images/");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);//设置文件传输模式为二进制，可以保证传输的内容不会被改变
            // 4. 读取本地文件
            FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\LENOVO\\Desktop\\配置文件\\u97.jpg"));

            // 6. 修改上传文件的格式为二进制
            // 7. 服务器存储文件，第一个参数是存储在服务器的文件名，第二个参数是文件流
            boolean b = ftpClient.storeFile("u97.jpg", inputStream);
            System.out.println(b);
            // 8. 关闭连接
            ftpClient.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
