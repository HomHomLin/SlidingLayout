package lib.homhomlib.design.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import lib.homhomlib.design.SlidingLayout;

/**
 * Created by Linhh on 16/4/15.
 */
public class ListViewActivity extends AppCompatActivity {
    private SlidingLayout mSlidingLayout;
    private final static String TAG = "ListViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        mSlidingLayout = (SlidingLayout) findViewById(R.id.slidingLayout);
//        View front = View.inflate(this,R.layout.view_front,null);
        ListView listView = (ListView) this.findViewById(R.id.listview);
        listView.setAdapter(new Adapter());
        mSlidingLayout.setSlidingListener(new SlidingLayout.SlidingListener() {
            @Override
            public void onSlidingOffset(View view, float delta) {
                Log.i(TAG,"onSlidingOffset:" + delta);
            }

            @Override
            public void onSlidingStateChange(View view, int state) {
                Log.i(TAG,"onSlidingStateChange:" + state);
            }

            @Override
            public void onSlidingChangePointer(View view, int pointerId) {

            }
        });
//        mSlidingLayout.setFrontView(front);
    }

    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 60;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(ListViewActivity.this,R.layout.list_item,null);

            }
            return convertView;
        }
    }
}