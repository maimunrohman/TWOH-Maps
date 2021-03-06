package id.attwhx.twmaps;



import java.util.ArrayList;
import java.util.List;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.MapActivity;


@SuppressWarnings("unused")
public class Checkin extends ListActivity implements OnClickListener{

	private TextView textView;
	private DBDataSource datasource;
	private View checkinbut;
	private Double lat,lng;
	private Button bt;
	ArrayList<DBLokasi> values;
	@Override
	public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.checkin);
      checkinbut = findViewById(R.id.checkinbt);
      checkinbut.setOnClickListener(this);
      textView =  (TextView) findViewById(R.id.textcheckin);
      Bundle b = this.getIntent().getExtras();
      lat = b.getDouble("latitude");
      lng = b.getDouble("longitude");
      textView.setText("Ingin check-in di koordinat berikut ? "+lat+","+lng);
      
      // Instanstiasi kelas DataSource yang berfungsi sebagai controller atau DAO
      
      datasource = new DBDataSource(this);
      datasource.open();
      
      // Ambil semua lokasi
      
      values = datasource.getAllLokasi();
      
      // Tampilkan pada ListView
      ArrayAdapter<DBLokasi> adapter = new ArrayAdapter<DBLokasi>(this,
    	        android.R.layout.simple_list_item_1, values);
      setListAdapter(adapter);
      
      bt = (Button) findViewById(R.id.showallinmap);
      bt.setOnClickListener(this);
    }
	
	

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String text = " Pindah ke lokasi " + values.get(position).getId();
		DBLokasi lokasi = datasource.getLokasi(position+1);
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		
		Intent i = new Intent(this, TWMaps.class);
    	Bundle b = new Bundle();
    	b.putDouble("longitude", Double.valueOf(lokasi.getLng()));
    	b.putDouble("latitude", Double.valueOf(lokasi.getLat()));
        i.putExtras(b);
    	startActivity(i);
	}
	      
	
	
	// Masukkan lokasi baru ketika tombol check in di-klik
	
	public void onClick(View v) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<DBLokasi> adapter = (ArrayAdapter<DBLokasi>) getListAdapter();
		DBLokasi lokasi = null;
		switch (v.getId())
		{
			case R.id.checkinbt:
				lokasi = datasource.createLokasi(lat,lng);
				if(lokasi !=null&&adapter!=null)
				{
					adapter.add(lokasi);
				}else
				{
					Toast.makeText(this, "NULL", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.showallinmap: 
				ArrayList<DBLokasi> lokasi2 = datasource.getAllLokasi();
				Bundle b = new Bundle();
				Intent i = new Intent(this,TWMaps.class);
		    	b.putParcelableArrayList("daftar", lokasi2);
		    	i.putExtras(b);
		    	startActivity(i);
		}
		adapter.notifyDataSetChanged();
		
	}

	@Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    datasource.close();
	    super.onPause();
	  }
	
}
