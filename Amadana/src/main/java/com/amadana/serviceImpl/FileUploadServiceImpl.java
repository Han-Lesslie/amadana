package com.amadana.serviceImpl;

import com.amadana.properties.MessagesProperties;
import com.amadana.service.FileUploadService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${FTP_ADDRESS}")
    private String address;
    @Value("${FTP_PORT}")
    private Integer port;

    @Value("${FTP_USERNAME}")
    private String username;

    @Value("${FTP_PASSWORD}")
    private String password;
    @Value("${FTP_BASE_PATH}")
    private String basePath;
    @Value("${FTP_BASE_URL}")
    private String baseUrl;

    private final String[] IMG_TYPE = {"jpg","png","jpeg"};

    @Autowired
    private MessagesProperties messagesProperties;

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    Map<String,Object> result = new HashMap<>();
    List<String> fileNames = new ArrayList<>();
    Map<String, Object> path = new HashMap<>();
    @Override
    public Map<String, Object> uploadPicture(MultipartFile file) throws IOException {
        try{
            boolean isPitcure = false;
            for (String type:IMG_TYPE) {
                if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(),type)){
                   isPitcure = true;
                   break;
                }
            }
            if (isPitcure) {
                FTPClient ftpClient = getFtpClient();
                //设置文件存储
                ftpClient.changeWorkingDirectory(basePath);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                //原名称
                String imageName = file.getOriginalFilename();
                boolean flag = ftpClient.storeFile(imageName,file.getInputStream());
                ftpClient.logout();
                if (flag) {
                    result.put("msg","上传图片成功");
                    result.put("code",200);
                    String filePath = baseUrl.concat(imageName);
                    result.put("path",filePath);
                    fileNames.add(filePath);
                    path.put("path",filePath);
                    return result;
                }
            }else {
                result.put("msg","图片格式不对!");
                result.put("code",404);
                return result;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getPath() {
        return path;
    }

    @Override
    public List<String> getFileNames() {
        return fileNames;
    }

    private FTPClient getFtpClient() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(1000*30);
        ftpClient.setControlEncoding("utf-8");
        ftpClient.enterLocalPassiveMode();

        try{
            ftpClient.connect(address,port);
            ftpClient.login(username, password);
            int replyCode = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                LOGGER.error("connect ftp {} failed");
                ftpClient.disconnect();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return ftpClient;
    }
}
