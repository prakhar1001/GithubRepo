package prakhar.com.githubrepo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lendingkart on 2/27/2017.
 */

public interface APIInterface {

    String ENDPOINT = "https://api.github.com/";

    @GET("search/repositories")
    Call<GithubRepo> GET_REPO_FULLNAME(@Query("q") String keyword);


}
