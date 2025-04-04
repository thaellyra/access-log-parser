import ua_parser.Client;
import ua_parser.Parser;

public class UserAgent {
    private final String browser;
    private final String osType;
    private final boolean bot;
    private static final Parser uaParser = new Parser();

    public UserAgent(String str) {
        Client client = uaParser.parse(str);
        browser = client.userAgent.family;
        osType = client.os.family;
        bot = browser.toLowerCase().contains("bot");
    }

    public String getBrowser() {
        return browser;
    }

    public String getOsType() {
        return osType;
    }

    public boolean isBot() {
        return bot;
    }
}
