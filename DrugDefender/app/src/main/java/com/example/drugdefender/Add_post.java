package com.example.drugdefender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Add_post extends AppCompatActivity
{
    ImageView iv1;
    Button u1,c1;
    EditText e1;
    String picturePath;
    String imgpath;
    String caption;
    String image;
    File img_file;
    String encodedImage="";
    private static int RESULT_LOAD_IMAGE = 1;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        SharedPreferences prefs = getSharedPreferences("userdetails", MODE_PRIVATE);
        String username=prefs.getString("username", "");


        e1=(EditText)findViewById(R.id.caption);
        c1 = (Button) findViewById(R.id.choose);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0)
            {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        u1=(Button)findViewById(R.id.upload);
        u1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    caption= e1.getText().toString();
                    Log.e("Caption is : ",caption);
                    imgpath=picturePath;
                    Log.e("Image Path is : ",imgpath);
                }
                catch(NullPointerException e)
                {
                    Toast.makeText(getApplicationContext(),"Please select an Image ",Toast.LENGTH_SHORT).show();
                }


                if(caption.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Add Caption ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                    StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.50.203:8000/upload_post/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("Response is: ", response.toString());
                            try {
                                JSONObject o = new JSONObject(response);
                                String dat = o.getString("msg");
                                if(dat.equals("yes"))
                                {
                                    Toast.makeText(Add_post.this, "Post Uploaded Successfully!", Toast.LENGTH_LONG).show();
                                    Intent i1 = new Intent(Add_post.this, User_home.class);
                                    startActivity(i1);
                                }
                                else
                                {
                                    Toast.makeText(Add_post.this, "Error Happened!!!", Toast.LENGTH_LONG).show();
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
//                            Map<String,File> m=new HashMap<>();
                            m.put("image",image);
                            m.put("imagepath",picturePath);
                            m.put("caption", caption);
                            m.put("username",username);

                            return m;
                        }

                    };
                    requ.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(requ);
                }
            }
        });


        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.home)
                {
                    Intent i4=new Intent(Add_post.this,User_home.class);
                    startActivity(i4);

                }
//
                else if(id==R.id.addpost)
                {
//
                    Intent i3=new Intent(Add_post.this,Add_post.class);
                    startActivity(i3);
//                    Toast.makeText(getApplicationContext(),"You clicked View my Bucket",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.viewmypost)
                {
                    Intent i2=new Intent(Add_post.this,View_my_post.class);
                    startActivity(i2);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.reports)
                {
                    Intent i20=new Intent(Add_post.this,Get_reports.class);
                    startActivity(i20);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.my_profile)
                {
                    Intent i21=new Intent(Add_post.this,My_profile.class);
                    startActivity(i21);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.logout)
                {
                    Intent i1=new Intent(Add_post.this,LoginPage.class);
                    startActivity(i1);
                }


                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
                image = getStringImage(lastBitmap);
//                Log.e("image", image);

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(filePath,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                Log.e("Picture Path : ",picturePath);
//                img_file = new File(picturePath);

                iv1 = (ImageView) findViewById(R.id.image);
                iv1.setImageURI(data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
}