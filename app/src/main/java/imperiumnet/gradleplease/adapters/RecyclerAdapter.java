package imperiumnet.gradleplease.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.callbacks.Listeners;
import imperiumnet.gradleplease.models.MCModel;
import imperiumnet.gradleplease.models.QueryModel;
import imperiumnet.gradleplease.singleton.Singleton;
import io.realm.Sort;

public abstract class RecyclerAdapter extends RecyclerView.Adapter implements Listeners.SwipeListener {
    private static ArrayList<MCModel> mSearchList = new ArrayList<>();
    private static int mItemPosition;
    private static int mItemRange;
    private boolean isSearch;
    private Context mContext;

    public RecyclerAdapter(boolean isSearch, Context context) {
        this.isSearch = isSearch;
        this.mContext = context;
    }

    public static int getItemPos() {
        return mItemPosition;
    }

    public static ArrayList<MCModel> getSearchList() {
        return mSearchList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isSearch)
            return new SearchHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.recycler_row, parent, false));
        else
            return new HistoryHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.recycler_history_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isSearch) {
            ((SearchHolder) holder).mPrimaryText.setText(String.format("'%s:%s'", mSearchList.get(position).getLibrary(), mSearchList.get(position).getLatestVersion()));
            if (((SearchHolder) holder).mPrimaryText.getText().toString().length() > 50)
                ((SearchHolder) holder).mPrimaryText.setTextSize(14);
            else ((SearchHolder) holder).mPrimaryText.setTextSize(16);
            ((SearchHolder) holder).mTimestamp.setText(mSearchList.get(position).getTimestamp());

        } else {
            ((HistoryHolder) holder).mResult.setText(Singleton.getRealm().allObjectsSorted(QueryModel.class, "mCreationTime", Sort.DESCENDING).get(position).getmFirstResult());
            ((HistoryHolder) holder).mQuery.setText(Singleton.getRealm().allObjectsSorted(QueryModel.class, "mCreationTime", Sort.DESCENDING).get(position).getmQuery());
        }
    }

    @Override
    public int getItemCount() {
        if (isSearch)
            return mSearchList.size();
        else {
            return Singleton.getRealm().allObjects(QueryModel.class).size();
        }
    }

    // General display things
    public void update(@Nullable ArrayList<MCModel> list, @Nullable MCModel model) {
        if (list != null) {
            mItemRange = RecyclerAdapter.mSearchList.size();
            RecyclerAdapter.mSearchList.clear();
            notifyItemRangeRemoved(0, mItemRange);
            for (MCModel e : list) {
                RecyclerAdapter.mSearchList.add(e);
                mItemRange = RecyclerAdapter.mSearchList.size();
                notifyItemInserted(getItemCount());
            }
        } else {
            mItemRange = RecyclerAdapter.mSearchList.size();
            RecyclerAdapter.mSearchList.clear();
            notifyItemRangeRemoved(0, mItemRange);
            RecyclerAdapter.mSearchList.add(model);
            notifyItemInserted(0);
        }
    }

    public void clearAll() {
        mItemRange = mSearchList.size();
        mSearchList.clear();
        notifyItemRangeRemoved(0, mItemRange);
    }

    public abstract void onClick1(View view, int position);

    class SearchHolder extends RecyclerView.ViewHolder {

        TextView mPrimaryText;
        TextView mTimestamp;
        CardView card;

        public SearchHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cardView);
            mPrimaryText = (TextView) itemView.findViewById(R.id.primary_text);
            mTimestamp = (TextView) itemView.findViewById(R.id.timestamp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick1(v, getAdapterPosition());
                    mItemPosition = getAdapterPosition();
                }
            });
        }
    }

    class HistoryHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        TextView mQuery;
        TextView mResult;

        public HistoryHolder(View itemView) {
            super(itemView);
            mResult = (TextView) itemView.findViewById(R.id.text_result);
            mQuery = (TextView) itemView.findViewById(R.id.text_query);
            mCardView = (CardView) itemView.findViewById(R.id.card_history);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick1(v, getAdapterPosition());
                    mItemPosition = getAdapterPosition();
                }
            });
        }
    }

}