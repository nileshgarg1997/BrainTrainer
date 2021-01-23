package com.myapp.braintrainerpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.service.chooser.ChooserTarget;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    public InterstitialAd mInterstitialAd;
    private String TAG="Nilesh";
    int locationOfCorrectAns;
    int scorecount=0;
    int numberOfQuestions=0;
    RelativeLayout gameRL;
    TextView question;
    TextView remark,score,timerTv,finalscore;
    MediaPlayer mediaPlayer;

    Button bt1;
    Button bt2, bt3, bt4,playAgain;
    ArrayList<Integer> answers=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //banner ad
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //For testing in realme u1
        List<String> testDeviceIds = Arrays.asList("6CE7341ADF01E2F45DA3A77C31A2B13A");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        //Interstitial Ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        InterstitialAd interstitialAd=new InterstitialAd(this);
        this.mInterstitialAd=interstitialAd;
        interstitialAd.setAdUnitId("ca-app-pub-6611402119785086/6457872619");
        this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        this.mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG,"onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.d(TAG,"onAdFailed"+ adError.toString());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.d(TAG,"onAdOpened");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG,"onAdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d(TAG,"onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                Log.d(TAG,"onAdClosed");
                MainActivity.this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        TextView score=findViewById(R.id.score);
        TextView question=findViewById(R.id.question);

        question.setText("Start");
        score.setText("0/0");

        gameRL=findViewById(R.id.gameRL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.shareBt:
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(i.EXTRA_SUBJECT,"Share");
                String shareMessage="https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
                i.putExtra(i.EXTRA_TEXT,shareMessage);
                Intent chooser=Intent.createChooser(i,"Share the App using...");
                startActivity(chooser);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void PLAY(View view) {
        TextView question=findViewById(R.id.question);
        TextView finalscore=findViewById(R.id.finalScore);
        Button playAgain=findViewById(R.id.playAgain);
        TextView remark=findViewById(R.id.remark);
        TextView timerTv=findViewById(R.id.timerTv);
        Button playBt=findViewById(R.id.playBt);
        playBt.setVisibility(View.INVISIBLE);
        gameRL.setVisibility(View.VISIBLE);
        genQues();
        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long l) {
                timerTv.setText(l/1000+"s");
                player();
            }

            @Override
            public void onFinish() {
                timerTv.setText(0+"s");
                finalscore.setText("Your Score "+Integer.toString(scorecount)+ " out of "+Integer.toString(numberOfQuestions)+"!");
                playAgain.setVisibility(View.VISIBLE);
                gameRL.setVisibility(View.INVISIBLE);
                remark.setVisibility(View.INVISIBLE);
                question.setText("End");
                showAd();
            }
        }.start();
    }

    public void genQues(){
        Button bt1=findViewById(R.id.bt1);
        Button bt2=findViewById(R.id.bt2);
        Button bt3=findViewById(R.id.bt3);
        Button bt4=findViewById(R.id.bt4);
        TextView question=findViewById(R.id.question);
        Random rand=new Random();
        int a=rand.nextInt(30);
        int b=rand.nextInt(30);
        question.setText(Integer.toString(a)+"+"+Integer.toString(b));
        locationOfCorrectAns=rand.nextInt(4);
        answers.clear();
        int incorrectAns;
        for (int i=0;i<4;i++){
            if (i==locationOfCorrectAns){
                answers.add(a+b);
            }
            else {
                incorrectAns=rand.nextInt(61);
                while (incorrectAns==a+b){
                    incorrectAns=rand.nextInt(61);
                }
                answers.add(incorrectAns);
            }

        }
        bt1.setText(Integer.toString(answers.get(0)));
        bt2.setText(Integer.toString(answers.get(1)));
        bt3.setText(Integer.toString(answers.get(2)));
        bt4.setText(Integer.toString(answers.get(3)));
    }

    public void btnClick(View n){
        TextView score=findViewById(R.id.score);
        TextView remark=findViewById(R.id.remark);
        if(n.getTag().toString().equals(Integer.toString(locationOfCorrectAns)))
        {
            scorecount++;
            remark.setVisibility(View.VISIBLE);
            remark.setText("Correct!");
        }
        else {
            remark.setVisibility(View.VISIBLE);
            remark.setText("Incorrect!");
        }
        numberOfQuestions++;
        score.setText(scorecount+"/"+numberOfQuestions);
        genQues();
    }


    public void playAgain(View view) {
        TextView finalscore=findViewById(R.id.finalScore);
        TextView question=findViewById(R.id.question);
        TextView score=findViewById(R.id.score);
        Button playAgain=findViewById(R.id.playAgain);
        TextView remark=findViewById(R.id.remark);
        TextView timerTv=findViewById(R.id.timerTv);
        finalscore.setVisibility(View.INVISIBLE);
        scorecount=0;
        numberOfQuestions=0;
        timerTv.setText("30s");
        score.setText("0/0");
        remark.setText("");
        playAgain.setVisibility(View.INVISIBLE);
        question.setVisibility(View.VISIBLE);
        gameRL.setVisibility(View.VISIBLE);
        genQues();
        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long l) {
                timerTv.setText(l/1000+"s");
                player();
            }

            @Override
            public void onFinish() {
                finalscore.setVisibility(View.VISIBLE);
                timerTv.setText(0+"s");
                finalscore.setText("Your Score "+Integer.toString(scorecount)+ " out of "+Integer.toString(numberOfQuestions)+"!");
                playAgain.setVisibility(View.VISIBLE);
                gameRL.setVisibility(View.INVISIBLE);
                remark.setVisibility(View.INVISIBLE);
                question.setText("End");
                showAd();
            }
        }.start();
    }

    public void player(){
        mediaPlayer = MediaPlayer.create(this,R.raw.tick);
        mediaPlayer.start();
    }

    public void showAd(){
//        while (mInterstitialAd==null){
//            mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        }
        if (MainActivity.this.mInterstitialAd.isLoaded()) {
            MainActivity.this.mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }
}