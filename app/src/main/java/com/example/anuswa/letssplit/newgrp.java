package com.example.anuswa.letssplit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.firebase.database.Query;


public class newgrp extends AppCompatActivity {

    private EditText grpname_edit;
    private Button addPer, save, back, next;
    private final int Pick_contact = 1;
    private ImageView grpimg;
    private DatabaseReference myref;
    FirebaseUser firebaseUser;
    String ConName, grpName, uid, con;
    private TextView grp;

    String  grpname,sresult,key;
    String[] personname = new String[10];
    String[] persondebt = new String[10];
    int count=1,x=0;

    String mtotbill,mcat,contactnm;

    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listg = new ArrayList<>();
    private ListView listgrp;
    FirebaseAuth mAuth;
    DatabaseReference grpref;
    String namep ;
    List list = new List();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgrp);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        grpname_edit = (EditText) findViewById(R.id.grpnm_id);
        addPer = (Button) findViewById(R.id.addP_id);
       save = (Button) findViewById(R.id.save_id);
        grpimg = findViewById(R.id.grpimg);
        grp = findViewById(R.id.grp1_id);
        back = findViewById(R.id.back_id);
        next = findViewById(R.id.next_id);

        listgrp = findViewById(R.id.list_id);

        grpref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        arrayAdapter = new ArrayAdapter<String>(newgrp.this, android.R.layout.simple_list_item_1, listg);
        listgrp.setAdapter(arrayAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(newgrp.this, crtgrp.class);
                startActivity(in);
            }
        });
       /* next.setOnClickListener(new View.OnClickListener() {
            @Override
            //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            public void onClick(View v) {

                Intent in1 = new Intent(newgrp.this, ingrp.class);
                in1.putExtra("gnm", grp.getText());
                startActivity(in1);

            }

        });*/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(newgrp.this);
                View mview = getLayoutInflater().inflate(R.layout.category_dialog, null);
                final EditText totalbill = mview.findViewById(R.id.idtotbill);
                final EditText category = mview.findViewById(R.id.idcat);
                mtotbill =totalbill.getText().toString();
                mcat=category.getText().toString();


                builder.setPositiveButton("Unequally", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mtotbill =totalbill.getText().toString();
                        mcat=category.getText().toString();

                        //Toast.makeText(MainActivity.this, "added successfully", Toast.LENGTH_SHORT).show();
                        if (!mtotbill.isEmpty() && !mcat.isEmpty())
                        {
                            startActivity(new Intent(newgrp.this, Distribute.class).putExtra("gn", grpname).putExtra("Result", sresult).putExtra("contactname", contactnm));
                        }
                        else
                        {
                            Toast.makeText(newgrp.this, "please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                builder.setNegativeButton("Equally", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mtotbill =totalbill.getText().toString();
                        mcat=category.getText().toString();


                        if (!mtotbill.isEmpty() && !mcat.isEmpty())
                        {
                            x=x+1;
                            Double result;
                            try {
                                double num = Double.parseDouble(mtotbill);
                                result = num /count;
                                sresult = String.valueOf(result);
                                DisplayMembers();

                                startActivity(new Intent(newgrp.this, ingrp.class));


                            } catch (NumberFormatException e) {
                            }
                        }

                        else
                        {
                            Toast.makeText(newgrp.this, "please fill all fields", Toast.LENGTH_SHORT).show();
                        }

                    }
                });




                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setView(mview);
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   Toast.makeText(newgrp.this,"You pressed save ",Toast.LENGTH_SHORT).show();
                uid = firebaseUser.getUid();
                grpName = grpname_edit.getText().toString();
                myref = FirebaseDatabase.getInstance().getReference("users").child(uid);
                myref.child("Group").child(grpName).setValue(grpName);

                grp.setText(grpName);
                if (!grpName.isEmpty()) {
                    grpname_edit.setVisibility(View.INVISIBLE);
                    grpimg.setVisibility(View.INVISIBLE);
                }
            }
        });


        addPer.setOnClickListener(new View.OnClickListener() {
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
                   //// Retrive();
                }
            }
        }
    }

    void setCon(String name) {
        String contactnms = name;
        int cost = 1;
        //String connum = Contact;
        this.ConName = name;
        List list = new List(contactnms,cost);
        //list.setPerson(contactnm);
        getLayoutInflater();
        //this.con = Contact;
        namep = name;
        myref = FirebaseDatabase.getInstance().getReference("users").child(uid).child("Group");

        myref.child(grpName).child(name).setValue(list);


    }

    private void Retrive() {
        grpref.child("users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    // List list =dataSnapshot.getValue();
                    set.add(((DataSnapshot) iterator.next()).getKey());
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

        private void DisplayMembers()
        {

            myref=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpname).child("Members");
            myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    AmountInfo amountInfo=new AmountInfo(contactnm,sresult);

                    int i=0;
                    for(DataSnapshot ds: dataSnapshot.getChildren())
                    {

                        amountInfo.setPersonname(ds.getValue(AmountInfo.class).getPersonname());
                        amountInfo.setPricee(ds.getValue(AmountInfo.class).getPricee());
                        i=i+1;

                        personname[i]=amountInfo.getPersonname();
                        persondebt[i]=amountInfo.getPricee();

                        if(x>0) {

                            Query ref = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpname).child("Members").orderByChild("personname").equalTo(contactnm);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                    for (DataSnapshot childsnap : dataSnapshot.getChildren()) {
                                        key = childsnap.getKey();
                                        update(key, sresult);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        private void update (String key , String sresult)
        {
            myref=FirebaseDatabase.getInstance().getReference();
            myref=myref.child("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpname).child("Members");
            myref.child(key).child("pricee").setValue(sresult);

        }



}




/*
 public void callContact(View v){
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