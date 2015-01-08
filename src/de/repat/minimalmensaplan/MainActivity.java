package de.repat.minimalmensaplan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private ArrayList<HashMap<String, String>> dishList = new ArrayList<HashMap<String, String>>();
	private String date;
	private TextView dateTextView;
	private SimpleAdapter simpleAdapter; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dateTextView = (TextView) findViewById(R.id.dateTextView);

		new Network().execute();
	}

	private class Network extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Document doc = Jsoup
						.connect(
								"http://speiseplan.studierendenwerk-hamburg.de/de/530/2015/0/")
						.get();
				date = doc.select(".category").text();
				int maxDishes = doc.getElementsByClass("dish-description")
						.size();
				int indexForPrice = 0;
				for (int i = 0; i < maxDishes; i++) {
					String dish = doc.getElementsByClass("dish-description")	
							.get(i).text().replaceAll("(\\W[\\d\\W]*\\W)", " ");
					String price = doc.getElementsByClass("price")
							.get(indexForPrice).text();
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("dish", dish);
					item.put("price", price);
					dishList.add(item);
					indexForPrice += 2;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			 dateTextView.setText(date);
			 simpleAdapter = new SimpleAdapter(MainActivity.this,
			 dishList, R.layout.list, new String[] { "dish",
			 "price" }, new int[] { R.id.text1, R.id.text2 });
			
			 setListAdapter(simpleAdapter);
		}
	}
}
