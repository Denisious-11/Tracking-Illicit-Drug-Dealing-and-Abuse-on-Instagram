from django.shortcuts import render
from .models import *
import json
from django.core import serializers
from django.http import HttpResponse, JsonResponse
from django.db.models import Q
from django.db.models import Count
import re
from django.views.decorators.cache import never_cache
from django.core.files.storage import FileSystemStorage
import random
from django.views.decorators.csrf import csrf_exempt
import os
from datetime import datetime
from datetime import date
import base64
import numpy as np
import cv2
from PIL import Image
from tensorflow.keras.models import load_model
import pickle
from nltk.stem.porter import PorterStemmer
import re
import nltk
from nltk.corpus import stopwords
from sklearn.feature_extraction.text import TfidfVectorizer




# Create your views here.

@never_cache
# Create your views here.
###############LOGIN START
# def display_login(request):
#     return render(request, "login.html", {})


# @never_cache
# def logout(request):
#     if 'uid' in request.session:
#         del request.session['uid']
#     return render(request,'login.html')

# def check_login(request):
# 	username = request.GET.get("uname")
# 	password = request.GET.get("password")

# 	print(username)
# 	print(password)

# 	if username == 'admin' and password == 'admin':
# 		request.session["uid"] = "admin"
# 		return HttpResponse("Admin Login Successful")
# 	else:
# 	    return HttpResponse("Invalid")

###############LOGIN END

# @never_cache
# ###############ADMIN START
# def show_home_admin(request):
# 	if 'uid' in request.session:
# 		print(request.session['uid'])
# 		return render(request,'home_admin.html') 
# 	else:
# 		return render(request,'login.html')

# @never_cache
# def view_users_admin(request):
# 	if 'uid' in request.session:
# 		users_list=Users.objects.all()

# 		return render(request,"view_users_admin.html",{'usr':users_list,})
# 	else:
# 		return render(request,'login.html')


# def delete(request):
# 	get_user_id=request.POST.get("u_id")
# 	print("Get user id : ",get_user_id)
# 	f = Users.objects.get(u_id=get_user_id)
# 	f.delete()
# 	return HttpResponse("<script>alert('Deleted Successfully');window.location.href='/view_users_admin/'</script>")

# @never_cache
# def view_drug_post_admin(request):
# 	if 'uid' in request.session:
# 		drug_list=Drug_Posts.objects.all()

# 		return render(request,"view_drug_post_admin.html",{'dl':drug_list})
# 	else:
# 		return render(request,'login.html')

# def take_action(request):
# 	p_id = request.POST.get("p_id")
# 	p_text=request.POST.get("p_text")
# 	p_image=request.POST.get("p_image")
# 	time=request.POST.get("time")
# 	date=request.POST.get("date")
# 	name=request.POST.get("name")
# 	username =request.POST.get("username")
# 	phone = request.POST.get("phone")
# 	email_id=request.POST.get("email_id")
# 	status=request.POST.get("status")
# 	encoded_image=request.POST.get("encoded_image")

# 	if(status=="Pending"):
# 		b = Police_DBs(p_id=p_id,p_text=p_text,p_image=p_image,time=time,date=date,name=name,username=username,phone=phone,email_id=email_id,encoded_image=encoded_image)
# 		b.save()

# 		c=Drug_Posts.objects.get(p_id=p_id)
# 		c.status="Action Taken"
# 		c.save()

# 		return HttpResponse("<script>alert('Action Taken Successfully');window.location.href='/view_drug_post_admin'</script>")
# 	else:
# 		return HttpResponse("<script>alert('Already Sent to Cops');window.location.href='/view_drug_post_admin'</script>")

# ######################################################################################################

@csrf_exempt
def register(request):
	name=request.POST.get("name")
	username=request.POST.get("username")
	phone=request.POST.get("phone")
	email_id=request.POST.get("email_id")
	password=request.POST.get("password")

	imagepath=request.POST.get("imagepath")
	print("imagepath ",imagepath)
	image=request.POST.get("image")

	final_path_image=os.path.basename(imagepath)
	print("final_path_image",final_path_image)

	base64_img_bytes = image.encode('utf-8')
	with open('dpd_app/static/user_images/'+final_path_image, 'wb') as file_to_save:
		decoded_image_data = base64.decodebytes(base64_img_bytes)
		file_to_save.write(decoded_image_data)

	print(name)
	print(username,phone,email_id,password)
	response_data={}
	try:
		d = Userss.objects.filter(username=username)
		c = d.count()
		if c == 1:
			response_data['msg'] = "Already registered"
		else:
		    ob=Userss(name=name,username=username,phone=phone,email_id=email_id,password=password,user_image=final_path_image,encoded_image=image)
		    ob.save()

		    response_data['msg'] = "yes"
	except:
	    response_data['msg'] = "no"
	return JsonResponse(response_data)

@csrf_exempt
def find_login(request):
	username=request.POST.get("username")
	password=request.POST.get("password")

	print(username,password)
	if(username=="police" and password=="police"):
	    
	    data={"msg":"Police"}
	 
	    return JsonResponse(data,safe=False)
	elif(username=="admin" and password=="admin"):

		data={"msg":"Admin"}
		return JsonResponse(data,safe=False)
	else:
	    try:
	        ob=Userss.objects.get(username=username,password=password)
	     
	        data={"msg":"User"}
	        return JsonResponse(data,safe=False)
	    except:
	        data={"msg":"no"}
	        return JsonResponse(data,safe=False)




def prediction_image(path):
	#load trained image model
	loaded_model = load_model("dpd_app/static/image_model.h5")
	width = 150
	height = 150

	image=cv2.imread(path)
	resize_image = cv2.resize(image,(height,width))
	out_image=np.expand_dims(resize_image,axis=0)/255
	# print(out_image.shape)

	my_pred = loaded_model.predict(out_image)
	my_pred = my_pred[0][0]
	print(my_pred)

	if my_pred <=0.5:
	    print("Non-Drug")
	    out="Non-Drug"
	    return out
	elif my_pred >0.5:
	    print("Drug")
	    out="Drug"
	    return out




def stemming(content):
	port_stem = PorterStemmer()
	review = re.sub('[^a-zA-Z]',' ',content)
	review = review.lower()
	review = review.split()
	review = [port_stem.stem(word) for word in review if not word in stopwords.words('english')]
	review = ' '.join(review)
	return review

def cleantext(text):
  x=str(text).lower().replace('\\','').replace('_','')
  tag=' '.join(re.sub("(@[A-Za-z0-9]+)|([^0-9A-Za-z \t])|(\w+:\/\/\S+)"," ",x).split())
  spcl=tag.replace('[^\w\s]','')
  return spcl

def prediction_text(val):
	#Loading trained model and vectorizer
	model=pickle.load(open( "dpd_app/static/svmmodel.pickle", "rb" ))
	tfidf=pickle.load(open( "dpd_app/static/svmvec.pickle", "rb" ))
	
	x=cleantext(val)
	# print("clean text==>",x)
	x=stemming(x)
	# print("stem text==>",x)
	vec=tfidf.transform([x])
	pred=model.predict(vec)
	# print(pred)
	pred=pred[0]
	print(pred)
	if(pred==1):
		a="Drug"
		print("Drug")
		return a
	else:
		a="Non-Drug"
		print("Non-Drug")
		return a


@csrf_exempt
def upload_post(request):
	
	p_text=request.POST.get("caption")
	print("p_text",p_text)
	username=request.POST.get("username")
	print("username",username)
	imagepath=request.POST.get("imagepath")
	# print("imagepath ",imagepath)
	image=request.POST.get("image")

	final_path_image=os.path.basename(imagepath)
	print("final_path_image",final_path_image)

	base64_img_bytes = image.encode('utf-8')
	with open('dpd_app/static/post_images/'+final_path_image, 'wb') as file_to_save:
		decoded_image_data = base64.decodebytes(base64_img_bytes)
		file_to_save.write(decoded_image_data)

	obj1=Userss.objects.get(username=username)

	name=obj1.name
	phone=obj1.phone
	email_id=obj1.email_id

	print("name",name)
	print("phone",phone)
	print("email_id",email_id)

	now = datetime.now()
	time = now.strftime("%H:%M:%S")
	print("Current Time =", time)

	today = date.today()
	current_date = today.strftime("%d/%m/%Y")
	print("date =",current_date)

	obj2=Posts(p_text=p_text,p_image=final_path_image,time=time,date=current_date,name=name,username=username,phone=phone,email_id=email_id,encoded_image=image)
	obj2.save()

	#drug post detection
	path_of_image='dpd_app/static/post_images/'+final_path_image
	output_image=prediction_image(path_of_image)

	output_text=prediction_text(p_text)

	if(output_image=="Drug" and output_text=="Drug"):
		status="Pending"
		obj3=Drug_Posts(p_text=p_text,p_image=final_path_image,time=time,date=current_date,name=name,username=username,phone=phone,email_id=email_id,status=status,encoded_image=image)
		obj3.save()


	data={"msg":"yes"}
	return JsonResponse(data,safe=False)

@csrf_exempt
def get_all_posts(request):
 
    resplist=[]
    respdata={}
    ob=Posts.objects.all().order_by('p_id').reverse()
    
    resplist=[]
    respdata={}
    for i in ob:
        data={}
        data["p_id"]=i.p_id
        data["p_text"]=i.p_text
        data["p_image"]=i.p_image
        data["time"]=i.time
        data["date"]=i.date
        data["name"]=i.name
        data["username"]=i.username
        data["phone"]=i.phone
        data["email_id"]=i.email_id
        data["encoded_image"]=i.encoded_image

  
        resplist.append(data)
    respdata["data"]=resplist
    print("respdata : ",respdata)
    return JsonResponse(respdata,safe=False)


@csrf_exempt
def get_my_posts(request):
	username1=request.POST.get("username")

	resplist=[]
	respdata={}
	ob=Posts.objects.filter(username=username1).order_by('p_id').reverse()
	for j in ob:
	    p_id=j.p_id
	    p_text=j.p_text
	    p_image=j.p_image
	    time=j.time
	    date=j.date
	    name=j.name
	    username=j.username
	    phone=j.phone
	    email_id=j.email_id
	    encoded_image=j.encoded_image


	    data={}
	    data["p_id"]=p_id
	    data["p_text"]=p_text
	    data["p_image"]=p_image
	    data["time"]=time
	    data["date"]=date
	    data["name"]=name
	    data["username"]=username
	    data["phone"]=phone
	    data["email_id"]=email_id
	    data["encoded_image"]=encoded_image

	    resplist.append(data)
	print(resplist)

	respdata["data"]=resplist
	print(respdata)
	return JsonResponse(respdata,safe=False)



@csrf_exempt
def view_drug_posts(request):
 
    resplist=[]
    respdata={}
    ob=Police_DBs.objects.all().order_by('p_id').reverse()
    
    resplist=[]
    respdata={}
    for i in ob:
        data={}
        data["p_id"]=i.p_id
        data["p_text"]=i.p_text
        data["p_image"]=i.p_image
        data["time"]=i.time
        data["date"]=i.date
        data["name"]=i.name
        data["username"]=i.username
        data["phone"]=i.phone
        data["email_id"]=i.email_id
        data["encoded_image"]=i.encoded_image

  
        resplist.append(data)
    respdata["data"]=resplist
    print("respdata : ",respdata)
    return JsonResponse(respdata,safe=False)

@csrf_exempt
def view_users(request):
 
    resplist=[]
    respdata={}
    ob=Userss.objects.all()
    
    resplist=[]
    respdata={}
    for i in ob:
        data={}
        data["u_id"]=i.u_id
        data["name"]=i.name
        data["username"]=i.username
        data["phone"]=i.phone
        data["email_id"]=i.email_id
  
        resplist.append(data)
    respdata["data"]=resplist
    print("respdata : ",respdata)
    return JsonResponse(respdata,safe=False)

@csrf_exempt
def delete(request):
	u_id=request.POST.get("u_id")
	print("u id :::::>>>>>>",u_id)

	response_data={}
	obj1=Userss.objects.get(u_id=int(u_id))
	obj1.delete()

	response_data["msg"]="yes"
	return JsonResponse(response_data,safe=False)


@csrf_exempt
def admin_view_drug_posts(request):
 
    resplist=[]
    respdata={}
    ob=Drug_Posts.objects.all().order_by('p_id').reverse()
    
    resplist=[]
    respdata={}
    for i in ob:
        data={}
        data["p_id"]=i.p_id
        data["p_text"]=i.p_text
        data["p_image"]=i.p_image
        data["time"]=i.time
        data["date"]=i.date
        data["name"]=i.name
        data["username"]=i.username
        data["phone"]=i.phone
        data["email_id"]=i.email_id
        data["status"]=i.status
        data["encoded_image"]=i.encoded_image

  
        resplist.append(data)
    respdata["data"]=resplist
    print("respdata : ",respdata)
    return JsonResponse(respdata,safe=False)



@csrf_exempt
def take_action(request):
	p_id=request.POST.get("p_id")
	p_text=request.POST.get("p_text")
	time=request.POST.get("time")
	date=request.POST.get("date")
	name=request.POST.get("name")
	username=request.POST.get("username")
	phone=request.POST.get("phone")
	email_id=request.POST.get("email_id")
	status=request.POST.get("status")
	encoded_image=request.POST.get("encoded_image")
	p_image=request.POST.get("p_image")

	response_data={}
	if(status=="Pending"):
		b = Police_DBs(p_id=p_id,p_text=p_text,p_image=p_image,time=time,date=date,name=name,username=username,phone=phone,email_id=email_id,encoded_image=encoded_image)
		b.save()

		c=Drug_Posts.objects.get(p_id=p_id)
		c.status="Action Taken"
		c.save()

		response_data['msg'] = "yes"
	else:
	    response_data['msg'] = "no"
	return JsonResponse(response_data)



@csrf_exempt
def get_my_reports(request):
	username1=request.POST.get("username")

	resplist=[]
	respdata={}
	ob=Police_DBs.objects.filter(username=username1).order_by('p_id').reverse()
	for j in ob:
	    p_id=j.p_id
	    p_text=j.p_text
	    p_image=j.p_image
	    encoded_image=j.encoded_image


	    data={}
	    data["p_id"]=p_id
	    data["p_text"]=p_text
	    data["p_image"]=p_image
	    data["encoded_image"]=encoded_image

	    resplist.append(data)
	print(resplist)

	respdata["data"]=resplist
	print(respdata)
	return JsonResponse(respdata,safe=False)


@csrf_exempt
def post_delete(request):
	p_id=request.POST.get("p_id")
	print("p_id :::::>>>>>>",p_id)
	caption=request.POST.get("caption")
	date=request.POST.get("date")
	time=request.POST.get("time")
	print(caption)
	print(date)
	print(time)

	response_data={}
	obj10=Police_DBs.objects.filter(p_text=caption,date=date,time=time)
	c = obj10.count()
	if c == 1:
		response_data["msg"]="action already taken"
		return JsonResponse(response_data,safe=False)

	else:
		obj1=Posts.objects.get(p_id=int(p_id))
		obj1.delete()

		obj2=Drug_Posts.objects.get(p_text=caption,date=date,time=time)
		obj2.delete()

		response_data["msg"]="yes"
		return JsonResponse(response_data,safe=False)


@csrf_exempt
def get_user_details(request):
 
	username=request.POST.get("username")
	resplist=[]
	respdata={}
	ob=[Userss.objects.get(username=username)]

	resplist=[]
	respdata={}
	for i in ob:
	    data={}
	    data["username"]=i.username
	    data["email_id"]=i.email_id
	    data["phone"]=i.phone
	    data["password"]=i.password
	    data["user_image"]=i.user_image
	    data["encoded_image"]=i.encoded_image

	    resplist.append(data)

	respdata["data"]=resplist
	print(respdata)
	return JsonResponse(respdata,safe=False)


@csrf_exempt
def update_user_details(request):
	username=request.POST.get("username")
	email_id=request.POST.get("email")
	phone=request.POST.get("phone")
	password=request.POST.get("password")
	print("username :::>>> ",username)


	response_data={}
	obj1=Userss.objects.get(username=username)
	obj1.email_id=email_id
	obj1.phone=phone
	obj1.password=password
	obj1.save()

	response_data['msg'] = "yes"

	return JsonResponse(response_data)
