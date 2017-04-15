package com.teamtreehouse.courses.model;

import java.util.List;

/**
 * Created by scott on 4/2/2017.
 */
public interface CourseIdeaDao {
    boolean add(CourseIdea idea);

    List<CourseIdea> findAll();

    CourseIdea findBySlug(String slug);
}
