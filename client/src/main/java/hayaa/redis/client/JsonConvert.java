package hayaa.redis.client;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

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
}
