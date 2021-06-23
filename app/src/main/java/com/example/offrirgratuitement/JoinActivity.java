package com.example.offrirgratuitement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class JoinActivity extends AppCompatActivity {

    private Button joinAccountButton ;
    private EditText inputEmail , inputPassword ;
    private Button resetPassword;

    ////// FIREBASE //////
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        joinAccountButton= (Button) findViewById(R.id.login_btn);
        inputEmail = (EditText) findViewById(R.id.login_mail_input);
        inputPassword = (EditText) findViewById(R.id.login_password_input);
        resetPassword = (Button) findViewById(R.id.join_forgot_password);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

       joinAccountButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               userLogin();
           }
       });

        /*
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(JoinActivity.this, "Vous êtes maintenant connectés !", Toast.LENGTH_SHORT).show();
                        JoinActivity.this.startActivity(new Intent(JoinActivity.this.getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(JoinActivity.this, "Erreur !! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }); */

    }
    public void userLogin() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        ////// VERIFIER SI LES CHAMPS NE SONT PAS EMPTY //////

        if (email.isEmpty()) {
            inputEmail.setError("Saisissez votre adresse mail !");
            inputEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            inputPassword.setError("Saisissez votre mot de passe !");
            inputPassword.requestFocus();
            return;
        }

        ////// VERIFIER SI L'EMAIL EST VALIDE //////

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Adresse mail non valide !");
            inputEmail.requestFocus();
            return;
        }

        ////// VERIFIER SI LE MOT DE PASSE EST VALIDE //////

        if (password.length() < 10) {
            inputPassword.setError("Votre mot de passe doit contenir au moins 10 caractères !");
            inputPassword.requestFocus();
            return;
        }

        fAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        Toast.makeText(JoinActivity.this , "Vous êtes maintenant connectés !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(JoinActivity.this, FeedActivity.class);
                        startActivity(intent);
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(JoinActivity.this , "Vérifiez votre boite mail puis ressayez ! " , Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(JoinActivity.this , "Erreur !" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
