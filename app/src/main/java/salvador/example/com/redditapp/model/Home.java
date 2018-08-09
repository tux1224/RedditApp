package salvador.example.com.redditapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Home {

	public Home(){}
	@SerializedName("data")
	private Data data;

	@SerializedName("kind")
	private String kind;

	protected Home(Parcel in) {
		kind = in.readString();
	}


	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setKind(String kind){
		this.kind = kind;
	}

	public String getKind(){
		return kind;
	}

	@Override
 	public String toString(){
		return 
			"Home{" + 
			"data = '" + data + '\'' + 
			",kind = '" + kind + '\'' + 
			"}";
		}


}