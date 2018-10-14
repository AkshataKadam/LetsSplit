package com.example.anuswa.letssplit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anuswa.letssplit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.example.anuswa.letssplit.R.layout.activity_ingrp;

public class ingrp extends AppCompatActivity {

    private TextView grptxt;
    String grpnm;
    FirebaseAuth mAuth;
    DatabaseReference grpref;
    ArrayAdapter<List> arrayAdapter;
    ArrayList<List> listg = new ArrayList<>();
    private ListView listgrp;
String name1;
    String ConName,uid;
    private final int Pick_contact = 1;
    private Button addmem;
    private DatabaseReference myref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_ingrp);
        grptxt = findViewById(R.id.grptxt_id);
        listgrp = findViewById(R.id.ingrplist_id);

        addmem = findViewById(R.id.addmem_id);


        grpref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        arrayAdapter = new ArrayAdapter<List>(ingrp.this, android.R.layout.simple_list_item_1, listg);
        listgrp.setAdapter(arrayAdapter);


        Bundle b = getIntent().getExtras();
        grpnm = b.getString("gnm");
        if (b != null) {
            grptxt.setText(grpnm);

            Retrive();
        }

        addmem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callContact(v);

            }
        });
    }


    public void callContact(View v) {
        Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(in, Pick_contact);
    }

    @Override
    protected void onActivityResult(int reqCode, int ResultCode, Intent data) {
        super.onActivityResult(reqCode, ResultCode, data);

        if (reqCode == Pick_contact) {
            if (ResultCode == AppCompatActivity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = getContentResolver().query(contactData, null, null, null, null);

                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    //String Conc=c.getString(c.getColumnIndexOrThrow(ContactsContract))
                    Toast.makeText(this, "You have picked " + name, Toast.LENGTH_LONG).show();
                    setCon(name);
                    //Retrive();
                }
            }
        }
    }

    void setCon(String name) {
        Toast.makeText(this, "Yayyyyyy", Toast.LENGTH_LONG).show();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
      //  String contactnm = name;
        //String connum = Contact;
        this.ConName = name;
        name1 = name;
        List list = new List();
        //this.con = Contact;
        myref = FirebaseDatabase.getInstance().getReference("users").child(uid).child("Group");

        myref.child(grpnm).child(name).child();


    }

    private void Retrive() {
        grpref.child("users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpnm).child(name1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<List> set = new ArrayList<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    set.add((List) ((DataSnapshot) iterator.next()).getValue());
                }

                listg.clear();
                listg.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}

