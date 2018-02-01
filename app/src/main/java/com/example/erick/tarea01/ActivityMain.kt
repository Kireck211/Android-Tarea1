package com.example.erick.tarea01

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.example.erick.tarea01.databinding.ActivityMainBinding
import com.example.erick.tarea01.dto.Student

class ActivityMain : AppCompatActivity(), View.OnClickListener {
    private val TAG = "Debug " + ActivityMain::class.java.simpleName
    private val FEMALE = "female"
    private val MALE = "male"
    private val NO_BOOK = -1

    private var mSelectedStudy: Int = 0
    private var mSelectedBook: Int = 0

    private var mBinding: ActivityMainBinding? = null
    private var mBooksAdapter: ArrayAdapter<String>? = null
    private var mStudiesAdapter: ArrayAdapter<String>? = null
    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")

        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding!!.btnCleanMainActivity.setOnClickListener(this)
        mBinding!!.spStudiesMainActivity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mSelectedStudy = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        mBinding!!.actvBookMainActivity.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> mSelectedBook = position }

        setAdapters()
        if (savedInstanceState != null) {
            initView(savedInstanceState)
        } else {
            initView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_save -> showConfirmation()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        Log.d(TAG, "onClick")
        when (v.id) {
            R.id.btn_clean_main_activity -> showConfirmation()
        }
    }

    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putString("name", mBinding!!.etNameMainActivity.text.toString())
        outState.putString("phone", mBinding!!.etPhoneMainActivity.text.toString())
        outState.putInt("studies", mSelectedStudy)
        outState.putString("gender", if (mBinding!!.rbFemaleMainActivity.isChecked) FEMALE else MALE)
        outState.putInt("book", mSelectedBook)
        outState.putBoolean("sports", mBinding!!.chbSportsMainActivity.isChecked)
    }

    private fun setAdapters() {
        val books = resources.getStringArray(R.array.books)
        mBooksAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, books)
        mBinding!!.actvBookMainActivity.setAdapter<ArrayAdapter<String>>(mBooksAdapter)
        val studies = resources.getStringArray(R.array.studies)
        mStudiesAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, studies)
        mBinding!!.spStudiesMainActivity.adapter = mStudiesAdapter
    }

    private fun initView() {
        Log.d(TAG, "initView")
        mBinding!!.rbFemaleMainActivity.toggle()

        mSelectedBook = NO_BOOK // Selected element is default
        mBinding!!.spStudiesMainActivity.setSelection(0) // Select first element
    }

    private fun initView(savedInstanceState: Bundle) {
        Log.d(TAG, "initView (Bundle)")
        val gender = savedInstanceState.getString("gender")
        mSelectedStudy = savedInstanceState.getInt("studies")
        mSelectedBook = savedInstanceState.getInt("book")
        mBinding!!.etNameMainActivity.setText(savedInstanceState.getString("name"))
        mBinding!!.etPhoneMainActivity.setText(savedInstanceState.getString("phone"))
        mBinding!!.spStudiesMainActivity.setSelection(savedInstanceState.getInt("studies"))
        if (gender != null && gender == FEMALE)
            if (!mBinding!!.rbFemaleMainActivity.isChecked)
                mBinding!!.rbFemaleMainActivity.toggle()
            else if (!mBinding!!.rbMaleMainActivity.isChecked)
                mBinding!!.rbMaleMainActivity.toggle()
        if (mSelectedBook != NO_BOOK) {
            mBinding!!.actvBookMainActivity.setText(mBooksAdapter!!.getItem(savedInstanceState.getInt("book")))
        }

        mBinding!!.chbSportsMainActivity.isChecked = savedInstanceState.getBoolean("sports")
    }

    private fun showConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Desea limpiar el contenido?")
                .setPositiveButton("OK") { dialog, which -> makeToast() }
                .setNegativeButton("CANCELAR") { dialog, which -> }
        builder.create()
    }

    private fun makeToast() {
        Log.d(TAG, "makeToast")
        if (mToast != null && mToast!!.view.isShown)
            return

        if (mBinding!!.etNameMainActivity.text.toString().length == 0) {
            mToast = Toast.makeText(this, "El campo de nombre no puede estar vacio", Toast.LENGTH_LONG)
            mToast!!.show()
            return
        }
        if (mBinding!!.etPhoneMainActivity.text.toString().length == 0) {
            mToast = Toast.makeText(this, "El campo de telefono no puede estar vacio", Toast.LENGTH_LONG)
            mToast!!.show()
            return
        }

        val student = Student(
                mBinding!!.etNameMainActivity.text.toString(),
                mBinding!!.etPhoneMainActivity.text.toString(),
                if (mStudiesAdapter!!.getItem(mSelectedStudy) == null) "" else mStudiesAdapter!!.getItem(mSelectedStudy),
                if (mBinding!!.rbFemaleMainActivity.isChecked) FEMALE else MALE,
                mBinding!!.chbSportsMainActivity.isChecked,
                if (mSelectedBook == NO_BOOK) null else mBooksAdapter!!.getItem(mSelectedBook)
        )
        Toast.makeText(this, student.toString(), Toast.LENGTH_LONG).show()
        cleanView()
    }

    private fun cleanView() {
        Log.d(TAG, "cleanView")
        mBinding!!.etNameMainActivity.setText("")
        mBinding!!.etPhoneMainActivity.setText("")
        if (!mBinding!!.rbFemaleMainActivity.isChecked)
            mBinding!!.rbFemaleMainActivity.toggle()
        mBinding!!.spStudiesMainActivity.setSelection(0)
        mBinding!!.actvBookMainActivity.setText("")
        mBinding!!.etNameMainActivity.requestFocus()
        mSelectedBook = NO_BOOK
    }
}
