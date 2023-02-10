package com.vaadin.bugrap.dao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.vaadin.bugrap.domain.entities.Comment;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;
import org.vaadin.bugrap.domain.spring.CommentRepository;

@Component
public class CommentDaoImpl implements CommentDao{

    private CommentRepository commentRepository;

    public CommentDaoImpl(CommentRepository commentRepository) {
            this.commentRepository = commentRepository;
    }

    public void saveComment(Comment comment) {
            this.commentRepository.save(comment);
    }

    public Map<Reporter, List<Comment>> getAllComments(Report report) {
            List<Comment> allComments = this.commentRepository.findAllByReport(report);
            if (allComments.size() > 0) {
                    return allComments.stream().collect(Collectors.groupingBy(Comment::getAuthor));
            }
            return null;
    }

}
