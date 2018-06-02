package com.manoranjank.hangman;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

     String[] words = {"genre","animals","clothes","films","entertainment","jobs","summer","halloween","building","room"};
    public static int[] mBodyparts={R.drawable.h0,R.drawable.h1,R.drawable.h2,R.drawable.h3,R.drawable.h4,R.drawable.h5,R.drawable.h6};
   public static  ImageView bodyparts;
    private Random rand;
    String word;
    public static String input;
    public static int check=0;
    AlertDialog.Builder builder;

    public static TextView mClue;
    public static char[] dash;
    private int numParts = 6;
    int randomWordNumber;
    public static   EditText mEditText;
    public static char[] enteredLetters;
    public static TextView mIns,mStore;
    boolean asteriskPrinted;
    public static char userInput;
    int Bestscore;
    Button mButton;

   int triesCount = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences=getSharedPreferences("PREFS",0);
        Bestscore=preferences.getInt("BestScore",50);
        mIns=(TextView) findViewById(R.id.ins);
        mClue=(TextView) findViewById(R.id.dash);
        mEditText=(EditText) findViewById(R.id.textView3);
        mButton=(Button) findViewById(R.id.button);
        mStore=(TextView) findViewById(R.id.store);
        rand = new Random();
        randomWordNumber = rand.nextInt(10);
       word=words[randomWordNumber];
        mIns.setText(word);




        bodyparts = (ImageView) findViewById(R.id.imageView2);

         enteredLetters = new char[word.length()];
         dash=new char[word.length()];



            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                // Check if letter already have been entered bu user before
                if (inEnteredLetters(letter, enteredLetters))
                    dash[i]=letter; // If yes - print it
                else {
                    dash[i]='*';

                    asteriskPrinted = true;
                }
            }
            mClue.setText(String.valueOf(dash));
         mIns.setText("(Guess) Enter a letter in word ");




              mButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      switch (enterLetter(word,enteredLetters)) {
                          case 0:
                              triesCount++;
                              break;
                          case 1:
                              break;
                          case 2:
                              triesCount++;
                              break;
                          case 3:


                              if(triesCount<Bestscore)
                              {
                                  Bestscore=triesCount;
                                  SharedPreferences preferences=getSharedPreferences("PREFS",0);
                                  SharedPreferences.Editor editor=preferences.edit();
                                  editor.putInt("BestScore",Bestscore);
                                  editor.commit();


                              }
                              AlertDialog malert = builder.create();
                              malert.setTitle("You Won!!");
                              malert.setMessage("The word is: "+word+"\n Your Score:"+String.valueOf(triesCount)+"\nBest Score"+String.valueOf(Bestscore));

                              malert.show();



                              break;
                          case 4:
                          {



                              if(triesCount<Bestscore)
                              {
                                  Bestscore=triesCount;
                                  SharedPreferences preferences=getSharedPreferences("PREFS",0);
                                  SharedPreferences.Editor editor=preferences.edit();
                                  editor.putInt("BestScore",Bestscore);
                                  editor.commit();

                              }
                              AlertDialog alert = builder.create();
                              alert.setMessage("The word is: "+word+"\n Your Score:"+String.valueOf(triesCount)+"\nBest Score"+String.valueOf(Bestscore));
                              alert.setTitle("Game Over !!");
                              alert.show();

                          }

                      }
                  }

              });
        builder = new AlertDialog.Builder(this);


               builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.setNegativeButton("Play Again", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent mIntent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(mIntent);
            }
        });






    }


    public static int enterLetter(String word, char[] enteredLetters)    {

       if(!printWord(word, enteredLetters))
       {
           return 3;
       }
        input=String.valueOf(mEditText.getText());
        userInput=Character.toLowerCase(input.charAt(0));
        mStore.setText("Guessed word is :"+String.valueOf(userInput));

        int emptyPosition = findEmptyPosition(enteredLetters);

        if (inEnteredLetters(userInput, enteredLetters)) {
            mIns.setText(userInput + " is already in the word");
            return 2;
        }
        else if (word.contains(String.valueOf(userInput))) {
            enteredLetters[emptyPosition] = userInput;
            printWord(word, enteredLetters);
            mIns.setText("Good !!");
            if(!printWord(word, enteredLetters))
            {
                return 3;
            }

            return 1;
        }
        else {
            mIns.setText(userInput + " is not in the word");
            check++;

           bodyparts.setImageResource(mBodyparts[check]);
           if(check==6)
           {
               return 4;
           }
            return 0;
        }

       //  printWord(word, enteredLetters);
            //return 3;}
    }


    public static boolean printWord(String word, char[] enteredLetters) {

        boolean asteriskPrinted = false;
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (inEnteredLetters(letter, enteredLetters))
                dash[i]=letter;
            else {
                dash[i]='*';

                asteriskPrinted = true;
            }
        }
        mClue.setText(String.valueOf(dash));
        return asteriskPrinted;
    }

    public static boolean inEnteredLetters(char letter, char[] enteredLetters) {
        return new String(enteredLetters).contains(String.valueOf(letter));
    }

    public static int findEmptyPosition(char[] enteredLetters) {
        int i = 0;
        while (enteredLetters[i] != '\u0000') i++;
        return i;
    }
}