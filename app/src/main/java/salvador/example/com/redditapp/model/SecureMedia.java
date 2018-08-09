package salvador.example.com.redditapp.model;


import com.google.gson.annotations.SerializedName;

public class SecureMedia{

	@SerializedName("secure_media")
	private SecureMedia secureMedia;

	@SerializedName("reddit_video")
	private RedditVideo redditVideo;

	public void setSecureMedia(SecureMedia secureMedia){
		this.secureMedia = secureMedia;
	}

	public SecureMedia getSecureMedia(){
		return secureMedia;
	}

	public void setRedditVideo(RedditVideo redditVideo){
		this.redditVideo = redditVideo;
	}

	public RedditVideo getRedditVideo(){
		return redditVideo;
	}

	@SerializedName("oembed")
	private Oembed oembed;

	@SerializedName("type")
	private String type;

	public void setOembed(Oembed oembed){
		this.oembed = oembed;
	}

	public Oembed getOembed(){
		return oembed;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"SecureMedia{" + 
			"secure_media = '" + secureMedia + '\'' + 
			",reddit_video = '" + redditVideo + '\'' +
					"oembed = '" + oembed + '\'' +
					",type = '" + type + '\'' +
					"}";
		}
}