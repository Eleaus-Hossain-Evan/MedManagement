package com.healthcare.Fragments.module_vaccination;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthcare.Activities.MainActivity;
import com.healthcare.R;
import com.healthcare.handlers.VaccinationDBHandler;

import java.util.ArrayList;

/**
 * Created by guptaanirudh100 on 2/20/2017.
 */

public class VaccineRecyclerAdapter extends RecyclerView.Adapter<VaccineRecyclerAdapter.MyHolder> {

    Context context;
    ArrayList<String> name;
    ArrayList<String> dob;
    VaccinationDBHandler dbHandler;

    public VaccineRecyclerAdapter(Context context, ArrayList<String> name, ArrayList<String> dob) {
        this.context = context;
        this.name = name;
        this.dob = dob;
        dbHandler = new VaccinationDBHandler(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.vaccine_adapter_layout, null);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(name.get(holder.getAdapterPosition()));
        holder.dob.setText(dob.get(holder.getAdapterPosition()));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.moreOptions);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.viewvisit) {
                            ((MainActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack(null)
                                    .replace(R.id.container,AllVaccineReminders.newInstance(name.get(holder.getAdapterPosition())))
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .commit();
                        } else {
                            dbHandler.delChild(name.get(position));
                            name.remove(position);
                            dob.remove(position);
                            notifyDataSetChanged();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name, dob;
        ImageView moreOptions;
        RelativeLayout relativeLayout;

        public MyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvName);
            dob = (TextView) itemView.findViewById(R.id.tvDob);
            moreOptions = (ImageView) itemView.findViewById(R.id.moreOptions);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relLayout);
        }
    }
}
