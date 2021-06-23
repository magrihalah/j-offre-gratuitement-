package com.example.offrirgratuitement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ////// DECLARATION DES VARIABLES //////

    private Button createAccountButton ;
    private EditText inputName , inputPhoneNumber , inputPassword, inputEmail ;


    ////// FIREBASE //////

    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ////// ASSOCIATION DES VARIABLES A LEUR ID //////

        createAccountButton = (Button) findViewById(R.id.register_btn);
        inputName = (EditText) findViewById(R.id.register_username_input);
        inputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        inputEmail = (EditText) findViewById(R.id.register_email_input);
        inputPassword = (EditText) findViewById(R.id.register_password_input);


        ////// FIREBASE //////

        mAuth = FirebaseAuth.getInstance();


        ////// CE QUI SE PASSE QD JE CLIQUE SUR S'INSCRIRE //////

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }
    private void createAccount ()
    {
        ////// COMMANDE QUI OBTIENT LE STRING DES VARIABLES //////

        String name = inputName.getText().toString().trim();
        String phoneNumber = inputPhoneNumber.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        ////// VERIFICATION SI LES CHAMPS NE SONT PAS EMPTY //////

        if (TextUtils.isEmpty(name))
        {
            inputName.setError("Saisissez votre nom !");
            inputName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phoneNumber))
        {
            inputPhoneNumber.setError("Saisissez votre numéro de téléphone !");
            inputPhoneNumber.requestFocus();
            return;
        }

            ////// NUMERO DE TELEPHONE NON VALIDE //////

        if (phoneNumber.length() < 10 || phoneNumber.length() > 14) {
            inputPhoneNumber.setError("Numéro de téléphone non valide !");
            inputPhoneNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Saisissez votre adresse mail !");
            inputEmail.requestFocus();
            return;
        }
                ////// ADRESSE MAIL NON VALIDE //////

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Adresse mail non valide !");
            inputEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password))
        {
            inputPassword.setError("Saisissez votre mot de passe  !");
            inputPassword.requestFocus();
            return;
        }

                ////// VERIFICATION MOT DE PASSE EST LONG DE 10 CARACTERES //////

        if (password.length() < 10) {
            inputPassword.setError("Votre mot de passe doit contenir au moins 10 caractères !");
            inputPassword.requestFocus();
            return;
        }
        if (!(phoneNumber.length() > 6 || phoneNumber.length() < 13)) {
            inputPhoneNumber.setError("Numéro de téléphone non valide !");
            inputPhoneNumber.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, phoneNumber, email);

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", user.email);
                            userData.put("name", user.fullName);
                            userData.put("password", password);
                            userData.put("phoneNumber", user.phoneNumber);
                            fStore.collection("Users").add(userData);
                            startActivity(new Intent(getApplicationContext(),JoinActivity.class));
                        }else {
                            Toast.makeText(RegisterActivity.this, "Inscription échoué ! Veuillez réessayer.", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

}