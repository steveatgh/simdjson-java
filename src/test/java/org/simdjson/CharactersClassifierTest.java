package org.simdjson;

import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.simdjson.StringUtils.chunk0;
import static org.simdjson.StringUtils.chunk1;

public class CharactersClassifierTest {

    @Test
    public void classifiesOperators() {
        // given
        CharactersClassifier classifier = new CharactersClassifier();
        String str = "a{bc}1:2,3[efg]aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        // when
        JsonCharacterBlock block = classifier.classify(chunk0(str), chunk1(str));

        // then
        assertThat(block.op()).isEqualTo(0x4552);
        assertThat(block.whitespace()).isEqualTo(0);
    }

    @Test
    public void classifiesControlCharactersAsOperators() {
        // given
        CharactersClassifier classifier = new CharactersClassifier();
        String str = new String(new byte[] {
                'a', 'a', 'a', 0x1a, 'a', 0x0c, 'a', 'a', // 0x1a = <SUB>, 0x0c = <FF>
                'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
                'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
                'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
                'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
                'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
                'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
                'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a'
        }, UTF_8);

        // when
        JsonCharacterBlock block = classifier.classify(chunk0(str), chunk1(str));

        // then
        assertThat(block.op()).isEqualTo(0x28);
        assertThat(block.whitespace()).isEqualTo(0);
    }

    @Test
    public void classifiesWhitespaces() {
        // given
        CharactersClassifier classifier = new CharactersClassifier();
        String str = "a bc\t1\n2\r3efgaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        // when
        JsonCharacterBlock block = classifier.classify(chunk0(str), chunk1(str));

        // then
        assertThat(block.whitespace()).isEqualTo(0x152);
        assertThat(block.op()).isEqualTo(0);
    }
}
