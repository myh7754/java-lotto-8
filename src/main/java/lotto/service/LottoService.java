package lotto.service;

import camp.nextstep.edu.missionutils.Randoms;
import lotto.domain.Lotto;
import lotto.domain.Rank;

import java.util.*;

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

    private Rank calculateLottoRank(Lotto lotto, List<Integer> lottoNumbers, int bonusNumber) {
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

    public Map<Rank,Integer> calculateLottoResults(List<Lotto> lottos, List<Integer> winningNumbers, int bonusNumber) {
        Map<Rank,Integer> results = new EnumMap<>(Rank.class);
        for (Lotto lotto : lottos) {
            Rank rank = calculateLottoRank(lotto, winningNumbers, bonusNumber);
            results.put(rank, results.getOrDefault(rank, 0) + 1);
        }
        return results;
    }

    public double calculateRateOfReturn(Map<Rank,Integer> results, int purchaseAmount) {
        double totalPrize = 0;
        for (Map.Entry<Rank,Integer> entry : results.entrySet()) {
            totalPrize += entry.getKey().getPrize()* entry.getValue();
        }

        double rate = totalPrize / purchaseAmount * 100;

        return Math.round(rate*100) / 100.0;
    }
}
