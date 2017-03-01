package prakhar.com.githubrepo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import prakhar.com.githubrepo.db.Repo;
import prakhar.com.githubrepo.db.RepoDao;

/**
 * Created by lendingkart on 2/27/2017.
 */
public class RepoAdapter extends BaseAdapter {

    //Dao --> Data Access Object
    private RepoDao repoDao; // Sql access object
    private Repo repo; // Used for creating a LOG Object

    private final String DB_NAME = "repo-db";  //Name of Db file in the Device

    RepoViewHolder viewHolder;
    Context context;
    ArrayList<GithubRepo.Item> repolist = null;

    public RepoAdapter(Context context, ArrayList repolist) {
        this.context = context;
        if (context != null) {
            this.repolist = repolist;
        }
    }

    public void addData(ArrayList<GithubRepo.Item> repolist) {
        this.repolist = repolist;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.repolist.size();
    }

    @Override
    public Object getItem(int position) {
        return this.repolist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.repo_item_row, parent, false);
            viewHolder = new RepoViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RepoViewHolder) convertView.getTag();
        }

        viewHolder.RepoTitle.setText(this.repolist.get(position).getFullName());

        return convertView;
    }


    class RepoViewHolder {
        TextView RepoTitle;

        RepoViewHolder(View view) {
            RepoTitle = (TextView) view.findViewById(R.id.repotitle);

        }

    }
}