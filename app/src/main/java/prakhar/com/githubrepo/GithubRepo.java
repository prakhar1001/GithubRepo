package prakhar.com.githubrepo;

/**
 * Created by lendingkart on 2/27/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GithubRepo implements Parcelable {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("incomplete_results")
    @Expose
    private Boolean incompleteResults;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    protected GithubRepo(Parcel in) {
    }

    public static final Creator<GithubRepo> CREATOR = new Creator<GithubRepo>() {
        @Override
        public GithubRepo createFromParcel(Parcel in) {
            return new GithubRepo(in);
        }

        @Override
        public GithubRepo[] newArray(int size) {
            return new GithubRepo[size];
        }
    };

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }


    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }


    public static class Item implements Parcelable {

        @SerializedName("full_name")
        @Expose
        private String fullName;
        @SerializedName("html_url")
        @Expose
        private String htmlUrl;

        protected Item(Parcel in) {
            if (in != null) {
                fullName = in.readString();
                htmlUrl = in.readString();
            }
        }

        public final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(fullName);
            dest.writeString(htmlUrl);
        }
    }


}