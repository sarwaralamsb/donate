package com.sarwar.app.donate;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PostViewAdapter extends ArrayAdapter <DonationPost> {
    private Activity context;
    private int resource;
    private List<DonationPost> UserposthistoryList;

    public PostViewAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<DonationPost> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        UserposthistoryList = objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView Name=(TextView)v.findViewById(R.id.postnametv) ;
        TextView Time=(TextView) v.findViewById(R.id.postdatetv);
        TextView Status=(TextView)v.findViewById(R.id.poststatustv);
        TextView Details=(TextView)v.findViewById(R.id.postdetailstv);
        TextView Phone=(TextView)v.findViewById(R.id.postphonetv) ;
        TextView Address=(TextView)v.findViewById(R.id.postaddresstv);
        ImageView Pimage=(ImageView)v.findViewById(R.id.postimage);
        TextView unique_key=(TextView)v.findViewById(R.id.keytv);

        if(UserposthistoryList.get(position).getUrl()==null){
           // Pimage.setVisibility(View.INVISIBLE);
            Pimage.getLayoutParams().height=0;
            Pimage.getLayoutParams().width=0;
        }

        Name.setText(UserposthistoryList.get(position).getName());
        Time.setText(UserposthistoryList.get(position).getCuurent_time());
        Status.setText(UserposthistoryList.get(position).getStatus());
        Details.setText(UserposthistoryList.get(position).getDetails());
        Phone.setText(UserposthistoryList.get(position).getPhone());
        Address.setText(UserposthistoryList.get(position).getAddress());
        unique_key.setText(UserposthistoryList.get(position).getKey());
        Glide.with(context).load(UserposthistoryList.get(position).getUrl()).into(Pimage);


        return v;

    }

}
