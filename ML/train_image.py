#importing necessary libraries
import numpy as np
import cv2
from PIL import Image
import os
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.callbacks import ModelCheckpoint
from sklearn.utils.class_weight import compute_class_weight
import tensorflow as tf
from tensorflow.keras.layers import Flatten,Dropout, Dense,Convolution2D,MaxPooling2D,GlobalMaxPooling2D
from tensorflow.keras.models import Sequential
from tensorflow.keras.applications import InceptionV3
from tensorflow.keras.applications import VGG16

import seaborn as sns
from matplotlib import pyplot as plt





#Initiliaze path to the training & testing folder
train_dir='Image_Dataset/Train'
test_dir='Image_Dataset/Test'


###Generating images for Training set
train_datagen=ImageDataGenerator(rescale=1./255,
                                 rotation_range=40,
                                 width_shift_range=0.2,
                                 height_shift_range=0.2,
                                 shear_range=0.2,
                                 zoom_range=0.2,
                                 horizontal_flip=True,
                                 fill_mode='nearest')
###Generating images for Test set
test_datagen=ImageDataGenerator(rescale=1./255)

###Creating Training set
training_set=train_datagen.flow_from_directory(train_dir,
                                               target_size=(150,150),
                                               batch_size=10,
                                               class_mode='binary')
###Creating validation set
test_set=test_datagen.flow_from_directory(test_dir,
                                          target_size=(150,150),
                                          batch_size=10,
                                          class_mode="binary")



#shape printing (splitted dataset)
a,b=training_set.next()
print(a.shape)
print(b.shape)

c,d=test_set.next()
print(c.shape)
print(d.shape)


# #data balancing
# train=training_set.classes
# class_weights =compute_class_weight(class_weight='balanced',classes=np.unique(train),y=train)
# class_weights=dict(zip(np.unique(train),class_weights))
# print(class_weights)

#specifying image width and height
img_size = 150


def model3():
    input_shape=(img_size,img_size,3)

    base_cnn = VGG16( weights='imagenet', include_top=False, input_shape=input_shape)

    model = Sequential()
    model.add(base_cnn)
    # don't train existing weights
    for layer in base_cnn.layers:
        layer.trainable = False

    model.add(GlobalMaxPooling2D(name="gap"))
    model.add(Dense(256, activation="relu"))
    model.add(Dense(1, activation="sigmoid"))

    return model

    
# model=model2()
model=model3()

#printing model summary
print(model.summary())

#compiling the model
model.compile(loss='binary_crossentropy',
              optimizer='adam',
              metrics=['acc'])

#saving the model
checkpoint=ModelCheckpoint("Project_Saved_Models/image_model.h5",
                           monitor="val_acc",
                           save_best_only=True,
                           verbose=1)

Epoch=20
#training
history= model.fit_generator(training_set,
                   steps_per_epoch = training_set.__len__()/10,
                   epochs = Epoch,
                   validation_data = test_set,
                   validation_steps = test_set.__len__()/10,
                   callbacks=[checkpoint])


                   #class_weight=class_weights,

#plot accuracy and loss 
plt.plot(history.history['acc'])
plt.plot(history.history['val_acc'])
plt.title('model accuracy')
plt.ylabel('accuracy')
plt.xlabel('epoch')
plt.legend(['train', 'val'], loc='upper left')
plt.show()
#plt.savefig("Project_Extra/cnn_model_acc.png")

plt.plot(history.history['loss'])
plt.plot(history.history['val_loss'])
plt.title('model loss')
plt.ylabel('loss')
plt.xlabel('epoch')
plt.legend(['train', 'val'], loc='upper left')
plt.show()
#plt.savefig("Project_Extra/cnn_model_loss.png")