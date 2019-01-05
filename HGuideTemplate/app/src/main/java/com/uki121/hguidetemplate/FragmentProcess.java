package com.uki121.hguidetemplate;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.TextView;
=======
>>>>>>> 4d67f131148073efb500e99c724514a85ac62201
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentProcess extends Fragment {
    private Button btn_confirm;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_scroll, container, false);
<<<<<<< HEAD
        //init
        HGIndicator hgIndicator = new HGIndicator(view);
        //scroll
        final TextView textViewTos1 = (TextView) view.findViewById(R.id.content_tos1);
        final TextView textViewTos2 = (TextView) view.findViewById(R.id.content_tos2);
        final TextView textViewTos3 = (TextView) view.findViewById(R.id.content_tos3);

        List<String> tosList = Arrays.asList(getResources().getStringArray(R.array.tos));
        textViewTos1.setText(tosList.get(0));
        textViewTos2.setText(tosList.get(1));
        textViewTos3.setText(tosList.get(2));
        //
        final Button btn_confirm = (Button) view.findViewById(R.id.btn_scroll_cofirm);
        List < Integer > target_id = Arrays.asList(R.id.btn_scroll_cofirm);
        hgIndicator.Trigger("btn_confirmer", target_id, "Except")
                    .Action(target_id, "FOCUS")
                    .Commit();
=======
        HGIndicator hgIndicator = new HGIndicator(view);
        //btn
        btn_confirm = (Button) view.findViewById(R.id.btn_scroll_cofirm);
        List< Integer > target_id = Arrays.asList(R.id.btn_scroll_cofirm);
        hgIndicator.Trigger("process_confirm", target_id, "Except")
                    .Action(target_id, "FOCUS")
                    .Commit();

>>>>>>> 4d67f131148073efb500e99c724514a85ac62201
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        return view;
    }
    public void show() {
<<<<<<< HEAD
        //sensitive class will be replaced with below.
        final String sCategory[] = {"Sensor Level", "Touch_count", "Time_count"};
        final String sContents[] = {"NO_HELP", "30", "300000"};
        final List<String> ListItems = new ArrayList<>();
        ListItems.add(sCategory[0] + " : " + sContents[0]);
        ListItems.add(sCategory[1] + " : " + sContents[1]);
        ListItems.add(sCategory[2] + " : " + sContents[2]);
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sensitive Info");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                Toast.makeText(getContext(), selectedText, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, new FragmentEdit());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
=======
        //sensitive-info
        //get sensitive infomation from sensitive class
        final List < String > sensitiveInfo = new ArrayList<>();
        String category[] = {"Sensitive Level", "Touch count", "Time Count"};
        String category_contents[] = {"NO_HELP", "30", "50000"};
        for (int i = 0; i < category.length; ++i) {
            sensitiveInfo.add(category[i] + ": " + category_contents[i]);
            Log.i("info", sensitiveInfo.get(i));
        }
        final CharSequence[] items =  sensitiveInfo.toArray(new String[sensitiveInfo.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sensitive Check");
        //builder.setMessage("Detailed Content");
        builder.setItems(items, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_main, new FragmentEdit());
                    }
                });
        /*
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"you selected yes",Toast.LENGTH_LONG).show();
                    }
                });

        builder.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"You selected no",Toast.LENGTH_LONG).show();
                    }
                    });
        */
>>>>>>> 4d67f131148073efb500e99c724514a85ac62201
        builder.show();
    }
}
