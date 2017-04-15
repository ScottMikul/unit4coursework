package com.teamtreehouse.courses.model;

import com.github.slugify.Slugify;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by scott on 3/28/2017.
 */
public class CourseIdea {
    private String slug;
    private String title;
    private String Creator;
    private Set<String> voters;

    public CourseIdea(String title, String creator) {
        voters = new HashSet<>();
        this.title = title;
        Creator = creator;
        try {
            Slugify slugify = new Slugify();
            slug = slugify.slugify(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public String getSlug() {
        return slug;
    }

    public boolean addVoter(String username){
        return voters.add(username);
    }

    public int getVoteCount(){
        return voters.size();
    }

    public Set<String> getVoters() {
        return voters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseIdea that = (CourseIdea) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return Creator != null ? Creator.equals(that.Creator) : that.Creator == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (Creator != null ? Creator.hashCode() : 0);
        return result;
    }
}
