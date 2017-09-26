package hayaa.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 版权所有。
 *
 * @version 1.0 17-9-25 下午4:09 by谢青靖（xieqj@cloud-young.com）创建
 */
public class JsonConvert {
    public static String SerializeObject(Object value) {
        ObjectMapper jsonMapper = new ObjectMapper();
        String r = null;
        try {
            r = jsonMapper.writeValueAsString(value);
        } catch (Exception ex) {

        }
        return r;
    }

    public static <T> T DeserializeObject(String value, Class<T> valueType)
            throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        T r = null;
        try {
            r = jsonMapper.readValue(value, valueType);
        } catch (Exception ex) {

        }
        return r;
    }

    public static <T> List<T> DeserializeList(String value, Class<T> valueType)
            throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        List<T> r = new ArrayList<T>();
        try {
            JSONArray jsonArray = JSON.parseArray(value);
            if (jsonArray != null) {
                r.add(jsonMapper.readValue(value, valueType));
            }
        } catch (Exception ex) {
            r = null;
        }
        return r;
    }
}
