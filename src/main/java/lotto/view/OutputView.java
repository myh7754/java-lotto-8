package lotto.view;

import lotto.domain.Lotto;

import java.util.List;

public class OutputView {
    public void printNumberOfPurchases(int purchaseAmount) {
        System.out.println(purchaseAmount+"개를 구매했습니다.");
    }

    public void printWinningNumbers(List<Lotto> lottos) {
        for (Lotto lotto : lottos) {
            System.out.println(lotto.getNumbers());
        }
    }

}
