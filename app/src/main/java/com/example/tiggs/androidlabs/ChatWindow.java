package com.example.tiggs.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    ListView myList;
    EditText chatEdit;
    Button sendButton;
    ArrayList<String> chatLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        chatLog = new ArrayList<>();
        //reference xml
        myList=findViewById(R.id.myList);
        chatEdit=findViewById(R.id.chatEdit);
        sendButton=findViewById(R.id.sendButton);

        ChatAdapter messageAdapter=new ChatAdapter(this);
        myList.setAdapter(messageAdapter);

        sendButton.setOnClickListener(e -> {
            chatLog.add(chatEdit.getText().toString());
            messageAdapter.notifyDataSetChanged();//this restarts the process of getCount() & getView()
            chatEdit.setText("");
        });

    }
    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx){
            super(ctx,0);
        }
        public int getCount(){
            return chatLog.size();
        }
        public String getItem(int position){
            return chatLog.get(position);
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2==0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);}
                else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position));//get the string at position
            return result;
        }
        public long getItemId(int position){
            return position;
        }
    }
}
