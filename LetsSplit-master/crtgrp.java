package com.example.anuswa.letssplit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class crtgrp extends AppCompatActivity {
    private Button crtgrp;
    private ImageButton back;
   // private final int Pick_contact = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crtgrp);
        crtgrp = findViewById(R.id.crtGrp_id);
        back = findViewById(R.id.bck);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(crtgrp.this,login.class);
                startActivity(in);
            }
        });

        crtgrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent in = new Intent(crtgrp.this,newgrp.class);
               startActivity(in);
            }
        });
    }

    /*public void callContact(View v){
        Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(in,Pick_contact);
    }


    @Override
    protected void onActivityResult(int reqCode,int ResultCode, Intent data) {
        super.onActivityResult(reqCode,ResultCode,data);

        if (reqCode==Pick_contact)
        {
            if (ResultCode==AppCompatActivity.RESULT_OK)
            {
                Uri contactData=data.getData();
                Cursor c=getContentResolver().query(contactData,null,null,null,null);

                if (c.moveToFirst())
                {
                    String name=c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    Toast.makeText(this,"You have picked" + name,Toast.LENGTH_LONG).show();
                }
            }
        }
    }*/
}
