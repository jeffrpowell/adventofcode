package com.jeffrpowell.adventofcode.aoc2023;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.algorithms.CharArrayUtils;
import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.rule.Rule;


public class Day7 extends Solution2023<Rule>{
    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public InputParser<Rule> getInputParser() {
        return InputParserFactory.getRuleParser("\n", Pattern.compile("^(.....) (\\d+$)"));
    }

    @Override
    protected String part1(List<Rule> input) {
        List<Hand> hands = input.stream()
            .map(r -> new Hand(r.getString(0), r.getInt(1)))
            .sorted(PLAIN_HAND_COMPARATOR.reversed())
            .collect(Collectors.toList());
        long total = 0;
        for (int rank = 1; rank < hands.size() + 1; rank++) {
            total += hands.get(rank - 1).bid * rank;
        }
        return Long.toString(total);
    }

    @Override
    protected String part2(List<Rule> input) {
        List<Hand> hands = input.stream()
            .map(r -> new Hand(r.getString(0), r.getInt(1)))
            .sorted(JOKER_HAND_COMPARATOR.reversed())
            .collect(Collectors.toList());
        long total = 0;
        for (int rank = 1; rank < hands.size() + 1; rank++) {
            total += hands.get(rank - 1).bid * rank;
        }
        return Long.toString(total);
    }

    enum HandType {
        FIVE(m -> {
            return m.size() == 1 && m.values().stream().filter(n -> n == 5).count() == 1L;
        }), 
        FOUR(m -> {
            return m.size() == 2 && m.values().stream().filter(n -> n == 4).count() == 1L;
        }), 
        FULL(m -> {
            return m.size() == 2 && m.values().stream().filter(n -> n == 3).count() == 1L && m.values().stream().filter(n -> n == 2).count() == 1L;
        }), 
        THREE(m -> {
            return m.size() == 3 && m.values().stream().filter(n -> n == 3).count() == 1L;
        }), 
        TWO_PAIR(m -> {
            return m.size() == 3 && m.values().stream().filter(n -> n == 2).count() == 2L;
        }), 
        PAIR(m -> {
            return m.size() == 4 && m.values().stream().filter(n -> n == 2).count() == 1L;
        }), 
        HIGH(m -> {
            return m.size() == 5;
        });

        Predicate<Map<Character, Integer>> definitionPred;
        HandType(Predicate<Map<Character, Integer>> definitionPred) {
            this.definitionPred = definitionPred;
        }

        public static HandType findHandType(Map<Character, Integer> cardMap, boolean preProcessJokers) {
            if (preProcessJokers) {
                if (cardMap.containsKey('J')) {
                    int jokerCount = cardMap.remove('J');
                    if (jokerCount == 5) {
                        cardMap.put('A', 0);
                    }
                    Character bestCardCandidate = cardMap.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
                    cardMap.put(bestCardCandidate, cardMap.get(bestCardCandidate) + jokerCount);
                }
            }
            return Arrays.stream(HandType.values())
                .filter(handtype -> Boolean.TRUE.equals(handtype.definitionPred.test(cardMap)))
                .findFirst().orElse(HIGH);
        }
    }

    private static class Hand{
        final String cards;
        final int bid;
        final HandType handType;
        final HandType jokerHandType;
        public Hand(String cards, int bid) {
            this.cards = cards;
            this.bid = bid;
            Map<Character, Integer> cardCounts = CharArrayUtils.toStream(cards.toCharArray()).collect(Collectors.toMap(
                Function.identity(),
                c -> 1,
                (a, b) -> a + 1
            ));
            this.handType = HandType.findHandType(cardCounts, false);
            this.jokerHandType = HandType.findHandType(cardCounts, true);
        }
        
    }
    private static final List<Character> CARD_TYPE_ORDER = List.of(
        'A','K','Q','J','T','9','8','7','6','5','4','3','2'
    );
    private static final List<Character> JOKER_CARD_TYPE_ORDER = List.of(
        'A','K','Q','T','9','8','7','6','5','4','3','2','J'
    );
    private static final Comparator<Hand> JOKER_HAND_COMPARATOR = (h1, h2) -> {
        int typeCompare = Integer.compare(h1.jokerHandType.ordinal(), h2.jokerHandType.ordinal());
        if (typeCompare != 0) {
            return typeCompare;
        }
        for (int i = 0; i < 5; i++) {
            Character c1 = h1.cards.charAt(i);
            Character c2 = h2.cards.charAt(i);
            int cardCompare = Integer.compare(JOKER_CARD_TYPE_ORDER.indexOf(c1), JOKER_CARD_TYPE_ORDER.indexOf(c2));
            if (cardCompare != 0) {
                return cardCompare;
            }
        }
        return 0;
    };

    private static final Comparator<Hand> PLAIN_HAND_COMPARATOR = (h1, h2) -> {
        int typeCompare = Integer.compare(h1.handType.ordinal(), h2.handType.ordinal());
        if (typeCompare != 0) {
            return typeCompare;
        }
        for (int i = 0; i < 5; i++) {
            Character c1 = h1.cards.charAt(i);
            Character c2 = h2.cards.charAt(i);
            int cardCompare = Integer.compare(CARD_TYPE_ORDER.indexOf(c1), CARD_TYPE_ORDER.indexOf(c2));
            if (cardCompare != 0) {
                return cardCompare;
            }
        }
        return 0;
    };
}
