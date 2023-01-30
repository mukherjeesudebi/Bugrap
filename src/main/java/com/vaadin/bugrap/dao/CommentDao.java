package com.vaadin.bugrap.dao;

import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.spring.CommentRepository;

public class CommentDao {
	private CommentRepository commentRepository;

	public CommentDao(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	public void saveComment(Comment comment) {
		this.commentRepository.save(comment);
	}
}
