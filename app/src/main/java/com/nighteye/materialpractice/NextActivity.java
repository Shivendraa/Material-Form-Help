package com.nighteye.materialpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class NextActivity extends AppCompatActivity {

    private TextView name, number, dropdownval, dateval;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        dropdownval = findViewById(R.id.dropdownvalue);
        dateval = findViewById(R.id.date);
        imageView = findViewById(R.id.imageRecvd);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        if(!bundle.isEmpty()){
            name.setText(bundle.getString("name"));
            number.setText(bundle.getString("number"));
            dropdownval.setText(bundle.getString("dropdown"));
            dateval.setText(bundle.getString("date"));
            byte[] imgBytes = bundle.getByteArray("imageBytes");
            assert imgBytes != null;
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            imageView.setImageBitmap(bitmap);
        }
    }
}