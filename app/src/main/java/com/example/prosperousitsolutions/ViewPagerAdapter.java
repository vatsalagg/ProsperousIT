package com.example.prosperousitsolutions;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>{
    private List<IntroModel> introModelList;
    Context context;

    public ViewPagerAdapter(Context context,List<IntroModel> introModelList) {
        this.context= context;
        this.introModelList = introModelList;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.container_intro_view_pager,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
        String title =introModelList.get(position).getHeading();
        String subtitle =introModelList.get(position).getSubheading();
        String background =introModelList.get(position).getBackgroundColor();
        holder.setData(title,subtitle,background);
    }

    @Override
    public int getItemCount() {
        return introModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, subtitle;
        private ConstraintLayout backgroundColor;

        public ViewHolder(@NonNull final View view) {
            super(view);
            title = view.findViewById(R.id.introHeading);
            subtitle = view.findViewById(R.id.introSubHeading);
            backgroundColor= view.findViewById(R.id.containerBackground);

        }

        private void setData(String heading, String subheading,String background) {
            title.setText(heading);
            subtitle.setText(subheading);
            backgroundColor.setBackgroundColor(Color.parseColor(background));
        }
    }
}
