package com.example.odataapp01.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.odataapp01.R;
import com.example.odataapp01.data.CowEntry;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class CowEntriesListAdapter extends
        RecyclerView.Adapter<CowEntriesListAdapter.ViewHolder> {

    private List<CowEntry> cowEntries;
    private AdapterView.OnItemClickListener onItemClickListener;

    // Pass in the cowEntries array into the constructor
    public CowEntriesListAdapter(List<CowEntry> cowEntries) {
        this.cowEntries = cowEntries;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivIcon;
        public TextView tvTipUnosa;
        public TextView tvInfoLine1;
        public TextView tvInfoLine2;
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

            //Tip unosa
            this.tvTipUnosa = (TextView) itemView.findViewById(R.id.tvTipUnosa);

            //InfoLine1
            this.tvInfoLine1 = (TextView) itemView.findViewById(R.id.tvInfoLine1);

            //Laktacija
            this.tvInfoLine2 = (TextView) itemView.findViewById(R.id.tvInfoLine2);

            //LinearLayout
            this.llRow_layout = (LinearLayout) itemView.findViewById(R.id.llRow_layout);

        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public CowEntriesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View rowLayout = inflater.inflate(R.layout.list_item_cow_entry, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(rowLayout);
        return viewHolder;

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(CowEntriesListAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        final CowEntry cowEntry = cowEntries.get(position);

        // Set item views based on your views and data model

        //Set color for icons
        int color = Color.parseColor("#1A237E");

        //Ikona
        switch (cowEntry.getTipUnosa()) {
            case "Tretman":
                viewHolder.ivIcon.setImageResource(android.R.drawable.ic_menu_add);
                //viewHolder.ivIcon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                break;
            case "Premeštaj":
                viewHolder.ivIcon.setImageResource(android.R.drawable.ic_menu_directions);
                //viewHolder.ivIcon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                break;
            case "Teljenje":
                viewHolder.ivIcon.setImageResource(android.R.drawable.ic_menu_compass);
                //viewHolder.ivIcon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                break;
            case "Dodavanje u stado":
                viewHolder.ivIcon.setImageResource(android.R.drawable.ic_menu_upload);
                //viewHolder.ivIcon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                break;
            case "Promena kategorije":
                viewHolder.ivIcon.setImageResource(android.R.drawable.ic_menu_set_as);
                //viewHolder.ivIcon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                break;
            case "Osemenjivanje":
                viewHolder.ivIcon.setImageResource(android.R.drawable.ic_menu_more);
                //viewHolder.ivIcon.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                break;
        }

        //Tip unosa
        viewHolder.tvTipUnosa.setText(cowEntry.getTipUnosa());

        //InfoLine1
        viewHolder.tvInfoLine1.setText(String.format(cowEntry.getDatun().toString("dd.MM.yyyy")));

        //InfoLine2
        viewHolder.tvInfoLine2.setText(cowEntry.getSazetak());

        /*viewHolder.llRow_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(view.getContext(),"click on item: "+ cow.getGrlib(),Toast.LENGTH_LONG).show();

                Intent detailsIntent = new Intent(view.getContext(), DetailsActivity.class);
                detailsIntent.putExtra("EXTRA_BUKRS", cow.getBukrs());
                detailsIntent.putExtra("EXTRA_GRLID", cow.getGrlid());
                view.getContext().startActivity(detailsIntent);

            }
        });*/

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if (cowEntries != null) {
            return cowEntries.size();
        }
        else {
            return 0;
        }
    }

}
