package salvador.example.com.redditapp.model;


import com.google.gson.annotations.SerializedName;

 public class RedditVideo{

	@SerializedName("duration")
	private int duration;

	@SerializedName("is_gif")
	private boolean isGif;

	@SerializedName("dash_url")
	private String dashUrl;

	@SerializedName("fallback_url")
	private String fallbackUrl;

	@SerializedName("width")
	private int width;

	@SerializedName("scrubber_media_url")
	private String scrubberMediaUrl;

	@SerializedName("hls_url")
	private String hlsUrl;

	@SerializedName("transcoding_status")
	private String transcodingStatus;

	@SerializedName("height")
	private int height;

	public void setDuration(int duration){
		this.duration = duration;
	}

	public int getDuration(){
		return duration;
	}

	public void setIsGif(boolean isGif){
		this.isGif = isGif;
	}

	public boolean isIsGif(){
		return isGif;
	}

	public void setDashUrl(String dashUrl){
		this.dashUrl = dashUrl;
	}

	public String getDashUrl(){
		return dashUrl;
	}

	public void setFallbackUrl(String fallbackUrl){
		this.fallbackUrl = fallbackUrl;
	}

	public String getFallbackUrl(){
		return fallbackUrl;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getWidth(){
		return width;
	}

	public void setScrubberMediaUrl(String scrubberMediaUrl){
		this.scrubberMediaUrl = scrubberMediaUrl;
	}

	public String getScrubberMediaUrl(){
		return scrubberMediaUrl;
	}

	public void setHlsUrl(String hlsUrl){
		this.hlsUrl = hlsUrl;
	}

	public String getHlsUrl(){
		return hlsUrl;
	}

	public void setTranscodingStatus(String transcodingStatus){
		this.transcodingStatus = transcodingStatus;
	}

	public String getTranscodingStatus(){
		return transcodingStatus;
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
			"RedditVideo{" + 
			"duration = '" + duration + '\'' + 
			",is_gif = '" + isGif + '\'' + 
			",dash_url = '" + dashUrl + '\'' + 
			",fallback_url = '" + fallbackUrl + '\'' + 
			",width = '" + width + '\'' + 
			",scrubber_media_url = '" + scrubberMediaUrl + '\'' + 
			",hls_url = '" + hlsUrl + '\'' + 
			",transcoding_status = '" + transcodingStatus + '\'' + 
			",height = '" + height + '\'' + 
			"}";
		}
}