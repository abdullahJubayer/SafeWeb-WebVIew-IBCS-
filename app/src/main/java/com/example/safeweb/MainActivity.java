package com.example.safeweb;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    Set<String> images;
    Set<String> itemName;
    List<String> item_image;
    List<String> item_name;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        preferences=getSharedPreferences("com.example.safeweb",MODE_PRIVATE);

        Set<String>get_images=preferences.getStringSet("images",null);
        if (get_images==null){
            writeImage();
        }
        Set<String>get_name=preferences.getStringSet("name",null);
        if (get_name==null){
            writeName();
        }

        refreshPage();


    }

    private void refreshPage() {

        Set<String> i=preferences.getStringSet("images",null);
        Set<String> n=preferences.getStringSet("name",null);

        if ((i!=null) && (n!=null)){

            item_name=new ArrayList<>(n);
            item_image= new ArrayList<>(i);
            Log.i("nammmm",item_name.get(5));
            showData(item_name,item_image);
        }
    }

    public void showData(final List<String> item_name, final List<String> item_image){
        gridView=findViewById(R.id.grid_view);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return item_image.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null){
                    convertView=getLayoutInflater().inflate(R.layout.grid_item_design,parent,false);
                }
                TextView name=convertView.findViewById(R.id.text_name);
                ImageView imageView=convertView.findViewById(R.id.image_show);
                name.setText(item_name.get(position));
                imageView.setImageResource(Integer.valueOf(item_image.get(position)));
                return convertView;
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==(item_image.size()-1)){
                    getCustomAlert().show();
                }
            }
        });
    }

    public AlertDialog.Builder getCustomAlert(){

        LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
        final View alertView=inflater.inflate(R.layout.custom_alert_dialog,null);
        AlertDialog.Builder alert= new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add")
                .setView(alertView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText title=alertView.findViewById(R.id.title_id);
                        EditText link=alertView.findViewById(R.id.web_address);
                        String name=title.getText().toString();
                        String address=title.getText().toString();
                        if (name.isEmpty() || name.startsWith(" ")){
                            title.setError("Set a Title");
                        }
                        else if (address.isEmpty() || address.startsWith(" ")){
                            link.setError("Set a Valid Web Link");
                        }
                        else {
                                writeName(name);
                                writeImage(R.drawable.ic_internet);
                                refreshPage();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                ;
        return  alert;

    }

    private void writeName(){
        itemName=new HashSet<>();
        itemName.add("Google");
        itemName.add("Facebook");
        itemName.add("Youtube");
        itemName.add("Twitter");
        itemName.add("Amazon");
        itemName.add("Add");
        preferences.edit().putStringSet("name",itemName).apply();
    }
    private void writeImage(){
        preferences.edit().clear();
        images=new HashSet<>();
        images.add(String.valueOf(R.drawable.ic_google));
        images.add(String.valueOf(R.drawable.ic_facebook));
        images.add(String.valueOf(R.drawable.ic_youtube));
        images.add(String.valueOf(R.drawable.ic_twitter));
        images.add(String.valueOf(R.drawable.ic_amazon));
        images.add(String.valueOf(R.drawable.add));
        preferences.edit().putStringSet("images",images).apply();

    }
    private void writeName(String name){
        item_name.add(name);
        itemName=new HashSet<>(item_name);
        preferences.edit().putStringSet("name",itemName).apply();
    }
    private void writeImage(Integer img){
        item_image.add(String.valueOf(img));
        images=new HashSet<>(item_image);
        preferences.edit().putStringSet("images",images).apply();
    }

}
