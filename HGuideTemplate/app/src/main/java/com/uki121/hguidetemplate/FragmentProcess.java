package com.uki121.hguidetemplate;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentProcess extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_scroll, container, false);
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
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        return view;
    }
    public void show() {
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
        builder.show();
    }
}
