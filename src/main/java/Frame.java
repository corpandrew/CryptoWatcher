import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by corpa on 5/11/17.
 */
public class Frame extends JFrame {
    private JTextField priceOfTicker;
    private JComboBox<String> marketsComboBox;
    private String tickerName;
    private Map<String, Ticker> marketsMap;

    public Frame(Map<String, Ticker> markets) {
        this.tickerName = tickerName;
        setTitle(tickerName);

        marketsComboBox = new JComboBox<>();
        priceOfTicker = new JTextField("");
        Font font = new Font("SansSerif", Font.BOLD, 50);
        priceOfTicker.setSize(200,200);
        priceOfTicker.setFont(font);
        add(priceOfTicker);
        add(marketsComboBox);

        for(String s : markets.keySet()) {
            marketsComboBox.addItem(s);
            System.out.println( "\"" + s + "\"" + ",");
        }

        marketsComboBox.addItemListener(e -> {
            marketsMap = RESTfulUtils.getTickers();

            priceOfTicker.setText(marketsMap.get((String) e.getItem()).getLast());

            System.out.println(marketsMap.get((String) e.getItem()));
        });

        pack();
        setVisible(true);

    }
}
