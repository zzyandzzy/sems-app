package cool.zzy.sems.application.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cool.zzy.sems.application.OpencvJni;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.model.Rect;
import cool.zzy.sems.application.ui.ProgressDialog;
import cool.zzy.sems.application.util.CameraHelper;
import cool.zzy.sems.application.util.CameraUtils;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.context.dto.DeliveryPickUpDTO;
import cool.zzy.sems.context.dto.LogisticsAddDTO;
import cool.zzy.sems.context.model.Delivery;
import cool.zzy.sems.context.service.DeliveryLogisticsService;
import cool.zzy.sems.context.service.DeliveryService;
import cool.zzy.sems.context.service.LogisticsService;

import java.util.*;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/30 5:49 下午
 * @since 1.0
 */
public class BarcodeActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback, View.OnClickListener {
    private static final String TAG = BarcodeActivity.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int BARCODE_RESULT_SIZE = 3;
    private Map<String, Integer> barcodeResultMap = new HashMap<>();


    private OpencvJni openCvJni;
    private CameraHelper cameraHelper;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private SurfaceView surfaceView;
    private LinearLayout barcodeBack;
    private ProgressDialog progressDialog;

    public static final int USER_SCAN_TYPE = 1;
    public static final int LOGISTICS_PERSONNEL_SCAN_TYPE = 2;
    public static final int NEW_DELIVERY_SCAN_TYPE = 3;
    private int type = USER_SCAN_TYPE;
    public static final String TYPE_NAME = "type";
    // 播放音效
    private MediaPlayer pickUpMediaPlayer;
    private MediaPlayer inDeliveryMediaPlayer;

    public interface BarcodeCallback {
        void callback(String postId);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_barcode;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        type = intent.getIntExtra(TYPE_NAME, USER_SCAN_TYPE);
    }

    @Override
    protected void initViews() {
        initOpenCV();
        barcodeBack = findViewById(R.id.fragment_barcode_back);
        progressDialog = new ProgressDialog(this, getString(R.string.pick_upping));
        inDeliveryMediaPlayer = MediaPlayer.create(this, R.raw.in_delivery);
        pickUpMediaPlayer = MediaPlayer.create(this, R.raw.pickup);
    }

    @Override
    protected void initData() {
        cameraHelper.setPreviewCallback(this);
        surfaceView.getHolder().addCallback(this);
        barcodeBack.setOnClickListener(this);
        inDeliveryMediaPlayer.setLooping(false);
        pickUpMediaPlayer.setLooping(false);
    }

    @Override
    protected int getFragmentViewId() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startOpenCV();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    private void initOpenCV() {
        openCvJni = new OpencvJni();
        cameraHelper = new CameraHelper(cameraId);
        surfaceView = findViewById(R.id.fragment_barcode_surface_view);
    }

    private void startOpenCV() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraHelper.startPreview();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(this, R.string.camera_permission_not_granted, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void stopCamera() {
        cameraHelper.stopPreview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_barcode_back:
                finish();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Rect rect = openCvJni.haveBarcode(data, CameraHelper.WIDTH, CameraHelper.HEIGHT);
        if (rect.getWidth() * rect.getHeight() > 0) {
            byte[] jpegData = CameraUtils.runInPreviewFrame(data, camera);
            Bitmap bitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length);
            Object barcodeObject = openCvJni.recognitionBarcode(bitmap,
                    rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            if (barcodeObject instanceof String) {
                return;
            }
            if (barcodeObject instanceof List) {
                List<String> barcodeList = (List<String>) barcodeObject;
                if (barcodeList.isEmpty()) {
                    return;
                }
                // hashmap记录出现次数
                Map<String, Integer> map = new HashMap<>();
                for (String code : barcodeList) {
                    Integer count = map.get(code);
                    if (count == null) {
                        map.put(code, 1);
                    } else {
                        map.put(code, ++count);
                    }
                }
                // 排序
                List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
                //然后通过比较器来实现排序
                Collections.sort(list, Map.Entry.comparingByValue());
                String barcode = list.get(list.size() - 1).getKey();
                if ("2222222222222".equals(barcode)) {
                    return;
                }
                Integer barcodeFindCount = barcodeResultMap.get(barcode);
                if (barcodeFindCount != null) {
                    if (barcodeFindCount == BARCODE_RESULT_SIZE) {
                        showBarcode(barcode);
                        Log.i(TAG, "onPreviewFrame: " + barcode);
                        barcodeResultMap.clear();
                    } else {
                        barcodeResultMap.put(barcode, ++barcodeFindCount);
                    }
                } else {
                    barcodeResultMap.put(barcode, 1);
                }
            }
        }
    }

    private void showBarcode(String postId) {
        if (!postId.isEmpty()) {
            Log.d(TAG, postId);
            stopCamera();
            if (type == USER_SCAN_TYPE) {
                pickUp(postId);
            } else if (type == LOGISTICS_PERSONNEL_SCAN_TYPE) {
                inOutbound(postId);
            } else if (type == NEW_DELIVERY_SCAN_TYPE) {
                newDelivery(this, progressDialog, postId, id -> finish());
            }
        }
    }

    /**
     * 新建快递
     *
     * @param postId
     */
    public static void newDelivery(Activity activity, ProgressDialog progressDialog, String postId, BarcodeCallback callback) {
        progressDialog.setTitle(activity.getString(R.string.saveing_delivery));
        progressDialog.show();
        Delivery newDelivery = SemsApplication.instance.getNewDelivery();
        if (newDelivery != null) {
            newDelivery.setPostId(postId);
            DeliveryService deliveryService = SemsApplication.instance.getDeliveryService();
            if (deliveryService == null) {
                DialogUtils.showConnectErrorDialog(activity);
                return;
            }
            new Thread(() -> {
                boolean b = deliveryService.saveDelivery(newDelivery);
                activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    SemsApplication.instance.setNewDelivery(null);
                    DialogUtils.showTipDialog(activity, b ? activity.getString(R.string.success) : activity.getString(R.string.fail),
                            (dialog, which) -> {
                                dialog.dismiss();
                                if (callback != null) {
                                    callback.callback(postId);
                                }
                            });
                });
            }).start();
        }
    }

    /**
     * 入库
     *
     * @param postId
     */
    private void inOutbound(String postId) {
        progressDialog.setTitle(getString(R.string.in_the_outbound));
        progressDialog.show();
        LogisticsService logisticsService = SemsApplication.instance.getLogisticsService();
        if (logisticsService == null) {
            DialogUtils.showConnectErrorDialog(this);
            return;
        }
        new Thread(() -> {
            LogisticsAddDTO logisticsAddDTO = logisticsService.addLogistics(postId, userDTO);
            this.runOnUiThread(() -> {
                if (logisticsAddDTO.getState().equals(LogisticsAddDTO.SUCCESS.getState())) {
                    inDeliveryMediaPlayer.start();
                }
                progressDialog.dismiss();
                DialogUtils.showTipDialog(this, logisticsAddDTO.getInfo(),
                        (dialog, which) -> {
                            dialog.dismiss();
                            startOpenCV();
                        });
            });
        }).start();
    }

    /**
     * 取件
     *
     * @param postId
     */
    private void pickUp(String postId) {
        progressDialog.setTitle(getString(R.string.pick_upping));
        progressDialog.show();
        DeliveryLogisticsService deliveryLogisticsService = SemsApplication.instance.getDeliveryLogisticsService();
        if (deliveryLogisticsService == null) {
            DialogUtils.showConnectErrorDialog(this);
            return;
        }
        new Thread(() -> {
            DeliveryPickUpDTO deliveryPickUpDTO = deliveryLogisticsService.pickUp(postId, user);
            this.runOnUiThread(() -> {
                if (DeliveryPickUpDTO.SUCCESS.getState().equals(deliveryPickUpDTO.getState())) {
                    pickUpMediaPlayer.start();
                }
                progressDialog.dismiss();
                DialogUtils.showTipDialog(this, deliveryPickUpDTO.getInfo(),
                        (dialog, which) -> {
                            finish();
                            dialog.dismiss();
                        });
            });
        }).start();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        openCvJni.setSurface(holder.getSurface());
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
    }
}
