package salvador.example.com.redditapp.network.Interface;


import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import salvador.example.com.redditapp.model.Home;
import salvador.example.com.redditapp.network.ApiUtils;


public interface APIService {

    @GET(ApiUtils.BASE)
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<Home> getHome(@Path("type") String type);

    @GET(ApiUtils.HOMES)
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<Home> getHomeBase();

    @GET(ApiUtils.BASENOSLASH)
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<JsonArray> getPost(@Path("type") String type);



}
