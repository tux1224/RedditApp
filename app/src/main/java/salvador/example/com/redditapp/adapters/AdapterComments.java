package salvador.example.com.redditapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import salvador.example.com.redditapp.CommentsListener;
import salvador.example.com.redditapp.R;
import salvador.example.com.redditapp.model.Comments;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.ViewHolder> implements View.OnClickListener {

    private CommentsListener listener;
    private Context context;
    private ArrayList<Comments> mComments;
    private String TAG = AdapterComments.class.getSimpleName();

    public void setListener(CommentsListener listener){
        this.listener = listener;
    }
    public void setData(ArrayList<Comments> comments){
        if (comments == null){
            return;
        }

        mComments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(viewType,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        if(mComments.get(position).getMoreComments().size()>0){
            holder.view.setTag(R.id.flow,"down");
            holder.view.setTag(R.id.position, position);
            holder.date.setText(mComments.get(position).getCreated_utc());
            holder.user.setText(mComments.get(position).getAuthor());
            holder.score.setText(mComments.get(position).getScore());
            holder.body.setText(mComments.get(position).getBody());
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.score.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.user.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.body.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.date.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.view.setOnClickListener(this);
        }else{
            holder.view.setTag(R.id.flow,"up");
            holder.view.setTag(R.id.position, position);
            holder.date.setText(mComments.get(position).getCreated_utc());
            holder.user.setText(mComments.get(position).getAuthor());
            holder.score.setText(mComments.get(position).getScore());
            holder.body.setText(mComments.get(position).getBody());
            holder.view.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.score.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.user.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.body.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.date.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.view.setOnClickListener(this);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_comments;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return (mComments == null) ? 0 : mComments.size();
    }

    @Override
    public void onClick(View v) {
        String flow = (String) v.getTag(R.id.flow);
        switch (flow){

            case "up":
                listener.onUp(v);
                break;
            case "down":
                listener.onClick(v);

                break;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView user, date, score, body;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view    = (View)     itemView;
            user    = (TextView) itemView.findViewById(R.id.user);
            score   = (TextView) itemView.findViewById(R.id.score_comments);
            date    = (TextView) itemView.findViewById(R.id.date);
            body    = (TextView) itemView.findViewById(R.id.body);


        }


    }
}
