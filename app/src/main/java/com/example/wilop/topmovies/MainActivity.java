package com.example.wilop.topmovies;


import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {


    List<String> NamesList = new ArrayList<String>();
    List<String> RatesList = new ArrayList<String>();
    List<String> RscList = new ArrayList<String>();


    public class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage){
            this.bmImage = bmImage;
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                e.printStackTrace();
            }
            return mIcon11;
            }
        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        getHTML();



    }


    public void getHTML(){

        Ion.with(getApplicationContext())
                //.load("http://dtlinks.club/?v=652")
                .load("http://www.imdb.com/list/ls064079588")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d("HTML",result);
                        HTMLParser(result);
                    }
                });


    }

    public void HTMLParser(String result){

        Document document = Jsoup.parse(result);

        Elements names = document.select(" div .lister-item-content h3 a");
        Elements rates = document.select(" div .inline-block.ratings-imdb-rating");
        Elements rscs = document.select("div .lister-item-image.ribbonize a img");


        for (int i=0; i<20;i++){
            String name = names.get(i).text();
            String rate = rates.get(i).text();
            String rsc = rscs.get(i).attr("loadlate");
            NamesList.add(name);
            RatesList.add(rate);
            RscList.add(rsc);
        }
        FullView();

    }

    public void FullView(){

        for (int i = 0 ; i<4;i++){
            int id = getResources().getIdentifier("textView"+i+"a", "id", getPackageName());
            int idb = getResources().getIdentifier("textView"+i+"b", "id", getPackageName());
            int idI = getResources().getIdentifier("imageView"+i, "id", getPackageName());
            TextView textView1 = findViewById(id);
            textView1.setText(NamesList.get(i));
            TextView textView2 = findViewById(idb);
            textView2.setText(RatesList.get(i));
            //ImageView imageView = findViewById(idI);
            new DownloadImageTask((ImageView) findViewById(idI)).execute(RscList.get(i));


        }
    }




}
