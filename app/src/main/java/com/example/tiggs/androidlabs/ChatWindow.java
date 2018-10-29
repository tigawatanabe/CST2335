package com.example.tiggs.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.tiggs.androidlabs.ChatDataBaseHelper.ACTIVITY_NAME;
import static com.example.tiggs.androidlabs.ChatDataBaseHelper.KEY_MESSAGE;
import static com.example.tiggs.androidlabs.ChatDataBaseHelper.TABLE_NAME;

public class ChatWindow extends Activity {
    ListView myList;
    EditText chatEdit;
    Button sendButton;
    ArrayList<String> chatLog;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //connect writable database and open database
        ChatDataBaseHelper dbHelper = new ChatDataBaseHelper(this);
        db = dbHelper.getWritableDatabase();

        //insert data to ArrayList
        Cursor cursor = db.rawQuery("SELECT*from " + dbHelper.TABLE_NAME, new String[] {});
            //find message index
            int message_index = cursor.getColumnIndex(KEY_MESSAGE);
            //move cursor to first row
            cursor.moveToFirst();

        //add values to arraylist
        chatLog = new ArrayList<>();
        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount());
        while(!cursor.isAfterLast() ) {

            chatLog.add(cursor.getString(message_index));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: "
                    + cursor.getString(cursor.getColumnIndex(ChatDataBaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();

        }
            for (int i = 0; i < cursor.getColumnCount(); i++){
                cursor.getColumnName(i);
            }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        //reference xml
        myList=findViewById(R.id.myList);
        chatEdit=findViewById(R.id.chatEdit);
        sendButton=findViewById(R.id.sendButton);

        ChatAdapter messageAdapter=new ChatAdapter(this);
        myList.setAdapter(messageAdapter);


        //send button
        sendButton.setOnClickListener(e -> {
            ContentValues cValue = new ContentValues();
            chatLog.add(chatEdit.getText().toString());
            cValue.put(dbHelper.KEY_MESSAGE, chatEdit.getText().toString());
            db.insert(dbHelper.TABLE_NAME, "", cValue);

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
    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }
}
