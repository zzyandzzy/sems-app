package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 18:39
 * @since 1.0
 */

/**
 * 物流位置表
 */
@Data
public class LogisticsLocation implements Serializable {
    private Integer id;

    private Date created;

    private Date modified;

    private String locationName;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_CREATED = "created";

    public static final String COL_MODIFIED = "modified";

    public static final String COL_LOCATION_NAME = "location_name";
}