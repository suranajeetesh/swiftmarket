package com.swiftmarket.utils.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swiftmarket.data.LocalProductsModel;

import java.util.ArrayList;

public class FirebaseDBHelper {

    private static FirebaseDBHelper instance;
    private DatabaseReference databaseReference;
    private FirebaseDBHelperInterface helperInterface;

    // Private constructor to enforce singleton pattern
    private FirebaseDBHelper() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true); // Enable offline persistence if needed
        databaseReference = firebaseDatabase.getReference("product");
    }

    // Set the listener for database events
    public void setListener(FirebaseDBHelperInterface helperInterface){
        this.helperInterface=helperInterface;
    }

    // Singleton instance creation
    public static synchronized FirebaseDBHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDBHelper();
        }
        return instance;
    }

    // Get the database reference
    public DatabaseReference getReference() {
        return databaseReference;
    }


    // write data to a specific node
    public void writeData(String node, Object data) {
        DatabaseReference nodeReference = databaseReference.child(node);
        nodeReference.setValue(data);
    }

    // Update data in a specific node
    public void updateData(String nodeKey, LocalProductsModel localProductsModel) {
        DatabaseReference productsReference = databaseReference.child(nodeKey);
        productsReference.setValue(localProductsModel);
    }

    // Delete data from a specific node
    public void deleteData(Context context,String nodeKey) {
        DatabaseReference productsReference = databaseReference.child(nodeKey);
        // Remove the node with the specified key
        productsReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    // Read data from the database
    public void readData(){
        // Read from the "product" node
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<LocalProductsModel> productList = new ArrayList<>();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    // Assuming LocalProductsModel has a constructor that takes DataSnapshot as a parameter
                    String productKey = productSnapshot.getKey();
                    LocalProductsModel product = productSnapshot.getValue(LocalProductsModel.class);
                    assert product != null;
                    product.setNodeKey(productKey);
                    productList.add(product);
                }

                if (helperInterface!=null){
                    helperInterface.readData(productList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error reading data: " + databaseError.getMessage());
                if (helperInterface!=null){
                    helperInterface.onError(databaseError.getMessage());
                }
            }
        });
    }


    // Listener interface for Firebase database events
    public interface FirebaseDBHelperInterface {
        // Default method for reading data
        default void readData(ArrayList<LocalProductsModel> productList) {}

        // Default method for handling errors
        default void onError(String message) {}
    }
}
