package lotto.service;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.List;

public class LottoService {
    public int calculateLottoCount(int purchaseAmount) {
        return purchaseAmount/1000;
    }
}
