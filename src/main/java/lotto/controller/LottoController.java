package lotto.controller;

import lotto.domain.Lotto;
import lotto.domain.Rank;
import lotto.service.LottoInputParser;
import lotto.service.LottoInputValidation;
import lotto.service.LottoService;
import lotto.view.InputView;
import lotto.view.OutputView;

import java.util.List;
import java.util.Map;

public class LottoController {
    private LottoService lottoService;
    private LottoInputParser parser;
    private LottoInputValidation validator;
    private InputView inputView;
    private OutputView outputView;

    public LottoController(LottoService lottoService, LottoInputParser parser, LottoInputValidation validator, InputView inputView, OutputView outputView) {
        this.lottoService = lottoService;
        this.parser = parser;
        this.validator = validator;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        int purchaseAmount = readPurchaseAmount();
        int lottoCount = lottoService.calculateLottoCount(purchaseAmount);

        outputView.printNumberOfPurchases(lottoCount);
        List<Lotto> lottos = lottoService.generateLottos(lottoCount);
        outputView.printPurchaseLottoNumbers(lottos);

        List<Integer> winningNumbers = readLottoNumber();
        int bonusNumber = readBonusNumber(winningNumbers);

        Map<Rank, Integer> results = lottoService.calculateLottoResults(lottos, winningNumbers, bonusNumber);
        double rateOfReturn = lottoService.calculateRateOfReturn(results, purchaseAmount);

        outputView.printResultMessage(results);
        outputView.printRateOfReturn(rateOfReturn);
    }

    private int readPurchaseAmount() {
        while (true) {
            String readPurchaseAmount = inputView.readPurchaseAmount();
            validator.validateNumberFormat(readPurchaseAmount);
            int purchaseAmount = parser.parseToInt(readPurchaseAmount);
            validator.validatePurchaseAmount(purchaseAmount);
            return purchaseAmount;
        }
    }

    private List<Integer> readLottoNumber() {
        while (true) {
            String winningNumbers = inputView.readWinningNumbers();
            List<String> lottoNumbers = parser.parseLottoNumbers(winningNumbers);
            validator.validateSize(lottoNumbers);
            validator.validateDuplicate(lottoNumbers);
            for (String lottoNumber : lottoNumbers) {
                validator.validateNumberFormat(lottoNumber);
                int parsedLottoNumber = parser.parseToInt(lottoNumber);
                validator.validateNumberRange(parsedLottoNumber);
            }
            return parser.convertToIntegerList(lottoNumbers);
        }
    }

    private int readBonusNumber(List<Integer> lottoNumbers) {
        while (true) {
            String readBonusNumber = inputView.readBonusNumber();
            validator.validateNumberFormat(readBonusNumber);
            int parsedBonusNumber = parser.parseToInt(readBonusNumber);
            validator.validateNumberRange(parsedBonusNumber);
            validator.validateBonusNotInLotto(lottoNumbers,parsedBonusNumber);
            return parsedBonusNumber;
        }
    }
}
