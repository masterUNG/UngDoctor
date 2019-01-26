package masterung.th.in.androidthai.ungdoctor;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Register Controller
        registerController();

//        Login Controller
        loginController();

    }   // Main Method

    @Override
    public void onResume() {
        super.onResume();

        try {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
            String idString = sharedPreferences.getString("id", "");

            if (!idString.isEmpty()) {
                Intent intent = new Intent(getActivity(), DoctorActivity.class);
                intent.putExtra("id", idString);
                startActivity(intent);
                getActivity().finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void loginController() {
        Button button = getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userEditText = getView().findViewById(R.id.edtUser);
                EditText passwordEditText = getView().findViewById(R.id.edtPassword);
                MyAlert myAlert = new MyAlert(getActivity());

                String user = userEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String urlPHP = "https://www.androidthai.in.th/sam/getUserWhereUserMaster.php";
                boolean userAbool = true;

                if (user.isEmpty() || password.isEmpty()) {
                    myAlert.normalDialog("Have Space", "Please Fill All Every Blank");
                } else {
                    try {

                        GetUserWhereUserThread getUserWhereUserThread = new GetUserWhereUserThread(getActivity());
                        getUserWhereUserThread.execute(user, urlPHP);
                        String json = getUserWhereUserThread.get();
                        Log.d("26JanV1", "json ==> " + json);

                        if (json.equals("null")) {
                            myAlert.normalDialog("User False", "No " + user + " in my Database");
                        } else {
                            JSONArray jsonArray = new JSONArray(json);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            if (password.equals(jsonObject.getString("Password"))) {

                                CheckBox checkBox = getView().findViewById(R.id.chbRemember);
                                if (checkBox.isChecked()) {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("id", jsonObject.getString("id"));
                                    editor.commit();
                                }

                                Intent intent = new Intent(getActivity(), DoctorActivity.class);
                                intent.putExtra("id", jsonObject.getString("id"));
                                startActivity(intent);
                                getActivity().finish();

                            } else {
                                myAlert.normalDialog("Password False", "Please Try Again Password False");
                            }

                        }   // if

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void registerController() {
        TextView textView = getView().findViewById(R.id.txtRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Replace Fragment
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentMainFragment, new RegisterFragment()).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

}   // Main Class
