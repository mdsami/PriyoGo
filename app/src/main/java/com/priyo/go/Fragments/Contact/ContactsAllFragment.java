package com.priyo.go.Fragments.Contact;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.priyo.go.CustomView.ProfileImageView;
import com.priyo.go.DatabaseHelper.DBConstants;
import com.priyo.go.DatabaseHelper.DataHelper;
import com.priyo.go.DatabaseHelper.SQLiteCursorLoader;
import com.priyo.go.R;
import com.priyo.go.Utilities.ApiConstants;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.view.activity.people.PeopleActivity;

import java.util.List;


public class ContactsAllFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SearchView.OnQueryTextListener {

    private static final int CONTACTS_QUERY_LOADER = 0;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mEmptyContactsTextView;
    private String mQuery = "";

    private ContactListAdapter mAdapter;
    private Cursor mCursor;

    private boolean mShowVerifiedUsersOnly;
    private boolean miPayMembersOnly;
    private boolean mBusinessMemberOnly;
    private boolean mShowInvitedOnly;
    private boolean mShowNonInvitedNonMembersOnly;

    private int nameIndex;
    private int phoneNumberIndex;
    private int isMemberIndex;

    private ContactLoadFinishListener contactLoadFinishListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isDialogFragment())
            setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mShowVerifiedUsersOnly = getArguments().getBoolean(Constants.VERIFIED_USERS_ONLY, true);
            miPayMembersOnly = getArguments().getBoolean(Constants.IPAY_MEMBERS_ONLY, true);
            mBusinessMemberOnly = getArguments().getBoolean(Constants.BUSINESS_ACCOUNTS_ONLY, true);
            mShowInvitedOnly = getArguments().getBoolean(Constants.SHOW_INVITED_ONLY, true);
            mShowNonInvitedNonMembersOnly = getArguments().getBoolean(Constants.SHOW_NON_INVITED_NON_MEMBERS_ONLY, true);
        }

        getLoaderManager().initLoader(CONTACTS_QUERY_LOADER, null, this).forceLoad();

        mEmptyContactsTextView = (TextView) v.findViewById(R.id.contact_list_empty);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) v.findViewById(R.id.contact_list);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ContactListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onDestroyView() {
        getLoaderManager().destroyLoader(CONTACTS_QUERY_LOADER);
        mCursor = null;
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search_contacts);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(this);
        searchViewAndroidActionBar.clearFocus();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new SQLiteCursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                DataHelper dataHelper = DataHelper.getInstance(getActivity());
                List<String> invitees = null;

                Cursor cursor = dataHelper.searchFriends(mQuery, miPayMembersOnly, mBusinessMemberOnly, mShowNonInvitedNonMembersOnly,
                        mShowVerifiedUsersOnly, mShowInvitedOnly, mShowNonInvitedNonMembersOnly, invitees);

                if (cursor != null) {
                    nameIndex = cursor.getColumnIndex(DBConstants.KEY_NAME);
                    phoneNumberIndex = cursor.getColumnIndex(DBConstants.KEY_MOBILE_NUMBER);
                    isMemberIndex = cursor.getColumnIndex(DBConstants.KEY_IS_MEMBER);

                    if (contactLoadFinishListener != null) {
                        contactLoadFinishListener.onContactLoadFinish(cursor.getCount());
                    }

                    this.registerContentObserver(cursor, DBConstants.DB_TABLE_FRIENDS_URI);
                }

                return cursor;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        populateList(data, getString(R.string.no_contacts));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    boolean isDialogFragment() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("fragmentActionType", ApiConstants.Parameter.QUERY_STRING_PARAMETER);
        bundle.putString(ApiConstants.Parameter.QUERY_STRING_PARAMETER, query);
        bundle.putString("Title", "Search");

        Intent intent = new Intent(getContext(), PeopleActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("searchBundle", bundle);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mQuery = newText;
        getLoaderManager().restartLoader(CONTACTS_QUERY_LOADER, null, this);

        return true;
    }

    private void populateList(Cursor cursor, String emptyText) {
        this.mCursor = cursor;

        if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
            mAdapter = new ContactListAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mEmptyContactsTextView.setVisibility(View.GONE);
        } else {
            mEmptyContactsTextView.setText(emptyText);
            mEmptyContactsTextView.setVisibility(View.VISIBLE);
        }
    }

    public interface ContactLoadFinishListener {
        void onContactLoadFinish(int contactCount);
    }

    public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int EMPTY_VIEW = 10;
        private static final int FRIEND_VIEW = 100;

        @SuppressWarnings("UnnecessaryLocalVariable")
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v;

            if (viewType == EMPTY_VIEW) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_empty_description, parent, false);
                return new EmptyViewHolder(v);
            } else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact_1, parent, false);
                return new ViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            try {

                if (holder instanceof ViewHolder) {
                    ViewHolder vh = (ViewHolder) holder;
                    vh.bindView(position);
                } else if (holder instanceof EmptyViewHolder) {
                    EmptyViewHolder vh = (EmptyViewHolder) holder;
                    vh.mEmptyDescription.setText(getString(R.string.no_contacts));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            if (mCursor == null || mCursor.isClosed())
                return 0;
            else
                return mCursor.getCount();
        }

        @Override
        public int getItemViewType(int position) {
            if (getItemCount() == 0)
                return EMPTY_VIEW;
            else
                return FRIEND_VIEW;
        }

        public class EmptyViewHolder extends RecyclerView.ViewHolder {
            public final TextView mEmptyDescription;

            public EmptyViewHolder(View itemView) {
                super(itemView);
                mEmptyDescription = (TextView) itemView.findViewById(R.id.empty_description);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final View itemView;

            private final TextView name1View;
            private final ProfileImageView profilePictureView;
            private final TextView mobileNumberView;
            private final ImageView isSubscriber;

            public ViewHolder(View itemView) {
                super(itemView);

                this.itemView = itemView;

                name1View = (TextView) itemView.findViewById(R.id.name1);
                mobileNumberView = (TextView) itemView.findViewById(R.id.mobile_number);
                profilePictureView = (ProfileImageView) itemView.findViewById(R.id.profile_picture_image_view);
                isSubscriber = (ImageView) itemView.findViewById(R.id.is_member);
            }

            public void bindView(int pos) {

                mCursor.moveToPosition(pos);

                final String name = mCursor.getString(nameIndex);
                final String mobileNumber = mCursor.getString(phoneNumberIndex);
                final boolean isMember = mCursor.getInt(isMemberIndex) == DBConstants.IPAY_MEMBER;

                name1View.setText(name);

                mobileNumberView.setText(mobileNumber);
                if (isMember) {
                    isSubscriber.setVisibility(View.VISIBLE);
                } else {
                    isSubscriber.setVisibility(View.GONE);
                }
            }

        }
    }
}