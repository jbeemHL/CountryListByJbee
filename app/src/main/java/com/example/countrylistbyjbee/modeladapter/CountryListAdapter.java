package com.example.countrylistbyjbee.modeladapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.countrylistbyjbee.R;
import com.example.countrylistbyjbee.activities.CountryViewActivity;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private ArrayList<CountryList> countryLists;
    private Context context;

    public CountryListAdapter(Context context, ArrayList<CountryList> countryLists) {
        this.context = context;
        this.countryLists = countryLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_country, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CountryList country = countryLists.get(position);

        holder.tvCountryName.setText(country.getCountryName());
        GlideToVectorYou
                .init()
                .with(context)
                .withListener(new GlideToVectorYouListener() {
                    @Override
                    public void onLoadFailed() {
                        //Toast.makeText(context, "Load failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResourceReady() {
                        //Toast.makeText(context, "Image ready", Toast.LENGTH_SHORT).show();
                    }
                })
                .load(Uri.parse(country.getFlag()), holder.imgFlag);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CountryViewActivity.class);
                intent.putExtra("countryName", country.getCountryName());
                intent.putExtra("capital", country.getCapital());
                intent.putExtra("region", country.getRegion());
                intent.putExtra("abbv", country.getAbbv());
                intent.putExtra("callingCodes", country.getCallingCodes());
                intent.putExtra("population", country.getPopulation());
                intent.putExtra("currencies", country.getCurrencies());
                intent.putExtra("latlng", country.getLnglat());
                intent.putExtra("languages", country.getLanguages());
                intent.putExtra("borders", country.getBorders());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountryName;
        ImageView imgFlag;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCountryName = (TextView) itemView.findViewById(R.id.tv_countryname);
            imgFlag = (ImageView) itemView.findViewById(R.id.img_flag);
        }
    }
}