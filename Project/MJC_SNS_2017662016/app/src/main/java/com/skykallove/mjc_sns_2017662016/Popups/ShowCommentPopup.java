package com.skykallove.mjc_sns_2017662016.Popups;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skykallove.mjc_sns_2017662016.DataManager.CommentDataManager;
import com.skykallove.mjc_sns_2017662016.DataManager.UserDataManager;
import com.skykallove.mjc_sns_2017662016.DataManager.VariousDataManager;
import com.skykallove.mjc_sns_2017662016.Network.HttpConnection;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.Values.Strings;
import com.skykallove.mjc_sns_2017662016.Values.Tags;
import com.skykallove.mjc_sns_2017662016.Values.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sky on 2017-11-24.
 */

public class ShowCommentPopup extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    private ListView commentView = null;
    public ShowCommentPopup.ListViewAdapter adapter = null;

    TextView comment_postNumber;

    SwipeRefreshLayout comment_swipeLayout;

    EditText content;

    public static String _postNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_show_comment);
        setTitle(Strings.SHOW_COMMENT);

        // 화면 세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // SwipeRefreshLayout 값 설정
        comment_swipeLayout = (SwipeRefreshLayout) findViewById(R.id.comment_swipeLayout);

        // SwipeRefreshLayout 온리프레시 리스너 설정
        comment_swipeLayout.setOnRefreshListener(this);

        // 텍스트뷰 값 설정
        comment_postNumber = (TextView) findViewById(R.id.comment_postNumber);

        // 에디트텍스트 값 설정
        content = (EditText) findViewById(R.id.showComment_comment);

        // 전송 버튼 온클릭 리스너 설정
        Button sendButton = (Button) findViewById(R.id.comment_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkIsEmpty()) {
//                    Toast.makeText(
//                            ShowCommentPopup.this,
//                            "내용을 입력해주세요",
//                            Toast.LENGTH_SHORT
//                    ).show();
//                    return;
//                }
                String writer = VariousDataManager.getInstance().getUserId();
                EditText _content = (EditText)findViewById(R.id.showComment_comment);
                String content = _content.getText().toString();
                ContentValues values = new ContentValues();
                values.put(Strings.POST_NUMBER, VariousDataManager.getInstance().postNumber);
                values.put(Strings.CONTENT, content);
                values.put(Strings.WRITER, writer);
                CommentSendTask commentSendTask = new CommentSendTask(Urls.ADD_COMMENT, values);
                commentSendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                _content.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshListView();
    }

    @Override
    public void onRefresh() {
        refreshListView();
    }

    public void refreshListView() {
        ContentValues value = new ContentValues();
        VariousDataManager variousDataManager = VariousDataManager.getInstance();
        _postNumber = variousDataManager.postNumber;
        value.put(Strings.POST_NUMBER, _postNumber);
        GetPostCommentTask detailsTask = new GetPostCommentTask(Urls.GET_POST_COMMENT, value);
        detailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class GetPostCommentTask extends AsyncTask<String, String, String> {

        String url;
        ContentValues values;

        public GetPostCommentTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConnection loginConnection = new HttpConnection();
            result = loginConnection.request(url, values);
            if (result == null || result.equals("")) {
                result = "error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONParser(result);
            comment_swipeLayout.setRefreshing(false);
        }
    }

    public void JSONParser(String result) {
        List<ContentValues> _list = new ArrayList<>();
        String writer, writedaytime, content;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(Strings.POST_COMMENT);
            for (int i = 0; i < jsonArray.length(); i++) {
                ContentValues _postValues = new ContentValues();
                JSONObject jObject = jsonArray.getJSONObject(i);
                writer = jObject.getString(Strings.WRITER);
                writedaytime = jObject.getString(Strings.WRITE_DAYTIME);
                content = jObject.getString(Strings.CONTENT);

                _postValues.put(Strings.WRITER, writer);
                _postValues.put(Strings.WRITE_DAYTIME, writedaytime);
                _postValues.put(Strings.CONTENT, content);
                _list.add(_postValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            invalidateListView(_list);
        }
    }

    public void invalidateListView(List<ContentValues> list) {
        adapter = null;
        adapter = new ShowCommentPopup.ListViewAdapter(this);
        commentView = null;
        commentView = (ListView) findViewById(R.id.comment_boardView);
        commentView.setAdapter(adapter);
        for (int i = list.size() - 1; i >= 0 ; i--) {
            ContentValues values = list.get(i);
            adapter.addItem(
                    values.get(Strings.WRITER).toString(),
                    values.get(Strings.WRITE_DAYTIME).toString(),
                    values.get(Strings.CONTENT).toString()
            );
        }
        adapter.notifyDataSetChanged();
    }

    private class ViewHolder {
        public TextView writer;
        public TextView writedaytime;
        public TextView content;
    }

    private class ListViewAdapter extends BaseAdapter {

        private Context context = null;
        private ArrayList<CommentDataManager> listData = new ArrayList<>();

        public ListViewAdapter(Context context) {
            super();
            this.context = context;
        }

        public void addItem(String writer, String writerdaytime, String content) {
            CommentDataManager addInfo = new CommentDataManager();
            addInfo.writer = writer;
            addInfo.writedaytime = writerdaytime;
            addInfo.content = content;

            listData.add(addInfo);
            addInfo = null;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_show_comment, null);

                holder.writer = (TextView) convertView.findViewById(R.id.comment_writer);
                holder.writedaytime = (TextView) convertView.findViewById(R.id.comment_writedaytime);
                holder.content = (TextView) convertView.findViewById(R.id.comment_content);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CommentDataManager data = this.listData.get(position);
            holder.writer.setText(data.writer);
            holder.writedaytime.setText(data.writedaytime);
            holder.content.setText(data.content);
            
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "하이", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            return convertView;
        }
    }

    private class CommentSendTask extends AsyncTask<String, String, String> {

        String url;
        ContentValues values;

        public CommentSendTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConnection commentConnection = HttpConnection.getInstance();
            result = commentConnection.request(url, values);
            if (result == null || result == "") {
                result = "error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(Tags.TAG_DEBUG, result);
            if (result.equals("ADD_SUCCESS")) {
                Toast.makeText(
                        ShowCommentPopup.this,
                        Strings.COMMENT_SUCCESS,
                        Toast.LENGTH_SHORT
                ).show();
                refreshListView();
            } // if
            else if (result.equals("ADD_FAIL")) {
                Toast.makeText(
                        ShowCommentPopup.this,
                        Strings.SERVER_ERROR,
                        Toast.LENGTH_SHORT
                ).show();
            } // else if
            else {

            } // else
        }
    }

}
