package alexsander.com.br.friendsintown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccountButton = (Button) findViewById(R.id.btnCreateAc);
        createAccountButton.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v == createAccountButton){
            startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));

        }
}


        }
