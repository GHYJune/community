package ghy.community.service;

import ghy.community.dto.PaginationDTO;
import ghy.community.dto.QuestionDTO;
import ghy.community.mapper.QuestionMapper;
import ghy.community.mapper.UserMapper;
import ghy.community.model.Question;
import ghy.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    public PaginationDTO list(Integer page,Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount,page,size);

        if(page < 1){
            page = 1;
        }
        if(page > paginationDTO.getTotalPage())
        {
            page = paginationDTO.getTotalPage();
        }

        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        for(Question question : questions){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOS);

        return paginationDTO;

    }
}
