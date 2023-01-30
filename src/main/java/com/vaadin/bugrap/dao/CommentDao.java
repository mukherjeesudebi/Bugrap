package com.vaadin.bugrap.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.spring.CommentRepository;

@Component
public class CommentDao {
	private CommentRepository commentRepository;

	public CommentDao(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	public void saveComment(Comment comment) {
		this.commentRepository.save(comment);
	}
	
	public List<Comment> getAllComments(Report report) {
		return this.commentRepository.findAllByReport(report);
	}
}
