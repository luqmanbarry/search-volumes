# AMAZON SEARCH VOLUMES DEMO

## Assumptions made

1 - Amazon API sometime attempts to match a typo keyword to the nearest grammatically correct matching word

2 - As per assignment hint, the order of the returned keywords is nearly irrelevant

3 - A keyword cannot go beyond 100

4 - A sufficiently large bucket of words containing words returned by the API may be able to sample the API **Candidate-Set**

## How does the Algorithm works?

To implement this algorithm, I followed the following steps:

1 - Extract the **keyword** chars into a collection of chars

2 - Create a bucket containing all possible word permutations matching the **keyword** size 

3 - Make a call to the autocomplete API for each word permutation and store all returned data to a single bucket I would call ApiResult-Bucket

4 - Compute the number of strings matching or prefixed by the **keyword**; I will call it **keyword_match_count**

5 - Compute the score sing the formula: `keyworkd_score = keyword_match_count || keyword_match_count % 100`

6 - Return the result

**Note**: The **%** stands for modulo

## Is the provided hint correct?

No I do no think so because for better search result accuracy on the amazon side, they would want to sort the result-set.

Meaning that the best possible suggestions are closer to the user's fingertips. It is human nature to want to peek

the top items over the remaining ones from a list of choices.

## How precise is the outcome?

I think real is statistically more significant. However, this method samples only holds certain features of the amazon API data set.

Since the sample data set does not wholly reflect the API data; in my humble opinion the result can only be so much accurate. 