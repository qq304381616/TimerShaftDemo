package cn.samson.timershaftdemo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.samson.timershaftdemo.po.News;
import cn.samson.timershaftdemo.view.XListView;
import cn.samson.timershaftdemo.view.XListView.IXListViewListener;

import com.samson.timershaftdemo.R;

public class MainActivity extends Activity implements IXListViewListener {
	private XListView sListView;
	private List<News> sNewsList;
	private Handler sHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	private NewsAdapter sNewsAdapter;
	private Context sContext;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sContext = MainActivity.this;
		sNewsList = new ArrayList<News>();
		geneItems();
		sListView = (XListView) findViewById(R.id.xListView);
		sListView.setPullLoadEnable(true);
		sNewsAdapter = new NewsAdapter();
		sListView.setAdapter(sNewsAdapter);
		// sListView.setPullLoadEnable(false);
		// sListView.setPullRefreshEnable(false);
		sListView.setXListViewListener(this);
		sHandler = new Handler();
	}

	private void geneItems() {
		for (int i = 0; i != 5; ++i) {
			// items.add("refresh cnt " + (++start));
			++start;
			News news = new News();
			news.setTime(R.string.str_time);
			news.setIcon(R.drawable.img_icon);
			news.setPic(R.drawable.img_person_icon);
			sNewsList.add(news);
		}
	}

	private void onLoad() {
		sListView.stopRefresh();
		sListView.stopLoadMore();
		Date date = new Date();
		String time = date.getHours() + ":" + date.getMinutes() + ":"
				+ date.getSeconds();
		sListView.setRefreshTime(time);
	}

	@Override
	public void onRefresh() {
		sHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// start = ++refreshCnt;
				start = refreshCnt;
				// items.clear();
				geneItems();
				// sAdapter.notifyDataSetChanged();
				sNewsAdapter = new NewsAdapter();
				sListView.setAdapter(sNewsAdapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		sHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				sNewsAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	private class NewsAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public NewsAdapter() {
			mInflater = LayoutInflater.from(sContext);
		}

		@Override
		public int getCount() {
			return sNewsList.size();
		}

		@Override
		public Object getItem(int position) {
			return sNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder h = null;
			if (convertView == null) {
				h = new Holder();
				convertView = mInflater.inflate(R.layout.list_item, null);
				h.tv = (TextView) convertView.findViewById(R.id.tv_time);
				h.iv = (ImageView) convertView.findViewById(R.id.iv_icon);
				h.content = (TextView) convertView
						.findViewById(R.id.tv_content);
				convertView.setTag(h);
			} else {
				h = (Holder) convertView.getTag();
			}
			News news = sNewsList.get(position);
			int time = news.getTime();
			int icon = news.getIcon();
			int img = news.getPic();

			h.tv.setText(sContext.getString(time));
			h.iv.setBackgroundResource(icon);
			h.content.setBackgroundResource(img);

			return convertView;
		}

		private class Holder {
			public TextView tv;
			public ImageView iv;
			public TextView content;
		}
	}
}