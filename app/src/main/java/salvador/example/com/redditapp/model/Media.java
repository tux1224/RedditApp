package salvador.example.com.redditapp.model;


import com.google.gson.annotations.SerializedName;

public class Media{



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
			"Media{" +
			",oembed = '" + oembed + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}