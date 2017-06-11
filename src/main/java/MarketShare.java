import java.math.BigDecimal;

/**
 * Created by corpa on 6/10/17.
 */
public class MarketShare {

    private String market;
    private BigDecimal amountBought;
    private BigDecimal priceBoughtAt;

    public MarketShare(String market, BigDecimal amountBought, BigDecimal priceBoughtAt) {
        this.market = market;
        this.amountBought = amountBought;
        this.priceBoughtAt = priceBoughtAt;
    }

    @Override
    public String toString() {
        return market + "," + amountBought.toPlainString() + "," + priceBoughtAt.toPlainString() + "\n";
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public BigDecimal getAmountBought() {
        return amountBought;
    }

    public void setAmountBought(BigDecimal amountBought) {
        this.amountBought = amountBought;
    }

    public BigDecimal getPriceBoughtAt() {
        return priceBoughtAt;
    }

    public void setPriceBoughtAt(BigDecimal priceBoughtAt) {
        this.priceBoughtAt = priceBoughtAt;
    }
}
