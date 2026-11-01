import numpy as np
import pandas as pd
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import accuracy_score

# 1. Define the 5-document corpus with labels
corpus = [
    "great fun movie",        # doc1 (pos)
    "great acting great plot", # doc2 (pos)
    "boring plot flat movie",  # doc3 (neg)
    "boring acting",          # doc4 (neg)
    "fun acting"              # doc5 (pos)
]
labels = ["pos", "pos", "neg", "neg", "pos"]
doc_names = ['doc1', 'doc2', 'doc3', 'doc4', 'doc5']

print("=== Part A: Unique Feature Words & Sorted Index ===")
vectorizer = CountVectorizer()
X_counts = vectorizer.fit_transform(corpus)
feature_names = vectorizer.get_feature_names_out()

# Display sorted index mapping
for index, word in enumerate(feature_names):
    print(f"Index {index}: {word}")
print()


print("=== Part B: Numeric Term-Document Matrix (Frequencies) ===")
df_counts = pd.DataFrame(X_counts.toarray(), columns=feature_names, index=doc_names)
print(df_counts)
print()


print("=== Part C & D: 80% Train, 20% Test Split & Evaluation ===")
# Split data: 80% train (4 docs), 20% test (1 doc)
# random_state ensures reproducible splits
X_train_80, X_test_20, y_train_80, y_test_20 = train_test_split(
    X_counts, labels, test_size=0.20, random_state=42
)

# Train the Naïve Bayes Model
model_80 = MultinomialNB()
model_80.fit(X_train_80, y_train_80)

# Evaluate
y_pred_20 = model_80.predict(X_test_20)
accuracy_80 = accuracy_score(y_test_20, y_pred_20)
print(f"Documents used for Training: {X_train_80.shape[0]}")
print(f"Documents used for Testing: {X_test_20.shape[0]}")
print(f"Model Accuracy (80% Train / 20% Test): {accuracy_80 * 100}%")
print()


print("=== Part E: Reverse Split (20% Train, 80% Test Split) ===")
# Split data: 20% train (1 doc), 80% test (4 docs)
X_train_20, X_test_80, y_train_20, y_test_80 = train_test_split(
    X_counts, labels, test_size=0.80, random_state=42
)

# Train a brand new model on the tiny training dataset
model_20 = MultinomialNB()
model_20.fit(X_train_20, y_train_20)

# Evaluate
y_pred_80 = model_20.predict(X_test_80)
accuracy_20 = accuracy_score(y_test_80, y_pred_80)
print(f"Documents used for Training: {X_train_20.shape[0]}")
print(f"Documents used for Testing: {X_test_80.shape[0]}")
print(f"Model Accuracy (20% Train / 80% Test): {accuracy_20 * 100}%")
