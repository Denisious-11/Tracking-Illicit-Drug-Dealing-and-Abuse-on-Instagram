package com.example.drugdefender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class My_profile extends AppCompatActivity
{
    TextView tv1;
    EditText ed2,ed3,ed4;
    Button b1;
    NavigationView navigationView;
    ImageView im1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        SharedPreferences prefs = getSharedPreferences("userdetails", MODE_PRIVATE);
        String username=prefs.getString("username", "");

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.50.203:8000/get_user_details/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response is: ", response.toString());
                try
                {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject result = jsonArray.getJSONObject(i);
                        Log.e("result", String.valueOf(result));
                        String username=result.getString("username");
                        String email=result.getString("email_id");
                        String phone=result.getString("phone");
                        String password=result.getString("password");

                        Log.e("username : ",username);
                        Log.e("email : ",email);
                        Log.e("phone : ",phone);
                        Log.e("password : ",password);


                        tv1=(TextView) findViewById(R.id.username);
                        ed2=(EditText) findViewById(R.id.email);
                        ed3=(EditText) findViewById(R.id.phone);
                        ed4=(EditText) findViewById(R.id.password);
                        im1=(ImageView)findViewById(R.id.im1);

                        String strimg=result.getString("encoded_image");
                        byte[] decodedString = Base64.decode(strimg, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        im1.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 398, 250, false));

                        tv1.setText(username);
                        ed2.setText(email);
                        ed3.setText(phone);
                        ed4.setText(password);


                    }

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

        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.home)
                {
                    Intent i4=new Intent(My_profile.this,User_home.class);
                    startActivity(i4);

                }
//
                else if(id==R.id.addpost)
                {
//
                    Intent i3=new Intent(My_profile.this,Add_post.class);
                    startActivity(i3);
//                    Toast.makeText(getApplicationContext(),"You clicked View my Bucket",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.viewmypost)
                {
                    Intent i2=new Intent(My_profile.this,View_my_post.class);
                    startActivity(i2);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.reports)
                {
                    Intent i20=new Intent(My_profile.this,Get_reports.class);
                    startActivity(i20);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.my_profile)
                {
                    Intent i21=new Intent(My_profile.this,My_profile.class);
                    startActivity(i21);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.logout)
                {
                    Intent i1=new Intent(My_profile.this,LoginPage.class);
                    startActivity(i1);
                }


                return true;
            }
        });

        b1=(Button) findViewById(R.id.update);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String username=tv1.getText().toString();

                Log.e("Username ;;;;;>>>>",username);


                String email=ed2.getText().toString();
                String phone=ed3.getText().toString();
                String password=ed4.getText().toString();

                boolean a= Patterns.EMAIL_ADDRESS.matcher(email).matches();
                String converted_a=String.valueOf(a);


                if (email.equals("")||phone.equals("")||password.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please fill the fields!!",Toast.LENGTH_LONG).show();
                }
                else if((password.length()<5))
                {
                    Toast.makeText(getApplicationContext(),"Password should contain atleast 5 characters",Toast.LENGTH_SHORT).show();
                }
                else if(converted_a.equals("false")) //(Objects.equals(a, "false"))
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
                }
                else if (phone.length()==10)
                {

                    RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                    StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.50.203:8000/update_user_details/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {

                            try {
                                JSONObject o = new JSONObject(response);
                                String dat = o.getString("msg");
                                if(dat.equals("yes"))
                                {
                                    Toast.makeText(My_profile.this, "Profile Updated Successfully!", Toast.LENGTH_LONG).show();

                                    Intent i5=new Intent(My_profile.this,My_profile.class);
                                    startActivity(i5);

                                }
                                else {
                                    Toast.makeText(My_profile.this, "Error Occured! ", Toast.LENGTH_LONG).show();
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
                            m.put("username",username);
                            m.put("email",email);
                            m.put("phone",phone);
                            m.put("password",password);

                            return m;
                        }
                    };
                    requestQueue.add(requ);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Phone number",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}