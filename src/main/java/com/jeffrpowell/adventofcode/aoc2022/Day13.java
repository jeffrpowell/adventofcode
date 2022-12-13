package com.jeffrpowell.adventofcode.aoc2022;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jeffrpowell.adventofcode.inputparser.InputParser;
import com.jeffrpowell.adventofcode.inputparser.InputParserFactory;
import com.jeffrpowell.adventofcode.inputparser.section.Section;
import com.jeffrpowell.adventofcode.inputparser.section.SectionSplitStrategyFactory;

public class Day13 extends Solution2022<Section>{

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public InputParser<Section> getInputParser() {
        return InputParserFactory.getSectionParser(SectionSplitStrategyFactory.emptyLines(1, true));
    }

    @Override
    protected String part1(List<Section> input) {
        int answer = 0;
        for (int i = 0; i < input.size(); i++) {
            List<List<String>> pair = input.get(i).getInput(InputParserFactory.getTokenSVParser(""));
            Group a = Group.parseGroup(pair.get(0));
            Group b = Group.parseGroup(pair.get(1));
            if (a.compare(b) == Group.Response.TRUE) {
                answer += i + 1;
            }
        }
        return Integer.toString(answer);
    }

    @Override
    protected String part2(List<Section> input) {
        List<Group> groups = input.stream()
            .map(section -> section.getInput(InputParserFactory.getTokenSVParser("")))
            .flatMap(List::stream)
            .map(Group::parseGroup)
            .collect(Collectors.toList());
        Group delimiter1 = Group.parseGroup(List.of("[","[","2","]","]"));
        Group delimiter2 = Group.parseGroup(List.of("[","[","6","]","]"));
        groups.add(delimiter1);
        groups.add(delimiter2);
        groups.sort(Comparator.naturalOrder());
        return Integer.toString((groups.indexOf(delimiter1) + 1) * (groups.indexOf(delimiter2) + 1));
    }

    private static interface Value{
        public boolean isNum();
    }

    private static class Num implements Value {
        int num;

        public Num(int num) {
            this.num = num;
        }
        

        @Override
        public boolean isNum() {
            return true;
        }


        public Integer getNum() {
            return num;
        }

        @Override
        public String toString() {
            return Integer.toString(num);
        }
    }

    private static class Group implements Value, Comparable<Group> {
        enum Response {TRUE, FALSE, CONTINUE;}
        List<Value> values;

        public static Group parseGroup(List<String> s) {
            Deque<List<Value>> groupStack = new ArrayDeque<>();
            Iterator<String> i = s.iterator();
            i.next(); //empty initial [ char
            List<Value> context = new ArrayList<>();
            Optional<String> numberInProgress = Optional.empty();
            while (i.hasNext()) {
                String c = i.next();
                if (c.equals("[")) {
                    groupStack.push(context);
                    context = new ArrayList<>();
                }
                else if (c.equals("]")) {
                    if (numberInProgress.isPresent()) {
                        context.add(new Num(Integer.parseInt(numberInProgress.get())));
                        numberInProgress = Optional.empty();
                    }
                    Group innerGroup = new Group(context);
                    if (groupStack.isEmpty()) {
                        return innerGroup;
                    }
                    context = groupStack.pop();
                    context.add(innerGroup);
                }
                else if (c.equals(",")) {
                    if (numberInProgress.isPresent()) {
                        context.add(new Num(Integer.parseInt(numberInProgress.get())));
                        numberInProgress = Optional.empty();
                    }
                }
                else if (numberInProgress.isPresent()) {
                    numberInProgress = numberInProgress.map(num -> num + c);
                }
                else {
                    numberInProgress = Optional.of(c);
                }
            }
            return new Group(Collections.emptyList());
        }

        public Group(List<Value> values) {
            this.values = values;
        }

        public Response compare(Group o) {

            for (int i = 0; i < values.size(); i++) {
                Value oValue = o.getValue(i);
                if (oValue == null) {
                    return Response.FALSE; // right side ran out of items first
                }
                Value myValue = values.get(i);
                boolean oValIsNum = oValue.isNum();
                boolean myValIsNum = myValue.isNum();
                if (myValIsNum && oValIsNum) {
                    Num oNum = (Num) oValue;
                    Num myNum = (Num) myValue;
                    if (myNum.getNum() > oNum.getNum()) {
                        return Response.FALSE; // left side greater than right side
                    }
                    else if (myNum.getNum() < oNum.getNum()) {
                        return Response.TRUE; // left side less than right side; not equal so terminate
                    }
                }
                else if (!myValIsNum && !oValIsNum) {
                    Group oGroup = (Group) oValue;
                    Group myGroup = (Group) myValue;
                    Response r = myGroup.compare(oGroup);
                    if (r == Response.TRUE || r == Response.FALSE) {
                        return r;
                    }
                }
                else if (myValIsNum) {
                    Group oGroup = (Group) oValue;
                    Group myGroup = Group.parseGroup(List.of("[",((Num) myValue).getNum().toString(),"]"));
                    Response r = myGroup.compare(oGroup);
                    if (r == Response.TRUE || r == Response.FALSE) {
                        return r;
                    }
                }
                else {
                    Group myGroup = (Group) myValue;
                    Group oGroup = Group.parseGroup(List.of("[",((Num) oValue).getNum().toString(),"]"));
                    Response r = myGroup.compare(oGroup);
                    if (r == Response.TRUE || r == Response.FALSE) {
                        return r;
                    }
                }
            }
            if (values.size() == o.values.size()) {
                return Response.CONTINUE; 
            }
            else {
                return Response.TRUE; // left side ran out of items before right
            }
        }

        public Value getValue(int i) {
            if (values.size() <= i) {
                return null;
            }
            return values.get(i);
        }

        @Override
        public boolean isNum() {
            return false;
        }

        @Override
        public String toString() {
            return "[" + 
                values.stream().map(Object::toString).collect(Collectors.joining(","))
            + "]";
        }

        @Override
        public int compareTo(Group o) {
            return switch (compare(o)) {
                case FALSE -> 1;
                case TRUE -> -1;
                default -> 0;
            };
        }
    }

}
