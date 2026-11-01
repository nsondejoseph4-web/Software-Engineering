import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer, TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

# The Corpus from the assignment
corpus = [
    'chocolates are a type of sweet. I like sweet chocolates.', # doc1
    'I like sweet apples.',                                     # doc2
    'chocolates are sweet.'                                     # doc3
]
doc_names = ['doc1', 'doc2', 'doc3']

print("--- Part A: Distinct Feature Words (Excluding Stop Words) ---")
count_vect = CountVectorizer(stop_words='english')
count_matrix = count_vect.fit_transform(corpus)
feature_names = count_vect.get_feature_names_out()
print(list(feature_names))
print()

print("--- Part B: Term-Document Frequency Count Matrix ---")
df_count = pd.DataFrame(count_matrix.toarray(), columns=feature_names, index=doc_names)
print(df_count)
print()

print("--- Part C: Term-Document TF-IDF Weights Matrix ---")
tfidf_vect = TfidfVectorizer(stop_words='english')
tfidf_matrix = tfidf_vect.fit_transform(corpus)
df_tfidf = pd.DataFrame(tfidf_matrix.toarray(), columns=feature_names, index=doc_names)
print(df_tfidf.round(4))
print()

print("--- Part D: Cosine Similarity Matrix ---")
similarity = cosine_similarity(tfidf_matrix)
df_sim = pd.DataFrame(similarity, columns=doc_names, index=doc_names)
print(df_sim.round(4))
