package com.priyo.go.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.Model.node.Business;
import com.priyo.go.R;
import com.priyo.go.view.activity.business.BusinessDetailsActivity;

import java.util.List;

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.ViewHolder> {
    private List<Business> people;
    private Context context;

    public BusinessListAdapter(Context context, List<Business> people) {
        this.people = people;
        this.context = context;
    }

    @Override
    public BusinessListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_business_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessListAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tv_android.setText(people.get(i).getNodeTitle());
        viewHolder.tv_address.setText(people.get(i).getNodeTag());
        viewHolder.tv_pic.setProfilePicture(people.get(i).getNodePhoto(), false);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToDetailsActivity(people.get(i).getNodeID().toString());
            }
        });
        viewHolder.tv_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToDetailsActivity(people.get(i).getNodeID().toString());
            }
        });

        viewHolder.tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToDetailsActivity(people.get(i).getNodeID().toString());
            }
        });

        viewHolder.tv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToDetailsActivity(people.get(i).getNodeID().toString());
            }
        });

        viewHolder.tv_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToDetailsActivity(people.get(i).getNodeID().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    private void switchToDetailsActivity(String nodeid) {
        Intent intent = new Intent(context, BusinessDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Id", nodeid);
        intent.putExtra("Node", "Business");
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_android;
        private TextView tv_address;
        private ProfileImageView tv_pic;
        private RelativeLayout tv_root;

        public ViewHolder(View view) {
            super(view);

            tv_android = (TextView) view.findViewById(R.id.catregory_name);
            tv_address = (TextView) view.findViewById(R.id.address);
            tv_pic = (ProfileImageView) view.findViewById(R.id.profile_picture_image_view);
            tv_pic.setPlaceHolder(R.mipmap.buld);
            tv_root = (RelativeLayout) view.findViewById(R.id.root_touch);
        }
    }

}
