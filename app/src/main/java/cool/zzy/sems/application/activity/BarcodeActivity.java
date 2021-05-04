package cool.zzy.sems.application.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
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
import cool.zzy.sems.application.fragment.BarcodeFragment;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/30 5:49 下午
 * @since 1.0
 */
public class BarcodeActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback, View.OnClickListener {
    private static final String TAG = BarcodeFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int BARCODE_RESULT_SIZE = 3;
    private final List<String> barcodeResultList = new ArrayList<>(BARCODE_RESULT_SIZE);

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
    }

    @Override
    protected void initData() {
        cameraHelper.setPreviewCallback(this);
        surfaceView.getHolder().addCallback(this);
        barcodeBack.setOnClickListener(this);
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
            String barcode = openCvJni.recognitionBarcode(bitmap,
                    rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            if ("2222222222222".equals(barcode)) {
                return;
            }
            if (barcodeResultList.size() == BARCODE_RESULT_SIZE) {
                // 在BARCODE_RESULT_SIZE个结果里面存在相同次数最多的字符串
                int[] ret = new int[BARCODE_RESULT_SIZE];
                for (int i = 0; i < BARCODE_RESULT_SIZE; i++) {
                    for (int j = 0; j < BARCODE_RESULT_SIZE; j++) {
                        if (i != j && barcodeResultList.get(i).equals(barcodeResultList.get(j))) {
                            ret[i] = ret[i]++;
                        }
                    }
                }
                int maxIndex = 0;
                int barcodeCount = 0;
                for (int i = 0; i < BARCODE_RESULT_SIZE; i++) {
                    if (ret[i] > barcodeCount) {
                        maxIndex = i;
                    }
                }
                showBarcode(barcodeResultList.get(maxIndex));
                barcodeResultList.clear();
            } else {
                barcodeResultList.add(barcode);
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
                newDelivery(this, progressDialog, postId);
            }
        }
    }

    /**
     * 新建快递
     *
     * @param postId
     */
    public static void newDelivery(Activity activity, ProgressDialog progressDialog, String postId) {
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
