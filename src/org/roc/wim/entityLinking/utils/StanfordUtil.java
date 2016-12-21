package org.roc.wim.entityLinking.utils;

import com.roc.core.Utils.StringUtil;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: rocwu
 * Date: 15-12-23
 * Time: 下午9:11
 * Desc: Stanford Tools
 */
public class StanfordUtil {

    public static String[] Tokenize(String setence) {
        // This option shows loading and using an explicit tokenizer
        if (StringUtil.isEmpty(setence))
            return new String[0];
        TokenizerFactory<CoreLabel> tokenizerFactory =
                PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(setence));
        List<CoreLabel> rawWords2 = tok.tokenize();
        String[] tokens = new String[rawWords2.size()];
        for (int i = 0; i < rawWords2.size(); i++) {
            tokens[i] = rawWords2.get(i).toString();
        }
        return tokens;
    }
}
