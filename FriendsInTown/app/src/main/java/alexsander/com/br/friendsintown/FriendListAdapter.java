package alexsander.com.br.friendsintown;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 212571132 on 7/14/16.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendViewHolder>{
    private List<Friend> friends;
    private Context context;
    private OnFriendClickListener onFriendClickListener;


    public FriendListAdapter(List<Friend> friends, Context context, OnFriendClickListener onFriendClickListener) {
        this.friends = friends;
        this.context = context;
        this.onFriendClickListener = onFriendClickListener;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_info, parent, false);
        FriendViewHolder holder = new FriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FriendViewHolder holder, final int position) {
        Friend friend = friends.get(position);

        holder.tvFriendName.setText(friend.getName());
        if (friend.getLocation() != "") {
            holder.tvFriendLocation.setText(friend.getLocation());
        }
        if (friend.getDistance() > 0) {
            if (friend.getDistance() > 1000) {
                holder.tvFriendDistance.setText(String.format("%.2f km", friend.getDistance()/1000));
            } else {
                holder.tvFriendDistance.setText(friend.getDistance() + "m");
            }
        }

        if (onFriendClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFriendClickListener.onFriendClick(holder.itemView, position);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }

    public interface OnFriendClickListener {
        void onFriendClick(View view, int idx);
    }
}
