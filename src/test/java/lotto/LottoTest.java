package lotto;

import lotto.domain.Lotto;
import lotto.service.LottoInputParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoTest {
    private String lottoInput;
    private LottoInputParser lottoInputParser;

    @BeforeEach
    void setUp() {
        lottoInputParser = new LottoInputParser();
    }

    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // TODO: 추가 기능 구현에 따른 테스트 코드 작성
    @Test
    @DisplayName("로또 번호를 쉼표로 구분되어 정상적인 리스트를 변환한다")
    void 로또번호_입력_쉼표_구분() {
        lottoInput = "1,2,3,4,5,6";
        List<String> lottoNumbers = lottoInputParser.parseLottoNumbers(lottoInput);

        assertThat(lottoNumbers).isEqualTo(List.of("1","2","3","4","5","6"));
    }
}
