import java.util.List;

import static java.util.stream.Collectors.toList;

public class Record {

    public static void main(String[] args) {
        Point point = new Point(3, 5);
    }

    record Point (int x, int y) {}

    record Merchant(String sales) {
    }

    /**
     *  Local record
     */
    private List<Merchant> findTopMerchants(List<Merchant> merchants, int month) {
        record MerchantSales(Merchant merchant, double sales) {}

        return merchants.stream()
                .map(merchant -> new MerchantSales(merchant, computeSales(merchant, month)))
                .sorted((m1, m2) -> Double.compare(m2.sales(), m1.sales()))
                .map(MerchantSales::merchant)
                .collect(toList());
    }
    private double computeSales(Merchant merchant, int month) {
        // ...
        return 8933200.43;
    }

}


