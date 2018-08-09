package salvador.example.com.redditapp.model;

import com.google.gson.annotations.SerializedName;

public class LinkFlairRichtextItem{

	@SerializedName("t")
	private String T;

	@SerializedName("e")
	private String E;

	@SerializedName("a")
	private String A;

	@SerializedName("u")
	private String U;

	public void setT(String T){
		this.T = T;
	}

	public String getT(){
		return T;
	}

	public void setE(String E){
		this.E = E;
	}

	public String getE(){
		return E;
	}

	public void setA(String A){
		this.A = A;
	}

	public String getA(){
		return A;
	}

	public void setU(String U){
		this.U = U;
	}

	public String getU(){
		return U;
	}

	@Override
 	public String toString(){
		return 
			"LinkFlairRichtextItem{" + 
			"t = '" + T + '\'' + 
			",e = '" + E + '\'' + 
			",a = '" + A + '\'' + 
			",u = '" + U + '\'' + 
			"}";
		}
}