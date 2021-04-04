package cool.zzy.sems.application.fragment;

import android.Manifest;
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
import cool.zzy.sems.application.model.Rect;
import cool.zzy.sems.application.ui.ProgressDialog;
import cool.zzy.sems.application.util.CameraHelper;
import cool.zzy.sems.application.util.CameraUtils;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.context.dto.DeliveryPickUpDTO;
import cool.zzy.sems.context.dto.LogisticsAddDTO;
import cool.zzy.sems.context.service.DeliveryLogisticsService;
import cool.zzy.sems.context.service.LogisticsService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 14:44
 * @since 1.0
 */
public class BarcodeFragment extends BaseFragment implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = BarcodeFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int BARCODE_RESULT_SIZE = 3;

    private OpencvJni openCvJni;
    private CameraHelper cameraHelper;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private SurfaceView surfaceView;

    private LinearLayout barcodeBack;
//    private AppCompatImageView barcodeImageView;

    private ProgressDialog progressDialog;

    private List<String> barcodeResultList = new ArrayList<>(BARCODE_RESULT_SIZE);

    public static final int USER_SCAN_TYPE = 1;
    public static final int LOGISTICS_PERSONNEL_SCAN_TYPE = 2;
    private final int type;

    public BarcodeFragment(int type) {
        this.type = type;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_barcode;
    }

    @Override
    protected void initViews(View rootView) {
        initOpenCV(rootView);
        barcodeBack = rootView.findViewById(R.id.fragment_barcode_back);
        progressDialog = new ProgressDialog(getActivity(), getString(R.string.pick_upping));
    }

    @Override
    protected void initData() {
        cameraHelper.setPreviewCallback(this);
        surfaceView.getHolder().addCallback(this);
        barcodeBack.setOnClickListener(this);
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_barcode_back:
                enterMainFragment();
                break;
        }
    }

    private void initOpenCV(View rootView) {
        openCvJni = new OpencvJni();
        cameraHelper = new CameraHelper(cameraId);
        surfaceView = rootView.findViewById(R.id.fragment_barcode_surface_view);
//        barcodeImageView = rootView.findViewById(R.id.fragment_clock_barcode);
//        barcodeImageView.setImageBitmap(EAN13Utils.drawEan13Code("978730238251"));
    }

    @Override
    public void onResume() {
        super.onResume();
        startOpenCV();
    }

    private void startOpenCV() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraHelper.startPreview();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(), R.string.camera_permission_not_granted, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    private void stopCamera() {
        cameraHelper.stopPreview();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), R.string.camera_permission_not_granted,
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

    private void showBarcode(String barcode) {
        if (!barcode.isEmpty()) {
            Log.d(TAG, barcode);
            stopCamera();
            if (type == USER_SCAN_TYPE) {
                pickUp(barcode);
            } else if (type == LOGISTICS_PERSONNEL_SCAN_TYPE) {
                inOutbound(barcode);
            }
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
            DialogUtils.showConnectErrorDialog(this.getMainActivity());
            return;
        }
        new Thread(() -> {
            LogisticsAddDTO logisticsAddDTO = logisticsService.addLogistics(postId, userDTO);
            getMainActivity().runOnUiThread(() -> {
                progressDialog.dismiss();
                DialogUtils.showTipDialog(this.getActivity(), logisticsAddDTO.getInfo(),
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
            DialogUtils.showConnectErrorDialog(this.getMainActivity());
            return;
        }
        new Thread(() -> {
            DeliveryPickUpDTO deliveryPickUpDTO = deliveryLogisticsService.pickUp(postId, user);
            getMainActivity().runOnUiThread(() -> {
                progressDialog.dismiss();
                DialogUtils.showTipDialog(this.getActivity(), deliveryPickUpDTO.getInfo(),
                        (dialog, which) -> {
                            enterMainFragment();
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
