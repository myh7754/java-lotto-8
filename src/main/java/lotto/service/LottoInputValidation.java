package lotto.service;

import java.util.List;

public class LottoInputValidation {
    public void validateSize(List<String> lottoNumbers) {
        if (lottoNumbers.size() != 6) {
            throw new IllegalArgumentException(String.format("[ERROR] 로또 번호 개수를 잘못입력하였습니다 : %d개 입력됨", lottoNumbers.size()));
        }
    }
}
