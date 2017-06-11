import java.util.ArrayList;

/**
 * Created by corpa on 6/10/17.
 */
public class Profile {

    private String name;
    private ArrayList<MarketShare> marketShares;

    public Profile() {
        marketShares = new ArrayList<>();
    }

    public Profile(String name) {
        this.name = name;
        marketShares = new ArrayList<>();
    }


    @Override
    public String toString() {
        String content = "";
        for(MarketShare marketShare : marketShares) {
            content += marketShare.toString();
        }
        return content;
    }

    public ArrayList<MarketShare> getMarketShares() {
        return marketShares;
    }

    public void add(MarketShare marketShare) {
        marketShares.add(marketShare);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
