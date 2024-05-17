package com.example.drugdefender.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
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
import com.example.drugdefender.Admin_view_drug_post;
import com.example.drugdefender.R;
import com.example.drugdefender.View_users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin_drug_recy extends RecyclerView.Adapter<Admin_drug_recy.MyViewHolder>
{
    private static final String TAG = "RecyclerNews";
    private final Context context;
    private final JSONArray array;
    private static final String fsts ="0";
    Activity act;



    public Admin_drug_recy(Context applicationContext, JSONArray jsonArray, Activity a)
    {
        this.context = applicationContext;
        this.array = jsonArray;
        this.act = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_drug_post_list, null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

        try {
            JSONObject jsonObject = array.getJSONObject(position);

            String strimg=jsonObject.getString("encoded_image");
            byte[] decodedString = Base64.decode(strimg, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.iv1.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 398, 250, false));

            holder.tv1.setText(jsonObject.getString("p_id"));
            holder.tv2.setText(jsonObject.getString("p_text"));
            holder.tv3.setText(jsonObject.getString("time"));
            holder.tv4.setText(jsonObject.getString("date"));
            holder.tv5.setText(jsonObject.getString("name"));
            holder.tv6.setText(jsonObject.getString("username"));
            holder.tv7.setText(jsonObject.getString("phone"));
            holder.tv8.setText(jsonObject.getString("email_id"));
            holder.tv9.setText(jsonObject.getString("status"));
            holder.tv10.setText(jsonObject.getString("encoded_image"));
            holder.tv11.setText(jsonObject.getString("p_image"));




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
        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11;
        ImageView iv1;
        Button b1;
        CardView cv;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.p_id);
            tv2 = (TextView) itemView.findViewById(R.id.p_text);
            tv3 = (TextView) itemView.findViewById(R.id.time);
            tv4 = (TextView) itemView.findViewById(R.id.date);
            tv5 = (TextView) itemView.findViewById(R.id.name);
            tv6 = (TextView) itemView.findViewById(R.id.username);
            tv7 = (TextView) itemView.findViewById(R.id.phone);
            tv8 = (TextView) itemView.findViewById(R.id.email_id);
            tv9 = (TextView) itemView.findViewById(R.id.status);
            tv10 = (TextView) itemView.findViewById(R.id.encoded_image);
            tv11 = (TextView) itemView.findViewById(R.id.image);

            iv1 = (ImageView) itemView.findViewById(R.id.p_image);
            cv=(CardView)itemView.findViewById(R.id.card_view) ;

            b1 =(Button)itemView.findViewById(R.id.take_action);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String p_id=tv1.getText().toString();
                    String p_text=tv2.getText().toString();
                    String time=tv3.getText().toString();
                    String date=tv4.getText().toString();
                    String name=tv5.getText().toString();
                    String username=tv6.getText().toString();
                    String phone=tv7.getText().toString();
                    String email_id=tv8.getText().toString();
                    String status=tv9.getText().toString();
                    String encoded_image=tv10.getText().toString();
                    String p_image=tv11.getText().toString();



                    RequestQueue requestQueue= Volley.newRequestQueue(context);
                    StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.50.203:8000/take_action/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {

                            try {
                                JSONObject o = new JSONObject(response);
                                String dat = o.getString("msg");
                                if(dat.equals("yes"))
                                {
                                    Toast.makeText(context,"Action Taken Successfully",Toast.LENGTH_SHORT).show();

                                    Intent i1= new Intent(context, Admin_view_drug_post.class);
                                    context.startActivity(i1);

                                }
                                else
                                {
                                    Toast.makeText(context, "Already Sent to Cops", Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                Log.e(TAG,error.getMessage());
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> m=new HashMap<>();
                            m.put("p_id",p_id);
                            m.put("p_text",p_text);
                            m.put("time",time);
                            m.put("date",date);
                            m.put("name",name);
                            m.put("username",username);
                            m.put("phone",phone);
                            m.put("email_id",email_id);
                            m.put("status",status);
                            m.put("encoded_image",encoded_image);
                            m.put("p_image",p_image);


                            return m;
                        }
                    };
                    requestQueue.add(requ);
                }
            });

        }
    }

}

