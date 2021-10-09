package com.healthcare.Fragments.module_visitScheduler;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.healthcare.R;

import java.util.ArrayList;

/**
 * Created by Ashish on 31-Jan-17.
 */
public class FragDocVisit extends Fragment
{
    RecyclerView recyclerView;
    Button button;
    ArrayList<VisitInfo> arrayList;
    RecycleVisitAdapter adapter;
    MyDBHandler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.doc_visit_frag,container,false);
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Checkup Schedule");

        recyclerView=(RecyclerView)view.findViewById(R.id.rv);
        handler=new MyDBHandler(getContext(),null,null,1);
        arrayList=handler.display();

        for(int i=0;i<arrayList.size();i++)
        {
            System.out.println(arrayList.get(i).ename+"\n");
        }

        adapter=new RecycleVisitAdapter(arrayList,getContext());
            recyclerView.swapAdapter(adapter,true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        button=(Button)view.findViewById(R.id.addb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFrag fragment2 = new AddFrag();
                FragmentManager fragmentManager =getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container,fragment2,"Two");
                fragmentTransaction.commit();

            }
        });
        return view;
    }


}
