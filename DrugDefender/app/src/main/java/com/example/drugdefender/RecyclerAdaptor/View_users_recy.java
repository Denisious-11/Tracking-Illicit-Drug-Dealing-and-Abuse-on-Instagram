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
import com.example.drugdefender.R;
import com.example.drugdefender.View_users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class View_users_recy extends RecyclerView.Adapter<View_users_recy.MyViewHolder> {
    private static final String TAG = "RecyclerNews";
    private final Context context;
    private final JSONArray array;
    private static final String fsts ="0";
    Activity act;



    public View_users_recy(Context applicationContext, JSONArray jsonArray, Activity a) {
        this.context = applicationContext;
        this.array = jsonArray;
        this.act = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_list, null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            JSONObject jsonObject = array.getJSONObject(position);

            holder.tv1.setText(jsonObject.getString("u_id"));
            holder.tv2.setText(jsonObject.getString("name"));
            holder.tv3.setText(jsonObject.getString("username"));
            holder.tv4.setText(jsonObject.getString("phone"));
            holder.tv5.setText(jsonObject.getString("email_id"));




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
        CardView cv;
        Button b1;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.u_id);
            tv2 = (TextView) itemView.findViewById(R.id.name);
            tv3 = (TextView) itemView.findViewById(R.id.username);
            tv4 = (TextView) itemView.findViewById(R.id.phone);
            tv5 = (TextView) itemView.findViewById(R.id.email_id);

            cv=(CardView)itemView.findViewById(R.id.card_view) ;

            b1 =(Button)itemView.findViewById(R.id.delete);
            b1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    String u_id=tv1.getText().toString();
                    Log.e("User id : ",u_id);

                    RequestQueue requestQueue= Volley.newRequestQueue(context);
                    StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.50.203:8000/delete/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {

                            try {
                                JSONObject o = new JSONObject(response);
                                String dat = o.getString("msg");
                                if(dat.equals("yes"))
                                {
                                    Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show();

                                    Intent i1= new Intent(context, View_users.class);
                                    context.startActivity(i1);

                                }
                                else {
                                    Toast.makeText(context, "Error Occured! ", Toast.LENGTH_LONG).show();
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
                            m.put("u_id",u_id);

                            return m;
                        }
                    };
                    requestQueue.add(requ);
                }
            });


        }
    }

}

