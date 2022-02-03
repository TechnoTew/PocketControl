package Pages;

import android.app.AlertDialog;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.EditText;

import com.example.pocketcontrol.AdHandler;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.KeyboardManager;
import com.example.pocketcontrol.R;
import com.example.pocketcontrol.SharedPreferenceHandler;

public class Settings extends Fragment {

    private DatabaseHandler db;
    private SharedPreferenceHandler sph;
    private Button resetAllButton;
    private Button resetAllItemsButton;
    private EditText editNameTextBox;
    private Button saveSettingButton;

    public Settings() {
        super(R.layout.fragment_settings);
    }

    private AlertDialog alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Name Input Box cannot be empty!").setMessage("You must input a name!");


        builder.setPositiveButton("Ok", (dialog, id) -> {
            // User clicked OK button
        });

        AlertDialog dialog = builder.create();

        return dialog;
    }

    public AlertDialog successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Name Successfully changed!")
                .setMessage("Redirecting to Overview page...");

        builder.setPositiveButton("Ok", (dialog, id) -> {
            // User clicked OK button
        });
        AlertDialog dialog = builder.create();

        return dialog;
    }

    public void onStart() {
        super.onStart();

        // roll a chance from 1 to 10 to show an ad
        AdHandler adHandler = new AdHandler(this.getActivity());
        adHandler.showAdAtRandom(10);

        db = new DatabaseHandler(getContext());
        sph = new SharedPreferenceHandler(getContext());

        AlertDialog alertDialog = alertDialog();
        AlertDialog successDialog = successDialog();

        resetAllButton = (Button) this.getView().findViewById(R.id.fullResetbutton);
        resetAllItemsButton = (Button) this.getView().findViewById(R.id.clearAllItemsButton);
        editNameTextBox = (EditText) this.getView().findViewById(R.id.changeNameEditText);
        saveSettingButton = (Button) this.getView().findViewById(R.id.saveSettingsButton);

        // set the hint of the old text box to the old username
        editNameTextBox.setHint(sph.getUserName());

        resetAllButton.setOnClickListener(InputFragmentView -> {

            // wipe all items and categories from the database (will be recreated upon startup)
            db.wipeAllItems();
            db.wipeAllCategories();

            // clear username and theme preferences
            sph.clearSharedPreferences();
            Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            getActivity().finish();
        });

        resetAllItemsButton.setOnClickListener(InputFragmentView -> {
            // wipe all items
            db.wipeAllItems();

            Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            getActivity().finish();
        });

        saveSettingButton.setOnClickListener(InputFragmentView -> {
            // check if the edit name text box is empty, if yes throw a modal
            if (editNameTextBox.getText().toString().trim().length() == 0) {

                alertDialog.show();
            } else {
                sph.setUserName(editNameTextBox.getText().toString());

                //hideKeyboard(returnView);
                KeyboardManager.hideKeyboard(this.getView());
                successDialog.show();

                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim) // set animation between page transition
                        .replace(R.id.homeFragment, new Overview())
                        .commit();
            }
        });

    }



}