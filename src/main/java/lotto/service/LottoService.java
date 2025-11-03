package lotto.service;

import camp.nextstep.edu.missionutils.Randoms;
import lotto.domain.Lotto;
import lotto.domain.Rank;

import java.util.ArrayList;
import java.util.List;

public class LottoService {
    public int calculateLottoCount(int purchaseAmount) {
        return purchaseAmount/1000;
    }

    public List<Integer> generateLottoNumbers() {
        return Randoms.pickUniqueNumbersInRange(1, 45, 6).stream().sorted().toList();
    }

    public List<Lotto> generateLottos(int lottoCount) {
        List<Lotto> lottos = new ArrayList<>();
        for (int i = 0; i < lottoCount; i++) {
            lottos.add(new Lotto(generateLottoNumbers()));
        }
        return lottos;
    }

    public Rank calculateLottoRank(Lotto lotto, List<Integer> lottoNumbers, int bonusNumber) {
        List<Integer> numbers = lotto.getNumbers();
        long matchCount = numbers.stream()
                .filter(lottoNumbers::contains)
                .count();
        boolean matchBonus = false;
        if (matchCount == 5 && numbers.contains(bonusNumber)) {
           matchBonus = true;
        }

        return Rank.valueOf((int) matchCount, matchBonus);
    }
}
