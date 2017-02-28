package prakhar.com.githubrepo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lendingkart on 2/27/2017.
 */
public class RepoAdapter extends BaseAdapter {

    RepoViewHolder viewHolder;
    Context context;
    ArrayList<Item> repolist = null;

    public RepoAdapter(Context context, ArrayList repolist) {
        this.context = context;
        if (context != null) {
            this.repolist = repolist;
        }
    }

    public void addData(ArrayList<Item> repolist) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

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
}

class RepoViewHolder {
    TextView RepoTitle;

    RepoViewHolder(View view) {
        RepoTitle = (TextView) view.findViewById(R.id.repotitle);

    }

}
