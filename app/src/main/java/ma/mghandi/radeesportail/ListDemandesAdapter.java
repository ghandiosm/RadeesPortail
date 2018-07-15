package ma.mghandi.radeesportail;

import android.content.Context;
import android.content.DialogInterface;
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
public class ListDemandesAdapter extends BaseAdapter implements Filterable {

    public static final String ADRESSE_ERROR = "L'Adresse N'existe Pas";
    private ArrayList<Demande> demandesList = null;
    private ArrayList<Demande> filteredList = null;
    private ItemFilter itemFilter;
    private Context context;
    private LayoutInflater layoutInflater;

    public ListDemandesAdapter(Context context, ArrayList<Demande> arraylistdemandes) {
        this.demandesList = arraylistdemandes;
        this.filteredList = arraylistdemandes;
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
            v = layoutInflater.inflate(R.layout.demande_list_row, null);
            holder = new Holder();
            holder.DemandeName = (TextView) v.findViewById(R.id.demandeName);
            holder.DemandeNature = (TextView) v.findViewById(R.id.demandeNature);
            holder.DemandeDate = (TextView) v.findViewById(R.id.demandeDate);
            holder.DemandeIcon = (ImageView) v.findViewById(R.id.iconDemande);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        Picasso.with(context).load(R.drawable.ic_demandes).into(holder.DemandeIcon);
        holder.DemandeName.setText(filteredList.get(position).getName());
        holder.DemandeDate.setText(filteredList.get(position).getDate());
        holder.DemandeNature.setText(filteredList.get(position).getNature());
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
        TextView DemandeName, DemandeDate, DemandeNature;
        ImageView DemandeIcon;
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
                ArrayList<Demande> tempList = new ArrayList<>();
                // search content in friend list
                for (Demande demande : demandesList) {
                    if (demande.getName() != null && demande.getDate() != null && demande.getNature() != null) {
                        if (demande.getName().toLowerCase().contains(constraint) ) {
                            tempList.add(demande);
                        }
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = demandesList.size();
                filterResults.values = demandesList;
            }

            return filterResults;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Demande>) results.values;
            notifyDataSetChanged();
        }

    }
}
