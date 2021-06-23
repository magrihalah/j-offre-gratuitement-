package com.example.offrirgratuitement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText inputMail;
    private Button resetPasswordBtn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputMail = (EditText) findViewById(R.id.forgot_password_mail_input);
        resetPasswordBtn = (Button) findViewById(R.id.forgot_password_btn);

        auth = FirebaseAuth.getInstance();

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = inputMail.getText().toString().trim();

        if (email.isEmpty()) {
            inputMail.setError("Saisissez votre adresse mail !");
            inputMail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputMail.setError("Adresse mail invalide !");
            inputMail.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener< Void >() {
            @Override
            public void onComplete(@NonNull Task< Void > task) {

                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Vérifiez votre boite mail puis réessayez !" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, JoinActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ForgotPasswordActivity.this , "Erreur. Veuillez réessayer !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}