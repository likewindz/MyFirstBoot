package org.bdqn.firstwork.service;

import java.util.ArrayList;
import java.util.List;

import org.bdqn.firstwork.dto.PaginationDTO;
import org.bdqn.firstwork.dto.QuestionDTO;
import org.bdqn.firstwork.exception.CustomizeException;
import org.bdqn.firstwork.mapper.QuestionMapper;
import org.bdqn.firstwork.model.Question;
import org.bdqn.firstwork.model.User;
import org.bdqn.firstwork.utils.ControllerError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value="questionService")
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionMapper questionMapper;
	
	@Override
	public void addQuestion(Question question) {
		questionMapper.addQuestion(question);
	}

	@Override
	public PaginationDTO<QuestionDTO> questionList(Integer curPage,Integer size,Integer userId) {
		
		PaginationDTO<QuestionDTO> dto = new PaginationDTO<QuestionDTO>();
		Integer totalCount = questionMapper.getTotalCount(userId);
		dto.oprData(curPage, size, totalCount);
		Integer offset = (dto.getCurPage()-1)*size;
		 List<Question> questionList = questionMapper.questionList(offset,size,userId);
		 List<QuestionDTO> questionDTOs =null;
		 if(questionList!=null) {
			 questionDTOs  =new ArrayList<QuestionDTO>();
			 for (Question q : questionList) {
				 QuestionDTO qdto =new QuestionDTO();
				 BeanUtils.copyProperties(q, qdto);
				 User user = questionMapper.getUserByCreateId(qdto.getCreater());
				 qdto.setUser(user);
				 questionDTOs.add(qdto);
			}
			 dto.setData(questionDTOs);
		 }
		return dto;
	}

	@Override
	@Transactional
	public QuestionDTO getQuestionMeg(Integer id,boolean commited) {
		if(!commited) {
			questionMapper.addViewCount(id);
		}
		QuestionDTO q = questionMapper.getQuestionMeg(id);
		if(q==null) {
			throw new CustomizeException(ControllerError.not_find);
		}
		q.setDescription(q.getDescription().replaceAll(" ","&nbsp;").replaceAll("\r","<br/>"));
		return q;
	}

	@Transactional
	@Override
	public void updateQuestion(Question question) {
		int result = questionMapper.updateQuestion(question);
		if(result==0) {
			throw new CustomizeException(ControllerError.not_find);
		}
	}
	
	

}
