package com.example.hwhong.firebasechatexplore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hwhong on 7/26/16.
 */
public class Chat extends AppCompatActivity{

    private Button sendButton;
    private EditText editText;
    private TextView convo;

    private String chatRoom;
    private String userName;

    private DatabaseReference databaseReference;
    private String key;

    private String chatUsername, chatMessg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);

        sendButton = (Button) findViewById(R.id.sendButton);
        editText = (EditText) findViewById(R.id.msg);
        convo = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();

        chatRoom = intent.getExtras().get("room_Name").toString();
        userName = intent.getExtras().get("user_Name").toString();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(chatRoom);

        setTitle(""+chatRoom);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> hashmap = new HashMap<String, Object>();

                key = databaseReference.push().getKey();
                databaseReference.updateChildren(hashmap);


                //within the key object
                DatabaseReference child = databaseReference.child(key);
                Map<String, Object> hashMap2 = new HashMap<String, Object>();

                hashMap2.put("user_Name", userName);
                hashMap2.put("msg", editText.getText().toString());

                child.updateChildren(hashMap2);
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //when app connects with the database
                appendConvo(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //when child has new values
                appendConvo(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void appendConvo(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            chatMessg = (String) ((DataSnapshot)i.next()).getValue();
            chatUsername = (String) ((DataSnapshot)i.next()).getValue();

            convo.append(chatUsername + " : " + chatMessg + " \n");
        }
    }
}
