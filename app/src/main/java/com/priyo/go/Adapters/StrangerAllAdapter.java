package com.priyo.go.Adapters;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.Model.Friend.StrangerNode;
import com.priyo.go.R;

public class StrangerAllAdapter extends RecyclerView.Adapter<StrangerAllAdapter.ViewHolder> {
    private List<StrangerNode> people;
    private Context context;

    public StrangerAllAdapter(Context context, List<StrangerNode> people) {
        this.people = people;
        this.context = context;
    }

    @Override
    public StrangerAllAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_contact, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StrangerAllAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tv_android.setText(people.get(i).getName());
        viewHolder.tv_mobile.setText(people.get(i).getPhoneNumber());
        viewHolder.img_android.setProfilePicture(people.get(i).getProfilePictureUrl(),true);

        viewHolder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createContact(people.get(i).getName(), people.get(i).getPhoneNumber());
                people.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, people.size());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_android;
        private TextView tv_mobile;
        private ProfileImageView img_android;
        private ImageView img_add;
        public ViewHolder(View view) {
            super(view);

            tv_android = (TextView)view.findViewById(R.id.title);
            tv_mobile = (TextView)view.findViewById(R.id.mobile_number);
            img_android = (ProfileImageView) view.findViewById(R.id.thumbnail);
            img_add = (ImageView) view.findViewById(R.id.overflow);


        }
    }


    private void createContact(String name, String phone) {
        ContentResolver cr = context.getContentResolver();
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "accountname@gmail.com")
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "com.google")
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());


        try {
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Toast.makeText(context, "Created a new contact with name: " + name + " and Phone No: " + phone, Toast.LENGTH_SHORT).show();

    }

}
