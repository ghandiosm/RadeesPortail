package ma.mghandi.radeesportail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by icomme on 10/10/2016.
 */
public class ListFacturesAdapter extends BaseAdapter implements Filterable {

    public static final String ADRESSE_ERROR = "L'Adresse N'existe Pas";
    private ArrayList<Facture> facturesList = null;
    private ArrayList<Facture> filteredList = null;
    private ItemFilter itemFilter;
    private Context context;
    private LayoutInflater layoutInflater;

    public ListFacturesAdapter(Context context, ArrayList<Facture> arraylistfactures) {
        this.facturesList = arraylistfactures;
        this.filteredList = arraylistfactures;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getFilter();
    }

    @Override
    public int getCount() {
        if (filteredList == null) {
            filteredList = new ArrayList<>();
        }
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.facture_list_row, null);
            holder = new Holder();
            holder.FactureName = (TextView) v.findViewById(R.id.factureName);
            holder.FactureDate = (TextView) v.findViewById(R.id.factureDate);
            holder.FactureMT = (TextView) v.findViewById(R.id.factureMT);
            holder.Image = (ImageView) v.findViewById(R.id.imagePartner);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        Picasso.with(context).load(R.drawable.facture_icon).into(holder.Image);
        holder.FactureName.setText(filteredList.get(position).getName());
        holder.FactureDate.setText(filteredList.get(position).getDate());
        holder.FactureMT.setText(filteredList.get(position).getMontant());
        //holder.PartnerEmail.setText(filteredList.get(position).getEmail());
        /*holder.EditMagazin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filteredList.get(position).getVille() == null && filteredList.get(position).getAdresse() == null) {
                    Toast.makeText(context, "L'adresse est NULL", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(filteredList.get(position).getVille()) && TextUtils.isEmpty(filteredList.get(position).getAdresse())) {
                        Toast.makeText(context, "Adresse n'existe pas", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, CentreSAVDetailMapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Position", position);
                        intent.putExtra("Adresse", filteredList.get(position).getVille() + ", " + filteredList.get(position).getAdresse());
                        context.getApplicationContext().startActivity(intent);
                    }
                }
            }
        });*/
       /*holder.MapMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PartnerGeolocationActivity.class);
                intent.putExtra("partnerlat",filteredList.get(position).getLat());
                intent.putExtra("partnerlng",filteredList.get(position).getLng());
                intent.putExtra("partnerName",filteredList.get(position).getName());
                intent.putExtra("partnerTelephone",filteredList.get(position).getPhone());
                context.startActivity(intent);
            }
        });*/
        return v;
    }

    class Holder {
        TextView FactureName, FactureDate, FactureMT;
        ImageView Image;
    }

    public static void AlerteMsg(Context context, final int position, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public Filter getFilter() {
        if (itemFilter == null) {
            itemFilter = new ItemFilter();
        }

        return itemFilter;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Facture> tempList = new ArrayList<>();
                // search content in friend list
                for (Facture facture : facturesList) {
                    if (facture.getName() != null && facture.getDate() != null) {
                        if (facture.getName().toLowerCase().contains(constraint) || facture.getDate().toLowerCase().contains(constraint) ) {
                            tempList.add(facture);
                        }
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = facturesList.size();
                filterResults.values = facturesList;
            }

            return filterResults;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Facture>) results.values;
            notifyDataSetChanged();
        }

    }
}
