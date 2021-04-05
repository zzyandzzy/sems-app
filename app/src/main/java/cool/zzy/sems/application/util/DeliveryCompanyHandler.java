package cool.zzy.sems.application.util;

import cool.zzy.sems.application.R;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/3 14:14
 * @since 1.0
 */
public class DeliveryCompanyHandler {

    private final static Map<Integer, DeliveryCompanyEntity> DELIVERY_COMPANY_MAP = new HashMap<>(16);
    public final static DeliveryCompanyEntity[] DELIVERY_COMPANY_ARRAY = new DeliveryCompanyEntity[]{
            new DeliveryCompanyEntity(317, "顺丰速运", R.drawable.delivery_sf),
            new DeliveryCompanyEntity(96, "EMS快递", R.drawable.delivery_ems),
            new DeliveryCompanyEntity(28, "百世快递", R.drawable.delivery_baishi),
            new DeliveryCompanyEntity(439, "圆通速递", R.drawable.delivery_yuantong),
            new DeliveryCompanyEntity(440, "韵达快递", R.drawable.delivery_yunda),
            new DeliveryCompanyEntity(198, "京东物流", R.drawable.delivery_jd),
            new DeliveryCompanyEntity(69, "德邦快递", R.drawable.delivery_debang),
            new DeliveryCompanyEntity(318, "申通快递", R.drawable.delivery_sentong),
            new DeliveryCompanyEntity(525, "中通快运", R.drawable.delivery_zhongtong),
    };

    static {
        for (DeliveryCompanyEntity deliveryCompanyEntity : DELIVERY_COMPANY_ARRAY) {
            DELIVERY_COMPANY_MAP.put(deliveryCompanyEntity.id, deliveryCompanyEntity);
        }
    }

    public static DeliveryCompanyEntity get(int id) {
        return DELIVERY_COMPANY_MAP.get(id);
    }

    @Data
    @AllArgsConstructor
    public static class DeliveryCompanyEntity {
        private int id;
        private String name;
        private int avatar;
    }
}
