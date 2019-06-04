package com.example.asus.mobiletracker.userSide.googleApi.models;

import android.support.v7.app.AppCompatActivity;

public class ListNews extends AppCompatActivity {

//    private FirebaseAuth mAuth;
//    private DatabaseReference myRef;
//    private List<String> DiscrNews;
//
//    ListView ListOfNews;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list_news);
//
//
//        ListOfNews = (ListView)findViewById(R.id.discr_for_news);
//        myRef = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user = mAuth.getInstance().getCurrentUser();
//
//        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
//                DiscrNews = dataSnapshot.child("News").getValue(t);
//
//                updateUI();
//            }
//
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }
//    private void updateUI() {
//        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, DiscrNews);
//
//        ListOfNews.setAdapter(adapter);
//    }
}
