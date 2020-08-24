package life.genny.qwanda;

import java.io.Serializable;


import life.genny.qwanda.entity.BaseEntity;

public class QuestionSourceTarget implements Serializable {
	/**
	 * 
	 */
	 static final long serialVersionUID = 1L;
	
	 String questionCode;
	
	 String sourceCode;
	
	 String targetCode;
	
	 Question question;
	 BaseEntity source;
	 BaseEntity target;
	
	public QuestionSourceTarget(final Question question, final BaseEntity source, final BaseEntity target) {
		super();
		this.questionCode = question.getCode();
		this.sourceCode = source.getCode();
		this.targetCode = target.getCode();
		this.question = question;
		this.source = source;
		this.target = target;
	}
	
	public QuestionSourceTarget(String questionCode, String sourceCode, String targetCode) {
		super();
		this.questionCode = questionCode;
		this.sourceCode = sourceCode;
		this.targetCode = targetCode;
	}

	/**
	 * @return the questionCode
	 */
	public String getQuestionCode() {
		return questionCode;
	}

	/**
	 * @param questionCode the questionCode to set
	 */
	public void setQuestionCode(String questionCode) {
		this.questionCode = questionCode;
	}

	/**
	 * @return the sourceCode
	 */
	public String getSourceCode() {
		return sourceCode;
	}

	/**
	 * @param sourceCode the sourceCode to set
	 */
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	/**
	 * @return the targetCode
	 */
	public String getTargetCode() {
		return targetCode;
	}

	/**
	 * @param targetCode the targetCode to set
	 */
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	/**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @return the source
	 */
	public BaseEntity getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(BaseEntity source) {
		this.source = source;
	}

	/**
	 * @return the target
	 */
	public BaseEntity getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(BaseEntity target) {
		this.target = target;
	}
	
	
}
