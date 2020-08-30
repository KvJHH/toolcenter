package top.superookie.toolcenter;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;


public class SpringContext extends ClassPathXmlApplicationContext {

    private static final String PREFIX = "META-INF/";

    private static String getFileRootPath(String name) {
        String[] splitNames = name.split("\\.");
        String runmode = ConfigProperty.get("runmode");
        if (splitNames.length == 2 && !StringUtils.isEmpty(runmode)) {
            StringBuilder builder = new StringBuilder(PREFIX);
            builder.append(splitNames[0]);
            builder.append("_");
            builder.append(runmode.toUpperCase());
            builder.append(".");
            builder.append(splitNames[1]);
            return builder.toString();
        }
        System.out.println(splitNames.length);
        System.out.println(StringUtils.isEmpty(runmode));
        return null;
    }

    public SpringContext(String name) {
        super(new String[] { getFileRootPath(name) }, true);
    }

    public SpringContext(String name, boolean refresh) {
        super(new String[] {getFileRootPath(name)}, refresh);
    }

}

