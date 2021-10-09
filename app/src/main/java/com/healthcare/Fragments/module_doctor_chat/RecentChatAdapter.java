package com.healthcare.Fragments.module_doctor_chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.Activities.MainActivity;
import com.healthcare.R;

import java.util.ArrayList;


public class RecentChatAdapter extends RecyclerView.Adapter<RecentChatAdapter.MyHolder> {

    ArrayList<RecentChatInfo> chatInfo;
    Context context;
    public RecentChatAdapter(Context context,ArrayList<RecentChatInfo> chatInfo){
        this.chatInfo=chatInfo;
        this.context=context;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.recent_chat_adapter,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final RecentChatInfo recentChatInfo=chatInfo.get(position);
        holder.tvUsername.setText(recentChatInfo.getUserName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, ChatRoom.newInstance(recentChatInfo.getUserName(),recentChatInfo.getPhone()))
                        .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatInfo.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvUsername,tvLastChat;
        public MyHolder(View itemView) {
            super(itemView);
            tvUsername=(TextView)itemView.findViewById(R.id.tvUser);

        }
    }
}
