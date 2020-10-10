package com.nighteye.materialpractice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Keep Variables Private
    private TextInputLayout textString, textNumber;
    private EditText eDob,iPic;
    private Button button;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath1;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Basic text fetch
        textString = findViewById(R.id.textField);
        textNumber = findViewById(R.id.textField2);
        button = findViewById(R.id.button);

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
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select an Image"),PICK_IMAGE_REQUEST);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            filePath1 = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath1);
                iPic.setText(filePath1.getEncodedPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}