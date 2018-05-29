package com.skykallove.mjc_sns_2017662016.Layouts;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skykallove.mjc_sns_2017662016.DataManager.PostDataManager;
import com.skykallove.mjc_sns_2017662016.DataManager.VariousDataManager;
import com.skykallove.mjc_sns_2017662016.Network.HttpConnection;
import com.skykallove.mjc_sns_2017662016.Popups.ShowCommentPopup;
import com.skykallove.mjc_sns_2017662016.R;
import com.skykallove.mjc_sns_2017662016.SuperClasses.ViewOnClickListenerClass;
import com.skykallove.mjc_sns_2017662016.Values.IntValue;
import com.skykallove.mjc_sns_2017662016.Values.Strings;
import com.skykallove.mjc_sns_2017662016.Values.Tags;
import com.skykallove.mjc_sns_2017662016.Values.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sky on 2017-11-21.
 */

public class FreeBoardActivity extends ViewOnClickListenerClass implements
        ListView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    SwipeRefreshLayout swipeLayout;

    String userId;
    private ListView boardView = null;
    public FreeBoardActivity.ListViewAdapter adapter = null;

    public static FreeBoardActivity freeBoardActivity;

    TextView question;

    @Override
    protected void onStart() {
        super.onStart();
        String randomQuestion = Strings.QUESTION[new Random().nextInt(Strings.QUESTION.length)];
        new GetPostTask(Urls.LOAD_POST, null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        question.setText(randomQuestion);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_board);
        setTitle(Strings.FEED);

        // FreeBoardActivity 객체 값 저장
        freeBoardActivity = FreeBoardActivity.this;

        // 화면 세로 고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 데이터 클래스 인스턴스 가져오기
//        VariousDataManager data = VariousDataManager.getInstance();

        // SwipeRefreshLayout 값 설정
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

        // SwipeRefreshLayout 온리프레시 리스너 설정
        swipeLayout.setOnRefreshListener(this);

        // 리스트뷰 값 설정
        boardView = (ListView) findViewById(R.id.boardView);

        // 텍스트뷰 값 설정
        question = (TextView) findViewById(R.id.question);

        // 텍스트뷰 온클릭리스너 설정
        question.setOnClickListener(this);

        // 리스트뷰 아이템클릭리스터 설정
        boardView.setOnItemClickListener(this);

        adapter = new ListViewAdapter(this);
        boardView.setAdapter(adapter);

        // 쉐어드 프리퍼런스 값 설정
        preferences = getSharedPreferences(Tags.BASE_PREFERENCE, MODE_PRIVATE);

        // 쉐어드 프리퍼런스 에디터 값 설정
        editor = preferences.edit();

        // userId의 값을 프리퍼런스에서 가져옴
        userId = preferences.getString(Tags.USER_ID, "");

        // 접속완료 토스트 메세지 출력
        showHelloToastMessage(userId);

//        // 게시판 초기화 액티비티 실행
//        GetPostTask getPostTask = new GetPostTask(
//                Urls.LOAD_POST,
//                null
//        );
//        getPostTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    } // onCreate

    // 온리프레시 리스너
    @Override
    public void onRefresh() {
        new GetPostTask(Urls.LOAD_POST, null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options:
                startIntent(SettingsActivity.class);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.question:
                startIntent(PostWritingActivity.class);
        }
    }

    public void JSONParse(String result) {
        List<ContentValues> _list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray(Strings.POST_INFO);
            for (int i = 0; i < jsonArray.length(); i++) {
                ContentValues _values = new ContentValues();
                JSONObject jObject = jsonArray.getJSONObject(i);

                String postNumber = jObject.getString(Strings.POST_NUMBER);
                String title = jObject.getString(Strings.TITLE);
                String writer = jObject.getString(Strings.WRITER);
                String writedaytime = jObject.getString(Strings.WRITE_DAYTIME);
                String content = jObject.getString(Strings.CONTENT);
                String commentcount = jObject.getString(Strings.COMMENT_COUNT);
                String like = jObject.getString(Strings.LIKE);
                String dislike = jObject.getString(Strings.DISLIKE);

                _values.put(Strings.POST_NUMBER, postNumber);
                _values.put(Strings.TITLE, title);
                _values.put(Strings.WRITER, writer);
                _values.put(Strings.WRITE_DAYTIME, writedaytime);
                _values.put(Strings.CONTENT, content);
                _values.put(Strings.COMMENT_COUNT, commentcount);
                _values.put(Strings.LIKE, like);
                _values.put(Strings.DISLIKE, dislike);
                _list.add(_values);
            }
        } catch (JSONException e) {
            //
        } finally {
            invalidateListView(_list);
        }

    }

    public void invalidateListView(List<ContentValues> list) {
        adapter = null;
        adapter = new FreeBoardActivity.ListViewAdapter(this);
        boardView = null;
        boardView = (ListView) findViewById(R.id.boardView);
        boardView.setAdapter(adapter);
        for (int i = list.size() - 1; i >= 0; i--) {
            ContentValues values = list.get(i);
            adapter.addItem(
                    values.get(Strings.POST_NUMBER).toString(),
                    values.get(Strings.TITLE).toString(),
                    values.get(Strings.WRITE_DAYTIME).toString(),
                    values.get(Strings.WRITER).toString(),
                    values.get(Strings.LIKE).toString(),
                    values.get(Strings.DISLIKE).toString(),
                    values.get(Strings.CONTENT).toString(),
                    values.get(Strings.COMMENT_COUNT).toString()
            );
        }
        adapter.notifyDataSetChanged();
    }

    public void startIntent(Class c) {
        startActivity(new Intent(getApplicationContext(), c));
    }

    // 화면 중상단에 토스트 메세지 출력
    public void showHelloToastMessage(String id) {
        Toast toast = Toast.makeText(getApplicationContext(), id + "님 환영합니다.", Toast.LENGTH_SHORT);
        Display dp = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int y = (int) (0.25 * dp.getHeight());
        toast.setGravity(Gravity.CENTER, 0, -y);
        toast.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, Integer.toString(position), Toast.LENGTH_SHORT).show();
        String a = ((TextView) view.findViewById(R.id.freeBoard_postNumber)).getText().toString();
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
        VariousDataManager variousDataManager = VariousDataManager.getInstance();
        TextView _temp = (TextView) view.findViewById(R.id.freeBoard_postNumber);
        variousDataManager.postNumber = _temp.getText().toString();
        startIntent(ShowCommentPopup.class);
    }

    private class GetPostTask extends AsyncTask<String, String, String> {

        String url;
        ContentValues values;

        public GetPostTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConnection loginConnection = HttpConnection.getInstance();
            result = loginConnection.request(url, values);
            if (result == null || result.equals("")) {
                result = "error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // SwipeRefreshLayout 프로그래스 바 정지
            swipeLayout.setRefreshing(false);
            JSONParse(result);
        }

    }


    public void sendRequest(String postNumber, int value) {
        ContentValues values = new ContentValues();
        values.put(Strings.POST_NUMBER, postNumber);
        switch (value) {
            // 좋아요 버튼을 눌렀을 때
            case IntValue.LIKE:
                values.put(Strings.LIKE_DISLIKE, Strings.LIKE);
                break;
            // 싫어요 버튼을 눌렀을 때
            case IntValue.DISLIKE:
                values.put(Strings.LIKE_DISLIKE, Strings.DISLIKE);
                break;
            default:
                break;
        }
        LikeDislikeTask likeDislikeTask = new LikeDislikeTask(Urls.LIKE_DISLIKE, values);
        likeDislikeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class LikeDislikeTask extends AsyncTask<String, String, String> {

        String url;
        ContentValues values;

        public LikeDislikeTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            HttpConnection likeDislikeConnection = HttpConnection.getInstance();
            result = likeDislikeConnection.request(url, values);
            if (result == null || result.equals("")) {
                return "error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("ADD_SUCCESS")) {
                // 아무일도 하지 않음
            }
            else if (result.equals("ADD_FAIL")) {
                Toast.makeText(FreeBoardActivity.this, Strings.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
            else {

            }
//            adapter.notifyDataSetChanged();
            new GetPostTask(Urls.LOAD_POST, null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    // 리스트뷰의 뷰홀더 클래스
    private class ViewHolder {
        public TextView postNumber;
        public TextView title;
        public TextView writeDayTime;
        public TextView writer;
        public TextView like;
        public TextView dislike;
        public TextView content;
        public TextView commentCount;
    }

    // 리스트뷰 어댑터 클래스
    private class ListViewAdapter extends BaseAdapter {
        private Context context = null;
        private ArrayList<PostDataManager> listData = new ArrayList<>();

        public ListViewAdapter(Context context) {
            super();
            this.context = context;
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
                convertView = inflater.inflate(R.layout.listview_post, null); // parent => null

                holder.postNumber = (TextView) convertView.findViewById(R.id.freeBoard_postNumber);
                holder.title = (TextView) convertView.findViewById(R.id.freeBoard_title);
                holder.writeDayTime = (TextView) convertView.findViewById(R.id.freeBoard_writeDayTime);
                holder.writer = (TextView) convertView.findViewById(R.id.freeBoard_writer);
                holder.like = (TextView) convertView.findViewById(R.id.freeBoard_like);
                holder.dislike = (TextView) convertView.findViewById(R.id.freeBoard_dislike);
                holder.content = (TextView) convertView.findViewById(R.id.freeBoard_content);
                holder.commentCount = (TextView) convertView.findViewById(R.id.freeBoard_commentCount);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 좋아요 버튼 온클릭 리스너 설정
            Button likeButton = (Button) convertView.findViewById(R.id.list_likeButton);
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 좋아요 증가
                    String postNumber = holder.postNumber.getText().toString();
                    sendRequest(postNumber, IntValue.LIKE);
                }
            });

            // 싫어요   버튼 온클릭 리스너 설정
            Button dislikeButton = (Button) convertView.findViewById(R.id.list_dislikeButton);
            dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 싫어요 증가
                    String postNumber = holder.postNumber.getText().toString();
                    sendRequest(postNumber, IntValue.DISLIKE);
                }
            });

            // 댓글 달기 버튼 온클릭 리스너 설정
            Button writeComment = (Button) convertView.findViewById(R.id.list_writeCommentButton);
            writeComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String postNumber = holder.postNumber.getText().toString();
                    VariousDataManager variousDataManager = VariousDataManager.getInstance();
                    variousDataManager.postNumber = postNumber;
                    startIntent(ShowCommentPopup.class);
                }
            });


            PostDataManager data = this.listData.get(position);

            // holder.postNumber = 서버에서 가져온 데이터
            holder.postNumber.setText(data.postNumber);
            holder.title.setText(data.title);
            holder.writeDayTime.setText(data.writeDayTime);
            holder.writer.setText(data.writer);
            holder.like.setText(data.like);
            holder.dislike.setText(data.dislike);
            holder.content.setText(data.content);
            holder.commentCount.setText(data.commentCount);

            return convertView;
        }


        public void addItem(String postNumber, String title, String writeDayTime, String writer,
                            String like, String dislike, String content, String commentCount) {
            PostDataManager addInfo = new PostDataManager();
            addInfo.postNumber = postNumber;
            addInfo.title = title;
            addInfo.writeDayTime = writeDayTime;
            addInfo.writer = writer;
            addInfo.like = like;
            addInfo.dislike = dislike;
            addInfo.content = content;
            addInfo.commentCount = commentCount;
            listData.add(addInfo);
            addInfo = null;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

    // 취소 키가 눌렸을 때
    @Override
    public void onBackPressed() {
        showFinishAlertDialog();
    }

    public void showFinishAlertDialog() {
        // AlertDialog(Builder) 객체 생성
        AlertDialog.Builder finishDialog = new AlertDialog.Builder(this);
        // AlertDialog(Builder) 메인 메세지 설정
        finishDialog.setMessage(Strings.REALLY_EXIT);
        // AlertDialog(Builder) 긍정 버튼 설정
        finishDialog.setPositiveButton(Strings.YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                        어플리케이션 종료
                System.gc();
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        // AlertDialog(Builder) 부정 버튼 설정
        finishDialog.setNegativeButton(Strings.NO, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 메세지 창 취소
                dialog.cancel();
            }
        });
        // AlertDialog(Builder) 글자 색 설정
        final AlertDialog dialog = finishDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
            }
        });
        // AlertDialog(Builder) 활성화
        finishDialog.create().show();
    }
} // FreeBoardActivity