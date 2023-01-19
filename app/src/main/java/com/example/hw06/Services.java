package com.example.hw06;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Services {

    public static final String DOC_FORUM = "forums";
    public static final String DOC_COM = "comments";

    public static class Forum implements Serializable {
        private String title;
        private String createdById;
        private String createdByName;
        private Date createdAt;
        private String description;
        private ArrayList<String> likedBy = new ArrayList<>();
        private String forumId;
        private ArrayList<Comment> comments = new ArrayList<>();

        public Forum() {
        }

        public Forum(String forumId, String title, String createdById, String createdByName, Date createdAt, String description) {
            this.title = title;
            this.createdById = createdById;
            this.createdByName = createdByName;
            this.createdAt = createdAt;
            this.description = description;
            this.forumId = forumId;
        }

        public String getForumId() {
            return forumId;
        }

        public String getTitle() {
            return title;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setLikedBy(ArrayList<String> likedBy) {
            this.likedBy = likedBy;
        }

        public String getCreatedById() {
            return createdById;
        }

        public ArrayList<Comment> getComments() {
            return this.comments;
        }

        public void clearComments(){
            this.comments.clear();
        }

        public void addComment(Comment comment){
            this.comments.add(comment);
        }

        public void setComments(ArrayList<Comment> comments) {
            this.comments = comments;
        }

        public void addLike(String uid){
            this.likedBy.add(uid);
        }

        public void unLike(String uid){
            this.likedBy.remove(uid);
        }

        @Override
        public String toString() {
            return "Forum{" +
                    "title='" + title + '\'' +
                    ", createdById='" + createdById + '\'' +
                    ", createdByName='" + createdByName + '\'' +
                    ", createdAt=" + createdAt +
                    ", description='" + description + '\'' +
                    ", likedBy=" + likedBy +
                    ", forumId='" + forumId + '\'' +
                    ", comments=" + comments +
                    '}';
        }

        public void setCreatedById(String createdById) {
            this.createdById = createdById;
        }

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
        }

        public void setForumId(String forumId) {
            this.forumId = forumId;
        }

        public String getDescription() {
            return description;
        }

        public ArrayList<String> getLikedBy() {
            return likedBy;
        }


    }

    public static class Comment implements Serializable {

        String id;
        String text;
        String createdById;
        String createdByName;
        Date createdAt;
        String commentId;

        public Comment(String id, String text, String createdById, String createdByName, Date createdAt) {
            this.id = id;
            this.text = text;
            this.createdById = createdById;
            this.createdByName = createdByName;
            this.createdAt = createdAt;
        }

        public Comment(){}

        @Override
        public String toString() {
            return "Comment{" +
                    "id='" + id + '\'' +
                    ", text='" + text + '\'' +
                    ", createdById='" + createdById + '\'' +
                    ", createdByName='" + createdByName + '\'' +
                    ", createdAt=" + createdAt +
                    ", commentId='" + commentId + '\'' +
                    '}';
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedById() {
            return createdById;
        }

        public void setCreatedById(String createdById) {
            this.createdById = createdById;
        }

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }
    }

}
