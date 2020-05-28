package com.example.odataapp01.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.odataapp01.activities.CowDetailsActivity;
import com.example.odataapp01.R;
import com.example.odataapp01.data.CowState;

import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class CowsListAdapter extends
        RecyclerView.Adapter<CowsListAdapter.ViewHolder> {

    private List<CowState> cowsList;
    private AdapterView.OnItemClickListener onItemClickListener;

    // Pass in the contact array into the constructor
    public CowsListAdapter(List<CowState> cowsList) {
        this.cowsList = cowsList;
    }
    /*public CowsListAdapter(List<CowState> cowsList, AdapterView.OnItemClickListener OnItemClickListener) {
        cowsList = cowsList;
        onItemClickListener = OnItemClickListener;
    }*/

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        //public TextView nameTextView;
        //public Button messageButton;
        public ImageView ivIcon;
        public TextView tvGrlib;
        public TextView tvGrloInfo1;
        public TextView tvGrloInfo2;
        public LinearLayout llRow_layout;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            //nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            //messageButton = (Button) itemView.findViewById(R.id.message_button);

            //Ikonica
            this.ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);

            //IB Grla
            this.tvGrlib = (TextView) itemView.findViewById(R.id.tvGrlib);

            //Repro status
            this.tvGrloInfo1 = (TextView) itemView.findViewById(R.id.tvGrloInfo1);

            //Laktacija
            this.tvGrloInfo2 = (TextView) itemView.findViewById(R.id.tvGrloInfo2);

            //LinearLayout
            this.llRow_layout = (LinearLayout) itemView.findViewById(R.id.llRow_layout);

        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public CowsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View rowLayout = inflater.inflate(R.layout.list_item_cow_state, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(rowLayout);
        return viewHolder;

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(CowsListAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        final CowState cow = cowsList.get(position);

        // Set item views based on your views and data model

        //Ikona
        switch (cow.getKnjgk()) {
            case "K1":
            case "K2":
                viewHolder.ivIcon.setImageResource(R.drawable.ic_cow);
                break;
            case "B1":
                viewHolder.ivIcon.setImageResource(R.drawable.ic_bull);
                break;
            case "M1":
                viewHolder.ivIcon.setImageResource(R.drawable.ic_calf);
                break;
        }

        //Grlib
        viewHolder.tvGrlib.setText(cow.getNazkg() + " " + cow.getGrlib());

        //GrloInfo1
        if (cow.getReproDate() != null){
            //viewHolder.tvGrloInfo1.setText(String.format(cow.getRepro() + " " + cow.getReproDate().toString("dd.MM.yyyy")));
            LocalDateTime now = new LocalDateTime();
            int days = Days.daysBetween(cow.getReproDate(), now ).getDays();
            viewHolder.tvGrloInfo1.setText(String.format(cow.getRepro() + " pre " + days + " dana"));
        }
        else{
            viewHolder.tvGrloInfo1.setText( "Rođeno" + " " + cow.getDatRodj().toString("dd.MM.yyyy"));
        }

        //GrloInfo2
        //switch (cow.getKnjgk() == "B1"){
        switch (cow.getKnjgk()){
            case "K1": {
                viewHolder.tvGrloInfo2.setText(String.format(cow.getFazal(), cow.getBrlak(), cow.getDanul()));
                break;
            }
            case "B1": {
                viewHolder.tvGrloInfo2.setText(cow.getGrlime());
                break;
            }
/*            case "M1": {
                viewHolder.tvGrloInfo2.setText(String.format(cow.getFazal(), cow.getBrlak(), cow.getDanul()));
                break;
            }*/
            default: {
                viewHolder.tvGrloInfo2.setText("");
                break;
            }
        }



        viewHolder.llRow_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent detailsIntent = new Intent(view.getContext(), CowDetailsActivity.class);
                detailsIntent.putExtra("EXTRA_BUKRS", cow.getBukrs());
                detailsIntent.putExtra("EXTRA_GRLID", cow.getGrlid());
                detailsIntent.putExtra("EXTRA_KNJGK", cow.getKnjgk());
                view.getContext().startActivity(detailsIntent);

            }
        });

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if (cowsList != null) {
            return cowsList.size();
        }
        else {
            return 0;
        }
    }

}
