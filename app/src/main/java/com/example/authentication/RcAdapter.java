package com.example.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.example.authentication.AdminButtonActionActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RcAdapter extends RecyclerView.Adapter<RcAdapter.RCViewHolder>{

    Context context;
    ArrayList<User> userArrayList;

    private Button buttonAccept, buttonReject;

    public RcAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_view, parent, false);
        return new RCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        User user = userArrayList.get(position);
        holder.rc_name.setText(user.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MoreDetailActivity.class);
                intent.putExtra("userClass", user);
                intent.putExtra("position", holder.getAdapterPosition());

                ((Activity) context).startActivityForResult(intent, 101);
            }
        });

    }
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    public class RCViewHolder extends RecyclerView.ViewHolder {

        TextView rc_name;

        public RCViewHolder(@NonNull View itemView) {
            super(itemView);

            rc_name = itemView.findViewById(R.id.RC_name);

        }
    }

    public void removeItem(int position) {
        if (position >= 0 && position < userArrayList.size()) {
            userArrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, userArrayList.size()); // optional, keeps indexes correct
        }
    }

}

