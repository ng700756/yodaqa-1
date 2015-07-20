Text entailment in MovieQA
==========================

Text entailment in closed-domain QA should in theory have several
advantages over open-domain QA,both performance-wise and during analysis.
For example, we can anticipate certain kinds of questions and optimize accordingly.
Some questions also become an easy case of structured database search. 

TODO:
  * Literature: http://brenocon.com/watson_special_issue/08%20textual%20evidence%20gatherint.pdf

Recognizing Yes/No Questions
----------------------------

The first step would be to consistently recognize closed (aka yes/no) questions.
A closed question should (in fact, must) include one of the verbs “be”,
“do”, “have” or a modal verb. Since modal verbs include verbs such as
 “can”, “could”, “may”, “might”, “should”, “must”, “would” or “ought to” and 
questions containing these verbs (except “can” in some cases) are usually 
difficult to answer, I will disregard them.

Examples include:
* Did X play in Y?
* Was X in the movie Y? (we are asking for the same information using a different sentence)
* Did X play with Y in a movie? (these questions might not yet be answerable in our current state)
* Has X received an academy award?
* Is X still alive?
* Can X kill a bear with his bare hands?

Our pipeline really depends on the accuracy of our question analysis, as the wrong 
assumption would generate useless answers (e.g. Who was the first US president? - “Yes.”).
But I will assume that we always recognize the correct 
type of question for any grammatically correct input.

Evidence Gathering Strategies
-----------------------------

Afterwards, we must change our answer generation. Usually we output
several question candidates during the search and the final answers
with the scoring at the end. In the case of closed questions, we would 
ideally only output “yes” or “no”, with some information about the reasoning, 
such as the source.  Thus, we must gather evidence for either yes or no.

## Answering from Structured Knowledge Bases

Now we will try to (very) broadly show the solutions for the given question categories.
 We consider only freebase search, as the fulltext search would have different properties.
* **Questions about properties:** Has X received an academy award – 
	We would have to look for the “awards” property of X.
	We may use fbpath infrastructure to learn mapping from dataset to properties.
* **Questions about 2 entity relations:** Did X play in/direct/wrote screenplay for Y – 
	We will have to search a path from X to Y with the same/similar label as the verb. 
For example when we ask “Did Keanu Reeves direct the Matrix?”, the answer should be no.
	We may again use the fbpath infrastructure to learn mapping from dataset to property paths between given concepts.
* **Questions about 3 entity relations:** Did X play with Y in a movie? - 
	Questions of this kind have more 'concepts' and
	we must consider the graph structure of the database.
	We can construct sub-graph match patterns like the paths for 2-entity relation matching.

## Answering from Fulltext Knowledge Bases
	
As a baseline,
we may simply run a phrase search for X or Y respectively in concept articles of Y, X,
using log-number of occurences and the position of first occurence as a feature
(important relationships will be mentioned early), and co-occurence of the selection verb
as another feature.

Scoring
-------

At last, we should also reconsider the certainty scoring (what does it mean 
when we're 60% certain about “yes”?) and the sources. 
During normal search, we have multiple text passages and properties for any given question.
 In our simplified model, it would probably be only one source. 