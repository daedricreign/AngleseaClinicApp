package com.angleseahospital.admin.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.angleseahospital.admin.R;
import com.angleseahospital.admin.classes.RosterView;
import com.angleseahospital.admin.firestore.Constants;
import com.angleseahospital.admin.firestore.Nurse;

public class AddEditNurse extends Fragment {
    private View v;

    private EditText etxt_firstname;
    private EditText etxt_lastname;
    private EditText etxt_pin;

    public Nurse nurse;

    private RosterView rosterView;

    private String firstname;
    private String lastname;
    private String pin;

    private boolean editing = false;

    public AddEditNurse() {
        nurse = new Nurse();
        nurse.generateID();
    }

    public AddEditNurse(Nurse nurse) {
        this.nurse = nurse;
        editing = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return v = inflater.inflate(R.layout.frag_addeditnurse, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etxt_firstname = v.findViewById(R.id.etxt_firstname);
        etxt_lastname = v.findViewById(R.id.etxt_lastname);
        etxt_pin = v.findViewById(R.id.etxt_pin);

        rosterView = new RosterView(v, nurse);

        if (nurse == null || !editing)
            return;

        etxt_firstname.setText(nurse.firstName);
        etxt_lastname.setText(nurse.lastName);
        etxt_pin.setText(nurse.pin);

        if (!nurse.roster.isBuilt())
            nurse.roster.build(task -> rosterView.displayRoster());
        else
            rosterView.displayRoster();
    }


    @Override
    public void onDetach() {
        super.onDetach();

        if (!verifyInputs()) {
            //TODO: Add visual indication that nurse changes didn't save
            return;
        }

        nurse.firstName = firstname;
        nurse.lastName = lastname;
        nurse.pin = pin;


        if (nurse.roster.build(v))
            nurse.updateDatabase(editing).continueWith(task -> {
                if (task.isSuccessful())
                    Log.d("UpdateDatabaseContinuation", "Database Updated!");
                else {
                    Log.d("UpdateDatabaseContinuation", "Database Failed to Update");
                    Log.e("UpdateDatabaseContinuation", task.getException().getMessage());
                }
                return null;
            });
    }

    private boolean verifyInputs() {
        String firstname = etxt_firstname.getText().toString().trim();
        if (firstname.equals(""))
            return false;

        String lastname = etxt_lastname.getText().toString().trim();
        if (lastname.equals(""))
            return false;

        String pin = etxt_pin.getText().toString().trim();
        if (pin.equals(""))
            return false;
        if (pin.length() != Constants.NURSE_PIN_LENGTH)
            return false;

        this.firstname = firstname;
        this.lastname = lastname;
        this.pin = pin;

        return true;
    }
}