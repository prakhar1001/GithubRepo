package prakhar.com.githubrepo;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import prakhar.com.githubrepo.db.DaoMaster;
import prakhar.com.githubrepo.db.DaoSession;
import prakhar.com.githubrepo.db.Repo;
import prakhar.com.githubrepo.db.RepoDao;
import prakhar.com.githubrepo.network.RetroClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    //Dao --> Data Access Object
    private RepoDao repoDao; // Sql access object
    private Repo repo; // Used for creating a LOG Object


    private final String DB_NAME = "repo-db";  //Name of Db file in the Device

    ListView repoListView;
    private ProgressDialog mProgressDialog;
    MaterialSearchView searchView;
    RepoAdapter repoAdapter;
    ArrayList<GithubRepo.Item> repoArrayList;
    Toolbar toolbar;
    String globalquery = "Github Repo";
    boolean repoListStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repoListView = (ListView) findViewById(R.id.repolist);

        mProgressDialog = displayProgressDialog(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        repoDao = setup_db();

        // TODO: 2/28/2017 get arraylist of bookmarked repos from database with their html urls
        repoAdapter = new RepoAdapter(MainActivity.this, changeRepoToGithubItem(getFromSQL()));
        repoListView.setAdapter(repoAdapter);

        repoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (repoArrayList != null) {
                    repo = new Repo(null, repoArrayList.get(position).getFullName(), repoArrayList.get(position).getHtmlUrl());
                    saveObjectToSQL(repo);
                }
            }
        });


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

    public ArrayList<GithubRepo.Item> changeRepoToGithubItem(List<Repo> repo) {

        ArrayList<GithubRepo.Item> repoarraylist = new ArrayList<>(repo.size());
        if (repo.size() > 0) {
            for (int position = 0; position < repo.size(); position++) {
                GithubRepo.Item repoitem = new GithubRepo.Item(null);
                repoitem.setFullName(repo.get(position).getTitle());
                repoitem.setHtmlUrl(repo.get(position).getHtml_url());

                repoarraylist.add(repoitem);
            }
        } else {
            GithubRepo.Item repoitem = new GithubRepo.Item(null);
            repoitem.setFullName("No Repository has been bookmarked");
            repoitem.setHtmlUrl(null);

            repoarraylist.add(repoitem);

        }
        return repoarraylist;
    }

    public List<Repo> getFromSQL() {
        List<Repo> repo_List = repoDao.queryBuilder().orderDesc(RepoDao.Properties.Id).build().list();
        //Get the list of all LOGS in Database in descending order

        if (repo_List.size() > 0) {  //if list is not null

            return repo_List;
            //get(0)--> 1st object
            // getText() is the function in LOG class
        }
        return repo_List;
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
                repoListStatus = true;
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
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        } else if (!searchView.isSearchOpen() && !repoListStatus) {
            super.onBackPressed();
        }
        if (repoListStatus) {
            repoListStatus = false;
            repoAdapter = new RepoAdapter(MainActivity.this, changeRepoToGithubItem(getFromSQL()));
            repoListView.setAdapter(repoAdapter);
        }


    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("TAG, onSavedInstanceState");

        outState.putString("query", globalquery);
        outState.putParcelableArrayList("ParceableRepoList", repoArrayList);
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        System.out.println("TAG, onRestoreInstanceState");

        repoArrayList = savedState.getParcelableArrayList("ParceableRepoList");
        repoAdapter.addData(repoArrayList);

        globalquery = savedState.getString("query");

    }

    @Override
    protected void onResume() {
        super.onResume();

        toolbar.setTitle(globalquery);
        toolbar.setTitleTextColor(Color.WHITE);

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


    public void saveObjectToSQL(Repo repo_object) {
        repoDao.insert(repo_object);
    }

    //Return the Configured LogDao Object
    public RepoDao setup_db() {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this, DB_NAME, null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        DaoMaster master = new DaoMaster(db);//create masterDao
        DaoSession masterSession = master.newSession(); //Creates Session session
        return masterSession.getRepoDao();
    }


}
