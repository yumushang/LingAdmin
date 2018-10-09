package xyz.linghuayicao.service.system.impl;

import java.io.File;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import xyz.linghuayicao.service.system.TSysFileService;
import xyz.linghuayicao.utils.FileUtil;
import xyz.linghuayicao.service.system.TSysFileService;
import xyz.linghuayicao.utils.FileUtil;

@Service
public class TSysFileServiceImpl implements TSysFileService {

    /**
     *
     * @param filepath 文件相对路径
     * @param folderName 文件夹名称
     * @param session
     * @return
     * @throws Exception
     */
    @Override
    public String saveTempFile(String filepath, String folderName, HttpSession session) throws Exception {
        //将临时文件夹中的文件转移到正式的upload文件夹下
        String oldpath = session.getServletContext().getRealPath("temp");
        String newpath = session.getServletContext().getRealPath("upload");
        String[] paths = filepath.split("/");
        //如果传进来的值是upload中的文件则直接返回
        if(paths[1].equals("upload")){
            return filepath;
        }
        for(int i = 2;i<paths.length;i++){
            oldpath+=("\\"+paths[i]);
            if((i+1)<paths.length){
                FileUtil.pathValidate(oldpath);
            }
        }
        newpath = newpath + "\\" +folderName;
        //
        FileUtil.pathValidate(newpath);

        if(FileUtil.isFileExist(oldpath)){
            FileUtil.CopyFile(oldpath,newpath);
            FileUtil.deleteFile(oldpath);
        }
        //System.out.println("oldpath:"+oldpath);
        //System.out.println("newpath:"+newpath);
        File file = new File(newpath);
        String path = "../upload/"+folderName+"/"+file.getName();
        return path;
    }

    @Override
    public boolean deleteUploadFile(String filepath, HttpSession session) throws Exception {
        return false;
    }
}
