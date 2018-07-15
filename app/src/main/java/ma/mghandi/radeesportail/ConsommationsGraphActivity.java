package ma.mghandi.radeesportail;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class ConsommationsGraphActivity extends AppCompatActivity {
    BarChart consommationBarChart;
    private List<BarEntry> consomValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consommations_graph);
        //arrayGraphListConsommations = getIntent().getParcelableArrayListExtra("listConsommations");
        //contract_number = SharedData.getKey(ConsommationsGraphActivity.this, "contract_number");
        setTitle("Graph des Consommations");
        /********************** Initiation des valeurs *************************/
        consommationBarChart = (BarChart) findViewById(R.id.consommationBarChart);
        consomValues =new ArrayList<>();

        /******************* Defining a Chart **********************/


        //Retrieve the IDs and Noms Gerances
        ArrayList<String> consommationPeriods = SharedData.getArrayList(ConsommationsGraphActivity.this, "consommationPeriods");
        ArrayList<String> consommationPrix = SharedData.getArrayList(ConsommationsGraphActivity.this, "consommationPrix");
        //String[] consPeriods = ;

        int i = 0 ;
        for (String s : consommationPrix) {
            i++;
            consomValues.add(new BarEntry((float)i,Float.parseFloat(s)));
        }

        final String[] consPeriods =new String[consommationPeriods.size()+1];
        int j = 0 ;
        for (String consomPeriod : consommationPeriods) {
            j++;
            consPeriods[j] = consomPeriod;
        }



        BarDataSet setConsomSet= new BarDataSet(consomValues,"Vos Consommations");
        int colorConsomSet = getResources().getColor(R.color.colorPrimary);
        setConsomSet.setColor(colorConsomSet);


        BarData consommationData = new BarData(setConsomSet);
        consommationData.setValueFormatter(new LargeValueFormatter());
        consommationBarChart.setData(consommationData);
        consommationBarChart.invalidate(); // refresh
        //final String[] consPeriods = consommationPeriods.toArray(new String[consommationPeriods.size()+1]);

        //final String[] consPeriods = new String[] {"Janv.", "Fév.","Mars", "Avril", "Mai", "Juin","Juill.","Aout","Sept.","Oct.","Nov.","Dec."};

        IAxisValueFormatter formatter =new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return consPeriods[(int)value];
            }
        };
        XAxis xAxis = consommationBarChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        YAxis yAxisLeft =consommationBarChart.getAxisLeft();
        YAxis yAxisRight =consommationBarChart.getAxisRight();
        yAxisLeft.setValueFormatter(new LargeValueFormatter());
        yAxisRight.setValueFormatter(new LargeValueFormatter());
    }
}
