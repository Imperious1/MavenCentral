package imperiumnet.gradleplease.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import imperiumnet.gradleplease.R;
import imperiumnet.gradleplease.models.MCModel;

/**
 * Created by overlord on 4/18/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private int itemRange;
    private ArrayList<MCModel> list = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mPrimaryText.setText(String.format("'%s%s'", list.get(position).getLibrary(), list.get(position).getLatestVersion()));
            if (holder.mPrimaryText.getText().toString().length() > 50)
                holder.mPrimaryText.setTextSize(14);
            else holder.mPrimaryText.setTextSize(16);
            holder.mTimestamp.setText(list.get(position).getTimestamp());
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.clearFocus();
                }
            });
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.clearFocus();
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // General display things
    public void update(@Nullable ArrayList<MCModel> list, @Nullable MCModel model) {
        if(list != null) {
            itemRange = this.list.size();
            this.list.clear();
            notifyItemRangeRemoved(0, itemRange);
            for (MCModel e : list) {
                this.list.add(e);
                itemRange = this.list.size();
                notifyItemInserted(getItemCount());
            }
        } else {
            itemRange = this.list.size();
            this.list.clear();
            notifyItemRangeRemoved(0, itemRange);
            this.list.add(model);
            notifyItemInserted(0);
        }
    }

    public void updateCount(MCModel model) {
        list.add(model);
        notifyItemInserted(list.size()-1);
    }

    // Clear when nothing inputted or exit pressed
    public void clearAll() {
        itemRange = list.size();
        list.clear();
        notifyItemRangeRemoved(0, itemRange);
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView mPrimaryText;
    TextView mTimestamp;
    CardView card;
    RelativeLayout layout;

    public MyViewHolder(View itemView) {
        super(itemView);
        layout = (RelativeLayout) itemView.findViewById(R.id.relative_card);
        card = (CardView) itemView.findViewById(R.id.cardView);
        mPrimaryText = (TextView) itemView.findViewById(R.id.primary_text);
        mTimestamp = (TextView) itemView.findViewById(R.id.timestamp);
    }
}
