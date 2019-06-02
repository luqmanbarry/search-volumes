package com.sellics.assignment.searchvolumes.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class SearchResponse implements Serializable {

    private static final long serialVersionUID = 3344151036144791459L;

    @NotBlank
    private String keyword;
    @Size(min = 0, max = 100, message = "score must be between 0 and 100")
    private int score;

    public SearchResponse(String keyword, int score) {
        this.keyword = keyword;
        this.score = score;
    }

    public SearchResponse() { }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "keyword='" + keyword + '\'' +
                ", score=" + score +
                '}';
    }
}
