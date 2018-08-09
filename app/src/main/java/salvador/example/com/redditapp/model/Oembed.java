package salvador.example.com.redditapp.model;

 import com.google.gson.annotations.SerializedName;

 public class Oembed{

	@SerializedName("author_name")
	private String authorName;

	@SerializedName("description")
	private String description;

	@SerializedName("provider_url")
	private String providerUrl;

	@SerializedName("title")
	private String title;

	@SerializedName("thumbnail_url")
	private String thumbnailUrl;

	@SerializedName("type")
	private String type;

	@SerializedName("version")
	private String version;

	@SerializedName("thumbnail_height")
	private int thumbnailHeight;

	@SerializedName("author_url")
	private String authorUrl;

	@SerializedName("thumbnail_width")
	private int thumbnailWidth;

	@SerializedName("width")
	private int width;

	@SerializedName("html")
	private String html;

	@SerializedName("provider_name")
	private String providerName;

	@SerializedName("height")
	private int height;

	public void setAuthorName(String authorName){
		this.authorName = authorName;
	}

	public String getAuthorName(){
		return authorName;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setProviderUrl(String providerUrl){
		this.providerUrl = providerUrl;
	}

	public String getProviderUrl(){
		return providerUrl;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setThumbnailUrl(String thumbnailUrl){
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getThumbnailUrl(){
		return thumbnailUrl;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setVersion(String version){
		this.version = version;
	}

	public String getVersion(){
		return version;
	}

	public void setThumbnailHeight(int thumbnailHeight){
		this.thumbnailHeight = thumbnailHeight;
	}

	public int getThumbnailHeight(){
		return thumbnailHeight;
	}

	public void setAuthorUrl(String authorUrl){
		this.authorUrl = authorUrl;
	}

	public String getAuthorUrl(){
		return authorUrl;
	}

	public void setThumbnailWidth(int thumbnailWidth){
		this.thumbnailWidth = thumbnailWidth;
	}

	public int getThumbnailWidth(){
		return thumbnailWidth;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public int getWidth(){
		return width;
	}

	public void setHtml(String html){
		this.html = html;
	}

	public String getHtml(){
		return html;
	}

	public void setProviderName(String providerName){
		this.providerName = providerName;
	}

	public String getProviderName(){
		return providerName;
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
			"Oembed{" + 
			"author_name = '" + authorName + '\'' + 
			",description = '" + description + '\'' +
			",provider_url = '" + providerUrl + '\'' +
			",title = '" + title + '\'' + 
			",thumbnail_url = '" + thumbnailUrl + '\'' + 
			",type = '" + type + '\'' + 
			",version = '" + version + '\'' + 
			",thumbnail_height = '" + thumbnailHeight + '\'' + 
			",author_url = '" + authorUrl + '\'' + 
			",thumbnail_width = '" + thumbnailWidth + '\'' + 
			",width = '" + width + '\'' + 
			",html = '" + html + '\'' + 
			",provider_name = '" + providerName + '\'' + 
			",height = '" + height + '\'' + 
			"}";
		}
}