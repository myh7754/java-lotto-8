package lotto;

import lotto.controller.LottoController;
import lotto.service.LottoInputParser;
import lotto.service.LottoInputValidation;
import lotto.service.LottoService;
import lotto.view.InputView;
import lotto.view.OutputView;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        LottoController controller = new LottoController(
                new LottoService(),
                new LottoInputParser(),
                new LottoInputValidation(),
                new InputView(),
                new OutputView()
        );
        controller.run();
    }
}
