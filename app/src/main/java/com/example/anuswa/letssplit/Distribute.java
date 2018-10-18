package com.example.anuswa.letssplit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Distribute extends AppCompatActivity {

    ListView lv;
    String gname,key,sresult,cnm,total,payable;
    String[] personname = new String[10];
    String[] persondebt = new String[10];

    double tot,payy,result;
    String stot,spay,sres;

    Button done;

    private DatabaseReference myref,groupref;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribute);



        done=findViewById(R.id.done_id);
        lv=findViewById(R.id.dislist_id);
        mAuth=FirebaseAuth.getInstance();
        myref=FirebaseDatabase.getInstance().getReference();

        Bundle bundle=getIntent().getExtras();
        gname=bundle.getString("gn");
        sresult=bundle.getString("Result");
        cnm=bundle.getString("contactname");


        DisplayMembers(sresult);
        MyAdapter myAdapter=new MyAdapter(Distribute.this,personname,persondebt);
        lv.setAdapter(myAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Distribute.this);
                View mview = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final EditText mtot = mview.findViewById(R.id.totshare);
                final EditText mpay = mview.findViewById(R.id.pay);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        total =mtot.getText().toString();
                        payable=mpay.getText().toString();

                        //Toast.makeText(MainActivity.this, "added successfully", Toast.LENGTH_SHORT).show();
                        if (!total.isEmpty() && !payable.isEmpty())
                        {
                            tot = Double.parseDouble(total);
                            payy=Double.parseDouble(payable);
                            result = tot - payy ;

                            //condition

                            stot = String.valueOf(tot);
                            spay = String.valueOf(payy);
                            sresult= String.valueOf(result);
                            DisplayMembers(sresult);

                        }
                        else
                        {
                            Toast.makeText(Distribute.this, "please fill all fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               // startActivity(new Intent(Distribute.this,Groups_List.class));
            }
        });

    }

    private void DisplayMembers(final String sresult)
    {

        myref= FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(gname).child("Members");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                AmountInfo amountInfo=new AmountInfo(cnm,sresult);

                int i=-1;
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {


                    amountInfo.setPersonname(ds.getValue(AmountInfo.class).getPersonname());
                    amountInfo.setPricee(ds.getValue(AmountInfo.class).getPricee());
                    i=i+1;

                    personname[i]=amountInfo.getPersonname();
                    persondebt[i]=amountInfo.getPricee();

                        Query ref =FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(gname).child("Members").orderByChild("personname").equalTo(cnm);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                for(DataSnapshot childsnap : dataSnapshot.getChildren())
                                {
                                    key=childsnap.getKey();
                                    update(key, sresult);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void update(String key,String sresult)
    {
        groupref=FirebaseDatabase.getInstance().getReference();
        groupref=groupref.child("Users").child(mAuth.getCurrentUser().getUid()).child("Group").child(gname).child("Members");
        groupref.child(key).child("cost").setValue(sresult);

    }

}
