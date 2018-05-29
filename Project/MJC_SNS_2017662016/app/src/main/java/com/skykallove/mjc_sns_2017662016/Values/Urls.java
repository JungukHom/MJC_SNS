package com.skykallove.mjc_sns_2017662016.Values;

/**
 * Created by sky on 2017-11-24.
 */

public class Urls {

    private static final String IP_ADDRESS_SKY = "http://192.168.0.37/";
    private static final String IP_ADDRESS_HJU = "http://121.131.92.157/";
    private static final String IP_ADDRESS_SKY_2 = "http://172.30.1.11/";

    private static final String CONNECT_IP = IP_ADDRESS_SKY;

    public static final String DIRECTORY = "MJC_SNS/";

    // 인터넷 연결 테스트
    public static final String SERVER_TEST = CONNECT_IP + DIRECTORY + "serverTest.php";

    // 로그인
    public static final String SERVER_LOGIN = CONNECT_IP + DIRECTORY + "login.php";

    // 회원가입
    public static final String REGISTER = CONNECT_IP + DIRECTORY + "register.php";

    // 아이디 중복 확인
    public static final String ID_CHECK = CONNECT_IP + DIRECTORY + "checkId.php";

    // 게시글 추가
    public static final String ADD_POST = CONNECT_IP + DIRECTORY + "addPost.php";

    // 댓글 가져오기
    public static final String GET_POST_COMMENT = CONNECT_IP + DIRECTORY + "getPostComment.php";

    // 게시글 갱신
    public static final String LOAD_POST = CONNECT_IP + DIRECTORY + "loadPost.php";

    // 좋아요 싫어요 증가
    public static final String LIKE_DISLIKE = CONNECT_IP + DIRECTORY + "addLikeAndDislike.php";

    // 댓글 추가
    public static final String ADD_COMMENT = CONNECT_IP + DIRECTORY + "addComment.php";

    // 회원 탈퇴
    public static final String REMOVE_USER = CONNECT_IP + DIRECTORY + "removeUser.php";

    // 댓글 삭제
    public static final String REMOVE_COMMENT = CONNECT_IP + DIRECTORY + "removeComment.php";

    // 게시글 삭제
    public static final String REMOVE_POST = CONNECT_IP + DIRECTORY + "removePost.php";

    // 개인정보처리방침
    public static final String PERSONAL_INFORMATION = CONNECT_IP + DIRECTORY + "personalInformation.php";
}
