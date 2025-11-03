package lotto;

import lotto.domain.Lotto;
import lotto.domain.Rank;
import lotto.service.LottoInputParser;
import lotto.service.LottoInputValidation;
import lotto.service.LottoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class LottoTest {
    private String lottoInput;
    private LottoInputParser lottoInputParser;
    private List<String> lottoNumbers;
    private LottoInputValidation validator;
    private LottoService lottoService;
    private String bonusNumber;
    private int purchaseAmount;

    @BeforeEach
    void setUp() {
        lottoInputParser = new LottoInputParser();
        validator = new LottoInputValidation();
        lottoService = new LottoService();
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
        assertThatThrownBy(() -> validator.validateBonusNotInLotto(lottoNumbers, bonusNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(String.format("[ERROR] 보너스 숫자 : %s가 기존 당첨 번호와 중복됩니다.", bonusNumber));
    }

    @Test
    @DisplayName("보너스 번호가 로또 번호와 중복되지 않으면 성공")
    void 보너스_번호_중복_성공_테스트() {
        bonusNumber = "7";
        assertThatCode(() -> validator.validateBonusNotInLotto(lottoNumbers, bonusNumber))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("구매 금액이 1000원 단위로 떨어지는지 테스트")
    void 구매금액_검증_실패() {
        purchaseAmount = 3100;
        assertThatThrownBy(() ->
                validator.validatePurchaseAmount(purchaseAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 1,000원 단위의 금액이어야 합니다.");

    }

    @Test
    @DisplayName("구매 금액이 1000원 단위로 떨어지는지 테스트")
    void 구매금액_검증_성공() {
        purchaseAmount = 3000;
        assertThatCode(() -> validator.validatePurchaseAmount(purchaseAmount))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("로또 번호 생성 테스트")
    void 로또_번호_생성() {
        List<Lotto> lottos = lottoService.generateLottos(1);
        assertThat(lottos.getFirst().getNumbers()).hasSize(6);
        assertThatCode(() -> validator.validateDuplicate(lottoNumbers))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("구매 가능한 로또 개수 계산 테스트")
    void 로또_개수_계산() {
        purchaseAmount = 3000;
        int lottoCount = lottoService.calculateLottoCount(purchaseAmount);
        assertThat(lottoCount).isEqualTo(3);
    }

    @Test
    @DisplayName("전체 결과 집계 테스트")
    void 전체_로또_결과_집계_테스트() {
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),
                new Lotto(List.of(1, 2, 3, 4, 5, 7)),
                new Lotto(List.of(10, 11, 12, 13, 14, 15))
        );
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;
        Map<Rank, Integer> results = lottoService.calculateLottoResults(lottos, winningNumbers, bonusNumber);
        assertThat(results.get(Rank.FIRST)).isEqualTo(1);
        assertThat(results.get(Rank.SECOND)).isEqualTo(1);
        assertThat(results.get(Rank.THIRD)).isEqualTo(null);
    }

    @Test
    @DisplayName("수익률 계산")
    void 당첨된_수익률_계산() {
        List<Lotto> lottos = List.of(
                new Lotto(List.of(8, 21, 23, 41, 42, 43)),
                new Lotto(List.of(3, 5, 11, 16, 32, 38)),
                new Lotto(List.of(7, 11, 16, 35, 36, 44)),
                new Lotto(List.of(1, 8, 11, 31, 41, 42)),
                new Lotto(List.of(13, 14, 16, 38, 42, 45)),
                new Lotto(List.of(7, 11, 30, 40, 42, 43)),
                new Lotto(List.of(2, 13, 22, 32, 38, 45)),
                new Lotto(List.of(1, 3, 5, 14, 22, 45))
                );
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;
        purchaseAmount = 8000;

        Map<Rank, Integer> lottoResult = lottoService.calculateLottoResults(lottos, winningNumbers, bonusNumber);
        double rateOfReturn = lottoService.calculateRateOfReturn(lottoResult, purchaseAmount);
        assertThat(rateOfReturn).isEqualTo(62.5);
    }
}
