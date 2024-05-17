package com.example.drugdefender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class User_reg extends AppCompatActivity
{
    Button b1,c1;
    EditText ed1,ed2,ed3,ed4,ed5,ed6;
    TextView tv1;
    String picturePath;
    String image;
    private static int RESULT_LOAD_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);

        ed1=(EditText)findViewById(R.id.username);
        ed2=(EditText)findViewById(R.id.email);
        ed3=(EditText)findViewById(R.id.phone);
        ed4=(EditText)findViewById(R.id.pass);
        ed5=(EditText)findViewById(R.id.cpass);
        ed6=(EditText)findViewById(R.id.name);
        tv1=(TextView)findViewById(R.id.gotologin);
        tv1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i2=new Intent(User_reg.this,LoginPage.class);
                startActivity(i2);
            }
        });

        c1 = (Button) findViewById(R.id.choose);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0)
            {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        b1=(Button)findViewById(R.id.reg);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("1","Register button Clicked --------");

                String username=ed1.getText().toString();
                String email=ed2.getText().toString();
                String phone=ed3.getText().toString();
                String pass=ed4.getText().toString();
                String cpass=ed5.getText().toString();
                String name=ed6.getText().toString();

                boolean a= Patterns.EMAIL_ADDRESS.matcher(email).matches();
                String converted_a=String.valueOf(a);
                Log.e("email : ", String.valueOf(a));


                if (username.equals("")||phone.equals("")||email.equals("")||pass.equals("")||cpass.equals("")||name.equals("")){
                    Toast.makeText(getApplicationContext(),"Please provide full details",Toast.LENGTH_SHORT).show();
                }
                else if(!pass.equals(cpass)){
                    Toast.makeText(getApplicationContext(),"Passwords should be same",Toast.LENGTH_SHORT).show();

                }
                else if((username.length()<5)||(pass.length()<5))
                {
                    Toast.makeText(getApplicationContext(),"Username/Password should contain atleast 5 characters",Toast.LENGTH_SHORT).show();
                }
                else if(converted_a.equals("false")) //(Objects.equals(a, "false"))
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
                }
                else if (phone.length()==10){
                    Log.e("Entered here","Entered here");
                    RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                    StringRequest requ=new StringRequest(Request.Method.POST, "http://192.168.50.203:8000/register/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("Response is: ", response.toString());
                            try {
                                JSONObject o = new JSONObject(response);
                                String dat = o.getString("msg");
                                if(dat.equals("yes"))
                                {
                                    Toast.makeText(User_reg.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                                    Intent i1=new Intent(User_reg.this,LoginPage.class);
                                    startActivity(i1);
                                }
                                else if(dat.equals("Already registered"))
                                {
                                    Toast.makeText(User_reg.this, "This username is already taken", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(User_reg.this, "Error Happened!!!", Toast.LENGTH_LONG).show();
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
                            m.put("name",name);
                            m.put("username",username);
                            m.put("phone",phone);
                            m.put("email_id",email);
                            m.put("password",pass);
                            m.put("image",image);
                            m.put("imagepath",picturePath);


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

                Toast.makeText(getApplicationContext(),"Image Selected :" +picturePath ,Toast.LENGTH_SHORT).show();


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