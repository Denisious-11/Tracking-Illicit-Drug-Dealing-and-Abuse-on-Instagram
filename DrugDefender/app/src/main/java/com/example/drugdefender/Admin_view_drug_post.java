package com.example.drugdefender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.drugdefender.RecyclerAdaptor.Admin_drug_recy;
import com.example.drugdefender.RecyclerAdaptor.Police_drug_recy;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin_view_drug_post extends AppCompatActivity
{
    Button b1;
    RecyclerView recv;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_drug_post);

        SharedPreferences prefs = getSharedPreferences("userdetails", MODE_PRIVATE);
        String username=prefs.getString("username", "");

        recv=(RecyclerView)findViewById(R.id.recyclerView11);
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.50.203:8000/admin_view_drug_posts/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response.trim());

                    JSONArray jsonArray = object.getJSONArray("data");
                    Admin_drug_recy ordrec = new Admin_drug_recy(Admin_view_drug_post.this, jsonArray, Admin_view_drug_post.this);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Admin_view_drug_post.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recv.getContext(),
                            linearLayoutManager.getOrientation());
                    recv.addItemDecoration(dividerItemDecoration);
                    recv.setLayoutManager(linearLayoutManager);
                    recv.setAdapter(ordrec);


                } catch (JSONException e) {
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
                m.put("username",username);

                return m;
            }
        };
        requestQueue.add(requ);



        navigationView = (NavigationView) findViewById(R.id.nav11);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.home)
                {
                    Intent i4=new Intent(Admin_view_drug_post.this,Admin_home.class);
                    startActivity(i4);

                }
                else if(id==R.id.users)
                {
                    Intent i3=new Intent(Admin_view_drug_post.this,View_users.class);
                    startActivity(i3);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
//
                else if(id==R.id.viewdrugpost)
                {
                    Intent i2=new Intent(Admin_view_drug_post.this,Admin_view_drug_post.class);
                    startActivity(i2);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.logout)
                {
                    Intent i1=new Intent(Admin_view_drug_post.this,LoginPage.class);
                    startActivity(i1);
                }


                return true;
            }
        });

    }
}