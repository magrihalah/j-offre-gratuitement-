package com.example.offrirgratuitement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.EventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FeedActivity extends AppCompatActivity {

    private Button giveButton;
    private Button disconnectButton;
    private Button personalButton;

    /// IMAD //

    ListView listView;
    String mTitle[] = {"T-Shirt","Sac","Livres","Manteau",""};
    String mDescription[] = {"Très bonne qualité","étanche","Développement personnel.","Couleur : beige foncé",""};
    String mNum[] = {"0612345678","0601020304","0722334455","0612345678",""};
    int images[] = {R.drawable.tshirt,R.drawable.sac,R.drawable.livre,R.drawable.manteau,R.drawable.charity2};

    // IMAD //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        listView = findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter (this, mTitle, mDescription, images, mNum);
        listView.setAdapter(adapter);

        giveButton = (Button) findViewById(R.id.feed_give_btn);
        disconnectButton = (Button) findViewById(R.id.feed_disconnect_btn);
        personalButton = (Button) findViewById(R.id.feel_personal_btn);

        giveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeedActivity.this, ProductActivity.class);
                startActivity(intent);

            }
        });
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FeedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        personalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });



    }
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];
        String rNum[];

        MyAdapter (Context c, String title[], String description[], int imgs[], String num[] ) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;
            this.rNum = num ;

        }

        @Nonnull
        @Override
        public View getView(int position, @Nullable View convertView, @Nonnull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);
            TextView myNum = row.findViewById(R.id.textView3);


            // now set our resources on views
            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);
            myNum.setText(rNum[position]);




            return row;
        }
    }
}