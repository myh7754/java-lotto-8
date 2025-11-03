package lotto.service;

import java.util.Arrays;
import java.util.List;

public class LottoInputParser {
    public List<String> parseLottoNumbers(String input) {
        List<String> parseNumbers = Arrays.stream(input.split(",", -1))
                .map(String::trim)
                .toList();
        return parseNumbers;
    }

    public List<Integer> convertToIntegerList(List<String> input) {
        return input.stream()
                .map(Integer::parseInt)
                .toList();
    }

    public int parseToInt(String input) {
        return Integer.parseInt(input);
    }
}
