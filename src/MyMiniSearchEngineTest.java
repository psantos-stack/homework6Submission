import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MyMiniSearchEngineTest {
    private List<String> documents() {
        return new ArrayList<String>(
                Arrays.asList(
                        "hello world",
                        "hello",
                        "world",
                        "world world hello",
                        "seattle rains hello abc world",
                        "sunday hello world fun"));
    }

    @Test
    public void testOneWord() {
        MyMiniSearchEngine engine = new MyMiniSearchEngine(documents());
        List<Integer> result = engine.search("seattle");
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(4), result.get(0));
        result = engine.search("hello");
        List<Integer> expected = new LinkedList<>();
        for(int i = 0; i < documents().size(); i = (i==1)? i+2:i+1){
            expected.add(i);
        }
        assertEquals(5, result.size());
        assertEquals(expected, result);
    }

    @Test
    public void testTwoWord() {
        MyMiniSearchEngine engine = new MyMiniSearchEngine(documents());
        List<Integer> result = engine.search("hello world");
        assertEquals(2, result.size());
        List<Integer> expected = new LinkedList<>();
        expected.add(0);
        expected.add(5);
        assertEquals(expected, result);
        List<Integer> result2 = engine.search("world hello");
        assertEquals(1, result2.size());
        List<Integer> expected2 = new LinkedList<>();
        expected2.add(3);
        assertEquals(expected2, result2);
    }

    @Test
    public void testThreeWord() {
        MyMiniSearchEngine engine = new MyMiniSearchEngine(documents());

        String[] inputs = {
                "rains hello abc",
                "rains Hello abc",
                "Rains Hello Abc",
                "raINS HELLo aBC",
        };
        List<Integer> expected = new LinkedList<>();
        expected.add(4);
        for (String input : inputs) {
            List<Integer> result = engine.search(input);
            assertEquals(1, result.size());
            assertEquals(expected, result);
        }
    }

    @Test
    public void testFourWord() {
        MyMiniSearchEngine engine = new MyMiniSearchEngine(documents());
        String[] inputs = {
                "sunday hello world fun",
                "SUNDAY HELLO WORLD FUN",
                "seattle rains hello ABC"
        };
        List<Integer> expected = new LinkedList<>();
        expected.add(5);
        for (int i = 0; i < inputs.length; i++) {
            List<Integer> result = engine.search(inputs[i]);
            assertEquals(1, result.size());
            if(i == inputs.length-1){
                expected.clear();
                expected.add(4);
            }
            assertEquals(expected, result);
        }
    }

    @Test
    public void testWordNotFound() {
        MyMiniSearchEngine engine = new MyMiniSearchEngine(documents());
        String[] inputs = {
                "llo wo",
                "day hell",
                "seattle rains hell",
                "money"
        };
        List<Integer> expected = new LinkedList<>();
        for (String input : inputs) {
            List<Integer> result = engine.search(input);
            assertEquals(0, result.size());
            assertEquals(expected, result);
        }
    }
}