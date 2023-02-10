package com.vaadin.bugrap.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

@Component
public interface CommentDao {

    public void saveComment(Comment comment);

    public Map<Reporter, List<Comment>> getAllComments(Report report);
}
