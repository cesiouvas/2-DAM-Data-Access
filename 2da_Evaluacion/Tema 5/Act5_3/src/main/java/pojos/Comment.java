package pojos;

import org.bson.types.ObjectId;

/*
 * @author Sergio Castillo Llorens
 */

public class Comment {

    private int score;
    private ObjectId user_id;
    private String text;
    
    public Comment() {
    }

    public Comment(int score, ObjectId user_id, String text) {
        this.score = score;
        this.user_id = user_id;
        this.text = text;
    }
    
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ObjectId getUser_id() {
        return user_id;
    }

    public void setUser_id(ObjectId user_id) {
        this.user_id = user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    
}
