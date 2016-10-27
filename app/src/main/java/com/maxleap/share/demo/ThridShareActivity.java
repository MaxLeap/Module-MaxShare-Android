package com.maxleap.share.demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxleap.social.EventListener;
import com.maxleap.social.HermsException;
import com.maxleap.social.MLHermes;
import com.maxleap.social.thirdparty.param.ShareItem;
import com.maxleap.social.thirdparty.platform.Platform.Type;
import com.maxleap.social.thirdparty.share.QQShareProvider;
import com.maxleap.social.thirdparty.share.QZoneShareProvider;
import com.maxleap.social.thirdparty.share.ShareProvider;
import com.maxleap.social.thirdparty.share.ShareType;
import com.maxleap.social.thirdparty.share.WechatShareProvider;
import com.maxleap.social.thirdparty.share.WeiboShareProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 第三方分享示例
 */
public class ThridShareActivity extends BaseActivity {

    @BindView(R.id.et_share_text)
    EditText etShareText;
    @BindView(R.id.et_share_description)
    EditText etShareDescription;
    @BindView(R.id.et_share_actionurl)
    EditText etShareActionurl;
    @BindView(R.id.et_share_iamgepath)
    EditText etShareIamgepath;
    @BindView(R.id.et_share_imageurl)
    EditText etShareImageurl;
    @BindView(R.id.et_share_videourl)
    EditText etShareVideourl;
    @BindView(R.id.et_share_musicurl)
    EditText etShareMusicurl;
    @BindView(R.id.et_share_imagepathlist)
    EditText etShareImagepathlist;
    @BindView(R.id.btn_text)
    Button btnText;
    @BindView(R.id.cb_text)
    CheckBox cbText;
    @BindView(R.id.cb_description)
    CheckBox cbDescription;
    @BindView(R.id.cb_actionurl)
    CheckBox cbActionurl;
    @BindView(R.id.cb_imagepath)
    CheckBox cbImagepath;
    @BindView(R.id.cb_imageurl)
    CheckBox cbImageurl;
    @BindView(R.id.cb_videourl)
    CheckBox cbVideourl;
    @BindView(R.id.cb_musicurl)
    CheckBox cbMusicurl;
    @BindView(R.id.cb_imagepathlist)
    CheckBox cbImagepathlist;
    @BindView(R.id.btn_add_image)
    Button btnAddImage;
    @BindView(R.id.btn_add_video)
    Button btnAddVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_thrid);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_text)
    public void onClick() {
        share();
    }

    @OnClick({R.id.btn_add_image, R.id.btn_add_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_image:
                startPickLocaleImage();

                break;
            case R.id.btn_add_video:
                startPickLocaleVedio();
                break;
        }
    }

    private void startPickLocaleImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        this.startActivityForResult(Intent.createChooser(intent, "本地图片"), 0);
    }

    private void startPickLocaleVedio() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (android.os.Build.VERSION.SDK_INT >= Utils.Build_VERSION_KITKAT) {
            intent.setAction(Utils.ACTION_OPEN_DOCUMENT);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        startActivityForResult(
                Intent.createChooser(intent, "本地视频"), 100);
    }

    private void share() {

        ShareItem shareItem = ShareItem.newBuilder()
                .text(cbText.isChecked() ? etShareText.getText().toString() : null)
                .description(cbDescription.isChecked() ? etShareDescription.getText().toString() : null)
                .actionUrl(cbActionurl.isChecked() ? etShareActionurl.getText().toString() : null)
                .audioUrl(cbMusicurl.isChecked() ? etShareMusicurl.getText().toString() : null)
                .vdeoUrl(cbVideourl.isChecked() ? etShareVideourl.getText().toString() : null)
                .imagePath(cbImagepath.isChecked() ? etShareIamgepath.getText().toString() : null)
                .imageUrl(cbImageurl.isChecked() ? etShareImageurl.getText().toString() : null)
                .createShareItem();

        showShareDialog(shareItem);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (shareProvider != null) {
            shareProvider.onNewIntent(intent);
        }
    }

    Dialog bottomDialog;

    private void showShareDialog(ShareItem shareItem) {

        View dialogView = View.inflate(this, R.layout.view_dialog_info, null);

        RecyclerView rc_share = (RecyclerView) dialogView.findViewById(R.id.rc_share);
        rc_share.setLayoutManager(new GridLayoutManager(this, 3));
        rc_share.setAdapter(new DialogAdapter(shareItem));
        bottomDialog = Utils.bottomDialog(this, dialogView);
    }


    public List<ItemBean> list;

    public ShareItem mShareItem;


    public class DialogAdapter extends RecyclerView.Adapter<MyViewHolder> {

        public DialogAdapter(ShareItem shareItem) {
            mShareItem = shareItem;
            list = new ArrayList<>();
            list.add(new ItemBean("微信好友", R.mipmap.ml_herms_wechat, ShareType.TYPE_WEIXIN_FRIEND, false));
            list.add(new ItemBean("微信朋友圈", R.mipmap.ml_herms_timeline, ShareType.TYPE_WEIXIN_TIMELINE, true));
            list.add(new ItemBean("QQ好友", R.mipmap.ml_herms_qq, ShareType.TYPE_QQ_FRIEND, false));
            list.add(new ItemBean("QQ空间", R.mipmap.ml_herms_qzone, ShareType.TYPE_QQ_ZONE, true));
            list.add(new ItemBean("微博", R.mipmap.ml_herms_weibo, ShareType.TYPE_SINA, false));
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.view_share_item, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ItemBean itemBean = list.get(position);
            holder.iv.setImageResource(itemBean.res);
            holder.tv.setText(itemBean.title);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    ShareProvider shareProvider;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv = (TextView) itemView.findViewById(R.id.tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (bottomDialog != null) {
                        bottomDialog.dismiss();
                    }

                    ItemBean itemBean = list.get(getLayoutPosition());
                    mShareItem.isTimeLine = itemBean.isTimeLine;

                    //新浪微博分享视频、音乐、网页，都需要显示一张缩略图,大小不得超过 32kb，如果不设置，将分享不成功！
                    if (itemBean.type == ShareType.TYPE_SINA && mShareItem.bitmap == null && (!TextUtils.isEmpty(mShareItem.actionUrl) || !TextUtils.isEmpty(mShareItem.musicUrl) || !TextUtils.isEmpty(mShareItem.videoUrl))) {
                        mShareItem.bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    }

                    shareProvider = itemBean.getShareProvider();

                    shareProvider.shareItem(mShareItem, new EventListener() {
                        @Override
                        public void onSuccess() {

                            Toast.makeText(ThridShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(HermsException e) {
                            Toast.makeText(ThridShareActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(ThridShareActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });
        }

    }


    public class ItemBean {
        public String title;
        public int res;
        public int type;
        public boolean isTimeLine;

        public ItemBean(String title, int res, int type, boolean isTimeLine) {
            this.title = title;
            this.res = res;
            this.type = type;
            this.isTimeLine = isTimeLine;
        }

        public ShareProvider getShareProvider() {
            ShareProvider sp = null;
            switch (type) {
                case ShareType.TYPE_QQ_FRIEND:
                    sp = new QQShareProvider(ThridShareActivity.this, MLHermes.getPlatform(Type.QQ));
                    break;
                case ShareType.TYPE_QQ_ZONE:
                    sp = new QZoneShareProvider(ThridShareActivity.this, MLHermes.getPlatform(Type.QQ));

                    break;
                case ShareType.TYPE_SINA:
                    sp = new WeiboShareProvider(ThridShareActivity.this, MLHermes.getPlatform(Type.WEIBO));
                    break;

                case ShareType.TYPE_WEIXIN_FRIEND:
                case ShareType.TYPE_WEIXIN_TIMELINE:
                    sp = new WechatShareProvider(ThridShareActivity.this, MLHermes.getPlatform(Type.WECHAT));
                    break;
            }
            return sp;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            String path = null;
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    // 根据返回的URI获取对应的SQLite信息
                    Uri uri = data.getData();
                    path = Utils.getPath(this, uri);
                }
            }
            if (path != null) {
                etShareIamgepath.setText(path);
            }
        } else if (requestCode == 100) {

            String path = null;
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    path = Utils.getPath(this, uri);
                }
            }
            if (path != null) {
                etShareVideourl.setText(path);
            }
        }

        if (shareProvider != null) {
            shareProvider.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (shareProvider != null) {
            shareProvider.dispose();
        }
    }
}
