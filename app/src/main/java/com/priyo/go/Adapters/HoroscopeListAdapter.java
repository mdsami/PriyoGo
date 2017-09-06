package com.priyo.go.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.priyo.go.Model.HoroscopeNode;
import com.priyo.go.R;

import java.util.List;

public class HoroscopeListAdapter extends RecyclerView.Adapter<HoroscopeListAdapter.ViewHolder> {
    private static String []horName = {"Aquarius","Aries","Cancer","Capricorn","Gemini","Leo","Libra","Pisces","Sagittarius","Scorpio","Taurus","Virgo"};
    private static String []horNameBang = {"কুম্ভ","মেষ","কর্কট"," মকর"," মিথুন","সিংহ ","তুলা","মীন","ধনু","বৃশ্চিক","বৃষ","কন্যা"};
    private static int[] horImage = {R.mipmap.aquarius, R.mipmap.aries, R.mipmap.cancer, R.mipmap.capricorn, R.mipmap.gemini, R.mipmap.leo, R.mipmap.libra, R.mipmap.pisces, R.mipmap.sagittarius, R.mipmap.scorpius, R.mipmap.taurus, R.mipmap.virgo};
    private List<HoroscopeNode> people;
    private Context context;


    public HoroscopeListAdapter(Context context, List<HoroscopeNode> people) {
        this.people = people;
        this.context = context;
    }

    @Override
    public HoroscopeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_horoscope, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HoroscopeListAdapter.ViewHolder viewHolder, final int i) {
        for(int in=0;in<horName.length;in++){
            if(horName[in].equalsIgnoreCase(people.get(i).getKeyName())){
                viewHolder.tv_title.setText(horNameBang[in] +" | "+horName[in]);
                viewHolder.tv_detail.setText(people.get(i).getHoroscopeName());
                viewHolder.tv_thumb.setImageResource(horImage[in]);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title;
        private TextView tv_detail;
        private ImageView tv_thumb;

        public ViewHolder(View view) {
            super(view);

            tv_title = (TextView)view.findViewById(R.id.news_title);
            tv_detail = (TextView)view.findViewById(R.id.news_detail);
            tv_thumb = (ImageView)view.findViewById(R.id.thumbnail);
        }
    }
}
