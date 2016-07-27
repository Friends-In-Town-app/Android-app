package alexsander.com.br.friendsintown;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class CreateAccountActivity extends AppCompatActivity implements OnClickListener {

    private Button saveNewAccountButton;
    private TextView passwordText, nPasswordText, nEmailText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        saveNewAccountButton = (Button) findViewById(R.id.btnCreateNAc);
        saveNewAccountButton.setOnClickListener(this);

        nEmailText = (TextView) findViewById(R.id.edtNEmail);
        passwordText = (TextView) findViewById(R.id.edtPassword);
        nPasswordText = (TextView) findViewById(R.id.edtNPassword);

    }

    public void onClick(View v) {
        Context contexto = getApplicationContext();
        int duracao = Toast.LENGTH_SHORT;

        if (v == saveNewAccountButton){
            if (passwordText != nPasswordText){
                // add toast password nao confere
                Toast toast = Toast.makeText(contexto, "Password não confere", duracao);
                toast.show();

            }
            if (nPasswordText.length() == 0){ //TextView de email está vazio
                // toast digitar email
                Toast toast = Toast.makeText(contexto, "Precisa informar e-mail", duracao);
                toast.show();
            }
        }
    }
}
