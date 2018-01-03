# **Calculator using Handwriting Recognition**
This project was my final project for the Object Oriented Programming course.
The steps which the algorithm takes to show the result for an input image that contains a mathematical equation are described below.
### 1- Image Segmentation
The program segments the input image and extracts only the needed digits or operators for the calculation, then convert every digit or operator to a small image of 28x28 pixels, which will be the input for the **Neural Network.** 
The program accpects digits from **0-9**, and the valid operations are: **addition, subtraction, multiplication, division, power, and using parentheses.**
### 2- Classification
The extracted thumbnails from the first step of the algorithm are fed into a pre-trained **Neural Network** with only one hidden layer, the prediction of the Neural Network is a vector of sigmoid activations, each describes a dependant probability p(i) of the input image to belong the (i)th class.
### 3- Calculating the result
After classifiying every segmented image, we convert this classification to its corresponding digit or operator and concatincate it to an expression string.
Then we pass this string to a stack-based calculator to calculate its result.<br/>
Then wrapping all of this into a JavaFX application with a simple GUI.<br/>
---
A **documentation** will be posted for more details about the algorithm's steps, and how to use the ***NeuralNetwork, Training, and CrossValidation*** classes in other projects.
