package top.superookie.toolcenter;

import org.apache.commons.lang3.StringUtils;
import top.superookie.toolcenter.exception.ErrorCode;
import top.superookie.toolcenter.exception.ToolException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConfigProperty {

    private static final String COMMON_CONFIG = "config.property";

    private static final String RUNMODE = "runmode";

    private static Map<String, String> propMap = new HashMap<>();

    private static ClassLoader classLoader = ConfigProperty.class.getClassLoader();

    /**
     * 确保系统属性中的runmode优先级比配置文件高
     */
    private static boolean isRunmodeInSystemProperty() {
        String value = System.getProperty(RUNMODE);
        if (!StringUtils.isEmpty(value)) {
            propMap.put(RUNMODE, value);
            return true;
        } else {
            return false;
        }
    }

    private static boolean isRunmodeAlreadyLoaded() {
        return StringUtils.isNotEmpty(get(RUNMODE));
    }

    private static void loadConfig(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            Set<String> propertyNames = properties.stringPropertyNames();
            for (String name : propertyNames) {
                propMap.put(name, properties.getProperty(name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getConfigFileName() {
        StringBuilder builder = new StringBuilder(COMMON_CONFIG);
        builder.append(".");
        builder.append(propMap.get(RUNMODE));
        return builder.toString();
    }

    public static String get(String key) {
        String value = propMap.get(key);
        if (StringUtils.isEmpty(value)) {
            throw new ToolException(ErrorCode.MISS_CONFIG_PROPERTY, key);
        }
        return value;
    }

    static {
        InputStream commonInput = classLoader.getResourceAsStream(COMMON_CONFIG);
        loadConfig(commonInput);
        //runmode在系统属性中存在 || 在配置文件有配置
        if (isRunmodeInSystemProperty() || isRunmodeAlreadyLoaded()) {
            String configFileName = getConfigFileName();
            InputStream specifiedInput = classLoader.getResourceAsStream(configFileName);
            if (specifiedInput != null) {
                loadConfig(specifiedInput);
                System.out.println("执行环境: " + get(RUNMODE));
            } else {
                throw new ToolException(ErrorCode.MISS_CONFIG_FILE, configFileName);
            }
        } else {
            System.out.println("执行环境: normal");
        }
    }


}
