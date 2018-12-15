package de.valeapps.teamrandomizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class MainActivity extends AppCompatActivity {
    ItemAdapter adapter;
    ArrayList<String> names = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeRecyclerView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddNameAlert());

        initializeButton();
    }

    private void initializeButton() {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            //when randomize Button is clicked
            showTeamSizeAlert();
        });
    }

    private void initializeRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView mRecyclerView = findViewById(R.id.reclerview);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(this, names);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        SimpleTouchCallback simpleTouchCallback = new SimpleTouchCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void showAddNameAlert() {
        //set up Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Name: ");

        // Set up the input
        EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        builder.setView(input);
        builder.setCancelable(false);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            names.add(input.getText().toString());
            adapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            dialog.cancel();
        });
        builder.show();
        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void showTeamSizeAlert() {
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(2);
        numberPicker.setWrapSelectorWheel(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set the Team Size");
        builder.setView(numberPicker);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", (dialog, which) -> randomize(String.valueOf(numberPicker.getValue())));
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public void randomize(String teamSizeString) {
        int teamSizeInt = Integer.parseInt(teamSizeString);
        if (names.size() % teamSizeInt == 0) {
            Collections.shuffle(names);

            int CHUNK_SIZE = 2;
            List<String> chunk;
            int columns = names.size() / teamSizeInt;
            int rows = teamSizeInt;
            String[][] newArray = new String[columns][rows];
            for (int i = 0, k = 0; i < names.size(); i += CHUNK_SIZE, k++) {
                chunk = names.subList(i, Math.min(i + CHUNK_SIZE, names.size()));
                for(int j = 0; j < chunk.size(); j++) {
                    newArray[k][j] = chunk.get(j);
                }
            }
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", newArray);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Geht nicht auf!", Toast.LENGTH_LONG).show();
        }
    }
}
