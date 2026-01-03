package com.example.authentication;

import android.content.Context;
import android.content.Intent;
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

        buttonAccept = holder.itemView.findViewById(R.id.user_accept);
        buttonReject = holder.itemView.findViewById(R.id.user_reject);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AdminButtonActionActivity buttonActionActivity = new AdminButtonActionActivity();
                buttonActionActivity.AcceptRequest(user);

                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required if context = application context
                context.startActivity(intent); // TEMPORARY

                Toast.makeText(context, "Sent A mail", Toast.LENGTH_SHORT).show();

                // REMOVING THE ITEM
                removeItem(holder.getAbsoluteAdapterPosition());

            }
        });

        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Rejected", Toast.LENGTH_SHORT).show();

                // REMOVE THE REJECTED USER FROM THE FIREBASE  //***TESTING LEFT***
                int pos = holder.getAbsoluteAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    User userToRemove = userArrayList.get(pos);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students").child(userToRemove.getName());
                    reference.removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            Toast.makeText(context, "Completely Removed!!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, "Wasn't able to remove!!", Toast.LENGTH_SHORT).show();
                    });
                }

                // REMOVING THE ITEM // ***TESTING LEFT***
                removeItem(holder.getAbsoluteAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MoreDetailActivity.class);
                intent.putExtra("detailName",user.getName());
                intent.putExtra("detailEmail",user.getEmail());
                intent.putExtra("detailContactNo",user.getContactNo());
                intent.putExtra("detailParentContactNo",user.getParentContactNo());
                intent.putExtra("detailJoinYear",user.getJoinYear());

                context.startActivity(intent);
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

    // *** CREATE A NEW CLASS *** HAVING METHOD OF BUTTONS SO IT CAN BE EXTENDED IN MORE DETAIL ACTIVITY
}

