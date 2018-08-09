package salvador.example.com.redditapp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Comments  {

    public Comments(){}




    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getScore() {
        return "Score: "+score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreated_utc() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(created_utc)*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm  dd/MM/yy", Locale.ENGLISH);
        String time = simpleDateFormat.format(calendar.getTime());
        return time;
    }

    public void setCreated_utc(String created_utc) {
        this.created_utc = created_utc;
    }

    public ArrayList<Comments> getMoreComments() {
        return moreComments;
    }

    public void setMoreComments(ArrayList<Comments> moreComments) {
        this.moreComments = moreComments;
    }

    private String body;
    private String score;
    private String id;
    private String author;
    private String created_utc;
    private ArrayList<Comments> moreComments;


}
