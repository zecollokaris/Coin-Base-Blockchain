package com.zecollokaris.cryptocurrency;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

//import java.util.HashMap;
import android.app.ProgressDialog;

public class Tab2 extends Fragment {
    Button mRegisterBtn;
    EditText username, email, password;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          View view=inflater.inflate(R.layout.fragment_tab2, container, false);
          createDialog();
          mRegisterBtn=(Button) view.findViewById(R.id.registerBtn);
          username = view.findViewById(R.id.username);
          email = view.findViewById(R.id.email);
          password = view.findViewById(R.id.password);
          auth = FirebaseAuth.getInstance();
          mRegisterBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String txt_username = username.getText().toString();
                  String txt_email = email.getText().toString().trim();
                  String txt_password = password.getText().toString();


                  if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                      Toast.makeText(getContext(), "All Fields are required!",Toast.LENGTH_SHORT).show();
                  } else if (txt_password.length() < 6) {
                      Toast.makeText(getContext(), "Password must be atleast 6 characters!",Toast.LENGTH_SHORT).show();
                  } else {
                      dialog.show();
                      register(txt_username, txt_email, txt_password);
                  }
              }
          });
          return view;
    }

    private void register(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(),new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if (task.isSuccessful()){
                   // DD! for debug Toast.makeText(getContext(), "Your Account Has Been Created. You Can Now Login!",Toast.LENGTH_SHORT).show();

                    // This piece of text adds the UserName to the new user's entry Firebase
                    // FirebaseUser firebaseUser = auth.getCurrentUser();  // zecollokaris
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();  //DD
                    assert firebaseUser != null;

            /*      String userid = firebaseUser.getUid();  // zecollokaris Not Work Right
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    // DD: NOT WORK TRUE: userid is good; username is good BUT setValue(hashMap) return false ALWAYS
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            */      // DD: Another Code Work right:
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build();
                    firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // DD: You old text now work good
                            if (task.isSuccessful())
                                Toast.makeText(getContext(), "Successful Your Account Has Been Created. You Can Now Login!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "NOT Successful but Your Account Has Been Created. You Can Now Login! ", Toast.LENGTH_SHORT).show(); //ДО
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "You can't register with this email or password",Toast.LENGTH_SHORT).show();
                }
            }
        });  // end createUserWithEmailAndPassword
    }  // end register

    public void createDialog(){
        dialog=new ProgressDialog(getContext());
        dialog.setTitle("Registering User");
        dialog.setMessage("Loading... Please Wait");
    }


}
