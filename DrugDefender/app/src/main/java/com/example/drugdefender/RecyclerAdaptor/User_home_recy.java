package com.example.drugdefender.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drugdefender.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User_home_recy extends RecyclerView.Adapter<User_home_recy.MyViewHolder> {
    private static final String TAG = "RecyclerNews";
    private final Context context;
    private final JSONArray array;
    private static final String fsts ="0";
    Activity act;



    public User_home_recy(Context applicationContext, JSONArray jsonArray, Activity a) {
        this.context = applicationContext;
        this.array = jsonArray;
        this.act = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_post_list, null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            JSONObject jsonObject = array.getJSONObject(position);

            String strimg=jsonObject.getString("encoded_image");
            byte[] decodedString = Base64.decode(strimg, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.iv1.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 398, 250, false));

            holder.tv1.setText(jsonObject.getString("p_id"));
            holder.tv2.setText(jsonObject.getString("username"));
            holder.tv3.setText(jsonObject.getString("time"));
            holder.tv4.setText(jsonObject.getString("date"));
            holder.tv5.setText(jsonObject.getString("p_text"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv1,tv2,tv3,tv4,tv5;
        ImageView iv1;
        CardView cv;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.post_id);
            tv2 = (TextView) itemView.findViewById(R.id.username);
            tv3 = (TextView) itemView.findViewById(R.id.time);
            tv4 = (TextView) itemView.findViewById(R.id.date);
            tv5 = (TextView) itemView.findViewById(R.id.caption);
            iv1 = (ImageView) itemView.findViewById(R.id.image);

            cv=(CardView)itemView.findViewById(R.id.card_view) ;

        }
    }

}

