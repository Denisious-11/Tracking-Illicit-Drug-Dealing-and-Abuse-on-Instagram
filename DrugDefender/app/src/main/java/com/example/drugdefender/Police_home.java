package com.example.drugdefender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Police_home extends AppCompatActivity
{
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_home);

        navigationView = (NavigationView) findViewById(R.id.nav1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.home)
                {
                    Intent i4=new Intent(Police_home.this,Police_home.class);
                    startActivity(i4);

                }
//
                else if(id==R.id.viewdrugpost)
                {
                    Intent i2=new Intent(Police_home.this,Police_view_drug_post.class);
                    startActivity(i2);
//                    Toast.makeText(getApplicationContext(),"You clicked Profile",Toast.LENGTH_SHORT).show();

                }
                else if(id==R.id.logout)
                {
                    Intent i1=new Intent(Police_home.this,LoginPage.class);
                    startActivity(i1);
                }


                return true;
            }
        });



    }
}