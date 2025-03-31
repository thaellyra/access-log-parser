import ua_parser.Client;
import ua_parser.Parser;

public class UserAgent {
    private final String browser;
    private final String osType;

    public UserAgent(String str) {
        Parser uaParser = new Parser();
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
