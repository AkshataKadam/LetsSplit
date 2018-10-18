package com.example.anuswa.letssplit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.Query;
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
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listg = new ArrayList<>();
    private ListView listgrp;
String name1;
    String ConName,uid;
    private final int Pick_contact = 1;
    private Button addmem;
    private DatabaseReference myref;

    String  grpname,sresult,key;
    String[] personname = new String[10];
    String[] persondebt = new String[10];
    int count=1,x=0;
    String mtotbill,mcat,contactnm;

    Button equal,next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_ingrp);
        grptxt = findViewById(R.id.grptxt_id);
        listgrp = findViewById(R.id.ingrplist_id);
        equal = findViewById(R.id.equ_id);
        next = findViewById(R.id.next_id);

        addmem = findViewById(R.id.addmem_id);



        grpref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        arrayAdapter = new ArrayAdapter<String>(ingrp.this, android.R.layout.simple_list_item_1, listg);
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

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ingrp.this, Distribute.class);
                startActivity(in);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ingrp.this);
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
                            startActivity(new Intent(ingrp.this, Distribute.class).putExtra("gn", grpname).putExtra("Result", sresult).putExtra("contactname", contactnm));
                        }
                        else
                        {
                            Toast.makeText(ingrp.this, "please fill all fields", Toast.LENGTH_SHORT).show();
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

                                startActivity(new Intent(ingrp.this, ingrp.class));


                            } catch (NumberFormatException e) {
                            }
                        }

                        else
                        {
                            Toast.makeText(ingrp.this, "please fill all fields", Toast.LENGTH_SHORT).show();
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

        myref.child(grpnm).child(name);


    }

    private void Retrive() {
        grpref.child("users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpnm).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set=new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    set.add(((DataSnapshot)iterator.next()).getKey());
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

        // grpref.child("users").child(mAuth.getCurrentUser().getUid()).child("Group").child(grpName).addValueEventListener(new ValueEventListener() {


    }



}

