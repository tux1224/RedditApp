package salvador.example.com.redditapp.model;

 import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{



	@SerializedName("children")
	private List<ChildrenItem> children;

	@SerializedName("secure_media")
	private SecureMedia secureMedia;

	@SerializedName("subreddit")
	private String subreddit;

	@SerializedName("subreddit_id")
	private String subredditId;

	@SerializedName("score")
	private int score;

	@SerializedName("num_comments")
	private int numComments;

	@SerializedName("post_hint")
	private String postHint;

	@SerializedName("id")
	private String id;

	@SerializedName("created_utc")
	private double createdUtc;

	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("created")
	private double created;

	@SerializedName("author")
	private String author;


	@SerializedName("subreddit_name_prefixed")
	private String subredditNamePrefixed;

	@SerializedName("domain")
	private String domain;

	@SerializedName("name")
	private String name;

	@SerializedName("ups")
	private int ups;

	@SerializedName("permalink")
	private String permalink;

	@SerializedName("preview")
	private Preview preview;

	@SerializedName("media")
	private Media media;

	@SerializedName("title")
	private String title;

	@SerializedName("secure_media_embed")
	private SecureMediaEmbed secureMediaEmbed;

	@SerializedName("is_self")
	private boolean isSelf;

	@SerializedName("subreddit_type")
	private String subredditType;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@SerializedName("url")
	private String url;

	@SerializedName("is_reddit_media_domain")
	private boolean isRedditMediaDomain;




	public void setChildren(List<ChildrenItem> children){
		this.children = children;
	}

	public List<ChildrenItem> getChildren(){
		return children;
	}

	public void setSecureMedia(SecureMedia secureMedia){
		this.secureMedia = secureMedia;
	}

	public SecureMedia getSecureMedia(){
		return secureMedia;
	}

	public void setSubreddit(String subreddit){
		this.subreddit = subreddit;
	}

	public String getSubreddit(){
		return subreddit;
	}

	public void setSubredditId(String subredditId){
		this.subredditId = subredditId;
	}

	public String getSubredditId(){
		return subredditId;
	}

	public void setScore(int score){
		this.score = score;
	}

	public int getScore(){
		return score;
	}

	public void setNumComments(int numComments){
		this.numComments = numComments;
	}

	public int getNumComments(){
		return numComments;
	}

	public void setPostHint(String postHint){
		this.postHint = postHint;
	}

	public String getPostHint(){
		return postHint;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCreatedUtc(double createdUtc){
		this.createdUtc = createdUtc;
	}

	public double getCreatedUtc(){
		return createdUtc;
	}

	public void setThumbnail(String thumbnail){
		this.thumbnail = thumbnail;
	}

	public String getThumbnail(){
		return thumbnail;
	}


	public void setCreated(double created){
		this.created = created;
	}

	public double getCreated(){
		return created;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setSubredditNamePrefixed(String subredditNamePrefixed){
		this.subredditNamePrefixed = subredditNamePrefixed;
	}

	public String getSubredditNamePrefixed(){
		return subredditNamePrefixed;
	}

	public void setDomain(String domain){
		this.domain = domain;
	}

	public String getDomain(){
		return domain;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}


	public void setUps(int ups){
		this.ups = ups;
	}

	public int getUps(){
		return ups;
	}


	public void setPermalink(String permalink){
		this.permalink = permalink;
	}

	public String getPermalink(){
		return permalink;
	}


	public void setPreview(Preview preview){
		this.preview = preview;
	}

	public Preview getPreview(){
		return preview;
	}


	public void setMedia(Media media){
		this.media = media;
	}

	public Media getMedia(){
		return media;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setSecureMediaEmbed(SecureMediaEmbed secureMediaEmbed){
		this.secureMediaEmbed = secureMediaEmbed;
	}

	public SecureMediaEmbed getSecureMediaEmbed(){
		return secureMediaEmbed;
	}

	public void setIsSelf(boolean isSelf){
		this.isSelf = isSelf;
	}

	public boolean isIsSelf(){
		return isSelf;
	}

	public void setIsRedditMediaDomain(boolean isRedditMediaDomain){
		this.isRedditMediaDomain = isRedditMediaDomain;
	}

	public boolean isIsRedditMediaDomain(){
		return isRedditMediaDomain;
	}





}