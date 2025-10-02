import { useParams, useNavigate } from "react-router-dom";
import { useState } from "react";
import useQuestions from "../hooks/useQuestions";
import "./QuizDetail.css";

function QuizDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { data: questions, loading, error } = useQuestions(id);

  const [answers, setAnswers] = useState({});
  const [submitted, setSubmitted] = useState(false);
  const [score, setScore] = useState(0);

  if (loading) return <h2>Loading questions...</h2>;
  if (error) return <h2>Failed to load questions</h2>;

  const handleOptionClick = (questionId, optionKey) => {
    if (submitted) return;
    setAnswers(prev => ({ ...prev, [questionId]: optionKey }));
  };

  const handleSubmit = () => {
    let calculatedScore = 0;
    questions.forEach(q => {
      if (answers[q.id] === q.correctans) calculatedScore++;
    });
    setScore(calculatedScore);
    setSubmitted(true);
  };

  const handleRestart = () => {
    setAnswers({});
    setSubmitted(false);
    setScore(0);
  };

  const handleBack = () => navigate("/");

  return (
    <div className="quiz-detail">
      {/* Back Button */}
      <div className="back-btn-container">
        <button className="back-btn" onClick={handleBack}>← Back</button>
      </div>

      <h2>Quiz {id}</h2>

      {Array.isArray(questions) && questions.length > 0 ? (
        <>
          {questions.map(q => {
            const options = [
              ["option1", q.option1],
              ["option2", q.option2],
              ["option3", q.option3],
              ["option4", q.option4],
            ];

            return (
              <div key={q.id} className="question-card">
                <h3>{q.question}</h3>
                <ul>
                  {options.map(([key, text]) => {
                    const isSelected = answers[q.id] === key;
                    const isCorrect = key === q.correctans;
                    const isWrongSelected = submitted && isSelected && !isCorrect;

                    const className = [
                      "option",
                      isSelected ? "selected" : "",
                      submitted ? (isCorrect ? "correct" : isWrongSelected ? "wrong" : "") : ""
                    ].join(" ").trim();

                    return (
                      <li
                        key={key}
                        className={className}
                        onClick={() => handleOptionClick(q.id, key)}
                      >
                        {text}
                      </li>
                    );
                  })}
                </ul>
              </div>
            );
          })}

          {!submitted ? (
            <button className="submit-btn" onClick={handleSubmit}>
              Submit
            </button>
          ) : (
            <div className="result-row">
              <h2>You scored {score} / {questions.length}</h2>
              <button className="submit-btn" onClick={handleRestart}>
                Restart
              </button>
            </div>
          )}
        </>
      ) : (
        <p>No questions available</p>
      )}
    </div>
  );
}

export default QuizDetail;
