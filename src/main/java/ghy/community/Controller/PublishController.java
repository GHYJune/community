package ghy.community.Controller;

import ghy.community.mapper.QuestionMapper;
import ghy.community.mapper.UserMapper;
import ghy.community.model.Question;
import ghy.community.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/publish")
    public  String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model)
    {
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if(title == null || title.equals(""))
        {
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || description.equals(""))
        {
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if(tag == null || tag.equals(""))
        {
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        User user = null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null)
        {
            for(Cookie cookie:cookies)
            {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if(user != null)
                    {
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";
    }
}
