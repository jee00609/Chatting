
package com.kakao.sdk.newtone.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerActivity;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

public class MyClient extends Activity implements SpeechRecognizeListener, TextToSpeechListener
{
    ArrayList<String> texts;
    ArrayList<String> results;
    protected SpeechRecognizerClient client;
    private TextToSpeechClient ttsClient;



    private Socket socket;  //소켓생성
    BufferedReader in = null;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out = null;        //서버에 데이터를 전송한다.
    EditText input;         //화면구성
    Button button,stt_button,stt_cancel,stt_restart,stt_stop,ui_button;          //화면구성
    TextView output, chat_text;        //화면구성
    Intent intent;
    String id, data,intro,serviceType;
    String fromothers;
    Thread message;
    Timer timer = new Timer();
    ScrollView scrollView;  // 스크롤바
    Thread thread,temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        TextToSpeechManager.getInstance().initializeLibrary(getApplicationContext());

        ttsClient = new TextToSpeechClient.Builder()
                .setSpeechMode(TextToSpeechClient.NEWTONE_TALK_1)     // 음성합성방식
                .setSpeechSpeed(1.0)            // 발음 속도(0.5~4.0)
                .setSpeechVoice(TextToSpeechClient.VOICE_WOMAN_READ_CALM)  //TTS 음색 모드 설정(여성 차분한 낭독체)
                .setListener(this)
                .build();


        intent = getIntent();               // 인텐드를 받는다(메인 액티비티에서 넘긴 id)
        // 인텐트로 받은 아이디 저장
        id = intent.getStringExtra("id");
        //start
        input = (EditText) findViewById(R.id.sendtext);           // 글자입력칸(에디트텍스트)을 찾는다.
        button = (Button) findViewById(R.id.button1);            // 버튼을 찾는다.

        ui_button=(Button)findViewById(R.id.uibutton);

        chat_text = (TextView) findViewById(R.id.chat_text);    //채팅 상황 보여주는 창
        scrollView = (ScrollView) findViewById(R.id.scrollView);//자동 스크롤 위한 객체

        setButtonsStatus(true);



        // 전송 완료 버튼 누른 경우
        // 순수하게 전송완료 기능만 한다
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //data 변수에 우리가 입력한 메세지를 저장한다.
                data = input.getText().toString(); //글자입력칸에 있는 글자를 String 형태로 받아서 data에 저장

                if (data != null) {

                    //서버로 부터 받은 메세지 receive 변수에 저장
                    try {
                        //우리가 만든 자바 서버에 메세지 전송
                        out.println(data);

                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
                else if(data=="quit"||data=="Quit"||data=="QUIT")
                {

                    MyClient.this.finish();//앱 실행 중단
                }


            }
        });

        //ui button -->Listener
        ui_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                serviceType = SpeechRecognizerClient.SERVICE_TYPE_WORD;
                Intent i = new Intent(getApplicationContext(), VoiceRecoActivity.class);


                if (serviceType.equals(SpeechRecognizerClient.SERVICE_TYPE_WORD)) {
                    EditText words = (EditText)findViewById(R.id.words_edit);
                    String wordList = words.getText().toString();

                    Log.i("SpeechSampleActivity", "word list : " + wordList.replace('\n', ','));

                    i.putExtra(SpeechRecognizerActivity.EXTRA_KEY_USER_DICTIONARY, wordList);
                }

                i.putExtra(SpeechRecognizerActivity.EXTRA_KEY_SERVICE_TYPE, serviceType);

                startActivityForResult(i, 0);

            }
        });




        //소켓 설정 & 스트림 설정 스레드 실행구문
        //아이디 입력하고 나서 바로 실행됨
        thread = new Thread()
        {
            public void run()
            {
                try {
                    //소켓을 생성&서버와의 연결 시도
                    socket = new Socket("192.168.55.172", 3001);

                    //입출력 스트림 설정
                    //서버에서 메세지 읽어올 때  or  서버에 메세지 보낼 때 필요
                    out = new PrintWriter(socket.getOutputStream(), true); //데이터를 전송시 stream 형태로 변환하여                                                                                                                       //전송한다.
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //데이터 수신시 stream을 받아들인다.
                    //서버한테 아이디 전송
                    out.println(id);

                    //맨 처음에 입장 안내문 출력
                    Intro();

                }
                catch (IOException e)//예외 처리
                {
                    e.printStackTrace();
                }

                while (true) {
                    //맨 처음에 입장 안내문 출력

                    try {
                        fromothers = in.readLine();
                        if (fromothers != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ttsClient.play(fromothers);
                                    chat_text.append(fromothers + "\n\n");

                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);   // 밑으로 스크롤해준다.
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                 //fromothers라는 변수에 bufferedreader로 부터
                // 읽은 내용을 String형식으로 저장 & chat_text에 추가하는 방식으로 출력


            };
    };
    //스레드 시작
        thread.start();



}

    private void setButtonsStatus(boolean enabled) {
        findViewById(R.id.uibutton).setEnabled(enabled);
    }




    //그냥 맨 처음에 입장 안내문 읽어오기 때문에 intro로 이름 지음
    public void Intro() {
        try {
            intro = in.readLine();
            chat_text.append(intro + "\n");
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    //앱 종료시
    protected void onStop() {
        super.onStop();
        try {
            //socket.close(); //소켓을 닫는다.
            thread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // API를 더이상 사용하지 않을 때 finalizeLibrary()를 호출한다.
        SpeechRecognizerManager.getInstance().finalizeLibrary();

        TextToSpeechManager.getInstance().finalizeLibrary();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
           results = data.getStringArrayListExtra(VoiceRecoActivity.EXTRA_KEY_RESULT_ARRAY);

            final StringBuilder builder = new StringBuilder();

            for (String result : results) {
                builder.append(result);
                builder.append("\n");
            }

            new AlertDialog.Builder(this).
                    setMessage(builder.toString()).
                    setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //얻어진 데이터를 에티트 텍스트에 옮긴다.
                            for (int i=0;i<results.size();i++)
                            {
                                if(i==0)
                                {
                                    //ArrayList에서 처음 글자만 추출해서 올리기
                                    String temp_result = results.get(i);
                                    input.setText(temp_result);

                                }
                            }
                            dialog.dismiss();
                        }
                    }).
                    show();
        }
        else if (requestCode == RESULT_CANCELED) {
            // 음성인식의 오류 등이 아니라 activity의 취소가 발생했을 때.
            if (data == null) {
                return;
            }

            int errorCode = data.getIntExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_CODE, -1);
            String errorMsg = data.getStringExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_MESSAGE);

            if (errorCode != -1 && !TextUtils.isEmpty(errorMsg)) {
                new AlertDialog.Builder(this).
                        setMessage(errorMsg).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                dialog.dismiss();
                            }
                        }).
                        show();
            }
        }
    }

    //@Override
    public void onReady() {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

   // @Override
    public void onBeginningOfSpeech() {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

   // @Override
    public void onEndOfSpeech() {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

   // @Override
    public void onError(int errorCode, String errorMsg) {
        //TODO implement interface DaumSpeechRecognizeListener method
        Log.e("SpeechSampleActivity", "onError");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // setButtonsStatus(true);
                Toast.makeText(MyClient.this,"Error 1", Toast.LENGTH_SHORT).show();
            }
        });

        client = null;
    }

   // @Override
    public void onPartialResult(String text) {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

  //  @Override
    public void onResults(Bundle results) {
        final StringBuilder builder = new StringBuilder();
        Log.i("SpeechSampleActivity", "onResults");

        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

        for (int i = 0; i < texts.size(); i++) {
            builder.append(texts.get(i));
            builder.append(" (");
            builder.append(confs.get(i).intValue());
            builder.append(")\n");
        }

        final Activity activity = MyClient.this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // finishing일때는 처리하지 않는다.
                if (activity.isFinishing()) return;

                AlertDialog.Builder dialog = new AlertDialog.Builder(activity).
                        setMessage(builder.toString()).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                dialog.dismiss();
                            }
                        });
                dialog.show();

                setButtonsStatus(true);

                Toast.makeText(MyClient.this,builder, Toast.LENGTH_SHORT).show();

            }
        });

        client = null;
    }

   // @Override
    public void onAudioLevel(float v) {
        //TODO implement interface DaumSpeechRecognizeListener method
    }

   // @Override
    public void onFinished() {
        Log.i("SpeechSampleActivity", "onFinished");

    }

    //////////////TTS//////////////




}



