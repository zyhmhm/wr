package com.wr.service.impl;

import com.wr.service.FileService;
import com.wr.vo.PageFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
//@PropertySources({@PropertySource("classpath:/properties/image.properties"),@PropertySource("classpath:/properties/image.properties")})//多个配置文件
public class FileServiceImpl implements FileService {

    //动态获取属性值，将数据信息写入到properties中去
    //取值前提：spring容器必须管理配置文件
    //定义本地磁盘路径
    @Value("${image.localDirPath}")
    //D:/JT-SOFTWARE/images/
    private String localDirPath;

    //定义虚拟路径地址
    @Value("${image.urlDirPath}")
    //http://image.jt.com/
    private String urlDirPath;

    /**
     * 1.判断文件是否为图片  jpg|png|gif
     * 2.防止恶意程序   特有属性：高度宽度
     * 3.图片分文件保存    分布式文件系统、按时间维度：yyyy/MM/dd
     * 4.防止文件重名         hash随机UUID
     * @param uploadFile
     * @return
     */
    @Override
    public PageFile fileUpload(MultipartFile uploadFile,String type) {
        //1.判断文件是否为图片类型
        String fileName = uploadFile.getOriginalFilename();
        //将字符串转换成小写，防止系统问题出bug
        //if(fileName==null) return EasyUIFile.fail();
        fileName = fileName.toLowerCase();

        if(!fileName.matches("^.+\\.(jpg|png|gif)$")){
            //表示不满足规则
            return PageFile.fail();
        }
        PageFile pageFile = new PageFile();
        try{
            //2.判断是否为恶意程序 转换为图片对象
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if(width==0 || height==0){
                return PageFile.fail();
            }
            //3.分文件存储 按照yyyy/MM/dd
            String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
            String fileDirPath = localDirPath + dateDir;

            //如果文件夹不存在则创建
            File dirFile = new File(fileDirPath);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }

            //4.生成文件名称防止重名
            //获取文件类型
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();
            //拼接文件名称
            String realFileName = uuid + fileType;

            //5.实现将文件存储到本地
            String filePath = fileDirPath + realFileName;
            uploadFile.transferTo(new File(filePath));

            //暂时使用网络地址代替真实的url的地址
            String url = urlDirPath + dateDir + realFileName;
            pageFile.setWidth(width).setHeight(height).setUrl(url).setType(type);
        }catch (Exception e){
            e.printStackTrace();
            return PageFile.fail();
        }
        return pageFile;
    }
}
