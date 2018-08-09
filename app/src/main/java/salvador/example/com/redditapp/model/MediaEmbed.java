package salvador.example.com.redditapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MediaEmbed {

	@SerializedName("scrolling")
	private boolean scrolling;

	@SerializedName("width")
	private int width;

	@SerializedName("content")
	private String content;

	@SerializedName("height")
	private int height;


	public void setScrolling(boolean scrolling){
		this.scrolling = scrolling;
	}

	public boolean isScrolling(){
		return scrolling;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getWidth(){
		return width;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setHeight(int height){
		this.height = height;
	}

	public int getHeight(){
		return height;
	}

	@Override
 	public String toString(){
		return 
			"MediaEmbed{" + 
			"scrolling = '" + scrolling + '\'' + 
			",width = '" + width + '\'' + 
			",content = '" + content + '\'' + 
			",height = '" + height + '\'' + 
			"}";
		}


}