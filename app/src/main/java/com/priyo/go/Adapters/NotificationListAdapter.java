package com.priyo.go.Adapters;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.priyo.go.Activities.News.NotificationDetailsActivity;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.Model.Friend.PushNode;
import com.priyo.go.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
    private List<PushNode> people;
    private Context context;

    public NotificationListAdapter(Context context, List<PushNode> people) {
        this.people = people;
        this.context = context;
    }

    @Override
    public NotificationListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_notifications, viewGroup, false);


        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final NotificationListAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.tv_android.setText(people.get(i).getTitle());
        viewHolder.tv_msg.setText(people.get(i).getMsg());

        long currentTime = System.currentTimeMillis();
        long update = people.get(i).getUpdatedAt();

        long diff = currentTime - update;
        long diffSeconds = (diff / 1000)%60;
        long diffMinutes = (diff / (60 * 1000));
        long diffHours = (diff / (60 * 60 * 1000))%24;
        long diffDays = (diff / (60 * 60 * 1000 *24))%30;
        long diffMonths = (diff / (60 * 60 * 1000 *24 *30))%12;
        long diffYears = (diff / (60 * 60 * 1000 *24 *30 *12));
        System.out.println("Time in seconds: " + diffSeconds + " seconds.");
        System.out.println("Time in minutes: " + diffMinutes + " minutes.");
        System.out.println("Time in hours: " + diffHours + " hours.");
        System.out.println("Time in days: " + diffDays + " days.");

        if(diffYears!=0)
            viewHolder.tv_time.setText(diffYears + " years ago");
        else if(diffMonths!=0)
            viewHolder.tv_time.setText(diffMonths + " months ago");
        else if(diffDays!=0) {
            if(diffDays>1)
                viewHolder.tv_time.setText(diffDays + " days ago");
            else
                viewHolder.tv_time.setText("yesterday");
        }
        else if(diffHours!=0)
            viewHolder.tv_time.setText(diffHours + " hours ago");
        else if(diffMinutes!=0)
            viewHolder.tv_time.setText(diffMinutes + " minutes ago");
        else if(diffSeconds!=0)
            viewHolder.tv_time.setText(diffSeconds + " seconds ago");


        if(people.get(i).isOpen())
            viewHolder.tv_root.setBackgroundResource(R.color.colorWhite);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = people.get(i).getUrl();
                if(!json.equals("")&& json!=null) {
                    try {
                        //JSONObject jobj = new JSONObject(json);
                        //String url = jobj.getString("url");
//                        String url = json.replace("{url=","");
//                        url = url.replace("}","");


                        if(people.get(i).getType().equals("external")){

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(json));
                            context.startActivity(i);

                        }else {
                            if (json.contains("priyo.com")) {
                                String[] aa = json.split("articles/");
                                System.out.println("News Url " + aa[1]);

                                if (aa.length > 1) {
                                    Intent intent = new Intent(context, NotificationDetailsActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("Slug", aa[1]);
                                    context.startActivity(intent);
                                }
                            }
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                PushNode pp = new PushNode(people.get(i).getId(),people.get(i).getTitle(),people.get(i).getMsg(),people.get(i).getUrl(),people.get(i).getType(),people.get(i).getUpdatedAt(),true);


                DataHelper dataHelper = DataHelper.getInstance(context);
                dataHelper.updatePushEvents(pp);
                viewHolder.tv_root.setBackgroundResource(R.color.colorWhite);




            }
        });

//        viewHolder.img_android.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ProfileActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
//
//        viewHolder.img_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createContact(people.get(i).getName(), people.get(i).getPhoneNumber());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_android;
        private TextView tv_msg;
        private TextView tv_time;
        private LinearLayout tv_root;

        public ViewHolder(View view) {
            super(view);

            tv_android = (TextView)view.findViewById(R.id.catregory_name);
            tv_msg = (TextView)view.findViewById(R.id.catregory_msg);
            tv_time = (TextView)view.findViewById(R.id.catregory_time);
            tv_root = (LinearLayout) view.findViewById(R.id.root);


        }
    }


    private void createContact(String name, String phone) {
        ContentResolver cr = context.getContentResolver();

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

//        if (cur.getCount() > 0) {
//            while (cur.moveToNext()) {
//                String existName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                if (existName.contains(name)) {
//                    Toast.makeText(context,"The contact name: " + name + " already exists", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        }

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
