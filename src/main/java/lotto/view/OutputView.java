package lotto.view;

import lotto.domain.Lotto;
import lotto.domain.Rank;

import java.util.List;
import java.util.Map;

public class OutputView {
    public void printNumberOfPurchases(int purchaseAmount) {
        System.out.println(purchaseAmount+"개를 구매했습니다.");
    }

    public void printPurchaseLottoNumbers(List<Lotto> lottos) {
        for (Lotto lotto : lottos) {
            System.out.println(lotto.getNumbers());
        }
    }

    public void printResultMessage(Map<Rank,Integer> results) {
        System.out.println("당첨 통계");
        System.out.println("---");

        for (Rank rank : results.keySet()) {
            System.out.println(rank.getMessage()+" - "+results.get(rank)+"개");
        }
    }

    public void printRateOfReturn(double rate) {
        System.out.println("총 수익률은 " + rate + "%입니다.");
    }
}
