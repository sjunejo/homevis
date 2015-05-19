# Final Year BSc Project
## Sadruddin Junejo

This repo hosts the code I've written (plus open-source libraries that I used) for my final year BSc project at King's College, University of London, entitled: "Investigation of Various Classification Techniques for Speech Recognition Using a Smartphone as the Interface".

The code containing in this repo comprises of three individual apps:

1. A *speech recording app*. This is an Android app that records one-second video clips of speech into the smartphone mic, and was used to gather data to train the *supervised learning models* (i.e. the speech classifiers) that I later built.

2. A *speech classification app*. This is another Android app with a UI that I intended to resemble a home automation system. Essentially, the user is meant to speak commands into the smartphone and the app UI responds accordingly, depending on the word that it has detected.
Four classifiers were used in this project: an artificial neural network (utilising the backpropagation algorithm, in this case), the k-Nearest Neighbours algorithm, a naive bayes classifier and a support vector machine.

3. A Pebble smartwatch 'watchface' that displays the current state of the simulated home automation system, essentially functioning as an extension to the Android UI in the speech classification app.


