package ghy.community.Controller;

import ghy.community.dto.PaginationDTO;
import ghy.community.dto.QuestionDTO;
import ghy.community.mapper.QuestionMapper;
import ghy.community.mapper.UserMapper;
import ghy.community.model.Question;
import ghy.community.model.User;
import ghy.community.service.QuestionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null)
        {
            for(Cookie cookie:cookies)
            {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if(user != null)
                    {
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        PaginationDTO paginationDTO = questionService.list(page, size);
        model.addAttribute("pagination",paginationDTO);
        return "index";
    }
}
