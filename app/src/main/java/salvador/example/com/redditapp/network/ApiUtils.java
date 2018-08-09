package salvador.example.com.redditapp.network;


import salvador.example.com.redditapp.BuildConfig;
import salvador.example.com.redditapp.RetroFitClient;
import salvador.example.com.redditapp.network.Interface.APIService;

public class ApiUtils {

    public static final APIService mApiService = getAPIService();
    public static final String BASE_URL = BuildConfig.SERVER_API_URL;
    public static final String BASE_URLWTHOUT = BuildConfig.SERVER_API_URL;

    public static final String BASE = "{type}/.json";
    public static final String HOMES = "/.json";
    public static final String BASENOSLASH = "{type}.json";
    public static final String POPULAR = "r/popular";
    public static final String HOME = "home";
    public static final String ALL = "r/all";
    public static final String ORIGINAL = "original/all";
    public static final String VIDEOS = "r/videos";

    public static final String HOT = "/hot";
    public static final String NEW = "/new";
    public static final String CONTROVERSIAL = "/controversial";
    public static final String TOP = "/top";
    public static final String RISING = "/rising";

    private ApiUtils() {}


    public static APIService getAPIService() {

        return RetroFitClient.getClient(BASE_URL).create(APIService.class);
    }
    public static APIService getAPIServiceWithOut() {

        return RetroFitClient.getClient(BASE_URLWTHOUT).create(APIService.class);
    }
}
