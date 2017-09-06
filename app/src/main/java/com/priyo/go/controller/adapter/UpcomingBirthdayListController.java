package com.priyo.go.controller.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.priyo.go.Model.Friend.BirthdayNode;
import com.priyo.go.R;
import com.priyo.go.Utilities.Utilities;
import com.priyo.go.view.activity.BirthdaysActivity;
import com.priyo.go.view.adapter.holder.BirthdayListItemViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sajid.shahriar on 5/1/17.
 */

public class UpcomingBirthdayListController implements IRecyclerViewAdapterController<BirthdayListItemViewHolder, BirthdayNode>, View.OnClickListener {

    private Activity activity;
    private List<BirthdayNode> birthdayNodeList;
    private LayoutInflater layoutInflater;
    private SimpleDateFormat birthDateFormat;
    private SimpleDateFormat monthFormat;
    private Event birthDayEvent;
    private int selectedItemPosition = -1;
    private boolean hasCalendarReadPermission = false;


    public UpcomingBirthdayListController(Activity activity, List<BirthdayNode> birthdayNodeList) {
        this.activity = activity;
        this.birthdayNodeList = birthdayNodeList;
        layoutInflater = activity.getLayoutInflater();
        birthDateFormat = new SimpleDateFormat("MMMM dd", Locale.US);
        monthFormat = new SimpleDateFormat("MMM", Locale.US);
    }

    @Override
    public BirthdayListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BirthdayListItemViewHolder(layoutInflater.inflate(R.layout.item_birthday, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(BirthdayListItemViewHolder birthdayListItemViewHolder, int i) {
        BirthdayNode birthdayNode = getItem(i);
        if (!TextUtils.isEmpty(birthdayNode.getName()))
            birthdayListItemViewHolder.profileNameTextView.setText(birthdayNode.getName());
        if (!TextUtils.isEmpty(birthdayNode.getPhoto()))
            birthdayListItemViewHolder.profilePictureImageView.setProfilePicture(birthdayNode.getPhoto(), false);
        else
            birthdayListItemViewHolder.profilePictureImageView.setProfilePicture("", false);
        if (!TextUtils.isEmpty(birthdayNode.getDateOfBirth()))
            birthdayListItemViewHolder.birthdayDateTextView.setText(birthdayNode.getDateOfBirth());
        birthdayListItemViewHolder.birthdayAlarmButton.setTag(i);
        birthdayListItemViewHolder.birthdayAlarmButton.setOnClickListener(this);
        birthdayListItemViewHolder.birthdayAlarmButton.getParent().requestDisallowInterceptTouchEvent(false);

        if (i != 0) {
            BirthdayNode previousBirthdayNode = getItem(i - 1);
            String[] monthPrev = previousBirthdayNode.getDateOfBirth().split(" ");
            String[] month = birthdayNode.getDateOfBirth().split(" ");
            birthdayListItemViewHolder.monthNameTextView.setText(month[0]);

            if (!monthPrev[0].equals(month[0])) {

                birthdayListItemViewHolder.monthNameTextView.setVisibility(View.VISIBLE);
            } else {
                birthdayListItemViewHolder.monthNameTextView.setVisibility(View.INVISIBLE);
            }

        } else {
            String[] month = birthdayNode.getDateOfBirth().split(" ");
            birthdayListItemViewHolder.monthNameTextView.setText(month[0]);
            birthdayListItemViewHolder.monthNameTextView.setVisibility(View.VISIBLE);
        }

        if (hasCalendarReadPermission) {
            final String eventTitle = birthdayNode.getName() + "'s Birthday";
            birthdayListItemViewHolder.birthdayAlarmButton.setImageResource(
                    !Utilities.hasBirthdayEvent(activity, eventTitle) ?
                            R.mipmap.birthday_alarm :
                            R.mipmap.birthday_select);
        }
    }

    @Override
    public int getItemCount() {
        if (birthdayNodeList != null) {
            return birthdayNodeList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void updateItemList(List<BirthdayNode> birthdayNodeList) {
        this.birthdayNodeList.clear();
        this.birthdayNodeList.addAll(birthdayNodeList);
    }

    @Override
    public void addItem(BirthdayNode birthdayNode) {
        addAllItem(Arrays.asList(birthdayNode));
    }

    @Override
    public BirthdayNode getItem(int position) {
        return this.birthdayNodeList.get(position);
    }

    @Override
    public void addAllItem(List<BirthdayNode> birthdayNodeList) {
        this.birthdayNodeList.addAll(birthdayNodeList);
    }

    @Override
    public void clear() {
        this.birthdayNodeList.clear();
    }

    @Override
    public void removeItem(BirthdayNode birthdayNode) {
        this.birthdayNodeList.remove(birthdayNode);
    }

    @Override
    public void removeAt(int position) throws IndexOutOfBoundsException {
        this.birthdayNodeList.remove(position);
    }

    public boolean isHasCalendarReadPermission() {
        return hasCalendarReadPermission;
    }

    public void setHasCalendarReadPermission(boolean hasCalendarReadPermission) {
        this.hasCalendarReadPermission = hasCalendarReadPermission;
    }

    public void attemptAddBirthdayEvent() {
        if (selectedItemPosition != -1) {
            if (ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                createBirthdayEvent();
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALENDAR},
                        BirthdaysActivity.CALENDAR_WRITE_REQUEST_CODE);
            } else {
                if (birthDayEvent == null) {
                    createBirthdayEvent();
                }
                doAddBirthdayEventToCalendar();
            }
        }
    }

    private void createBirthdayEvent() {
        birthDayEvent = new Event();
        birthDayEvent.eventTitle = getItem(selectedItemPosition).getName() + "'s Birthday";
        try {
            birthDayEvent.dateOfBirth = new SimpleDateFormat("MMM dd", Locale.US).parse(getItem(selectedItemPosition).getDateOfBirth());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        birthDayEvent.eventID = getItem(selectedItemPosition).getNodeID();
    }

    private void doAddBirthdayEventToCalendar() {
        selectedItemPosition = -1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        birthDayEvent.dateOfBirth.setYear(year - 1900);

        // Adding event date to calendar
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, birthDayEvent.dateOfBirth.getTime());
        intent.putExtra(CalendarContract.Events._ID, birthDayEvent.eventID);
        intent.putExtra("title", birthDayEvent.eventTitle);
        intent.putExtra("description", birthDayEvent.eventTitle);
        activity.startActivityForResult(intent, BirthdaysActivity.CREATE_BIRTHDAY_EVENT_REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Integer) {
            birthDayEvent = null;
            selectedItemPosition = (int) view.getTag();
            attemptAddBirthdayEvent();
        }
    }

    private class Event {
        String eventTitle;
        int eventID;
        Date dateOfBirth;
    }
}
