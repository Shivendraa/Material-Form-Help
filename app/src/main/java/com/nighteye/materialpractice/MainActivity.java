package com.nighteye.materialpractice;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Keep Variables Private
    private TextInputLayout textString, textNumber;
    private EditText eDob,iPic, iDoc;
    private Button button;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath1, filePath2;
    private Bitmap bitmap;
    private static final int PICK_PDF_REQUEST = 134;
    File file;
    private byte[] docBytes;
    private RadioGroup radioGroup;
    String radioChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Basic text fetch
        textString = findViewById(R.id.textField);
        textNumber = findViewById(R.id.textField2);
        button = findViewById(R.id.button);
        radioGroup = findViewById(R.id.radioGroup);

        //DatePicker Code
        eDob = findViewById(R.id.eDob);
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        eDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = day+"/"+(month+1)+"/"+year;
                        if(!date.isEmpty())
                            eDob.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        //DropDown Code
        String[] COUNTRIES = new String[] {"Japan", "India", "Germany", "Uzbekistan"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, COUNTRIES);
        final AutoCompleteTextView dropDownMenu = findViewById(R.id.drop_down);
        dropDownMenu.setAdapter(adapter);

        //ImagePicker Code
        iPic = findViewById(R.id.iPic);
        iPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select an Image"),PICK_IMAGE_REQUEST);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {


                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup,@IdRes int i) {
                        switch (i){
                            case R.id.radio_button_1:
                                radioChoice = "Radio Button 1";
                                break;
                            case R.id.radio_button_2:
                                radioChoice = "Radio Button 2";
                                break;
                            default:
                                radioChoice = "Not Selected";
                        }
                    }
                });

                String name = Objects.requireNonNull(textString.getEditText()).getText().toString();
                String number = Objects.requireNonNull(textNumber.getEditText()).getText().toString();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bitmap.recycle();



                String date = eDob.getText().toString();
                String dropDownVal = dropDownMenu.getText().toString();
                Intent intent = new Intent(MainActivity.this, NextActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("number",number);
                intent.putExtra("dropdown",dropDownVal);
                intent.putExtra("date",date);
                intent.putExtra("imageBytes",byteArray);
                intent.putExtra("radioChoice",radioChoice);
                startActivity(intent);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && Objects.requireNonNull(data).getData() != null) {
            filePath1 = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath1);
                iPic.setText(filePath1.getEncodedPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && Objects.requireNonNull(data).getData() != null) {
            try {
                filePath2 = data.getData();
                file = new File(Objects.requireNonNull(Objects.requireNonNull(filePath2).getPath()));
                iDoc.setText(filePath2.getEncodedPath());
                docBytes = Files.readAllBytes(file.toPath());
            } catch (Exception E){
                E.printStackTrace();
            }
        }
    }
}