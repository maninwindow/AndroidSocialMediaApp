package com.tn.tnclient.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tn.tnclient.Api.ApiClient;
import com.tn.tnclient.Api.ApiInterface;
import com.tn.tnclient.Models.ResponseBody;
import com.tn.tnclient.R;
import com.tn.tnclient.Utils.InputDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageUploader extends AppCompatActivity {

    private Dialog mCameraDialog;
    @BindView(R.id.user_profile_photo) ImageView imageView;
    private ProgressDialog mProgress;
    private Bitmap imageBitmap;
    private TextView user_username;
    private TextView user_phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_uploader);
        ButterKnife.bind(this);
        /*
        progress bar for login
         */
        mProgress = new ProgressDialog(ImageUploader.this);
        mProgress.setTitle("正在更换头像..");
        mProgress.setMessage("请稍等...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

    }

    public void uploadToServer(MultipartBody.Part imageParts){
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "ProfileImage");
        Call<ResponseBody> call = apiInterface.uploadImage(imageParts,name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody body = response.body();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void uploader(){
        MultipartBody.Part imageParts = bitmapToMultipart(imageBitmap);
        mProgress.show();
        uploadToServer(imageParts);
        mProgress.dismiss();
    }

    @OnClick(R.id.user_photo)
    public void onClickAvatar(){
        mCameraDialog = new Dialog(this, R.style.my_dialog);
        mCameraDialog.setCanceledOnTouchOutside(true);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.fragment_bottom_dialog, null);
        root.findViewById(R.id.btn_open_camera).setOnClickListener(btnlistener);
        root.findViewById(R.id.btn_choose_img).setOnClickListener(btnlistener);
        root.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = -20;
        lp.width = (int) getResources().getDisplayMetrics().widthPixels;
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_camera:
                //Intent for Camera
                Intent u = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(u,102);
                break;
            // 打开相册
            case R.id.btn_choose_img:
                //Intent for Gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,101);
                break;
            //Cancel
            case R.id.btn_cancel:
                if (mCameraDialog != null) {
                    mCameraDialog.dismiss();
                }
                break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 101:
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                        mCameraDialog.dismiss();
                        imageBitmap = bitmap;
                        uploader();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 102:
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    mCameraDialog.dismiss();
                    imageBitmap = bitmap;
                    uploader();
                }
                break;
        }
    }

    public MultipartBody.Part bitmapToMultipart(Bitmap imageBitmap){
        File file = null;
        try {
            //create a file to write bitmap data
            file = new File(this.getCacheDir(), "imageBitmap");
            file.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        return body;
    }

    @OnClick(R.id.user_username)
    public void onClickUsername(){
        user_username = (TextView)findViewById(R.id.user_username_value);
        dialogEditText(user_username,"姓名",user_username.getText().toString());
    }

    @OnClick(R.id.user_phonenumber)
    public void onClickPhonenumber(){
        user_phonenumber = (TextView)findViewById(R.id.user_phonenumber_value);
        dialogEditText(user_phonenumber,"手机号",user_phonenumber.getText().toString());
    }

    private void dialogEditText(final TextView view, String title, final String text){
        new InputDialog.Builder(this)
                .setTitle(title)
                .setInputDefaultText(text)
                .setInputMaxWords(20)
                .setInputHint("编辑个人信息")
                .setPositiveButton("确定", new InputDialog.ButtonActionListener() {
                    @Override
                    public void onClick(CharSequence inputText) {
                        view.setText(inputText);
                    }
                })
                .setNegativeButton("取消", new InputDialog.ButtonActionListener() {
                    @Override
                    public void onClick(CharSequence inputText) {
                        // TODO
                    }
                })
                .setOnCancelListener(new InputDialog.OnCancelListener() {
                    @Override
                    public void onCancel(CharSequence inputText) {
                        // TODO
                    }
                })
                .interceptButtonAction(new InputDialog.ButtonActionIntercepter() { // 拦截按钮行为
                    @Override
                    public boolean onInterceptButtonAction(int whichButton, CharSequence inputText) {
                        if ("/sdcard/my".equals(inputText) && whichButton == DialogInterface.BUTTON_POSITIVE) {
                            // TODO 此文件夹已存在，在此做相应的提示处理
                            // 以及return true拦截此按钮默认行为
                            return true;
                        }
                        return false;
                    }
                })
                .show();
    }


}