package com.amadana.serviceImpl;

import com.amadana.service.FileUploadService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.net.ftp.FtpClient;

import java.io.*;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Override
    public Map<String, Object> uploadPicture(MultipartFile file) throws IOException {

        Map<String,Object> result = new HashMap<>();

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
                    result.put("name",imageName);
                    result.put("code",200);
                    String filePath = baseUrl.concat(imageName);
                    result.put("path",filePath);
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
    public Map<String, Object> uploadVideo(MultipartFile file) throws IOException {
        Map<String, Object> result = new HashMap<>();
        try{
            FTPClient ftpClient = getFtpClient();
            //设置文件存储
            ftpClient.changeWorkingDirectory(basePath);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            //原名称
            String videoName = file.getOriginalFilename();
            boolean flag = ftpClient.storeFile(videoName,file.getInputStream());
            ftpClient.logout();

            if (flag) {
                result.put("msg","上传视频成功");
                result.put("name",videoName);
                result.put("code",200);
                String filePath = baseUrl.concat(videoName);
                result.put("path",filePath);
                return result;
            }else {
                result.put("msg","上传视频失败");
                result.put("code",500);
                return result;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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


    /**
     * Description: 从FTP服务器下载文件
     * @param host FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 要下载的文件名
     * @param localPath 下载后保存到本地的路径
     * @return
     */
    @Override
    public  boolean downloadFile(String fileName) {
        String localPath = "C:\\Users\\LENOVO\\Desktop\\test\\";
        boolean result = false;
        FTPClient ftp = this.getFtpClient();
        try {
            int reply;
            ftp.setControlEncoding("UTF-8");
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(basePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath.concat("/") + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    @Override
    public void deteleFile(String name) {
        try{
            FTPClient client = getFtpClient();
            client.sendCommand("dele",name);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
