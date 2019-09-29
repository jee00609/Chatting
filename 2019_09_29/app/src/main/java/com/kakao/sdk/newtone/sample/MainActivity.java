package com.kakao.sdk.newtone.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //안드로이드 3.0 부터는 mainactivity에서 인터넷 연결시
        //꼭 필요한 코드 부분이다!!
        //여기부터~
        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //여기까지~

        Button sendId = (Button) findViewById(R.id.sendId);
        sendId.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText editId = (EditText)findViewById(R.id.editId);  // 에디트 텍스트 변수 선언
                String msg = editId.getText().toString();               // 에디트 텍스트에 입력된 값(id == msg) 가져오기

                // 메인 액티비티에서 세컨드 액티비티(새로운 액티비티) 호출
                Intent intent = new Intent(MainActivity.this, MyClient.class);

                // id라는 이름으로 msg를 보낸다.
                intent.putExtra("id", msg);     //id

                // 세컨드 액티비티 시작
                startActivity(intent);
            }
        });


//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.example.gunyoungkim.baseapplication.Thread", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
    }



}