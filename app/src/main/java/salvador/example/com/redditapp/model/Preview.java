package salvador.example.com.redditapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Preview {

	public Preview(){}
	@SerializedName("images")
	private List<ImagesItem> images;

	@SerializedName("enabled")
	private boolean enabled;


	protected Preview(Parcel in) {
		enabled = in.readByte() != 0;
	}


	public void setImages(List<ImagesItem> images){
		this.images = images;
	}

	public List<ImagesItem> getImages(){
		return images;
	}

	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}

	public boolean isEnabled(){
		return enabled;
	}

	@Override
 	public String toString(){
		return 
			"Preview{" + 
			"images = '" + images + '\'' + 
			",enabled = '" + enabled + '\'' + 
			",reddit_video_preview = '"  + '\'' +
			"}";
		}

}