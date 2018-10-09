package xyz.linghuayicao.controller.system;

import xyz.linghuayicao.entity.system.TSysFile;
//import xyz.linghuayicao.service.TSysFileService;
import xyz.linghuayicao.utils.Consts;
import xyz.linghuayicao.utils.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value="/file")
public class FileController {

    /*@Autowired
    TSysFileService fileService;*/

    @RequestMapping(value="/upload")
    @ResponseBody
    // 这里的MultipartFile对象变量名跟表单中的file类型的input标签的name相同，所以框架会自动用MultipartFile对象来接收上传过来的文件，当然也可以使用@RequestParam("img")指定其对应的参数名称
    public HttpResult upload(HttpServletRequest request, HttpSession session)
            throws Exception {
        HttpResult result = new HttpResult();

        List<TSysFile> myFiles = new ArrayList<TSysFile>();
        try{
            //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
            CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            //检查form中是否有enctype="multipart/form-data"
            if(multipartResolver.isMultipart(request)){
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
                //获取multiRequest 中所有的文件名
                Iterator iter=multiRequest.getFileNames();

                while(iter.hasNext())
                {
                    //一次遍历所有文件
                    MultipartFile file=multiRequest.getFile(iter.next().toString());
                    if(file!=null)
                    {
                        TSysFile myFile = new TSysFile();
                        //String path="E:/springUpload"+file.getOriginalFilename();
                        String path = session.getServletContext().getRealPath("temp");
                        //上传
                        String fileName = file.getOriginalFilename().replaceAll(" ", "");//去掉文件名中的空格
                        File dir = new File(path+"\\"+fileName);
                        if(!dir.exists()){
                            dir.mkdirs();
                        }
                        File file1 = new File(path+"\\"+fileName, UUID.randomUUID().toString()+fileName);
                        file.transferTo(file1);
                        myFile.setFilepath("../temp/"+fileName+"/"+file1.getName());
                        myFile.setOldname(fileName);
                        myFile.setFilename(file1.getName());
                        myFiles.add(myFile);
                    }

                }
            }
            result.setCode(Consts.SUCCESS);
            result.setData(myFiles);
            result.setMsg("上传成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg(e.getMessage());
        }
        return result;
    }

}

