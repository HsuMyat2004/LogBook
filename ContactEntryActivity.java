package com.kmd.uog.logbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kmd.uog.logbook.database.DatabaseHelper;
import com.kmd.uog.logbook.database.Contact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ContactEntryActivity extends AppCompatActivity {
    private EditText txtName, txtDate, txtEmail;
    private ImageView userImageView;
    private Button btnChooseImage, btnView, btnSave;

    private DatabaseHelper databaseHelper;

    private Integer id;
    private String selectedImageFilePath; // Store the selected image file path

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_entry);

        txtName = findViewById(R.id.txtName);
        txtDate = findViewById(R.id.txtDate);
        txtEmail = findViewById(R.id.txtEmail);
        userImageView = findViewById(R.id.userImageView);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnView = findViewById(R.id.btnView);
        btnSave = findViewById(R.id.btnSave);

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Button to choose profile image
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }

            // Open the image picker
            private void openImagePicker() {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        // Inside onCreate method of ContactEntryActivity.java
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContactListActivity();
            }

            private void openContactListActivity() {
                Intent intent = new Intent(ContactEntryActivity.this, ContactListActivity.class);
                startActivity(intent);
            }
        });

        // Button to save contact details
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });

        // Retrieve contact details if editing an existing contact
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt(DatabaseHelper.CONTACT_ID);
            txtName.setText(bundle.getString(DatabaseHelper.NAME));
            txtDate.setText(bundle.getString(DatabaseHelper.DATE));
            txtEmail.setText(bundle.getString(DatabaseHelper.EMAIL));
            selectedImageFilePath = bundle.getString(DatabaseHelper.IMAGE_FILE_PATH);
            if (selectedImageFilePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImageFilePath);
                userImageView.setImageBitmap(bitmap);
            }
        }
    }

    // Handle the result from the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            Uri imageUri = data.getData();

            try {
                // Decode and display the selected image
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                userImageView.setImageBitmap(bitmap);

                // Store the selected image's file path with a unique name
                selectedImageFilePath = saveImageToFile(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Save the contact details to the database
    private void saveContact() {
        String name = txtName.getText().toString();
        String date = txtDate.getText().toString();
        String email = txtEmail.getText().toString();

        long result;

        if (id == null) {
            // Insert a new contact
            result = databaseHelper.save(name, date, email, selectedImageFilePath);
        } else {
            // Update an existing contact
            result = databaseHelper.update(id, name, date, email, selectedImageFilePath);
        }

        if (result > 0) {
            new AlertDialog.Builder(ContactEntryActivity.this)
                    .setTitle("Saved")
                    .setMessage("Data Saved to database")
                    .show();
        } else {
            new AlertDialog.Builder(ContactEntryActivity.this)
                    .setTitle("Error")
                    .setMessage("Data does not save")
                    .show();
        }
    }

    // Save the selected image to a file with a unique name and return the file path
    private String saveImageToFile(Bitmap bitmap) {
        try {
            // Create a unique file name using UUID
            String uniqueFileName = UUID.randomUUID().toString() + ".png";

            // Create a file in the app's cache directory
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            File imageFile = new File(cachePath, uniqueFileName);

            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
