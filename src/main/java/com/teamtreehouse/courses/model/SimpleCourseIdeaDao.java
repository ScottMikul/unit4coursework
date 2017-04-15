package com.teamtreehouse.courses.model;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 4/2/2017.
 */
public class SimpleCourseIdeaDao implements CourseIdeaDao {
    private List<CourseIdea> ideas;

    public SimpleCourseIdeaDao() {
        this.ideas = new ArrayList<>();
    }

    public boolean add(CourseIdea idea) {
        ideas.add(idea);
        return true;
    }

    public List<CourseIdea> findAll() {
        return ideas;
    }

    @Override
    public CourseIdea findBySlug(String slug) {
        return ideas.stream()
                .filter(idea->idea.getSlug().equals(slug))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
