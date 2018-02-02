package com.example.erick.tarea01;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.erick.tarea01.databinding.ActivityMainBinding;
import com.example.erick.tarea01.dto.Student;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "Debug " + ActivityMain.class.getSimpleName() + ": ";
    private String FEMALE = "female";
    private String MALE = "male";
    private int NO_BOOK = -1;

    private int mSelectedStudy;
    private int mSelectedBook;

    private ActivityMainBinding mBinding;
    private ArrayAdapter<String> mBooksAdapter;
    private ArrayAdapter<String> mStudiesAdapter;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.btnCleanMainActivity.setOnClickListener(this);
        mBinding.spStudiesMainActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedStudy = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBinding.actvBookMainActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedBook = position;
            }
        });

        setAdapters();
        if (savedInstanceState != null) {
            initView(savedInstanceState);
        } else {
            initView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.ic_save:
                makeToast();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick");
        switch (v.getId()) {
            case R.id.btn_clean_main_activity:
                showConfirmation();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putString("name", mBinding.etNameMainActivity.getText().toString());
        outState.putString("phone", mBinding.etPhoneMainActivity.getText().toString());
        outState.putInt("studies", mSelectedStudy);
        outState.putString("gender", mBinding.rbFemaleMainActivity.isChecked() ? FEMALE : MALE);
        outState.putInt("book", mSelectedBook);
        outState.putBoolean("sports", mBinding.chbSportsMainActivity.isChecked());
    }

    private void setAdapters() {
        Log.d(TAG, "setAdapters");
        String[] books = getResources().getStringArray(R.array.books);
        mBooksAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, books);
        mBinding.actvBookMainActivity.setAdapter(mBooksAdapter);
        String[] studies = getResources().getStringArray(R.array.studies);
        mStudiesAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, studies);
        mBinding.spStudiesMainActivity.setAdapter(mStudiesAdapter);
    }

    private void initView() {
        Log.d(TAG, "initView");
        mBinding.rbFemaleMainActivity.toggle();

        mSelectedBook = NO_BOOK; // Selected element is default
        mBinding.spStudiesMainActivity.setSelection(0); // Select first element
    }

    private void initView(Bundle savedInstanceState) {
        Log.d(TAG, "initView (Bundle)");
        final String gender = savedInstanceState.getString("gender");
        mSelectedStudy = savedInstanceState.getInt("studies");
        mSelectedBook = savedInstanceState.getInt("book");
        mBinding.etNameMainActivity.setText(savedInstanceState.getString("name"));
        mBinding.etPhoneMainActivity.setText(savedInstanceState.getString("phone"));
        mBinding.spStudiesMainActivity.setSelection(savedInstanceState.getInt("studies"));
        if (gender != null && gender.equals(FEMALE))
            if (!mBinding.rbFemaleMainActivity.isChecked())
                mBinding.rbFemaleMainActivity.toggle();
        else
            if (!mBinding.rbMaleMainActivity.isChecked())
                mBinding.rbMaleMainActivity.toggle();
        if (mSelectedBook != NO_BOOK) {
            mBinding.actvBookMainActivity.setText(mBooksAdapter.getItem(savedInstanceState.getInt("book")));
        }

        mBinding.chbSportsMainActivity.setChecked(savedInstanceState.getBoolean("sports"));
    }

    private void showConfirmation() {
        Log.d(TAG, "showConfirmation");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea limpiar el contenido?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cleanView();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void makeToast() {
        Log.d(TAG, "makeToast");

        if (mBinding.etNameMainActivity.getText().toString().length() == 0) {
            mToast = Toast.makeText(this, "El campo de nombre no puede estar vacio", Toast.LENGTH_LONG);
            mToast.show();
            return;
        }
        if (mBinding.etPhoneMainActivity.getText().toString().length() == 0) {
            mToast = Toast.makeText(this, "El campo de telefono no puede estar vacio", Toast.LENGTH_LONG);
            mToast.show();
            return;
        }

        if (mToast != null && mToast.getView().isShown())
            return;

        Student student = new Student(
                mBinding.etNameMainActivity.getText().toString(),
                mBinding.etPhoneMainActivity.getText().toString(),
                mStudiesAdapter.getItem(mSelectedStudy) == null ? "" : mStudiesAdapter.getItem(mSelectedStudy),
                mBinding.rbFemaleMainActivity.isChecked() ? FEMALE : MALE,
                mBinding.chbSportsMainActivity.isChecked(),
                mSelectedBook == NO_BOOK ? null : mBooksAdapter.getItem(mSelectedBook)
        );
        Toast.makeText(this, student.toString(), Toast.LENGTH_LONG).show();
        cleanView();
    }

    private void cleanView() {
        Log.d(TAG, "cleanView");
        mBinding.etNameMainActivity.setText("");
        mBinding.etPhoneMainActivity.setText("");
        if (!mBinding.rbFemaleMainActivity.isChecked())
            mBinding.rbFemaleMainActivity.toggle();
        mBinding.spStudiesMainActivity.setSelection(0);
        mBinding.actvBookMainActivity.setText("");
        mBinding.etNameMainActivity.requestFocus();
        mSelectedBook = NO_BOOK;
    }
}
