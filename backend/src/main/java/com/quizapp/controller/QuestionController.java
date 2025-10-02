package com.quizapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.repository.QuestionRepo;
import com.quizapp.repository.QuizRepo;

@RestController
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
	QuestionRepo questionRepo;
	
	@Autowired
	QuizRepo quizRepo;
	
/* -------------------------------------------------------------------------------------------------
		                         CREAT or INSERT
		
	   post  http://localhost:8080/question/save/1
	  		{
		    	"question":"first question"
				"option1":"first option"
				"option2":"second option"
				"option3":"third option"
				"option4":"fourth option"
				"correctans":"correct option"
			}
*/
	
	@PostMapping("/save/{pid}")
	public String savequestion(@RequestBody Question question, @PathVariable("pid") int pid) {
		Optional<Quiz> opt= quizRepo.findById(pid);
		if (opt.isPresent()) {
			question.setQuiz(opt.get());
			questionRepo.save(question);
			return "questions saved ....";
		}
		return null;
	}
	
// ---------------------------------------------------------------------------------------------------
//                                    READ ALL	
	
	// Get http://localhost:8080/question/read
	@GetMapping("/read")
	public List<Question> getallquestions() {		 
		return questionRepo.findAll();
	}

// ----------------------------------------------------------------------------------------------------
//                               	READ only REQUIRED
	
	// Get  http://localhost:8080/question/quizquestions/1
	
	@GetMapping("/quizquestions/{pid}")
	public List<Question> getQuizQuestions(@PathVariable("pid")int pid){
			return questionRepo.findByQuizPid(pid);

	}
	
/* ----------------------------------------------------------------------------------------------------
										UPDATE
	
	PUT http://localhost:8080/question/update/1/1
	
{
    "question":"Updated question text",
    "option1":"A",
    "option2":"B",
    "option3":"C",
    "option4":"D",
    "correctans":"B"
}

	
*/
	@PutMapping("/update/{id}/{pid}")
	public String updateQuestion(@RequestBody Question question,
	                             @PathVariable("id") int id,
	                             @PathVariable("pid") int pid) {
	    Optional<Question> qOpt = questionRepo.findById(id);
	    Optional<Quiz> quizOpt = quizRepo.findById(pid);

	    if (qOpt.isPresent() && quizOpt.isPresent()) {
	        Question existing = qOpt.get();
	        existing.setQuestion(question.getQuestion());
	        existing.setOption1(question.getOption1());
	        existing.setOption2(question.getOption2());
	        existing.setOption3(question.getOption3());
	        existing.setOption4(question.getOption4());
	        existing.setCorrectans(question.getCorrectans());
	        existing.setQuiz(quizOpt.get());

	        questionRepo.save(existing);
	        return "Question Updated ........";
	    }
	    return "Question or Quiz not found!";
	}

// -------------------------------------------------------------------------------------------------------
// 									DELETE	

// 	Delete http://localhost:8080/question/delete/1
	
	@DeleteMapping("/delete/{id}")
	public String deletequestion(@PathVariable("id") int id) {
		questionRepo.deleteById(id);
		return "Question deleted ........";
	}
	
	
}
