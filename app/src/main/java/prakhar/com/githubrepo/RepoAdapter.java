package prakhar.com.githubrepo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by lendingkart on 2/27/2017.
 */
public class RepoAdapter extends BaseAdapter {

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
        viewHolder.RepoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,repolist.get(position).getHtmlUrl(),Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}

class RepoViewHolder {
    TextView RepoTitle;

    RepoViewHolder(View view) {
        RepoTitle = (TextView) view.findViewById(R.id.repotitle);

    }

}
