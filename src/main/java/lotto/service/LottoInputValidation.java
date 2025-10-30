package lotto.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LottoInputValidation {
    public void validateSize(List<String> lottoNumbers) {
        if (lottoNumbers.size() != 6) {
            throw new IllegalArgumentException(String.format("[ERROR] 로또 번호 개수를 잘못입력하였습니다 : %d개 입력됨", lottoNumbers.size()));
        }
    }
    public void validateDuplicate(List<String> lottoNumbers) {
        Set<String> uniqueLottoNumbers = new HashSet<>(lottoNumbers);
        if  (uniqueLottoNumbers.size() != lottoNumbers.size()) {
            throw new IllegalArgumentException("[ERROR] 로또 번호에 중복된 숫자가 있습니다.");
        }
    }

    public void validateNumberFormat(String lottoNumber) {
        if (!lottoNumber.matches("\\d+")) { // 숫자만 있는지 확인
            throw new IllegalArgumentException(String.format("[ERROR] %s이 숫자 형식이 아닙니다.", lottoNumber));
        }
    }
}
