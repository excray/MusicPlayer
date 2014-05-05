package com.example.nofrills;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class Player extends Activity {

	private final String MEDIA_PATH = "/sdcard";
	private MediaPlayer mp = new MediaPlayer();
	private int currentPos = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);

		final ListView listview = (ListView) findViewById(R.id.listView1);
		// String[] values = new String[] { "Android", "iPhone",
		// "WindowsMobile",
		// "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
		// "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
		// "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
		// "Android", "iPhone", "WindowsMobile" };
		//
		String[] values = getRawFiles();
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
			list.add(values[i]);
		}
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		// listview.setAdapter(adapter);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				playSong(item, position);
			}

		});

		ImageButton shuffle_btn = (ImageButton) findViewById(R.id.imageButton3);
		shuffle_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				shuffle(list);
				adapter.notifyDataSetChanged();
			}

			private void shuffle(ArrayList<String> list) {
				// TODO Auto-generated method stub
				Random rnd = new Random();
				for (int i = list.size() - 1; i > 0; i--) {
					int rnd_int = rnd.nextInt(i + 1);
					swap(list, i, rnd_int);
				}
			}

			private void swap(ArrayList<String> list, int i, int rnd_int) {
				// TODO Auto-generated method stub
				String t = list.get(i);
				list.set(i, list.get(rnd_int));
				list.set(rnd_int, t);
			}
		});

		// listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		// {

		// @SuppressLint("NewApi") @Override
		// public void onItemClick(AdapterView<?> parent, final View view,
		// int position, long id) {
		// final String item = (String) parent.getItemAtPosition(position);
		// view.animate().setDuration(2000).alpha(0)
		// .withEndAction(new Runnable() {
		// @Override
		// public void run() {
		// list.remove(item);
		// adapter.notifyDataSetChanged();
		// view.setAlpha(1);
		// }
		// });
		// }

		// });
	}

	@Override
	protected void onDestroy() {
		mp.release();
	}

	private String[] getRawFiles() {
		// TODO Auto-generated method stub

		// Field[] fields = R.raw.class.getFields();
		// ArrayList<String> files = new ArrayList<String>();
		// for(int count = 0; count < fields.length; count++)
		// {
		// files.add(fields[count].getName());
		// }

		File f = new File(MEDIA_PATH);
		String[] files = f.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				if (filename.endsWith(".mp3")) {
					return true;
				}
				return false;
			}
		});

		return files;
	}

	public void stopSong(View v) {
		if (mp.isPlaying()) {
			mp.stop();
		}
	}

	public void skipSong(View v) {
		if (mp.isPlaying()) {
			ListView list_view = (ListView) findViewById(R.id.listView1);
			int next_pos = -1;
			Toast.makeText(getApplicationContext(),
					"List count" + Integer.toString(list_view.getCount()),
					Toast.LENGTH_SHORT).show();
			while (next_pos == -1 && list_view.getCount() > 1) {
				Random r = new Random();
				int rnd = r.nextInt(list_view.getCount());
				if (rnd != currentPos) {
					next_pos = rnd;
					break;
				}
			}
			final String item = (String) list_view.getItemAtPosition(next_pos);
			playSong(item, next_pos);
		}
	}

	private void playSong(String item, int position) {
		// TODO Auto-generated method stub
		if (item == null) {
			// Toast.makeText(getApplicationContext(), "Listview is null",
			// Toast.LENGTH_SHORT).show();
			// displayError("NUll Listview !!!");
		}
		currentPos = position;
		final TextView txtview = (TextView) findViewById(R.id.textView1);
		txtview.setText(item);
		Uri uri = Uri.parse(MEDIA_PATH + "/" + item);
		if (mp.isPlaying()) {
			mp.reset();
		}
		try {
			mp.setDataSource(getApplicationContext(), uri);
			mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mp.prepareAsync();
	}

	private void displayError(String msg) {
		// TODO Auto-generated method stub
		AlertDialog ad = new AlertDialog.Builder(getApplicationContext())
				.create();
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setMessage(msg);
		// ad.set
		ad.setButton(DialogInterface.BUTTON_NEGATIVE, "OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		ad.show();

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
			View rowView = inflater.inflate(R.layout.activity_player, parent,
					false);
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
