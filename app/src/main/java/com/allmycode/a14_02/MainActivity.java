package com.allmycode.a14_02;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity
    implements OnClickListener, AnimationListener {

  final int NUMBER_OF_EMOJI = 10;
  final int MINIMUM_DURATION = 500;
  final int MAXIMUM_ADDITIONAL_DURATION = 1000;
  int countShown = 0, countClicked = 0;
  Random random = new Random();

  RelativeLayout layout;
  int displayWidth, displayHeight;

  /* Activity lifecycle methods */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    layout = (RelativeLayout) findViewById(R.id.relativeLayout);

    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    displayWidth = size.x;
    displayHeight = size.y;
  }

  @Override
  public void onResume() {
    super.onResume();
    startPlaying();
  }

  /* Game play methods */

  void startPlaying() {
    countClicked = countShown = 0;
    layout.removeAllViews();
    showAnEmoji();
  }

  void showAnEmoji() {

    long duration =
        MINIMUM_DURATION + random.nextInt(MAXIMUM_ADDITIONAL_DURATION);

    LayoutParams params = new LayoutParams
        (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    params.leftMargin = random.nextInt(displayWidth) * 3 / 4;
    params.topMargin = random.nextInt(displayHeight) * 5 / 8;

    ImageView emoji = new ImageView(this);
    emoji.setImageResource(R.drawable.emoji_1);
    emoji.setLayoutParams(params);
    emoji.setOnClickListener(this);
    emoji.setVisibility(View.INVISIBLE);

    layout.addView(emoji);

    AlphaAnimation animation = new AlphaAnimation(0.0F, 1.0F);
    animation.setDuration(duration);
    animation.setAnimationListener(this);
    emoji.startAnimation(animation);
  }

  /* OnClickListener method */

  public void onClick(View view) {
    countClicked++;
    ((ImageView) view).setImageResource(R.drawable.emoji_2);
    view.setVisibility(View.VISIBLE);
  }

  /* AnimationListener methods */

  public void onAnimationEnd(Animation animation) {
    if (++countShown < NUMBER_OF_EMOJI) {
      showAnEmoji();
    } else {
      Toast.makeText(this, "Game Over", Toast.LENGTH_LONG).show();
    }
  }

  public void onAnimationRepeat(Animation arg0) {
  }

  public void onAnimationStart(Animation arg0) {
  }

  /* Menu methods */

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.show_scores:
        showScores();
        return true;
      case R.id.play_again:
        startPlaying();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void showScores() {
    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    int highScore = prefs.getInt("highScore", 0);

    if (countClicked > highScore) {
      highScore = countClicked;
      SharedPreferences.Editor editor = prefs.edit();
      editor.putInt("highScore", highScore);
      editor.commit();
    }

    Toast.makeText(this, "Your score: " + countClicked +
        "\nHigh score: " + highScore, Toast.LENGTH_LONG).show();
  }
}
