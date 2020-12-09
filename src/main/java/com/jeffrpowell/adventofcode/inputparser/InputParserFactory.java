package com.jeffrpowell.adventofcode.inputparser;

import com.jeffrpowell.adventofcode.inputparser.rule.Rule;
import com.jeffrpowell.adventofcode.inputparser.rule.RuleParser;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class InputParserFactory
{
	private static final InputParser<Integer> INTEGER_PARSER = new IntegerParser();
	private static final InputParser<Long> LONG_PARSER = new LongParser();
	private static final InputParser<String> STRING_PARSER = new StringParser();
	private static final InputParser<List<Integer>> INT_CSV_PARSER = new IntegerCSVParser();
	private static final InputParser<List<String>> CSV_PARSER = new TokenSVParser(",");
	private static final InputParser<List<BigInteger>> BIG_INT_CSV_PARSER = new BigIntegerCSVParser();
	
	private InputParserFactory() {}
	
	public static InputParser<Integer> getIntegerParser() {
		return INTEGER_PARSER;
	}
    
	public static InputParser<Long> getLongParser() {
		return LONG_PARSER;
	}
	
	public static InputParser<String> getStringParser() {
		return STRING_PARSER;
	}
	
	public static InputParser<List<Integer>> getIntegerCSVParser() {
		return INT_CSV_PARSER;
	}
	
	public static InputParser<List<String>> getCSVParser() {
		return CSV_PARSER;
	}
	
	public static InputParser<List<BigInteger>> getBigIntegerCSVParser() {
		return BIG_INT_CSV_PARSER;
	}
	
	public static InputParser<List<String>> getTokenSVParser(String delimiter) {
		return new TokenSVParser(delimiter);
	}
	
	public static InputParser<List<Integer>> getIntegerTokenSVParser(String delimiter) {
		return new IntegerTokenSVParser(delimiter);
	}
    
    public static InputParser<Rule> getRuleParser(String ruleDelimiter, Pattern singleRegexPattern) {
        return new RuleParser(ruleDelimiter, Map.of("pattern", singleRegexPattern));
    }
    
    public static InputParser<Rule> getRuleParser(String ruleDelimiter, Map<String, Pattern> namedRegexPatterns) {
        return new RuleParser(ruleDelimiter, namedRegexPatterns);
    }
}
