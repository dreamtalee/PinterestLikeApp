package com.dreamtale.pintrestlike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends Activity
{
    private Button button1;
    private Button button2;
    
    private View.OnClickListener choiceListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
            case R.id.gridstyle:
                UIConfig.getInstance().setMode(UIConfig.MODE_GRID_VIEW);
                break;
                
            case R.id.pintersetstyle:
                UIConfig.getInstance().setMode(UIConfig.MODE_PINTEREST_VIEW);
                break;
            default:
                break;
            }
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        initlizeViews();
    }
    
    private void initlizeViews()
    {
        button1 = (Button)findViewById(R.id.gridstyle);
        button2 = (Button)findViewById(R.id.pintersetstyle);
        
        button1.setOnClickListener(choiceListener);
        button2.setOnClickListener(choiceListener);
    }
}
