package com.skykallove.mjc_sns_2017662016.Network;

import android.content.ContentValues;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by sky on 2017-11-21.
 * @version 1.0
 */

public class HttpConnection {

    private static HttpConnection _inst = null;

    public static HttpConnection getInstance() {
        if (_inst == null) {
            _inst = new HttpConnection();
        }
        return _inst;
    }

    public String request(String _url, ContentValues params) {

        // HttpURLConnection 참조 변수 선언
        HttpURLConnection connection = null;

        // StringBuffer URL 뒤에 붙혀 보낼 파라미터
        StringBuffer sbParams = new StringBuffer();

        // 1. StringBuffer 파라미터 연결
        // 보낼 데이터가 없으면 파라미터를 비움
        if (params == null) {
            sbParams.append("");
        } // if
        else {

            // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성
            boolean isAnd = false;

            // 파라미터 키, 값
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : params.valueSet()) {
                key = parameter.getKey();
                value = parameter.getValue().toString();

                // 파라미터가 두개 이상일때 파라미터 사이에 & 추가
                if (isAnd) {
                    sbParams.append("&");
                }
                sbParams.append(key).append("=").append(value);

                // 파라미터가 두개 이상이면 isAnd 변수를 참으로 바꾸고 다음 루프부터 &를 붙힌다.
                if (!isAnd) {
                    if (params.size() >= 2) {
                        isAnd = true;
                    } // if
                } // if
            } // for
        } // else
        // 2. HttpURLConnection 을 통해 웹서버의 데이터를 가져온다.
        try {
            URL url = new URL(_url);
            connection = (HttpURLConnection) url.openConnection();

            // connection 설정
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Context_Type", "application/json");
            connection.setConnectTimeout(3000);
            // 파라미터 전달 및 데이터 읽어오기
            String strParams = sbParams.toString();
            OutputStream os = connection.getOutputStream();
            os.write(strParams.getBytes("UTF-8")); // 출력 스트림에 출력
            os.flush(); // 버퍼링된 모든 출력 바이트를 강제 실행
            os.close(); // 출력 스트림 닫기 (시스템 자원 해제)

            // 연결 요청 확인 (실패 시 null 을 리턴하고  메서드 종료)
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            // 읽어온 결과물 리턴 (요청한 URL 의 출력물을 BufferedReader 로 받는다)
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null) {
                page += line;
            }

            return page;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            } // if
        } // finally
        return null;
    }
}
