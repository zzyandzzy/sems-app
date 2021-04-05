package cool.zzy.sems.application.util;

import junit.framework.TestCase;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/5 13:07
 * @since 1.0
 */
public class EAN13UtilsTest extends TestCase {

    public void testRandomCode() {
        System.out.println(EAN13Utils.randomCode());
    }
}