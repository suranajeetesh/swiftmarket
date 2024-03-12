package com.swiftmarket.utils.helper;


import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.swiftmarket.utils.Constant;

public class FirebaseAuthHelper {
    private FirebaseAuth mAuth;
    private Context context;
    private FirebaseAuthHelperListener firebaseAuthHelperListener;
    private UserDatabaseHandler userDatabaseHandler;
    // Constructor
    public FirebaseAuthHelper(Context context, FirebaseAuthHelperListener firebaseAuthHelperListener) {
        Preferences.getPreferences(context);
        mAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.firebaseAuthHelperListener = firebaseAuthHelperListener;
        this.userDatabaseHandler= new UserDatabaseHandler(context);
    }

    // Perform login with email and password
    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Preferences.writeString(context, Constant.KEY_EMAIL,email);
                    Preferences.writeString(context, Constant.KEY_PASSWORD,password);
                    Preferences.writeString(context, Constant.KEY_USER_UID,task.getResult().getUser().getUid());
                    userDatabaseHandler.insertUser(email,password,"");
                    firebaseAuthHelperListener.onLogin();
                } else {
                    firebaseAuthHelperListener.onError(task.getException().getMessage().toString());
                    // Toast.makeText(context, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Perform user registration
    public void onSingUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Preferences.writeString(context, Constant.KEY_EMAIL,email);
                    Preferences.writeString(context, Constant.KEY_PASSWORD,password);
                    Preferences.writeString(context, Constant.KEY_USER_UID,task.getResult().getUser().getUid());
                    userDatabaseHandler.insertUser(email,password,"");
                    firebaseAuthHelperListener.onSignUp();
                } else {
                    firebaseAuthHelperListener.onError(task.getException().getMessage());
                }
            }
        });
    }

    // Handle password reset
    public void onForgotPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseAuthHelperListener.onForgotPassword();
                }else {
                    firebaseAuthHelperListener.onError(task.getException().getMessage());
                }
            }
        });
    }

    // Listener interface for Firebase authentication events
    public interface FirebaseAuthHelperListener {

        // Default login event
        default void onLogin() {
        }

        // Default signup event
        default void onSignUp() {
        }

        // Default forgot password event
        default void onForgotPassword() {
        }

        // Handle errors during authentication
        void onError(String message);
    }
}
