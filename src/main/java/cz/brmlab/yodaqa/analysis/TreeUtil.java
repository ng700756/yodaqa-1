package cz.brmlab.yodaqa.analysis;

import java.util.LinkedList;
import java.util.List;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.Constituent;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;

/** Collection of dependency tree annotation tools. For example for iterating
 * dependencies of a specific token through prepositions. */

public class TreeUtil {
	public static List<Token> getAllGoverned(JCas jcas, Constituent sentence, Token gov, String typeMatch) {
		List<Token> list = new LinkedList<Token>();
		for (Dependency d : JCasUtil.selectCovered(Dependency.class, sentence)) {
			if (d.getGovernor() != gov)
				continue;
			if (d.getDependencyType().equals("prep")) { // - of -
				list.addAll(getAllGoverned(jcas, sentence, d.getDependent(), typeMatch));
			} else if (d.getDependencyType().matches(typeMatch)) {
				list.add(d.getDependent());
			} // else det: "the" name, amod: "last" name, ...
		}
		return list;
	}
}
