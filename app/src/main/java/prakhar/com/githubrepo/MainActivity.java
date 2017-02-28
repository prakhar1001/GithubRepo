package prakhar.com.githubrepo;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import prakhar.com.githubrepo.network.RetroClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    MaterialSearchView searchView;
    RepoAdapter repoAdapter;
    ArrayList<GithubRepo.Item> repoArrayList;
    Toolbar toolbar;
    String globalquery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView repoListView = (ListView) findViewById(R.id.repolist);

        mProgressDialog = displayProgressDialog(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: 2/28/2017 get arraylist of bookmarked repos from database with their html urls
        repoAdapter = new RepoAdapter(MainActivity.this, new ArrayList<GithubRepo.Item>());
        repoListView.setAdapter(repoAdapter);


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                callGithubRepoAPI(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });


        //if (NetworkUtils.isNetworkConnected(MainActivity.this)) {

        /*}else {
            dismissProgress();
            Toast.makeText(MainActivity.this, "Please connect with working network.", Toast.LENGTH_LONG).show();
        }*/
    }

    private void callGithubRepoAPI(final String query) {

        showProgress();
        Call<GithubRepo> GithubRepoCall = RetroClient.getInstance().getRetrofit().create(APIInterface.class).GET_REPO_FULLNAME(query);
        GithubRepoCall.enqueue(new Callback<GithubRepo>() {
            @Override
            public void onResponse(Call<GithubRepo> call, Response<GithubRepo> response) {
                dismissProgress();
                toolbar.setTitle(query);
                toolbar.setTitleTextColor(Color.WHITE);

                globalquery = query;
                repoArrayList = new ArrayList<GithubRepo.Item>();
                repoArrayList = (ArrayList<GithubRepo.Item>) response.body().getItems();
                if (repoArrayList.size() > 0) {
                    repoAdapter.addData(repoArrayList);
                } else
                    Toast.makeText(MainActivity.this, "No Related Repo Found", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onFailure(Call<GithubRepo> call, Throwable t) {
                dismissProgress();
                Log.d("failure", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("TAG, onSavedInstanceState");

        outState.putString("query",globalquery);
        outState.putParcelableArrayList("ParceableRepoList", repoArrayList);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        System.out.println("TAG, onRestoreInstanceState");

        repoArrayList = savedState.getParcelableArrayList("ParceableRepoList");
        repoAdapter.addData(repoArrayList);

        globalquery = savedState.getString("query");

        toolbar.setTitle(globalquery);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    private void showProgress() {
        if (mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public static ProgressDialog displayProgressDialog(Context context) {
        ProgressDialog progressDialog = ProgressDialog.show(context, "", "", true);
        progressDialog.setContentView(R.layout.tresbudialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        assert progressDialog.getWindow() != null;
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.dismiss();
        return progressDialog;
    }
}
