import ua_parser.Client;
import ua_parser.Parser;

public class UserAgent {
    private final String browser;
    private final String osType;
    private static final Parser uaParser = new Parser();

    public UserAgent(String str) {
        Client client = uaParser.parse(str);
        browser = client.userAgent.family;
        osType = client.os.family;
    }

    public String getBrowser() {
        return browser;
    }

    public String getOsType() {
        return osType;
    }
}
