package imperiumnet.gradleplease.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.models.MCModel;

public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private static ArrayList<MCModel> mSearchList = new ArrayList<>();
    private static int mItemPosition;
    private static int mItemRange;

    public static int test() {
        return mItemPosition;
    }

    public static ArrayList<MCModel> getmSearchList() {
        return mSearchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mPrimaryText.setText(String.format("'%s:%s'", mSearchList.get(position).getLibrary(), mSearchList.get(position).getLatestVersion()));
        if (holder.mPrimaryText.getText().toString().length() > 50)
            holder.mPrimaryText.setTextSize(14);
        else holder.mPrimaryText.setTextSize(16);
        holder.mTimestamp.setText(mSearchList.get(position).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
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

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mPrimaryText;
        TextView mTimestamp;
        CardView card;

        public MyViewHolder(View itemView) {
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

}
