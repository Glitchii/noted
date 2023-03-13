package com.example.noted;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class DiceRollerActivity extends GlobalAppCompatActivity {
    TextView rolledText;
    ImageView emptyDice, dice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.configure(R.layout.activity_dice_roller);

        emptyDice = findViewById(R.id.emptyDice);
        rolledText = findViewById(R.id.rolled);
        dice = findViewById(R.id.dice);

        findViewById(R.id.rollButton).setOnClickListener(v -> roll());
    }

    /**
     * Picks a number and performs transition animations
     */
    private void roll() {
        animateIn(emptyDice, 200);
        animateOut(dice);
        animateOut(rolledText);

        // int randomNumber = (int) Math.ceil(Math.random() * 6);
        int randomNumber = new Random().nextInt(6) + 1;

        new Handler().postDelayed(() -> {
            switch (randomNumber) {
                case 1:
                    dice.setImageResource(R.drawable.dice1);
                    break;
                case 2:
                    dice.setImageResource(R.drawable.dice2);
                    break;
                case 3:
                    dice.setImageResource(R.drawable.dice3);
                    break;
                case 4:
                    dice.setImageResource(R.drawable.dice4);
                    break;
                case 5:
                    dice.setImageResource(R.drawable.dice5);
                    break;
                case 6:
                    dice.setImageResource(R.drawable.dice6);
                    break;
            }

            rolledText.setText(Integer.toString(randomNumber));
            animateIn(dice);
            animateOut(emptyDice);
            animateIn(rolledText, .6f);
        }, 500);
    }

}