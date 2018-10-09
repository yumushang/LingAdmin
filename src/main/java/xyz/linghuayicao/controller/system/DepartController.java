package xyz.linghuayicao.controller.system;

import com.github.pagehelper.PageInfo;
import xyz.linghuayicao.controller.base.BaseController;
import xyz.linghuayicao.entity.system.TSysDepart;
import xyz.linghuayicao.entity.system.TSysDepartCriteria;
import xyz.linghuayicao.service.system.TSysDepartService;
import xyz.linghuayicao.utils.Consts;
import xyz.linghuayicao.utils.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value="/depart")
public class DepartController extends BaseController {


    @Autowired
    private TSysDepartService departService;


    //菜单管理
    @RequestMapping(value="/list")
    public String list(ModelMap mm){
        return "depart/list";
    }

    //用户input页
    @RequestMapping(value="/input")
    public String Input(ModelMap mm,HttpServletRequest request){
        if(!StringUtils.isEmpty(request.getParameter("id"))){
            Integer id = Integer.parseInt(request.getParameter("id"));
            mm.addAttribute("object",departService.selectByPrimaryKey(id));
        }
        mm.addAttribute("departList",departService.selectByExample(new TSysDepartCriteria()));
        return "depart/input";
    }

    @RequestMapping(value="/departList")
    @ResponseBody
    public HttpResult departList(Integer pageSize, Integer pageNum, String search){
        HttpResult result = new HttpResult();
        try{
            TSysDepartCriteria criteria = new TSysDepartCriteria();
            if(search!=null&&!search.equals("")){
                criteria.createCriteria().andDepartnameLike(search);
            }
            PageInfo info = departService.selectByExample(criteria,pageSize,pageNum);
            result.setCount(info.getTotal());
            result.setData(info.getList());
            result.setCode(Consts.SUCCESS);
            result.setMsg("获取成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("获取失败!"+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/saveDepart")
    @ResponseBody
    public HttpResult saveDepart(TSysDepart depart){
        HttpResult result = new HttpResult();
        try{
            if (depart==null) throw new Exception("参数错误!");
            //id为空则新增
            if(depart.getId()==null||depart.getId()==0){
                depart.setCreatetime(new Date());
                depart = departService.insertSelective(depart);
            }else{
                //更新
                departService.updateByPrimaryKeySelective(depart);
            }
            //返回id
            result.setData(depart.getId());
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/delDepart")
    @ResponseBody
    public HttpResult delDepart(@RequestParam(value = "departIds[]")Integer[] departIds){
        HttpResult result = new HttpResult();
        try{
            List<Integer> ids = Arrays.asList(departIds);
            TSysDepartCriteria criteria = new TSysDepartCriteria();
            criteria.createCriteria().andIdIn(ids);
            departService.deleteByExample(criteria);
            //返回id
            result.setCode(Consts.SUCCESS);
            result.setMsg("操作成功");
        }catch (Exception e){
            result.setCode(Consts.ERROR);
            result.setMsg("操作失败!"+e.getMessage());
        }
        return result;
    }

}
