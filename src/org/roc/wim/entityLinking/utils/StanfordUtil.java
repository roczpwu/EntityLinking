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

    public static List<String> Tokenize(String setence) {
        // This option shows loading and using an explicit tokenizer
        if (StringUtil.isEmpty(setence))
            return new ArrayList<String>();
        TokenizerFactory<CoreLabel> tokenizerFactory =
                PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(setence));
        List<CoreLabel> rawWords2 = tok.tokenize();
        List<String> token_list = new ArrayList<String>();
        for (CoreLabel coreLabel : rawWords2)
            token_list.add(coreLabel.toString());
        return token_list;
    }
}
