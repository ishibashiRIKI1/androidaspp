package com.example.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class FormActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etMail;
    private EditText etComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        //送信ボタンが押されたら
        Button button=findViewById(R.id.sendbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName = findViewById(R.id.etName);
                String Name = etName.getText().toString();

                etMail = findViewById(R.id.etMail);
                String Mail = etMail.getText().toString();

                etComment = findViewById(R.id.etComment);
                String Comment = etComment.getText().toString();



                asyncTask a=new asyncTask();
                //メールアドレス（@の前まで）,パスワード,件名,本文の順番
                a.execute("sekaican.11", "cnehzsvofsglnyla","お問い合わせ",Name+"\n"+Mail+"\n\n\n"+Comment) ;

            }
        });
        //ホームボタンが押されたら
        findViewById(R.id.HomeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent= new Intent(getApplication(), CalendarActivity.class);
                startActivity(intent);
            }
        });
    }
//メール送るための処理
    private class asyncTask extends android.os.AsyncTask{
        protected String account;
        protected String password;
        protected String title;
        protected String text;

        @Override
        protected Object doInBackground(Object... obj){
            account=(String)obj[0];
            password=(String)obj[1];
            title=(String)obj[2];
            text=(String)obj[3];

            java.util.Properties properties = new java.util.Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.socketFactory.post", "465");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            final javax.mail.Message msg = new javax.mail.internet.MimeMessage(javax.mail.Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(account,password);
                }
            }));

            try {
                msg.setFrom(new javax.mail.internet.InternetAddress(account + "@gmail.com"));
                //自分自身にメールを送信
                msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(account + "@gmail.com"));
                msg.setSubject(title);
                msg.setText(text);

                javax.mail.Transport.send(msg);

            } catch (Exception e) {
                return (Object)e.toString();
            }

            return (Object)"送信が完了しました";

        }
        @Override
        protected void onPostExecute(Object obj) {
            //画面にメッセージを表示する
            Toast.makeText(FormActivity.this,(String)obj,Toast.LENGTH_LONG).show();
        }
    }

    //オプションメニューの実装
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // オプションメニューを作成する
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // オプションメニューの選択時処理
        int itemId = item.getItemId();
        Intent intent;
        switch (itemId) {
            case R.id.notice_menu:
                intent= new Intent(getApplication(), AlarmActivity.class);
                startActivity(intent);
                break;
            case R.id.search_menu:
                intent = new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.area_menu:
                intent= new Intent(getApplication(), MuniActivity.class);
                startActivity(intent);
            case R.id.contactMun_menu:
                intent = new Intent(getApplication(), JichitaiActivity.class);
                startActivity(intent);
                break;
            case R.id.contactUs_menu:
                intent = new Intent(getApplication(), FormActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}