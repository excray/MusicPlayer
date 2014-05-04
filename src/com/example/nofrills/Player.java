package com.example.nofrills;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Player extends Activity {

	
	  private final String MEDIA_PATH="/sdcard";
	  private MediaPlayer mp = null;

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_player);

	    final ListView listview = (ListView) findViewById(R.id.listView1);
//	    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//	        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//	        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
//	        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
//	        "Android", "iPhone", "WindowsMobile" };
//
	    String[] values = getRawFiles();
	    final ArrayList<String> list = new ArrayList<String>();
	    for (int i = 0; i < values.length; ++i) {
	      list.add(values[i]);
	    }
	    final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1,  list);
	    //listview.setAdapter(adapter);
	    listview.setAdapter(adapter);
	    
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		
	    	public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
	    	{
	    		final String item = (String) parent.getItemAtPosition(position);
	    		final TextView txtview = (TextView) findViewById(R.id.textView1);
	    		txtview.setText(item);
	    		Uri uri = Uri.parse(MEDIA_PATH+"/"+item);
	    		if(mp!=null)
	    		{
	    			mp.reset();
	    		}
	    		mp = MediaPlayer.create(getApplicationContext(), uri);
	    		mp.start();
	    	}
	    	
	    });

	    //listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

//	      @SuppressLint("NewApi") @Override
//	      public void onItemClick(AdapterView<?> parent, final View view,
//	          int position, long id) {
//	        final String item = (String) parent.getItemAtPosition(position);
//	        view.animate().setDuration(2000).alpha(0)
//	            .withEndAction(new Runnable() {
//	              @Override
//	              public void run() {
//	                list.remove(item);
//	                adapter.notifyDataSetChanged();
//	                view.setAlpha(1);
//	              }
//	            });
//	      }

//	    });
	  }
	  
	  @Override
	  protected void onDestroy()
	  {
		  mp.release();
	  }
	  
	  private String[] getRawFiles() {
		// TODO Auto-generated method stub
		
//		  Field[] fields = R.raw.class.getFields();
//		  ArrayList<String> files = new ArrayList<String>();
//		  for(int count = 0; count < fields.length; count++)
//		  {
//			  files.add(fields[count].getName());
//		  }
		  
		  File f = new File(MEDIA_PATH);
		  String[] files = f.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				if(filename.endsWith(".mp3"))
				{
					return true;
				}
				return false;
			}
		});
		
		return files;
	}

	public void playSong(View v)
	  {
		  if(mp.isPlaying())
		  {
			  mp.stop();
			  ImageButton butt = (ImageButton) findViewById(R.id.imageButton2);
			  butt.setImageResource(R.drawable.stop_button);
		  }
		  else
		  {   mp.start();
			  ImageButton butt = (ImageButton) findViewById(R.id.imageButton2);
			  butt.setImageResource(R.drawable.play_button);
		  }
	  }

	  public class MySimpleArrayAdapter extends ArrayAdapter<String> {
		  private final Context context;
		  private final ArrayList<String> values;

		  public MySimpleArrayAdapter(Context context, ArrayList<String> values) {
		    super(context, R.layout.activity_player, values);
		    this.context = context;
		    this.values = values;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.activity_player, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.label);
		    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		    textView.setText(values.get(position));
		    // change the icon for Windows and iPhone
		    String s = values.get(position);
		    if (s.startsWith("iPhone")) {
		      imageView.setImageResource(R.drawable.ic_launcher);
		    } else {
		      imageView.setImageResource(R.drawable.ic_launcher);
		    }

		    return rowView;
		  }
		} 
	  

	  private class StableArrayAdapter extends ArrayAdapter<String> {

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      String item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }

	  }

	 
}
