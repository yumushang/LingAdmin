package xyz.linghuayicao.controller.system;

import xyz.linghuayicao.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController {


    @RequestMapping(value="/html/{path}")
    public String freeMarker(ModelMap mm,@PathVariable String path){
        return "html/"+path;
    }
    @RequestMapping(value="/doc/{path}")
    public String freeMarker1(ModelMap mm,@PathVariable String path){
        return "doc/"+path;
    }
    /*@RequestMapping(value="/temple/{path}")
    public String freeMarker3(ModelMap mm,@PathVariable String path){
        return "temple/"+path;
    }*/

    //后台主页
    @RequestMapping(value="/Index.html")
    public String lying(ModelMap mm){
        return "index1";
    }

    //后台主页
    @RequestMapping(value="/index.html")
    public String index(ModelMap mm){
        return "sys_index";
    }
}
