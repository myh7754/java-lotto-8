package lotto;

import lotto.domain.Lotto;
import lotto.service.LottoInputParser;
import lotto.service.LottoInputValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LottoTest {
    private String lottoInput;
    private LottoInputParser lottoInputParser;
    private List<String> lottoNumbers;
    private LottoInputValidation validator;
    private String bonusNumber;
    private int purchaseAmount;

    @BeforeEach
    void setUp() {
        lottoInputParser = new LottoInputParser();
        validator = new LottoInputValidation();
        lottoInput = "1,2,3,4,5,6";
        lottoNumbers = new ArrayList<>(lottoInputParser.parseLottoNumbers(lottoInput));
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

        assertThat(lottoNumbers).isEqualTo(List.of("1", "2", "3", "4", "5", "6"));
    }

    @Test
    @DisplayName("입력받은 로또 번호 개수가 6개 초과면 예외 발생")
    void 로또_번호가_6개_초과인_경우() {
        lottoNumbers.add("7");
        assertThatThrownBy(() -> validator.validateSize(lottoNumbers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 로또 번호 개수를 잘못입력하였습니다");
    }

    @Test
    @DisplayName("입력받은 로또 번호 개수가 6개 미만이면 예외 발생")
    void 로또_번호가_6개_미만인_경우() {
        lottoNumbers.remove("6");
        assertThatThrownBy(() -> validator.validateSize(lottoNumbers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 로또 번호 개수를 잘못입력하였습니다");
    }

    @Test
    @DisplayName("입력받은 로또 번호가 중복된 수가 있다면 에러발생")
    void 로또_번호가_중복된_경우() {
        lottoNumbers.remove("5");
        lottoNumbers.add("6");
        assertThatThrownBy(() -> validator.validateDuplicate(lottoNumbers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 로또 번호에 중복된 숫자가 있습니다.");
    }

    @Test
    @DisplayName("입력값이 숫자 형식이 아닌경우 에러발생")
    void 로또_번호_문자열형식_검증() {
        String invalidNumber = "a";
        lottoNumbers.add(invalidNumber);
        assertThatThrownBy(() -> {
            for (String lottoNumber : lottoNumbers) {
                validator.validateNumberFormat(lottoNumber);
            }
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("[ERROR] %s이 숫자 형식이 아닙니다.", invalidNumber));
    }

    @Test
    @DisplayName("입력값이 공백일 경우 예외발생")
    void 로또_번호_공백_검증() {
        String invalidNumber = " ";
        lottoNumbers.add(invalidNumber);
        assertThatThrownBy(() -> {
            for (String lottoNumber : lottoNumbers) {
                validator.validateNumberFormat(lottoNumber);
            }
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("[ERROR] %s이 숫자 형식이 아닙니다.", invalidNumber));
    }

    @Test
    @DisplayName("1~45사이의 숫자가 아니라면 오류 발생")
    void 로또_번호의_범위_검증() {
        lottoNumbers.add("46");
        assertThatThrownBy(() -> {
            for (String lottoNumber : lottoNumbers) {
                int parseLottoNumber = Integer.parseInt(lottoNumber);
                validator.validateNumberRange(parseLottoNumber);
            }
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] $d는 1~45사이의 숫자가 아닙니다.");
    }

    @Test
    @DisplayName("1~45사이의 숫자가 아니고 음수면 오류 발생")
    void 로또_번호의_범위_검증_음수테스트() {
        lottoNumbers.add("-1");
        assertThatThrownBy(() -> {
            for (String lottoNumber : lottoNumbers) {
                int parseLottoNumber = Integer.parseInt(lottoNumber);
                validator.validateNumberRange(parseLottoNumber);
            }
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] $d는 1~45사이의 숫자가 아닙니다.");
    }

    @Test
    @DisplayName("보너스 번호가 로또 번호와 중복되면 오류 발생")
    void 보너스_번호_중복_테스트() {
        bonusNumber = "6";
        assertThatThrownBy(()-> validator.validateBonusNotInLotto(lottoNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("[ERROR] 보너스 숫자 : %s가 기존 당첨 번호와 중복됩니다.", bonusNumber));
    }

    @Test
    @DisplayName("보너스 번호가 로또 번호와 중복되지 않으면 성공")
    void 보너스_번호_중복_성공_테스트() {
        bonusNumber = "7";
        assertThatCode(()-> validator.validateBonusNotInLotto(lottoNumbers, bonusNumber))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("구매 금액이 1000원 단위로 떨어지는지 테스트")
    void 구매금액_검증_실패() {
        purchaseAmount = 3100;
        assertThatThrownBy(()->
            validator.validatePurchaseAmount(purchaseAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 1,000원 단위의 금액이어야 합니다.");

    }

    @Test
    @DisplayName("구매 금액이 1000원 단위로 떨어지는지 테스트")
    void 구매금액_검증_성공() {
        purchaseAmount = 3000;
        assertThatCode(()-> validator.validatePurchaseAmount(purchaseAmount))
                .doesNotThrowAnyException();

    }
}
